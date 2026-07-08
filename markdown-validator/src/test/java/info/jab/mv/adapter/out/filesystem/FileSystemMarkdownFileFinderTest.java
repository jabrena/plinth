package info.jab.mv.adapter.out.filesystem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.assertj.core.api.Assertions.assertThat;

class FileSystemMarkdownFileFinderTest {

    @TempDir
    Path tempDir;

    @Test
    void findMarkdownFiles_returnsSortedMarkdownFilesOnly() throws IOException {
        Path docs = Files.createDirectories(tempDir.resolve("docs"));
        Path nested = Files.createDirectories(docs.resolve("nested"));
        Path b = Files.writeString(docs.resolve("b.MD"), "# B");
        Files.writeString(docs.resolve("notes.txt"), "not markdown");
        Path a = Files.writeString(nested.resolve("a.md"), "# A");
        FileSystemMarkdownFileFinder finder = new FileSystemMarkdownFileFinder();

        List<Path> files = finder.findMarkdownFiles(tempDir, List.of("docs"));

        assertThat(files).containsExactly(b, a);
    }

    @Test
    void findMarkdownFiles_ignoresMissingDirectories() throws IOException {
        FileSystemMarkdownFileFinder finder = new FileSystemMarkdownFileFinder();

        List<Path> files = finder.findMarkdownFiles(tempDir, List.of("missing"));

        assertThat(files).isEmpty();
    }
}
