package info.jab.pml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Inventory of embedded command assets, loaded from {@code commands.xml}.
 */
public final class CommandIndexes {

    private static final String INVENTORY_RESOURCE = "commands.xml";

    private CommandIndexes() {}

    /**
     * Returns command asset file names in installation order.
     *
     * @return stream of command file names, for example {@code update-issue.md}
     */
    public static Stream<String> commandFiles() {
        return loadInventory().stream();
    }

    static List<String> loadInventory() {
        try (InputStream stream = getResource(INVENTORY_RESOURCE)) {
            if (stream == null) {
                throw new RuntimeException("Command inventory not found: " + INVENTORY_RESOURCE);
            }
            return parseInventory(stream);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load command inventory", e);
        }
    }

    private static List<String> parseInventory(InputStream in) {
        try {
            Element root = InventoryXmlLoader.parse(in).getDocumentElement();
            if (!"command-inventory".equals(root.getNodeName())) {
                throw new RuntimeException("Command inventory root must be <command-inventory>");
            }

            NodeList commandNodes = root.getElementsByTagName("command");
            List<String> commandFiles = new ArrayList<>();
            for (int i = 0; i < commandNodes.getLength(); i++) {
                if (!(commandNodes.item(i) instanceof Element commandEl)) {
                    continue;
                }
                if (!commandEl.getParentNode().isSameNode(root)) {
                    continue;
                }
                String file = commandEl.getAttribute("file").trim();
                if (file.isEmpty()) {
                    throw new RuntimeException("command-inventory entry missing file attribute");
                }
                if (file.startsWith("/") || file.contains("..") || !file.endsWith(".md")) {
                    throw new RuntimeException("Command file must be a markdown file name: " + file);
                }
                commandFiles.add(file);
            }
            if (commandFiles.isEmpty()) {
                throw new RuntimeException("Command inventory must contain at least one <command> entry");
            }
            return List.copyOf(commandFiles);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse command inventory", e);
        }
    }

    private static InputStream getResource(String name) {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl == null) {
            cl = CommandIndexes.class.getClassLoader();
        }
        InputStream in = cl.getResourceAsStream(name);
        if (in == null && !Objects.equals(cl, CommandIndexes.class.getClassLoader())) {
            in = CommandIndexes.class.getClassLoader().getResourceAsStream(name);
        }
        return in;
    }
}
