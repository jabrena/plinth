package info.jab.markdownvalidator.adapter.out.filesystem;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
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
        FileSystemMarkdownFileFinder finder =
                new FileSystemMarkdownFileFinder(new PrintStream(OutputStream.nullOutputStream()));

        List<Path> files = finder.findMarkdownFiles(tempDir, List.of("docs"), false);

        assertThat(files).containsExactly(b, a);
    }

    @Test
    void findMarkdownFiles_reportsMissingDirectoriesOnlyInVerboseMode() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        FileSystemMarkdownFileFinder finder =
                new FileSystemMarkdownFileFinder(new PrintStream(output, true, StandardCharsets.UTF_8));

        List<Path> files = finder.findMarkdownFiles(tempDir, List.of("missing"), true);

        assertThat(files).isEmpty();
        assertThat(output.toString(StandardCharsets.UTF_8)).contains("Directory not found");
    }
}
