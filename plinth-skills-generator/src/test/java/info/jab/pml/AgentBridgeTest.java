package info.jab.pml;

import java.io.InputStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Agent Bridge Tests")
class AgentBridgeTest {

    @Test
    @DisplayName("Bridged agent assets must match plinth-agents-generator manifest")
    void should_stageAllAgentAssets_when_generateResourcesBridgeRuns() {
        AgentIndexes.agentFiles().forEach(agentFile -> {
            String resource = "skill-references/assets/agents/" + agentFile;
            assertThat(getTestResource(resource))
                .withFailMessage("Bridged agent asset missing on classpath: %s", resource)
                .isNotNull();
        });
    }

    @Test
    @DisplayName("Bridged inventory template must be available for skill generation")
    void should_stageInventoryTemplate_when_generateResourcesBridgeRuns() {
        assertThat(getTestResource("skill-references/assets/java-agents-inventory-template.md"))
            .isNotNull();
    }

    private InputStream getTestResource(String resourceName) {
        return AgentBridgeTest.class.getClassLoader().getResourceAsStream(resourceName);
    }
}
