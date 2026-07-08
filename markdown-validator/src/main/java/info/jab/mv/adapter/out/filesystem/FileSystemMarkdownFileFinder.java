package info.jab.mv.adapter.out.filesystem;

import info.jab.mv.application.port.MarkdownFileFinder;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class FileSystemMarkdownFileFinder implements MarkdownFileFinder {

    private static final Logger logger = LoggerFactory.getLogger(FileSystemMarkdownFileFinder.class);
    private static final List<String> MARKDOWN_EXTENSIONS = List.of(".md");

    public FileSystemMarkdownFileFinder() {
    }

    @Override
    public List<Path> findMarkdownFiles(Path root, List<String> targetDirectories) throws IOException {
        try (Stream<Path> paths = targetDirectories.stream().flatMap(targetDir -> walkTarget(root, targetDir))) {
            return paths.filter(Files::isRegularFile)
                    .filter(FileSystemMarkdownFileFinder::isMarkdownFile)
                    .sorted(Comparator.comparing(Path::toString))
                    .toList();
        }
    }

    private Stream<Path> walkTarget(Path root, String targetDir) {
        Path dir = root.resolve(targetDir);
        if (!Files.isDirectory(dir)) {
            logger.debug("event=markdown.directory_missing path={}", dir);
            return Stream.empty();
        }
        try {
            return Files.walk(dir);
        } catch (IOException e) {
            logger.warn("event=markdown.directory_walk_failed path={}", dir, e);
            return Stream.of(dir);
        }
    }

    private static boolean isMarkdownFile(Path file) {
        String fileName = file.getFileName().toString().toLowerCase();
        return MARKDOWN_EXTENSIONS.stream().anyMatch(fileName::endsWith);
    }
}
