# EU AI Act Engineering Review Report

This report is not legal advice. Use it as engineering evidence for legal, compliance, privacy, security, risk, architecture, and business-owner review.

The purpose of this report is to increase awareness of potential gaps in the system and create engineering evidence for qualified review. The response produced from this template does not represent legal advice, a legal opinion, or a final regulatory determination.

## 1. Review Context

- **System or capability name:** Ecommerce deployment example with direct-to-main GenAI-assisted software delivery
- **Repository, service, product, or platform:** `java-cursor-rules` example architecture materials
- **Review date:** 2026-06-14
- **Reviewers:** EU AI Act engineering review using `801-regulations-eu-ai-act`
- **Business owner:** Not identified in reviewed materials
- **Technical owner:** Not identified in reviewed materials
- **Legal/compliance owner:** Not identified in reviewed materials
- **Privacy/security owner:** Not identified in reviewed materials
- **Source materials reviewed:**
  - `examples/diagrams/deployment/system-example-cicd-model.md`
  - `examples/diagrams/deployment/expected-system-deployment.puml`
  - `examples/diagrams/deployment/checkout-service-feature-request.md`
  - `.agents/skills/801-regulations-eu-ai-act/references/801-regulations-eu-ai-act.md`
  - `.agents/skills/801-regulations-eu-ai-act/references/801-regulations-eu-ai-act-chapters-summary.md`
  - `.agents/skills/801-regulations-eu-ai-act/assets/questions/801-eu-ai-act-risk-questionnaire.md`
  - `.agents/skills/801-regulations-eu-ai-act/assets/reports/801-eu-ai-act-engineering-review-report-template.md`

## 2. Capability Summary

- **Capability type:** AI-generated engineering artifact support in a direct-to-main software delivery lifecycle
- **AI system, decision-support workflow, RAG system, generated-artifact pipeline, general-purpose model integration, or AI agent:** Generated-artifact pipeline / AI assistant support for internal engineers; no deployed customer-facing AI system is described.
- **Intended purpose:** Support engineers with design exploration, code generation, SQL and database migration changes, Kafka message contract changes, refactoring suggestions, test creation, documentation updates, and commit summaries.
- **Primary users:** Internal software engineers using Extreme Programming practices and GenAI coding assistants.
- **Affected people or groups:** Internal developers directly; shoppers, delivery operations, downstream Kafka consumers, and operations teams may be indirectly affected if AI-assisted checkout changes are committed to `main` and released to production.
- **Deployment geography:** Unknown or unclear EU territorial connection from the reviewed source material.
- **Environments in scope:** Developer workstations, source control, main branch CI, artifact publication, automated deployment pipeline, Azure runtime, PostgreSQL, and Kafka.
- **Enterprise systems accessed:** Source repository and CI/CD pipeline are described. The checkout feature request affects the Order DB schema and Kafka event contracts. Direct GenAI access to databases, APIs, production systems, secrets, IAM, or cloud infrastructure is not described.
- **Tool actions available:** The source describes GenAI assistance, not autonomous tool execution. Code, SQL migration, Kafka schema, test, and documentation suggestions may become production artifacts after engineers review and commit them directly to `main`.
- **Human-in-the-loop status:** Human engineering review is described before committing to `main`, but the delivery model lacks a pull request pre-merge gate. The main branch CI/CD pipeline can automatically publish and deploy validated artifacts after a direct commit.

## 3. Questionnaire Findings

Acceptance-test note: the feature scenario requires questionnaire answers to be based only on information present in `examples/diagrams/deployment/system-example-cicd-model.md` and `examples/diagrams/deployment/checkout-service-feature-request.md`. Unknown items below are intentionally not filled from assumptions outside those sources.

| Question | Answer based only on reviewed source material | Gap or note |
| -------- | -------------------------------------------- | ----------- |
| 1. AI capability type | AI-generated code, SQL/database migration, Kafka message contract, generated-artifact pipeline, direct-to-main deployment workflow, and AI assistant support for engineering tasks. | No customer-facing AI capability is described. |
| 2. Outputs | Code, tests, documentation updates, design suggestions, refactoring suggestions, SQL migration for `orders`, Kafka event schema version changes, and commit summaries. | Infrastructure definitions or runbooks are not explicitly described as GenAI outputs. |
| 3. Execution without human approval | No autonomous execution is described; engineers review generated output before committing. | Direct commits to `main` reduce independent pre-merge review evidence. |
| 4. Enterprise systems accessible | Source control and CI/CD appear in the delivery workflow. | Direct GenAI access to databases, APIs, IAM, secrets, deployment systems, or production systems is not described. |
| 5. Affected people | Internal developers directly; shoppers may be indirectly affected by accepted production changes. | No direct AI decision about shoppers is described. |
| 6. EU territorial scope | Unknown or unclear EU territorial connection. | Azure runtime is described, but EU establishment, EU market placement, or EU users are not stated. |
| 7. Prohibited-practice signals | None identified. | No manipulation, exploitation, social scoring, biometric, emotion recognition, surveillance, or criminal-risk profiling use is described. |
| 8. Annex III high-risk domains | None identified for the GenAI-assisted delivery capability. | Ecommerce checkout/payment exists, but no AI is described as deciding access to essential services, credit, employment, education, law enforcement, migration, justice, or biometrics. |
| 9. Annex I product or sector | No. | No regulated product, safety component, medical device, transport, or civil aviation security use is described. |
| 10. General-purpose AI model | Unknown. | The source says "GenAI tools" but does not identify provider, model, hosting, or version. |
| 11. Sensitive data or rights-impacting records | Production operational data, customer profile/order/payment-related records, delivery instructions, free-text delivery notes, secrets, and security telemetry exist in the platform. | The feature request intentionally avoids broadcasting the free-text note in Kafka; GenAI access to those records is not described and must be verified. |
| 12. Owners required | Legal, compliance/risk, privacy/data protection, security, product/business, architecture/platform, database owner, Kafka contract owner, and SRE/operations. | Required if GenAI tooling handles repository context, customer-impacting code, deployment artifacts, or production evidence. |
| 13. HITL control | Human review of generated output before use, human approval before committing to `main`, database migration review, Kafka schema compatibility review, and rollback or disablement path. | No independent pull request reviewer or protected-main approval gate is described. |
| 14. AI-generated engineering artifacts | Yes, an AI assistant drafts artifacts for human review only. | The source names code generation, tests, documentation, design exploration, refactoring suggestions, commit summaries, SQL migration impact, and Kafka message impact. |
| 15. Production without HITL | AI-generated artifacts can enter the main branch after engineer review, and the pipeline can automatically deploy validated artifacts. | Missing pre-merge review and protected-main evidence should be treated as a release-readiness gap. |
| 16. Tool restrictions | Read-only or human-mediated repository access, protected-main controls, approval gate for write actions, block production system access, and revocation path. | Current GenAI tool permissions and branch protections are not described. |
| 17. Audit evidence | User request/task prompt, model/provider/version where available, generated artifacts, Git changes, migration review, Kafka schema review, human approvals, test results, validation reports, SBOM/provenance metadata, deployment evidence, and rollback evidence. | AI-specific audit records are not described. |
| 18. Data governance | Data minimization, access control aligned with source systems, sensitive-data redaction/exclusion, source attribution, retention/deletion handling, and free-text delivery note containment. | Need confirmation that GenAI tools do not receive secrets, customer data, production logs, or regulated records. |
| 19. Runtime monitoring | Usage monitoring for GenAI-assisted delivery, rejected action monitoring if tools exist, security/abuse monitoring, incident reporting path, and deployment monitoring. | Runtime monitoring for the ecommerce platform exists, but AI-specific monitoring is not described. |
| 20. Release artifacts | Test evidence, migration tests, Kafka contract tests, security scans, SBOM, provenance metadata, CI/CD validation, and deployment evidence exist or are requested for software delivery. | AI classification note, prohibited-practice assessment, model documentation review, AI-specific approval records, and branch-protection evidence are not described. |
| 21. Current release decision | Approved for development only / human-reviewed delivery support, based on the described use of GenAI as assistant. | Do not approve autonomous AI agent access or direct production access without additional controls. |
| 22. Residual risks | Incorrect or misleading outputs, unsupported decision influence, missing audit evidence, sensitive data exposure, prompt injection or context poisoning, unauthorized code/infrastructure change, incomplete documentation, and direct-to-main release control gaps. | Risks increase because a direct commit can trigger the validated delivery path without a pull request approval record. |
| 23. Next steps | Create or update classification note, add audit logging/evidence, restrict tools or credentials, add data governance controls, require pre-commit or pre-merge review, run legal/compliance/privacy/security review, and complete release readiness evidence. | Required before expanding GenAI assistance into autonomous agents or production-connected tools. |

Material unanswered questions:

- Which GenAI tools, model providers, model versions, and hosting arrangements are used by engineers?
- Do GenAI tools read repository contents, logs, traces, incidents, secrets, customer data, delivery instruction notes, or production operational data?
- Are AI-generated artifacts labelled or traceable through commits, database migration reviews, Kafka schema reviews, CI results, SBOM/provenance metadata, and deployments?
- Are direct commits to `main` protected by pair or mob review evidence, required signed commits, status checks, CODEOWNERS-style ownership, database-owner approvals, and Kafka contract-owner approvals?
- Are there policy gates that prevent AI tools from directly writing to protected branches, CI/CD, infrastructure, secrets, or production systems?
- Does the ecommerce platform serve EU users, use EU deployers, or place AI-enabled outputs on the EU market?

## 4. EU AI Act Risk Classification

- **Prohibited-practice signals:** None identified from the reviewed source material.
- **Annex III high-risk signals:** None identified for the described GenAI-assisted engineering workflow. Escalate if future AI features influence employment, credit, essential services, insurance, education, biometrics, law enforcement, migration, justice, democratic processes, or critical infrastructure.
- **Annex I product or sector signals:** None identified.
- **General-purpose AI model concerns:** Unknown. "GenAI tools" likely involve a general-purpose AI model, but provider, model name, model version, licence, deployment mode, data handling, and systemic-risk status are not documented.
- **Sensitive data or regulated record concerns:** The ecommerce platform handles customer profiles, payment flows, orders, delivery instructions, inventory, secrets, logs, traces, metrics, and production operational records. The checkout feature changes the `orders` schema and outbound Kafka events, so free-text delivery notes, event payload minimization, and schema compatibility require explicit review. GenAI access to those records is not described and must be controlled before any integration.
- **Transparency obligation concerns:** Developers should know when content, code, documentation, tests, or commit summaries are AI-assisted. Customer-facing transparency is not triggered by the reviewed material because no shopper-facing AI interaction is described.
- **Real-world testing or sandbox concerns:** No autonomous AI capability is described. If GenAI tooling gains tool-calling or write access, sandbox/dry-run enforcement is required before production workflows, database migrations, Kafka schema changes, or direct-to-main commits.
- **Post-market monitoring concerns:** Existing Azure Monitor, App Insights, logs, traces, metrics, alerts, rollback, and deployment evidence support platform operations. AI-specific monitoring, incident classification, and corrective-action tracking are not described.
- **Classification conclusion:** The reviewed capability is best classified as an AI-assisted generated-artifact workflow for internal software delivery of a checkout feature that changes database structure and Kafka message data. It is not a deployed customer-facing AI system and not an autonomous AI agent based on current evidence. The direct-to-main delivery model raises stronger engineering-control concerns because AI-assisted artifacts can enter the release path without a pull request pre-merge gate.
- **Required escalation:** Escalate to legal/compliance for EU territorial scope and AI Act applicability, privacy/security for data exposure and tool permissions, architecture/platform for delivery controls and branch protection, database/Kafka owners for schema and event compatibility, and SRE/operations for monitoring and incident evidence.

## 5. Potential Violation Or Non-Compliance Mapping

No confirmed EU AI Act violation is identified from the reviewed source material. The direct-to-main model creates stronger potential non-compliance and evidence gaps because AI-assisted artifacts can enter the validated release path without a pull request pre-merge record. The items below require qualified review if EU territorial scope is confirmed, if the GenAI tooling is within the Act's scope, or if the assistant workflow expands into customer-facing AI, RAG over enterprise data, tool calling, or autonomous release actions.

| Potential violation or non-compliance signal | EU AI Act reference | Evidence from reviewed system | Current status | Required owner review | Engineering action |
| -------------------------------------------- | ------------------- | ----------------------------- | -------------- | --------------------- | ------------------ |
| Prohibited-practice signal | Chapter II / Article 5 | The reviewed materials describe GenAI assistance for engineering artifacts only. No manipulation, exploitation, social scoring, biometric categorisation, emotion recognition, surveillance, or criminal-risk profiling is described. | None identified | Legal / compliance / privacy / security | Keep a prohibited-use gate before adding customer-facing AI, biometric, workplace-monitoring, or agentic capabilities. |
| High-risk classification or missing high-risk evidence | Chapter III / Articles 6-49 / Annex III | The checkout feature changes the `orders` schema and Kafka events, but no AI decides employment, education, credit, essential services, insurance, law enforcement, migration, justice, biometrics, or critical infrastructure outcomes. | None identified for current capability; reassess on scope change | Legal / compliance / risk / business owner | Maintain the AI capability classification note and rerun classification if AI output starts influencing shopper eligibility, payment, delivery access, or operational decisions. |
| Missing human-oversight, record-keeping, or protected-main evidence for AI-assisted release artifacts | Chapter III / Articles 12 and 14 where high-risk obligations apply; Chapter IX evidence expectations where deployed AI obligations apply | Engineers review generated output before committing directly to `main`, but no independent pull request gate, protected-main approval record, CODEOWNERS-style approval, signed-commit policy, or AI-origin acknowledgement is described. | Potential gap | Legal / compliance / platform / engineering leadership | Add required checks, protected-main policy, pair or mob review evidence, AI-origin acknowledgement, and database/Kafka owner approval before production release. |
| Missing AI-origin transparency or disclosure evidence | Chapter IV / Article 50 | Engineers may use AI-assisted code, tests, documentation, design suggestions, and commit summaries. The source material does not describe AI-origin labels or reviewer acknowledgement for direct commits. | Potential gap | Legal / compliance / engineering leadership | Add direct-commit or release evidence that identifies material AI-generated or AI-modified artifacts. |
| Missing general-purpose model documentation | Chapter V / Articles 51-56 / Annexes XI-XIII | The source says "GenAI tools" but does not identify provider, model, version, hosting mode, licence, data retention, training-use terms, or systemic-risk status. | Potential gap | Legal / compliance / platform / security | Inventory model/provider details and approved-use boundaries before broad or production-connected use. |
| Missing AI data-governance evidence for personal or regulated records | Chapter III / Articles 10-12 where high-risk obligations apply; Chapter V downstream model documentation where GPAI applies | The platform contains customer profile, order, payment, delivery instruction, free-text note, secret, log, trace, and security telemetry data. GenAI access to these records is not described. | Potential gap | Privacy / security / compliance | Document which repository, ticket, log, trace, secret, customer, and delivery-instruction data can reach GenAI tools; redact or exclude free-text notes and secrets. |
| Missing monitoring, incident, or corrective-action evidence | Chapter IX / Articles 72-94 | Azure Monitor, App Insights, logs, traces, metrics, alerts, rollback, and deployment evidence exist for the platform, but AI-assisted direct-to-main monitoring, incident categories, and corrective-action tracking are not described. | Potential gap | Legal / compliance / SRE / operations / security | Track AI-assisted direct commits, policy violations, rejected actions, incidents, corrective actions, and AI-tool disablement evidence. |
| Incorrect, incomplete, or unsupported regulatory information | Chapter XII / Articles 99-101 | EU territorial scope, model/provider details, GenAI data flows, AI-origin audit evidence, direct-to-main review evidence, and owner approvals are unknown or incomplete in the reviewed materials. | Potential gap if represented as compliant without qualification | Legal / compliance / risk owner | Keep unknowns explicit, avoid claiming compliance, and require qualified review before production expansion or autonomous tooling. |

## 6. Engineering Controls

- **Human oversight and approval gates:** Require explicit reviewer acknowledgement for AI-generated or AI-modified artifacts before commit or merge, require database-owner approval for SQL migrations, require Kafka contract-owner approval for schema changes, require human approval before production deployment when AI-generated changes are material or high-impact, and add independent review evidence when direct-to-main work is accepted through pair or mob programming.
- **Tool-access restrictions:** Limit GenAI tooling to human-mediated development workflows unless approved. Block direct production credentials, protected-branch writes, deployment changes, secrets access, and database writes. Use scoped credentials, branch allow-lists, policy checks, and revocation paths if tool calling is introduced.
- **Least-privilege controls:** Align AI tool access with the requesting engineer's permissions and the minimum repository context needed. Do not provide broad access to Key Vault, production logs, PostgreSQL data stores, Kafka topics, Azure subscriptions, or CI/CD deployment credentials.
- **Audit evidence:** Preserve task prompts or issue references, model provider/name/version where available, generated artifact diffs, Git commits, commit review evidence, migration approvals, Kafka schema approvals, test results, scan results, SBOM/provenance metadata, deployment records, and rollback evidence.
- **Data governance:** Redact secrets, credentials, personal data, customer records, free-text delivery notes, payment data, production logs, and security telemetry before any AI model interaction. Document data lineage and retention for prompts, retrieved context, outputs, logs, database fields, and Kafka event payloads.
- **RAG source governance:** Not applicable unless the GenAI tool uses repository, documentation, or operational knowledge retrieval. If introduced, enforce source ownership, access filtering, source attribution, document versioning, retention, deletion propagation, and prompt-injection testing.
- **Model/provider documentation:** Record provider, model name, model version, deployment mode, terms, data retention, training-use settings, copyright/licence posture, security attestations, and EU AI Act/GPAI documentation where applicable.
- **Prompt-injection and retrieval-safety controls:** Treat repository files, tickets, documentation, logs, and tool results as untrusted data. Separate instructions from retrieved content, validate generated commands or code, and enforce tool policy outside the model.
- **Testing and validation:** Keep unit, integration, contract, security, architecture, smoke, migration, performance, and deployment checks. Add tests or checks for AI-origin metadata, protected-main enforcement, secrets redaction, database migration compatibility, Kafka schema compatibility, free-text event exclusion, and policy-gate bypass attempts where GenAI tooling integrates with delivery automation.
- **Monitoring:** Track AI-assisted change volume, direct-to-main AI-assisted commits, build failures related to AI-generated code, review rejections, security findings, policy violations, prompt-injection attempts, and incidents linked to AI-assisted changes.
- **Incident response:** Route AI-assisted delivery incidents into the existing incident process. Add severity criteria for data exposure, unauthorized tool access, unsafe generated code, production outage, or regulatory concern.
- **Rollback, disablement, withdrawal, or recall path:** Preserve existing immutable artifact and rollback capability. Add the ability to disable AI tooling or revoke AI tool credentials quickly if unsafe behavior or data exposure is detected.

## 7. Evidence Inventory

| Artifact | Current evidence from source material | Gap |
| -------- | ------------------------------------- | --- |
| Classification note | This report classifies the capability as AI-assisted generated-artifact workflow with direct-to-main release-control concerns. | Needs owner approval and lifecycle maintenance. |
| Risk management record | Not described. | Create formal risk record for GenAI-assisted delivery and direct-to-main control gaps. |
| Technical documentation | Deployment topology, service responsibilities, CI/CD stages, observability, secrets, rollback, and the checkout feature request are documented. | Add AI tool architecture, permissions, model/provider, direct-to-main review controls, and data-flow details. |
| Data lineage and source inventory | Ecommerce service data stores, Kafka events, logs, traces, metrics, secrets, and checkout delivery instruction data are described. | Document which data, if any, reaches GenAI tools and ensure free-text delivery notes are excluded from Kafka events. |
| Model documentation | Not described. | Record provider, model, version, hosting, licence, and data-use terms. |
| Test evidence | CI runs unit, integration, contract, security, architecture, smoke, migration, and performance checks. The feature request asks for migration, Kafka contract, mapping, and privacy-safe logging tests. | Add AI-specific policy and provenance checks, protected-main checks, and link migration/Kafka contract evidence to release approval. |
| Security review | SAST, dependency scanning, secret scanning, license checks, image scanning, and WAF/Azure controls are described. | Add GenAI tool permission, data-exposure, and direct-to-main bypass review. |
| Privacy or data protection assessment | Not described. | Required if GenAI receives personal data, customer data, logs, or production records. |
| Fundamental rights impact assessment | Not described. | Likely not required for current internal engineering support, but reassess if AI affects rights-impacting decisions. |
| Approval record | Engineer review before direct commit and CI checks are described. | Add AI-origin acknowledgement, independent review evidence, database-owner approval, Kafka contract-owner approval, and governance approval where material. |
| Monitoring dashboard or alert evidence | Azure Monitor, App Insights, Log Analytics, metrics, traces, alerts, and dashboards are described. | Add AI-assisted delivery monitoring and direct-to-main change monitoring. |
| Operational runbook | Rollback and deployment concerns are described at a high level. | Add AI tool disablement and incident playbook. |

## 8. Residual Risks

| Residual risk | Impact | Likelihood | Mitigation | Owner | Acceptance decision |
| ------------- | ------ | ---------- | ---------- | ----- | ------------------- |
| Incorrect or insecure AI-generated code | Customer-facing defects, security vulnerabilities, or production incidents | Medium | Pair/mob review evidence, tests, static analysis, security scans, provenance tracking | Tech lead / security | Requires explicit acceptance |
| Sensitive data exposure to model provider | Privacy, contractual, or regulatory breach | Medium until data flows are proven | Redaction, data minimization, provider review, DPIA if personal data is processed | Privacy / security | Must be mitigated before broad use |
| Unsupported decision influence | Engineers may over-trust AI-generated code or commit summaries | Medium | Reviewer training, AI-origin labels, second review for high-risk changes | Engineering manager | Requires acceptance |
| Missing audit evidence | Weak ability to reconstruct why an AI-assisted direct commit was made | Medium to high | Preserve prompts, diffs, approvals, tests, model metadata, and direct-to-main review evidence | Platform / compliance | Requires remediation |
| Prompt injection or repository-context poisoning | Unsafe code, commands, or policy bypass | Low to medium | Treat retrieved content as untrusted and enforce policy outside the model | Security | Requires security review |
| Unauthorized tool or deployment action if agent features are added | Protected branch, infrastructure, or production changes | Low under current assistant-only evidence; high if tools are added | Scoped credentials, allow-lists, approvals, dry runs, revocation | Platform / SRE | Block autonomous access until controlled |
| Unsafe database or Kafka contract change | Order data corruption, consumer failure, privacy exposure, or production incident | Medium | Migration review, schema compatibility testing, contract tests, free-text exclusion, and rollback plan | Database owner / platform | Requires explicit release approval |
| Direct-to-main protected-branch bypass | AI-assisted artifacts can enter CI/CD without pull request evidence | Medium | Main branch protection, required checks, signed commits, pair/mob review record, CODEOWNERS-style approvals for risky files | Platform / tech lead | Requires explicit acceptance |
| Regulatory classification uncertainty | Incorrect EU AI Act obligations or missing review | Medium | Legal/compliance review and periodic reassessment | Legal / compliance | Requires qualified review |

## 9. Release Decision

- **Decision:** Approved for development-support use with human review only; not approved for autonomous AI agent tool execution, direct production access, or unreviewed direct-to-main release of AI-generated artifacts.
- **Conditions:** GenAI outputs remain drafts until accepted by accountable humans. Direct-to-main work must preserve review evidence and satisfy CI, security, database migration, Kafka contract, and deployment controls. AI-origin evidence, model/provider information, branch-protection evidence, and data-handling controls must be added before expanding the capability.
- **Blockers:** Unknown model/provider details, unknown GenAI data flows, missing AI-specific audit evidence, missing independent pre-merge or pre-commit review evidence, missing AI-specific incident path, and unclear EU territorial scope.
- **Required approvals:** Legal/compliance for EU AI Act scope, privacy/security for data handling and provider review, architecture/platform for tool access and direct-to-main controls, database owner for `orders` migration, Kafka contract owner for event schema changes, and SRE/operations for monitoring and disablement.
- **Expiry or review date:** Review within 90 days or earlier if GenAI tooling gains repository write access, CI/CD permissions, production credentials, RAG over enterprise data, customer-facing behavior, or tool-calling capability.
- **Environments approved:** Developer workflow and human-reviewed direct-to-main delivery support only.
- **Tool scopes approved:** Human-mediated drafting and review support. No direct production write access, protected branch writes, database changes, IAM/secrets access, or automated deployment authority is approved by this report.

## 10. Action Plan

| Priority | Action | Owner | Due date | Evidence expected | Status |
| -------- | ------ | ----- | -------- | ----------------- | ------ |
| High | Create a maintained AI capability classification note for GenAI-assisted direct-to-main delivery. | Architecture / tech lead | 2026-06-28 | Approved classification note linked from delivery documentation | Open |
| High | Inventory GenAI tools, providers, model names, model versions, hosting mode, data retention, and data-use terms. | Platform / security | 2026-06-28 | Model/provider inventory and approved-use matrix | Open |
| High | Document and control which repository, ticket, log, trace, customer, payment, secret, and production data can reach GenAI tools. | Privacy / security | 2026-07-05 | Data-flow diagram, redaction rules, and provider review | Open |
| High | Add human-review and AI-origin evidence expectations to direct-to-main commit and release guidance. | Engineering leadership | 2026-07-05 | Commit checklist, pair/mob evidence, or release gate with AI-assisted change evidence | Open |
| High | Add protected-main controls for direct commits, including required checks and explicit review evidence for AI-assisted changes. | Platform / tech lead | 2026-07-05 | Branch protection policy, required status checks, signed commit policy, or documented direct-to-main exception control | Open |
| High | Add database migration and Kafka schema approval gates for the CheckoutService delivery-instructions feature. | Database owner / platform / Kafka owner | 2026-07-05 | Migration review, Kafka schema compatibility review, rollback plan, and linked approvals | Open |
| High | Verify that free-text delivery instruction notes are not emitted to Kafka, logs, traces, prompts, or model-provider payloads. | Privacy / security / QA | 2026-07-12 | Contract tests, privacy-safe logging tests, and redaction evidence | Open |
| High | Block autonomous production access for AI tools unless a separate governance review approves scoped access. | Platform / SRE | 2026-07-05 | Branch protection, credential policy, and deployment approval evidence | Open |
| Medium | Add AI-assisted delivery monitoring and incident categories. | SRE / operations | 2026-07-19 | Dashboard or alert definitions and incident runbook updates | Open |
| Medium | Add prompt-injection, secrets-exposure, and policy-bypass checks for AI-assisted development workflows. | Security / QA | 2026-07-19 | Test cases, scanner configuration, or review checklist | Open |
| Medium | Review EU territorial scope and whether the ecommerce platform or AI-assisted artifacts are used in the EU. | Legal / compliance | 2026-07-19 | Legal/compliance assessment or documented escalation outcome | Open |
| Low | Train engineering reviewers on AI-origin review risks and evidence expectations. | Engineering manager | 2026-08-02 | Training material and attendance evidence | Open |

## 11. Final Notes

- **Items requiring legal interpretation:** EU territorial scope, EU AI Act applicability to internal GenAI-assisted delivery, GPAI/downstream documentation needs, transparency obligations for AI-generated engineering artifacts.
- **Items requiring architecture decision:** Whether GenAI remains assistant-only or becomes an AI agent with tool calling; how AI-origin metadata is stored; how direct-to-main review evidence is preserved; how approval gates integrate with CI/CD and GitOps deployment; how database migration and Kafka schema approvals are represented in release evidence.
- **Items requiring security exception:** Any request for AI tools to access production logs, secrets, customer data, protected branches, CI/CD deployment credentials, databases, IAM, or cloud infrastructure.
- **Items requiring product or business acceptance:** Residual risk from AI-assisted code reaching customer-facing ecommerce workflows, delivery-instruction data changing checkout behavior, direct-to-main review evidence expectations, review latency from stronger approval gates, and provider dependency risk.
- **Next review trigger:** Model/provider change, expanded GenAI tool permissions, RAG over enterprise data, AI-generated database/infrastructure changes, production incident linked to AI-assisted artifacts, EU deployment confirmation, or any customer-facing AI capability.
