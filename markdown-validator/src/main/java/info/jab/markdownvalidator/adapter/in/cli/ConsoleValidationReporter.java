package info.jab.markdownvalidator.adapter.in.cli;

import info.jab.markdownvalidator.domain.ValidationError;
import info.jab.markdownvalidator.domain.ValidationReport;
import java.io.PrintStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

final class ConsoleValidationReporter {

    private final PrintStream out;

    ConsoleValidationReporter(PrintStream out) {
        this.out = out;
    }

    void print(ValidationReport report) {
        out.printf("📄 Found %d markdown files to validate%n", report.documentsFound());
        report.results().stream().flatMap(result -> result.messages().stream()).forEach(out::println);

        out.println();
        out.println("=".repeat(60));

        if (report.passed()) {
            out.printf("✅ All markdown files are valid! documents_validated=%d%n", report.documentsValidated());
            out.printf("VALIDATION_RESULT status=passed errors=0 documents_validated=%d%n", report.documentsValidated());
        } else {
            printFailures(report);
        }

        out.println("=".repeat(60));
    }

    private void printFailures(ValidationReport report) {
        List<ValidationError> errors = report.errors();
        out.printf("❌ Found %d validation errors.%n%n", errors.size());
        out.println("Human-readable report:");
        out.println("Review each file and reason below, then rerun the validator.");
        out.println();

        Map<Path, List<ValidationError>> errorsByFile = new LinkedHashMap<>();
        for (ValidationError error : errors) {
            errorsByFile.computeIfAbsent(error.file(), ignored -> new ArrayList<>()).add(error);
        }

        for (Map.Entry<Path, List<ValidationError>> entry : errorsByFile.entrySet()) {
            out.printf("📄 %s:%n", entry.getKey());
            for (ValidationError error : entry.getValue()) {
                if (error.lineNumber() > 0) {
                    out.printf("   Line %d: %s%n", error.lineNumber(), error.message());
                } else {
                    out.printf("   %s%n", error.message());
                }
            }
            out.println();
        }

        out.println("Agent-readable issue records:");
        errors.forEach(error -> out.printf(
                "MARKDOWN_VALIDATION_ISSUE file=\"%s\" line=%d reason=\"%s\"%n",
                escapeConsoleValue(error.file().toString()),
                error.lineNumber(),
                escapeConsoleValue(error.message())));
        out.println();
        out.printf(
                "VALIDATION_RESULT status=failed errors=%d documents_validated=%d%n",
                errors.size(),
                report.documentsValidated());
    }

    private static String escapeConsoleValue(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"").replace("\r", "\\r").replace("\n", "\\n");
    }
}
