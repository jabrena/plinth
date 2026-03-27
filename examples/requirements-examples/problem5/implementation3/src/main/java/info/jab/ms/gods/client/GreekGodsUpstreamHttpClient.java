package info.jab.ms.gods.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.context.annotation.Value;
import jakarta.inject.Singleton;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

/**
 * Blocking upstream fetch using JDK {@link HttpClient} (no Micronaut Netty/reactive HTTP client).
 */
@Singleton
public final class GreekGodsUpstreamHttpClient {

    private static final TypeReference<List<String>> STRING_LIST = new TypeReference<>() {};

    private final HttpClient httpClient;
    private final URI greekUri;
    private final Duration readTimeout;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public GreekGodsUpstreamHttpClient(
            @Value("${greek-gods.upstream.base-url}") String baseUrl,
            @Value("${greek-gods.upstream.connect-timeout:5s}") Duration connectTimeout,
            @Value("${greek-gods.upstream.read-timeout:10s}") Duration readTimeout) {
        this.readTimeout = readTimeout;
        this.httpClient = HttpClient.newBuilder().connectTimeout(connectTimeout).build();
        String normalized = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
        this.greekUri = URI.create(normalized + "/greek");
    }

    /**
     * GET {@code {base-url}/greek} and parse JSON array of strings.
     *
     * @throws IOException on I/O, parse errors, or non-2xx status
     * @throws InterruptedException if the wait is interrupted
     */
    public List<String> fetchGreekNames() throws IOException, InterruptedException {
        HttpRequest request =
                HttpRequest.newBuilder().uri(greekUri).timeout(readTimeout).GET().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() / 100 != 2) {
            throw new IOException("upstream HTTP status=" + response.statusCode());
        }
        return objectMapper.readValue(response.body(), STRING_LIST);
    }
}
