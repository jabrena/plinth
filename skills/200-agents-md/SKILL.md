---
name: 200-agents-md
description: Use when you need to generate an AGENTS.md file for a Java repository — covering project conventions, tech stack, file structure, commands, Git workflow, and contributor boundaries — through a modular, step-based interactive process that adapts to your specific project needs. Part of the skills-for-java project
license: Apache-2.0
metadata:
  author: Juan Antonio Breña Moral
  version: 0.13.0-SNAPSHOT
---
# AGENTS.md Generator for Java repositories

Generate a comprehensive AGENTS.md file for Java repositories through a modular, step-based interactive process that covers role definition, tech stack, file structure, commands, Git workflow, and contributor boundaries. **This is an interactive SKILL**.

**What is covered in this Skill?**

- AGENTS.md generation for Java repositories of any complexity
- Role and expertise definition for AI agents and contributors
- Tech stack documentation: language, build tool, frameworks, pipelines
- File structure mapping with read/write boundaries
- Command catalogue for build/test/deploy/run workflows
- Git workflow conventions: branching strategy, commit message format
- Contributor boundaries using ✅ Always do / ⚠️ Ask first / 🚫 Never do formatting

## Workflow

1. **Review project structure** — Examine the repository layout, existing documentation, and configuration before starting.
2. **Requirements Assessment** — Ask 6 questions one-by-one in strict order: (1) role/expertise, (2) tech stack, (3) file structure rules, (4) essential commands, (5) Git workflow, (6) project boundaries. Wait for each response before proceeding.
3. **Confirm understanding** — Summarize the user's answers and confirm before generating the file.
4. **Generate AGENTS.md** — Map each answer to its corresponding section (Your role, Tech stack, File structure, Commands, Git workflow, Boundaries) and write the file to the project root.
5. **Validate output** — Verify all 6 sections are present, markdown formatting is correct, and boundaries use the required formatting.

## Quick Reference

AGENTS.md section template:

```markdown
# Agent Quickstart Guide

## Your role
You are [role from Q1].

## Tech stack
- **Language:** Java 21
- **Build:** Maven

## File structure
- `src/main/` – WRITE here
- `target/` – READ only, never edit

## Commands
# Build and verify
./mvnw clean verify

## Git workflow
- Conventional Commits

## Boundaries
- Always do: run ./mvnw clean verify before promoting
- Ask first: new config files, modifying generators
- Never do: edit generated output, commit secrets
```

## Constraints

No Maven validation is required before generating AGENTS.md. Review the project structure and existing documentation before starting to provide accurate answers during Step 1.

- **BEFORE STARTING**: Review the project structure and existing documentation to provide accurate answers during Step 1
- **BEFORE APPLYING**: Read the reference for detailed good/bad examples, constraints, and safeguards for each AGENTS.md generation pattern

## When to use this skill

- Create AGENTS.md
- Update AGENTS.md file
- Add agent instructions

## Reference

For detailed guidance, examples, and constraints, see [references/200-agents-md.md](references/200-agents-md.md).
