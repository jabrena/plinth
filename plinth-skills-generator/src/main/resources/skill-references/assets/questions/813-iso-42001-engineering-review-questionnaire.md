# ISO/IEC 42001 Engineering Review Questionnaire

IMPORTANT: Use these questions as an evidence checklist. Complete answers from trusted local project evidence or maintainer-approved sanitized facts. Mark missing facts as `Unknown` instead of inventing answers.

Evidence rules:

1. Work through Question 1 through Question 16 in order.
2. Record the selected answer and the trusted evidence reference that supports it.
3. Use maintainer-approved sanitized facts only for gaps that local evidence does not answer.
4. Redact passwords, API keys, tokens, session IDs, private keys, connection strings, credentials, personal data, regulated data, proprietary source code, trade secrets, and sensitive model outputs as `[REDACTED_SECRET]` or `[REDACTED_SENSITIVE_DETAIL]` before storing or repeating the answer.
5. Mark unresolved items as `Unknown` and include them in the escalation section.
6. Do **not** start implementation review, AI management system scope assessment, risk treatment recommendation, or the engineering report until all 16 questions have an evidence-backed answer or an `Unknown` marker.
7. If hallucinated code, insecure generated implementation, generated dependency risk, IP leakage, confidentiality breach, regulatory non-compliance risk, biased generated business logic, or missing owner accountability is selected or unknown, record it as an escalation item before release recommendations.
8. Do **not** include raw secrets, credentials, tokens, keys, session IDs, private keys, connection strings, personal data, proprietary source code, trade secrets, or sensitive model outputs in notes, evidence inventories, summaries, or reports.

The first review output after reading reference materials should summarize the trusted evidence sources and list any questionnaire items that remain `Unknown`.

---

Use this questionnaire before recommending controls for a Java system, AI-assisted delivery workflow, LLM integration, RAG workflow, AI agent, generated code path, generated dependency, prompt workflow, model-provider boundary, or AI-enabled business logic that may raise ISO/IEC 42001 AI management system concerns.

This questionnaire is not legal advice, certification advice, audit advice, an audit conclusion, or a final conformity decision. Escalate certification scope, conformity, audit evidence, legal obligations, regulatory interpretation, compliance approval, and final risk acceptance to qualified owners.

---

## Section 1: Map AI Management System Scope

Questions 1-5. Complete each item from trusted evidence or mark it `Unknown`.

**Question 1**: What AI or GenAI capability is being reviewed?

Options:

- AI-assisted Java code generation
- LLM integration in a Java application
- RAG workflow or enterprise knowledge assistant
- AI agent with tool calling
- Generated Java tests, SQL, migrations, configuration, or API contracts
- Generated dependency or build/plugin suggestion
- AI-enabled business logic or decision support
- Prompt workflow or model-provider integration
- Other (specify)
- Unknown

**Question 2**: Which lifecycle stage is in scope?

Options:

- Exploration or prototype
- Pull request or pre-merge review
- Pre-release validation
- Production operation
- Model, prompt, retrieval-source, or dependency update
- Incident response or corrective action
- Decommissioning or disablement
- Unknown

**Question 3**: Which owners are identified?

Options (select all that apply):

- System or service owner
- Product or business owner
- AI governance owner
- Risk owner
- Security owner
- Privacy or data owner
- Compliance or legal owner
- Platform or release owner
- Procurement or model-provider owner
- No owner review yet
- Unknown

**Question 4**: Which AI management system evidence exists?

Options (select all that apply):

- AI inventory or capability register
- Risk assessment or risk treatment record
- AI policy or development standard
- Prompt catalog or prompt version record
- Model/provider approval record
- RAG source registry or source approval record
- Generated artifact provenance
- Monitoring, incident, or corrective-action record
- Evidence is missing or incomplete
- Unknown

**Question 5**: Which data classes or protected information may enter prompts, retrieval, logs, model calls, or generated reports?

Options (select all that apply):

- Public documentation only
- Proprietary source code
- Trade secrets or confidential business logic
- Personal data
- Regulated or sector-sensitive data
- Secrets, credentials, tokens, keys, or connection strings
- Production logs, support tickets, or customer records
- Synthetic or anonymized test data
- Data classes are not documented
- Unknown

## Section 2: Review GenAI Development And Supply-Chain Risk

Questions 6-11. Complete each item from trusted evidence or mark it `Unknown`.

**Question 6**: How is hallucinated or incorrect generated code controlled?

Options (select all that apply):

- Linked requirement or issue reference
- Trusted-source verification for model claims
- Human engineering review
- Unit tests
- Integration or contract tests
- Static analysis or style checks
- Architecture or design review
- No generated-code control documented
- Unknown

**Question 7**: How is insecure generated implementation controlled?

Options (select all that apply):

- Authorization review
- Input validation review
- Injection-resistance review
- Secrets and credential handling review
- Logging and privacy review
- Secure coding or AppSec review
- Security tests or scanning
- No secure implementation review documented
- Unknown

**Question 8**: How are generated dependency or build suggestions governed?

Options (select all that apply):

- Approved repository or registry policy
- Maven coordinate and version verification
- SBOM update
- Vulnerability scanning
- License review
- Provenance or maintainer review
- Typosquat or malicious-package check
- Transitive dependency review
- No dependency governance documented
- Unknown

**Question 9**: How are prompt, IP, and confidentiality boundaries controlled?

Options (select all that apply):

- Approved model/provider route
- Data classification before model use
- Prompt minimization
- Secret and personal-data redaction
- Raw prompt storage disabled or controlled
- Provider contract or data-processing review
- Access control and retention policy
- No prompt confidentiality boundary documented
- Unknown

**Question 10**: How is biased or unfair generated business logic controlled?

Options (select all that apply):

- Protected attribute or proxy review
- Domain-owner approval
- Fairness, cohort, or impact tests
- Explanation and override path
- User complaint or appeal signal
- Runtime monitoring
- Legal, compliance, privacy, or risk escalation
- No bias or fairness review documented
- Unknown

**Question 11**: Which regulatory or cross-skill concerns are signaled?

Options (select all that apply):

- EU AI Act classification or transparency signal
- GDPR or personal-data processing signal
- Cyber Resilience Act or product cybersecurity signal
- Product Liability Directive or generated-instruction signal
- Sector regulation or contractual obligation signal
- No cross-regulation signal identified
- Unknown

## Section 3: Monitoring, Corrective Action, And Release Decision

Questions 12-16. Complete each item from trusted evidence or mark it `Unknown`.

**Question 12**: Which monitoring or measurement evidence exists?

Options (select all that apply):

- Hallucination or incorrect-output monitoring
- RAG grounding or source freshness monitoring
- Prompt injection or policy-bypass monitoring
- Data exposure or access-denial monitoring
- Dependency vulnerability alerting
- Bias, complaint, or impact monitoring
- Model/provider change monitoring
- No monitoring evidence documented
- Unknown

**Question 13**: Which incident or corrective-action path exists?

Options (select all that apply):

- AI incident intake and triage
- Prompt remediation
- RAG source removal or correction
- Model/provider rollback or disablement
- Generated dependency removal
- Feature flag or kill switch
- Owner notification and action tracking
- Regression test or monitoring update after incident
- No corrective-action path documented
- Unknown

**Question 14**: Which release or continued-operation risks remain unresolved?

Options (select all that apply):

- AI scope or inventory gap
- Owner accountability gap
- Data classification or prompt confidentiality gap
- Generated-code review gap
- Secure implementation gap
- Dependency governance gap
- Bias or unfair business logic gap
- Monitoring or incident response gap
- No unresolved risk identified
- Unknown

**Question 15**: Which qualified owners must review before release or continued operation?

Options (select all that apply):

- AI governance owner
- Legal or compliance owner
- Privacy or data protection owner
- Security or AppSec owner
- Platform or release owner
- Product or business owner
- Risk owner
- Procurement or model-provider owner
- No qualified owner review required by current evidence
- Unknown

**Question 16**: What release readiness decision is supported by the evidence?

Options:

- Ready with evidence
- Ready with conditions
- Blocked pending owner review
- Blocked pending engineering controls
- Blocked pending legal, compliance, certification, audit, privacy, security, or risk decision
- Unknown
