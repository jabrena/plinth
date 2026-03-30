package info.jab.ms.client;

import info.jab.ms.config.GodOutboundProperties;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class GodDataClient {

	private static final Logger log = LoggerFactory.getLogger(GodDataClient.class);

	private static final ParameterizedTypeReference<List<String>> STRING_LIST =
			new ParameterizedTypeReference<>() {};

	private final RestClient restClient;
	private final GodOutboundProperties properties;

	public GodDataClient(
			@Qualifier("godRestClient") RestClient restClient, GodOutboundProperties properties) {
		this.restClient = restClient;
		this.properties = properties;
	}

	/**
	 * Single GET to the configured URL for the pantheon. On connect/read failure or non-2xx, returns
	 * an empty list (no retries).
	 */
	public List<String> fetchNames(String pantheonKey) {
		String url = urlFor(pantheonKey);
		if (url == null || url.isBlank()) {
			log.warn("god.outbound.fetch.skipped source={} reason=no_url", pantheonKey);
			return List.of();
		}
		log.debug("god.outbound.fetch.start source={} url={}", pantheonKey, url);
		try {
			List<String> names =
					restClient.get().uri(url).retrieve().body(STRING_LIST);
			if (names == null) {
				log.warn("god.outbound.fetch.empty_body source={}", pantheonKey);
				return List.of();
			}
			log.info(
					"god.outbound.fetch.ok source={} url={} nameCount={}",
					pantheonKey,
					url,
					names.size());
			return names;
		} catch (RestClientException ex) {
			log.warn(
					"god.outbound.fetch.failed source={} url={} error={}",
					pantheonKey,
					url,
					ex.toString());
			return List.of();
		}
	}

	private String urlFor(String pantheonKey) {
		if (properties.urls() == null) {
			return null;
		}
		return switch (pantheonKey) {
			case "greek" -> properties.urls().greek();
			case "roman" -> properties.urls().roman();
			case "nordic" -> properties.urls().nordic();
			default -> null;
		};
	}
}
