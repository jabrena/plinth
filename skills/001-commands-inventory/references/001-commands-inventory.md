---
name: 001-commands-inventory
description: Use when you need to generate a checklist document with embedded commands inventory, following the embedded template exactly and producing INVENTORY-COMMANDS-JAVA.md in the project root.
license: Apache-2.0
metadata:
  author: Juan Antonio Breña Moral
  version: 0.17.0
---
# Create a Checklist with embedded commands inventory for Java

## Role

You are a Senior software engineer with extensive experience in Java software development

## Goal

Your task is to create a comprehensive checklist that follows the exact format
and structure defined in the embedded template below. Create a markdown file named
`INVENTORY-COMMANDS-JAVA.md` with the following content:

```markdown
# Embedded Commands Inventory

## Goal

Provide a quick checklist of the embedded commands available for installation in this repository.

## Embedded commands

| Command | SDLC phase | Primary purpose |
| --- | --- | --- |
| `/update-issue` | Analysis / Design | Update existing GitHub or Jira issues with structured user story, acceptance criteria, and resource content. |
| `/create-feature-branch` | Analysis / Design to Implementation | Create and switch to a conventionally named branch for repository-backed analysis, design, or implementation. |
| `/create-worktree` | Analysis / Design to Implementation | Create an isolated branch and linked worktree for parallel work. |
| `/explore-design` | Design | Compare technical approaches and obtain an approved design direction. |
| `/create-adr` | Design | Record an architectural decision, alternatives, rationale, and consequences. |
| `/create-diagram` | Design | Create a focused architecture or design diagram from approved artifacts. |
| `/create-spec` | Analysis / Design | Create or update one or more validated OpenSpec changes. |
| `/review-alignment` | Analysis / Design | Review available artifacts for traceability, consistency, completeness, and readiness. |
| `/implement-spec` | Implementation | Deliver an approved plan or validated OpenSpec task list through framework-aware delegation. |
| `/profile` | Operation | Coordinate Java profiling from baseline detection through verified optimization. |
| `/benchmark` | Operation | Select and coordinate JMeter, Gatling, or JMH performance workflows. |

## Installation target options

- `.github/commands`
- `.claude/commands`
- `.cursor/command`
- `.codex/commands`

```

## Constraints

**MANDATORY REQUIREMENT**: Follow the embedded template EXACTLY - do not add, remove, or modify any sections, rows, or command entries not explicitly shown in the template.

- **DO NOT** create additional rows beyond what's shown in the template
- **DO NOT** add command entries that are not explicitly listed in the embedded template
- **DO NOT** expand or elaborate on sections beyond what the template shows
- **ONLY** use the exact wording and structure from the template


## Output Format

- **File Creation**: Generate the complete markdown file named `INVENTORY-COMMANDS-JAVA.md` in the project root directory
- **Template Adherence**: Follow the embedded template structure and content exactly - no additions, modifications, or omissions
- **File Handling**: If `INVENTORY-COMMANDS-JAVA.md` already exists, overwrite it completely with the new generated content