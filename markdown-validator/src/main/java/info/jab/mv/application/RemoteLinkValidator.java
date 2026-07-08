package info.jab.mv.application;

import info.jab.mv.application.port.RemoteLinkRequester;
import info.jab.mv.application.port.RemoteLinkResponse;
import info.jab.mv.domain.ValidationError;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpTimeoutException;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class RemoteLinkValidator {

    private final RemoteLinkRequester remoteLinkRequester;
    private final Duration timeout;
    private final ConcurrentMap<String, Optional<String>> remoteLinkCache = new ConcurrentHashMap<>();

    public RemoteLinkValidator(RemoteLinkRequester remoteLinkRequester, Duration timeout) {
        this.remoteLinkRequester = remoteLinkRequester;
        this.timeout = timeout;
    }

    public Optional<ValidationError> validate(Path file, String destination) {
        return parseRemoteUri(destination)
                .flatMap(uri -> remoteLinkCache.computeIfAbsent(uri.toString(), ignored -> checkRemoteLink(uri))
                        .map(message -> new ValidationError(file, 0, message)));
    }

    private Optional<URI> parseRemoteUri(String destination) {
        if (destination == null || destination.isBlank()) {
            return Optional.empty();
        }

        try {
            URI uri = new URI(destination.strip());
            String scheme = uri.getScheme();
            return "http".equalsIgnoreCase(scheme) || "https".equalsIgnoreCase(scheme)
                    ? Optional.of(uri)
                    : Optional.empty();
        } catch (URISyntaxException e) {
            return Optional.empty();
        }
    }

    private Optional<String> checkRemoteLink(URI uri) {
        URI requestUri = removeFragment(uri);

        try {
            RemoteLinkResponse response = remoteLinkRequester.request(requestUri, "HEAD", timeout);
            if (response.statusCode() == 405 || response.statusCode() == 403) {
                response = remoteLinkRequester.request(requestUri, "GET", timeout);
            }

            int status = response.statusCode();
            if (status >= 400) {
                return Optional.of("Remote link is not reachable: " + uri + " (HTTP " + status + ")");
            }

            return Optional.empty();
        } catch (HttpTimeoutException e) {
            return Optional.of("Remote link timed out after " + timeout.toSeconds() + " seconds: " + uri);
        } catch (IOException | InterruptedException | IllegalArgumentException e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            return Optional.of("Remote link check failed: " + uri + " (" + describeException(e) + ")");
        }
    }

    private static URI removeFragment(URI uri) {
        try {
            return new URI(uri.getScheme(), uri.getAuthority(), uri.getPath(), uri.getQuery(), null);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Unable to normalize URI: " + uri, e);
        }
    }

    private static String describeException(Exception exception) {
        String message = exception.getMessage();
        if (message == null || message.isBlank()) {
            return exception.getClass().getSimpleName();
        }
        return message;
    }
}
