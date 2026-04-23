package info.jab.pml;

import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Inventory of skills to generate, loaded from {@code skill-indexes.xml}.
 * <p>
 * Each entry has an {@code id} (numeric or string like "010"). When {@code requiresSystemPrompt}
 * is true (default), the skillId is derived by matching skill-references with prefix {@code {id}-}.
 * When false, the entry must specify {@code skillId} and no skill-reference is required.
 * Each skill must have a summary in {@code skill-indexes/{id}-skill.md} or {@code skill-indexes/{id}-skill.xml}
 * when {@code xml="true"} on the entry.
 */
public final class SkillIndexes {

    private static final String INVENTORY_RESOURCE = "skill-indexes.xml";
    private static final String SYSTEM_PROMPTS_PREFIX = "skill-references/";

    private SkillIndexes() {}

    /**
     * Returns the skill IDs from the inventory. For each entry, validates the skill summary
     * exists. When {@code requiresSystemPrompt} is true, resolves skillId from skill-references;
     * when false, uses the provided {@code skillId}.
     *
     * @return stream of skill IDs (e.g. 110-java-maven-best-practices)
     * @throws RuntimeException if the inventory cannot be loaded or validation fails
     */
    public static Stream<String> skillIds() {
        List<InventoryEntry> entries = loadInventory();
        List<String> skillIds = new ArrayList<>();

        for (InventoryEntry entry : entries) {
            String numericId = entry.numericId();
            validateSkillSummaryExists(numericId, entry.useXml());
            String skillId = entry.requiresSystemPrompt()
                ? resolveSkillIdFromPrefix(numericId)
                : entry.skillId();
            skillIds.add(skillId);
        }

        return skillIds.stream();
    }

    /**
     * Returns skill descriptors (skillId + requiresSystemPrompt + useXml) for generator use.
     */
    public static Stream<SkillDescriptor> skillDescriptors() {
        List<InventoryEntry> entries = loadInventory();
        List<SkillDescriptor> descriptors = new ArrayList<>();
        for (InventoryEntry entry : entries) {
            String numericId = entry.numericId();
            validateSkillSummaryExists(numericId, entry.useXml());
            String skillId = entry.requiresSystemPrompt()
                ? resolveSkillIdFromPrefix(numericId)
                : entry.skillId();
            descriptors.add(new SkillDescriptor(skillId, entry.requiresSystemPrompt(), entry.useXml()));
        }
        return descriptors.stream();
    }

    /**
     * Skill ID, whether it requires a system prompt for reference generation, and whether to use XML source.
     */
    public record SkillDescriptor(String skillId, boolean requiresSystemPrompt, boolean useXml) {}

    /**
     * Resolves skillId by finding the skill-reference XML that starts with {@code {numericId}-}.
     *
     * @param numericId numeric id from inventory (e.g. "111" or "014")
     * @return full skillId (e.g. 110-java-maven-best-practices)
     * @throws RuntimeException if none or multiple skill-references match
     */
    public static String resolveSkillIdFromPrefix(String numericId) {
        String prefix = numericId + "-";
        List<String> matches = listSystemPromptBaseNames().stream()
            .filter(name -> name.startsWith(prefix) && name.endsWith(".xml"))
            .map(name -> name.substring(0, name.length() - 4))
            .toList();

        if (matches.isEmpty()) {
            throw new RuntimeException("No skill-reference found for id " + numericId
                + ". Add a skill-references/" + prefix + "*.xml file under skills-generator/src/main/resources/skill-references.");
        }
        if (matches.size() > 1) {
            throw new RuntimeException("Multiple skill-references match id " + numericId + ": " + matches);
        }
        return matches.getFirst();
    }

    private static List<String> listSystemPromptBaseNames() {
        try {
            // Anchor on skill-reference-to-markdown.xsl to locate the JAR or exploded classes directory
            URL anchor = getResourceUrl("skill-reference-to-markdown.xsl");
            if (anchor == null) {
                throw new RuntimeException("skill-reference-to-markdown.xsl not found on classpath");
            }
            if ("jar".equals(anchor.getProtocol())) {
                JarURLConnection conn = (JarURLConnection) anchor.openConnection();
                try (JarFile jar = conn.getJarFile()) {
                    return jar.stream()
                        .map(JarEntry::getName)
                        .filter(name -> name.startsWith(SYSTEM_PROMPTS_PREFIX) && name.endsWith(".xml"))
                        .filter(name -> !name.contains("/assets/"))
                        .map(name -> name.substring(SYSTEM_PROMPTS_PREFIX.length()))
                        .toList();
                }
            }
            if ("file".equals(anchor.getProtocol())) {
                Path base = Paths.get(anchor.toURI()).getParent();
                Path systemPromptsDir = base.resolve("skill-references");
                if (!Files.isDirectory(systemPromptsDir)) {
                    return List.of();
                }
                try (Stream<Path> files = Files.list(systemPromptsDir)) {
                    return files
                        .filter(p -> p.toString().endsWith(".xml"))
                        .filter(p -> !p.toString().contains("assets"))
                        .map(p -> p.getFileName().toString())
                        .toList();
                }
            }
            return List.of();
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException("Failed to list skill-references", e);
        }
    }

    private static URL getResourceUrl(String name) {
        ClassLoader cl = SkillIndexes.class.getClassLoader();
        URL url = cl.getResource(name);
        if (url == null) {
            url = Thread.currentThread().getContextClassLoader().getResource(name);
        }
        return url;
    }

    /**
     * Loads and parses skill-indexes.xml.
     */
    public static List<InventoryEntry> loadInventory() {
        try (InputStream stream = getResource(INVENTORY_RESOURCE)) {
            if (stream == null) {
                throw new RuntimeException("Skill inventory not found: " + INVENTORY_RESOURCE);
            }
            return parseInventory(stream);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load skill inventory", e);
        }
    }

    private static List<InventoryEntry> parseInventory(InputStream in) {
        try {
            Document doc = InventoryXmlLoader.parse(in);
            Element root = doc.getDocumentElement();
            if (!"skill-inventory".equals(root.getNodeName())) {
                throw new RuntimeException("Skill inventory root must be <skill-inventory>");
            }
            NodeList skillNodes = root.getElementsByTagName("skill");
            List<InventoryEntry> entries = new ArrayList<>();
            for (int i = 0; i < skillNodes.getLength(); i++) {
                if (!(skillNodes.item(i) instanceof Element skillEl)) {
                    continue;
                }
                if (skillEl.getParentNode() != root) {
                    continue;
                }
                String numericId = skillEl.getAttribute("id");
                if (numericId == null || numericId.isBlank()) {
                    throw new RuntimeException("skill-inventory entry missing id attribute");
                }
                boolean requiresSystemPrompt = parseBooleanAttribute(skillEl, "requiresSystemPrompt", true);
                String skillId = skillEl.hasAttribute("skillId")
                    ? skillEl.getAttribute("skillId").trim()
                    : null;
                if (skillId != null && skillId.isEmpty()) {
                    skillId = null;
                }

                if (!requiresSystemPrompt && (skillId == null || skillId.isBlank())) {
                    throw new RuntimeException("Entry with id " + numericId
                        + " has requiresSystemPrompt=false but no skillId specified.");
                }
                boolean useXml = parseXmlAttribute(skillEl);
                entries.add(new InventoryEntry(numericId, requiresSystemPrompt, skillId, useXml));
            }
            if (entries.isEmpty()) {
                throw new RuntimeException("Skill inventory must contain at least one <skill> entry");
            }
            return entries;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse skill inventory", e);
        }
    }

    private static boolean parseBooleanAttribute(Element el, String name, boolean defaultValue) {
        if (!el.hasAttribute(name)) {
            return defaultValue;
        }
        String v = el.getAttribute(name).trim().toLowerCase();
        return "true".equals(v) || "yes".equals(v) || "1".equals(v);
    }

    private static boolean parseXmlAttribute(Element skillEl) {
        if (!skillEl.hasAttribute("xml")) {
            return false;
        }
        String s = skillEl.getAttribute("xml").trim().toLowerCase();
        return "true".equals(s) || "yes".equals(s) || "1".equals(s);
    }

    private static void validateSkillSummaryExists(String numericId, boolean useXml) {
        String resourceName = useXml
            ? "skill-indexes/" + numericId + "-skill.xml"
            : "skill-indexes/" + numericId + "-skill.md";
        try (InputStream stream = getResource(resourceName)) {
            if (stream == null) {
                throw new RuntimeException("Skill summary not found: " + resourceName
                    + ". Add skill-indexes/" + numericId + (useXml ? "-skill.xml" : "-skill.md")
                    + " for each skill in the inventory.");
            }
        } catch (Exception e) {
            if (e instanceof RuntimeException re) {
                throw re;
            }
            throw new RuntimeException("Failed to validate skill summary: " + resourceName, e);
        }
    }

    private static InputStream getResource(String name) {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl == null) {
            cl = SkillIndexes.class.getClassLoader();
        }
        InputStream in = cl.getResourceAsStream(name);
        if (in == null && cl != SkillIndexes.class.getClassLoader()) {
            in = SkillIndexes.class.getClassLoader().getResourceAsStream(name);
        }
        return in;
    }

    /**
     * Single entry from skill-indexes.xml. When requiresSystemPrompt is true,
     * skillId is derived by matching skill-references with prefix {@code {numericId}-}.
     * When false, skillId must be provided and no skill-reference is required.
     * When useXml is true, skill summary is loaded from skill-indexes/{numericId}-skill.xml
     * and transformed via schema validation and XSLT; otherwise from skill-indexes/{numericId}-skill.md.
     */
    public record InventoryEntry(String numericId, boolean requiresSystemPrompt, String skillId, boolean useXml) {}
}
