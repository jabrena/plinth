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

package info.jab.markdownvalidator;

import info.jab.markdownvalidator.adapter.in.cli.MarkdownValidatorCommand;
import picocli.CommandLine;

public final class MarkdownValidator {

    private MarkdownValidator() {
    }

    public static void main(String... args) {
        int exitCode = new CommandLine(new MarkdownValidatorCommand()).execute(args);
        System.exit(exitCode);
    }
}
