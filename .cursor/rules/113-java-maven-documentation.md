---
author: Juan Antonio Breña Moral
version: 0.10.0
---
# Create README-DEV.md with information about how to use the Maven project

## Role

You are a Senior software engineer with extensive experience in Java software development

## Goal

When creating a README-DEV.md file for a Maven project, include ONLY the following sections with the specified Maven goals. Do NOT add any additional sections, explanations, or content beyond what is explicitly listed below.

Create a markdown file named `README-DEV.md` with the following content:

```markdown
# Essential Maven Goals:

```bash
# Analyze dependencies
./mvnw dependency:tree
./mvnw dependency:analyze
./mvnw dependency:resolve

./mvnw clean validate -U
./mvnw buildplan:list-plugin
./mvnw buildplan:list-phase
./mvnw help:all-profiles
./mvnw help:active-profiles
./mvnw license:third-party-report

# Clean the project
./mvnw clean

# Clean and package in one command
./mvnw clean package

# Run integration tests
./mvnw clean verify

# Check for dependency updates
./mvnw versions:display-property-updates
./mvnw versions:display-dependency-updates
./mvnw versions:display-plugin-updates

# Generate project reports
./mvnw site
jwebserver -p 8005 -d "$(pwd)/target/site/"
```

```

## Constraints

**MANDATORY REQUIREMENT**: Follow the embedded template EXACTLY - do not add, remove, or modify any steps, sections, or cursor rules that are not explicitly shown in the template. ### What NOT to Include:

- **DO NOT** create additional steps beyond what's shown in the template
- **DO NOT** expand or elaborate on sections beyond what the template shows
- **ONLY** use the exact wording and structure from the template
- If a cursor rule exists in the workspace but is not in the template, **DO NOT** include it

## Output Format

- Generate the complete markdown file following the embedded template exactly
- Use proper markdown formatting with headers, code blocks, tables, and checklists
- **VERIFY**: Final output contains ONLY what appears in the embedded template