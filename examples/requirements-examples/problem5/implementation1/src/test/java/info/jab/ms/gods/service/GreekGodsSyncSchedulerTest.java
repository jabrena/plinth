package info.jab.ms.gods.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class GreekGodsSyncSchedulerTest {

	@Mock
	private GreekGodsSyncService syncService;

	@InjectMocks
	private GreekGodsSyncScheduler scheduler;

	@Test
	void runScheduledSync_delegatesToSyncService() {
		scheduler.runScheduledSync();

		verify(syncService).syncFromUpstream();
	}
}
