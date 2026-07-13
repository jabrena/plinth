package info.jab.pml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Inventory of embedded agent assets, loaded from {@code agents.xml}.
 */
public final class AgentIndexes {

    private static final String INVENTORY_RESOURCE = "agents.xml";

    private AgentIndexes() {}

    /**
     * Returns agent asset file names in installation order.
     *
     * @return stream of agent file names, for example {@code robot-architect.md}
     */
    public static Stream<String> agentFiles() {
        return loadInventory().stream();
    }

    static List<String> loadInventory() {
        try (InputStream stream = getResource(INVENTORY_RESOURCE)) {
            if (stream == null) {
                throw new RuntimeException("Agent inventory not found: " + INVENTORY_RESOURCE);
            }
            return parseInventory(stream);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load agent inventory", e);
        }
    }

    private static List<String> parseInventory(InputStream in) {
        try {
            Element root = InventoryXmlLoader.parse(in).getDocumentElement();
            if (!"agent-inventory".equals(root.getNodeName())) {
                throw new RuntimeException("Agent inventory root must be <agent-inventory>");
            }

            NodeList agentNodes = root.getElementsByTagName("agent");
            List<String> agentFiles = new ArrayList<>();
            for (int i = 0; i < agentNodes.getLength(); i++) {
                if (!(agentNodes.item(i) instanceof Element agentEl)) {
                    continue;
                }
                if (!agentEl.getParentNode().isSameNode(root)) {
                    continue;
                }
                String file = agentEl.getAttribute("file").trim();
                if (file.isEmpty()) {
                    throw new RuntimeException("agent-inventory entry missing file attribute");
                }
                if (file.startsWith("/") || file.contains("..") || !file.endsWith(".md")) {
                    throw new RuntimeException("Agent file must be a markdown file name: " + file);
                }
                agentFiles.add(file);
            }
            if (agentFiles.isEmpty()) {
                throw new RuntimeException("Agent inventory must contain at least one <agent> entry");
            }
            return List.copyOf(agentFiles);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse agent inventory", e);
        }
    }

    private static InputStream getResource(String name) {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl == null) {
            cl = AgentIndexes.class.getClassLoader();
        }
        InputStream in = cl.getResourceAsStream(name);
        if (in == null && !Objects.equals(cl, AgentIndexes.class.getClassLoader())) {
            in = AgentIndexes.class.getClassLoader().getResourceAsStream(name);
        }
        return in;
    }
}
