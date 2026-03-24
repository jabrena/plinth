---
name: 170-java-documentation
description: Use when you need to generate or improve Java project documentation — including README.md files, package-info.java files, and Javadoc enhancements — through a modular, step-based interactive process that adapts to your specific documentation needs. Part of the skills-for-java project
license: Apache-2.0
metadata:
  author: Juan Antonio Breña Moral
  version: 0.13.0-SNAPSHOT
---
# Java Documentation Generator with modular step-based configuration

Generate comprehensive Java project documentation through a modular, step-based interactive process that covers README.md, package-info.java, and Javadoc. **This is an interactive SKILL**.

**What is covered in this Skill?**

- README.md generation for single-module and multi-module Maven projects
- package-info.java creation with basic/detailed/minimal documentation levels
- Javadoc enhancement: comprehensive `@param`/`@return`/`@throws` tags
- File handling strategies: overwrite/add/backup/skip
- Final documentation validation with `./mvnw clean compile` and `./mvnw javadoc:javadoc`

## Workflow

1. **Validate project**: Run `./mvnw validate` or `mvn validate` to ensure the project is in a valid state
2. **Assess documentation needs**: Ask the user which documentation to generate (README.md, package-info.java, Javadoc) and their preferences for detail level and file handling
3. **Generate documentation**: Analyze the codebase and produce the selected documentation types following the user's style and detail preferences
4. **Validate output**: Run `./mvnw clean compile` to verify package-info.java files compile, and `./mvnw javadoc:javadoc` to verify Javadoc generates correctly

## Quick Reference

**README.md template structure for a single-module Maven project:**

```markdown
# [Project Name]

## Software Description
[Analysis of src/main/java: purpose, key classes, architecture]

## Getting Started
### Prerequisites
- Java [version]
- Maven [version]

### Building the Project
./mvnw clean compile

### Running Tests
./mvnw test
```

## Constraints

Before applying any documentation generation, ensure the project validates. If validation fails, stop immediately — do not proceed until all validation errors are resolved.

- **MANDATORY**: Run `./mvnw validate` or `mvn validate` before applying any documentation generation
- **SAFETY**: If validation fails, stop immediately — do not proceed until all validation errors are resolved
- **BEFORE APPLYING**: Read the reference for detailed good/bad examples, constraints, and safeguards for each documentation generation pattern

## When to use this skill

- Review Java code for documentation
- Apply best practices for documentation in Java code

## Reference

For detailed guidance, examples, and constraints, see [references/170-java-documentation.md](references/170-java-documentation.md).
