///usr/bin/env jbang "$0" "$@" ; exit $?

//DEPS info.picocli:picocli:4.7.5
//DEPS org.commonmark:commonmark:0.21.0

import org.commonmark.node.*;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.stream.Stream;

@Command(name = "markdown-validator", 
         mixinStandardHelpOptions = true, 
         version = "1.0",
         description = "Validates markdown files from specified directories")
public class MarkdownValidator implements Callable<Integer> {

    @Option(names = {"-v", "--verbose"}, description = "Enable verbose output")
    boolean verbose;

    @Option(names = {"-f", "--fail-fast"}, description = "Stop on first validation error")
    boolean failFast;

    @Option(names = {"-d", "--directories"}, 
            description = "Directories to scan for markdown files (default: .cursor/rules,.cursor/rules/templates)",
            split = ",")
    List<String> targetDirectories = List.of(".cursor/rules", ".cursor/rules/templates");

    @Parameters(description = "Root directory to scan (default: current directory)")
    String rootDir = ".";

    private static final List<String> MARKDOWN_EXTENSIONS = List.of(".md", ".mdc");

    private final Parser parser;
    private final HtmlRenderer renderer;
    private final List<ValidationError> errors = new ArrayList<>();

    public MarkdownValidator() {
        this.parser = Parser.builder().build();
        this.renderer = HtmlRenderer.builder().build();
    }

    public static void main(String... args) {
        int exitCode = new CommandLine(new MarkdownValidator()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() throws Exception {
        System.out.println("🔍 Starting markdown validation...");
        
        Path root = Paths.get(rootDir);
        if (!Files.exists(root)) {
            System.err.println("❌ Root directory does not exist: " + root);
            return 1;
        }

        List<Path> markdownFiles = findMarkdownFiles(root);
        if (markdownFiles.isEmpty()) {
            System.out.println("⚠️  No markdown files found in target directories");
            return 0;
        }

        System.out.printf("📄 Found %d markdown files to validate\n", markdownFiles.size());

        for (Path file : markdownFiles) {
            validateFile(file);
            if (failFast && !errors.isEmpty()) {
                break;
            }
        }

        printResults();
        return errors.isEmpty() ? 0 : 1;
    }

    private List<Path> findMarkdownFiles(Path root) throws IOException {
        List<Path> files = new ArrayList<>();
        
        for (String targetDir : targetDirectories) {
            Path dir = root.resolve(targetDir);
            if (Files.exists(dir) && Files.isDirectory(dir)) {
                try (Stream<Path> paths = Files.walk(dir)) {
                    paths.filter(Files::isRegularFile)
                         .filter(this::isMarkdownFile)
                         .forEach(files::add);
                }
            } else if (verbose) {
                System.out.printf("⚠️  Directory not found: %s\n", dir);
            }
        }
        
        return files;
    }

    private boolean isMarkdownFile(Path file) {
        String fileName = file.getFileName().toString().toLowerCase();
        return MARKDOWN_EXTENSIONS.stream().anyMatch(fileName::endsWith);
    }

    private void validateFile(Path file) {
        System.out.printf("🔍 Validating: %s\n", file);

        try {
            String content = Files.readString(file);
            validateContent(file, content);
        } catch (IOException e) {
            addError(file, 0, "Failed to read file: " + e.getMessage());
        }
    }

    private void validateContent(Path file, String content) {
        try {
            // Parse markdown content
            Node document = parser.parse(content);
            
            // Try to render to HTML to validate structure
            String html = renderer.render(document);
            
            // Assert that HTML output is not null
            if (html == null) {
                addError(file, 0, "HTML rendering produced null output");
                return;
            }
            
            if (verbose) {
                System.out.printf("✅ Successfully parsed: %s (%d characters, HTML: %d characters)\n", 
                    file.getFileName(), content.length(), html.length());
            }
        } catch (Exception e) {
            addError(file, 0, "Failed to parse markdown: " + e.getMessage());
        }
    }

    private void addError(Path file, int lineNumber, String message) {
        errors.add(new ValidationError(file, lineNumber, message));
        if (verbose) {
            System.out.printf("❌ %s:%d - %s\n", file, lineNumber, message);
        }
    }

    private void printResults() {
        System.out.println("\n" + "=".repeat(60));
        
        if (errors.isEmpty()) {
            System.out.println("✅ All markdown files are valid!");
        } else {
            System.out.printf("❌ Found %d validation errors:\n\n", errors.size());
            
            Map<Path, List<ValidationError>> errorsByFile = new LinkedHashMap<>();
            for (ValidationError error : errors) {
                errorsByFile.computeIfAbsent(error.file, k -> new ArrayList<>()).add(error);
            }
            
            for (Map.Entry<Path, List<ValidationError>> entry : errorsByFile.entrySet()) {
                System.out.printf("📄 %s:\n", entry.getKey());
                for (ValidationError error : entry.getValue()) {
                    if (error.lineNumber > 0) {
                        System.out.printf("   Line %d: %s\n", error.lineNumber, error.message);
                    } else {
                        System.out.printf("   %s\n", error.message);
                    }
                }
                System.out.println();
            }
        }
        
        System.out.println("=".repeat(60));
    }

    private static class ValidationError {
        final Path file;
        final int lineNumber;
        final String message;

        ValidationError(Path file, int lineNumber, String message) {
            this.file = file;
            this.lineNumber = lineNumber;
            this.message = message;
        }
    }
}