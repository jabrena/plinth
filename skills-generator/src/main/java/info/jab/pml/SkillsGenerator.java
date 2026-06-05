package info.jab.pml;

import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

/**
 * Generator for Agent Skills (SKILL.md and references) from XML rule definitions and skill files.
 * <p>
 * Reuses SkillReferenceGenerator for full rule content. SKILL.md is sourced from
 * {@code skill-indexes/{numericId}-skill.md} (user-editable), where numericId is extracted from skillId (e.g. 110 from 110-java-maven-best-practices).
 * The list of skills to generate is defined in {@code skills.xml}; each must have a
 * matching skill summary in {@code skill-indexes/} and a matching system-prompt in {@code skill-references/}.
 */
public final class SkillsGenerator {

    private static final String PROJECT_TAG = " Part of cursor-rules-java project";
    private static final String LICENSE_FIELD = "license: Apache-2.0";

    private final SkillReferenceGenerator cursorRulesGenerator;

    public SkillsGenerator() {
        this.cursorRulesGenerator = new SkillReferenceGenerator();
    }

    /**
     * Generates SKILL.md and reference content for all skills in the inventory.
     * Skills with {@code requiresSystemPrompt=false} get SKILL.md only (no reference content).
     *
     * @return stream of generated skill outputs
     */
    public Stream<SkillOutput> generateAllSkills() {
        return SkillIndexes.skillDescriptors()
            .map(d -> generateSkill(d.skillId(), d.requiresSystemPrompt(), d.useXml(), d.references()));
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
     * @param useXml when true, loads skill from skill-indexes/{numericId}-skill.xml, validates against schema, transforms via XSLT
     * @return the generated skill output
     */
    public SkillOutput generateSkill(String skillId, boolean requiresSystemPrompt, boolean useXml) {
        List<String> references = SkillIndexes.skillDescriptors()
            .filter(d -> d.skillId().equals(skillId))
            .findFirst()
            .map(SkillIndexes.SkillDescriptor::references)
            .orElse(List.of());
        return generateSkill(skillId, requiresSystemPrompt, useXml, references);
    }

    public SkillOutput generateSkill(String skillId, boolean requiresSystemPrompt, boolean useXml, java.util.List<String> references) {
        Map<String, String> referenceContents = requiresSystemPrompt
            ? generateReferenceContents(references.isEmpty() ? List.of(skillId) : references)
            : Map.of();
        String skillMdContent = useXml
            ? loadSkillSummaryFromXml(skillId, references)
            : loadSkillSummary(skillId);
        return new SkillOutput(skillId, skillMdContent, referenceContents);
    }

    private Map<String, String> generateReferenceContents(List<String> referenceIds) {
        Map<String, String> contents = new LinkedHashMap<>();
        for (String referenceId : referenceIds) {
            contents.put(
                referenceId,
                cursorRulesGenerator.generate("skill-references/" + referenceId + ".xml", "skill-reference-to-markdown.xsl")
            );
        }
        return contents;
    }

    private String loadSkillSummary(String skillId) {
        String numericId = extractNumericId(skillId);
        String resourceName = "skill-indexes/" + numericId + "-skill.md";
        try (InputStream stream = getResource(resourceName)) {
            if (stream == null) {
                throw new RuntimeException("Skill resource not found: " + resourceName
                    + ". Each skill in SkillIndexes must have a matching file in skill-indexes/.");
            }
            String content = new String(stream.readAllBytes());
            return appendProjectTagToDescription(content);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load skill: " + resourceName, e);
        }
    }

    private String loadSkillSummaryFromXml(String skillId, java.util.List<String> references) {
        String numericId = extractNumericId(skillId);
        String xmlResource = "skill-indexes/" + numericId + "-skill.xml";
        String xsltResource = "skill-index-to-markdown.xsl";
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
            DOMSource xmlSource = createXIncludeDomSource(xmlStream, xmlResource);

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer(new StreamSource(xsltStream));
            StringWriter writer = new StringWriter();
            transformer.transform(xmlSource, new StreamResult(writer));
            String content = writer.toString();
            String withProjectTag = appendProjectTagToDescription(content);
            return appendReferencesSection(withProjectTag, references);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load and transform skill XML: " + xmlResource, e);
        }
    }

    private DOMSource createXIncludeDomSource(InputStream xmlStream, String xmlResource) throws Exception {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        docFactory.setNamespaceAware(true);
        docFactory.setXIncludeAware(true);
        DocumentBuilder builder = docFactory.newDocumentBuilder();
        InputSource inputSource = new InputSource(xmlStream);
        inputSource.setSystemId(resolveBaseUri(xmlResource));
        Document document = builder.parse(inputSource);
        return new DOMSource(document);
    }

    private String resolveBaseUri(String xmlResource) {
        URL xmlUrl = getClass().getClassLoader().getResource(xmlResource);
        if (xmlUrl != null) {
            String url = xmlUrl.toString();
            int lastSlash = url.lastIndexOf('/');
            if (lastSlash > 0) {
                return url.substring(0, lastSlash + 1);
            }
        }
        return Optional.ofNullable(getClass().getClassLoader().getResource(""))
            .map(URL::toString)
            .orElse("");
    }

    private String appendReferencesSection(String content, java.util.List<String> references) {
        if (references == null || references.isEmpty() || content.contains("## Reference")) {
            return content;
        }
        StringBuilder builder = new StringBuilder(content.stripTrailing());
        builder.append(System.lineSeparator())
            .append(System.lineSeparator())
            .append("## Reference")
            .append(System.lineSeparator())
            .append(System.lineSeparator());
        if (references.size() == 1) {
            String referencePath = "references/" + references.get(0) + ".md";
            builder.append("For detailed guidance, examples, and constraints, see [")
                .append(referencePath)
                .append("](")
                .append(referencePath)
                .append(").")
                .append(System.lineSeparator());
        } else {
            builder.append("For detailed guidance, examples, and constraints, see:")
                .append(System.lineSeparator())
                .append(System.lineSeparator());
            for (String path : references) {
                String referencePath = "references/" + path + ".md";
                builder.append("- [")
                    .append(referencePath)
                    .append("](")
                    .append(referencePath)
                    .append(")")
                    .append(System.lineSeparator());
            }
        }
        return builder.toString();
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
    public record SkillOutput(String skillId, String skillMd, Map<String, String> referenceMds) {
        public String referenceMd() {
            return referenceMds.values().stream().findFirst().orElse("");
        }
    }

}
