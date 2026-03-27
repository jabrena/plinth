package info.jab.ms.gods.service;

import io.micronaut.context.annotation.Requires;
import io.micronaut.context.annotation.Value;
import io.micronaut.scheduling.annotation.Scheduled;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
@Requires(property = "greek-gods.sync.scheduler-enabled", notEquals = "false")
public final class GreekGodsSyncJob {

    private static final Logger LOG = LoggerFactory.getLogger(GreekGodsSyncJob.class);

    private final GreekGodsSyncService syncService;

    @Value("${greek-gods.sync.enabled:true}")
    private boolean syncEnabled;

    public GreekGodsSyncJob(GreekGodsSyncService syncService) {
        this.syncService = syncService;
    }

    @Scheduled(
            fixedDelay = "${greek-gods.sync.fixed-delay:5m}",
            initialDelay = "${greek-gods.sync.initial-delay:30s}")
    void runScheduledSync() {
        if (!syncEnabled) {
            LOG.trace("Greek gods scheduled sync skipped: feature disabled");
            return;
        }
        LOG.debug("Greek gods scheduled sync starting");
        syncService.syncFromUpstream();
    }
}
