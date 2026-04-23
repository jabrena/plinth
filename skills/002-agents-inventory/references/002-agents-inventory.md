---
name: 002-agents-inventory
description: Use when you need to generate a checklist document with embedded agents inventory, following the embedded template exactly and producing INVENTORY-AGENTS-JAVA.md in the project root.
license: Apache-2.0
metadata:
  author: Juan Antonio Breña Moral
  version: 0.15.0-SNAPSHOT
---
# Create a Checklist with embedded agents inventory for Java

## Role

You are a Senior software engineer with extensive experience in Java software development

## Goal

Your task is to create a comprehensive checklist that follows the exact format
and structure defined in the embedded template below. Create a markdown file named
`INVENTORY-AGENTS-JAVA.md` with the following content:

```markdown
# Embedded Agents Inventory

## Goal

Provide a quick checklist of the embedded agents available for installation in this repository.

## Embedded agents

| Agent | Primary purpose |
| --- | --- |
| `robot-business-analyst` | Analyze requirements, user stories, and implementation plans. |
| `robot-coordinator` | Coordinate Java enterprise implementation flow across specialized coders. |
| `robot-java-coder` | Implement framework-agnostic Java changes and refactors. |
| `robot-micronaut-coder` | Implement Micronaut-specific code and architecture changes. |
| `robot-quarkus-coder` | Implement Quarkus-specific code and architecture changes. |
| `robot-spring-boot-coder` | Implement Spring Boot-specific code and architecture changes. |

## Installation target options

- `.cursor/agents`
- `.claude/agents`

```

## Constraints

**MANDATORY REQUIREMENT**: Follow the embedded template EXACTLY - do not add, remove, or modify any sections, rows, or agent entries not explicitly shown in the template.

- **DO NOT** create additional rows beyond what's shown in the template
- **DO NOT** add agent entries that are not explicitly listed in the embedded template
- **DO NOT** expand or elaborate on sections beyond what the template shows
- **ONLY** use the exact wording and structure from the template

## Output Format

- **File Creation**: Generate the complete markdown file named `INVENTORY-AGENTS-JAVA.md` in the project root directory
- **Template Adherence**: Follow the embedded template structure and content exactly - no additions, modifications, or omissions
- **File Handling**: If `INVENTORY-AGENTS-JAVA.md` already exists, overwrite it completely with the new generated content