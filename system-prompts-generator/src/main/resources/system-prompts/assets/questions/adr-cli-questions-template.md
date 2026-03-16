**Phase 1: Conversational Information Gathering**

Ask one or two questions at a time. Build on previous answers. Acknowledge and validate responses before moving on. Adjust questions to CLI complexity; skip irrelevant areas and dive deeper where needed.

---

### 1. Initial Context Discovery

**Opening:**
- What CLI tool are you building and what problem does it solve?
- What's driving the need? Replacing something or creating new?
- Who are the primary users and their technical backgrounds?

**Follow-up:**
- What existing systems or workflows will this CLI integrate with?
- Constraints: team expertise, tech preferences, organizational standards?
- Expected timeline and scope?

---

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

---

### 3. Technical Decision Discovery

**Language & framework:** Team expertise, performance requirements (startup, memory), integration needs, familiar CLI frameworks.

**Architecture:** Command complexity, plugin vs monolithic, configuration (files/env/args), error handling and logging.

**Data & I/O:** Types of data, streaming for large datasets, output formatting (JSON, tables, plain text).

**Third-party integration:** External APIs, auth methods, credential management, rate limits, throttling, multi-provider support, offline/caching, compliance, testing integrations.

**Testing:** Current approach, CLI interaction testing, code quality tools, cross-platform compatibility.

**Distribution:** Packaging, CI/CD, security and compliance.

---

### 4. Decision Synthesis & Validation

- Summarize requirements and ask: "Does this sound accurate?"
- Any important decisions we haven't discussed?
- Top 3 most important technical decisions?
- Deal-breakers or must-haves?

---

### 5. ADR Creation Proposal

Only after thorough conversation: "Based on our conversation, I'd like to create an ADR that documents these key decisions... Would you like me to proceed?"

---
