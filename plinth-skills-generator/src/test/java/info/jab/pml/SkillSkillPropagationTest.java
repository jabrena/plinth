package info.jab.pml;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Skill Skill Propagation Tests")
class SkillSkillPropagationTest {

    @Test
    @DisplayName("Generated 003 must ship inventory template asset and reference it without embedding content")
    void should_shipInventoryTemplateAsset_when_skillsInventorySkillIsGenerated() {
        SkillsGenerator generator = new SkillsGenerator();
        SkillsGenerator.SkillOutput output = generator.generateSkill("003-skills-inventory", true, true);

        String reference = output.referenceMds().get("003-skills-inventory");
        String templateAssetPath = "assets/java-skills-inventory-template.md";
        String template = output.resourceFiles().get(templateAssetPath);

        assertThat(output.resourceFiles())
            .withFailMessage("Generated 003 missing asset %s", templateAssetPath)
            .containsEntry(templateAssetPath, template);
        assertThat(reference)
            .contains("](../" + templateAssetPath + ")")
            .doesNotContain("# Skills for Java")
            .doesNotContain("## Regulations skills");

        assertThat(template)
            .contains("# Skills for Java")
            .contains("## Inventory")
            .contains("## Regulations skills");
    }
}
