///usr/bin/env jbang "$0" "$@" ; exit $?

//DEPS info.picocli:picocli:4.7.5
//DEPS org.commonmark:commonmark:0.21.0
//DEPS org.commonmark:commonmark-ext-gfm-tables:0.21.0

import org.commonmark.node.*;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.commonmark.ext.gfm.tables.TablesExtension;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.regex.Pattern;
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
    private final List<ValidationError> errors = new ArrayList<>();

    public MarkdownValidator() {
        this.parser = Parser.builder()
            .extensions(Collections.singletonList(TablesExtension.create()))
            .build();
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
        // Parse with CommonMark
        Node document = parser.parse(content);
        
        // Validate structure
        validateDocumentStructure(file, document);
        
        // Validate content patterns
        validateContentPatterns(file, content);
        
        // Validate links and references
        validateLinks(file, content, document);
        
        // Validate tables
        validateTables(file, document);
        
        // Validate code blocks
        validateCodeBlocks(file, document);
    }

    private void validateDocumentStructure(Path file, Node document) {
        boolean hasHeading = false;
        int headingLevel = 0;
        
        Node walker = document.getFirstChild();
        while (walker != null) {
            if (walker instanceof Heading) {
                Heading heading = (Heading) walker;
                hasHeading = true;
                
                // Check heading hierarchy
                if (headingLevel == 0) {
                    if (heading.getLevel() != 1) {
                        addError(file, 0, "Document should start with H1 heading, found H" + heading.getLevel());
                    }
                }
                headingLevel = heading.getLevel();
                
                // Check empty headings
                if (getTextContent(heading).trim().isEmpty()) {
                    addError(file, 0, "Empty heading found");
                }
            }
            walker = walker.getNext();
        }
        
        if (!hasHeading) {
            addError(file, 0, "Document should contain at least one heading");
        }
    }

    private void validateContentPatterns(Path file, String content) {
        String[] lines = content.split("\n");
        
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            int lineNum = i + 1;
            
            // Check trailing whitespace
            if (line.endsWith(" ") || line.endsWith("\t")) {
                addError(file, lineNum, "Line has trailing whitespace");
            }
            
            // Check for TODO/FIXME comments that might indicate incomplete content
            if (line.toUpperCase().contains("TODO") || line.toUpperCase().contains("FIXME")) {
                addError(file, lineNum, "TODO/FIXME found - content may be incomplete");
            }
            
            // Check for broken reference-style links
            if (line.matches(".*\\[.*\\]\\[.*\\].*") && !content.contains(line.replaceAll(".*\\[.*\\]\\[(.*)\\].*", "$1") + ":")) {
                addError(file, lineNum, "Possible broken reference-style link");
            }
        }
    }

    private void validateLinks(Path file, String content, Node document) {
        // Pattern for markdown links
        Pattern linkPattern = Pattern.compile("\\[([^\\]]+)\\]\\(([^\\)]+)\\)");
        var matcher = linkPattern.matcher(content);
        
        while (matcher.find()) {
            String linkText = matcher.group(1);
            String linkUrl = matcher.group(2);
            
            // Check for empty link text
            if (linkText.trim().isEmpty()) {
                addError(file, 0, "Empty link text found");
            }
            
            // Check for relative file links
            if (linkUrl.startsWith(".") || (!linkUrl.startsWith("http") && !linkUrl.startsWith("#"))) {
                Path targetPath = file.getParent().resolve(linkUrl);
                if (!Files.exists(targetPath)) {
                    addError(file, 0, "Broken relative link: " + linkUrl);
                }
            }
            
            // Check for cursor rule references
            if (linkUrl.startsWith("mdc:") || linkUrl.contains(".mdc")) {
                String referencedFile = linkUrl.replace("mdc:", "");
                Path cursorRulePath = Paths.get(rootDir).resolve(referencedFile);
                if (!Files.exists(cursorRulePath)) {
                    addError(file, 0, "Broken cursor rule reference: " + linkUrl);
                }
            }
        }
    }

    private void validateTables(Path file, Node document) {
        Node walker = document.getFirstChild();
        while (walker != null) {
            if (walker.getClass().getSimpleName().equals("TableBlock")) {
                validateTableBlock(file, walker);
            }
            walker = walker.getNext();
        }
    }

    private void validateTableBlock(Path file, Node tableBlock) {
        // Count rows and check consistency
        int columnCount = -1;
        Node row = tableBlock.getFirstChild();
        int rowIndex = 0;
        
        while (row != null) {
            if (row.getClass().getSimpleName().equals("TableRow")) {
                int currentColumnCount = 0;
                Node cell = row.getFirstChild();
                
                while (cell != null) {
                    currentColumnCount++;
                    
                    // Check for empty cells
                    String cellContent = getTextContent(cell).trim();
                    if (cellContent.isEmpty() && rowIndex > 0) { // Allow empty header cells
                        addError(file, 0, "Empty table cell found in row " + (rowIndex + 1));
                    }
                    
                    cell = cell.getNext();
                }
                
                if (columnCount == -1) {
                    columnCount = currentColumnCount;
                } else if (columnCount != currentColumnCount) {
                    addError(file, 0, "Inconsistent table column count. Expected " + columnCount + ", found " + currentColumnCount + " in row " + (rowIndex + 1));
                }
                
                rowIndex++;
            }
            row = row.getNext();
        }
    }

    private void validateCodeBlocks(Path file, Node document) {
        Node walker = document.getFirstChild();
        while (walker != null) {
            if (walker instanceof FencedCodeBlock) {
                FencedCodeBlock codeBlock = (FencedCodeBlock) walker;
                String info = codeBlock.getInfo();
                String literal = codeBlock.getLiteral();
                
                // Check for empty code blocks
                if (literal == null || literal.trim().isEmpty()) {
                    addError(file, 0, "Empty code block found");
                }
                
                // Validate language specification for syntax highlighting
                if (info != null && !info.trim().isEmpty()) {
                    String language = info.trim().split("\\s+")[0];
                    if (!isValidLanguage(language)) {
                        addError(file, 0, "Unknown language specification: " + language);
                    }
                }
            } else if (walker instanceof IndentedCodeBlock) {
                IndentedCodeBlock codeBlock = (IndentedCodeBlock) walker;
                if (codeBlock.getLiteral() == null || codeBlock.getLiteral().trim().isEmpty()) {
                    addError(file, 0, "Empty indented code block found");
                }
            }
            walker = walker.getNext();
        }
    }

    private boolean isValidLanguage(String language) {
        // Common programming languages and markup formats
        Set<String> validLanguages = Set.of(
            "java", "javascript", "python", "bash", "shell", "sh", "xml", "json", "yaml", "yml",
            "html", "css", "markdown", "md", "sql", "dockerfile", "properties", "ini", "text",
            "plaintext", "console", "log", "diff", "makefile", "gradle", "maven", "pom"
        );
        return validLanguages.contains(language.toLowerCase());
    }

    private String getTextContent(Node node) {
        StringBuilder sb = new StringBuilder();
        Node walker = node.getFirstChild();
        while (walker != null) {
            if (walker instanceof Text) {
                sb.append(((Text) walker).getLiteral());
            } else if (walker.getFirstChild() != null) {
                sb.append(getTextContent(walker));
            }
            walker = walker.getNext();
        }
        return sb.toString();
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