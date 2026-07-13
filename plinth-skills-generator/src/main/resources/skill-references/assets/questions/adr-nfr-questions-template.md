**Phase 1: Conversational Information Gathering**

Pose one or two discovery questions at a time. Build on current-session answers. Stay consultative, not interrogative. Skip quality characteristics irrelevant to the use case; dive deeper where there's uncertainty or risk.

---

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

---

### 1. Understanding the Challenge (3–4 questions)

- What's driving this decision? Proactive improvement or specific issues?
- Key constraints: timeline, budget, team expertise, tech stack, compliance?
- System context: what type of application, current architecture, who are the users?

---

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

---

### 3. Solution Exploration (3–4 questions)

- What solutions or patterns have you considered?
- Trade-off preferences: cost, simplicity, performance, security, scalability, time to implement?
- Team expertise, tech preferences, realistic complexity?
- Success definition: metrics to track, what would make you confident?

---

### 4. Decision Synthesis & Validation

- Summarize key NFR decisions and rationale; ask: "Does this accurately capture your quality needs?"
- Any important characteristics or trade-offs we haven't explored?
- Top 3 most critical NFRs? Deal-breakers?
- Filename for the ADR? Related documents or ADRs?

---

### 5. ADR Creation Proposal

Only after thorough conversation: "Based on our discussion about your non-functional requirements, I'd like to create an ADR that documents these quality decisions and their rationale... Should I proceed?"

---
