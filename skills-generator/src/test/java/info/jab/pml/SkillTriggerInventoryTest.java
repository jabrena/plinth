package info.jab.pml;

import java.io.IOException;
import java.io.InputStream;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Skill trigger inventory")
class SkillTriggerInventoryTest {

    private static final int MINIMUM_TRIGGER_COUNT = 5;

    @Test
    @DisplayName("Should require minimum triggers for every skill")
    void should_requireMinimumTriggersForEverySkill() throws Exception {
        List<SkillTriggerCount> counts = SkillIndexes.loadInventory().stream()
            .map(this::countTriggers)
            .sorted(Comparator.comparing(SkillTriggerCount::skillId))
            .toList();

        assertThat(counts)
            .hasSize(SkillIndexes.loadInventory().size())
            .allSatisfy(count -> assertThat(count.skillName()).isNotBlank());

        List<SkillTriggerCount> belowMinimumTriggerCount = counts.stream()
            .filter(count -> count.triggers() < MINIMUM_TRIGGER_COUNT)
            .toList();

        assertThat(belowMinimumTriggerCount)
            .withFailMessage(
                "Found %d skill(s) with fewer than %d triggers:%s",
                belowMinimumTriggerCount.size(),
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

    private String formatBelowMinimumTriggerCount(List<SkillTriggerCount> counts) {
        if (counts.isEmpty()) {
            return "";
        }

        return System.lineSeparator()
            + counts.stream()
                .sorted(Comparator
                    .comparingInt(SkillTriggerCount::triggers)
                    .thenComparing(SkillTriggerCount::skillId))
                .map(count -> " - " + count.skillId() + " / " + count.skillName())
                .collect(Collectors.joining(System.lineSeparator()));
    }

    private record SkillTriggerCount(String skillId, String skillName, int triggers) {}
}
