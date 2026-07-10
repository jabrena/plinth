---
name: 002-agents-inventory
description: Use when you need to generate a checklist document with embedded agents inventory, following the embedded template exactly and producing INVENTORY-AGENTS-JAVA.md in the project root.
license: Apache-2.0
metadata:
  author: Juan Antonio Breña Moral
  version: 0.17.0
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
| `robot-business-analyst` | Create GitHub or Jira issues and perform read-only alignment and readiness reviews. |
| `robot-architect` | Explore designs, record architecture decisions, and create architecture diagrams. |
| `robot-tech-lead` | Create plans or OpenSpec changes and coordinate delivery through specialized coders. |
| `robot-no-java` | Implement non-Java changes when issue, plan, or spec work does not use Java. |
| `robot-java-performance` | Coordinate profiling, benchmarking, evidence, and approved performance optimization delegation. |
| `robot-java-coder` | Implement framework-agnostic Java changes and refactors. |
| `robot-java-micronaut-coder` | Implement Micronaut-specific code and architecture changes. |
| `robot-java-quarkus-coder` | Implement Quarkus-specific code and architecture changes. |
| `robot-java-spring-boot-coder` | Implement Spring Boot-specific code and architecture changes. |

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