title=What's new in Cursor rules for Java 0.12.0?
date=2026-03-08
type=post
tags=blog
author=Juan Antonio Breña Moral
status=published
~~~~~~

## What are Cursor rules for Java?

A curated collection of `System prompts` & `Skills` for Java Enterprise development that help software engineers and pipelines in their daily programming work.

Recently, we reached the milestone of `300+` ⭐ in Github.

## What's new in this release?

In this release, the project introduces several updates and improvements:

- **Added Skill Support:**
- Published an initial set of 20 Skills for Java Enterprise development
- Designed and implemented a Skill generator with the capability to reuse the previous work on System prompts
- **Improvements in System Prompts:**
- Centralized version management and best practices for multi-module POM in `@110-java-maven-best-practices`
- Added `ArchUnit` support in `@111-java-maven-dependencies`
- Cyclomatic complexity analysis support in `@112-java-maven-plugins`
- Minimum Maven compiler support in `@112-java-maven-plugins`
- `@DEVELOPER.md` and plugin catalog in `@113-java-maven-documentation`
- Added `@132-java-testing-integration-testing` with WireMock support
- Split Java documentation into ADR capabilities in `@170-java-documentation` and `@171-java-adr`
- Added support to generate `AGENTS.md` with `@173-java-agents`
- **Project improvements:**
- Added a new generator to manage `SKILL` generation
- Added a validator to ensure good quality in `SKILL` validation against the [Skill Specification](https://agentskills.io/specification)

Let's explain each of the released features one by one.

## Why Skills?

![](/cursor-rules-java/images/2026/3/skills-everywhere.png)

The [`AI Assistants`](https://aws.amazon.com/en/blogs/devops/aws-named-as-a-leader-in-the-2025-gartner-magic-quadrant-for-ai-code-assistants/) market is changing the way of work in software development. More and more similar products are providing the same capabilities, but it was not possible to enhance these new services in a unified way. In the past, `Cursor` released the concept about `System prompts` but that idea required defining a `Standard` to be implemented for everyone. A `Skill` extends the AI agent capabilities with specialized knowledge and workflows that can be used by multiple products.

In the past, this project focused on the concept of `Prompt engineering` and released a great curated collection of `System prompts` (formerly Cursor rules), but after a period of time observing Skills' adoption rate and popularity, it became clear that it was necessary to understand how `Skill` works—that it is exactly the same as a `System prompt`—but it was clear that for this project to avoid becoming outdated soon, it was necessary to migrate the development to this new format.

Any `Skill` is defined in a file named `SKILL.md` which, per the specification, allows a maximum of 600 lines per file, but that limitation can be exceeded in files located in the `references` folder.

```bash
my-skill/
├── SKILL.md          # Required: instructions + metadata
├── scripts/          # Optional: executable code
├── references/       # Optional: documentation
└── assets/           # Optional: templates, resources
```
**Source:** https://agentskills.io/what-are-skills

Another detail to be taken into account is the `YAML frontmatter` which needs to be present in any Skill and defines `Metadata`. Take a look at the following example from the Skill `@110-java-maven-best-practices`:

```bash
---
name: 110-java-maven-best-practices
description: Use when you need to review, improve,
or troubleshoot a Maven pom.xml file — including dependency management with
BOMs, plugin configuration, version centralization,
multi-module project structure, build profiles, or any situation
where you want to align your Maven setup with industry best practices.
Part of the skills-for-java project
metadata:
  author: Juan Antonio Breña Moral
  version: 0.12.0
---
```

If you read [`the Skill specification`](https://agentskills.io/specification) in detail, any `Skill` requires a field `name` & `description` in the metadata section. The description is super important because the module will use that information to know when to use that skill or not.

The project added a new `Skill generator` and in this release you can see the results of this journey.

### What Skills were generated in this release?

Currently, the project has released the following 20 Skills:

- `@110-java-maven-best-practices`
- `@111-java-maven-dependencies`
- `@112-java-maven-plugins`
- `@113-java-maven-documentation`
- `@121-java-object-oriented-design`
- `@122-java-type-design`
- `@123-java-exception-handling`
- `@124-java-secure-coding`
- `@125-java-concurrency`
- `@128-java-generics`
- `@131-java-testing-unit-testing`
- `@132-java-testing-integration-testing`
- `@141-java-refactoring-with-modern-features`
- `@142-java-functional-programming`
- `@143-java-functional-exception-handling`
- `@144-java-data-oriented-programming`
- `@170-java-documentation`
- `@171-java-adr`
- `@172-java-diagrams`
- `@173-java-agents`

But in the next release, the project will generate the pending ones:

- `@126-java-observability-logging`
- `@151-java-performance-jmeter`
- `@161-java-profiling-detect`
- `@162-java-profiling-analyze`
- `@163-java-profiling-refactor`
- `@164-java-profiling-compare`
- `@164-java-profiling-verify`

And others that are identified in the [`Backlog`](https://github.com/jabrena/cursor-rules-java/issues).

In previous months, the project created a CLI to help engineers install the `System prompts` from this project or any project that has the `System prompts` located in `.cursor/rules` or contains a symbolic link for it:

```bash
jbang setup@jabrena init \
--cursor https://github.com/jabrena/cursor-rules-java
```

But now using Skills, `Vercel` provides a better solution located in https://skills.sh/. Using this website, it is very easy to find and install Skills.

You can browse all skills generated in this project [here](https://skills.sh/?q=jabrena) and download any Skill or all of them in the following easy way:

```bash
brew install node
npx skills add jabrena/cursor-rules-java --list
npx skills add https://github.com/jabrena/cursor-rules-java \
--skill 110-java-maven-best-practices
npx skills install jabrena/cursor-rules-java --all
```

Something that I like about https://skills.sh/ is the new `Security pipelines` to audit that our work is safe as usual:

[![](/cursor-rules-java/images/2026/3/skill-security-audit.png)](https://skills.sh/jabrena/cursor-rules-java/110-java-maven-best-practices)

**Source:** https://skills.sh/jabrena/cursor-rules-java/110-java-maven-best-practices

**Note:** I recommend using websites like https://skills.sh/ because any Skill available from it passed the security audits. A Skill can include a Script and it could be an `Attack vector`.

### What is SkillsJars?

When you develop a website using any Java stack, one popular solution is the usage of [WebJars](https://www.webjars.org/), which encapsulates popular web libraries using the Java way. Using the same criteria but for Skills, your JVM agents can now use Skills in an easy way. If you understood the previous ideas, now you understand the motivation behind [SkillsJars](https://www.skillsjars.com/), a new creation by [`James Ward`](https://x.com/JamesWard).

This project published a few Skills on SkillsJars to test the approach and capabilities, and in the future the whole collection will be published using that approach.

Using https://skills.sh/, you download the Skill directly from a Git repository but using `SkillsJars` goes beyond—you download the Skill from a Maven artifact previously published in `Sonatype`, which adds immutability to this process.

**Examples:**

If you unpackage a Skill delivered as a JAR, it has the following layout:

```bash
META-INF/
META-INF/skills/
META-INF/skills/jabrena/
META-INF/skills/jabrena/cursor-rules-java/
META-INF/maven/
META-INF/maven/com.skillsjars/
META-INF/MANIFEST.MF
META-INF/maven/com.skillsjars/jabrena__cursor-rules-java__111-java-maven-dependencies/pom.xml
META-INF/maven/com.skillsjars/jabrena__cursor-rules-java__111-java-maven-dependencies/pom.properties
META-INF/skills/jabrena/cursor-rules-java/111-java-maven-dependencies/SKILL.md
META-INF/skills/jabrena/cursor-rules-java/111-java-maven-dependencies/references/
META-INF/skills/jabrena/cursor-rules-java/111-java-maven-dependencies/references/111-java-maven-dependencies.md
```

**Source:** https://central.sonatype.com/artifact/com.skillsjars/jabrena__cursor-rules-java__111-java-maven-dependencies
**Skill:** `@111-java-maven-dependencies`

Using SkillsJars, you could publish your skills as JARs in your Artifactory:

```xml
<build>
    <plugins>
        <plugin>
            <groupId>com.skillsjars</groupId>
            <artifactId>maven-plugin</artifactId>
            <version>0.0.5</version>
            <executions>
                <execution>
                    <goals>
                        <goal>package</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

Or you could define the available skills to be used by your JVM agents in this way:

```xml
<build>
    <plugins>
        <plugin>
            <groupId>com.skillsjars</groupId>
            <artifactId>maven-plugin</artifactId>
            <version>0.0.5</version>
            <dependencies>
                <!-- Your SkillsJars -->
                <dependency>
                    <groupId>com.skillsjars</groupId>
                    <artifactId>SKILLJAR_ARTIFACT_ID</artifactId>
                    <version>SKILLJAR_VERSION</version>
                </dependency>
            </dependencies>
        </plugin>
    </plugins>
</build>
```

Once you have defined the `SkillsJars` in your build system, you could extract them for the daily work with your favorite `AI Tool` in this way:

```bash
./mvnw skillsjars:extract -Ddir=.agents/skills
```

If you are interested in `SkillsJars` and `Spring AI`, the following links could be interesting for you:

- https://www.skillsjars.com/?bt=maven&q=maven
- https://github.com/skillsjars/skillsjars-maven-plugin
- https://github.com/skillsjars/skillsjars-example-spring-ai
- https://spring.io/blog/2026/01/13/spring-ai-generic-agent-skills

**Note:** I suppose that `Quarkus` & `Micronaut` will add support soon.

### Awesome third party Skills

#### Good design practices

The design catalog offers skills for collaborative design, event modeling, hexagonal architecture, and modern CLI design. These skills guide agents toward structured, domain-driven design decisions and clean architectural boundaries.

- https://github.com/eferro/skill-factory/tree/main/output_skills/design

#### Good testing practices

The testing catalog covers TDD, approval tests, BDD with approvals, mutation testing, nullables handling, and test desiderata. Agents can follow disciplined red-green-refactor workflows and write behavior-focused tests.

- https://github.com/eferro/skill-factory/tree/main/output_skills/testing

#### Architecture skills

The tech-leads-club architecture catalog provides skills for domain analysis, component identification, coupling analysis, decomposition planning, legacy migration, and frontend blueprints. Agents can support modular design, bounded contexts, and migration roadmaps.

- https://github.com/tech-leads-club/agent-skills/blob/main/packages/skills-catalog/skills/(architecture)/

#### PreCommit

A comprehensive skill for managing pre-commit hooks - the framework for multi-language pre-commit hook management that automates code quality, formatting, linting, and security scanning.

- https://github.com/julianobarbosa/claude-code-skills/blob/main/skills/pre-commit-skill/SKILL.md

#### JDB Agentic Debugger

Agent Skill for debugging Java applications in real time using JDB (Java Debugger CLI)

- https://github.com/brunoborges/jdb-agentic-debugger

## Why Agents.md?

In general, any `Git repository` includes a `README.md` file where the authors explain the purpose of the repository from a product's perspective. In many cases, `README.md` includes technical information about the development lifecycle, mixing technical and non-technical information, for this purpose, this project includes a Skill named `@113-java-maven-documentation` which was designed to generate a file `DEVELOPER.md` to include technical details about how to use the build system and profiles, but `what happens when you interact with Models`? Maybe a document dedicated to Models should be required and this is the motivation for `AGENTS.md`.

The skill `@173-java-agents` could be useful for you because it was designed to generate an `AGENTS.md` file after an interactive set of interactions with the Skill.

If you remain interested in this topic, I share the following links:

- https://github.blog/ai-and-ml/github-copilot/how-to-write-a-great-agents-md-lessons-from-over-2500-repositories/

## Awesome MCP Servers & CLI tools for Java

As you know, Models are disconnected from the Internet and they require third-party tools for security reasons. `AI Tools` like `Cursor` or `Claude` maintain the configuration in `mcp.json` files which has the following structure:

```json
{
    "mcpServers": {
        ...
    }
}
```

The third-party MCP/CLI tools recommended in this period:

### Maven tools

MCP server providing AI assistants with Maven Central dependency intelligence for all JVM build tools (Maven, Gradle, SBT, Mill). Features Context7 integration for documentation support.

```json
{
    "mcpServers": {
        "maven-tools": {
            "command": "docker",
            "args": [
                "run",
                "-i",
                "--rm",
                "arvindand/maven-tools-mcp:latest"
            ]
        }
    }
}
```

This MCP tool is a good alternative to the usage of the classic Maven plugin:

```xml
<!-- Versions Maven Plugin -->
<plugin>
    <groupId>org.codehaus.mojo</groupId>
    <artifactId>versions-maven-plugin</artifactId>
    <version>${maven-plugin-versions.version}</version>
</plugin>
```

Because it is true that using the `Maven plugin` you can be aware if one dependency could be updated with:

```bash
# Check for dependency updates
./mvnw versions:display-dependency-updates
# Check for plugin updates
./mvnw versions:display-plugin-updates
# Check for property updates
./mvnw versions:display-property-updates
```

But using the `MCP Tool` you could apply the changes, so this tool goes beyond the Maven plugin.

- https://github.com/arvindand/maven-tools-mcp
- https://hub.docker.com/mcp/server/maven-tools-mcp/overview

### Javadocs

Artifacts in Maven Central typically provide a JavaDoc Jar that contains the versioned documentation for the artifact. While IDEs use this to display the docs for a library, it is also sometimes nice to browse the docs in a web browser. This project is a simple web app that allows you to view the JavaDoc for any artifact in Maven Central or be integrated with Models using a MCP.

```json
{
    "mcpServers": {
        "javadocs": {
            "url": "https://www.javadocs.dev/mcp"
        }
    }
}
```

- https://www.javadocs.dev/
- https://github.com/jamesward/javadoccentral

### GitHub's official MCP Server

The GitHub MCP Server connects AI tools directly to GitHub's platform. This gives AI agents, assistants, and chatbots the ability to read repositories and code files, manage issues and PRs, analyze code, and automate workflows. All through natural language interactions.

```json
{
    "mcpServers": {
        "github": {
            "command": "docker",
            "args": [
                "run",
                "-i",
                "--rm",
                "-e",
                "GITHUB_PERSONAL_ACCESS_TOKEN",
                "ghcr.io/github/github-mcp-server:latest"
            ],
            "env": {
                "GITHUB_PERSONAL_ACCESS_TOKEN": "YOUR_TOKEN_HERE"
            }
        }
    }
}
```

- https://github.com/github/github-mcp-server

### SonarQube MCP Server

The SonarQube MCP Server is designed to integrate code quality and code security tools with your favorite MCP clients.

```json
{
  "mcpServers": {
    "sonarqube": {
      "command": "docker",
      "args": [
        "run",
        "-i",
        "--rm",
        "--init",
        "--pull=always",
        "-e",
        "SONARQUBE_TOKEN",
        "-e",
        "SONARQUBE_ORG",
        //"-e",
        //"SONARQUBE_URL",
        "mcp/sonarqube"
      ],
      "env": {
        "SONARQUBE_TOKEN": "<YourSonarQubeUserToken>",
        "SONARQUBE_ORG": "<YourOrganizationName>",
        //"SONARQUBE_URL": "https://sonarqube.us"
      }
    }
  }
}
```

- https://docs.sonarsource.com/sonarqube-mcp-server

### Effect Oriented Programming book as MCP

The Effect Oriented Programming book is available as an MCP (Model Context Protocol) server, enabling you to use AI assistants to explore and interact with the book's content directly within your development environment.

```json
{
  "mcpServers": {
    "eop-book": {
      "url": "https://mcp.effectorientedprogramming.com"
    }
  }
}
```

- https://effectorientedprogramming.com/
- https://effectorientedprogramming.com/mcp/

**Note:** I like this `MCP` because it is a nice and honest way to monetize the author's effort.

If you observe, several MCP tools are delivered as Docker images to mitigate pain points related to installation and security. Take a look at the current Docker MCP Catalog to discover new possibilities:

- https://hub.docker.com/mcp

## How to use Prompts, Agents.md, Skills & MCP/Cli tools in your daily workflow?

In 2026, the usage of AI tools is not optional but it is true that it depends on your organization and the nature of your project to select which tools fit better for your purpose. Get in touch with your `Platform engineering teams` to learn which `AI tools` are compliant and which `Skills` and `MCP/CLI tools` are supported as part of the good engineering practices.

If you are using `Agile methodologies` and your team organizes the product evolution based on Agile concepts like `Intents`, `Epics`, `User Stories` & `Tasks`, your first step should be to analyze your backlog and review if the tasks are `Ready for development`.

[![](/cursor-rules-java/images/2026/3/agile.png)](https://learn.microsoft.com/en-us/azure/devops/boards/backlogs/define-features-epics?view=azure-devops&tabs=agile-process)

**Source:** https://learn.microsoft.com/en-us/azure/devops/boards/backlogs/define-features-epics?view=azure-devops&tabs=agile-process

You could improve the analysis and design phase using the [`System prompts for Agile`](https://github.com/jabrena/cursor-rules-agile) which provides support for defining tasks about `Epics`, `Features` & `User Stories` (with Gherkin acceptance criteria), `Diagrams`, Solutions for `functional` and `non-functional` requirements (ISO-25010) and others.

Using this kind of tools, your team will increase the delivery capacity but in order maintain the pace defining good User Stories, one alternative that you could apply is the usage of `Double Agile Loop`:

[![](/cursor-rules-java/images/2026/3/double-agile-loop.png)](https://www.stride.build/blog/what-is-dual-track-agile-and-how-does-it-work)

**Source:** https://www.stride.build/blog/what-is-dual-track-agile-and-how-does-it-work

### Solving an User Story with a Prompting Engineering approach

Imagine that you pick up the following [User Story](https://github.com/jabrena/latency-problems/blob/master/docs/problem1/README.md) from your Backlog and you start reading the details:

```
As an API consumer / data analyst
I want to consume God APIs (Greek, Roman & Nordic), filter gods whose names start with 'n', convert each filtered god name into a decimal representation, and return the sum of those values
So that I can perform cross-pantheon analysis and aggregate mythology data for research, reporting, or educational applications.
```

including the following Acceptance criteria:

```gherkin
Feature: God Analysis API
# REST API: GET /api/v1/gods/stats/sum
# Notes:
# - Decimal Conversion Rule: Name then each char to its Unicode int value, then concatenate these ints as strings.
# (e.g., "Zeus" -> Z(90)e(101)u(117)s(115) -> "90101117115").
# - If in the process to load the list, the timeout is reached, the process will calculate with the rest of the lists.
# - Filtering for gods starting with 'n' is case-sensitive (only lowercase 'n').
# - Greek API: https://my-json-server.typicode.com/jabrena/latency-problems/greek
# - Roman API: https://my-json-server.typicode.com/jabrena/latency-problems/roman
# - Nordic API: https://my-json-server.typicode.com/jabrena/latency-problems/nordic

  Background:
    Given the God Analysis API is available at "/api/v1"
    And the system is configured with an API call timeout of 5 seconds

  Scenario: Happy path - Get sum with explicit sources
    When the client sends a GET request to "/gods/stats/sum" with query parameters "filter" = "n" and "sources" = "greek,roman,nordic"
    Then the response status code should be 200
    And the response body should contain a JSON object with a "sum" field
    And the value of "sum" should be "78179288397447443426"
```

and finally you review that the `User story` includes details about the Stack to be used:

```
- Java 25
- Spring Boot 4.0.x
- Spring Boot Modulith
- RestClient
- Junit
- Wiremock
- RestAssured
```

So, lets clone the repository to implement the feature and when you you have the repo in the local dev environment, review that Build system works and review if the repository has any `AGENTS.md` file to help in the interactions with models. If the repo doesn´t have that file, you could use the following Skill `@173-java-agents` focused on this kind of files.

Once you have the repository ready, the next step is to create a `Plan`. The most popular AI tools like `Cursor` & `Claude` have support to generates Plan.

- https://cursor.com/docs/agent/plan-mode
- https://code.claude.com/docs/en/common-workflows#use-plan-mode-for-safe-code-analysis

review and iterate the document created and when the Plan is stable, pass to the model. Include in the context the different files created in design phase, OAS files and Gherkin files to enhance the process.

![](/cursor-rules-java/images/2026/3/workflow.png)

Using a `Prompting engineering` aproach, it is necessary to iterate over the development. In any iteration, it is important that model verify changes it self and not the Software engineer, for this verification step, it is nice to count with the file `AGENTS.md` because it explain that kind of technical details.

PENDING

Read this section carefully and explore this approach. In this repository, you could find some nontrivial problems: https://github.com/jabrena/latency-problems to be solved with this approach.

### Enhance your pipeline with AI Tools

On the other hand, if you are interested in adding `AI Capabilities` to your pipelines:

![](/cursor-rules-java/images/2026/3/workflow-pipelines.png)

I recommend reading the following article: https://www.javaadvent.com/2025/12/delegating-java-tasks-to-supervised-ai-dev-pipelines.html

## Not everything is perfect, your repository is full of `.FOLDERs`

PENDING

## Improvements in System prompts

**Improvements in Maven:**

- Centralized version management and best practices for multi-module POM in `@110-java-maven-best-practices`
- Added `ArchUnit` support in `@111-java-maven-dependencies`
- Cyclomatic complexity analysis support in `@112-java-maven-plugins`
- Minimum Maven compiler support in `@112-java-maven-plugins`
- `@DEVELOPER.md` and plugin catalog in `@113-java-maven-documentation`

**Improvements in Testing:**

- Added `@132-java-testing-integration-testing` with WireMock support

**Improvements in Architecture:**

- Split Java documentation into ADR capabilities in `@170-java-documentation` and `@171-java-adr`

**Improvements in the interactions with LLMs:**

- Added support to generate `AGENTS.md` with `@173-java-agents`

## What are the next steps?

In this article, a few standards were described that AI tools like `Cursor`, `Claude` and others use, but this market continues to evolve and the next topic to explore is **Subagents**.

## Do you still have questions about the project?

If you feel stuck using this project or have questions, you can attend the following Workshop at `Codemotion Madrid 2026`:

[![](/cursor-rules-java/images/2026/3/codemotion-madrid-2026.jpg)](https://conferences.codemotion.com/madrid/)

https://conferences.codemotion.com/madrid/workshop/
