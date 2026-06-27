package info.jab.markdownvalidator;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import info.jab.markdownvalidator.adapter.in.cli.MarkdownValidatorCommand;
import info.jab.markdownvalidator.adapter.out.filesystem.FileSystemMarkdownFileFinder;
import info.jab.markdownvalidator.adapter.out.http.HttpClientRemoteLinkRequester;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.onionArchitecture;

@AnalyzeClasses(packages = "info.jab.markdownvalidator", importOptions = ImportOption.DoNotIncludeTests.class)
class ArchitectureTest {

    @ArchTest
    static final ArchRule onion_architecture_boundaries = onionArchitecture()
            .domainModels("info.jab.markdownvalidator.domain..")
            .applicationServices("info.jab.markdownvalidator.application..")
            .adapter("cli", "info.jab.markdownvalidator.adapter.in.cli..")
            .adapter("filesystem", "info.jab.markdownvalidator.adapter.out.filesystem..")
            .adapter("http", "info.jab.markdownvalidator.adapter.out.http..")
            .withOptionalLayers(true)
            .ignoreDependency(
                    "info.jab.markdownvalidator.MarkdownValidator",
                    "info.jab.markdownvalidator.adapter.in.cli.MarkdownValidatorCommand")
            .ignoreDependency(MarkdownValidatorCommand.class, FileSystemMarkdownFileFinder.class)
            .ignoreDependency(MarkdownValidatorCommand.class, HttpClientRemoteLinkRequester.class);

    @ArchTest
    static final ArchRule application_does_not_depend_on_adapters = noClasses()
            .that()
            .resideInAPackage("info.jab.markdownvalidator.application..")
            .should()
            .dependOnClassesThat()
            .resideInAPackage("info.jab.markdownvalidator.adapter..");
}
