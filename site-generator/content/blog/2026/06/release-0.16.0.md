title=What's new in Cursor rules for Java 0.16.0?
date=2026-06-22
type=post
tags=blog,skills,java,agents,regulations
author=Juan Antonio Breña Moral
status=published
~~~~~~

## What is the purpose?

An opinionated, AI-native workflow for modern Java Enterprise delivery, now organized around reusable `Skills`, role-specific `Agents`, team `Commands`, and validation gates.

The project now supports three complementary paths: `Plan`, `Build`, and `Operate`.

Previous releases expanded the catalog with Java, Maven, Spring Boot, Quarkus, Micronaut, Kafka, MongoDB, Observability, Testing, OpenSpec, and planning guidance. 

This release strengthens the operating model around those capabilities:

- [Enriching the workflow with Commands and Agents, not only Skills](#enriching-the-workflow-with-commands-and-agents-not-only-skills)
- [What are the Top 10 Skills from this project in Skills.sh?](#what-are-the-top-10-skills-from-this-project-in-skillssh)
- [Applying Zero Trust with your Agent skills](#applying-zero-trust-with-your-agent-skills)
- [Improving the approach to test the behavior of an Agent Skills](#improving-the-approach-to-test-the-behavior-of-an-agent-skills)
- [Decoupling changes in daily development with the artifacts used by Skill registries](#decoupling-changes-in-daily-development-with-the-artifacts-used-by-skill-registries)
- [Improving the way to install Agents and Commands](#improving-the-way-to-install-agents-and-commands)
- [New capabilities for Java Enterprise Frameworks](#new-capabilities-for-java-enterprise-frameworks)
- [Increasing the Engineering awareness with EU regulations](#increasing-the-engineering-awareness-with-eu-regulations)

Thanks to our community members in `Singapore`, `Hong Kong`, `Hanoi`, `London`, and `New York`. 👋👋👋

**Help this project grow:** [If this project helps your team, become a sponsor.](https://github.com/sponsors/jabrena)

<a id="enriching-the-workflow-with-commands-and-agents-not-only-skills"></a>

## Enriching the workflow with Commands and Agents, not only Skills

The project started more than one year back with a set of reusable `rules / system prompts`: opinionated Markdown guidance for Java, Maven, testing, architecture, performance, and framework work. That was useful because teams could point an AI assistant at a consistent body of engineering expectations instead of rewriting the same instructions for every conversation. But as documented in [`ADR-002`](https://github.com/jabrena/cursor-rules-java/blob/main/documentation/adr/ADR-002-configure-cursor-rules-manual-scope.md), automatic activation through broad `.java` frontmatter did not scale well: multiple rules could enter the context at the same time, increasing latency, consuming context, and making answers less deterministic.

The next step was `Skills`. Skills turned those prompts into packaged, discoverable capabilities with references, assets, validation rules, and release output. A skill is smaller than a full agent, but more durable than a pasted prompt. It gives an AI assistant a focused way to perform one type of work with the same rules every time.

`0.16.0` moves further toward connected workflows. `Commands` become the entry points that a team can run. `Agents` become the role-specific workers that interpret the task. `Skills` become the reusable knowledge and procedure each agent applies.

In practice, the operating model now looks like this:

```text
Command -> Agent -> Skills -> Change in your repository
```

That model is organized around three delivery paths:

```text
Plan
  /create-issue
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

In coming releases, this model will be improved but `the pillars` are stablished in this release.

<a id="what-are-the-top-10-skills-from-this-project-in-skillssh"></a>

## What are the Top 10 Skills from this project in Skills.sh?

The project has `106 skills` and use the following [Skills.sh](https://www.skills.sh/jabrena/cursor-rules-java) as the main Skill registry and it has `10.0K` total installs. These are the current top 10 skills:

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

<a id="applying-zero-trust-with-your-agent-skills"></a>

## Applying Zero Trust with your Agent skills

`Skills` are not ordinary Markdown files. They are executable guidance for AI agents. A skill can tell an agent how to read code, run commands, inspect evidence, write files, install tools, or make a technical recommendation.

That is useful, but it also means generated skills need a `zero trust` review mindset.

In this release, the project continues to reinforce generated-skill validation with multiple independent gates:

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

Further information: [How to validate skills?](/cursor-rules-java/blog/2026/06/skill-validators-pipeline.html)

<a id="improving-the-approach-to-test-the-behavior-of-an-agent-skills"></a>

## Improving the approach to test the behavior of an Agent Skills

If you generate skills locally, validate them before you publish or use them in a shared workflow.

For local generated output, run:

```bash
./mvnw clean install -pl skills-generator
```

That command generates local agent skills under `.agents/skills` without refreshing the public `skills/` release output.

`0.16.0` also adds a new layer for behavioral validation: acceptance criteria written in `Gherkin`.

The prompt inventory lives in `skills-generator/src/test/resources/gherkin/skills/acceptance-tests-prompts-skills.md`. When a generated local skill changes and that skill is listed in the inventory, run only the matching prompt for that changed skill.

For example, the Maven dependencies skill is validated with this prompt:

```text
execute @skills-generator/src/test/resources/gherkin/skills/111-java-maven-dependencies.feature
and verify that acceptance-tests passes.
```

The linked Gherkin scenario makes the expected behavior reviewable:

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

This is intentionally deterministic. The scenario fixes the example project, the selected dependency answers, the expected `pom.xml` changes, the expected `.mvn/jvm.config` changes, the validation command, the accepted compiler failure, and the cleanup expectation. The goal is not to test every possible conversation. The goal is to prove that the changed skill still follows its intended workflow against a stable fixture.

In the next release cycle, this validation model will continue to grow. The plan is to add Gherkin files gradually for all skills, commands, and agents, so every important workflow can move from "the prompt looks good" to "the behavior has an executable acceptance criterion."

<a id="decoupling-changes-in-daily-development-with-the-artifacts-used-by-skill-registries"></a>

## Decoupling changes in daily development with the artifacts used by Skill registries

One of the most important delivery upgrades in `0.16.0` is not only what the skills contain. It is where generated skills are published during iteration.

Before this release, it was too easy for generated output to land directly in the public `skills/` folder while a change was still being tested. That folder is not just an internal build artifact. It is the public release surface consumed by external catalogs and portals such as `skills.sh`.

That matters because a half-finished skill can be valid Markdown, pass a first structural check, and still be wrong for real users. If that intermediate output appears in `skills/`, downstream consumers may index something that was only meant to be a local experiment.

`0.16.0` makes the delivery path cleaner:

```text
XML source -> generator -> .agents/skills -> validation -> skills/
```

The `.agents/skills` folder becomes the local staging area. Maintainers can regenerate skills, inspect the result, run prompt acceptance checks, and keep iterating without publishing those intermediate artifacts to public consumers.

The `skills/` folder becomes the intentional release output. It should only change when the project is ready to publish a stable version of the generated skills.

This is a big upgrade for delivery discipline:

- Local iteration is faster because generated skills are immediately available to agents.
- Public consumers avoid unfinished skills during the middle of a change.
- Release diffs become easier to review because `skills/` represents a deliberate publishing event.
- Portals such as `skills.sh` receive cleaner release artifacts instead of work-in-progress output.

The mental model is simple:

```text
Use .agents/skills while building.
Publish skills/ when releasing.
```

This release also adds runtime resource packaging, command index generation, and tests alongside the existing skill index workflow.

Another important addition is the `Skill acceptance prompt inventory`. Scanner checks answer questions like:

- Is the package valid?
- Does the package contain risky instructions?
- Does the generated content look structurally correct?

Acceptance prompts answer a different question:

```text
Does the changed skill still behave as expected for its intended workflow?
```

That is especially useful for skills that guide interactive work, such as planning, architecture, framework migration, or database changes.

<a id="improving-the-way-to-install-agents-and-commands"></a>

## Improving the way to install Agents and Commands

This release makes it easier to treat AI-agent workflows as shared team infrastructure.

New inventory and installation skills help teams review and install the same assets:

- [`@001-commands-inventory`](https://www.skills.sh/jabrena/cursor-rules-java/001-commands-inventory)
- [`@004-commands-installation`](https://www.skills.sh/jabrena/cursor-rules-java/004-commands-installation)
- [`@005-agents-installation`](https://www.skills.sh/jabrena/cursor-rules-java/005-agents-installation)

A team can use those workflows to align on:

- Which commands are available in the repository
- Which agents should be installed in Cursor or Claude
- Which skills support each delivery workflow
- Which artifacts are generated and which are source-controlled
- Which validations must run before a change is promoted

The benefit is not only convenience. It is shared language.

Instead of every developer inventing a different prompt for planning, ADRs, diagrams, profiling, benchmarking, feature branches, worktrees, or issue implementation, the project can provide a common command and agent layer.

<a id="new-capabilities-for-java-enterprise-frameworks"></a>

## New capabilities for Java Enterprise Frameworks

`0.16.0` keeps the framework support organized around the three Java Enterprise stacks used by the project: `Spring Boot 4.0.x`, `Quarkus 3.x`, and `Micronaut 4.x`.

The value is that teams can pick the framework lane first, then apply the matching skills for project creation, REST APIs, validation, security, persistence, messaging, migrations, MongoDB, and tests.

Spring Boot skills:

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

Quarkus skills:

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

Micronaut skills:

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

<a id="increasing-the-engineering-awareness-with-eu-regulations"></a>

## Increasing the Engineering awareness with EU regulations

`0.16.0` introduces a new alpha family of regulation engineering review skills:

- [`@801-regulations-eu-ai-act`](https://www.skills.sh/jabrena/cursor-rules-java/801-regulations-eu-ai-act)
- [`@802-regulations-dora`](https://www.skills.sh/jabrena/cursor-rules-java/802-regulations-dora)
- [`@803-regulations-gdpr`](https://www.skills.sh/jabrena/cursor-rules-java/803-regulations-gdpr)
- [`@804-regulations-eu-nis2`](https://www.skills.sh/jabrena/cursor-rules-java/804-regulations-eu-nis2)
- [`@805-regulations-eu-cyber-resilience-act`](https://www.skills.sh/jabrena/cursor-rules-java/805-regulations-eu-cyber-resilience-act)
- [`@806-regulations-eu-data-act`](https://www.skills.sh/jabrena/cursor-rules-java/806-regulations-eu-data-act)
- [`@807-regulations-eu-digital-services-act`](https://www.skills.sh/jabrena/cursor-rules-java/807-regulations-eu-digital-services-act)
- [`@808-regulations-eu-digital-markets-act`](https://www.skills.sh/jabrena/cursor-rules-java/808-regulations-eu-digital-markets-act)

These skills are engineering review aids. They do not provide legal advice and they do not replace qualified legal, compliance, privacy, security, risk, product, or governance owners.

They help Java teams prepare better evidence:

- What system is in scope?
- What data is processed?
- Where are AI models, RAG, agents, or tool calls used?
- What operational controls exist?
- Which owners need to review the decision?
- What gaps should block or shape a release?

For distributed systems using GenAI tools, a practical review set is:

- `EU AI Act` when the system uses AI models, LLMs, RAG, AI agents, tool calling, or generated decision support.
- `GDPR` when the system processes personal data in prompts, logs, embeddings, retrieval sources, exports, backups, or generated outputs.
- `NIS2` when the system supports essential or important services, cybersecurity incident flows, supply-chain dependencies, or critical operations.
- `DORA` when the system supports financial entities, important business services, ICT risk, third-party ICT providers, or operational resilience evidence.
- `Cyber Resilience Act` when a product, library, agent tool, or software component may be distributed as a product with digital elements.
- `Data Act` when the system exposes data access, sharing, portability, cloud switching, connected-product data, or interoperability workflows.
- `Digital Services Act` when the system supports hosting, platforms, marketplaces, content moderation, recommender systems, advertising, or transparency reporting.
- `Digital Markets Act` when the system supports gatekeeper-platform concerns, core platform services, interoperability, data access, ranking, or self-preferencing controls.

Further information: [Introduction to EU regulations Part I](/cursor-rules-java/blog/2026/06/introduction-to-eu-regulations-part-i.html)

## Next steps

The next phase is already visible in the [`v0.17.0` milestone](https://github.com/jabrena/cursor-rules-java/milestone/11). The backlog continues the same direction as `0.16.0`: make agent workflows more useful, more deterministic, and easier to adopt in real teams.

Functionally, the next workstreams are:

- Expand executable acceptance coverage for `Skills`, `Agents`, and `Commands`, so important behavior is checked with stable Gherkin scenarios instead of relying only on package shape or manual review.
- Complete the EU regulation review family, so teams can map distributed-system and GenAI decisions against a broader set of engineering evidence patterns.
- Extend Maven guidance with JavaMoney support in the Maven plugin workflow, improving how teams introduce money and currency handling into enterprise builds.
- Add LLM engineering references such as Karpathy's LLM Wiki, so agent and skill guidance can stay closer to widely used mental models for building with language models.
- Improve analysis methods, including the hamburger method and the two-step method, so discovery work can become more structured before teams generate ADRs, specs, plans, or implementation tasks.

Enjoy.
