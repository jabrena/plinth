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

- **Added SKILL Support:**
- Added initial release with 20 `SKILL`s for Java Enterprise development
- **Improvements in system prompts:**
- Added `@132-java-testing-integration-testing` with WireMock support
- Added `ArchUnit` support in `@111-java-maven-dependencies`
- Centralized version management and best practices for multi-module POM in `@110-java-maven-best-practices`
- Cyclomatic complexity analysis support in `@112-java-maven-plugins`
- `DEVELOPER.md` and plugin catalog in `@113-java-maven-documentation`
- Added support to generate `AGENTS.md` with `@173-java-agents`
- Split Java documentation into ADR capabilities in `@170-java-documentation` and `@171-java-adr`
- Minimum Maven compiler support in `@112-java-maven-plugins`
- **Project improvements:**
- Added a new generator to manage `SKILL` generation
- Added a validator to ensure good quality in `SKILL` valitation against Specification

Let's explain one by one the different features released.

## Why Skills?

When the market about [`AI Assistants`](https://aws.amazon.com/es/blogs/devops/aws-named-as-a-leader-in-the-2025-gartner-magic-quadrant-for-ai-code-assistants/) grow and Software engineers has multiple ways to enrich the experience using models, it is necessary to define a standard for extending AI agent capabilities with specialized knowledge and workflows.

In the past, this project put focus on the concept about `Prompting engineering` and we developed nice `System prompts` (formerly Cursor rules) but after understanding the `SKILL`'s scafolding:

```bash
my-skill/
├── SKILL.md          # Required: instructions + metadata
├── scripts/          # Optional: executable code
├── references/       # Optional: documentation
└── assets/           # Optional: templates, resources
```

It was possible to generate Skills but reusing the previous work from the past.

**What Skills was generated in this release?**

Currently, the project has released 20 Skills:

- `110-java-maven-best-practices`
- `111-java-maven-dependencies`
- `112-java-maven-plugins`
- `113-java-maven-documentation`
- `121-java-object-oriented-design`
- `122-java-type-design`
- `123-java-exception-handling`
- `124-java-secure-coding`
- `125-java-concurrency`
- `128-java-generics`
- `131-java-testing-unit-testing`
- `132-java-testing-integration-testing`
- `141-java-refactoring-with-modern-features`
- `142-java-functional-programming`
- `143-java-functional-exception-handling`
- `144-java-data-oriented-programming`
- `170-java-documentation`
- `171-java-adr`
- `172-java-diagrams`
- `173-java-agents`

In the next release, the project will generate the pending ones:

- `126-java-observability-logging`
- `151-java-performance-jmeter`
- `161-java-profiling-detect`
- `162-java-profiling-analyze`
- `163-java-profiling-refactor`
- `164-java-profiling-compare`
- `164-java-profiling-verify`

## Why Agents.md?

## Improvements in System prompts

## What is the next steps?

- Sub Agents
