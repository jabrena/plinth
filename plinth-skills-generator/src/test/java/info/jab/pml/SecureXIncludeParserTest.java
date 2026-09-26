package info.jab.pml;

import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Secure XInclude Parser Tests")
class SecureXIncludeParserTest {

    private static final String SECRET = "TOP-SECRET-RUNNER-FILE-CONTENT";

    @TempDir
    Path tempDir;

    private Path resourceRoot;
    private Path secretFile;

    @BeforeEach
    void setUp() throws Exception {
        resourceRoot = Files.createDirectories(tempDir.resolve("resources"));
        Files.createDirectories(resourceRoot.resolve("skill-references/assets"));
        Files.writeString(resourceRoot.resolve("skill-references/assets/template.md"), "template body");
        secretFile = Files.writeString(tempDir.resolve("secret.txt"), SECRET);
    }

    @Test
    @DisplayName("Should resolve XInclude inside the resource root")
    void should_resolveXInclude_when_hrefIsInsideRoot() throws Exception {
        Document document = parse("""
            <reference xmlns:xi="http://www.w3.org/2001/XInclude">
              <description><xi:include href="assets/template.md" parse="text"/></description>
            </reference>
            """);

        assertThat(document.getDocumentElement().getTextContent()).contains("template body");
    }

    @Test
    @DisplayName("Should reject DOCTYPE external entities")
    void should_rejectDoctype_when_xxePayloadIsProvided() {
        String xml = """
            <?xml version="1.0"?>
            <!DOCTYPE reference [
              <!ENTITY xxe SYSTEM "%s">
            ]>
            <reference><description>&xxe;</description></reference>
            """.formatted(secretFile.toUri());

        assertThatThrownBy(() -> parse(xml)).hasMessageContaining("DOCTYPE");
    }

    @Test
    @DisplayName("Should reject XInclude of an absolute file outside the resource root")
    void should_rejectXInclude_when_hrefIsAbsoluteFileOutsideRoot() {
        String xml = """
            <reference xmlns:xi="http://www.w3.org/2001/XInclude">
              <description><xi:include href="%s" parse="text"/></description>
            </reference>
            """.formatted(secretFile.toUri());

        assertRejectedWithoutLeak(xml);
    }

    @Test
    @DisplayName("Should reject XInclude that escapes the resource root with parent segments")
    void should_rejectXInclude_when_hrefTraversesOutsideRoot() {
        String xml = """
            <reference xmlns:xi="http://www.w3.org/2001/XInclude">
              <description><xi:include href="../../secret.txt" parse="text"/></description>
            </reference>
            """;

        assertRejectedWithoutLeak(xml);
    }

    @Test
    @DisplayName("Should reject XInclude of remote URLs")
    void should_rejectXInclude_when_hrefIsRemote() {
        String xml = """
            <reference xmlns:xi="http://www.w3.org/2001/XInclude">
              <description><xi:include href="http://169.254.169.254/latest/meta-data/" parse="text"/></description>
            </reference>
            """;

        assertThatThrownBy(() -> parse(xml)).hasStackTraceContaining("Blocked XML resource");
    }

    private void assertRejectedWithoutLeak(String xml) {
        assertThatThrownBy(() -> parse(xml))
            .hasStackTraceContaining("Blocked XML resource")
            .satisfies(e -> assertThat(e).hasMessageNotContaining(SECRET));
    }

    private Document parse(String xml) throws Exception {
        InputSource source = new InputSource(new StringReader(xml));
        source.setSystemId(resourceRoot.resolve("skill-references").toUri().toString());
        return SecureXIncludeParser.parse(source, resourceRoot.toUri().toString());
    }
}
