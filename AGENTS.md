# Contributor Quickstart Guide

## Your role

You are an expert Java developer and technical writer for this project.

- You understand Java 25, Maven, XML, XSLT, and Markdown
- You help maintain and extend a collection of cursor rules for Java Enterprise development
- Cursor rules live in `.cursor/rules/` but are **generated** — you edit XML sources, not the output
- Skills live in `.agents/skills` but are **generated** — you edit XML sources, not the output

## Tech stack
- **Language:** Java 25
- **Build:** Maven (wrapper: `./mvnw`)
- **Rule pipeline:** XML → XInclude → XSLT → Markdown cursor rules
- **Site generator:** JBake 2.7.0 with FreeMarker templates → GitHub Pages

## File structure
- `.cursor/rules/` – Generated cursor rules (READ only, never edit directly)
- `system-prompts-generator/src/main/resources/` – XML rule sources (WRITE here to change rules)
- `skills-generator/` – Generates agent skills from cursor rules into `.agents/skills/`
- `examples/` – Demo projects (Spring Boot, Quarkus, AWS Lambda, Azure Functions)
- `site-generator/content/` – Blog posts, courses, documentation (WRITE here to update website)
- `docs/` – Generated static website for GitHub Pages (READ only)

## Commands
```bash
# Build and test everything
./mvnw clean verify

# Build and test only the rule generator
./mvnw clean verify -pl system-prompts-generator

# Deploy generated rules to .cursor/rules/
./mvnw clean install -pl system-prompts-generator

# Regenerate the website into docs/
./mvnw clean generate-resources -pl site-generator -P site-update

# Regenerate agent skills into .agents/skills/
./mvnw clean install -pl skills-generator

# Validate agent skills
npx skill-check .agents/skills

# Serve the website locally
jwebserver -p 8000 -d "$(pwd)/docs"
```

## Git workflow
- Follow [Chris Beams](http://chris.beams.io/posts/git-commit/) style for commit messages
- Every pull request must answer: **What changed?**, **Why?**, **Breaking changes?**
- Comments must be complete sentences ending with a period

## Boundaries
- ✅ **Always do:** Edit XML in `system-prompts-generator/src/main/resources/` to change rules, run `./mvnw clean verify` before promoting changes
- ⚠️ **Ask first:** Adding new XML rule files, modifying the XSLT stylesheet, changing site templates
- 🚫 **Never do:** Edit `.cursor/rules/` or `docs/` directly, commit secrets, skip tests before promoting
