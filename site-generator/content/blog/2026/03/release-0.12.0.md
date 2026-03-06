title=What's new in Cursor rules for Java 0.12.0?
date=2026-03-08
type=post
tags=blog
author=Juan Antonio Breña Moral
status=published
~~~~~~

## What are Cursor rules for Java?

The project provides a collection of `System prompts` & `Skills` for Java Enterprise development that help software engineers and pipelines in their daily programming work.

## What's new in this release?

In this release, the project introduces several updates:

- **Added Skill Support:**
- Published an initial set of 20 `SKILLs` for Java Enterprise development
- Designed and implemented a Skill generator with the capability to reuse previous work with System prompts
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

The [`AI Assistants`](https://aws.amazon.com/en/blogs/devops/aws-named-as-a-leader-in-the-2025-gartner-magic-quadrant-for-ai-code-assistants/) market is changing the way software engineers work worldwide. More and more similar products are providing the same capabilities, but it was not possible to enhance these new services in a unified way. Therefore, it became necessary to define a `standard` for extending AI agent capabilities with specialized knowledge and workflows that can be used by multiple products.

In the past, this project focused on the concept of `Prompting engineering` and released a great collection of `System prompts` (formerly Cursor rules), but after a period of time observing Skills' adoption rate and popularity, it became clear that it is necessary to understand how `SKILL` works.

Any Skill is defined in a file named `SKILL.md` which, per the specification, allows a maximum of 600 lines per file, but that limitation can be exceeded in files located in the `references` folder.

```bash
my-skill/
├── SKILL.md          # Required: instructions + metadata
├── scripts/          # Optional: executable code
├── references/       # Optional: documentation
└── assets/           # Optional: templates, resources
```
**Source:** https://agentskills.io/what-are-skills

Another detail to be taken into account is the YAML frontmatter which needs to be present in any Skill. Take a look at the following example from the Skill `@110-java-maven-best-practices`

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

If you follow the [Skill specification](https://agentskills.io/specification), any `Skill` requires a field `name` & `description`. The description is important because the module will use that information to know when to use that skill or not.

Once the project understood the standard and how to reuse the previous work, the hardest part—the generation of Skills—was easy.

Now you can use the results in your projects.

### What Skills were generated in this release?

Currently, the project has released 20 Skills:

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

In previous months, the project created a CLI to help engineers install the `System prompts` from this project or any project that has the `System prompts` located in `.cursor/rules` or contains a symbolic link for it:

```bash
jbang setup@jabrena init \
--cursor https://github.com/jabrena/cursor-rules-java
```

But now using Skills, `Vercel` provides a better solution at https://skills.sh/ to find and install Skills. You can browse all skills generated in this project [here](https://skills.sh/?q=jabrena) and download any skill or all of them in the following easy way:

```bash
npx skills add jabrena/cursor-rules-java --list
npx skills add jabrena/cursor-rules-java
npx skills install jabrena/cursor-rules-java --all
npx skills add https://github.com/jabrena/cursor-rules-java \
--skill 110-java-maven-best-practices
```

Something that I like about the Skill ecosystem is the new Security pipelines to ensure that our work is safe as usual.

[![](/cursor-rules-java/images/2026/3/skill-security-audit.png)](https://skills.sh/jabrena/cursor-rules-java/110-java-maven-best-practices)

**Source:** https://skills.sh/jabrena/cursor-rules-java/110-java-maven-best-practices

### What is SkillsJars?

When you develop a website using the Java stack, one popular solution is the use of [WebJars](https://www.webjars.org/), which encapsulates popular web libraries using the Java way. Using the same criteria but for Skills, your JVM agents can now use Skills in an easy way. If you understood the previous ideas, `James Ward` created [SkillsJars](https://www.skillsjars.com/).

The project published a few Skills on SkillsJars to test the format and capabilities, and in the future the whole collection will be published.

Publishing `Skills` to `Sonatype` adds an extra security layer and immutable nature, and a Skill published as a Jar has the following layout:

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

**Source:** https://repo.maven.apache.org/maven2/com/skillsjars/jabrena__cursor-rules-java__111-java-maven-dependencies/2026_02_23-96a0bd2/jabrena__cursor-rules-java__111-java-maven-dependencies-2026_02_23-96a0bd2.jar
**Skill:** `@111-java-maven-dependencies`

You could publish your skills as Jars and add them to the classpath with this Maven plugin:

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

But maybe you could publish to an Artifactory and use them once you extract them for the daily work using `Prompting engineering:`

```bash
./mvnw skillsjars:extract -Ddir=.agents/skills
```

If you are interested in `Skillsjars` and `Spring AI`, the following links could be interesting for you:

- https://www.skillsjars.com/?bt=maven&q=maven
- https://github.com/skillsjars/skillsjars-maven-plugin
- https://github.com/skillsjars/skillsjars-example-spring-ai
- https://spring.io/blog/2026/01/13/spring-ai-generic-agent-skills

## Why Agents.md?

In general, any `Git repository` includes a `README.md` file where authors explain the purpose of the repository from a product's perspective. From a developer's perspective, this project includes a Skill named `@113-java-maven-documentation` which was designed to generate a file `DEVELOPER.md` with technical details about how to use the build system and profiles, but what happens when you interact with models? Maybe a document explaining a few details about the project could be useful for models.

For this purpose, the skill `@173-java-agents` was designed to generate an `AGENTS.md` file after an interactive set of interactions with the Skill.

Interesting article: https://github.blog/ai-and-ml/github-copilot/how-to-write-a-great-agents-md-lessons-from-over-2500-repositories/

## How to use Prompts, Agents.md, Skills & MCP/Cli tools in your daily work?

In 2026, the usage of AI tooling is not optional but it is true that it depends on your organization and the nature of your project to select which tools fit better for your area.

First, create an empty Java project based on your favourite build system like `Maven` or `Gradle`. Once you have the pillars in place, ask your `Cursor`, `Claude` or other AI assistant to create an `AGENTS.md` using the Skill `@173-java-agents`. Once you define that file, in the next interactions the models will understand where the files are and how to interact with your build system and other details.

If your approach is based on `Prompting Engineering`:

[![](/cursor-rules-java/images/2026/3/workflow.png)](https://skills.sh/jabrena/cursor-rules-java/110-java-maven-best-practices)

You could begin the development by creating an empty unit test and adding a few Java comments. Those comments could be your first prompt in that project. Using this approach and depending on the level of detail of your notes as a prompt, you could send that selection to the context to develop the test and later the implementation following a TDD approach. The model will follow the `User prompt` and the `AGENTS.md` file.

After that, you could continue indicating notes incrementally and maybe triggering some Skills to improve the development.

Read this section carefully and explore this approach. In this repository, you could find some nontrivial problems: https://github.com/jabrena/latency-problems to be solved with this approach.

On the other hand, if you are interested in adding `AI Capabilities` to your pipelines, I recommend reading the following article: https://www.javaadvent.com/2025/12/delegating-java-tasks-to-supervised-ai-dev-pipelines.html

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

**Subagents:**

- https://code.claude.com/docs/en/sub-agents
- https://code.claude.com/docs/en/agent-teams
- https://cursor.com/docs/context/subagents
- https://block.github.io/goose/docs/guides/subagents/

## Do you still have questions about the project?

If you feel stuck using this project or have questions, you can attend the following Workshop at `Codemotion Madrid 2026`:

[![](/cursor-rules-java/images/2026/3/codemotion-madrid-2026.jpg)](https://conferences.codemotion.com/madrid/)
