---
name: 030-architecture-adr-general
description: Use when you need to generate Architecture Decision Records (ADRs) for a Java project through an interactive, conversational process that systematically gathers context, stakeholders, options, and outcomes to produce well-structured ADR documents. Part of the skills-for-java project
license: Apache-2.0
metadata:
  author: Juan Antonio Breña Moral
  version: 0.13.0-SNAPSHOT
---
# Java ADR Generator with interactive conversational approach

Generate Architecture Decision Records (ADRs) for Java projects through an interactive, conversational process that systematically gathers all necessary context to produce well-structured ADR documents. **This is an interactive SKILL**.

**What is covered in this Skill?**

- ADR file storage configuration
- Conversational information gathering: context, stakeholders, decision drivers, options with pros/cons, outcome, consequences
- MADR template generation
- Validation with `./mvnw validate` or `mvn validate` before proceeding

## Workflow

1. **Validate project** — Run `./mvnw validate` or `mvn validate`. If validation fails, stop and resolve errors before proceeding.
2. **ADR Preferences Assessment** — Ask the user where to store ADR files, what numbering convention to use, and confirm selections before continuing.
3. **Interactive ADR Generation** — Gather information through targeted questions asked one-by-one: context and problem statement, stakeholders (decision-makers, consulted, informed), decision drivers, considered options with pros/cons, chosen option with justification, consequences, and confirmation criteria.
4. **Document Generation** — Produce the ADR file using the MADR template, replacing all placeholders with gathered content and using the current date.
5. **Validation and Summary** — Verify all sections are filled, no placeholders remain, the file is in the correct location, and provide a summary report with next steps.

## Quick Reference

MADR template structure:

```markdown
---
status: "{proposed | accepted | deprecated | superseded by ADR-NNNN}"
date: 2024-01-15
decision-makers: [list of people]
consulted: [subject-matter experts]
informed: [stakeholders for one-way updates]
---

# Short title of solved problem and solution

## Context and Problem Statement
## Decision Drivers
## Considered Options
## Decision Outcome
### Consequences
### Confirmation
## Pros and Cons of the Options
## More Information
```

## Constraints

Before applying any ADR generation, ensure the project validates. If validation fails, stop immediately — do not proceed until all validation errors are resolved.

- **MANDATORY**: Run `./mvnw validate` or `mvn validate` before applying any ADR generation
- **SAFETY**: If validation fails, stop immediately — do not proceed until all validation errors are resolved
- **BEFORE APPLYING**: Read the reference for detailed good/bad examples, constraints, and safeguards for each ADR generation pattern

## When to use this skill

- Generate ADR
- Create Architecture Decision Record
- Document architecture decision
- Architecture Decision Record for Java
- Write ADR
- Document technical decision
- Architecture documentation
- Record design decision
- Technology choice documentation
- Framework selection ADR
- Database choice ADR
- Architectural trade-offs
- Technical alternatives evaluation
- Why did we choose
- Deployment strategy ADR
- Infrastructure choice
- Vendor selection ADR

## Reference

For detailed guidance, examples, and constraints, see [references/030-architecture-adr-general.md](references/030-architecture-adr-general.md).
