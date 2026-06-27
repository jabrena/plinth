package info.jab.pml;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Skill trigger inventory")
class SkillTriggerInventoryTest {

    private static final Logger logger = LoggerFactory.getLogger(SkillTriggerInventoryTest.class);
    private static final int MINIMUM_TRIGGER_COUNT = 5;

    @Test
    @DisplayName("Should report trigger counts for every skill")
    void should_reportTriggerCounts_when_skillIndexesAreLoaded() throws Exception {
        List<SkillTriggerCount> counts = SkillIndexes.loadInventory().stream()
            .map(this::countTriggers)
            .sorted(Comparator.comparing(SkillTriggerCount::skillId))
            .toList();

        assertThat(counts)
            .hasSize(SkillIndexes.loadInventory().size())
            .allSatisfy(count -> assertThat(count.skillName()).isNotBlank());

        String report = buildReport(counts);
        Path reportPath = writeReport(report);
        logger.info("Skill trigger count details:{}", System.lineSeparator() + report);

        assertThat(reportPath)
            .exists()
            .isRegularFile();

        List<SkillTriggerCount> belowMinimumTriggerCount = counts.stream()
            .filter(count -> count.triggers() < MINIMUM_TRIGGER_COUNT)
            .toList();

        assertThat(belowMinimumTriggerCount)
            .withFailMessage(
                "Skills with fewer than %d triggers: %s",
                MINIMUM_TRIGGER_COUNT,
                formatBelowMinimumTriggerCount(belowMinimumTriggerCount)
            )
            .isEmpty();
    }

    private SkillTriggerCount countTriggers(SkillIndexes.InventoryEntry entry) {
        String resourceName = "skill-indexes/" + entry.numericId() + "-skill.xml";
        try (InputStream stream = getClass().getClassLoader().getResourceAsStream(resourceName)) {
            assertThat(stream)
                .withFailMessage("Skill XML not found: %s", resourceName)
                .isNotNull();

            Document document = InventoryXmlLoader.parse(stream);
            Element root = document.getDocumentElement();
            String skillName = elementText(root, "title");
            int triggerCount = root.getElementsByTagName("trigger").getLength();
            String skillId = SkillIndexes.resolveSkillId(entry);

            return new SkillTriggerCount(skillId, skillName, triggerCount);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read " + resourceName, e);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to parse " + resourceName, e);
        }
    }

    private String elementText(Element parent, String tagName) {
        NodeList nodes = parent.getElementsByTagName(tagName);
        assertThat(nodes.getLength())
            .withFailMessage("Expected <%s> element in %s", tagName, parent.getAttribute("id"))
            .isGreaterThan(0);

        String text = nodes.item(0).getTextContent();
        return text == null ? "" : text.trim();
    }

    private String buildReport(List<SkillTriggerCount> counts) {
        String rows = counts.stream()
            .map(count -> "| " + count.skillId() + " | " + count.skillName() + " | " + count.triggers() + " |")
            .collect(Collectors.joining(System.lineSeparator()));

        return """
            # Skill Trigger Counts

            Total skills: %d

            | Skill | Name | Triggers |
            |---|---|---:|
            %s
            """.formatted(counts.size(), rows);
    }

    private Path writeReport(String report) throws IOException {
        Path reportPath = Paths.get("target", "skill-trigger-counts.md");
        Files.createDirectories(reportPath.getParent());
        Files.writeString(reportPath, report);
        logger.info("Skill trigger count report saved to: {}", reportPath.toAbsolutePath());
        return reportPath;
    }

    private String formatBelowMinimumTriggerCount(List<SkillTriggerCount> counts) {
        return counts.stream()
            .map(count -> count.skillId() + "=" + count.triggers())
            .collect(Collectors.joining(", "));
    }

    private record SkillTriggerCount(String skillId, String skillName, int triggers) {}
}
