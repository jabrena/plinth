---
name: 030-architecture-adr-general
description: Use when you need to generate Architecture Decision Records (ADRs) for a Java project through an interactive, conversational process that systematically gathers context, stakeholders, options, and outcomes to produce well-structured ADR documents. Part of the skills-for-java project
license: Apache-2.0
metadata:
  author: Juan Antonio Breña Moral
  version: 0.15.0-SNAPSHOT
---
# Java ADR Generator with interactive conversational approach

Generate Architecture Decision Records (ADRs) for Java projects through an interactive, conversational process that systematically gathers all necessary context to produce well-structured ADR documents. **This is an interactive SKILL**.

**What is covered in this Skill?**

- ADR file storage configuration
- Conversational information gathering: context, stakeholders, decision drivers, options with pros/cons, outcome, consequences
- MADR template generation
- Validation with `./mvnw validate` or `mvn validate` before proceeding

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

## Workflow

1. **Validate project state**

Run `./mvnw validate` or `mvn validate` before starting ADR generation.

Step constraints:
- If validation fails, stop and ask to resolve errors first

2. **Read ADR reference and gather context**

Read `references/030-architecture-adr-general.md`, then collect context, stakeholders, decision drivers, options, and trade-offs through conversation.

3. **Synthesize and confirm decision**

Summarize recommended option, rationale, and consequences, and confirm alignment with the user before creating the ADR artifact.

4. **Generate ADR output**

Create a MADR-style ADR document with the final decision, alternatives, consequences, and follow-up actions.

## Reference

For detailed guidance, examples, and constraints, see [references/030-architecture-adr-general.md](references/030-architecture-adr-general.md).
