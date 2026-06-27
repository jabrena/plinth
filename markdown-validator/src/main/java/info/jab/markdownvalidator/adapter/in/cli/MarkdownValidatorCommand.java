package info.jab.markdownvalidator.adapter.in.cli;

import info.jab.markdownvalidator.adapter.out.filesystem.FileSystemMarkdownFileFinder;
import info.jab.markdownvalidator.adapter.out.http.HttpClientRemoteLinkRequester;
import info.jab.markdownvalidator.application.MarkdownDocumentValidator;
import info.jab.markdownvalidator.application.MarkdownValidationService;
import info.jab.markdownvalidator.application.RemoteLinkValidator;
import info.jab.markdownvalidator.application.SequentialValidationRunner;
import info.jab.markdownvalidator.application.StructuredValidationRunner;
import info.jab.markdownvalidator.application.port.RemoteLinkRequester;
import info.jab.markdownvalidator.domain.ValidationReport;
import java.io.PrintStream;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.Callable;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(
        name = "markdown-validator",
        mixinStandardHelpOptions = true,
        version = "1.0",
        description = "Validates markdown files from specified directories")
public class MarkdownValidatorCommand implements Callable<Integer> {

    static final List<String> DEFAULT_TARGET_DIRECTORIES = List.of(".cursor/rules", "skills", ".cursor/agents");
    static final Duration LINK_CHECK_TIMEOUT = Duration.ofSeconds(10);

    @Option(names = { "-v", "--verbose" }, description = "Enable verbose output")
    boolean verbose;

    @Option(
            names = { "-f", "--fail-fast" },
            description = "Stop on first validation error")
    boolean failFast;

    @Option(
            names = { "-d", "--directories" },
            description = "Directories to scan for markdown files (default: .cursor/rules,skills,.cursor/agents)",
            split = ",")
    List<String> targetDirectories = DEFAULT_TARGET_DIRECTORIES;

    @Parameters(arity = "0..1", defaultValue = ".", description = "Root directory to scan (default: current directory)")
    String rootDir = ".";

    private final MarkdownValidationService validationService;
    private final PrintStream out;
    private final PrintStream err;

    public MarkdownValidatorCommand() {
        this(
                new HttpClientRemoteLinkRequester(LINK_CHECK_TIMEOUT),
                Math.max(1, Math.min(Runtime.getRuntime().availableProcessors(), 8)),
                System.out,
                System.err);
    }

    MarkdownValidatorCommand(RemoteLinkRequester remoteLinkRequester, int parallelism, PrintStream out, PrintStream err) {
        this.out = out;
        this.err = err;
        this.validationService = createValidationService(remoteLinkRequester, parallelism, out);
    }

    public static void main(String... args) {
        int exitCode = new CommandLine(new MarkdownValidatorCommand()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() throws Exception {
        Path root = Path.of(rootDir);
        out.println("🔍 Starting markdown validation...");

        ValidationReport report = validationService.validate(root, targetDirectories, failFast, verbose);

        if (report.rootMissing()) {
            err.println("❌ Root directory does not exist: " + root);
            return 1;
        }
        if (report.noMarkdownFiles()) {
            out.println("⚠️  No markdown files found in target directories");
            return 0;
        }

        new ConsoleValidationReporter(out).print(report);
        return report.passed() ? 0 : 1;
    }

    private MarkdownValidationService createValidationService(
            RemoteLinkRequester remoteLinkRequester, int parallelism, PrintStream out) {
        RemoteLinkValidator remoteLinkValidator = new RemoteLinkValidator(remoteLinkRequester, LINK_CHECK_TIMEOUT);
        MarkdownDocumentValidator documentValidator = new MarkdownDocumentValidator(remoteLinkValidator);
        return new MarkdownValidationService(
                new FileSystemMarkdownFileFinder(out),
                new SequentialValidationRunner(documentValidator),
                new StructuredValidationRunner(documentValidator, parallelism));
    }
}
