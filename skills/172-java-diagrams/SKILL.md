---
name: 172-java-diagrams
description: Use when you need to generate Java project diagrams — including UML sequence diagrams, UML class diagrams, C4 model diagrams, and UML state machine diagrams — through a modular, step-based interactive process that adapts to your specific visualization needs. Part of the skills-for-java project
license: Apache-2.0
metadata:
  author: Juan Antonio Breña Moral
  version: 0.13.0-SNAPSHOT
---
# Java Diagrams Generator with modular step-based configuration

Generate comprehensive Java project diagrams through a modular, step-based interactive process that covers UML sequence diagrams, UML class diagrams, C4 model diagrams, and UML state machine diagrams using PlantUML syntax. **This is an interactive SKILL**.

**What is covered in this Skill?**

- UML sequence diagram generation for application workflows and API interactions
- UML class diagram generation for package structure and class relationships
- C4 model diagram generation at Context/Container/Component/Code abstraction levels
- UML state machine diagram generation for entity lifecycles and business workflows
- PlantUML syntax for all diagram types
- File organization strategies: single-file, separate-files, or integrated with existing documentation
- Final diagram validation with PlantUML syntax checking

**Multi-step scope:** Step 1 assesses diagram preferences. Step 2 generates UML sequence diagrams — conditionally executed if selected. Step 3 generates UML class diagrams — conditionally executed if selected. Step 4 generates C4 model diagrams — conditionally executed if selected. Step 5 generates UML state machine diagrams — conditionally executed if selected. Step 6 validates all generated diagrams and produces a comprehensive summary.

## Constraints

Before applying any diagram generation, ensure the project validates. If validation fails, stop immediately — do not proceed until all validation errors are resolved.

- **MANDATORY**: Run `./mvnw validate` or `mvn validate` before applying any diagram generation
- **SAFETY**: If validation fails, stop immediately — do not proceed until all validation errors are resolved
- **BEFORE APPLYING**: Read the reference for detailed good/bad examples, constraints, and safeguards for each diagram generation pattern

## Reference

For detailed guidance, examples, and constraints, see [references/172-java-diagrams.md](references/172-java-diagrams.md).
