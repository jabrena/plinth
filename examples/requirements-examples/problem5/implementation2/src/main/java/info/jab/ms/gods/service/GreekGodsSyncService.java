package info.jab.ms.gods.service;

import info.jab.ms.gods.repository.GreekGodRepository;
import info.jab.ms.gods.repository.GreekGodsDataAccessException;
import io.quarkus.arc.Unremovable;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

@ApplicationScoped
@Unremovable
public class GreekGodsSyncService {

    private static final Logger LOG = Logger.getLogger(GreekGodsSyncService.class);

    @Inject
    @RestClient
    GreekGodsUpstreamClient upstreamClient;

    @Inject
    GreekGodRepository repository;

    public void syncFromUpstream() {
        try {
            List<String> names = upstreamClient.fetchGreek();
            if (names == null || names.isEmpty()) {
                LOG.info("Upstream returned no Greek god names; repository unchanged");
                return;
            }
            int attempted = 0;
            for (String raw : names) {
                if (raw == null || raw.isBlank()) {
                    continue;
                }
                attempted++;
                try {
                    repository.upsertByName(raw);
                } catch (GreekGodsDataAccessException ex) {
                    LOG.warn("Greek gods sync upsert failed for name=" + raw.trim(), ex);
                }
            }
            LOG.debugf("Greek gods sync finished: upstream size=%d, non-blank attempted=%d", names.size(), attempted);
        } catch (Exception ex) {
            LOG.warnf("Greek gods upstream sync failed; will retry on next schedule tick: %s", ex.getMessage());
            LOG.debug("Greek gods sync failure detail", ex);
        }
    }
}
