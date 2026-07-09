package info.jab.mv.adapter.in.cli;

import info.jab.mv.domain.ValidationReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class ConsoleValidationReporter {

    private static final Logger logger = LoggerFactory.getLogger(ConsoleValidationReporter.class);

    void print(ValidationReport report) {
        report.results().stream()
                .flatMap(result -> result.messages().stream())
                .forEach(message -> logger.debug("event=markdown.validation.detail message=\"{}\"", escapeLogValue(message)));

        if (report.passed()) {
            logger.info(
                    "event=markdown.validation.completed status=passed documentsFound={} documentsValidated={} errors=0",
                    report.documentsFound(),
                    report.documentsValidated());
        } else {
            logFailures(report);
        }
    }

    private void logFailures(ValidationReport report) {
        logger.error(
                "event=markdown.validation.completed status=failed documentsFound={} documentsValidated={} errors={}",
                report.documentsFound(),
                report.documentsValidated(),
                report.errors().size());
        report.errors().forEach(error -> logger.error(
                "event=markdown.validation.issue file=\"{}\" line={} reason=\"{}\"",
                escapeLogValue(error.file().toString()),
                error.lineNumber(),
                escapeLogValue(error.message())));
    }

    private static String escapeLogValue(String value) {
        return value.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\r", "\\r")
                .replace("\n", "\\n")
                .replace("\t", "\\t");
    }
}
