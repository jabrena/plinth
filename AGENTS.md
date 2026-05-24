# Contributor Quickstart Guide

## Your role

You are an expert Java developer and technical writer for this project.

- You understand Java 25, Maven, XML, XSLT, and Markdown
- You help maintain and extend a collection of cursor rules for Java Enterprise development
- Cursor rules under `.cursor/rules/` are legacy checked-in content — you edit XML in `skills-generator`, not those Markdown files
- Skills live in `skills/` but are **generated** — you edit XML sources, not the output

## Tech stack

- **Language:** Java 25
- **Build:** Maven (wrapper: `./mvnw`)
- **Rule pipeline:** XML → XInclude → XSLT → Markdown cursor rules
- **Skill scanning:** `skill-check@latest` plus `cisco-ai-skill-scanner` behavioral scans
- **Site generator:** JBake 2.7.0 with FreeMarker templates → GitHub Pages

### Framework version baseline

- **Spring Boot:** Target **4.0.x** by default;
- **Quarkus:** Target the current **3.x** line by default;
- **Micronaut:** Target the current **4.x** line by default;

## Change workflow

This project uses **OpenSpec** for structured change management and planning:

**GitHub Issues → OpenSpec → Agents + Skills**

1. **GitHub Issues** - Track problems, features, and architectural decisions
2. **OpenSpec** (`documentation/openspec/`) - Plan and coordinate implementation through:
   - **Proposals** - Problem statements and solution approaches
   - **Specs** - Requirements with Given/When/Then scenarios
   - **Tasks** - Detailed implementation checklists
3. **Agents + Skills** - Generate the actual cursor rules and agent skills

**Key principle**: Complex changes (especially architectural ones) should be planned in OpenSpec before implementation to ensure thorough analysis and stakeholder alignment.

## File structure

- `skills/` – Generated SKILLS (READ only, never edit directly)
- `.cursor/rules/` – Legacy Cursor rules (READ only; not produced by the default Maven install after generator consolidation)
- `skills-generator/src/main/resources/` – XML rule sources (`skill-references/`, `skills/`, XSLT) and skill inventory (WRITE here to change rules and skills)
- `skills-generator/` – Unified generator module: builds agent skills into `skills/` (WRITE)
- `documentation/openspec/` – OpenSpec change management (proposals, specs, tasks) (WRITE)
- `documentation/adr/` – Architecture Decision Records (WRITE)
- `site-generator/content/` – Blog posts, courses, documentation (WRITE here to update website)
- `docs/` – Generated static website for GitHub Pages (READ only)

## Commands

```bash
# Build and test everything
./mvnw clean verify

# Build and test only the unified generator module
./mvnw clean verify -pl skills-generator

# Deploy generated skills to skills/
./mvnw clean install -pl skills-generator

# Serve the website locally (mirrors GitHub Pages path: http://localhost:8820/)
./mvnw clean generate-resources jbake:inline -pl site-generator -P local-preview

# Regenerate the website into docs/
./mvnw clean generate-resources -pl site-generator -P site-update

# Validate agent skills with the same formatter used in CI
npx skill-check@latest skills --no-security-scan --format github

# Install the skill scanner used by CI
python -m pip install --upgrade cisco-ai-skill-scanner

# Scan generated skills with behavioral strict policy
skill-scanner scan-all ./skills --recursive --use-behavioral --policy strict --fail-on-severity high

# OpenSpec change management (run from documentation/ directory)
cd documentation/
openspec list                        # List all changes and their progress
openspec show <change-name>          # Show details of a specific change
openspec validate --all              # Validate all changes meet requirements
openspec new change <change-name>    # Create a new change
openspec archive <change-name>       # Archive a completed change

```

## Git workflow

- **Conventional Commits**: Use conventional commit format for all commit messages
- Format: `type(scope): description`

| Type | Typical use |
|------|-------------|
| **feat** | New behavior / user-facing capability |
| **fix** | Bug fix |
| **docs** | Documentation only |
| **style** | Formatting, whitespace, etc. (no logic change) |
| **refactor** | Internal change, same external behavior |
| **perf** | Performance improvement |
| **test** | Tests only |
| **build** | Build system or dependencies (e.g. Maven, Gradle) |
| **ci** | CI config (workflows, pipelines) |
| **chore** | Maintenance, tooling, meta (when nothing else fits) |
| **revert** | Reverts a previous commit |

The [Conventional Commits](https://www.conventionalcommits.org/en/v1.0.0/) spec allows other types if your team agrees. **This repository’s commit-msg hook** accepts the types in the table above and requires a **scope** (see [`.pre-commit-config.yaml`](.pre-commit-config.yaml)).

### Pre-commit hooks (recommended)

This repository includes [pre-commit](https://pre-commit.com/) configuration at [`.pre-commit-config.yaml`](.pre-commit-config.yaml): YAML checks and a **commit-msg** hook that enforces the [Conventional Commits](https://www.conventionalcommits.org/en/v1.0.0/) rules above (including a required **scope**).

**Setup (once per clone):**

```bash
pip install pre-commit   # or: brew install pre-commit
pre-commit install --install-hooks
```

The install registers both the default `pre-commit` stage and `commit-msg` hooks. To validate the latest commit message manually:

```bash
git log -1 --pretty=%B > /tmp/msg.txt
pre-commit run conventional-pre-commit --hook-stage commit-msg --commit-msg-filename /tmp/msg.txt
```

## Boundaries

- ✅ **Always do:** Edit XML in `skills-generator/src/main/resources/` (`skill-references/`, `skills/`) to change rules and skills, run `./mvnw clean verify` before promoting changes. For skill changes, also run `npx skill-check@latest skills --no-security-scan --format github` and the `skill-scanner scan-all ./skills --recursive --use-behavioral --policy strict --fail-on-severity high` scan when the scanner is available. When editing XML, follow PML Schema: [https://jabrena.github.io/pml/schemas/0.7.0/pml.xsd](https://jabrena.github.io/pml/schemas/0.7.0/pml.xsd). For complex changes, create OpenSpec proposals first.
- ⚠️ **Ask first:** Adding new XML rule files, modifying the XSLT stylesheet, changing site templates, architectural changes (use OpenSpec for planning)
- 🚫 **Never do:** Edit `.cursor/rules/` or `docs/` directly, commit secrets, skip tests before promoting, bypass OpenSpec for major changes
