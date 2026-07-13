**Phase 1: Conversational Information Gathering**

Pose one or two discovery questions at a time. Build on current-session answers. Acknowledge and validate responses before moving on. Adjust depth to interface complexity (CLI, REST/HTTP API, or both); skip irrelevant areas and dive deeper where needed.

---

### 0. Surface discovery (CLI vs REST/HTTP API)

**Before other phases — establish what this ADR covers.**

**Infer from context when possible:** If the user attached a project folder or relevant files, use `list_dir`, `file_search`, `grep`, or `codebase_search` to look for signals, for example:

- **REST/HTTP API:** OpenAPI/Swagger specs, `@RestController`, JAX-RS resources, Spring Web/MVC, Micronaut/Quarkus HTTP, `application.yml` server ports, API modules.
- **CLI:** Picocli, JCommander, Spring Shell, `main` methods with CLI parsing, shell scripts that invoke a jar, GraalVM native-image CLI configs.

**Then:**

- If evidence clearly points to one surface, state your inference briefly and confirm with one question (e.g. "I see REST controllers and OpenAPI—should this ADR focus on the HTTP API?").
- If you see both (e.g. API + admin CLI), confirm whether one ADR covers both or which to prioritize first.
- If there is no codebase in context or signals conflict, ask directly: "Should this ADR focus on a **CLI**, a **REST/HTTP API**, or **both**?"

Record the outcome and use only the matching question branches below for functional requirements and technical discovery.

---

### 1. Initial Context Discovery

**Opening (adapt wording to CLI vs API vs both):**

- What are you building and what problem does it solve?
- What's driving the need—replacement, new capability, or integrations?
- Who are the primary users or consumers, and how technical are they?

**Follow-up:**

- What existing systems, workflows, or data sources will this integrate with?
- Constraints: team expertise, tech preferences, organizational standards, compliance (e.g. GDPR, HIPAA, PCI) if relevant?
- Expected timeline, scope, and success criteria?
- For **APIs**, when relevant: anticipated load (users, requests/sec, data volume)?

---

### 2. Functional Requirements

**When the surface includes CLI:**

**Core functionality:**

- Main workflow: what does a user do from start to finish?
- Essential commands or operations?
- Input handling: files, arguments, configuration?
- Output formats and feedback?

**User experience:**

- How technical are users? Need extensive help?
- Simple single-purpose tool or multi-command suite?
- Critical error scenarios to handle gracefully?
- How will users install and update?

**When the surface includes REST/HTTP API:**

**Core functionality:**

- Main use cases and essential operations?
- Resources and entities to expose; how do they relate?
- Input validation and data transformation needs?
- Response formats and data structures consumers need?

**API design & consumer experience:**

- How technical are consumers? Need extensive docs and SDKs?
- Simple CRUD API or complex business operations?
- Critical error scenarios to handle gracefully?
- How will consumers discover endpoints (docs, discovery, versioning)?
- Need real-time capabilities (webhooks, SSE)?

**When both CLI and API are in scope:** cover both subsections; note how the CLI and API relate (e.g. same domain, operator vs integrator).

---

### 3. Technical Decision Discovery

**Language & framework:** Team expertise, performance (startup/memory for CLI; latency/throughput for APIs), integration with existing systems, familiar stacks.

**When the surface includes REST/HTTP API:**

- **API design & architecture:** Monolithic vs microservices; resource-oriented REST vs GraphQL vs RPC; versioning; bulk ops, filtering, sorting, pagination; synchronous vs async vs webhooks for long-running work.
- **Authentication & security:** JWT, OAuth2, API keys, mTLS; authorization and RBAC; rate limiting, throttling, quotas; securing sensitive data.
- **Data management:** SQL vs NoSQL vs hybrid; caching; validation, serialization, schema evolution; consistency and transactions.
- **Third-party integration:** External services, failure handling, circuit breakers, contract testing, webhooks.
- **Infrastructure & operations:** Containers, cloud, config/secrets, scaling, deployment strategies.
- **Testing & monitoring:** Unit, integration, contract/OpenAPI tests, load testing; health, logging, tracing, alerting, business metrics.

**When the surface includes CLI:**

- **Architecture:** Command structure, plugin vs monolithic, configuration (files/env/args), error handling and logging.
- **Data & I/O:** Data types, streaming for large inputs/outputs, output formatting (JSON, tables, plain text).
- **Third-party integration:** External APIs, auth, credentials, rate limits, offline/caching, compliance, testing against integrations.
- **Testing:** CLI interaction testing, cross-platform behavior, code quality tools.
- **Distribution:** Packaging, CI/CD, security and compliance of releases.

---

### 4. Decision Synthesis & Validation

- Summarize requirements and key decisions; ask: "Does this accurately capture your requirements?"
- Any important decisions or trade-offs not yet explored?
- Top three most important technical decisions?
- Deal-breakers or must-haves?
- Filename for the ADR? Related documents or ADRs to reference?

---

### 5. ADR Creation Proposal

Only after thorough conversation: "Based on our discussion, I'd like to create an ADR that documents these key decisions and their rationale… Would you like me to proceed?"

---
