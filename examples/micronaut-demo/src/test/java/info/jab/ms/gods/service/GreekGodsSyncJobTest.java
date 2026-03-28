package info.jab.ms.gods.service;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GreekGodsSyncJobTest {

    @Mock
    GreekGodsSyncService syncService;

    @Test
    void runScheduledSync_whenSyncDisabled_doesNotCallService() throws Exception {
        var job = new GreekGodsSyncJob(syncService);
        var field = GreekGodsSyncJob.class.getDeclaredField("syncEnabled");
        field.setAccessible(true);
        field.set(job, false);
        job.runScheduledSync();
        verify(syncService, never()).syncFromUpstream();
    }

    @Test
    void runScheduledSync_whenSyncEnabled_callsService() throws Exception {
        var job = new GreekGodsSyncJob(syncService);
        var field = GreekGodsSyncJob.class.getDeclaredField("syncEnabled");
        field.setAccessible(true);
        field.set(job, true);
        job.runScheduledSync();
        verify(syncService).syncFromUpstream();
    }
}
