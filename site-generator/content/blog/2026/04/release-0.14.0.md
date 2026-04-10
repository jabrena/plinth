title=What's new in Cursor rules for Java 0.14.0?
date=2026-04-13
type=post
tags=blog
author=Juan Antonio Breña Moral
status=published
~~~~~~

## What are Cursor rules for Java?

A curated collection of `Skills` and `Agents` to be used in modern `SDLC` workflows for Java Enterprise development.

With appreciation for our community located in `Singapore`, `Copenhagen`, `Quito`, `Shah Alam`, and `Amstelveen`. 👋👋👋

## What's new in this release?

### Rules support dropped in favor of Skills

It is a bit weird to explain that the project is named `Cursor rules for Java`, but one of the changes in this minor release is to drop support for Rules.

**Why were Cursor rules dropped?**

Until this release, the project maintained 3 very different deliverables:

- Rules
- Skills
- Agents

`Rules` and `Skills` both guide model behaviour in broadly similar ways, but Rules were Cursor’s approach while `Skills` have recently become the standard, so consolidating on a single solution makes more sense. **On the other hand**, maintaining a single generator (`skills-generator`) made it possible to improve how skills are packaged, which was somewhat constrained when both `rules-generator` and `skills-generator` existed.

```bash
skill-name/
├── SKILL.md          # Required: metadata + instructions
├── scripts/          # Optional: executable code
├── references/       # Optional: documentation
├── assets/           # Optional: templates, resources
└── ...               # Any additional files or directories
```

**Source:** https://agentskills.io/specification

The last generated rules are preserved in release 0.13.0: https://github.com/jabrena/cursor-rules-java/releases/tag/0.13.0; you can download them there if needed.

### Improvements in the Agile process

When you create a `User Story`, the flow not only generates the usual structure plus acceptance criteria in `Gherkin` format; it also reviews the user story as a whole using `INVEST`. INVEST is an acronym used in Agile to evaluate the quality of a user story, ensuring it is `Independent`, `Negotiable`, `Valuable`, `Estimable`, `Small`, and `Testable`.

**Skills:**

- `@014-agile-user-story`

### Improvements in the planning process

It is tedious to copy issue details from your issue tracker into the context of whichever AI tool you use. With that in mind, you can now access assigned issues more easily if you work with `GitHub Issues` or `Jira`. Those `Skills` combine well with `@014-agile-user-story` to turn the classic `Anemic one line User Story` into something richer.

Once the information is `ready for development`, you can convert it into a `Change` or `Delta` using `OpenSpec`.

![](/cursor-rules-java/images/2026/4/issue-to-openspec.png)

#### How does OpenSpec work?

OpenSpec is a spec-driven change workflow: you capture what you want and how you will verify it before (or alongside) changing code and generators.

The scaffolding for any project using OpenSpec looks like this:

```bash
openspec/
 └── changes/
      └── add-profile-filters/
           ├── proposal.md
           ├── tasks.md
           └── specs/profile/spec.md
 └── specs/
config.yaml
```

Commands you will use often:

```bash
openspec init
openspec list
openspec status --change <change-id>
openspec show <change-name>
openspec new change <change-name>
openspec validate --all
openspec archive <change-name>
```

Further information about OpenSpec: https://openspec.dev/

#### When to use OpenSpec in daily work?

If you pick up a user story from any issue-tracking tool and it relates to a service you know well, you can use the following workflow directly:

```bash
User story > Create a Change in OpenSpec > Implement with Java Agents
```

![](/cursor-rules-java/images/2026/4/implement-openspec-change.png)

But if you are less sure about the assigned user story, invest more time in the analysis phase:

```bash
User story > Create a Plan > Enhance the plan > Convert into multiple Changes in OpenSpec > Implement with Java Agents
```

Some factors to take into consideration:

- Design incremental Deltas
- Review the tests
- Use TDD. (Consider TCR)
- Do not execute all changes at once. Finish one and review for potential technical debt.

**Skills:**

- `@042-planning-openspec`
- `@043-planning-github-issues`
- `@044-planning-jira`

### Improvements in the Implementation phase

#### Improvements in Maven

Now it is easier to update your `pom.xml` with `@114-java-maven-search`. On the other hand, the Maven-related skills are gradually gaining popularity.

https://skills.sh/?q=maven

**Skills**

- `@114-java-maven-search`

### Reinforced REST API development with new technologies

In the previous release, the project added Agents to implement plans. In this release, you can apply changes in a more granular way, or keep using Plans when the change is small. You can review your REST contracts with `@701-technologies-openapi`, reinforce your integration tests with `@702-technologies-wiremock`, and—most significantly for testing—use the new black-box testing capabilities with `@703-technologies-fuzzing-testing` based on `CATS`.

You can run black-box testing against your development environment using your `OpenAPI` specification.

![](/cursor-rules-java/images/2026/4/cats.png)

Further information about CATS: https://github.com/Endava/cats

**Skills**

- `@701-technologies-openapi`
- `@702-technologies-wiremock`
- `@703-technologies-fuzzing-testing`

### Skill inventory

In version `v0.14.0`, the project ships `68 Skills`.

You can install the `Skills` easily with:

```bash
npx skills add jabrena/cursor-rules-java --all --agent cursor
npx skills add jabrena/cursor-rules-java --all --agent claude-code
```

Once you have the skills installed, you can install the `Agents` with:

```
@003-agents-installation Install Agents in Cursor
@003-agents-installation Install Agents in Claude
```

## What is the next step?

Improve some Skills based on notes from [Tessl](https://tessl.io/registry).

## Do you still have questions about the project?

If you feel stuck using this project or have questions, you can attend the following workshop at `Codemotion Madrid 2026`:

[![](/cursor-rules-java/images/2026/3/codemotion-madrid-2026.jpg)](https://conferences.codemotion.com/madrid/)

https://conferences.codemotion.com/madrid/workshop/
