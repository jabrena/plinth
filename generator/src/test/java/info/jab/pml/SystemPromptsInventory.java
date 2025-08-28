package info.jab.pml;

import java.util.stream.Stream;

public final class SystemPromptsInventory {

    private SystemPromptsInventory() {
    }

    public static final Stream<String> baseNames() {
        return Stream.of(
            "100-java-cursor-rules-list",
            "110-java-maven-best-practices",
            "111-java-maven-dependencies",
            "112-java-maven-plugins",
            "113-java-maven-documentation",
            "121-java-object-oriented-design",
            "122-java-type-design",
            "123-java-general-guidelines",
            "124-java-secure-coding",
            "125-java-concurrency",
            "126-java-logging",
            "127-java-functional-exception-handling",
            "128-java-generics",
            "131-java-unit-testing",
            "141-java-refactoring-with-modern-features",
            "142-java-functional-programming",
            "143-java-data-oriented-programming",
            "151-java-performance-jmeter",
            "161-java-profiling-detect",
            "162-java-profiling-analyze",
            "164-java-profiling-compare",
            "170-java-documentation"
        );
    }

    public static Stream<String> xmlFilenames() {
        return baseNames().map(name -> name + ".xml");
    }
}
