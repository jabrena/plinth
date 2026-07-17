package info.jab.pml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import org.jspecify.annotations.Nullable;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Inventory of embedded command assets, loaded from {@code commands.xml}.
 *
 * <p>Inventory {@code @file} entries reference XML sources (for example
 * {@code update-issue.xml}). {@link #commandFiles()} exposes the generated
 * Markdown asset names installers and the skills bridge consume
 * ({@code update-issue.md}).
 */
public final class CommandIndexes {

    private static final String INVENTORY_RESOURCE = "commands.xml";

    private CommandIndexes() {}

    /**
     * Returns generated command Markdown asset file names in installation order.
     *
     * @return stream of command file names, for example {@code update-issue.md}
     */
    public static Stream<String> commandFiles() {
        return loadInventorySources().stream().map(CommandIndexes::toMarkdownFileName);
    }

    /**
     * Returns command XML source file names in installation order.
     *
     * @return stream of command source file names, for example {@code update-issue.xml}
     */
    public static Stream<String> commandSources() {
        return loadInventorySources().stream();
    }

    static List<String> loadInventorySources() {
        try (InputStream stream = getResource(INVENTORY_RESOURCE)) {
            if (stream == null) {
                throw new RuntimeException("Command inventory not found: " + INVENTORY_RESOURCE);
            }
            return parseInventory(stream);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load command inventory", e);
        }
    }

    /**
     * Maps an inventory XML source file name to the generated Markdown asset name.
     *
     * @param sourceFile inventory {@code @file} value ending in {@code .xml}
     * @return corresponding Markdown file name ending in {@code .md}
     */
    static String toMarkdownFileName(String sourceFile) {
        if (!sourceFile.endsWith(".xml")) {
            throw new IllegalArgumentException("Command source must be an XML file name: " + sourceFile);
        }
        return sourceFile.substring(0, sourceFile.length() - ".xml".length()) + ".md";
    }

    private static List<String> parseInventory(InputStream in) {
        try {
            Element root = InventoryXmlLoader.parse(in).getDocumentElement();
            if (!"command-inventory".equals(root.getNodeName())) {
                throw new RuntimeException("Command inventory root must be <command-inventory>");
            }

            NodeList commandNodes = root.getElementsByTagName("command");
            List<String> commandSources = new ArrayList<>();
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
                if (file.startsWith("/") || file.contains("..") || !file.endsWith(".xml")) {
                    throw new RuntimeException("Command file must be an XML source file name: " + file);
                }
                commandSources.add(file);
            }
            if (commandSources.isEmpty()) {
                throw new RuntimeException("Command inventory must contain at least one <command> entry");
            }
            return List.copyOf(commandSources);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse command inventory", e);
        }
    }

    private static @Nullable InputStream getResource(String name) {
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
