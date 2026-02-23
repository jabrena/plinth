package info.jab.pml;

import java.util.stream.Stream;

public final class SkillsInventory {

    private SkillsInventory() {
    }

    public static Stream<String> skillIds() {
        return Stream.of(
            "110-java-maven-best-practices",
            "111-java-maven-dependencies"
        );
    }
}
