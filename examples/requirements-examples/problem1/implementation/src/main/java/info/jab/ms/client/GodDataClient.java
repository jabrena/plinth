package info.jab.ms.client;

import info.jab.ms.config.GodApiProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;

@Component
public class GodDataClient {

    private static final Logger log = LoggerFactory.getLogger(GodDataClient.class);

    private final RestClient restClient;
    private final GodApiProperties properties;

    public GodDataClient(RestClient restClient, GodApiProperties properties) {
        this.restClient = restClient;
        this.properties = properties;
    }

    public List<String> fetchGreekGods() {
        return fetch(properties.sources().greek().url(), "greek");
    }

    public List<String> fetchRomanGods() {
        return fetch(properties.sources().roman().url(), "roman");
    }

    public List<String> fetchNordicGods() {
        return fetch(properties.sources().nordic().url(), "nordic");
    }

    private List<String> fetch(String url, String source) {
        try {
            List<String> result = restClient.get()
                .uri(url)
                .retrieve()
                .body(new ParameterizedTypeReference<List<String>>() {});
            log.debug("Fetched {} gods from {}: {} names", source, url, result != null ? result.size() : 0);
            return result != null ? result : List.of();
        } catch (RestClientException e) {
            log.warn("Failed to fetch gods from source {}: {}", source, e.getMessage());
            return List.of();
        }
    }
}
