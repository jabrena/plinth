package info.jab.pml;

import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Command Skill Propagation Tests")
class CommandSkillPropagationTest {

    @Test
    @DisplayName("Generated 004 must ship command assets and reference them without embedding bodies")
    void should_shipCommandAssets_when_commandsInstallationSkillIsGenerated() {
        SkillsGenerator generator = new SkillsGenerator();
        SkillsGenerator.SkillOutput output = generator.generateSkill("004-commands-installation", true, true);

        String reference = output.referenceMds().get("004-commands-installation");
        assertThat(reference)
            .contains("assets/commands/")
            .contains("](../assets/commands/update-issue.md)")
            .contains("](../assets/commands/benchmark.md)")
            .doesNotContain("/update-issue <issue>")
            .doesNotContain("# update-issue");

        CommandIndexes.commandFiles().forEach(commandFile -> {
            String assetPath = "assets/commands/" + commandFile;
            String commandName = commandFile.substring(0, commandFile.length() - ".md".length());

            assertThat(output.resourceFiles())
                .withFailMessage("Generated 004 missing asset %s", assetPath)
                .containsKey(assetPath);
            assertThat(reference)
                .withFailMessage("Generated 004 reference missing link to %s", assetPath)
                .contains("](../" + assetPath + ")");
            assertThat(reference)
                .withFailMessage("Generated 004 reference must not embed command /%s", commandName)
                .doesNotContain(loadBridgedCommand(commandFile));
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

    private String loadBridgedCommand(String commandFile) {
        try (var stream = CommandSkillPropagationTest.class.getClassLoader()
            .getResourceAsStream("skill-references/assets/commands/" + commandFile)) {
            assertThat(stream)
                .withFailMessage("Bridged command asset not found: %s", commandFile)
                .isNotNull();
            return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load bridged command asset: " + commandFile, e);
        }
    }
}
