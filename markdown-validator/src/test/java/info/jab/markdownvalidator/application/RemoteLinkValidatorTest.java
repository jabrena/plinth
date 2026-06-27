package info.jab.markdownvalidator.application;

import info.jab.markdownvalidator.application.port.RemoteLinkRequester;
import info.jab.markdownvalidator.application.port.RemoteLinkResponse;
import java.net.URI;
import java.net.http.HttpTimeoutException;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class RemoteLinkValidatorTest {

    private static final Path DOCUMENT = Path.of("guide.md");
    private static final Duration TIMEOUT = Duration.ofSeconds(10);

    @AfterEach
    void clearInterruptedFlag() {
        Thread.interrupted();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = { "   ", "./guide.md", "#section", "mailto:team@example.test", "not a uri" })
    void validate_ignoresNonRemoteDestinations(String destination) {
        CountingRequester requester = new CountingRequester(Map.of());
        RemoteLinkValidator validator = new RemoteLinkValidator(requester, TIMEOUT);

        Optional<?> result = validator.validate(DOCUMENT, destination);

        assertThat(result).isEmpty();
        assertThat(requester.requestedUris()).isEmpty();
    }

    @Test
    void validate_removesFragmentOnlyForHttpRequest() {
        CountingRequester requester = new CountingRequester(Map.of(URI.create("https://example.test/page"), 200));
        RemoteLinkValidator validator = new RemoteLinkValidator(requester, TIMEOUT);

        Optional<?> result = validator.validate(DOCUMENT, "https://example.test/page#intro");

        assertThat(result).isEmpty();
        assertThat(requester.requestedUris()).containsExactly(URI.create("https://example.test/page"));
    }

    @ParameterizedTest
    @ValueSource(ints = { 403, 405 })
    void validate_retriesWithGetWhenHeadIsNotAllowed(int headStatusCode) {
        SequencedRequester requester = new SequencedRequester(
                new RemoteCall(URI.create("https://example.test/page"), "HEAD", headStatusCode),
                new RemoteCall(URI.create("https://example.test/page"), "GET", 200));
        RemoteLinkValidator validator = new RemoteLinkValidator(requester, TIMEOUT);

        Optional<?> result = validator.validate(DOCUMENT, "https://example.test/page");

        assertThat(result).isEmpty();
        assertThat(requester.requestedMethods()).containsExactly("HEAD", "GET");
    }

    @Test
    void validate_reportsRedirectsAsFailures() {
        CountingRequester requester = new CountingRequester(Map.of(URI.create("https://example.test/moved"), 301));
        RemoteLinkValidator validator = new RemoteLinkValidator(requester, TIMEOUT);

        Optional<?> result = validator.validate(DOCUMENT, "https://example.test/moved");

        assertThat(result).isPresent();
        assertThat(result.orElseThrow().toString()).contains("redirects instead of resolving directly");
    }

    @Test
    void validate_reportsBrokenRemoteLinks() {
        CountingRequester requester = new CountingRequester(Map.of(URI.create("https://example.test/missing"), 404));
        RemoteLinkValidator validator = new RemoteLinkValidator(requester, TIMEOUT);

        Optional<?> result = validator.validate(DOCUMENT, "https://example.test/missing");

        assertThat(result).isPresent();
        assertThat(result.orElseThrow().toString()).contains("Remote link is not reachable");
    }

    @Test
    void validate_cachesDuplicateRemoteLinks() {
        CountingRequester requester = new CountingRequester(Map.of(URI.create("https://example.test/same"), 200));
        RemoteLinkValidator validator = new RemoteLinkValidator(requester, TIMEOUT);

        validator.validate(DOCUMENT, "https://example.test/same");
        validator.validate(Path.of("other.md"), "https://example.test/same");

        assertThat(requester.callCount("https://example.test/same")).isEqualTo(1);
    }

    @Test
    void validate_reportsTimeoutWithoutThrowing() {
        RemoteLinkValidator validator = new RemoteLinkValidator(new TimeoutRequester(), TIMEOUT);

        Optional<?> result = validator.validate(DOCUMENT, "https://example.test/slow");

        assertThat(result).isPresent();
        assertThat(result.orElseThrow().toString()).contains("timed out after 10 seconds");
    }

    @Test
    void validate_preservesInterruptedFlagWhenRemoteCheckIsInterrupted() {
        RemoteLinkValidator validator = new RemoteLinkValidator(new InterruptedRequester(), TIMEOUT);

        Optional<?> result = validator.validate(DOCUMENT, "https://example.test/stop");

        assertThat(result).isPresent();
        assertThat(Thread.currentThread().isInterrupted()).isTrue();
        assertThat(result.orElseThrow().toString()).contains("Remote link check failed");
    }

    private static final class CountingRequester implements RemoteLinkRequester {

        private final Map<URI, Integer> statusCodes;
        private final List<URI> requestedUris = new ArrayList<>();
        private final Map<String, AtomicInteger> callCounts = new ConcurrentHashMap<>();

        private CountingRequester(Map<URI, Integer> statusCodes) {
            this.statusCodes = statusCodes;
        }

        @Override
        public RemoteLinkResponse request(URI uri, String method, Duration timeout) {
            synchronized (requestedUris) {
                requestedUris.add(uri);
            }
            callCounts.computeIfAbsent(uri.toString(), ignored -> new AtomicInteger()).incrementAndGet();
            return new RemoteLinkResponse(statusCodes.getOrDefault(uri, 200));
        }

        private List<URI> requestedUris() {
            synchronized (requestedUris) {
                return List.copyOf(requestedUris);
            }
        }

        private int callCount(String uri) {
            return callCounts.getOrDefault(uri, new AtomicInteger()).get();
        }
    }

    private static final class SequencedRequester implements RemoteLinkRequester {

        private final List<RemoteCall> calls;
        private final List<String> requestedMethods = new ArrayList<>();
        private int index;

        private SequencedRequester(RemoteCall... calls) {
            this.calls = List.of(calls);
        }

        @Override
        public RemoteLinkResponse request(URI uri, String method, Duration timeout) {
            RemoteCall call = calls.get(index++);
            requestedMethods.add(method);
            assertThat(uri).isEqualTo(call.uri());
            assertThat(method).isEqualTo(call.method());
            return new RemoteLinkResponse(call.statusCode());
        }

        private List<String> requestedMethods() {
            return List.copyOf(requestedMethods);
        }
    }

    private record RemoteCall(URI uri, String method, int statusCode) {}

    private static final class TimeoutRequester implements RemoteLinkRequester {

        @Override
        public RemoteLinkResponse request(URI uri, String method, Duration timeout) throws HttpTimeoutException {
            throw new HttpTimeoutException("timed out");
        }
    }

    private static final class InterruptedRequester implements RemoteLinkRequester {

        @Override
        public RemoteLinkResponse request(URI uri, String method, Duration timeout) throws InterruptedException {
            throw new InterruptedException("interrupted");
        }
    }
}
