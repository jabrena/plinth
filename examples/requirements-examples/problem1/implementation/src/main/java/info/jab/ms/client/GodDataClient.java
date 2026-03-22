package info.jab.ms.client;

import info.jab.ms.config.GodApiProperties;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class GodDataClient {

    private static final Logger logger = LoggerFactory.getLogger(GodDataClient.class);

    private final RestClient restClient;
    private final RetryRegistry retryRegistry;

    public GodDataClient(GodApiProperties properties, RetryRegistry retryRegistry) {
        this.retryRegistry = retryRegistry;
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofMillis(properties.getTimeout().getConnect()));
        factory.setReadTimeout(Duration.ofMillis(properties.getTimeout().getRead()));
        this.restClient = RestClient.builder()
                .requestFactory(factory)
                .build();
    }

    /**
     * Fetch god names from the given URL, applying per-source retry policy.
     * Returns empty list if all retry attempts are exhausted (partial result behavior).
     */
    public List<String> fetchGodNames(String url, String sourceName) {
        Retry retry = retryRegistry.retry(sourceName);
        retry.getEventPublisher()
                .onRetry(e -> logger.warn("Retry attempt #{} for source '{}': {}",
                        e.getNumberOfRetryAttempts(), sourceName, e.getLastThrowable().getMessage()))
                .onSuccess(e -> {
                    if (e.getNumberOfRetryAttempts() > 0) {
                        logger.info("Source '{}' succeeded after {} retry attempts", sourceName, e.getNumberOfRetryAttempts());
                    }
                })
                .onError(e -> logger.error("Source '{}' failed after all {} attempts",
                        sourceName, e.getNumberOfRetryAttempts()));
        try {
            return Retry.decorateCallable(retry, () -> doHttpFetch(url)).call();
        } catch (Exception finalException) {
            logger.warn("All {} retry attempts exhausted for source '{}' at {}: {}",
                    retry.getRetryConfig().getMaxAttempts(), sourceName, url, finalException.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Performs the actual HTTP call. Throws on any error so Resilience4j Retry can intercept.
     */
    private List<String> doHttpFetch(String url) {
        logger.info("Fetching god names from: {}", url);
        String[] names = restClient.get()
                .uri(url)
                .retrieve()
                .body(String[].class);
        return (names != null) ? Arrays.asList(names) : Collections.emptyList();
    }
}
