package info.jab.markdownvalidator.adapter.in.cli;

import info.jab.markdownvalidator.adapter.out.filesystem.FileSystemMarkdownFileFinder;
import info.jab.markdownvalidator.adapter.out.http.HttpClientRemoteLinkRequester;
import info.jab.markdownvalidator.application.MarkdownDocumentValidator;
import info.jab.markdownvalidator.application.MarkdownValidationService;
import info.jab.markdownvalidator.application.RemoteLinkValidator;
import info.jab.markdownvalidator.application.StructuredValidationRunner;
import info.jab.markdownvalidator.application.port.RemoteLinkRequester;
import info.jab.markdownvalidator.domain.ValidationReport;
import java.nio.file.Path;
import java.time.Duration;
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
    static final Duration LINK_CHECK_TIMEOUT = Duration.ofSeconds(10);

    @Option(names = { "-v", "--verbose" }, description = "Enable verbose output")
    boolean verbose;

    @Option(
            names = { "-d", "--directories" },
            description = "Directories to scan for markdown files (default: .cursor/rules,skills,.cursor/agents)",
            split = ",")
    List<String> targetDirectories = DEFAULT_TARGET_DIRECTORIES;

    @Parameters(arity = "0..1", defaultValue = ".", description = "Root directory to scan (default: current directory)")
    String rootDir = ".";

    private final MarkdownValidationService validationService;

    public MarkdownValidatorCommand() {
        this(new HttpClientRemoteLinkRequester(LINK_CHECK_TIMEOUT));
    }

    MarkdownValidatorCommand(RemoteLinkRequester remoteLinkRequester) {
        this.validationService = createValidationService(remoteLinkRequester);
    }

    @Override
    public Integer call() throws Exception {
        Path root = Path.of(rootDir);
        logger.info("event=markdown.validation.started root={} targetDirectories={}", root, targetDirectories);

        ValidationReport report = validationService.validate(root, targetDirectories, verbose);

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

    private MarkdownValidationService createValidationService(RemoteLinkRequester remoteLinkRequester) {
        RemoteLinkValidator remoteLinkValidator = new RemoteLinkValidator(remoteLinkRequester, LINK_CHECK_TIMEOUT);
        MarkdownDocumentValidator documentValidator = new MarkdownDocumentValidator(remoteLinkValidator);
        return new MarkdownValidationService(new FileSystemMarkdownFileFinder(), new StructuredValidationRunner(documentValidator));
    }
}
