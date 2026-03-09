---
name: 020-architecture-functional-requirements-cli
description: Facilitates conversational discovery to create Architectural Decision Records (ADRs) for CLI development. Use when the user wants to document CLI architecture, capture functional requirements for a command-line tool, create ADRs for CLI projects, or design CLI applications with documented decisions.
---

# Create ADRs for CLI Development

Guides stakeholders through a structured conversation to uncover and document technical decisions and functional requirements for CLI applications. The ADR is the documentation of that conversation, not the conversation itself.

---

## Phase 0: Get Current Date

Before starting, run `date` in the terminal to ensure accurate timestamps in the ADR document. Use this for all `[Current Date]` placeholders.

---

## Phase 1: Conversational Information Gathering

Ask one or two questions at a time. Build on previous answers. Acknowledge and validate responses before moving on. Adjust questions to CLI complexity; skip irrelevant areas and dive deeper where needed.

### 1. Initial Context Discovery

**Opening:**
- What CLI tool are you building and what problem does it solve?
- What's driving the need? Replacing something or creating new?
- Who are the primary users and their technical backgrounds?

**Follow-up:**
- What existing systems or workflows will this CLI integrate with?
- Constraints: team expertise, tech preferences, organizational standards?
- Expected timeline and scope?

### 2. Functional Requirements

**Core functionality:**
- Main workflow: what does a user do from start to finish?
- Essential commands or operations?
- Input handling: files, arguments, configuration?
- Output formats and feedback needed?

**User experience:**
- How technical are users? Need extensive help?
- Simple single-purpose tool or multi-command suite?
- Critical error scenarios to handle gracefully?
- How will users install and update?

### 3. Technical Decision Discovery

**Language & framework:** Team expertise, performance requirements (startup, memory), integration needs, familiar CLI frameworks.

**Architecture:** Command complexity, plugin vs monolithic, configuration (files/env/args), error handling and logging.

**Data & I/O:** Types of data, streaming for large datasets, output formatting (JSON, tables, plain text).

**Third-party integration:** External APIs, auth methods, credential management, rate limits, throttling, multi-provider support, offline/caching, compliance, testing integrations.

**Testing:** Current approach, CLI interaction testing, code quality tools, cross-platform compatibility.

**Distribution:** Packaging, CI/CD, security and compliance.

### 4. Decision Synthesis & Validation

- Summarize requirements and ask: "Does this sound accurate?"
- Any important decisions we haven't discussed?
- Top 3 most important technical decisions?
- Deal-breakers or must-haves?

### 5. ADR Creation Proposal

Only after thorough conversation: "Based on our conversation, I'd like to create an ADR that documents these key decisions... Would you like me to proceed?"

---

## Phase 2: ADR Document Generation

Inform the user you will generate the ADR. Use the current date from Phase 0 for all date placeholders.

### ADR Structure

```markdown
# ADR-XXX: [Title]

**Status:** Proposed | Accepted | Deprecated
**Date:** [Current Date]
**Decisions:** [Brief summary]

## Context
[Business context, problem statement, user needs]

## Functional Requirements
[Core commands, workflows, input/output, error handling]

## Technical Decisions
[With rationale for each]

### Language & Framework
[Choice and why]

### Architecture
[Command structure, configuration, plugins]

### Data & Integration
[Processing, external services, auth]

### Testing & Distribution
[Approach and tools]

## Alternatives Considered
[Rejected options and why]

## Consequences
[Impact, trade-offs, follow-up work]

## References
[Links, related ADRs]
```

---

## Phase 3: Next Steps and Recommendations

After generating the ADR:

**Next Steps:**
1. Review and validate with stakeholders
2. Create technical specifications and CLI documentation
3. Set up dev environment and project structure
4. Begin implementation with MVP
5. Establish testing and distribution frameworks

**ADR Management:**
- Keep the ADR as a living document
- Reference during code reviews and architectural discussions
- Plan regular reviews as the CLI evolves
- Link to user stories, requirements, implementation tasks

---

## Key Principles

| Principle | Practice |
|-----------|----------|
| **Discovery over assumption** | Never assume; ask. Understand the "why". Explore edge cases. |
| **Collaborative decisions** | Help stakeholders think through trade-offs. Document reasoning, not just decisions. |
| **Iterative understanding** | Build incrementally. Circle back when new information emerges. |
| **Context-aware** | Tailor to project complexity, team expertise, constraints. |

**Create the ADR when:** Clear context, key decisions identified, alternatives explored, understanding validated.

**Continue the conversation when:** Requirements unclear, decisions arbitrary, alternatives not considered, stakeholders uncertain.
