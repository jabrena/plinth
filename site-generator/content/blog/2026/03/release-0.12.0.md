title=What's new in Cursor rules for Java 0.12.0?
date=2026-03-08
type=post
tags=blog
author=Juan Antonio Breña Moral
status=published
~~~~~~

## What are Cursor rules for Java?

A curated collection of `System prompts` & `Skills` for Java Enterprise development that help software engineers and pipelines in their daily programming work.

## What's new in this release?

In this release, the project introduces several updates and improvements:

- **Added Skill Support:**
- Published an initial set of 20 `SKILLs` for Java Enterprise development
- Designed and implemented a Skill generator with the capability to reuse the previous work about System prompts
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

The [`AI Assistants`](https://aws.amazon.com/en/blogs/devops/aws-named-as-a-leader-in-the-2025-gartner-magic-quadrant-for-ai-code-assistants/) market is changing the way of work in software development. More and more similar products are providing the same capabilities, but it was not possible to enhance these new services in a unified way. In the past, `Cursor` released the concept about `System prompts` but that idea required to define a `Standard` to be implemented for everyone. A `Skill` extends the AI agent capabilities with specialized knowledge and workflows that can be used by multiple products.

In the past, this project focused on the concept of `Prompting engineering` and released a great curated collection of `System prompts` (formerly Cursor rules), but after a period of time observing the Skill's adoption rate and popularity, it became clear that it was necessary to understand how `Skill` works, that it is exactly the same than a `System prompt` but it was clear that this project in order to be outdated soon, it was necessary to migrate the development to this new format.

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

The project added a new `Skill generator` and in this release you appears the results of this journey.

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

And others that they are identified in the [`Backlog`](https://github.com/jabrena/cursor-rules-java/issues).

In previous months, the project created a CLI to help engineers install the `System prompts` from this project or any project that has the `System prompts` located in `.cursor/rules` or contains a symbolic link for it:

```bash
jbang setup@jabrena init \
--cursor https://github.com/jabrena/cursor-rules-java
```

But now using Skills, `Vercel` provides a better solution located in https://skills.sh/. Using this website, it is very easy to find and install Skills.

You can browse all skills generated in this project [here](https://skills.sh/?q=jabrena) and download any Skill or All of them in the following easy way:

```bash
npx skills add jabrena/cursor-rules-java --list
npx skills add https://github.com/jabrena/cursor-rules-java \
--skill 110-java-maven-best-practices
npx skills install jabrena/cursor-rules-java --all
```

Something that I like about https://skills.sh/ is the new `Security pipelines` to audit that our work is safe as usual:

[![](/cursor-rules-java/images/2026/3/skill-security-audit.png)](https://skills.sh/jabrena/cursor-rules-java/110-java-maven-best-practices)

**Source:** https://skills.sh/jabrena/cursor-rules-java/110-java-maven-best-practices

**Note:** I recommend to use webistes like https://skills.sh/ because any Skill available from it passed the security audits. An Skill can include a Script and it could be a `Attack vector`.

### What is SkillsJars?

When you develop a website using any Java stack, one popular solution is the usage of [WebJars](https://www.webjars.org/), which encapsulates popular web libraries using the Java way. Using the same criteria but for Skills, your JVM agents can now use Skills in an easy way. If you understood the previous ideas, now you understand the motivation behind [SkillsJars](https://www.skillsjars.com/) a new creation by [`James Ward`](https://x.com/JamesWard).

This project published a few Skills on SkillsJars to test the approach and capabilities, and in the future the whole collection will be published using that approach.

Using https://skills.sh/, you download the Skill directly from a Git repository but using `Skillsjars` go beyond and you download the Skill from a Maven artifact previously published in `Sonatype` which add immutability in this process.

**Examples:**

If you Unpackage a Skill delivered as Jar, it has the following layout:

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

Using Skillsjars, You could publish your skills as Jars in your Artifactory:

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

Once you have defined the `Skillsjars` in your build system, you could extract them for the daily work with your favorite `AI Tool` in this way:

```bash
./mvnw skillsjars:extract -Ddir=.agents/skills
```

If you are interested in `Skillsjars` and `Spring AI`, the following links could be interesting for you:

- https://www.skillsjars.com/?bt=maven&q=maven
- https://github.com/skillsjars/skillsjars-maven-plugin
- https://github.com/skillsjars/skillsjars-example-spring-ai
- https://spring.io/blog/2026/01/13/spring-ai-generic-agent-skills

**Note:** I suppose that `Quarkus` & `Micronaut` will add support soon.

### Awesome third party Skills

#### Good design practices

- https://github.com/eferro/skill-factory/tree/main/output_skills/testing

#### Good testing practices

- https://github.com/eferro/skill-factory/tree/main/output_skills/testing

#### PreCommit

A comprehensive skill for managing pre-commit hooks - the framework for multi-language pre-commit hook management that automates code quality, formatting, linting, and security scanning.

- https://github.com/julianobarbosa/claude-code-skills/blob/main/skills/pre-commit-skill/SKILL.md

#### JDB Agentic Debugger

Agent Skill for debugging Java applications in real time using JDB (Java Debugger CLI)

- https://github.com/brunoborges/jdb-agentic-debugger

## Why Agents.md?

In general, any `Git repository` includes a `README.md` file where the Authors explains the purpose of the repository from a product's perspective. In many cases, `README.md` include technical information about the Development lifecycle mixing technical and non-tecnical information, for this purpose, this project includes a Skill named `@113-java-maven-documentation` which was designed to generate a file `DEVELOPER.md` to include technical details about how to use the build system and profiles, but `what happens when you interact with Models`? Maybe a document dedicated to Models should be required and this is the motivation to `AGENTS.md`.

The skill `@173-java-agents` could be useful for you because it was designed to generate an `AGENTS.md` file after an interactive set of interactions with the Skill.

If you continue interested in this topic, I share the following links:

- https://github.blog/ai-and-ml/github-copilot/how-to-write-a-great-agents-md-lessons-from-over-2500-repositories/

## Awesomes MCP Servers & Cli tools for Java

As you know, Models are disconnected to Internet and they require the third party tools by Security reasons. `AI Tools` like `Cursor` or `Claude` maintain the configuration in `mcp.json` files which it has the following structure:

```json
{
    "mcpServers": {
        ...
    }
}
```

MCP/Cli tools recommend in this period:

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

This MCP tool is good alternative to the usage of the classic Maven plugin:

```xml
<!-- Versions Maven Plugin -->
<plugin>
    <groupId>org.codehaus.mojo</groupId>
    <artifactId>versions-maven-plugin</artifactId>
    <version>${maven-plugin-versions.version}</version>
</plugin>
```

Because it is true that using the `Maven plugin` you can be aware if one depenendecy could be updated with:

```bash
# Check for dependency updates
./mvnw versions:display-dependency-updates
# Check for plugin updates
./mvnw versions:display-plugin-updates
# Check for property updates
./mvnw versions:display-property-updates
```

But using the `MCP Tool` you could apply the changes, so this tool go beyond the maven tool.

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

**Note:** I like this `MCP` because it is a nice and honest way to monetize the Author`s effort.

## How to use Prompts, Agents.md, Skills & MCP/Cli tools in your daily work?

In 2026, the usage of AI tooling is not optional but it is true that it depends on your organization and the nature of your project to select which tools fit better for your area.

First, create an empty Java project based on your favourite build system like `Maven` or `Gradle`. Once you have the pillars in place, ask your `Cursor`, `Claude` or other AI assistant to create an `AGENTS.md` using the Skill `@173-java-agents`. Once you define that file, in the next interactions the models will understand where the files are and how to interact with your build system and other details.

If your approach is based on `Prompting Engineering`:

![](/cursor-rules-java/images/2026/3/workflow.png)

You could begin the development by creating an empty unit test and adding a few Java comments. Those comments could be your first prompt in that project. Using this approach and depending on the level of detail of your notes as a prompt, you could send that selection to the context to develop the test and later the implementation following a TDD approach. The model will follow the `User prompt` and the `AGENTS.md` file.

After that, you could continue indicating notes incrementally and maybe triggering some Skills to improve the development.

Read this section carefully and explore this approach. In this repository, you could find some nontrivial problems: https://github.com/jabrena/latency-problems to be solved with this approach.

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
