package info.jab.pml;

import java.util.stream.Stream;

/**
 * Inventory of system prompts to generate, loaded from {@code skills.xml}.
 * <p>
 * References are declared per skill under {@code reference-list/reference}.
 */
public final class SkillReferences {

    private SkillReferences() {
    }

    public static Stream<String> baseNames() {
        return SkillIndexes.loadInventory().stream()
            .flatMap(entry -> entry.references().stream())
            .distinct();
    }

    public static Stream<String> xmlFilenames() {
        return baseNames().map(name -> "skill-references/" + name + ".xml");
    }
}
