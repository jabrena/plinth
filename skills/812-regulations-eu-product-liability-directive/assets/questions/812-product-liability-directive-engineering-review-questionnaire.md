# Product Liability Directive Engineering Review Questionnaire

IMPORTANT: Use these questions as an evidence checklist. Complete answers from trusted local project evidence or maintainer-approved sanitized facts. Mark missing facts as `Unknown` instead of inventing answers.

Evidence rules:

1. Work through Question 1 through Question 18 in order.
2. Record the selected answer and the trusted evidence reference that supports it.
3. Use maintainer-approved sanitized facts only for gaps that local evidence does not answer.
4. Redact passwords, API keys, tokens, session IDs, private keys, connection strings, credentials, exploit details, personal data, trade secrets, and sensitive incident details as `[REDACTED_SECRET]` or `[REDACTED_SENSITIVE_DETAIL]` before storing or repeating the answer.
5. Mark unresolved items as `Unknown` and include them in the escalation section.
6. Do **not** start implementation review, product-liability classification, defectiveness analysis, or the engineering report until all 18 questions have an evidence-backed answer or an `Unknown` marker.
7. If generated instructions, AI-agent tool actions, automated updates, vulnerabilities, unsafe configurations, missing warnings, safety incidents, personal injury, non-professional property damage, or non-professional data corruption are selected or unknown, record them as product-safety escalation items before release recommendations.
8. Do **not** include raw secrets, credentials, tokens, keys, session IDs, private keys, connection strings, exploit details, personal data, trade secrets, or sensitive incident details in notes, evidence inventories, summaries, or reports.

The first review output after reading reference materials should summarize the trusted evidence sources and list any questionnaire items that remain `Unknown`.

---

Use this questionnaire before recommending controls for a Java software product, AI-enabled product, related service, generated-instruction flow, AI-agent action, automated update, warning, instruction, vulnerability handling, corrective update, or incident workflow that may raise Product Liability Directive concerns.

**Purpose:**

- Resolve gaps about software product scope, related services, intended use, foreseeable misuse, safety owners, and release context.
- Identify Product Liability Directive product-safety engineering signals without making legal conclusions.
- Map software and AI product-liability concerns to engineering controls and reviewable evidence.
- Decide which owners must review the product before production use, update, or continued operation.

This questionnaire is not legal advice. Escalate defectiveness, compensable damage, causal link, economic-operator responsibility, jurisdiction, limitation periods, disclosure duties, development-risk defences, and final liability assessment to qualified legal, compliance, product, safety, security, support, risk, or accountable business owners.

---

## Section 1: Map Product And Software Scope

Questions 1-6. Complete each item from trusted evidence or mark it `Unknown`.

**Question 1**: What product or product-adjacent capability is being reviewed?

Options:

- Java software product or SaaS product
- Embedded or device-connected Java component
- Related service integrated with or inter-connected to a product
- RAG or GenAI assistant
- AI-agent workflow with tool actions
- Automated software, prompt, model, retrieval-index, or configuration update
- Generated instructions for assembly, installation, use, maintenance, or repair
- Vulnerability handling, corrective update, recall, or field intervention workflow
- Other (specify)
- Unknown

**Question 2**: What possible damage signals must owners consider?

Options (select all that apply):

- Death or personal injury
- Medically recognized psychological health damage
- Non-professional property damage
- Destruction or corruption of data not used for professional purposes
- Unsafe configuration, unsafe maintenance, or unsafe operation
- Security compromise that can affect product safety
- No damage signal identified
- Unknown

**Question 3**: Which intended or reasonably foreseeable uses are in scope?

Options (select all that apply):

- User follows generated or documented instructions
- Technician follows maintenance, repair, calibration, or installation guidance
- Product receives automated software, model, prompt, retrieval, or configuration updates
- Product inter-connects with another product, device, API, sensor, or control system
- AI system continues to learn or acquire new features after release
- AI agent creates work orders, changes configuration, triggers tools, or sends commands
- Product is used by children, consumers, field technicians, operators, or vulnerable groups
- Intended and foreseeable uses are not documented
- Unknown

**Question 4**: Which owners have reviewed the product-liability scope?

Options (select all that apply):

- Product or business owner
- Technical owner or architect
- Product safety owner
- Product security or cybersecurity owner
- Support or field operations owner
- Legal owner
- Compliance or risk owner
- No owner review yet
- Unknown

**Question 5**: Which product lifecycle event is being reviewed?

Options:

- Initial market placement or putting into service
- Feature release
- Software update or upgrade
- Prompt, model, or retrieval-source update
- Corrective update, mitigation, recall, or field intervention
- Vulnerability remediation or security advisory
- Support response or incident reconstruction
- Continued operation of an existing product
- Unknown

**Question 6**: What economic-operator or supply-chain signals exist?

Options (select all that apply):

- Manufacturer signal
- Component manufacturer signal
- Related service provider signal
- Importer, distributor, fulfilment service, or online platform signal
- Substantial modification signal
- Third-party component, model, data source, or service provider signal
- Free and open-source software supplied outside commercial activity
- Role is not documented
- Unknown

---

## Section 2: Classify Software, AI, Update, And Warning Signals

Questions 7-12. Complete each item from trusted evidence or mark it `Unknown`.

**Question 7**: Does the product generate instructions, recommendations, decisions, or actions that users may rely on?

Options (select all that apply):

- Generated repair, maintenance, installation, assembly, use, or safety instructions
- RAG answer based on product manuals, service bulletins, tickets, or knowledge bases
- AI-generated work order, checklist, support response, or field instruction
- AI-agent tool action or command
- Automated decision support affecting safe operation
- No generated instruction or action
- Unknown

**Question 8**: Which provenance evidence exists for generated outputs or actions?

Options (select all that apply):

- Prompt version
- Model version
- Retrieval source IDs and source approval state
- Tool action policy version
- Human approval record
- Generated output audit log
- Test or evaluation evidence
- Provenance is incomplete or not retained
- Unknown

**Question 9**: Which warnings and instructions are available?

Options (select all that apply):

- Assembly instructions
- Installation instructions
- Use instructions
- Maintenance or repair instructions
- Generated instruction safety warnings
- Update, rollback, and opt-out instructions
- Known limitation or contraindication warnings
- Support escalation instructions
- Warnings or instructions are missing or stale
- Unknown

**Question 10**: How are automated updates controlled?

Options (select all that apply):

- Safety impact assessment before update
- Signed artifact or versioned update package
- Prompt, model, retrieval, or configuration change approval
- Rollback path
- User or support notification
- Corrective update procedure
- Update installation or deferral evidence
- Automated update path is not documented
- Unknown

**Question 11**: How are vulnerabilities and cybersecurity-relevant safety issues handled?

Options (select all that apply):

- Vulnerability intake and triage
- Coordinated disclosure or advisory workflow
- Safety impact assessment for vulnerabilities
- Security update delivery
- User mitigation guidance
- Incident escalation to product safety
- No vulnerability or cybersecurity-safety path documented
- Unknown

**Question 12**: Which validation evidence exists?

Options (select all that apply):

- Hazard analysis
- Hazardous-scenario tests
- Generated-instruction evaluation tests
- AI-agent tool-action tests
- Inter-connected product or integration tests
- Update and rollback tests
- Incident reconstruction drill
- User documentation review
- Validation evidence is missing or incomplete
- Unknown

---

## Section 3: Evidence, Escalation, And Release Decision

Questions 13-18. Complete each item from trusted evidence or mark it `Unknown`.

**Question 13**: Which incident or field evidence is retained?

Options (select all that apply):

- Support tickets or field reports
- Safety incident records
- Product audit logs
- Generated output and source evidence
- AI-agent action evidence
- Update, rollback, and corrective action evidence
- Recall, intervention, or user notification evidence
- Evidence retention is missing or unclear
- Unknown

**Question 14**: Which evidence must be protected from unnecessary disclosure?

Options (select all that apply):

- Personal data
- Trade secrets or confidential business information
- Secrets, credentials, keys, or tokens
- Exploit details or sensitive vulnerability information
- Sensitive incident or safety reports
- Proprietary model, prompt, source, or tool-action details
- No sensitive evidence identified
- Unknown

**Question 15**: Which release or continued-operation risks are unresolved?

Options (select all that apply):

- Product scope or owner gap
- Intended use or foreseeable misuse gap
- Generated instruction or RAG source governance gap
- AI-agent tool-action control gap
- Warning or instruction gap
- Automated update, corrective update, or rollback gap
- Vulnerability handling gap
- Validation or hazardous-scenario testing gap
- Incident evidence or support handoff gap
- No unresolved risk identified
- Unknown

**Question 16**: Which qualified owners must review before release or continued operation?

Options (select all that apply):

- Legal
- Compliance or risk
- Product or business
- Product safety
- Product security or cybersecurity
- Support or field operations
- Architecture or technical authority
- Executive accountability owner
- No owner review required by current decision
- Unknown

**Question 17**: What release decision is currently proposed?

Options:

- Block release until product-safety gaps are resolved
- Allow only internal testing
- Allow limited pilot with owner approval and monitoring
- Allow release with documented conditions
- Allow corrective update or emergency mitigation
- Continue operation with monitored residual risk
- No release decision yet
- Unknown

**Question 18**: What should trigger the next Product Liability Directive review?

Options (select all that apply):

- New generated instruction type
- New AI-agent tool action
- New prompt, model, retrieval source, or training/evaluation change
- Automated update mechanism change
- Vulnerability, incident, near miss, support trend, or safety complaint
- Warning, instruction, or user population change
- Substantial modification or new inter-connected product
- National transposition, case law, or qualified owner request
- Unknown
