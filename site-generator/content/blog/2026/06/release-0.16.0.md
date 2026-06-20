title=What's new in Cursor rules for Java 0.16.0?
date=2026-06-22
type=post
tags=blog,skills,java,agents,regulations,commands
author=Juan Antonio Breña Moral
status=published
~~~~~~

## What is the purpose?

An opinionated, AI-native development workflow for Java Enterprise: reusable `Skills`, `Agents`, `Commands`, and third-party `MCP servers` combined with a human-in-the-loop model to modernize real-world SDLC practices.

Starting with this release, the project introduces a simple way to describe any `SDLC` action through three phases: `Plan`, `Build`, and `Operate`. `Software engineers` can use this structure when writing a `User prompt` in an AI user interface or terminal.

**Example:**

```bash
Build
  /implement-issue
    @robot-tech-lead
      /create-feature-branch
      /create-worktree
      /review-alignment
      @robot-java-coder
      @robot-java-spring-boot-coder
      @robot-java-quarkus-coder
      @robot-java-micronaut-coder
      @robot-no-java
```

We will go into more detail later, but first, let's review the most interesting features added in this release:

- [Enriching the workflow with Commands and Agents, not only Skills](#enriching-the-workflow-with-commands-and-agents-not-only-skills)
- [What are the Top 10 Skills from this project in Skills.sh?](#what-are-the-top-10-skills-from-this-project-in-skillssh)
- [Applying Zero Trust with your Agent skills](#applying-zero-trust-with-your-agent-skills)
- [Improving the approach to test the behavior of Agent Skills](#improving-the-approach-to-test-the-behavior-of-an-agent-skills)
- [Improving the way to install Agents and Commands](#improving-the-way-to-install-agents-and-commands)
- [New capabilities for Java Enterprise Frameworks](#new-capabilities-for-java-enterprise-frameworks)
- [Increasing engineering awareness with EU regulations](#increasing-the-engineering-awareness-with-eu-regulations)

Thanks to our community members in `Singapore`, `Hong Kong`, `Hanoi`, `London`, and `New York`. 👋👋👋

If you have questions about the project, how to customize it for your team, how to use the skills in daily work, or how to solve tooling issues, use [`GitHub Discussions`](https://github.com/jabrena/cursor-rules-java/discussions).

**Help this project grow:** [If this project helps your team, become a sponsor.](https://github.com/sponsors/jabrena)

## Enriching the workflow with Commands and Agents, not only Skills

<a id="enriching-the-workflow-with-commands-and-agents-not-only-skills"></a>

The project started more than a year ago with a set of reusable `rules / system prompts`. That approach worked well after removing the restriction that associated rules with particular files, as described in [`ADR-002`](https://github.com/jabrena/cursor-rules-java/blob/main/documentation/adr/ADR-002-configure-cursor-rules-manual-scope.md). With the rise of `Skills`, it was a good decision to convert that material into skills and use the new capabilities provided by `Skill registries` like [https://www.skills.sh/](https://www.skills.sh/) and other registries.

In this release, we go further by adding new semantics for expressing the actions a software engineer performs while solving a problem.

That model is organized around three delivery paths:

```text
Plan
  /create-issue
  /update-issue
    @robot-business-analyst
      @043-planning-github-issues
      @044-planning-jira
      @014-agile-user-story
  /create-adr
    @robot-architect
      @030-architecture-adr-general
      @031-architecture-adr-functional-requirements
      @032-architecture-adr-non-functional-requirements
  /create-diagram
    @robot-architect
      @033-architecture-diagrams
  /create-spec
    @robot-tech-lead
      @042-planning-openspec
  /explore-design
    @robot-architect
      @034-architecture-design-exploration
  /review-alignment
    @robot-business-analyst

Build
  /implement-issue
    @robot-tech-lead
      /create-feature-branch
      /create-worktree
      /review-alignment
      @robot-java-coder
      @robot-java-spring-boot-coder
      @robot-java-quarkus-coder
      @robot-java-micronaut-coder
      @robot-no-java

Operate
  /profile
    @robot-java-performance
      @161-java-profiling-detect
      @162-java-profiling-analyze
      @163-java-profiling-refactor
      @164-java-profiling-verify
  /benchmark
    @robot-java-performance
      @151-java-performance-jmeter
      @152-java-performance-gatling
```

Of course, you can continue using the project in the classic way: add the Java class and a particular skill to the context, or describe the action in natural language and let the `AI agent harness tools` trigger the right skill. However, combining commands with agents and skills gives you more benefits.

**Example:**

```bash
Create AGENTS.md #It will trigger the skill @200-agents-md
/update-issue from github #xxx and use User Story format.
/create-spec using ideas from github issue #xxx
/review-alignment between the issue #xxx and the change #yyy
/implement-issue based on OpenSpec change #yyy
```

In coming releases, this model will be enriched in different ways, but its pillars are established in this release.

In other projects, you can find useful `Skills`, `Agents`, or `Commands`, but not always a fully connected workflow designed with `Java` in mind.

## What are the Top 10 Skills from this project in Skills.sh?

<a id="what-are-the-top-10-skills-from-this-project-in-skillssh"></a>

The project has `106 skills` and uses [Skills.sh](https://www.skills.sh/jabrena/cursor-rules-java) as its main skill registry. It has served `11.0K` installs in total. These are the current top 10 skills used by users there:

1. [`110-java-maven-best-practices`](https://www.skills.sh/jabrena/cursor-rules-java/110-java-maven-best-practices) - [maven](https://www.skills.sh/search?q=maven)
2. [`121-java-object-oriented-design`](https://www.skills.sh/jabrena/cursor-rules-java/121-java-object-oriented-design) - [java object oriented](https://www.skills.sh/search?q=java%20object%20oriented)
3. [`124-java-secure-coding`](https://www.skills.sh/jabrena/cursor-rules-java/124-java-secure-coding) - [java security](https://www.skills.sh/search?q=java%20security)
4. [`131-java-testing-unit-testing`](https://www.skills.sh/jabrena/cursor-rules-java/131-java-testing-unit-testing) - [java unit testing](https://www.skills.sh/search?q=java%20unit%20testing)
5. [`142-java-functional-programming`](https://www.skills.sh/jabrena/cursor-rules-java/142-java-functional-programming) - [java functional programming](https://www.skills.sh/search?q=java%20functional%20programming)
6. [`128-java-generics`](https://www.skills.sh/jabrena/cursor-rules-java/128-java-generics) - [java generics](https://www.skills.sh/search?q=java%20generics)
7. [`111-java-maven-dependencies`](https://www.skills.sh/jabrena/cursor-rules-java/111-java-maven-dependencies) - [maven](https://www.skills.sh/search?q=maven)
8. [`141-java-refactoring-with-modern-features`](https://www.skills.sh/jabrena/cursor-rules-java/141-java-refactoring-with-modern-features)
9. [`125-java-concurrency`](https://www.skills.sh/jabrena/cursor-rules-java/125-java-concurrency) - [java concurrency](https://www.skills.sh/search?q=java%20concurrency)
10. [`143-java-functional-exception-handling`](https://www.skills.sh/jabrena/cursor-rules-java/143-java-functional-exception-handling) - [java functional programming](https://www.skills.sh/search?q=java%20functional%20programming)

**What is your favorite `Skill` from this project?** You can share it here: https://github.com/jabrena/cursor-rules-java/discussions/804

## Applying Zero Trust with your Agent skills

<a id="applying-zero-trust-with-your-agent-skills"></a>

`Skills` are not ordinary Markdown files. They are executable guidance for AI agents. A skill can tell an agent how to read code, run commands, inspect evidence, write files, install tools, or make a technical recommendation.

That is useful, but it also means generated skills need a `zero trust` review mindset. In `0.15.0`, the project introduced its first validators for generated skills. In `0.16.0`, that support has grown into a broader validation stack with multiple independent gates:

- `MarkdownValidator` checks that project documentation and generated Markdown remain parseable and healthy.
- `skill-check` validates the skill package structure.
- `cisco-ai-skill-scanner` scans generated skills recursively with behavioral scanning and a strict policy.
- `SkillSpector` adds another static quality and security review.
- `Snyk Agent Scan` adds supply-chain and prompt-risk signals.

The point is not to claim that a generated skill is perfect. The point is to make suspicious behavior visible before maintainers or users rely on it.

Common risks include:

- Prompt injection patterns
- Data exfiltration instructions
- Suspicious command execution
- Hidden or obfuscated content
- Excessive agency
- Supply-chain risk
- Description-behavior mismatch
- Insecure credential handling
- System modification and privilege escalation
- Untrusted content and indirect prompt injection
- Tool poisoning and tool shadowing

If you are interested in this kind of validation, I recommend reading the following article: [How to validate skills?](/cursor-rules-java/blog/2026/06/skill-validators-pipeline.html)

## Improving the approach to test the behavior of Agent Skills

<a id="improving-the-approach-to-test-the-behavior-of-an-agent-skills"></a>

All elements in this project change for different reasons, so it is necessary to invest time in the release process to ensure that they continue to add value for software engineers and AI agents running in pipelines.

During this release, we ran a `Spike` to validate an improved testing process. We added `Gherkin` support for all skills created or updated in this release, reducing testing time and generating evidence for specific deterministic behaviors.

Let's review two examples to show the value of the new tests.

### Example to validate a skill

All skills have an acceptance-test inventory, and it lives in `acceptance-tests-prompts-skills.md`. When a generated skill changes for any reason, it is now possible to run only the matching prompt for that changed skill. Let's review the scenario for `@111-java-maven-dependencies`.

**@111-java-maven-dependencies:**

The inventory has a prompt to validate the skill:

```bash
execute @skills-generator/src/test/resources/gherkin/skills/111-java-maven-dependencies.feature
and verify that acceptance-tests pass.
```

You can run the following `Gherkin` file:

```gherkin
Feature: Validate changes from usage of Maven dependencies skill

Background:
  Given the skill "111-java-maven-dependencies"

@acceptance-test
Scenario: Add JSpecify and Error Prone + NullAway to Maven demo
  Given the example project "examples/maven/maven-demo"
  And the example project has a baseline "pom.xml"
  And the folder "examples/maven/maven-demo" has no git changes
  And the dependency selection answers are:
    | question                  | answer                           |
    | code-quality dependencies | JSpecify; Error Prone + NullAway |
    | main project package name | info.jab                         |
  When the skill "111-java-maven-dependencies" is applied to "examples/maven/maven-demo"
  Then "./mvnw validate" succeeds in "examples/maven/maven-demo"
  And the "pom.xml" declares selected dependencies and compiler plugin arguments
  And ".mvn/jvm.config" contains the required JVM arguments
  And "./mvnw clean verify" fails during compile in "examples/maven/maven-demo"
  And the verification failure is accepted because "-Xlint:all" and "-Werror" convert existing warnings into errors
  And any git changes produced during skill execution and verification are reset
```

For this particular skill, the scenario fixes the example project, the selected dependency answers, the expected `pom.xml` changes, the expected `.mvn/jvm.config` changes, the validation command, the accepted compiler failure, and the cleanup expectation. The goal is not to test every possible conversation. The goal is to prove that the changed skill still follows its intended workflow against a stable fixture.

Let's review another, more complex scenario.

### Example to validate a command

All commands have an acceptance-test inventory, and it lives in `acceptance-tests-prompts-skills.md`. When a generated command changes for any reason, it is now possible to run only the matching prompt for that changed command. Let's review the scenario for `@/implement-issue`.

As an example, let's try to solve the first problem from the project `Latency problems`: https://github.com/jabrena/latency-problems/blob/master/docs/problem1/README.md

```bash
# Problem 1

## User Story Statement

- **As an** API consumer / data analyst
- **I want to** consume God APIs (Greek, Roman & Nordic), filter gods whose names start with a requested letter, convert each filtered god name into a decimal representation, and return the sum of those values
- **So that** I can perform cross-pantheon analysis and aggregate mythology data for research, reporting, or educational applications.

**Notes:**

- Decimal conversion: For each god name, each character is converted to its Unicode integer value and those integers are concatenated as strings (for example, `Zeus` -> `90101117115`). The final result is the numeric sum of all per-name string representations.
- Case sensitivity: The `filter` parameter accepts exactly one Unicode code point and matching is case-sensitive. The documented source data returns god names with uppercase initial letters, such as `Nike`, `Nemesis`, `Neptun`, and `Njord`, so `filter=N` is the meaningful value for the documented aggregate examples. A lowercase `filter=n` is valid but returns no matches for the current documented data.
- HTTP timeouts: Outbound calls use Spring `RestClient` with connect/read timeouts set once in configuration (defaults in `application.yml`; optional environment variable overrides). There is no automatic retry of failed or timed-out requests; aggregation continues with the sources that return in time.
- Configuration: Single default configuration with environment variable overrides for operational flexibility.
- Data sources:
  - Greek API: https://my-json-server.typicode.com/jabrena/latency-problems/greek
  - Roman API: https://my-json-server.typicode.com/jabrena/latency-problems/roman
  - Nordic API: https://my-json-server.typicode.com/jabrena/latency-problems/nordic
```

Given this `User story` and the `OpenSpec` change defined here: https://github.com/jabrena/cursor-rules-java/tree/main/examples/openspec, you can implement it using the new `/implement-issue` command. Let's see how to do it and how to validate it.

**/implement-issue:**

Using this prompt:

```bash
execute @skills-generator/src/test/resources/gherkin/commands/implement-issue.feature
and verify that acceptance-tests pass.
```

You can run the following `Gherkin` file:

```bash
Feature: Validate implement-issue command with the God Analysis API OpenSpec example

Background:
  Given the command prompt file ".cursor/commands/implement-issue.md"
  And the OpenSpec project path "examples/openspec/god-analysis-api"
  And the OpenSpec change path "examples/openspec/god-analysis-api/openspec/changes/add-god-analysis-api"
  And the implementation target directory "examples/openspec/god-analysis-api/demo"
  And the implementation target directory starts empty except for ".gitkeep"

@acceptance-test
Scenario: Implement God Analysis API from a validated OpenSpec change
  Remark: Acceptance execution must use the implement-issue command contract and must not implement outside the requested demo directory.
  Given the OpenSpec change "add-god-analysis-api" contains "proposal.md", "design.md", "tasks.md", and "specs/god-analysis-api/spec.md"
  And the OpenSpec change is validated with "openspec validate --all" from "examples/openspec/god-analysis-api"
  And the command prompt source ".cursor/commands/implement-issue.md" is read before execution
  When the user executes the prompt "/implement-issue examples/openspec/god-analysis-api/openspec/changes/add-god-analysis-api implement in examples/openspec/god-analysis-api/demo"
  Then the command loads the selected OpenSpec "tasks.md" as the execution contract
  And the command confirms the selected OpenSpec change is current, validated, and internally consistent
  And the command identifies the implementation as a Spring Boot MVC Java service from the OpenSpec design and technology constraints
  And the command routes implementation work through "@robot-tech-lead" and the appropriate Java Spring Boot implementation agent
  And the command reports using the current branch as the isolation strategy before implementation starts
  And all generated implementation files are created under "examples/openspec/god-analysis-api/demo"
  And the implementation provides "GET /api/v1/gods/stats/sum"
  And the implementation covers the documented happy path sum "78179288397447443426"
  And the implementation covers the documented partial timeout sum "78101109179220212216"
  And the implementation rejects missing, empty, multi-character, and invalid query parameters with HTTP 400
  And the implementation does not add WebFlux, WebClient, Rest Assured, Resilience4j Retry, Spring Retry, or custom retry loops for US-001
  And the command runs the focused Maven verification command from "examples/openspec/god-analysis-api/demo"
  And the command marks OpenSpec tasks complete only after their acceptance criteria and verification gates pass
  And the command reports changed files, validation evidence, updated OpenSpec task status, risks, and blockers
  And any git changes produced under "examples/openspec/god-analysis-api/demo" during command execution and verification are reset
```

Under the hood, this `Gherkin` file triggers the following set of project elements, which can be located in `.agents/**` or in other locations depending on your preferred tool and installation method:

```
Build
  /implement-issue
    @robot-tech-lead
      /create-feature-branch
      /create-worktree
      /review-alignment
      @robot-java-coder
      @robot-java-spring-boot-coder
      @robot-java-quarkus-coder
      @robot-java-micronaut-coder
      @robot-no-java
```

In this case, the command internally uses the agent `@robot-tech-lead`, which redirects to the specific agent `@robot-java-spring-boot-coder` based on the analysis of the specification, and this is the result:

[![asciicast](https://asciinema.org/a/1257803.svg)](https://asciinema.org/a/1257803)

*Running the test with Codex CLI for the Spring Boot variant*

![](/cursor-rules-java/images/2026/6/vscode-codex.png)

*Running the test with VS Code + Codex plugin*

But if you refine the prompt a bit, you can implement the requirement in `Quarkus`:

```bash
execute @skills-generator/src/test/resources/gherkin/commands/implement-issue.feature
and verify that acceptance-tests pass. 
Implement it using Quarkus, not Spring Boot, as the default requirement.
```

In this case, the agent `@robot-tech-lead` redirects the workload to the specific agent `@robot-java-quarkus-coder`:

[![asciicast](https://asciinema.org/a/1257861.svg)](https://asciinema.org/a/1257861)

*Running the test with Codex CLI for the Quarkus variant*

Or, if required, the agent `@robot-tech-lead` redirects to the specific agent `@robot-java-micronaut-coder`:

```bash
execute @skills-generator/src/test/resources/gherkin/commands/implement-issue.feature
and verify that acceptance-tests pass. 
Implement it using Micronaut, not Spring Boot, as the default requirement.
```

And the project will implement the feature without any issues:

[![asciicast](https://asciinema.org/a/1258091.svg)](https://asciinema.org/a/1258091)

*Running the test with Codex CLI for the Micronaut variant*

As you can see, one of the unique features of this project is the ability to implement requirements across multiple Java frameworks.

## Improving the way to install Agents and Commands

<a id="improving-the-way-to-install-agents-and-commands"></a>

With the rise of `Skills`, there is a need for public registries for them. But what happens to `Agents`, `Commands`, or other files? The reality is that `Agents` and `Commands` are often treated as second-class citizens.

To take advantage of the public registry and the process for generating skills from `XML` sources, it is relatively easy to embed commands and agents in a `Meta Skill`. Once you have installed the skills, you can use the following inventory and installation workflows:

- [`@001-commands-inventory`](https://www.skills.sh/jabrena/cursor-rules-java/001-commands-inventory)
- [`@002-agents-inventory`](https://www.skills.sh/jabrena/cursor-rules-java/002-agents-inventory)
- [`@003-skills-inventory`](https://www.skills.sh/jabrena/cursor-rules-java/003-skills-inventory)
- [`@004-commands-installation`](https://www.skills.sh/jabrena/cursor-rules-java/004-commands-installation)
- [`@005-agents-installation`](https://www.skills.sh/jabrena/cursor-rules-java/005-agents-installation)

Then you can use them to install assets or generate the inventory files.

## New capabilities for Java Enterprise Frameworks

<a id="new-capabilities-for-java-enterprise-frameworks"></a>

In this new release, we have added a few new Java framework capabilities:

- **Project creation:** starter skills for Spring Boot, Quarkus, and Micronaut, helping teams bootstrap Maven-based services with the expected Java version, framework baseline, package structure, and verification commands from the first commit: `@300-frameworks-spring-boot-create-project`, `@400-frameworks-quarkus-create-project`, `@500-frameworks-micronaut-create-project`.
- **Spring Modulith:** dedicated guidance for modular monolith design with Spring Boot: `@305-frameworks-spring-boot-modulith`.
- **MongoDB migrations:** Mongock migration skills for Spring Boot, Quarkus, and Micronaut: `@316-frameworks-spring-mongodb-migrations-mongock`, `@416-frameworks-quarkus-mongodb-migrations-mongock`, `@516-frameworks-micronaut-mongodb-migrations-mongock`.

The value is that teams can pick the framework lane first, then apply the matching skills for project creation, REST APIs, validation, security, persistence, messaging, migrations, MongoDB, and tests. If you use one of the main Java frameworks, you can review the following skills:

**Spring Boot skills:**

- [`@300-frameworks-spring-boot-create-project`](https://www.skills.sh/jabrena/cursor-rules-java/300-frameworks-spring-boot-create-project)
- [`@301-frameworks-spring-boot-core`](https://www.skills.sh/jabrena/cursor-rules-java/301-frameworks-spring-boot-core)
- [`@302-frameworks-spring-boot-rest`](https://www.skills.sh/jabrena/cursor-rules-java/302-frameworks-spring-boot-rest)
- [`@303-frameworks-spring-boot-validation`](https://www.skills.sh/jabrena/cursor-rules-java/303-frameworks-spring-boot-validation)
- [`@304-frameworks-spring-boot-security`](https://www.skills.sh/jabrena/cursor-rules-java/304-frameworks-spring-boot-security)
- [`@305-frameworks-spring-boot-modulith`](https://www.skills.sh/jabrena/cursor-rules-java/305-frameworks-spring-boot-modulith)
- [`@311-frameworks-spring-jdbc`](https://www.skills.sh/jabrena/cursor-rules-java/311-frameworks-spring-jdbc)
- [`@312-frameworks-spring-data-jdbc`](https://www.skills.sh/jabrena/cursor-rules-java/312-frameworks-spring-data-jdbc)
- [`@313-frameworks-spring-db-migrations-flyway`](https://www.skills.sh/jabrena/cursor-rules-java/313-frameworks-spring-db-migrations-flyway)
- [`@314-frameworks-spring-kafka`](https://www.skills.sh/jabrena/cursor-rules-java/314-frameworks-spring-kafka)
- [`@315-frameworks-spring-mongodb`](https://www.skills.sh/jabrena/cursor-rules-java/315-frameworks-spring-mongodb)
- [`@316-frameworks-spring-mongodb-migrations-mongock`](https://www.skills.sh/jabrena/cursor-rules-java/316-frameworks-spring-mongodb-migrations-mongock)
- [`@321-frameworks-spring-boot-testing-unit-tests`](https://www.skills.sh/jabrena/cursor-rules-java/321-frameworks-spring-boot-testing-unit-tests)
- [`@322-frameworks-spring-boot-testing-integration-tests`](https://www.skills.sh/jabrena/cursor-rules-java/322-frameworks-spring-boot-testing-integration-tests)
- [`@323-frameworks-spring-boot-testing-acceptance-tests`](https://www.skills.sh/jabrena/cursor-rules-java/323-frameworks-spring-boot-testing-acceptance-tests)

**Quarkus skills:**

- [`@400-frameworks-quarkus-create-project`](https://www.skills.sh/jabrena/cursor-rules-java/400-frameworks-quarkus-create-project)
- [`@401-frameworks-quarkus-core`](https://www.skills.sh/jabrena/cursor-rules-java/401-frameworks-quarkus-core)
- [`@402-frameworks-quarkus-rest`](https://www.skills.sh/jabrena/cursor-rules-java/402-frameworks-quarkus-rest)
- [`@403-frameworks-quarkus-validation`](https://www.skills.sh/jabrena/cursor-rules-java/403-frameworks-quarkus-validation)
- [`@404-frameworks-quarkus-security`](https://www.skills.sh/jabrena/cursor-rules-java/404-frameworks-quarkus-security)
- [`@411-frameworks-quarkus-jdbc`](https://www.skills.sh/jabrena/cursor-rules-java/411-frameworks-quarkus-jdbc)
- [`@412-frameworks-quarkus-panache`](https://www.skills.sh/jabrena/cursor-rules-java/412-frameworks-quarkus-panache)
- [`@413-frameworks-quarkus-db-migrations-flyway`](https://www.skills.sh/jabrena/cursor-rules-java/413-frameworks-quarkus-db-migrations-flyway)
- [`@414-frameworks-quarkus-kafka`](https://www.skills.sh/jabrena/cursor-rules-java/414-frameworks-quarkus-kafka)
- [`@415-frameworks-quarkus-mongodb`](https://www.skills.sh/jabrena/cursor-rules-java/415-frameworks-quarkus-mongodb)
- [`@416-frameworks-quarkus-mongodb-migrations-mongock`](https://www.skills.sh/jabrena/cursor-rules-java/416-frameworks-quarkus-mongodb-migrations-mongock)
- [`@421-frameworks-quarkus-testing-unit-tests`](https://www.skills.sh/jabrena/cursor-rules-java/421-frameworks-quarkus-testing-unit-tests)
- [`@422-frameworks-quarkus-testing-integration-tests`](https://www.skills.sh/jabrena/cursor-rules-java/422-frameworks-quarkus-testing-integration-tests)
- [`@423-frameworks-quarkus-testing-acceptance-tests`](https://www.skills.sh/jabrena/cursor-rules-java/423-frameworks-quarkus-testing-acceptance-tests)

**Micronaut skills:**

- [`@500-frameworks-micronaut-create-project`](https://www.skills.sh/jabrena/cursor-rules-java/500-frameworks-micronaut-create-project)
- [`@501-frameworks-micronaut-core`](https://www.skills.sh/jabrena/cursor-rules-java/501-frameworks-micronaut-core)
- [`@502-frameworks-micronaut-rest`](https://www.skills.sh/jabrena/cursor-rules-java/502-frameworks-micronaut-rest)
- [`@503-frameworks-micronaut-validation`](https://www.skills.sh/jabrena/cursor-rules-java/503-frameworks-micronaut-validation)
- [`@504-frameworks-micronaut-security`](https://www.skills.sh/jabrena/cursor-rules-java/504-frameworks-micronaut-security)
- [`@511-frameworks-micronaut-jdbc`](https://www.skills.sh/jabrena/cursor-rules-java/511-frameworks-micronaut-jdbc)
- [`@512-frameworks-micronaut-data`](https://www.skills.sh/jabrena/cursor-rules-java/512-frameworks-micronaut-data)
- [`@513-frameworks-micronaut-db-migrations-flyway`](https://www.skills.sh/jabrena/cursor-rules-java/513-frameworks-micronaut-db-migrations-flyway)
- [`@514-frameworks-micronaut-kafka`](https://www.skills.sh/jabrena/cursor-rules-java/514-frameworks-micronaut-kafka)
- [`@515-frameworks-micronaut-mongodb`](https://www.skills.sh/jabrena/cursor-rules-java/515-frameworks-micronaut-mongodb)
- [`@516-frameworks-micronaut-mongodb-migrations-mongock`](https://www.skills.sh/jabrena/cursor-rules-java/516-frameworks-micronaut-mongodb-migrations-mongock)
- [`@521-frameworks-micronaut-testing-unit-tests`](https://www.skills.sh/jabrena/cursor-rules-java/521-frameworks-micronaut-testing-unit-tests)
- [`@522-frameworks-micronaut-testing-integration-tests`](https://www.skills.sh/jabrena/cursor-rules-java/522-frameworks-micronaut-testing-integration-tests)
- [`@523-frameworks-micronaut-testing-acceptance-tests`](https://www.skills.sh/jabrena/cursor-rules-java/523-frameworks-micronaut-testing-acceptance-tests)

## Increasing engineering awareness with EU regulations

<a id="increasing-the-engineering-awareness-with-eu-regulations"></a>

EU regulations are becoming part of the daily software engineering context, not something that only appears at the end of a release. Modern Java systems process personal data, integrate third-party services, expose APIs, run distributed infrastructure, and increasingly include AI models, RAG pipelines, agents, tool calling, or generated decision support.

For software development teams, the value is practical: regulations help turn vague risk into reviewable engineering questions. They push teams to identify the system scope, data flows, operational controls, human oversight, audit evidence, incident paths, and ownership boundaries before a change reaches production.

This becomes even more important with GenAI tooling. When prompts, embeddings, generated code, agent actions, and tool calls enter the SDLC, teams need a repeatable way to ask what data is being used, what decisions are automated, what evidence is kept, and which legal, security, privacy, risk, or product owners must review the work.

`0.16.0` introduces a new alpha family of regulation engineering review skills:

- [`@801-regulations-eu-ai-act`](https://www.skills.sh/jabrena/cursor-rules-java/801-regulations-eu-ai-act)
- [`@802-regulations-dora`](https://www.skills.sh/jabrena/cursor-rules-java/802-regulations-dora)
- [`@803-regulations-gdpr`](https://www.skills.sh/jabrena/cursor-rules-java/803-regulations-gdpr)
- [`@804-regulations-eu-nis2`](https://www.skills.sh/jabrena/cursor-rules-java/804-regulations-eu-nis2)
- [`@805-regulations-eu-cyber-resilience-act`](https://www.skills.sh/jabrena/cursor-rules-java/805-regulations-eu-cyber-resilience-act)
- [`@806-regulations-eu-data-act`](https://www.skills.sh/jabrena/cursor-rules-java/806-regulations-eu-data-act)
- [`@807-regulations-eu-digital-services-act`](https://www.skills.sh/jabrena/cursor-rules-java/807-regulations-eu-digital-services-act)
- [`@808-regulations-eu-digital-markets-act`](https://www.skills.sh/jabrena/cursor-rules-java/808-regulations-eu-digital-markets-act)

These skills are engineering review aids. _They do not provide legal advice and they do not replace qualified legal, compliance, privacy, security, risk, product, or governance owners._

For distributed systems using GenAI tools, a practical review set is:

- `EU AI Act` when the system uses AI models, LLMs, RAG, AI agents, tool calling, or generated decision support.
- `GDPR` when the system processes personal data in prompts, logs, embeddings, retrieval sources, exports, backups, or generated outputs.
- `NIS2` when the system supports essential or important services, cybersecurity incident flows, supply-chain dependencies, or critical operations.
- `DORA` when the system supports financial entities, important business services, ICT risk, third-party ICT providers, or operational resilience evidence.
- `Cyber Resilience Act` when a product, library, agent tool, or software component may be distributed as a product with digital elements.
- `Data Act` when the system exposes data access, sharing, portability, cloud switching, connected-product data, or interoperability workflows.
- `Digital Services Act` when the system supports hosting, platforms, marketplaces, content moderation, recommender systems, advertising, or transparency reporting.
- `Digital Markets Act` when the system supports gatekeeper-platform concerns, core platform services, interoperability, data access, ranking, or self-preferencing controls.

If you are interested in this set of skills, I recommend reading the following article: [Introduction to EU regulations Part I](/cursor-rules-java/blog/2026/06/introduction-to-eu-regulations-part-i.html)

## Next steps

The next phase is already visible in the [`v0.17.0` milestone](https://github.com/jabrena/cursor-rules-java/milestone/11). The backlog continues the same direction as `0.16.0`: make agent workflows more useful, more deterministic, and easier to adopt in real teams.

Functionally, the next workstreams are:

- Expand executable acceptance coverage for `Skills`, `Agents`, and `Commands`, so important behavior is checked with stable `Gherkin` scenarios instead of relying only on package shape or manual review.
- Add LLM engineering references such as `Karpathy's LLM Wiki`, so agent and skill guidance can stay closer to widely used mental models for building with language models.
- Improve analysis methods, including `the hamburger method` and `the two-step method`, so discovery work can become more structured before teams generate ADRs, specs, plans, or implementation tasks.
- Extend Maven guidance with `JavaMoney` support in the Maven plugin workflow, improving how teams introduce money and currency handling into enterprise builds.
- Complete the `EU regulation` review family, so teams can map distributed-system and `GenAI` decisions against a broader set of engineering evidence patterns.

Enjoy.
