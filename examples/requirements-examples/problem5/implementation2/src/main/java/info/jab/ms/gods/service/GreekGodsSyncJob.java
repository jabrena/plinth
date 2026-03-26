package info.jab.ms.gods.service;

import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class GreekGodsSyncJob {

    @Inject
    GreekGodsSyncService syncService;

    @Scheduled(every = "${greek.gods.sync.every}", identity = "greek-gods-sync")
    void runSync() {
        syncService.syncFromUpstream();
    }
}
