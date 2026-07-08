package info.jab.mv.application.port;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface MarkdownFileFinder {
    List<Path> findMarkdownFiles(Path root, List<String> targetDirectories) throws IOException;
}
