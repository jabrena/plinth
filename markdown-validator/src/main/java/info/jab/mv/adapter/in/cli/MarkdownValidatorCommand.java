package info.jab.mv.adapter.in.cli;

import info.jab.mv.application.MarkdownValidationService;
import info.jab.mv.domain.ValidationReport;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.Callable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(
        name = "markdown-validator",
        mixinStandardHelpOptions = true,
        version = "1.0",
        description = "Validates markdown files from specified directories")
public class MarkdownValidatorCommand implements Callable<Integer> {

    private static final Logger logger = LoggerFactory.getLogger(MarkdownValidatorCommand.class);
    static final List<String> DEFAULT_TARGET_DIRECTORIES = List.of(".cursor/rules", "skills", ".cursor/agents");

    @Option(
            names = { "-d", "--directories" },
            description = "Directories to scan for markdown files (default: .cursor/rules,skills,.cursor/agents)",
            split = ",")
    List<String> targetDirectories = DEFAULT_TARGET_DIRECTORIES;

    @Parameters(arity = "0..1", defaultValue = ".", description = "Root directory to scan (default: current directory)")
    String rootDir = ".";

    private final MarkdownValidationService validationService;

    public MarkdownValidatorCommand(MarkdownValidationService validationService) {
        this.validationService = validationService;
    }

    @Override
    public Integer call() throws Exception {
        Path root = Path.of(rootDir);
        logger.info("event=markdown.validation.started root={} targetDirectories={}", root, targetDirectories);

        ValidationReport report = validationService.validate(root, targetDirectories);

        if (report.rootMissing()) {
            logger.error("event=markdown.validation.root_missing root={}", root);
            return 1;
        }
        if (report.noMarkdownFiles()) {
            logger.info("event=markdown.validation.no_files root={} targetDirectories={}", root, targetDirectories);
            return 0;
        }

        new ConsoleValidationReporter().print(report);
        return report.passed() ? 0 : 1;
    }
}
