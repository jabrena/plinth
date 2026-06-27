package info.jab.markdownvalidator.application;

import info.jab.markdownvalidator.application.port.MarkdownFileFinder;
import info.jab.markdownvalidator.application.port.RemoteLinkRequester;
import info.jab.markdownvalidator.application.port.RemoteLinkResponse;
import info.jab.markdownvalidator.domain.ValidationReport;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.assertj.core.api.Assertions.assertThat;

class MarkdownValidationServiceTest {

    @TempDir
    Path tempDir;

    @Test
    void validate_returnsRootMissingReportWhenRootDoesNotExist() throws IOException {
        MarkdownValidationService service = service(new FixedFinder(List.of()), new CountingRequester(Map.of()));

        ValidationReport report = service.validate(tempDir.resolve("missing"), List.of("docs"), false);

        assertThat(report.rootMissing()).isTrue();
    }

    @Test
    void validate_returnsEmptyReportWhenNoMarkdownFilesExist() throws IOException {
        MarkdownValidationService service = service(new FixedFinder(List.of()), new CountingRequester(Map.of()));

        ValidationReport report = service.validate(tempDir, List.of("docs"), false);

        assertThat(report.noMarkdownFiles()).isTrue();
        assertThat(report.documentsValidated()).isZero();
    }

    @Test
    void validate_checksAllFilesWhenSomeDocumentsFail() throws IOException {
        Path first = markdownFile("a.md", "[bad](https://example.test/a)");
        Path second = markdownFile("b.md", "[bad](https://example.test/b)");
        CountingRequester requester = new CountingRequester(
                Map.of(URI.create("https://example.test/a"), 404, URI.create("https://example.test/b"), 404));
        MarkdownValidationService service = service(new FixedFinder(List.of(first, second)), requester);

        ValidationReport report = service.validate(tempDir, List.of("docs"), false);

        assertThat(report.documentsFound()).isEqualTo(2);
        assertThat(report.documentsValidated()).isEqualTo(2);
        assertThat(report.passed()).isFalse();
        assertThat(report.errors()).hasSize(2);
        assertThat(requester.requestedUris())
                .containsExactlyInAnyOrder(URI.create("https://example.test/a"), URI.create("https://example.test/b"));
    }

    private Path markdownFile(String name, String content) throws IOException {
        return Files.writeString(tempDir.resolve(name), content);
    }

    private static MarkdownValidationService service(MarkdownFileFinder finder, RemoteLinkRequester requester) {
        RemoteLinkValidator remoteLinkValidator = new RemoteLinkValidator(requester, Duration.ofSeconds(10));
        MarkdownDocumentValidator documentValidator = new MarkdownDocumentValidator(remoteLinkValidator);
        return new MarkdownValidationService(finder, new StructuredValidationRunner(documentValidator));
    }

    private record FixedFinder(List<Path> files) implements MarkdownFileFinder {

        @Override
        public List<Path> findMarkdownFiles(Path root, List<String> targetDirectories, boolean verbose) {
            return files;
        }
    }

    private static final class CountingRequester implements RemoteLinkRequester {

        private final Map<URI, Integer> statusCodes;
        private final List<URI> requestedUris = new ArrayList<>();

        private CountingRequester(Map<URI, Integer> statusCodes) {
            this.statusCodes = statusCodes;
        }

        @Override
        public RemoteLinkResponse request(URI uri, String method, Duration timeout) {
            synchronized (requestedUris) {
                requestedUris.add(uri);
            }
            return new RemoteLinkResponse(statusCodes.getOrDefault(uri, 200));
        }

        private List<URI> requestedUris() {
            synchronized (requestedUris) {
                return List.copyOf(requestedUris);
            }
        }
    }
}
