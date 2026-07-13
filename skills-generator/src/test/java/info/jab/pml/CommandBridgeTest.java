package info.jab.pml;

import java.io.InputStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Command Bridge Tests")
class CommandBridgeTest {

    @Test
    @DisplayName("Bridged command assets must match commands-generator manifest")
    void should_stageAllCommandAssets_when_generateResourcesBridgeRuns() {
        CommandIndexes.commandFiles().forEach(commandFile -> {
            String resource = "skill-references/assets/commands/" + commandFile;
            assertThat(getTestResource(resource))
                .withFailMessage("Bridged command asset missing on classpath: %s", resource)
                .isNotNull();
        });
    }

    @Test
    @DisplayName("Bridged inventory template must be available for skill generation")
    void should_stageInventoryTemplate_when_generateResourcesBridgeRuns() {
        assertThat(getTestResource("skill-references/assets/java-commands-inventory-template.md"))
            .isNotNull();
    }

    private InputStream getTestResource(String resourceName) {
        return CommandBridgeTest.class.getClassLoader().getResourceAsStream(resourceName);
    }
}
