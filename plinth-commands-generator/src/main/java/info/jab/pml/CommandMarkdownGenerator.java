package info.jab.pml;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.w3c.dom.Document;

/**
 * CLI entry point that converts command XML sources under a directory into
 * Cursor-compatible Markdown files for installers and the skills bridge.
 *
 * <p>Transformation uses classpath stylesheet {@code command-to-markdown.xsl}
 * (see {@link CommandMarkdownRenderer}). The XML directory is read-only input;
 * generated Markdown is written only under {@code target/}.
 *
 * <p>Usage: {@code CommandMarkdownGenerator <commandsXmlDir> <outputDir> [extraOutputDir...]}
 */
public final class CommandMarkdownGenerator {

    private CommandMarkdownGenerator() {}

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            throw new IllegalArgumentException(
                "Usage: CommandMarkdownGenerator <commandsXmlDir> <outputDir> [extraOutputDir...]"
            );
        }
        Path commandsXmlDir = Path.of(args[0]).toAbsolutePath().normalize();
        if (!Files.isDirectory(commandsXmlDir)) {
            throw new IllegalArgumentException("Commands XML directory not found: " + commandsXmlDir);
        }

        List<Path> outputDirs = new ArrayList<>();
        for (int i = 1; i < args.length; i++) {
            outputDirs.add(Path.of(args[i]).toAbsolutePath().normalize());
        }

        List<Path> xmlFiles = listCommandXmlFiles(commandsXmlDir);
        if (xmlFiles.isEmpty()) {
            throw new IllegalStateException("No command XML files found in " + commandsXmlDir);
        }

        for (Path xmlFile : xmlFiles) {
            String markdown = renderFile(xmlFile);
            String mdFileName = toMarkdownFileName(xmlFile.getFileName().toString());
            for (Path outputDir : outputDirs) {
                Files.createDirectories(outputDir);
                Path target = outputDir.resolve(mdFileName);
                Files.writeString(target, markdown, StandardCharsets.UTF_8);
            }
            System.out.println("Generated " + mdFileName + " from " + xmlFile.getFileName());
        }
    }

    static String renderFile(Path xmlFile) throws Exception {
        try (InputStream in = Files.newInputStream(xmlFile)) {
            Document document = InventoryXmlLoader.parse(in);
            return CommandMarkdownRenderer.render(document);
        }
    }

    static String toMarkdownFileName(String xmlFileName) {
        if (!xmlFileName.endsWith(".xml")) {
            throw new IllegalArgumentException("Expected .xml file name: " + xmlFileName);
        }
        return xmlFileName.substring(0, xmlFileName.length() - ".xml".length()) + ".md";
    }

    private static List<Path> listCommandXmlFiles(Path commandsDir) throws IOException {
        List<Path> files = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(commandsDir, "*.xml")) {
            for (Path path : stream) {
                if (Files.isRegularFile(path)) {
                    files.add(path);
                }
            }
        }
        files.sort(Comparator.comparing(path -> path.getFileName().toString()));
        return List.copyOf(files);
    }
}
