package info.jab.mv;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static org.assertj.core.api.Assertions.assertThat;

@AnalyzeClasses(
        packages = {
            "info.jab.mv.adapter",
            "info.jab.mv.application",
            "info.jab.mv.domain"
        },
        importOptions = ImportOption.DoNotIncludeTests.class)
class ArchitectureTest {

    @ArchTest
    static final ArchRule should_keep_application_core_independent_from_adapters = noClasses()
            .that()
            .resideInAnyPackage("info.jab.mv.domain..", "info.jab.mv.application..")
            .should()
            .dependOnClassesThat()
            .resideInAPackage("info.jab.mv.adapter..");

    @ArchTest
    static final ArchRule should_keep_domain_independent_from_application_services = noClasses()
            .that()
            .resideInAPackage("info.jab.mv.domain..")
            .should()
            .dependOnClassesThat()
            .resideInAPackage("info.jab.mv.application..");

    @ArchTest
    static final ArchRule should_keep_driving_adapters_independent_from_driven_adapters = noClasses()
            .that()
            .resideInAPackage("info.jab.mv.adapter.in..")
            .should()
            .dependOnClassesThat()
            .resideInAPackage("info.jab.mv.adapter.out..");

    @ArchTest
    static final ArchRule should_keep_driven_adapters_independent_from_driving_adapters = noClasses()
            .that()
            .resideInAPackage("info.jab.mv.adapter.out..")
            .should()
            .dependOnClassesThat()
            .resideInAPackage("info.jab.mv.adapter.in..");

    @Test
    void shouldContainOnlyExpectedScaffoldPackagesInMarkdownValidatorPackage() throws Exception {
        Path packagePath = Path.of("src/main/java/info/jab/mv");

        Set<String> packageDirectories;
        try (var paths = Files.list(packagePath)) {
            packageDirectories = paths.filter(Files::isDirectory)
                    .map(path -> path.getFileName().toString())
                    .collect(Collectors.toSet());
        }

        assertThat(packageDirectories).containsExactlyInAnyOrder("adapter", "application", "domain");
    }
}
