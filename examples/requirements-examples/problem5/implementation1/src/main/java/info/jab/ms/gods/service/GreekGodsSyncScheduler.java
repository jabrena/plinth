package info.jab.ms.gods.service;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "greek-gods.sync", name = "enabled", havingValue = "true", matchIfMissing = true)
public class GreekGodsSyncScheduler {

	private final GreekGodsSyncService syncService;

	public GreekGodsSyncScheduler(GreekGodsSyncService syncService) {
		this.syncService = syncService;
	}

	@Scheduled(
			initialDelayString = "${greek-gods.sync.initial-delay-ms:60000}",
			fixedDelayString = "${greek-gods.sync.fixed-delay-ms:3600000}")
	public void runScheduledSync() {
		syncService.syncFromUpstream();
	}
}
