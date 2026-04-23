**Question 1**: What role or expertise should the AI/agent have when helping with this project?

Options:
- Java developer + technical writer (documentation, rules, conventions)
- Java backend engineer (APIs, services, databases)
- Full-stack Java developer (backend + frontend)
- DevOps/Platform engineer (build, deploy, CI/CD)
- Architecture-focused (design, diagrams, ADRs)
- Custom (specify the expertise and persona)

---

**Question 2**: What tech stack should be documented in the AGENTS.md file?

Options:
- Language and version (e.g., Java 17, Java 21)
- Build tool (Maven, Gradle, other)
- Key frameworks (Spring Boot, Quarkus, Micronaut, etc.)
- Rule or template pipeline (if applicable)
- Site or doc generators (if applicable)
- All of the above (I'll provide details)

---

**Question 3**: What are the key directories and file structure rules for this project?

Options:
- Generated output directories (READ only, never edit directly)
- Source directories (WRITE here to change rules/code)
- Examples, docs, or content directories
- Custom structure (I'll describe the layout)

---

**Question 4**: What essential commands should contributors run frequently?

Options:
- Build and verify: `./mvnw clean verify` or equivalent
- Deploy or install: `./mvnw clean install` or equivalent
- Generate resources or rules
- Serve or run locally
- Custom commands (I'll list them)

---

**Question 5**: What Git workflow should contributors follow?

Options:
- Chris Beams style commit messages (subject line ≤ 50 chars, body wraps at 72)
- Conventional Commits
- PR checklist: What changed?, Why?, Breaking changes?
- Comments as complete sentences ending with a period
- Custom (I'll specify the requirements)

---

**Question 6**: What are the project boundaries the AI should respect?

Options:
- **Always do**: Key practices (e.g., edit XML sources, run `./mvnw clean verify` before promoting)
- **Ask first**: Changes that need approval (e.g., new config files, modifying generators)
- **Never do**: Forbidden actions (e.g., edit generated output directly, commit secrets, skip tests)
- Custom (I'll define the boundaries)

---
