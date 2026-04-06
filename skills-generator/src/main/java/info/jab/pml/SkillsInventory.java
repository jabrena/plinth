package info.jab.pml;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;

/**
 * Inventory of skills to generate, loaded from {@code skill-inventory.json}.
 * <p>
 * Each entry has an {@code id} (numeric or string like "010"). When {@code requiresSystemPrompt}
 * is true (default), the skillId is derived by matching system-prompts with prefix {@code {id}-}.
 * When false, the entry must specify {@code skillId} and no system-prompt is required.
 * Each skill must have a summary in {@code skills/{id}-skill.md}.
 */
public final class SkillsInventory {

    private static final String INVENTORY_RESOURCE = "skill-inventory.json";
    private static final String SYSTEM_PROMPTS_PREFIX = "system-prompts/";

    private SkillsInventory() {}

    /**
     * Returns the skill IDs from the inventory. For each entry, validates the skill summary
     * exists. When {@code requiresSystemPrompt} is true, resolves skillId from system-prompts;
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
     * Resolves skillId by finding the system-prompt XML that starts with {@code {numericId}-}.
     *
     * @param numericId numeric id from inventory (e.g. "111" or "014")
     * @return full skillId (e.g. 110-java-maven-best-practices)
     * @throws RuntimeException if none or multiple system-prompts match
     */
    public static String resolveSkillIdFromPrefix(String numericId) {
        String prefix = numericId + "-";
        List<String> matches = listSystemPromptBaseNames().stream()
            .filter(name -> name.startsWith(prefix) && name.endsWith(".xml"))
            .map(name -> name.substring(0, name.length() - 4))
            .toList();

        if (matches.isEmpty()) {
            throw new RuntimeException("No system-prompt found for id " + numericId
                + ". Add a system-prompts/" + prefix + "*.xml file under skills-generator/src/main/resources/system-prompts.");
        }
        if (matches.size() > 1) {
            throw new RuntimeException("Multiple system-prompts match id " + numericId + ": " + matches);
        }
        return matches.getFirst();
    }

    private static List<String> listSystemPromptBaseNames() {
        try {
            // Anchor on system-prompts.xsl to locate the JAR or exploded classes directory
            URL anchor = getResourceUrl("system-prompts.xsl");
            if (anchor == null) {
                throw new RuntimeException("system-prompts.xsl not found on classpath");
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
                Path systemPromptsDir = base.resolve("system-prompts");
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
            throw new RuntimeException("Failed to list system-prompts", e);
        }
    }

    private static URL getResourceUrl(String name) {
        ClassLoader cl = SkillsInventory.class.getClassLoader();
        URL url = cl.getResource(name);
        if (url == null) {
            url = Thread.currentThread().getContextClassLoader().getResource(name);
        }
        return url;
    }

    /**
     * Loads and parses skill-inventory.json.
     */
    public static List<InventoryEntry> loadInventory() {
        try (InputStream stream = getResource(INVENTORY_RESOURCE)) {
            if (stream == null) {
                throw new RuntimeException("Skill inventory not found: " + INVENTORY_RESOURCE);
            }
            String json = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
            return parseInventory(json);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load skill inventory", e);
        }
    }

    private static List<InventoryEntry> parseInventory(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(json);
            if (!root.isArray()) {
                throw new RuntimeException("Skill inventory must be a JSON array");
            }

            List<InventoryEntry> entries = new ArrayList<>();
            for (JsonNode node : root) {
                String numericId = node.get("id").isTextual()
                    ? node.get("id").asText()
                    : String.valueOf(node.get("id").asInt());
                boolean requiresSystemPrompt = node.has("requiresSystemPrompt")
                    ? node.get("requiresSystemPrompt").asBoolean()
                    : true;
                String skillId = node.has("skillId") ? node.get("skillId").asText() : null;

                if (!requiresSystemPrompt && (skillId == null || skillId.isBlank())) {
                    throw new RuntimeException("Entry with id " + numericId
                        + " has requiresSystemPrompt=false but no skillId specified.");
                }
                boolean useXml = parseXmlFlag(node);
                entries.add(new InventoryEntry(numericId, requiresSystemPrompt, skillId, useXml));
            }
            return entries;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse skill inventory", e);
        }
    }

    private static boolean parseXmlFlag(JsonNode node) {
        if (!node.has("xml")) {
            return false;
        }
        JsonNode xmlNode = node.get("xml");
        if (xmlNode.isBoolean()) {
            return xmlNode.asBoolean();
        }
        if (xmlNode.isTextual()) {
            String s = xmlNode.asText().toLowerCase();
            return "true".equals(s) || "yes".equals(s) || "1".equals(s);
        }
        return false;
    }

    private static void validateSkillSummaryExists(String numericId, boolean useXml) {
        String resourceName = useXml
            ? "skills/" + numericId + "-skill.xml"
            : "skills/" + numericId + "-skill.md";
        try (InputStream stream = getResource(resourceName)) {
            if (stream == null) {
                throw new RuntimeException("Skill summary not found: " + resourceName
                    + ". Add skills/" + numericId + (useXml ? "-skill.xml" : "-skill.md")
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
            cl = SkillsInventory.class.getClassLoader();
        }
        InputStream in = cl.getResourceAsStream(name);
        if (in == null && cl != SkillsInventory.class.getClassLoader()) {
            in = SkillsInventory.class.getClassLoader().getResourceAsStream(name);
        }
        return in;
    }

    /**
     * Single entry from skill-inventory.json. When requiresSystemPrompt is true,
     * skillId is derived by matching system-prompts with prefix {@code {numericId}-}.
     * When false, skillId must be provided and no system-prompt is required.
     * When useXml is true, skill summary is loaded from skills/{numericId}-skill.xml
     * and transformed via schema validation and XSLT; otherwise from skills/{numericId}-skill.md.
     */
    public record InventoryEntry(String numericId, boolean requiresSystemPrompt, String skillId, boolean useXml) {}
}
