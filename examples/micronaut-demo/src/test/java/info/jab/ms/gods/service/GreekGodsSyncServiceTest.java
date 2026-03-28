package info.jab.ms.gods.service;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import info.jab.ms.gods.repository.GreekGodRepository;
import info.jab.ms.gods.upstream.api.ExternalGreekGodsApi;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.http.client.exceptions.ReadTimeoutException;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GreekGodsSyncServiceTest {

    @Mock
    ExternalGreekGodsApi upstreamApi;

    @Mock
    GreekGodRepository repository;

    GreekGodsSyncService service;

    @BeforeEach
    void setUp() {
        service = new GreekGodsSyncService(upstreamApi, repository);
    }

    @Test
    void syncFromUpstream_upsertsTrimmedNames() {
        when(upstreamApi.getExternalGreekGods()).thenReturn(List.of("  Zeus  ", "Hera"));
        assertThatCode(() -> service.syncFromUpstream()).doesNotThrowAnyException();
        verify(repository).upsertByName("Zeus");
        verify(repository).upsertByName("Hera");
    }

    @Test
    void syncFromUpstream_skipsNullAndBlankNames() {
        when(upstreamApi.getExternalGreekGods()).thenReturn(Arrays.asList("Ares", null, "   "));
        service.syncFromUpstream();
        verify(repository, times(1)).upsertByName(anyString());
        verify(repository).upsertByName("Ares");
    }

    @Test
    void syncFromUpstream_whenEmptyList_doesNotUpsert() {
        when(upstreamApi.getExternalGreekGods()).thenReturn(List.of());
        service.syncFromUpstream();
        verify(repository, never()).upsertByName(anyString());
    }

    @Test
    void syncFromUpstream_whenNullList_doesNotUpsert() {
        when(upstreamApi.getExternalGreekGods()).thenReturn(null);
        service.syncFromUpstream();
        verify(repository, never()).upsertByName(anyString());
    }

    @Test
    void syncFromUpstream_onReadTimeout_doesNotUpsert() {
        when(upstreamApi.getExternalGreekGods()).thenThrow(ReadTimeoutException.TIMEOUT_EXCEPTION);
        service.syncFromUpstream();
        verify(repository, never()).upsertByName(anyString());
    }

    @Test
    void syncFromUpstream_onHttpClientError_doesNotUpsert() {
        HttpResponse<?> badResponse = HttpResponse.status(HttpStatus.BAD_REQUEST);
        when(upstreamApi.getExternalGreekGods()).thenThrow(new HttpClientResponseException("bad", badResponse));
        service.syncFromUpstream();
        verify(repository, never()).upsertByName(anyString());
    }

    @Test
    void syncFromUpstream_onUnexpectedException_doesNotUpsert() {
        when(upstreamApi.getExternalGreekGods()).thenThrow(new IllegalStateException("surprise"));
        assertThatCode(() -> service.syncFromUpstream()).doesNotThrowAnyException();
        verify(repository, never()).upsertByName(anyString());
    }

    @Test
    void syncFromUpstream_whenUpsertFails_continuesWithOtherNames() {
        when(upstreamApi.getExternalGreekGods()).thenReturn(List.of("One", "Two"));
        doThrow(new RuntimeException("db")).when(repository).upsertByName("One");
        assertThatCode(() -> service.syncFromUpstream()).doesNotThrowAnyException();
        verify(repository).upsertByName("One");
        verify(repository).upsertByName("Two");
    }
}

