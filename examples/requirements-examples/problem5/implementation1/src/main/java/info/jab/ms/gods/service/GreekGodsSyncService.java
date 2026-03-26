package info.jab.ms.gods.service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

@Service
public class GreekGodsSyncService {

	private static final Logger log = LoggerFactory.getLogger(GreekGodsSyncService.class);

	private static final String UPSERT =
			"""
			INSERT INTO greek_god (name) VALUES (:name)
			ON CONFLICT (name) DO NOTHING
			""";

	private final RestClient greekGodsSyncRestClient;

	private final JdbcClient jdbcClient;

	public GreekGodsSyncService(
			@Qualifier("greekGodsSync") RestClient greekGodsSyncRestClient, JdbcClient jdbcClient) {
		this.greekGodsSyncRestClient = greekGodsSyncRestClient;
		this.jdbcClient = jdbcClient;
	}

	@Transactional
	public void syncFromUpstream() {
		log.debug("Starting Greek gods upstream sync");
		try {
			List<String> names = greekGodsSyncRestClient
					.get()
					.uri("/greek")
					.retrieve()
					.body(new ParameterizedTypeReference<List<String>>() {});
			if (names == null || names.isEmpty()) {
				log.info("Upstream returned no Greek god names; repository unchanged");
				return;
			}
			int upserted = 0;
			for (String raw : names) {
				if (raw == null || raw.isBlank()) {
					continue;
				}
				String name = raw.trim();
				int updated = jdbcClient.sql(UPSERT).param("name", name).update();
				if (updated > 0) {
					upserted++;
				}
			}
			log.info(
					"Greek gods sync finished: upstream count={}, rows inserted (new)={}",
					names.size(),
					upserted);
		}
		catch (Exception ex) {
			log.warn("Greek gods upstream sync failed; will retry on next schedule tick: {}", ex.getMessage());
			log.debug("Greek gods sync failure detail", ex);
		}
	}
}
