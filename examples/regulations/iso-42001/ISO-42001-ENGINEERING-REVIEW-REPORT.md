# ISO/IEC 42001 Engineering Review Report

This report is not legal advice, certification advice, audit advice, an audit conclusion, or a final conformity decision. It records engineering evidence and gaps for qualified AI governance, legal, compliance, privacy, security, risk, platform, product, procurement, model-provider, architecture, release, and business-owner review.

## 1. Review Context

- Java system, service, repository, module, delivery pipeline, or AI-enabled capability: `CheckoutService` delivery-instructions change in the ecommerce platform delivery pipeline
- Repository or implementation path: `examples/diagrams/deployment/system-example-cicd-pr-model.md`, `examples/diagrams/deployment/expected-system-deployment.puml`, `examples/diagrams/deployment/checkout-service-feature-request.md`
- Review date: 2026-06-27
- Reviewers: AI-assisted engineering review using `.agents/skills/813-regulations-iso-42001`
- System owner: Unknown
- Product or business owner: Unknown
- AI governance owner: Unknown
- Technical owner or architect: Tech leads and reviewers are described as reviewing pull requests, architecture impacts, test coverage, and generated documentation
- Security owner: Platform/security review path is partially evidenced by CI checks for security policy compliance, SAST, dependency scanning, secret scanning, and license checks
- Privacy or data owner: Unknown
- Legal/compliance owner: Unknown
- Risk owner: Unknown
- Platform or release owner: Platform engineers maintain CI/CD templates, Artifactory, Docker registries, Azure Kubernetes Service clusters, secrets, observability, and release policies
- Procurement or model-provider owner: Unknown
- Source materials reviewed: system description, deployment diagram, feature request, generated ISO/IEC 42001 skill, bundled ISO/IEC 42001 summary, engineering examples, questionnaire, and report template

## 2. AI Management System Scope

- GenAI capability or AI-assisted delivery path: Software engineers use GenAI coding assistants for design exploration, code generation, refactoring suggestions, test creation, documentation updates, and pull request summaries.
- Lifecycle stage: Pull request and pre-merge review for a small checkout feature branch, then automated main-branch and deployment pipeline validation.
- Model provider or AI tool: GenAI tools are mentioned, but the specific tool, provider route, model, and approval status are not documented.
- Prompt workflow: Unknown.
- RAG sources or retrieval workflow: No RAG runtime is described for `CheckoutService`.
- AI-agent tools or actions: No AI-agent tool action is described for the checkout feature.
- Generated Java code, SQL, tests, migrations, configuration, API contracts, dependencies, or business rules: The delivery model allows GenAI-assisted code generation and test creation. The feature request requires a database migration, Kafka contract changes, request validation and mapping, and privacy-safe logging tests.
- Data classes in prompts, retrieval, logs, model calls, or reports: The feature request introduces `delivery_instruction_note`, which can contain personal data and must be excluded from operational logs and Kafka events. Whether this note or related source code can enter GenAI prompts is not documented.
- Environments in scope: Development, test, staging, and production Azure environments; AKS, PostgreSQL, Kafka, Key Vault, Azure Monitor, App Insights, Log Analytics, Artifactory, Docker registry, and CI/CD.
- Users, affected groups, operators, or business processes: Shoppers, support teams, delivery downstream services, software engineers, tech leads/reviewers, platform engineers, and support/operations teams.
- AI inventory or capability register evidence: Unknown.
- Open scope questions: AI governance ownership, approved GenAI provider route, prompt/data handling policy, AI inventory record, model/provider approval, generated-artifact provenance, and AI incident/corrective-action workflow.

## 3. Engineering Evidence Reviewed

- Java applications, libraries, APIs, jobs, agents, or generated code: Ecommerce platform uses Java Quarkus services. `CheckoutService` coordinates checkout saga behavior and persists order state.
- Build files, Maven dependencies, plugins, SBOM, vulnerability scans, and license/provenance evidence: The pipeline restores Maven and tool dependencies from Artifactory, runs dependency scanning and license checks, builds container images, generates SBOM and provenance metadata, and signs artifacts.
- Prompts, prompt versions, model versions, model-provider approvals, and provider-boundary records: Unknown.
- RAG retrieval sources, source approval, grounding checks, and source freshness evidence: Not applicable to the described checkout runtime; unknown for GenAI coding assistant context.
- AI-agent tool policies, tool action audit logs, preconditions, approvals, and rollback: No AI-agent action is evidenced.
- Generated outputs, generated business logic, fairness or impact tests, and domain-owner approvals: GenAI may assist with code, tests, and documentation, but generated artifact provenance and domain-owner approval records are not documented.
- CI/CD workflows, pull requests, tests, static analysis, security checks, and release approvals: Pull request validation includes formatting, compilation, unit tests, service-level integration tests, API contract compatibility, static analysis, dependency and image scanning, secret scanning, license checks, architecture tests, security policy compliance, database migration safety, smoke tests, and deployment health checks.
- Monitoring, incident, corrective-action, rollback, disablement, and continual-improvement records: Azure Monitor, App Insights, Log Analytics, alerts, dashboards, structured logs, metrics, traces, rollback using previous validated image and compatible database state are described. AI-specific incident and corrective-action paths are not documented.
- Owner handoffs and qualified review records: Tech leads/reviewers and platform engineers are described. AI governance, privacy, legal/compliance, procurement/model-provider, and risk owner handoffs are not documented.

## 4. Potential Management-System Nonconformity Mapping

This section is not a certification or audit finding. It lists potential ISO/IEC 42001 AI management system evidence gaps from the reviewed files and routes them to qualified owner review.

| Potential nonconformity or risk signal | ISO/IEC 42001 management-system reference area | Associated public source link | Evidence from reviewed system | Current status | Required owner review | Engineering action |
| -------------------------------------- | ---------------------------------------------- | ----------------------------- | ----------------------------- | -------------- | --------------------- | ------------------ |
| GenAI coding assistant use is described, but AI inventory, model/provider route, prompt workflow, and generated-artifact provenance are not documented | AI management system scope and documented information | https://www.iso.org/home/insights-news/resources/iso-42001-explained-what-it-is.html | System description says engineers use GenAI tools for design, code generation, refactoring, tests, docs, and PR summaries | Potential gap | AI governance / platform / product owner | Add an AI-assisted delivery inventory record for this change, including tool/provider, allowed data classes, generated artifacts, reviewers, and evidence links |
| GenAI-assisted Java changes may affect database migration, Kafka contract, validation, and privacy-safe logging behavior | Planning, risk treatment, and operational control | https://www.iso.org/standard/42001 | Feature request requires schema migration, Kafka schema version `3`, compatibility, validation, and privacy-safe logging tests | Potential gap | Architecture / platform / security / privacy owner | Require generated-code review gates, migration tests, Kafka contract tests, privacy-safe logging tests, and source verification for any AI-generated implementation |
| Personal data in delivery instruction notes could be exposed through prompts, logs, Kafka payloads, or generated reports if not controlled | Support, documented information, data governance, and provider boundary | https://learn.microsoft.com/en-us/compliance/regulatory/offering-iso-42001 | Feature request says note can contain personal data, must be excluded from operational logs, and must not be broadcast in Kafka events | Confirmed concern requiring controls | Privacy / security / legal / model-provider owner | Classify the note as personal data, block raw note use in prompts, redact evidence, and verify logging/Kafka tests before release |
| Generated dependency or build suggestions are possible in the delivery model, but generated dependency approval evidence is not specific to this change | Operational control and supply-chain governance | https://www.iso.org/standard/42001 | Pipeline has Artifactory, dependency scanning, license checks, SBOM, and provenance metadata; no generated dependency decision is documented for this feature | Potential gap | Platform / security / open-source program owner | If GenAI suggests dependencies or plugins, record Maven coordinate verification, SBOM update, vulnerability scan, license review, provenance review, and approval |
| AI-enabled or AI-generated business logic could affect checkout behavior if generated code is accepted without domain-owner review | Responsible AI objectives, risk treatment, monitoring, and improvement | https://www.iso.org/home/insights-news/resources/iso-42001-explained-what-it-is.html | Checkout saga affects order, delivery, and downstream service context; GenAI-assisted code generation is allowed in engineering workflow | Potential gap | Product / business / architecture / risk owner | Require domain-owner review for validation and mapping behavior, especially controlled values and event semantics |
| AI-specific monitoring, incident handling, corrective action, rollback, and disablement are not documented | Performance evaluation, improvement, nonconformity, and corrective action | https://www.iso.org/standard/42001 | General observability, alerts, rollback, and deployment checks are described; AI-specific incident path is not | Potential gap | AI governance / operations / risk owner | Extend incident and corrective-action records to cover GenAI-assisted delivery failures, unsafe generated output, prompt/data exposure, and rollback evidence |

## 5. Engineering Controls

- AI inventory and lifecycle scope: Create an AI-assisted delivery record for the checkout delivery-instructions change before merge.
- Owner accountability and review handoffs: Identify system owner, product/business owner, privacy/data owner, AI governance owner, security owner, platform/release owner, and legal/compliance owner before release readiness is claimed.
- Generated-code review gate: Require linked requirement, human engineering review, tests, static analysis, architecture review, and trusted-source verification for any generated code, SQL, Kafka contract, or mapping logic.
- Trusted-source verification for model claims: Verify generated implementation against the feature request, migration constraints, Kafka contract rules, and existing checkout saga behavior.
- Secure generated implementation review: Check authorization, validation, controlled value set for `delivery_drop_off_location`, SQL safety, error handling, and privacy-safe logging.
- Prompt minimization and redaction: Do not place free-text delivery notes, production logs, customer data, secrets, or proprietary source into external model prompts without approved policy and redaction.
- Approved model/provider route: Use only approved GenAI tools or model-provider routes for source-code assistance; record provider boundary and data-use policy.
- RAG source governance: Not applicable to the described checkout runtime; if a coding assistant uses project context retrieval, record approved source boundaries.
- AI-agent tool policy, approvals, and rollback: No AI-agent tool action is evidenced; block autonomous changes to database migrations, Kafka schemas, or production deployments without human approval.
- Generated dependency provenance: Use Artifactory, dependency scanning, license checks, SBOM, provenance metadata, and explicit approval for any generated dependency suggestion.
- SBOM, vulnerability, license, and malicious-package checks: Keep existing pipeline checks as required release evidence.
- Bias, fairness, and impact review: Confirm generated mapping and delivery-instruction handling does not create unsupported delivery outcomes, inaccessible options, or unfair downstream treatment.
- Monitoring and measurement: Monitor checkout errors, Kafka schema compatibility, delivery instruction mapping failures, privacy/logging violations, and downstream consumer failures after rollout.
- Incident response and corrective action: Add corrective-action workflow for leaked note data, bad Kafka contract rollout, migration issue, or generated-code defect.
- Rollback, disablement, and recovery: Preserve rollback using previous validated image and compatible database state; consider feature flagging delivery-instructions publication if rollout risk remains.
- Release readiness and owner approvals: Block release until privacy-safe logging tests, Kafka contract tests, migration tests, generated-code review evidence, and owner handoffs are complete.

## 6. Evidence Inventory

- AI inventory or capability register: Missing for GenAI-assisted delivery; create before release.
- Risk assessment and risk treatment record: Missing for this GenAI-assisted change; create before release.
- Prompt catalog and prompt versions: Unknown.
- Model/provider approval and version evidence: Unknown.
- RAG source registry and source approval evidence: Not applicable to runtime; unknown for coding assistant context.
- Generated artifact provenance: Unknown; record whether Java code, SQL migration, tests, Kafka schema changes, documentation, or PR summaries were generated.
- Pull request, review, test, static analysis, and security evidence: Pipeline requires PR checks, compile, unit/integration/contract/security/architecture tests, SAST, secret scanning, dependency scanning, license checks, SBOM, and provenance.
- Dependency decision, SBOM, vulnerability, license, and provenance evidence: General pipeline evidence exists; feature-specific generated dependency decisions are unknown.
- Data classification, minimization, redaction, and retention evidence: Feature request classifies `delivery_instruction_note` as personal data and requires excluding it from operational logs and Kafka payloads; retention and prompt-use controls remain open.
- Bias or impact testing evidence: Unknown.
- Monitoring dashboards or alerts: General Azure Monitor, App Insights, Log Analytics, alerts, dashboards, logs, metrics, and traces are described.
- Incident and corrective-action records: General operations evidence exists; AI-specific incident/corrective-action evidence is unknown.
- Release decision: Blocked pending owner review and engineering controls.

## 7. Residual Risks

- Residual risk: Free-text delivery notes leak into logs, Kafka events, prompts, model calls, generated reports, or support artifacts.
  - Impact: Personal-data exposure and privacy/compliance escalation.
  - Likelihood: Medium until tests and prompt/data policies are evidenced.
  - Mitigation: Privacy-safe logging tests, Kafka contract tests excluding `note`, prompt redaction/minimization policy, field-level authorization for lookup.
  - Owner: Privacy/data owner and security owner.
  - Acceptance decision: Required before release.
  - Review date: Before PR merge.

- Residual risk: AI-generated code or SQL migration introduces incorrect checkout behavior or incompatible Kafka schema.
  - Impact: Checkout saga failure, downstream consumer failure, delivery booking inconsistency, or rollback complexity.
  - Likelihood: Medium if generated artifacts are accepted without explicit provenance and review.
  - Mitigation: Human review, migration tests, Kafka contract tests for schema version `3`, backward compatibility checks, integration tests, rollback plan.
  - Owner: Technical owner, platform/release owner, and architecture reviewer.
  - Acceptance decision: Required before release.
  - Review date: Before PR merge.

- Residual risk: AI management system ownership is unclear for GenAI-assisted Java delivery.
  - Impact: Incomplete risk treatment, missing evidence, weak incident/corrective-action path, and unclear release accountability.
  - Likelihood: High because no AI governance owner or inventory record is documented in reviewed evidence.
  - Mitigation: Create AI inventory and risk-treatment record; identify AI governance, platform, privacy, security, and product owners.
  - Owner: AI governance owner and platform owner.
  - Acceptance decision: Required before release readiness is claimed.
  - Review date: Before PR merge.

## 8. Release Decision

- Decision: Blocked pending owner review and engineering controls.
- Conditions: Proceed only after generated-artifact provenance, privacy-safe logging tests, Kafka contract tests, migration tests, AI inventory/risk record, and owner approvals are available.
- Blockers: AI governance owner unknown; prompt/model-provider boundary unknown; generated-artifact provenance unknown; privacy/data owner unknown; feature-specific AI risk treatment missing.
- Required approvals: Technical owner or architect, platform/release owner, privacy/data owner, security owner, product/business owner, AI governance owner, and legal/compliance owner if privacy or regulatory interpretation remains unclear.
- Expiry or review date: Re-review before PR merge and before production deployment.
- Environments or versions approved: None approved by this review.
- Rollback path: Use previous validated container image and compatible database state; ensure migration remains backward compatible.
- Disablement or kill-switch path: Consider feature flag or publication toggle for `deliveryInstructions` in Kafka events during rollout.
- Next monitoring checkpoint: After test/staging deployment and again after production rollout if approved.

## 9. Action Plan

| Priority | Action | Owner | Due date | Evidence expected | Status |
| -------- | ------ | ----- | -------- | ----------------- | ------ |
| High | Create AI-assisted delivery inventory and risk-treatment record for this change | AI governance / platform owner | Before PR merge | AI inventory entry, generated-artifact list, model/provider route, allowed data classes, reviewers | Open |
| High | Prove delivery instruction note is not logged, broadcast to Kafka, or used in prompts/model calls without approved redaction | Privacy / security owner | Before PR merge | Privacy-safe logging tests, Kafka contract tests, prompt/data policy evidence | Open |
| High | Validate database migration and Kafka schema compatibility | Technical owner / platform owner | Before PR merge | Migration tests, schema version `3` contract tests, backward compatibility evidence | Open |
| Medium | Record generated-code and generated-test provenance | Tech lead / reviewers | Before PR merge | PR checklist entries, generated artifact hashes or notes, human review approvals | Open |
| Medium | Confirm dependency and build changes did not come from unapproved generated suggestions | Platform / security owner | Before release candidate | SBOM, dependency scan, license check, provenance metadata, dependency decision record if needed | Open |
| Medium | Add AI-specific incident and corrective-action route for GenAI-assisted delivery failures | Operations / AI governance owner | Before production rollout | Incident intake path, rollback/disablement procedure, corrective-action tracking | Open |
| Low | Add post-release monitoring for checkout delivery-instruction mapping and downstream consumer errors | Operations / product owner | Production rollout | Dashboards or alert rules for checkout errors, schema failures, privacy/logging violations | Open |

## 10. Final Notes

- Items requiring legal interpretation: Whether delivery instruction notes or GenAI prompt handling create additional legal obligations beyond the documented privacy constraints.
- Items requiring certification scope or conformity review: Whether AI-assisted development tools and generated artifact review are inside the organization's ISO/IEC 42001 management system scope.
- Items requiring audit evidence or internal audit review: AI inventory, risk treatment, provider approval, generated-artifact provenance, monitoring, and corrective-action records.
- Items requiring privacy or data protection review: Handling of `delivery_instruction_note` in persistence, logs, Kafka, service-to-service lookup, prompts, generated reports, retention, and access control.
- Items requiring security or AppSec review: Generated implementation, schema migration, Kafka event contract, field-level authorization, secret scanning, dependency scanning, and rollback safety.
- Items requiring AI governance or risk acceptance: GenAI coding assistant use, model/provider route, generated-code provenance, prompt/data classification, and AI-specific incident handling.
- Items requiring procurement or model-provider review: Approved GenAI provider route, data-use terms, retention, and source-code/IP exposure constraints.
- Items requiring architecture decision: Kafka schema versioning, free-text note lookup strategy, backward-compatible migration, and delivery-instructions rollout.
- Items requiring product or business acceptance: Controlled drop-off values, delivery service behavior, support workflow impact, and customer-facing delivery context.
- Next review trigger: PR opened or updated for the checkout delivery-instructions implementation; any GenAI-generated dependency, migration, Kafka schema, or business-rule change; staging deployment; production rollout.
