package info.jab.pml;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Command Index Tests")
class CommandIndexesTest {

    @Test
    @DisplayName("Command inventory XML must load command files in installation order")
    void should_loadCommandFiles_when_commandInventoryIsParsed() {
        List<String> commandFiles = expectedCommandFiles();

        assertThat(commandFiles)
            .isNotEmpty()
            .allSatisfy(commandFile -> assertThat(commandFile).endsWith(".md"));
        assertThat(new HashSet<>(commandFiles))
            .withFailMessage("Command inventory must not contain duplicate command files")
            .hasSize(commandFiles.size());
    }

    @Test
    @DisplayName("Command assets must include the complete command bundle")
    void should_haveCompleteCommandAssets_when_commandBundleIsInstalled() {
        expectedCommandFiles().forEach(commandFile -> {
            String resource = "skill-references/assets/commands/" + commandFile;
            assertThat(getTestResource(resource))
                .withFailMessage("Command asset missing: %s", resource)
                .isNotNull();
        });
    }

    @Test
    @DisplayName("Command installer must include the exact command bundle")
    void should_includeExactCommands_when_commandInstallerUsesXIncludes() throws Exception {
        List<String> commandIncludes = readCommandIncludes("skill-references/004-commands-installation.xml");

        assertThat(commandIncludes)
            .containsExactlyElementsOf(expectedCommandFiles().stream()
                .map(commandFile -> "assets/commands/" + commandFile)
                .toList());
    }

    @Test
    @DisplayName("Command inventory template must list the exact command bundle")
    void should_listExactCommands_when_inventoryTemplateIsGenerated() {
        String inventory = loadClasspathResource("skill-references/assets/java-commands-inventory-template.md");

        expectedCommandFiles().stream()
            .map(commandFile -> commandFile.substring(0, commandFile.length() - ".md".length()))
            .map(commandName -> "`/" + commandName + "`")
            .forEach(command -> assertThat(inventory).contains(command));

        long commandRows = inventory.lines()
            .filter(line -> line.startsWith("| `/"))
            .count();
        assertThat(commandRows).isEqualTo(expectedCommandFiles().size());
    }

    @Test
    @DisplayName("Update issue command must route through the business analyst and user-story skill")
    void should_routeUpdateIssueCommand_when_issueCommandsAreInstalled() {
        String updateIssue = loadClasspathResource("skill-references/assets/commands/update-issue.md");

        assertThat(updateIssue)
            .contains("/update-issue <issue> [<source>] [<tracker>]")
            .contains("`@robot-business-analyst`")
            .contains("`014-agile-user-story` when user-story refinement is required")
            .contains("Present the proposed body before overwriting");
    }

    @Test
    @DisplayName("Feature branch command must support analysis and design transition")
    void should_documentDesignTransition_when_featureBranchCommandIsInstalled() {
        String command = loadClasspathResource("skill-references/assets/commands/create-feature-branch.md");

        assertThat(command)
            .contains("issue/change identifier")
            .contains("Resolve the repository default branch and the current branch")
            .contains("current checkout is `main` or the repository default branch")
            .contains("stop and ask whether to switch to the default branch")
            .contains("safe working tree")
            .contains("OpenSpec artifacts")
            .contains("ADRs")
            .contains("diagrams")
            .contains("does not create a commit automatically");
    }

    @Test
    @DisplayName("Worktree command must require default branch review before creating branches")
    void should_requireDefaultBranchReview_when_worktreeCommandIsInstalled() {
        String command = loadClasspathResource("skill-references/assets/commands/create-worktree.md");

        assertThat(command)
            .contains("/create-worktree <issue-or-change|type description>")
            .contains("Resolve the repository default branch and the current branch")
            .contains("current checkout is `main` or the repository default branch")
            .contains("stop and ask whether to switch to the default branch")
            .contains("git worktree add -b")
            .contains("Leave existing branches, worktrees, directories, and files unchanged on conflict");
    }

    @Test
    @DisplayName("Implement spec command must route executable artifacts through the tech lead")
    void should_routeExecutableArtifact_when_implementSpecCommandIsInstalled() {
        String command = loadClasspathResource("skill-references/assets/commands/implement-spec.md");

        assertThat(command)
            .contains("/implement-spec <approved-plan|openspec-change>")
            .contains("approved implementation plan")
            .contains("validated `tasks.md`")
            .contains("Owner: `@robot-tech-lead`")
            .contains("`@robot-java-coder`")
            .contains("`@robot-java-spring-boot-coder`")
            .contains("`@robot-java-quarkus-coder`")
            .contains("`@robot-java-micronaut-coder`")
            .contains("`@robot-no-java`")
            .contains("file ownership")
            .contains("Mark OpenSpec tasks complete only after")
            .contains("Mandatory execution contract")
            .contains("If the command runner is not `@robot-tech-lead`")
            .contains("MUST invoke the selected implementation agent")
            .contains("Branch/worktree gate")
            .contains("Complete the branch/worktree gate")
            .contains("If the workspace is dirty, stop immediately")
            .contains("Continue only after the user cleans, commits, or stashes the workspace")
            .contains("Do not bypass a dirty workspace by asking for approval to continue")
            .contains("execute `/create-feature-branch`")
            .contains("`/create-worktree`")
            .contains("Do not start implementation before the feature-branch or worktree gate has passed")
            .contains("request `/review-alignment`")
            .contains("MUST NOT implement application code directly");
    }

    @Test
    @DisplayName("Create spec command must route OpenSpec creation through the architect skill set")
    void should_includeBreakingChangeAvoidance_when_createSpecCommandIsInstalled() {
        String command = loadClasspathResource("skill-references/assets/commands/create-spec.md");

        assertThat(command)
            .contains("/create-spec <issue|design|adr|plan|existing-change>")
            .contains("`@robot-architect`")
            .contains("`056-design-avoid-breaking-changes`")
            .contains("Apply breaking-change avoidance guidance")
            .contains("compatibility-review assumptions");
    }

    @Test
    @DisplayName("Performance commands must route to Java performance agent")
    void should_routePerformanceWorkflows_when_profileAndBenchmarkCommandsAreInstalled() {
        String profile = loadClasspathResource("skill-references/assets/commands/profile.md");
        String benchmark = loadClasspathResource("skill-references/assets/commands/benchmark.md");

        assertThat(profile)
            .contains("/profile <application-or-module>")
            .contains("Owner: `@robot-java-performance`")
            .contains("`@161-java-profiling-detect`")
            .contains("`@162-java-profiling-analyze`")
            .contains("`@163-java-profiling-refactor`")
            .contains("`@164-java-profiling-verify`")
            .contains("Do not optimize without user approval")
            .contains("non-equivalent measurements");
        assertThat(benchmark)
            .contains("/benchmark <target>")
            .contains("Owner: `@robot-java-performance`")
            .contains("`@151-java-performance-jmeter`")
            .contains("`@152-java-performance-gatling`")
            .contains("Maven/JMH guidance")
            .contains("JMeter or Gatling")
            .contains("JMH");
    }

    private static List<String> expectedCommandFiles() {
        return CommandIndexes.commandFiles().toList();
    }

    private List<String> readCommandIncludes(String xmlResource) throws Exception {
        try (InputStream xmlStream = getTestResource(xmlResource)) {
            assertThat(xmlStream)
                .withFailMessage("XML resource not found on classpath: %s", xmlResource)
                .isNotNull();

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlStream);
            NodeList includes = document.getElementsByTagNameNS("http://www.w3.org/2001/XInclude", "include");

            List<String> commandIncludes = new ArrayList<>();
            for (int i = 0; i < includes.getLength(); i++) {
                Element include = (Element) includes.item(i);
                String href = include.getAttribute("href");
                if (href.startsWith("assets/commands/")) {
                    commandIncludes.add(href);
                }
            }
            return commandIncludes;
        }
    }

    private String loadClasspathResource(String resourceName) {
        try (InputStream stream = getTestResource(resourceName)) {
            if (stream == null) {
                throw new IllegalArgumentException("Resource not found: " + resourceName);
            }
            return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load classpath resource: " + resourceName, e);
        }
    }

    private InputStream getTestResource(String resourceName) {
        return CommandIndexesTest.class.getClassLoader().getResourceAsStream(resourceName);
    }
}
