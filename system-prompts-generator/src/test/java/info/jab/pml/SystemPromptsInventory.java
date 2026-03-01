package info.jab.pml;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Inventory of system prompts to generate, loaded from {@code system-prompt-inventory.json}.
 * <p>
 * Each entry has a {@code name} field corresponding to the base name of the XML source file
 * (e.g. {@code 110-java-maven-best-practices} maps to {@code system-prompts/110-java-maven-best-practices.xml}).
 */
public final class SystemPromptsInventory {

    private static final String INVENTORY_RESOURCE = "system-prompt-inventory.json";

    private SystemPromptsInventory() {
    }

    public static Stream<String> baseNames() {
        return loadInventory().stream();
    }

    public static Stream<String> xmlFilenames() {
        return baseNames().map(name -> "system-prompts/" + name + ".xml");
    }

    private static List<String> loadInventory() {
        try (InputStream stream = SystemPromptsInventory.class.getClassLoader()
                .getResourceAsStream(INVENTORY_RESOURCE)) {
            if (stream == null) {
                throw new RuntimeException("System prompt inventory not found: " + INVENTORY_RESOURCE);
            }
            String json = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
            return parseInventory(json);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load system prompt inventory", e);
        }
    }

    private static List<String> parseInventory(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(json);
            if (!root.isArray()) {
                throw new RuntimeException("System prompt inventory must be a JSON array");
            }
            List<String> names = new ArrayList<>();
            for (JsonNode node : root) {
                names.add(node.required("name").asText());
            }
            return names;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse system prompt inventory", e);
        }
    }
}
