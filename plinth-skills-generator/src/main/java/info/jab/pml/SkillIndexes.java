package info.jab.pml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import org.jspecify.annotations.Nullable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Inventory of skills to generate, loaded from {@code skills.xml}.
 * <p>
 * Each entry has an {@code id} (numeric or string like "010"). The skillId is derived from the
 * first item in {@code references/reference-list/reference} unless {@code skillId} is explicitly set.
 * Optional {@code resource-list/resource} entries declare classpath sources and
 * skill-root-relative targets for standalone generated files.
 * Each skill must have a summary in {@code skill-indexes/{id}-skill.md} or
 * {@code skill-indexes/{id}-skill.xml}. XML summaries are auto-detected when the XML file exists.
 */
public final class SkillIndexes {

    private static final String INVENTORY_RESOURCE = "skills.xml";
    private SkillIndexes() {}

    /**
     * Returns the skill IDs from the inventory. For each entry, validates the skill summary
     * exists and resolves the skillId.
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
            String skillId = resolveSkillId(entry);
            skillIds.add(skillId);
        }

        return skillIds.stream();
    }

    /**
     * Returns skill descriptors for generator use.
     */
    public static Stream<SkillDescriptor> skillDescriptors() {
        List<InventoryEntry> entries = loadInventory();
        List<SkillDescriptor> descriptors = new ArrayList<>();
        for (InventoryEntry entry : entries) {
            String numericId = entry.numericId();
            validateSkillSummaryExists(numericId, entry.useXml());
            String skillId = resolveSkillId(entry);
            descriptors.add(new SkillDescriptor(
                skillId,
                entry.requiresSystemPrompt(),
                entry.useXml(),
                entry.references(),
                entry.resources()
            ));
        }
        return descriptors.stream();
    }

    /**
     * Skill ID, reference-generation behavior, whether to use an XML source, references, and resource files.
     * Inventory-loaded descriptors always require reference generation; the boolean component remains for API compatibility.
     */
    public record SkillDescriptor(
        String skillId,
        boolean requiresSystemPrompt,
        boolean useXml,
        List<String> references,
        List<SkillResource> resources
    ) {}

    /**
     * Classpath source and skill-root-relative target for a generated skill resource.
     */
    public record SkillResource(String sourcePath, String targetPath) {}

    /**
     * Resolves skillId from inventory entry.
     *
     * @param entry inventory entry
     * @return full skillId (e.g. 110-java-maven-best-practices)
     * @throws RuntimeException if required references are missing
     */
    public static String resolveSkillId(InventoryEntry entry) {
        if (entry.skillId() != null && !entry.skillId().isBlank()) {
            return entry.skillId();
        }
        if (entry.references().isEmpty()) {
            throw new RuntimeException("No skill-reference found for id " + entry.numericId()
                + ". Add at least one reference-list/reference entry in skills.xml.");
        }
        return entry.references().getFirst();
    }

    /**
     * Loads and parses skills.xml.
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
                if (!skillEl.getParentNode().isSameNode(root)) {
                    continue;
                }
                String numericId = skillEl.getAttribute("id");
                if (numericId == null || numericId.isBlank()) {
                    throw new RuntimeException("skill-inventory entry missing id attribute");
                }
                String skillId = skillEl.hasAttribute("skillId")
                    ? skillEl.getAttribute("skillId").trim()
                    : null;
                if (skillId != null && skillId.isEmpty()) {
                    skillId = null;
                }

                boolean useXml = detectXmlSummary(numericId);
                List<String> references = parseReferences(skillEl);
                List<SkillResource> resources = parseResources(skillEl, numericId);
                if (references.isEmpty() && (skillId == null || skillId.isBlank())) {
                    throw new RuntimeException("Entry with id " + numericId
                        + " has no reference-list/reference and no skillId.");
                }
                entries.add(new InventoryEntry(
                    numericId,
                    true,
                    skillId,
                    useXml,
                    references,
                    resources
                ));
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

    private static boolean detectXmlSummary(String numericId) {
        String xmlResource = "skill-indexes/" + numericId + "-skill.xml";
        try (InputStream stream = getResource(xmlResource)) {
            return stream != null;
        } catch (Exception e) {
            throw new RuntimeException("Failed to detect XML skill summary: " + xmlResource, e);
        }
    }

    private static List<String> parseReferences(Element skillEl) {
        // Canonical format: <skill><reference-list><reference>...</reference></reference-list></skill>
        NodeList referenceLists = skillEl.getElementsByTagName("reference-list");
        NodeList refs = referenceLists.getLength() > 0
            ? ((Element) referenceLists.item(0)).getElementsByTagName("reference")
            : skillEl.getElementsByTagName("reference"); // backward compatibility
        List<String> values = new ArrayList<>();
        for (int i = 0; i < refs.getLength(); i++) {
            if (!(refs.item(i) instanceof Element refEl)) {
                continue;
            }
            String value = refEl.getTextContent();
            if (value != null && !value.trim().isEmpty()) {
                values.add(value.trim());
            }
        }
        return values;
    }

    private static List<SkillResource> parseResources(Element skillEl, String numericId) {
        NodeList resourceLists = skillEl.getElementsByTagName("resource-list");
        if (resourceLists.getLength() == 0) {
            return List.of();
        }
        NodeList resources = ((Element) resourceLists.item(0)).getElementsByTagName("resource");
        List<SkillResource> values = new ArrayList<>();
        for (int i = 0; i < resources.getLength(); i++) {
            if (!(resources.item(i) instanceof Element resourceEl)) {
                continue;
            }
            String source = resourceEl.getAttribute("source").trim();
            String target = resourceEl.getAttribute("target").trim();
            if (source.isEmpty() || target.isEmpty()) {
                throw new RuntimeException("Resource entry for skill " + numericId
                    + " must define non-empty source and target attributes.");
            }
            if (target.startsWith("/") || target.contains("..")) {
                throw new RuntimeException("Resource target for skill " + numericId
                    + " must be relative to the skill root: " + target);
            }
            boolean duplicateTarget = values.stream()
                .anyMatch(resource -> resource.targetPath().equals(target));
            if (duplicateTarget) {
                throw new RuntimeException("Duplicate resource target for skill " + numericId + ": " + target);
            }
            values.add(new SkillResource(source, target));
        }
        return List.copyOf(values);
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
        if (in == null && !Objects.equals(cl, SkillIndexes.class.getClassLoader())) {
            in = SkillIndexes.class.getClassLoader().getResourceAsStream(name);
        }
        return in;
    }

    /**
     * Single entry from skills.xml. The skillId is derived from the first reference entry when
     * not explicitly provided.
     * The {@code requiresSystemPrompt} component remains for API compatibility and is always true
     * for inventory-loaded entries.
     * Resources declare classpath sources and skill-root-relative output targets.
     * When useXml is true, skill summary is loaded from skill-indexes/{numericId}-skill.xml
     * and transformed via schema validation and XSLT; otherwise from skill-indexes/{numericId}-skill.md.
     */
    public record InventoryEntry(
        String numericId,
        boolean requiresSystemPrompt,
        @Nullable String skillId,
        boolean useXml,
        List<String> references,
        List<SkillResource> resources
    ) {}
}
