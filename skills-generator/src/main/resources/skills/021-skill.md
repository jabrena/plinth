---
name: 021-architecture-functional-requirements-rest
description: Facilitates conversational discovery to create Architectural Decision Records (ADRs) for REST API development. Use when the user wants to document REST API architecture, capture functional requirements for APIs, create ADRs for REST/HTTP services, or design APIs with documented decisions.
metadata:
  author: Juan Antonio Breña Moral
  version: 0.13.0-SNAPSHOT
---

# Create ADRs for REST API Development

Guides stakeholders through a structured conversation to uncover and document technical decisions and functional requirements for REST API implementations. The ADR is the documentation of that conversation, not the conversation itself.

---

## Phase 0: Get Current Date

Before starting, run `date` in the terminal to ensure accurate timestamps in the ADR document. Use this for all `[Current Date]` placeholders.

---

## Phase 1: Conversational Information Gathering

Ask one or two questions at a time. Build on previous answers. Acknowledge and validate responses before moving on. Adjust questions to API complexity; skip irrelevant areas and dive deeper where needed.

### 1. Initial Context Discovery

**Opening:**
- What API are you building and what business problem does it solve?
- What's driving the need? Replacing existing system, new functionality, or integrations?
- Who are the primary consumers: internal services, mobile apps, third-party developers, or end users?

**Follow-up:**
- What existing systems or data sources will this API integrate with?
- Constraints: team expertise, organizational standards, compliance (GDPR, HIPAA, PCI)?
- Expected timeline and success criteria?
- Anticipated load: users, requests/sec, data volume?

### 2. Functional Requirements

**Core functionality:**
- Main use cases and essential operations?
- Resources and entities to expose; how do they relate?
- Input validation and data transformation needs?
- Response formats and data structures consumers need?

**API design & UX:**
- How technical are consumers? Need extensive docs and SDKs?
- Simple CRUD API or complex business operations?
- Critical error scenarios to handle gracefully?
- How will consumers discover endpoints?
- Need real-time capabilities (webhooks, server-sent events)?

### 3. Technical Decision Discovery

**Language & framework:** Team expertise, performance (response time, throughput, memory), integration with existing systems, familiar REST frameworks.

**API design & architecture:** Monolithic vs microservices; resource-oriented REST vs GraphQL vs RPC; API versioning; bulk ops, filtering, sorting, pagination; synchronous vs async vs webhooks for long-running ops.

**Authentication & security:** JWT, OAuth2, API keys, mutual TLS; authorization and RBAC; rate limiting, throttling, quotas; compliance; securing sensitive data.

**Data management:** SQL vs NoSQL vs hybrid; caching strategy; validation, serialization, transformation; consistency and transactions; schema evolution.

**Third-party integration:** External services, auth methods, failure handling (degrade vs fail fast), rate limits, circuit breakers, multi-provider support, caching, contract testing, webhook handling.

**Testing:** Current approach, unit/integration/contract testing, OpenAPI/Swagger, load testing, test data management.

**Infrastructure:** Containerization, cloud platform, config and secrets, scaling strategy, blue-green or canary deployments.

**Monitoring:** Health, performance, usage; logging; distributed tracing; alerting; business metrics and adoption.

### 4. Decision Synthesis & Validation

- Summarize key decisions and ask: "Does this accurately capture your requirements?"
- Any important decisions or trade-offs we haven't explored?
- Top 3 most critical technical decisions?
- Deal-breakers or must-haves? Aspects needing the most detail?
- Filename for the ADR? Related documents or ADRs to reference?

### 5. ADR Creation Proposal

Only after thorough conversation: "Based on our discussion, I'd like to create an ADR that documents these key decisions and their rationale... Should I proceed?"

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
[Business context, problem statement, consumer needs]

## Functional Requirements
[Use cases, resources, operations, response formats, error handling]

## Technical Decisions
[With rationale for each]

### Language & Framework
[Choice and why]

### API Design & Architecture
[Structure, versioning, patterns]

### Authentication & Security
[Mechanism, authorization, rate limiting]

### Data & Persistence
[Storage, caching, validation]

### Integration & Infrastructure
[External services, deployment, scaling]

### Testing & Monitoring
[Approach, observability]

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
1. Review and validate with stakeholders and technical teams
2. Create detailed technical specifications and API documentation
3. Set up dev environment and initial project structure
4. Begin implementation with MVP or proof-of-concept
5. Establish monitoring and testing frameworks early

**ADR Management:**
- Keep the ADR as a living document
- Reference during code reviews and architectural discussions
- Plan regular reviews as the system evolves
- Link to user stories, requirements, implementation tasks

---

## Key Principles

| Principle | Practice |
|-----------|----------|
| **Discovery over assumption** | Never assume; ask and validate. Understand the "why". Explore edge cases. |
| **Collaborative decisions** | Help stakeholders think through trade-offs. Document reasoning, not just decisions. |
| **Iterative understanding** | Build incrementally. Circle back when new information emerges. |
| **Context-aware** | Tailor to API complexity, business domain, team maturity, constraints. |

**Create the ADR when:** Clear context, key decisions identified, alternatives explored, understanding validated.

**Continue the conversation when:** Requirements unclear, decisions arbitrary, alternatives not explored, stakeholders uncertain, critical context missing.
