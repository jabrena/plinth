package info.jab.pml;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Command Skill Propagation Tests")
class CommandSkillPropagationTest {

    @Test
    @DisplayName("Generated 004 reference must embed command bodies from bridged assets")
    void should_embedCommandBodies_when_commandsInstallationSkillIsGenerated() {
        SkillsGenerator generator = new SkillsGenerator();
        SkillsGenerator.SkillOutput output = generator.generateSkill("004-commands-installation", true, true);

        String reference = output.referenceMds().get("004-commands-installation");
        assertThat(reference)
            .contains("/update-issue <issue>")
            .contains("/create-spec")
            .contains("/implement-spec")
            .contains("/benchmark");

        CommandIndexes.commandFiles().forEach(commandFile -> {
            String commandName = commandFile.substring(0, commandFile.length() - ".md".length());
            assertThat(reference)
                .withFailMessage("Generated 004 reference missing command /%s", commandName)
                .contains("# " + commandName);
        });
    }

    @Test
    @DisplayName("Generated 001 reference must list every command from the manifest")
    void should_listManifestCommands_when_commandsInventorySkillIsGenerated() {
        SkillsGenerator generator = new SkillsGenerator();
        SkillsGenerator.SkillOutput output = generator.generateSkill("001-commands-inventory", true, true);

        String reference = output.referenceMds().get("001-commands-inventory");
        assertThat(reference).contains("# Embedded Commands Inventory");

        CommandIndexes.commandFiles().forEach(commandFile -> {
            String commandName = commandFile.substring(0, commandFile.length() - ".md".length());
            assertThat(reference)
                .withFailMessage("Generated 001 reference missing command /%s", commandName)
                .contains("`/" + commandName + "`");
        });
    }
}
