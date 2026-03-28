package info.jab.ms.gods.service;

import info.jab.ms.gods.repository.GreekGodRepository;
import info.jab.ms.gods.upstream.api.ExternalGreekGodsApi;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.http.client.exceptions.ReadTimeoutException;
import jakarta.inject.Singleton;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public final class GreekGodsSyncService {

    private static final Logger LOG = LoggerFactory.getLogger(GreekGodsSyncService.class);

    private final ExternalGreekGodsApi upstreamApi;
    private final GreekGodRepository repository;

    public GreekGodsSyncService(ExternalGreekGodsApi upstreamApi, GreekGodRepository repository) {
        this.upstreamApi = upstreamApi;
        this.repository = repository;
    }

    /**
     * Fetches the upstream JSON array (aligned with {@code my-json-server-oas.yaml}, operation {@code
     * getExternalGreekGods}) and upserts each name. Logs and swallows client failures (no retry library in v1).
     */
    public void syncFromUpstream() {
        List<String> names;
        try {
            names = upstreamApi.getExternalGreekGods();
        } catch (ReadTimeoutException e) {
            LOG.warn(
                    "Greek gods upstream sync failed: event=upstream_timeout message={}",
                    e.getMessage());
            return;
        } catch (HttpClientResponseException e) {
            LOG.warn(
                    "Greek gods upstream sync failed: event=upstream_http status={} message={}",
                    e.getStatus(),
                    e.getMessage());
            return;
        } catch (Exception e) {
            LOG.warn(
                    "Greek gods upstream sync failed: event=upstream_unexpected message={}",
                    e.getMessage(),
                    e);
            return;
        }
        if (names == null || names.isEmpty()) {
            LOG.debug("Greek gods upstream returned no names; skipping upsert");
            return;
        }
        int written = 0;
        for (String name : names) {
            if (name == null || name.isBlank()) {
                continue;
            }
            try {
                repository.upsertByName(name.trim());
                written++;
            } catch (Exception e) {
                LOG.warn(
                        "Greek gods upsert failed: event=upsert_error nameLength={}",
                        name.length(),
                        e);
            }
        }
        LOG.info("Greek gods sync finished: upsertAttempts={} acceptedRows={}", names.size(), written);
    }
}
