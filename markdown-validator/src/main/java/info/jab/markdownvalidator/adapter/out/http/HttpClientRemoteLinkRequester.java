package info.jab.markdownvalidator.adapter.out.http;

import info.jab.markdownvalidator.application.port.RemoteLinkRequester;
import info.jab.markdownvalidator.application.port.RemoteLinkResponse;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public final class HttpClientRemoteLinkRequester implements RemoteLinkRequester {

    private final HttpClient httpClient;
    private final Duration connectTimeout;

    public HttpClientRemoteLinkRequester(Duration connectTimeout) {
        this.connectTimeout = connectTimeout;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(connectTimeout)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
    }

    @Override
    public RemoteLinkResponse request(URI uri, String method, Duration timeout) throws IOException, InterruptedException {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder(uri)
                .timeout(timeout)
                .header("User-Agent", "java-cursor-rules-markdown-validator")
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");

        if ("HEAD".equals(method)) {
            requestBuilder.method("HEAD", HttpRequest.BodyPublishers.noBody());
        } else {
            requestBuilder.GET();
        }

        HttpResponse<String> response = httpClient.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofString());
        return new RemoteLinkResponse(response.statusCode());
    }

    public Duration connectTimeout() {
        return connectTimeout;
    }
}
