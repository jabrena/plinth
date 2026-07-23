package info.jab.pml;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.snakeyaml.engine.v2.api.Load;
import org.snakeyaml.engine.v2.api.LoadSettings;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Command Frontmatter Tests")
class CommandFrontmatterTest {

    private static final String FRONTMATTER_END = "---\n\n";
    private static final Map<String, String> LEGACY_BODY_SHA_256 = legacyBodyHashes();
    private static final Map<String, ExpectedMetadata> APPROVED_METADATA = approvedMetadata();

    @Test
    @DisplayName("Generated frontmatter must equal every inventoried XML metadata contract")
    void should_renderApprovedMetadata_when_allCommandsAreGenerated() throws Exception {
        for (String source : CommandIndexes.commandSources().toList()) {
            Document command = loadXml("commands/" + source);
            String markdown = CommandMarkdownRenderer.render(command);
            Map<String, Object> frontmatter = parseFrontmatter(markdown);
            Element metadata = (Element) command.getElementsByTagName("metadata").item(0);
            ExpectedMetadata expected = Objects.requireNonNull(
                APPROVED_METADATA.get(command.getDocumentElement().getAttribute("id")),
                "Approved metadata must exist for every command"
            );

            assertThat(frontmatter)
                .as("Frontmatter keys for %s", source)
                .containsOnlyKeys("description", "argument-hint", "model", "agent", "tools");
            assertThat(frontmatter.get("description")).isEqualTo(text(metadata, "description"));
            assertThat(frontmatter.get("argument-hint")).isEqualTo(text(metadata, "argument-hint"));
            assertThat(frontmatter.get("model")).isEqualTo(text(metadata, "model"));
            assertThat(frontmatter.get("agent")).isEqualTo(text(metadata, "agent"));
            assertThat(frontmatter.get("tools")).isEqualTo(elements(metadata, "tool"));
            assertThat(frontmatter.get("description")).isEqualTo(expected.description());
            assertThat(frontmatter.get("argument-hint")).isEqualTo(expected.argumentHint());
            assertThat(frontmatter.get("model")).isEqualTo("inherit");
            assertThat(frontmatter.get("agent")).isEqualTo(expected.agent());
            assertThat(frontmatter.get("tools")).isEqualTo(expected.tools());
        }
        assertThat(APPROVED_METADATA).hasSize(14);
    }

    @Test
    @DisplayName("YAML scalars must preserve punctuation, backslashes, apostrophes, and boundary spaces")
    void should_preserveYamlSignificantCharacters_when_frontmatterIsRendered() throws Exception {
        String xml = "<command id=\"yaml-boundaries\"><metadata>"
            + "<description>  Plan: team's #1 [work] \\ path  </description>"
            + "<argument-hint>[issue: 'value']</argument-hint><model>inherit</model><agent>inherit</agent>"
            + "<tools><tools-list><tool>Read</tool><tool>Custom: Tool #1</tool></tools-list></tools>"
            + "</metadata><goal>Goal</goal><steps/></command>";

        Map<String, Object> metadata = parseFrontmatter(CommandMarkdownRenderer.render(
            InventoryXmlLoader.parse(new java.io.ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)))));

        assertThat(metadata.get("description")).isEqualTo("  Plan: team's #1 [work] \\ path  ");
        assertThat(metadata.get("argument-hint")).isEqualTo("[issue: 'value']");
        assertThat(metadata.get("tools")).isEqualTo(List.of("Read", "Custom: Tool #1"));
    }

    @Test
    @DisplayName("Frontmatter must not change any byte in the legacy Markdown body")
    void should_preserveLegacyBodyByteForByte_when_frontmatterIsPrepended() throws Exception {
        for (String source : CommandIndexes.commandSources().toList()) {
            String commandFile = CommandIndexes.toMarkdownFileName(source);
            String body = bodyAfterFrontmatter(CommandMarkdownRenderer.render(loadXml("commands/" + source)));

            assertThat(sha256(body))
                .as("Legacy Markdown body hash for %s", commandFile)
                .isEqualTo(LEGACY_BODY_SHA_256.get(commandFile));
        }
    }

    static Map<String, Object> parseFrontmatter(String markdown) {
        assertThat(markdown).startsWith("---\n");
        int end = markdown.indexOf(FRONTMATTER_END, 4);
        assertThat(end).isGreaterThan(3);
        String yaml = markdown.substring(4, end);
        Object loaded = new Load(LoadSettings.builder().build()).loadFromString(yaml);
        assertThat(loaded).isInstanceOf(Map.class);
        @SuppressWarnings("unchecked")
        Map<String, Object> metadata = (Map<String, Object>) loaded;
        return metadata;
    }

    static String bodyAfterFrontmatter(String markdown) {
        int end = markdown.indexOf(FRONTMATTER_END, 4);
        assertThat(end).isGreaterThan(3);
        return markdown.substring(end + FRONTMATTER_END.length());
    }

    private static Document loadXml(String resource) throws Exception {
        try (InputStream stream = CommandFrontmatterTest.class.getClassLoader().getResourceAsStream(resource)) {
            if (stream == null) {
                throw new IllegalStateException("Missing command resource: " + resource);
            }
            return InventoryXmlLoader.parse(stream);
        }
    }

    private static String text(Element parent, String name) {
        return parent.getElementsByTagName(name).item(0).getTextContent();
    }

    private static List<String> elements(Element parent, String name) {
        var nodes = parent.getElementsByTagName(name);
        return java.util.stream.IntStream.range(0, nodes.getLength())
            .mapToObj(index -> nodes.item(index).getTextContent())
            .toList();
    }

    private static String sha256(String value) throws Exception {
        return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256")
            .digest(value.getBytes(StandardCharsets.UTF_8)));
    }

    private static Map<String, String> legacyBodyHashes() {
        Map<String, String> hashes = new LinkedHashMap<>();
        hashes.put("benchmark.md", "b5efb1f94fb883a495267ee8263e75012a29b49d1c74d7725120e69347fabef0");
        hashes.put("close-spec.md", "54832426a9211ed0382c3416d134487f84a2e4a8cc8f0d25ad566956fbb7475c");
        hashes.put("create-acceptance-criteria.md", "a7c712ff5e6ee58a6749929be852a9262dcb3058f66b2beb133f3499b1297121");
        hashes.put("create-adr.md", "cb963c08544fbd51736b9ddf54d726d6af2195c0c8e85008b5e250444c14fd65");
        hashes.put("create-diagram.md", "b82650b24c655922d990e695124a8ca6d6cc6c4a2556aa276c989d5ca0796d5c");
        hashes.put("create-feature-branch.md", "6114d012e3c6d5140231831aa2907adb53dbcfc5c2ec6ef5fe4e5710ee0d7e20");
        hashes.put("create-spec.md", "c9409cfa88a30ae8ff613834f22890c26eb9a47edea497f10ed57bf124fb4cf0");
        hashes.put("create-worktree.md", "2f744fa6ded845ca31be3ecdf0df1cbd3cb6c90e03dae910535d9cf8ee5f9f25");
        hashes.put("explore-design.md", "192d877d65ea77c76645e1371666ac8a9bd2a78d44faaf58cf30aa6d625adc7c");
        hashes.put("explore-problem.md", "bf0b8c1acd2f79b79c5958124c06297a62d5faecbaa0933541808fdd49213cf2");
        hashes.put("implement-spec.md", "2ae28a480c28405205e38ee502138c9e695b3f07b5911ede7450154d5f1a2b30");
        hashes.put("profile.md", "98e107c4a577b9c745ca0639b4929b5e223b04668e78ff00138fd0f07657c060");
        hashes.put("review-alignment.md", "df4e45038ac1ebcead4a6bbd2b6f6d05603254596d8fb6da075e9156be0143d5");
        hashes.put("update-issue.md", "7766e1ae872b034570b1c183b2a2695722ab64a4bfbecefcc0567f540333ef41");
        return Map.copyOf(hashes);
    }

    private static Map<String, ExpectedMetadata> approvedMetadata() {
        Map<String, ExpectedMetadata> metadata = new LinkedHashMap<>();
        metadata.put("benchmark", expected("robot-java-performance",
            "Design and coordinate a reproducible Java performance test.",
            "[target]", "Read", "Write", "Edit", "Bash"));
        metadata.put("close-spec", expected("robot-architect", "Archive a completed OpenSpec change by name.", "[openspec-change]", "Read", "Bash"));
        metadata.put("create-acceptance-criteria", expected("robot-business-analyst", "Derive and post confirmed Gherkin acceptance criteria for an issue.", "[issue-url]", "Read", "Bash"));
        metadata.put("create-adr", expected("robot-architect", "Create a repository ADR for an approved architectural decision.", "[decision-source] [adr-type]", "Read", "Write", "Edit"));
        metadata.put("create-diagram", expected("robot-architect", "Create an architecture or design diagram from selected source artifacts.", "[source-artifact] [diagram-type]", "Read", "Write", "Edit"));
        metadata.put("create-feature-branch", expected("robot-tech-lead", "Create and switch to a conventionally named feature branch.", "[issue-or-change|type description] [base-reference]", "Read", "Bash"));
        metadata.put("create-spec", expected("robot-architect", "Create or update OpenSpec artifacts from approved source material.", "[issue-url]", "Read", "Write", "Edit", "Bash"));
        metadata.put("create-worktree", expected("robot-tech-lead", "Create an isolated Git worktree on a new conventionally named branch.", "[issue-or-change|type description] [target-path] [base-reference]", "Read", "Bash"));
        metadata.put("explore-design", expected("robot-architect", "Refine the technical design of an issue or OpenSpec change before implementation.", "[openspec-change]", "Read", "Write", "Edit", "Bash"));
        metadata.put("explore-problem", expected("robot-business-analyst", "Analyze an issue through five lenses and post a Functional Specification.", "[issue-url]", "Read", "Bash"));
        metadata.put("implement-spec", expected("robot-tech-lead", "Deliver an approved plan or OpenSpec change through controlled implementation.", "[openspec-change]", "Read", "Write", "Edit", "Bash"));
        metadata.put("profile", expected("robot-java-performance", "Coordinate a reproducible Java profiling and optimization lifecycle.", "[target]", "Read", "Write", "Edit", "Bash"));
        metadata.put("review-alignment", expected("robot-business-analyst", "Review analysis and design artifacts for implementation readiness.", "[artifact] [artifact ...]", "Read"));
        metadata.put("update-issue", expected("robot-business-analyst", "Update an issue description with structured, evidence-backed content.", "[issue-url]", "Read", "Bash"));
        return Map.copyOf(metadata);
    }

    private static ExpectedMetadata expected(String agent, String description, String argumentHint, String... tools) {
        return new ExpectedMetadata(agent, description, argumentHint, List.of(tools));
    }

    private record ExpectedMetadata(String agent, String description, String argumentHint, List<String> tools) {}
}
