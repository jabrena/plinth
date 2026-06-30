package info.jab.markdownvalidator;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.library.Architectures.onionArchitecture;
import static org.assertj.core.api.Assertions.assertThat;

@AnalyzeClasses(
        packages = {
            "info.jab.markdownvalidator.adapter",
            "info.jab.markdownvalidator.application",
            "info.jab.markdownvalidator.domain"
        },
        importOptions = ImportOption.DoNotIncludeTests.class)
class ArchitectureTest {

    @ArchTest
    static final ArchRule onion_architecture_boundaries = onionArchitecture()
            .domainModels("info.jab.markdownvalidator.domain..")
            .applicationServices("info.jab.markdownvalidator.application..")
            .adapter("cli", "info.jab.markdownvalidator.adapter.in.cli..")
            .adapter("filesystem", "info.jab.markdownvalidator.adapter.out.filesystem..")
            .adapter("http", "info.jab.markdownvalidator.adapter.out.http..")
            .withOptionalLayers(true);

    @Test
    void markdownValidatorPackage_containsOnlyExpectedScaffoldPackages() throws Exception {
        try (var paths = Files.list(Path.of("src/main/java/info/jab/markdownvalidator"))) {
            var packageDirectories = paths.filter(Files::isDirectory)
                    .map(path -> path.getFileName().toString())
                    .toList();

            assertThat(packageDirectories).containsExactlyInAnyOrder("adapter", "application", "domain");
        }
    }
}
