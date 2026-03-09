---
name: 030-architecture-non-functional-requirements
description: Facilitates conversational discovery to create Architectural Decision Records (ADRs) for non-functional requirements using the ISO/IEC 25010:2023 quality model. Use when the user wants to document quality attributes, NFR decisions, security/performance/scalability architecture, or design systems with measurable quality criteria.
metadata:
  author: Juan Antonio Breña Moral
  version: 0.13.0-SNAPSHOT
---

# Create ADRs for Non-Functional Requirements

Guides stakeholders through a structured conversation to uncover and document architectural decisions for quality attributes using the ISO/IEC 25010:2023 quality model. The ADR is the documentation of that conversation, not the conversation itself. Act as an architecture consultant: challenge-first, consultative, adaptive.

---

## Phase 0: Get Current Date

Before starting, run `date` in the terminal to ensure accurate timestamps in the ADR document. Use this for all `[Current Date]` placeholders.

---

## Phase 1: Conversational Information Gathering

Ask one or two questions at a time. Build on previous answers. Stay consultative, not interrogative. Skip quality characteristics irrelevant to the use case; dive deeper where there's uncertainty or risk.

### Opening (Challenge-First)

"What's the main non-functional challenge you're trying to solve? Based on ISO/IEC 25010:2023, are you dealing with:

- **Functional Suitability:** Completeness, correctness, appropriateness?
- **Performance Efficiency:** Response times, throughput, resource utilization, capacity?
- **Compatibility:** Co-existence, interoperability?
- **Interaction Capability:** Recognizability, learnability, operability, user protection, engagement, inclusivity, assistance, self-descriptiveness?
- **Reliability:** Faultlessness, availability, fault tolerance, recoverability?
- **Security:** Confidentiality, integrity, non-repudiation, accountability, authenticity, resistance?
- **Maintainability:** Modularity, reusability, analysability, modifiability, testability?
- **Flexibility:** Adaptability, installability, replaceability, scalability?
- **Safety:** Operational constraint, risk identification, fail safe, hazard warning, safe integration?

Or something spanning multiple characteristics?"

### 1. Understanding the Challenge (3–4 questions)

- What's driving this decision? Proactive improvement or specific issues?
- Key constraints: timeline, budget, team expertise, tech stack, compliance?
- System context: what type of application, current architecture, who are the users?

### 2. ISO 25010:2023 Quality-Specific Deep Dive (4–6 questions)

**Tailor questions to the primary NFR category identified.**

| Characteristic | Key sub-characteristics to explore |
|----------------|-----------------------------------|
| **Functional Suitability** | Completeness, correctness, appropriateness; targets; impact of gaps |
| **Performance Efficiency** | Time behaviour, resource utilization, capacity; targets; cost of slow performance |
| **Compatibility** | Co-existence, interoperability; data formats, protocols, standards |
| **Interaction Capability** | Recognizability, learnability, operability, error protection, engagement, inclusivity, assistance, self-descriptiveness |
| **Reliability** | Faultlessness, availability, fault tolerance, recoverability; uptime targets; business impact |
| **Security** | Confidentiality, integrity, non-repudiation, accountability, authenticity, resistance; data types; GDPR, HIPAA, SOC2, PCI |
| **Maintainability** | Modularity, reusability, analysability, modifiability, testability; impact on velocity |
| **Flexibility** | Adaptability, installability, replaceability, scalability; expected changes; growth patterns |
| **Safety** | Operational constraint, risk identification, fail safe, hazard warning, safe integration; harm potential; safety standards |

### 3. Solution Exploration (3–4 questions)

- What solutions or patterns have you considered?
- Trade-off preferences: cost, simplicity, performance, security, scalability, time to implement?
- Team expertise, tech preferences, realistic complexity?
- Success definition: metrics to track, what would make you confident?

### 4. Decision Synthesis & Validation

- Summarize key NFR decisions and rationale; ask: "Does this accurately capture your quality needs?"
- Any important characteristics or trade-offs we haven't explored?
- Top 3 most critical NFRs? Deal-breakers?
- Filename for the ADR? Related documents or ADRs?

### 5. ADR Creation Proposal

Only after thorough conversation: "Based on our discussion about your non-functional requirements, I'd like to create an ADR that documents these quality decisions and their rationale... Should I proceed?"

---

## Phase 2: ADR Document Generation

Provide a conversational summary first. Confirm accuracy, then generate the full ADR using the current date from Phase 0.

### ADR Structure

```markdown
# ADR-XXX: [Title] - Non-Functional Requirements

**Status:** Proposed | Accepted | Deprecated
**Date:** [Current Date]
**ISO 25010:2023 Focus:** [Primary quality characteristic(s)]

## Context
[Business context, quality challenge, system description]

## Non-Functional Requirements
[Quality characteristics with sub-characteristics and targets; use ISO 25010:2023 terminology]

### Primary Quality Characteristic
[Detailed NFRs for the main focus area]

### Secondary Quality Characteristics
[Other relevant NFRs]

## Technical Decisions
[Architectural approach with rationale for each quality attribute]

## Alternatives Considered
[Rejected options and why]

## Quality Metrics & Success Criteria
[Measurable criteria, thresholds, monitoring approach]

## Consequences
[Impact, trade-offs, follow-up work]

## References
[Links, related ADRs, ISO/IEC 25010:2023]
```

---

## Phase 3: Next Steps and Recommendations

After generating the ADR:

**Next Steps:**
1. Review and validate with stakeholders and technical teams
2. Create detailed quality metrics and measurement framework
3. Set up monitoring and observability for identified quality characteristics
4. Begin implementation with proof-of-concept for most critical NFRs
5. Establish quality gates and testing frameworks early

**ADR Management:**
- Keep the ADR as a living document
- Reference during code reviews and architectural discussions
- Plan regular reviews as the system evolves
- Link to user stories, requirements, implementation tasks
- Track quality metrics to validate decisions

**Optional follow-up offers:** Implementation roadmap, quality metrics framework, technology evaluation, QA strategy, ISO 25010:2023 compliance assessment.

---

## Key Principles

| Principle | Practice |
|-----------|----------|
| **Discovery over assumption** | Never assume NFRs; ask and validate. Understand the "why". Explore edge cases. |
| **Collaborative quality decisions** | Help stakeholders think through trade-offs. Document reasoning, not just decisions. |
| **Iterative understanding** | Build incrementally. Circle back when new information emerges. |
| **Context-aware** | Tailor to system type, complexity, team maturity, constraints. |

**Create the ADR when:** Clear context, key quality decisions identified, alternatives explored, understanding validated.

**Continue the conversation when:** NFRs unclear, decisions arbitrary, alternatives not explored, stakeholders uncertain, critical context missing.
