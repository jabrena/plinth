package info.jab.pml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Inventory of system prompts to generate, loaded from {@code system-prompt-inventory.xml}.
 * <p>
 * Each entry has a {@code name} attribute corresponding to the base name of the XML source file
 * (e.g. {@code 110-java-maven-best-practices} maps to {@code system-prompts/110-java-maven-best-practices.xml}).
 */
public final class SystemPromptsInventory {

    private static final String INVENTORY_RESOURCE = "system-prompt-inventory.xml";

    private SystemPromptsInventory() {
    }

    public static Stream<String> baseNames() {
        return loadInventory().stream();
    }

    public static Stream<String> xmlFilenames() {
        return baseNames().map(name -> "system-prompts/" + name + ".xml");
    }

    private static List<String> loadInventory() {
        ClassLoader cl = SystemPromptsInventory.class.getClassLoader();
        try (InputStream stream = cl.getResourceAsStream(INVENTORY_RESOURCE)) {
            if (stream == null) {
                throw new RuntimeException("System prompt inventory not found: " + INVENTORY_RESOURCE);
            }
            return parseInventory(stream);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load system prompt inventory", e);
        }
    }

    private static List<String> parseInventory(InputStream in) throws Exception {
        Document doc = InventoryXmlLoader.parse(in);
        Element root = doc.getDocumentElement();
        if (!"system-prompt-inventory".equals(root.getNodeName())) {
            throw new RuntimeException("System prompt inventory root must be <system-prompt-inventory>");
        }
        NodeList nodes = root.getElementsByTagName("prompt");
        List<String> names = new ArrayList<>();
        for (int i = 0; i < nodes.getLength(); i++) {
            if (!(nodes.item(i) instanceof Element el)) {
                continue;
            }
            if (el.getParentNode() != root) {
                continue;
            }
            String name = el.getAttribute("name");
            if (name == null || name.isBlank()) {
                throw new RuntimeException("system-prompt-inventory entry missing name attribute");
            }
            names.add(name.trim());
        }
        if (names.isEmpty()) {
            throw new RuntimeException("System prompt inventory must contain at least one <prompt> entry");
        }
        return names;
    }
}
