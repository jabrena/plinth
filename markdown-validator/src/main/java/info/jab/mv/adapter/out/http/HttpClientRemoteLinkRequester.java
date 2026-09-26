package info.jab.mv.adapter.out.http;

import info.jab.mv.application.port.BlockedRemoteLinkException;
import info.jab.mv.application.port.RemoteLinkRequester;
import info.jab.mv.application.port.RemoteLinkResponse;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

public final class HttpClientRemoteLinkRequester implements RemoteLinkRequester {

    static final int MAX_REDIRECTS = 5;
    private static final Set<Integer> REDIRECT_STATUS_CODES = Set.of(301, 302, 303, 307, 308);

    private final HttpClient httpClient;
    private final Duration connectTimeout;
    private final Predicate<URI> destinationPolicy;

    public HttpClientRemoteLinkRequester(Duration connectTimeout) {
        this(connectTimeout, new PublicAddressPolicy());
    }

    HttpClientRemoteLinkRequester(Duration connectTimeout, Predicate<URI> destinationPolicy) {
        this.connectTimeout = connectTimeout;
        this.destinationPolicy = destinationPolicy;
        // Redirects are followed manually so every hop is checked against the destination policy.
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(connectTimeout)
                .followRedirects(HttpClient.Redirect.NEVER)
                .build();
    }

    @Override
    public RemoteLinkResponse request(URI uri, String method, Duration timeout) throws IOException, InterruptedException {
        URI currentUri = uri;
        String currentMethod = method;
        for (int redirects = 0; ; redirects++) {
            if (!isHttp(currentUri) || !destinationPolicy.test(currentUri)) {
                throw new BlockedRemoteLinkException(currentUri);
            }

            HttpResponse<Void> response = send(currentUri, currentMethod, timeout);
            int status = response.statusCode();
            Optional<String> location = response.headers().firstValue("Location");
            if (!REDIRECT_STATUS_CODES.contains(status) || location.isEmpty()) {
                return new RemoteLinkResponse(status);
            }
            if (redirects >= MAX_REDIRECTS) {
                throw new IOException("Too many redirects: " + uri);
            }

            currentUri = resolveRedirect(currentUri, location.get());
            if (status == 303 && !"HEAD".equals(currentMethod)) {
                currentMethod = "GET";
            }
        }
    }

    private HttpResponse<Void> send(URI uri, String method, Duration timeout) throws IOException, InterruptedException {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder(uri)
                .timeout(timeout)
                .header("User-Agent", "java-cursor-rules-markdown-validator")
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");

        if ("HEAD".equals(method)) {
            requestBuilder.method("HEAD", HttpRequest.BodyPublishers.noBody());
        } else {
            requestBuilder.GET();
        }

        return httpClient.send(requestBuilder.build(), HttpResponse.BodyHandlers.discarding());
    }

    private static URI resolveRedirect(URI current, String location) throws IOException {
        try {
            return current.resolve(location.strip());
        } catch (IllegalArgumentException e) {
            throw new IOException("Invalid redirect location: " + location, e);
        }
    }

    private static boolean isHttp(URI uri) {
        String scheme = uri.getScheme();
        return "http".equalsIgnoreCase(scheme) || "https".equalsIgnoreCase(scheme);
    }

    public Duration connectTimeout() {
        return connectTimeout;
    }
}
