package info.jab.ms.gods.service;

import info.jab.ms.gods.client.GreekGodsUpstreamHttpClient;
import info.jab.ms.gods.repository.GreekGodRepository;
import jakarta.inject.Singleton;
import java.io.IOException;
import java.net.http.HttpTimeoutException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public final class GreekGodsSyncService {

    private static final Logger LOG = LoggerFactory.getLogger(GreekGodsSyncService.class);

    private final GreekGodsUpstreamHttpClient upstreamClient;
    private final GreekGodRepository repository;

    public GreekGodsSyncService(GreekGodsUpstreamHttpClient upstreamClient, GreekGodRepository repository) {
        this.upstreamClient = upstreamClient;
        this.repository = repository;
    }

    /**
     * Fetches the upstream JSON array and upserts each name. Logs and swallows client failures (no retry library in v1).
     */
    public void syncFromUpstream() {
        List<String> names;
        try {
            names = upstreamClient.fetchGreekNames();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOG.warn("Greek gods upstream sync failed: event=upstream_interrupted");
            return;
        } catch (HttpTimeoutException e) {
            LOG.warn(
                    "Greek gods upstream sync failed: event=upstream_timeout message={}",
                    e.getMessage());
            return;
        } catch (IOException e) {
            LOG.warn(
                    "Greek gods upstream sync failed: event=upstream_io message={}",
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
