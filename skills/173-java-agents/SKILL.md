---
name: 173-java-agents
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

**Multi-step scope:** Step 1 assesses project requirements through 6 targeted questions (role/expertise, tech stack, directory layout, key commands, Git workflow, contributor boundaries) — all questions must be answered in strict order. Step 2 generates the AGENTS.md by mapping each answer to the corresponding section, handles existing files via overwrite/merge/backup strategies, validates all 6 sections, and confirms boundaries use ✅/⚠️/🚫 icons.

## Constraints

No Maven validation is required before generating AGENTS.md. Review the project structure and existing documentation before starting to provide accurate answers during Step 1.

- **BEFORE STARTING**: Review the project structure and existing documentation to provide accurate answers during Step 1
- **BEFORE APPLYING**: Read the reference for detailed good/bad examples, constraints, and safeguards for each AGENTS.md generation pattern

## Reference

For detailed guidance, examples, and constraints, see [references/173-java-agents.md](references/173-java-agents.md).
