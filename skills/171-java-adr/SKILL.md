---
name: 171-java-adr
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

**Multi-step scope:** Step 1 assesses ADR preferences (storage location, template format). Step 2 generates the ADR through conversation: Phase 0 retrieves current date; Phase 1 gathers information one question at a time (decision topic, context, stakeholders — deciders/consulted/informed, decision drivers, options with pros and cons, chosen option with rationale, implementation consequences); Phase 2 produces the final ADR using the MADR template. Step 3 validates the ADR and produces a summary.

## Constraints

Before applying any ADR generation, ensure the project validates. If validation fails, stop immediately — do not proceed until all validation errors are resolved.

- **MANDATORY**: Run `./mvnw validate` or `mvn validate` before applying any ADR generation
- **SAFETY**: If validation fails, stop immediately — do not proceed until all validation errors are resolved
- **BEFORE APPLYING**: Read the reference for detailed good/bad examples, constraints, and safeguards for each ADR generation pattern

## Reference

For detailed guidance, examples, and constraints, see [references/171-java-adr.md](references/171-java-adr.md).
