# EU AI Act Engineering Review Questionnaire

IMPORTANT: Use these questions as an evidence checklist. Complete answers from trusted local project evidence or maintainer-approved sanitized facts. Mark missing facts as `Unknown` instead of inventing answers.

Evidence rules:

1. Work through Question 1 through Question 23 in order.
2. Record the selected answer and the trusted evidence reference that supports it.
3. Use maintainer-approved sanitized facts only for gaps that local evidence does not answer.
4. Redact passwords, API keys, tokens, session IDs, private keys, connection strings, credentials, and secret values as `[REDACTED_SECRET]` before storing or repeating the answer. Record only the secret type, affected component, and control gap.
5. Mark unresolved items as `Unknown` and include them in the escalation section.
6. Do **not** start final classification or the engineering report until all 23 questions have an evidence-backed answer or an `Unknown` marker.
7. If evidence indicates any prohibited-practice signal in Question 7, stop the review, escalate immediately, and do not proceed to release recommendations until governance owners review.
8. Do **not** include raw secrets, credentials, tokens, keys, session IDs, private keys, or connection strings in notes, evidence inventories, summaries, or reports.

The first review output after reading reference materials should summarize the trusted evidence sources and list any questionnaire items that remain `Unknown`.

Use this questionnaire before recommending controls for a Java enterprise AI system, LLM application, RAG workflow, AI agent, or tool-calling automation.

**Purpose:**

- Resolve gaps about the AI capability and its intended use.
- Classify whether prohibited-practice, high-risk, transparency, general-purpose model, regulated-domain, or enterprise-system-of-record concerns may apply.
- Identify engineering controls before deployment or expanded tool access.
- Decide which owners must review the system.

This questionnaire is not legal advice. Escalate legal interpretation, EU AI Act classification disputes, and jurisdiction questions to qualified legal or compliance owners.

---

## Section 1: Map the AI Capability

Questions 1–6. Complete each item from trusted evidence or mark it `Unknown`.

**Question 1**: What type of AI capability is being reviewed?

Options:

- AI assistant that generates content, summaries, or answers
- Recommendation or decision-support system
- Classification, ranking, scoring, prediction, or profiling system
- RAG or enterprise knowledge assistant
- AI agent that can call tools or execute actions
- AI-generated code, SQL, migration, infrastructure, runbook, or deployment workflow
- General-purpose AI model integration
- No AI capability — conventional software only (specify)
- Other (specify)

**Question 2**: What outputs does the system produce?

Options:

- Text, images, audio, video, or synthetic content
- Recommendations for humans
- Classifications, rankings, scores, predictions, or risk assessments
- Decisions or decision drafts
- Code, SQL, migrations, configuration, or infrastructure definitions
- Tool calls, API calls, database changes, file changes, CI/CD changes, or deployment actions
- Conventional application data with no AI-generated output (specify)
- Other (specify)

**Question 3**: Can the system or agent execute actions without human approval?

Options:

- No, it only drafts output for human review
- Yes, but only in a sandbox or dry-run mode
- Yes, it can modify non-production systems
- Yes, it can modify production systems
- Yes, it can change data, permissions, infrastructure, CI/CD, deployments, or regulated records
- Not applicable — no AI or agent actions
- Unknown

**Question 4**: Which enterprise systems can the AI capability access?

Options (select all that apply):

- No enterprise systems
- Read-only documents or knowledge bases
- Databases or data warehouses
- APIs or internal services
- Message brokers or event streams
- Filesystems or object storage
- IAM, secrets, or permission systems
- CI/CD, source control, deployment, or infrastructure platforms
- Systems of record for customers, employees, finance, legal, health, public services, or regulated operations
- Other (specify)

**Question 5**: Who is affected by the output or action?

Options:

- Internal developers only
- Internal employees or contractors
- Customers, consumers, or end users
- Job candidates or workers
- Students or trainees
- Citizens, applicants, patients, policyholders, borrowers, or beneficiaries
- Vulnerable groups or children
- Public authorities, regulated operators, or safety teams
- Unknown

**Question 6**: What is the deployment geography and EU territorial scope?

Options (select all that apply):

- Provider is established in the EU (regardless of where deployed or used)
- AI system outputs are used by people or organizations in the EU
- AI system makes decisions or provides services affecting people in the EU
- AI system is placed on the EU market or put into service in the EU
- Deployers are located in the EU
- All development, deployment, and users are outside the EU with no EU nexus
- Third-country provider serving EU customers or users
- Unknown or unclear EU territorial connection

**Action**: If ANY option indicates EU connection (provider in EU, outputs used in EU, affects people in EU, or placed on EU market), the EU AI Act likely applies regardless of where software runs or is developed. Consult legal counsel for definitive jurisdictional assessment.

---

## Section 2: Classify Risk and Escalation Needs

Questions 7–12. Complete each item from trusted evidence or mark it `Unknown`.

**Question 7**: Does the use case match any prohibited-practice signal?

Options:

- Manipulative or deceptive techniques
- Exploitation of vulnerabilities
- Social scoring or broad trustworthiness scoring
- Criminal risk assessment based mainly on profiling or personality traits
- Untargeted scraping for facial recognition databases
- Emotion recognition in workplace or education contexts
- Biometric categorisation using sensitive or protected attributes
- Real-time remote biometric identification in publicly accessible spaces
- None identified
- Unknown

**Action**: If any prohibited-practice signal is selected, stop implementation and escalate to legal, compliance, privacy, security, and accountable business owners.

**Question 8**: Does the use case touch any Annex III high-risk domain?

Options:

- Biometrics
- Critical infrastructure
- Education or vocational training
- Employment, worker management, or access to self-employment
- Essential private services or essential public services and benefits
- Creditworthiness, credit scoring, life insurance, or health insurance risk/pricing
- Emergency dispatch or healthcare triage
- Law enforcement
- Migration, asylum, or border control
- Administration of justice
- Democratic processes or election influence
- None identified
- Unknown

**Question 9**: Is the AI system embedded in or controlling a product, safety component, or regulated sector listed in Annex I?

Options:

- Machinery, lifts, pressure equipment, radio equipment, or similar regulated product
- Medical device or in vitro diagnostic medical device
- Vehicle, rail, aviation, marine, unmanned aircraft, or transport-related system
- Civil aviation security
- Other regulated product or sector
- No
- Unknown

**Question 10**: Does the system use or provide a general-purpose AI model?

Options:

- Uses a third-party general-purpose AI model through API
- Uses an open-weight or open-source general-purpose AI model
- Fine-tunes or adapts a general-purpose AI model
- Provides a general-purpose AI model to downstream teams or users
- Uses a model that may have systemic-risk indicators such as large reach, high compute, broad autonomy, tool access, multi-modality, or major market impact
- No
- Unknown

**Question 11**: Does the system process sensitive data or rights-impacting records?

Options:

- Personal data
- Special category data
- Biometric data
- Employee or candidate data
- Student data
- Health, insurance, credit, finance, public benefit, or legal data
- Authentication, authorization, secrets, or security telemetry
- Production operational data
- No sensitive or rights-impacting data
- Unknown

**Question 12**: Which owners must review this capability before release?

Options (select all that apply):

- Legal
- Compliance or risk
- Privacy or data protection
- Security
- Product or business owner
- Model risk or AI governance
- Architecture or platform owner
- SRE, operations, or incident response
- Safety, quality, or sector-regulated owner
- No additional owner identified

---

## Section 3: Apply Engineering Controls

Questions 13–19. Complete each item from trusted evidence or mark it `Unknown`.

**Question 13**: What human-in-the-loop control is required?

Options (select all that apply):

- Human review of generated output before use
- Human approval before tool execution
- Human approval before code merge
- Human approval before database, migration, infrastructure, IAM, or CI/CD changes
- Human approval before production deployment
- Human override, pause, rollback, or kill switch
- No HITL required because the system is read-only and low impact
- No HITL required by business decision
- Unknown

**Question 14**: Were engineering artifacts generated or modified by an AI assistant, AI agent, or agent harness?

Options:

- No AI-generated engineering artifacts were used
- Yes, an AI assistant drafted artifacts for human review only
- Yes, an AI agent generated artifacts in a sandbox or dry-run workflow
- Yes, an AI agent harness or automation runner generated or modified artifacts using repository, CI/CD, database, infrastructure, or deployment tools
- Yes, generated artifacts include code, tests, SQL, migrations, configuration, infrastructure definitions, runbooks, or deployment changes
- Unknown

**Question 15**: Can AI-generated or AI-modified engineering artifacts reach production without human-in-the-loop approval?

Options:

- No, human review is required before merge
- No, explicit human approval is required before production deployment
- Yes, artifacts can be merged automatically after tests pass
- Yes, artifacts can be deployed automatically to production
- Yes, the AI agent or agent harness can directly modify production systems
- Not applicable — no AI-generated artifacts
- Unknown

**Action**: If AI-generated or AI-modified artifacts can merge, deploy, or modify production without human-in-the-loop approval, block release until accountable owners define review, approval, audit evidence, test evidence, rollback or disablement, and production authorization.

**Question 16**: What tool-access restrictions are required?

Options (select all that apply):

- Read-only access
- Sandbox-only access
- Dry-run mode before execution
- Scoped credentials or task-specific tokens
- Allow-list of tools, APIs, repositories, branches, schemas, topics, or environments
- Block access to production data or production systems
- Approval gate for write actions
- Revocation and emergency disablement path
- Not applicable — no tool access
- Unknown

**Question 17**: What audit evidence must be preserved?

Options (select all that apply):

- User request or task prompt
- System prompt or policy context
- Model provider, model name, and model version
- Retrieved source identifiers and data lineage
- Inputs, outputs, recommendations, decisions, or generated artifacts
- Tool calls, parameters, approvals, and execution results
- Human approvals, overrides, rejections, and reasons
- Git changes
- Test results, validation reports, and monitoring events
- Incidents, complaints, corrective actions, rollback, withdrawal, or recall evidence
- Unknown

**Question 18**: What data governance controls are required?

Options (select all that apply):

- Data minimization
- Access control aligned with source systems
- RAG source attribution and document versioning
- Retention and deletion propagation
- Sensitive-data redaction or exclusion
- Dataset quality review
- Bias, discrimination, or representativeness review
- Data protection impact assessment
- Fundamental rights impact assessment
- Not applicable — no AI data processing
- Unknown

**Question 19**: What runtime monitoring is required?

Options (select all that apply):

- Usage and tool-call monitoring
- Rejected action monitoring
- Human approval latency and override monitoring
- Accuracy, robustness, drift, and quality metrics
- Security and abuse monitoring
- Prompt injection or unsafe retrieval monitoring
- Incident and serious incident reporting path
- Post-market monitoring plan
- No runtime monitoring required because the capability is not deployed
- Unknown

---

## Section 4: Verify Release Readiness

Questions 20–23. Complete each item from trusted evidence or mark it `Unknown`.

**Question 20**: Which release artifacts exist?

Options (select all that apply):

- AI capability classification note
- Prohibited-practice assessment
- Annex III high-risk assessment
- Annex I product or sector assessment
- General-purpose AI model documentation review
- Technical documentation
- Risk management record
- Human oversight design
- Security review
- Privacy or data protection assessment
- Fundamental rights impact assessment
- Test evidence
- Monitoring and incident response runbook
- Rollback or disablement plan
- Approval record
- None yet

**Question 21**: What is the current release decision?

Options:

- Approved for development only
- Approved for sandbox or dry-run use
- Approved for limited pilot with human approval
- Approved for production read-only use
- Approved for production with restricted write actions and approval gates
- Blocked pending legal, compliance, privacy, security, or risk review
- Blocked pending missing technical controls
- Blocked because prohibited-practice or high-risk classification is unresolved
- Unknown

**Question 22**: What residual risks remain?

Options (select all that apply):

- Incorrect or misleading outputs
- Unsupported decision influence
- Insufficient human oversight
- Excessive tool permissions
- Missing audit evidence
- Sensitive data exposure
- Prompt injection or retrieval poisoning
- Unauthorized code, data, infrastructure, or deployment change
- Incomplete documentation
- Missing monitoring or incident response
- Regulatory classification uncertainty
- No material residual risk identified

**Question 23**: What must happen next?

Options (select all that apply):

- Ask missing questions before continuing
- Create or update the classification note
- Add human approval gates
- Restrict tools or credentials
- Add audit logging
- Add data governance controls
- Add monitoring and incident response
- Run legal/compliance/privacy/security review
- Complete release readiness evidence
- Stop or redesign the use case

---

## After the questionnaire

Only after all 23 questions have an evidence-backed answer or an `Unknown` marker:

1. Review the Java implementation to verify claims and detect gaps between answers and code.
2. Match relevant example patterns from the reference materials.
3. Generate the engineering review report using `assets/reports/801-eu-ai-act-engineering-review-report-template.md`.
