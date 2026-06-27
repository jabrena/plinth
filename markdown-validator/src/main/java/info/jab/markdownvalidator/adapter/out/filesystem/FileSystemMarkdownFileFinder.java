package info.jab.markdownvalidator.adapter.out.filesystem;

import info.jab.markdownvalidator.application.port.MarkdownFileFinder;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public final class FileSystemMarkdownFileFinder implements MarkdownFileFinder {

    private static final List<String> MARKDOWN_EXTENSIONS = List.of(".md");

    private final PrintStream out;

    public FileSystemMarkdownFileFinder(PrintStream out) {
        this.out = out;
    }

    @Override
    public List<Path> findMarkdownFiles(Path root, List<String> targetDirectories, boolean verbose) throws IOException {
        try (Stream<Path> paths = targetDirectories.stream().flatMap(targetDir -> walkTarget(root, targetDir, verbose))) {
            return paths.filter(Files::isRegularFile)
                    .filter(FileSystemMarkdownFileFinder::isMarkdownFile)
                    .sorted(Comparator.comparing(Path::toString))
                    .toList();
        }
    }

    private Stream<Path> walkTarget(Path root, String targetDir, boolean verbose) {
        Path dir = root.resolve(targetDir);
        if (!Files.isDirectory(dir)) {
            if (verbose) {
                out.printf("⚠️  Directory not found: %s%n", dir);
            }
            return Stream.empty();
        }
        try {
            return Files.walk(dir);
        } catch (IOException e) {
            return Stream.of(dir);
        }
    }

    private static boolean isMarkdownFile(Path file) {
        String fileName = file.getFileName().toString().toLowerCase();
        return MARKDOWN_EXTENSIONS.stream().anyMatch(fileName::endsWith);
    }
}
