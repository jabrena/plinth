package info.jab.pml;

import java.io.InputStream;
import java.util.Optional;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Generator for Agent Skills (SKILL.md and references) from XML rule definitions.
 * <p>
 * Reuses CursorRulesGenerator for full rule content and parses XML metadata for
 * skill-specific frontmatter and SKILL.md generation.
 */
public final class SkillsGenerator {

    private final CursorRulesGenerator cursorRulesGenerator;

    public SkillsGenerator() {
        this.cursorRulesGenerator = new CursorRulesGenerator();
    }

    /**
     * Generates SKILL.md and reference content for a given skill.
     *
     * @param skillId the skill identifier (e.g. 110-java-maven-best-practices)
     * @return the generated skill output
     * @throws RuntimeException if resources cannot be loaded or generation fails
     */
    public SkillOutput generateSkill(String skillId) {
        SkillMetadata metadata = parseMetadata(skillId);
        String referenceContent = generateReferenceContent(skillId, metadata);
        String skillMdContent = generateSkillMd(skillId, metadata);
        return new SkillOutput(skillId, skillMdContent, referenceContent);
    }

    private SkillMetadata parseMetadata(String skillId) {
        String xmlFileName = skillId + ".xml";
        try (InputStream xmlStream = getResource(xmlFileName)) {
            if (xmlStream == null) {
                throw new RuntimeException("XML resource not found: " + xmlFileName);
            }
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlStream);

            Element metadata = (Element) doc.getElementsByTagName("metadata").item(0);
            if (metadata == null) {
                throw new RuntimeException("metadata element not found in " + xmlFileName);
            }

            String title = getElementText(metadata, "title");
            String description = getElementText(metadata, "description");
            String goalLongDescription = extractGoalFirstParagraph(doc);

            return new SkillMetadata(
                skillId,
                description != null ? description.replaceAll("\\.$", "") : "",
                title != null ? title : skillId,
                goalLongDescription != null ? goalLongDescription : ""
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse metadata from " + xmlFileName, e);
        }
    }

    private String getElementText(Element parent, String tagName) {
        NodeList nodes = parent.getElementsByTagName(tagName);
        if (nodes.getLength() == 0) {
            return null;
        }
        String text = nodes.item(0).getTextContent();
        return text != null ? text.trim() : null;
    }

    private String extractGoalFirstParagraph(Document doc) {
        NodeList goalNodes = doc.getElementsByTagName("goal");
        if (goalNodes.getLength() == 0) {
            return null;
        }
        String goalText = goalNodes.item(0).getTextContent();
        if (goalText == null || goalText.isEmpty()) {
            return null;
        }
        goalText = goalText.trim();
        int doubleNewline = goalText.indexOf("\n\n");
        int hashSection = goalText.indexOf("\n###");
        int end = goalText.length();
        if (doubleNewline > 0 && doubleNewline < end) {
            end = doubleNewline;
        }
        if (hashSection > 0 && hashSection < end) {
            end = hashSection;
        }
        goalText = goalText.substring(0, end).trim().replaceAll("\\s+", " ");
        return goalText.isEmpty() ? null : goalText;
    }

    private String generateReferenceContent(String skillId, SkillMetadata metadata) {
        String cursorRulesOutput = cursorRulesGenerator.generate(skillId + ".xml", "cursor-rules.xsl");
        return injectReferenceFrontmatter(cursorRulesOutput, metadata.displayTitle(), metadata.goalLongDescription());
    }

    private String injectReferenceFrontmatter(String content, String name, String description) {
        int closingFrontmatter = content.indexOf("\n---", 1);
        if (closingFrontmatter < 0) {
            return content;
        }
        String frontmatterAddition = "\nname: " + name + "\ndescription: " + description;
        return content.substring(0, closingFrontmatter) + frontmatterAddition + content.substring(closingFrontmatter);
    }

    private String generateSkillMd(String skillId, SkillMetadata metadata) {
        String template = loadTemplate();
        return template
            .replace("{{skillId}}", skillId)
            .replace("{{description}}", metadata.description())
            .replace("{{title}}", metadata.displayTitle());
    }

    private String loadTemplate() {
        try (InputStream stream = getResource("skill-template.md")) {
            if (stream == null) {
                throw new RuntimeException("skill-template.md not found");
            }
            return new String(stream.readAllBytes());
        } catch (Exception e) {
            throw new RuntimeException("Failed to load skill-template.md", e);
        }
    }

    private InputStream getResource(String name) {
        return Optional.ofNullable(getClass().getClassLoader().getResourceAsStream(name))
            .or(() -> Optional.ofNullable(Thread.currentThread().getContextClassLoader().getResourceAsStream(name)))
            .orElse(null);
    }

    /**
     * Output of skill generation: SKILL.md content and reference content.
     */
    public record SkillOutput(String skillId, String skillMd, String referenceMd) {}

    private record SkillMetadata(String skillId, String description, String displayTitle, String goalLongDescription) {}
}
