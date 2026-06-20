package info.jab.pml;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Skill reference example inventory")
class SkillReferenceExampleInventoryTest {

    private static final Logger logger = LoggerFactory.getLogger(SkillReferenceExampleInventoryTest.class);

    @Test
    @DisplayName("Should report example counts for every skill reference")
    void should_reportExampleCountsForEverySkillReference() throws Exception {
        List<ReferenceExampleCount> counts = SkillIndexes.skillDescriptors()
            .filter(SkillIndexes.SkillDescriptor::requiresSystemPrompt)
            .flatMap(descriptor -> descriptor.references().stream()
                .map(reference -> countExamples(descriptor.skillId(), reference)))
            .sorted(Comparator
                .comparing(ReferenceExampleCount::skillId)
                .thenComparing(ReferenceExampleCount::reference))
            .toList();

        assertThat(counts).isNotEmpty();
        assertThat(counts)
            .allSatisfy(count -> assertThat(count.examples()).isGreaterThanOrEqualTo(0));

        String report = buildReport(counts);
        Path reportPath = writeReport(report);
        logger.info("Skill reference example count details:{}", System.lineSeparator() + report);

        assertThat(reportPath)
            .exists()
            .isRegularFile();
    }

    private ReferenceExampleCount countExamples(String skillId, String reference) {
        String resourceName = "skill-references/" + reference + ".xml";
        try (InputStream stream = getClass().getClassLoader().getResourceAsStream(resourceName)) {
            assertThat(stream)
                .withFailMessage("System-prompt XML not found: %s", resourceName)
                .isNotNull();

            Document document = parseXml(stream, resourceName);
            int examples = document.getElementsByTagName("example").getLength();
            return new ReferenceExampleCount(skillId, reference, examples);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read " + resourceName, e);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to parse " + resourceName, e);
        }
    }

    private Document parseXml(InputStream stream, String resourceName) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setXIncludeAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource inputSource = new InputSource(stream);
        inputSource.setSystemId(resolveBaseUri(resourceName));
        return builder.parse(inputSource);
    }

    private String resolveBaseUri(String resourceName) {
        URL resourceUrl = getClass().getClassLoader().getResource(resourceName);
        if (resourceUrl == null) {
            return "";
        }
        String url = resourceUrl.toString();
        int lastSlash = url.lastIndexOf('/');
        return lastSlash > 0 ? url.substring(0, lastSlash + 1) : url;
    }

    private String buildReport(List<ReferenceExampleCount> counts) {
        long skillCount = counts.stream()
            .map(ReferenceExampleCount::skillId)
            .distinct()
            .count();
        String rows = counts.stream()
            .map(count -> "| " + count.skillId() + " | " + count.reference() + " | " + count.examples() + " |")
            .collect(Collectors.joining(System.lineSeparator()));

        return """
            # Skill Reference Example Counts

            Total skills: %d
            Total references: %d

            | Skill | Reference | Examples |
            |---|---|---:|
            %s
            """.formatted(skillCount, counts.size(), rows);
    }

    private Path writeReport(String report) throws IOException {
        Path reportPath = Paths.get("target", "skill-reference-example-counts.md");
        Files.createDirectories(reportPath.getParent());
        Files.writeString(reportPath, report);
        logger.info("Skill reference example count report saved to: {}", reportPath.toAbsolutePath());
        return reportPath;
    }

    private record ReferenceExampleCount(String skillId, String reference, int examples) {}
}
