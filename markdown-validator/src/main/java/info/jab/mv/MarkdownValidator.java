///usr/bin/env jbang "$0" "$@" ; exit $?

//JAVA 25
//JAVAC_OPTIONS --enable-preview --release 25
//JAVA_OPTIONS --enable-preview -Dlogback.configurationFile=markdown-validator/src/main/resources/logback.xml
//DEPS info.picocli:picocli:4.7.5
//DEPS org.commonmark:commonmark:0.21.0
//DEPS org.slf4j:slf4j-api:2.0.18
//DEPS ch.qos.logback:logback-classic:1.5.36
//SOURCES application/MarkdownDocumentValidator.java
//SOURCES application/MarkdownValidationService.java
//SOURCES application/RemoteLinkValidator.java
//SOURCES application/StructuredValidationRunner.java
//SOURCES application/port/MarkdownFileFinder.java
//SOURCES application/port/RemoteLinkRequester.java
//SOURCES application/port/RemoteLinkResponse.java
//SOURCES domain/FileValidationResult.java
//SOURCES domain/ParseSummary.java
//SOURCES domain/ValidationError.java
//SOURCES domain/ValidationReport.java
//SOURCES adapter/in/cli/ConsoleValidationReporter.java
//SOURCES adapter/in/cli/MarkdownValidatorCommand.java
//SOURCES adapter/out/filesystem/FileSystemMarkdownFileFinder.java
//SOURCES adapter/out/http/HttpClientRemoteLinkRequester.java

package info.jab.mv;

import info.jab.mv.adapter.in.cli.MarkdownValidatorCommand;
import info.jab.mv.adapter.out.filesystem.FileSystemMarkdownFileFinder;
import info.jab.mv.adapter.out.http.HttpClientRemoteLinkRequester;
import info.jab.mv.application.MarkdownDocumentValidator;
import info.jab.mv.application.MarkdownValidationService;
import info.jab.mv.application.RemoteLinkValidator;
import info.jab.mv.application.StructuredValidationRunner;
import java.time.Duration;
import picocli.CommandLine;

public final class MarkdownValidator {

    private static final Duration LINK_CHECK_TIMEOUT = Duration.ofSeconds(10);

    private MarkdownValidator() {
    }

    public static void main(String... args) {
        int exitCode = new CommandLine(new MarkdownValidatorCommand(createValidationService())).execute(args);
        System.exit(exitCode);
    }

    private static MarkdownValidationService createValidationService() {
        RemoteLinkValidator remoteLinkValidator =
                new RemoteLinkValidator(new HttpClientRemoteLinkRequester(LINK_CHECK_TIMEOUT), LINK_CHECK_TIMEOUT);
        MarkdownDocumentValidator documentValidator = new MarkdownDocumentValidator(remoteLinkValidator);
        return new MarkdownValidationService(new FileSystemMarkdownFileFinder(), new StructuredValidationRunner(documentValidator));
    }
}
