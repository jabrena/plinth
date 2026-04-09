package info.jab.pml;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.Optional;
import java.util.stream.Stream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Generator for Agent Skills (SKILL.md and references) from XML rule definitions and skill files.
 * <p>
 * Reuses CursorRulesGenerator for full rule content. SKILL.md is sourced from
 * {@code skills/{numericId}-skill.md} (user-editable), where numericId is extracted from skillId (e.g. 110 from 110-java-maven-best-practices).
 * The list of skills to generate is defined in {@code skill-inventory.xml}; each must have a
 * matching skill summary in {@code skills/} and a matching system-prompt in {@code system-prompts/}.
 */
public final class SkillsGenerator {

    private static final String PROJECT_TAG = " Part of the skills-for-java project";
    private static final String LICENSE_FIELD = "license: Apache-2.0";

    private final CursorRulesGenerator cursorRulesGenerator;

    public SkillsGenerator() {
        this.cursorRulesGenerator = new CursorRulesGenerator();
    }

    /**
     * Generates SKILL.md and reference content for all skills in the inventory.
     * Skills with {@code requiresSystemPrompt=false} get SKILL.md only (no reference content).
     *
     * @return stream of generated skill outputs
     */
    public Stream<SkillOutput> generateAllSkills() {
        return SkillsInventory.skillDescriptors()
            .map(d -> generateSkill(d.skillId(), d.requiresSystemPrompt(), d.useXml()));
    }

    /**
     * Generates SKILL.md and reference content for a given skill.
     *
     * @param skillId the skill identifier (e.g. 110-java-maven-best-practices)
     * @return the generated skill output
     * @throws RuntimeException if resources cannot be loaded or generation fails
     */
    public SkillOutput generateSkill(String skillId) {
        return generateSkill(skillId, true, false);
    }

    /**
     * Generates SKILL.md and reference content for a given skill.
     *
     * @param skillId the skill identifier (e.g. 110-java-maven-best-practices)
     * @param requiresSystemPrompt when false, skips system-prompt XML and reference generation
     * @return the generated skill output
     */
    public SkillOutput generateSkill(String skillId, boolean requiresSystemPrompt) {
        return generateSkill(skillId, requiresSystemPrompt, false);
    }

    /**
     * Generates SKILL.md and reference content for a given skill.
     *
     * @param skillId the skill identifier (e.g. 110-java-maven-best-practices)
     * @param requiresSystemPrompt when false, skips system-prompt XML and reference generation
     * @param useXml when true, loads skill from skills/{numericId}-skill.xml, validates against schema, transforms via XSLT
     * @return the generated skill output
     */
    public SkillOutput generateSkill(String skillId, boolean requiresSystemPrompt, boolean useXml) {
        String referenceContent = requiresSystemPrompt
            ? generateReferenceContent(skillId, parseMetadata(skillId))
            : "";
        String skillMdContent = useXml
            ? loadSkillSummaryFromXml(skillId)
            : loadSkillSummary(skillId);
        return new SkillOutput(skillId, skillMdContent, referenceContent);
    }

    private SkillMetadata parseMetadata(String skillId) {
        String xmlFileName = "system-prompts/" + skillId + ".xml";
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
        return cursorRulesGenerator.generate("system-prompts/" + skillId + ".xml", "system-prompts.xsl");
    }

    private String loadSkillSummary(String skillId) {
        String numericId = extractNumericId(skillId);
        String resourceName = "skills/" + numericId + "-skill.md";
        try (InputStream stream = getResource(resourceName)) {
            if (stream == null) {
                throw new RuntimeException("Skill resource not found: " + resourceName
                    + ". Each skill in SkillsInventory must have a matching file in skills/.");
            }
            String content = new String(stream.readAllBytes());
            return appendProjectTagToDescription(content);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load skill: " + resourceName, e);
        }
    }

    private String loadSkillSummaryFromXml(String skillId) {
        String numericId = extractNumericId(skillId);
        String xmlResource = "skills/" + numericId + "-skill.xml";
        String xsltResource = "skill-to-markdown.xsl";
        try (
            InputStream xmlStream = getResource(xmlResource);
            InputStream xsltStream = getResource(xsltResource)
        ) {
            if (xmlStream == null) {
                throw new RuntimeException("Skill XML not found: " + xmlResource);
            }
            if (xsltStream == null) {
                throw new RuntimeException("XSLT not found: " + xsltResource);
            }
            // Parse without schema validation: skills use PML schema (prompt root)
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = docFactory.newDocumentBuilder();
            builder.parse(xmlStream);

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer(new StreamSource(xsltStream));
            StringWriter writer = new StringWriter();
            try (InputStream xmlStreamForTransform = getResource(xmlResource)) {
                if (xmlStreamForTransform == null) {
                    throw new RuntimeException("Skill XML not found: " + xmlResource);
                }
                transformer.transform(new StreamSource(xmlStreamForTransform), new StreamResult(writer));
            }
            String content = writer.toString();
            return appendProjectTagToDescription(content);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load and transform skill XML: " + xmlResource, e);
        }
    }

    private String appendProjectTagToDescription(String content) {
        boolean hasLicense = content.contains("license:");
        return content.lines()
            .map(line -> {
                if (line.startsWith("description:") && !line.endsWith(PROJECT_TAG)) {
                    return line + PROJECT_TAG;
                }
                if (!hasLicense && line.startsWith("metadata:")) {
                    return LICENSE_FIELD + System.lineSeparator() + line;
                }
                return line;
            })
            .collect(java.util.stream.Collectors.joining(System.lineSeparator(), "", System.lineSeparator()));
    }

    private static String extractNumericId(String skillId) {
        int dash = skillId.indexOf('-');
        return dash > 0 ? skillId.substring(0, dash) : skillId;
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
