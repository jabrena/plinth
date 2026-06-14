# EU Cyber Resilience Act Engineering Review Report

Use this report as engineering evidence for legal, compliance, product, product-security, security, support, market-access, platform, risk, architecture, executive accountability, and business-owner review.

This report is not legal advice. It identifies engineering controls and escalation points from the reviewed direct-to-main delivery evidence. Product classification, economic-operator role, conformity assessment, CE marking implications, Article 14 reporting obligations, support-period interpretation, cybersecurity risk acceptance, and regulatory interpretation require qualified owner review.

## 1. Review Context

- Product, service, component, library, agent, plugin, or platform module: CheckoutService delivery instructions change
- Repository or implementation path: Fictional Java Quarkus ecommerce platform with direct commits to `main`
- Review date: 2026-06-14
- Reviewers: AI-assisted Cyber Resilience Act engineering review
- Business owner: Not identified in reviewed evidence
- Product owner: Not identified in reviewed evidence
- Technical owner: Software engineers, tech leads, and platform engineers are described, but no named CheckoutService owner is identified
- Product security owner: Not identified in reviewed evidence
- Support owner: Support and operations teams are described, but no named support owner is identified
- Legal/compliance owner: Not identified in reviewed evidence
- Market-access or conformity owner: Not identified in reviewed evidence
- Source materials reviewed:
  - `examples/diagrams/deployment/system-example-cicd-model.md`
  - `examples/diagrams/deployment/expected-system-deployment.puml`
  - `examples/diagrams/deployment/checkout-service-feature-request.md`
  - `.agents/skills/805-regulations-eu-cyber-resilience-act/references/805-regulations-eu-cyber-resilience-act-chapters-summary.md`
  - `.agents/skills/805-regulations-eu-cyber-resilience-act/references/805-regulations-eu-cyber-resilience-act-engineering-examples.md`
  - `.agents/skills/805-regulations-eu-cyber-resilience-act/assets/reports/805-eu-cyber-resilience-act-engineering-review-report-template.md`

## 2. Product Security Scope

- Product or component description: CheckoutService coordinates checkout saga steps for price validation, inventory reservation, payment authorization, order creation, delivery slot booking, PostgreSQL order state, saga state, and Kafka events.
- Possible product-with-digital-elements signal: The reviewed evidence describes a Java service and related platform components with network connectivity, APIs, databases, Kafka messages, identity, payment, delivery, and cloud dependencies. It does not establish whether the service is itself a product with digital elements or part of one.
- Possible remote data processing signal: CheckoutService, payment adapter, inventory, delivery slot service, notification worker, analytics pipeline, and cloud services process data remotely as part of the platform. Qualified owners must decide whether any remote processing is under manufacturer responsibility for a product with digital elements.
- Possible important or critical product signal: Not confirmed. Identity, API gateway, Kafka, container runtime, WAF, and platform services may be relevant dependencies, but the reviewed feature does not classify CheckoutService under Annex III or Annex IV.
- Intended purpose: Add delivery instructions to checkout and order events while preserving checkout, delivery, notification, and analytics behavior.
- Reasonably foreseeable use or misuse: Users may enter free-text delivery notes containing personal data, access codes, or unsafe content; downstream consumers may over-propagate or log the new fields; direct-to-main changes may reach product release without independent review.
- Economic-operator role signals: No manufacturer, importer, distributor, open-source steward, authorised representative, or market-access role is recorded.
- Deployment geography: Azure production environment is described; EU market availability is not specified.
- Environments in scope: Development, test, staging, and production.
- Users, integrators, or affected organizations: Shoppers, support and operations teams, platform engineers, downstream inventory, delivery, notification, analytics, and payment workflows.
- Support-period signal: No support period, security update availability commitment, or end-of-support signaling appears in the reviewed evidence.
- Security update delivery path: Main branch CI builds signed images, SBOM and provenance metadata, GitOps/CD deployment, automated promotion, health checks, and smoke tests. Security-update advisory, user notification, update availability, rollback evidence, and pre-merge review controls are not complete.
- Open applicability questions:
  - Is CheckoutService or the ecommerce platform a product with digital elements made available on the EU market?
  - Which entity, if any, is the manufacturer or other economic operator for this product or component?
  - Does direct-to-main delivery satisfy the organization's product-security release and evidence expectations for a potentially CRA-relevant change?
  - Does the change create a substantial modification or affect a product with digital elements already placed on the market?
  - Are Annex III important-product or Annex IV critical-product categories implicated by identity, gateway, container, security, or connected-product dependencies?
  - Which qualified owners must approve conformity assessment, CE marking implications, Article 14 reporting handoffs, and support-period statements?

## 3. Engineering Evidence Reviewed

- Java applications, libraries, agents, plugins, APIs, jobs, or installers: Web storefront BFF, API gateway, identity, catalog, search, cart, pricing, CheckoutService, payment adapter, inventory, delivery slot, notification worker, analytics pipeline, checkout saga logic, and deployment workflows.
- Data stores, queues, topics, caches, files, or remote processing: User Profile DB, Product DB, Search Index, Cart DB, Pricing DB, Order DB, Saga Support PostgreSQL, Inventory DB, Delivery DB, Kafka topics `order.created`, `payment.authorized`, `stock.reserved`, `slot.booked`, `delivery.slot.requested`, `order.confirmed`, and compensation events.
- Authentication, authorization, IAM, secrets, keys, and privileged operations: Azure Key Vault, managed identities, API gateway authentication enforcement, identity provider integration, payment tokens, idempotency keys, database credentials, Kafka credentials, and deployment credentials.
- CI/CD workflows, build provenance, artifact signing, and deployment paths: Direct commit to `main`, main branch CI, Artifactory, Docker registry, signed images, SBOM and provenance metadata, GitOps/CD pipeline, automated environment promotion, migration safety checks, health checks, and smoke tests.
- Third-party components, open-source dependencies, containers, and generated clients: Quarkus services, common Java libraries, container images, build pipeline tools, Azure services, external identity provider, external payment gateway, and email/SMS provider.
- SBOM, dependency scan, image scan, and vulnerability triage evidence: Pipeline generates SBOM and provenance and runs dependency, image, and secret scanning; concrete SBOM references, vulnerability triage records, owner assignments, and exception decisions are not present.
- Threat model, secure-by-design, secure defaults, and attack-surface evidence: The deployment model describes WAF, API gateway authentication, private networking, managed identities, Azure Policy, and immutable images. A product threat model for delivery instructions, Kafka propagation, support access, direct-to-main release risk, and foreseeable misuse is not present.
- Logging, monitoring, security event, and incident evidence: Azure Monitor, App Insights, Log Analytics, structured logs, metrics, traces, correlation IDs, dashboards, alerts, health checks, and smoke tests are described; sensitive-data-safe logging proof for delivery notes is not present.
- Coordinated disclosure, vulnerability contact, advisory, and reporting handoff evidence: Not present.
- Security update, rollback, and support-period evidence: Rollback by previous image and compatible database state are described. Security advisory workflow, update availability, user notification, support-period end date, and pre-merge review evidence are not present.
- Product security documentation and user instructions: Not present beyond delivery and deployment documentation.

## 4. Potential Violation Or Non-Compliance Mapping

This section is not a legal finding. It lists concrete potential Cyber Resilience Act violation or non-compliance signals from the reviewed direct-to-main delivery evidence and routes each item to qualified owner review. No violation is confirmed by this engineering review.

| Potential violation or non-compliance signal | CRA reference area | Associated official-source link | Evidence from reviewed system | Current status | Required owner review | Engineering action |
| -------------------------------------------- | ------------------ | ------------------------------- | ----------------------------- | -------------- | --------------------- | ------------------ |
| Unclear product-with-digital-elements scope and economic-operator role | Scope / product categories / economic-operator obligations | [Chapter I](https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32024R2847#cpt_I), [Chapter II](https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32024R2847#cpt_II) | Java connected services, cloud deployment, APIs, Kafka, identity, payment, and delivery dependencies are described, but no product classification or operator role is recorded | Potential gap | Legal / compliance / product / market-access owner | Record product scope, EU market availability, manufacturer or supplier role, substantial modification decision, and accountable owner |
| Missing pre-merge review for product-security-risk change | Secure development / release readiness / technical documentation | [Chapter II](https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32024R2847#cpt_II), [Annex VII](https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32024R2847#anx_VII) | Engineers commit directly to `main`; the feature modifies order database structure and Kafka event data | Potential gap | Product security / platform / architecture / risk owner | Require pre-commit or pre-merge review gate for product-security controls, database migrations, Kafka contracts, sensitive fields, and production-impacting changes |
| Protected-main bypass or insufficient approval evidence | Release readiness / secure development evidence | [Chapter II](https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32024R2847#cpt_II), [Chapter V](https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32024R2847#cpt_V) | The reviewed delivery path is `commit + push directly to main`; no protected-main rule, approval requirement, or exception workflow is shown | Potential gap | Platform / product security / executive accountability owner | Enforce branch protection or equivalent signed approval workflow before product release |
| Missing product threat model and secure-default evidence for delivery instructions | Essential cybersecurity requirements for product properties | [Chapter I](https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32024R2847#cpt_I), [Annex I](https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32024R2847#anx_I) | The feature adds free-text delivery notes and Kafka event changes; no threat model for foreseeable misuse, excessive data propagation, support access, logging, or direct-to-main release risk is present | Potential gap | Product security / architecture / security owner | Add threat model, secure default field propagation rules, authorization checks, data minimization, validation, and logging tests |
| Missing coordinated disclosure, vulnerability contact, and advisory workflow evidence | Manufacturer obligations / reporting / vulnerability handling | [Chapter II](https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32024R2847#cpt_II), [Annex I](https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32024R2847#anx_I) | No vulnerability reporting contact, coordinated disclosure policy, advisory process, Article 14 reporting handoff, or user notification workflow appears in reviewed files | Potential gap | Product security / legal / compliance / support owner | Add CVD policy, vulnerability intake, triage, remediation, advisory, user notification, and reporting escalation workflow |
| Incomplete security update and rollback evidence | Security updates / vulnerability remediation / user instructions | [Chapter II](https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32024R2847#cpt_II), [Annex II](https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32024R2847#anx_II) | Signed images, automated deployment, and rollback by previous image are described; security-only update path, advisory, update availability, and user instructions are not shown | Potential gap | Product security / support / platform owner | Document signed security update path, advisory messages, rollback procedure, user mitigation guidance, and update availability commitment |
| Incomplete dependency, open-source due diligence, and SBOM evidence | Component due diligence / SBOM / technical documentation | [Chapter II](https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32024R2847#cpt_II), [Annex VII](https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32024R2847#anx_VII) | Pipeline generates SBOM and scans dependencies/images, but concrete SBOM location, triage decisions, exception owners, and third-party review are absent | Potential gap | Product security / platform / procurement / risk owner | Attach SBOM, dependency and image scan evidence, vulnerability triage, remediation SLA, open-source due diligence, provider register, and exception records |
| Missing product security documentation, support-period disclosure, and secure decommissioning guidance | User information / technical documentation / support period | [Annex II](https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32024R2847#anx_II), [Annex VII](https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32024R2847#anx_VII) | No CRA-style user instructions, vulnerability contact, support-period end, end-of-support notice, security update instructions, or decommissioning guidance are present | Potential gap | Product / support / legal / market-access owner | Add product security documentation index, support-period rationale, end-of-support notice, secure installation, secure operation, update, and decommissioning instructions |

## 5. Engineering Controls

- Pre-commit review and main-branch protection: Replace direct-to-main release eligibility for this product-security-risk change with branch protection, required review, signed approval, or an equivalent auditable control before production deployment.
- Product security scope inventory: Create a CheckoutService product security inventory covering product/component status, owners, intended purpose, foreseeable misuse, dependencies, support period, update path, evidence references, and qualified owner decisions.
- Secure-by-design development: Require secure-by-design review for delivery notes, Kafka propagation, support access, database migration, direct-to-main release risk, and external provider paths before product release.
- Threat modeling: Add threat model for free-text delivery instructions, excessive data collection, downstream Kafka exposure, replay/idempotency risks, support lookup access, direct-to-main bypass, and incident evidence handling.
- Secure defaults and hardening: Keep WAF, API gateway authentication, managed identities, private networking, Azure Policy, signed images, and immutable deployment. Add secure default for not emitting full delivery notes to Kafka unless explicitly justified.
- Authentication and authorization: Verify field-level authorization for full delivery-note lookup and least privilege for CheckoutService, support tools, Kafka, PostgreSQL, Key Vault, and deployment credentials.
- Cryptography and key ownership: Confirm HTTPS ingress, service-to-service transport security, Kafka transport security, PostgreSQL encryption, Key Vault secret storage, key ownership, rotation, and privileged operation auditability.
- Sensitive-data-safe logging and monitoring: Prove `delivery_instruction_note` is not logged, traced, indexed, or included in alert payloads; preserve correlation IDs, saga IDs, order IDs, schema versions, and event names.
- Vulnerability intake and coordinated disclosure: Add vulnerability contact, CVD policy, triage workflow, severity model, remediation SLA, exception owner, advisory process, user notification, and Article 14 reporting handoff.
- Security update mechanism: Document signed artifact release, security-only update route where feasible, rollback, smoke tests, vulnerability regression tests, and update availability.
- Dependency and SBOM evidence: Attach SBOM, dependency scan, image scan, secret scan, Maven plugin inventory, container base image review, and generated-client review to the release.
- Product security documentation: Add secure installation, operation, update, decommissioning, vulnerability contact, support-period, and integrator guidance.
- Release readiness and owner approvals: Gate production release on product, product-security, platform, support, legal/compliance, market-access, risk, and executive owner approvals where applicable.

## 6. Evidence Inventory

- Product security scope inventory: Not present.
- Product classification or economic-operator handoff: Not present.
- Threat model: Not present for delivery instructions or direct-to-main release risk.
- Secure default configuration: Partially described through platform controls; feature-specific secure defaults are not present.
- Authentication and authorization review: API gateway authentication and managed identities are described; field-level authorization review is not present.
- Cryptography review: HTTPS, private networking, and Key Vault are described; key ownership and rotation evidence are not present.
- Logging and monitoring review: Observability stack is described; privacy and sensitive-data-safe logging evidence for delivery notes is not present.
- Vulnerability disclosure policy: Not present.
- Vulnerability contact address: Not present.
- Vulnerability scan or triage evidence: Scans are described; triage records are not present.
- Security advisory evidence: Not present.
- Security update and rollback evidence: Rollback concepts are described; security advisory and update availability evidence are not present.
- Dependency or SBOM evidence: Pipeline generates SBOM and provenance; concrete SBOM artifact is not linked.
- Technical documentation: Not present in CRA-ready form.
- User instructions: Not present.
- Support-period rationale: Not present.
- End-of-support notice: Not present.
- Conformity or CE marking owner handoff: Not present.
- Release decision: Not present.
- Main-branch protection or equivalent approval: Not present.

## 7. Residual Risks

- Residual risk: Direct-to-main delivery may allow a database migration, Kafka contract change, sensitive field change, or product-security control gap to reach release without independent review.
- Impact: Incomplete secure-by-design evidence, missing technical documentation, weak vulnerability or update evidence, customer-facing checkout disruption, and unapproved product-security risk.
- Likelihood: Medium, because the reviewed delivery model commits every change directly to `main`.
- Mitigation: Require protected-main rules or equivalent review controls, approval evidence, emergency exception process, and release gates for product-security, migration, Kafka, privacy, and security-risk changes.
- Owner: Platform owner, product-security owner, architecture owner, CheckoutService owner, and executive accountability owner.
- Acceptance decision: Required before production release.
- Review date: Before the direct-to-main commit is eligible for deployment.

- Residual risk: Product scope, economic-operator role, conformity route, CE marking implications, Article 14 reporting obligations, and support-period interpretation are unknown.
- Impact: Late compliance escalation, missing owner approvals, incomplete technical documentation, or incorrect product-security release assumptions.
- Likelihood: Unknown.
- Mitigation: Route the classification and conformity questions to legal, compliance, product, product-security, support, market-access, risk, and executive accountability owners.
- Owner: Legal/compliance owner, product owner, market-access owner, and product-security owner.
- Acceptance decision: Required before treating the change as CRA-ready.
- Review date: Before production release or product availability decision.

- Residual risk: Delivery instruction free text may increase sensitive data, logging, support access, Kafka propagation, or incident evidence exposure.
- Impact: Unnecessary data processing, unauthorized access, sensitive-data exposure, and incomplete secure-by-design evidence.
- Likelihood: Medium, because the feature explicitly introduces free text and event changes.
- Mitigation: Minimize fields, avoid Kafka payload propagation of full notes, add field-level authorization, redact logs, add tests, and document data removal.
- Owner: CheckoutService owner, product-security owner, support owner, data/privacy owner, and legal/compliance owner.
- Acceptance decision: Required before production release.
- Review date: Before committing to `main` and before deployment.

## 8. Release Decision

- Decision: Blocked for production until direct-to-main delivery is supplemented with product-security change-control evidence and CRA owner handoffs.
- Conditions:
  - Protected-main or equivalent pre-commit approval control is enforced for this change.
  - Product scope, economic-operator role, conformity, CE marking, reporting, and support-period questions are routed to qualified owners.
  - Threat model covers delivery notes, Kafka events, database migration, support lookup, sensitive-data-safe logging, direct-to-main release risk, and foreseeable misuse.
  - Secure defaults minimize delivery note propagation and restrict access to full notes.
  - Vulnerability contact, coordinated disclosure policy, advisory workflow, and Article 14 handoff are documented.
  - SBOM, dependency scan, image scan, secret scan, vulnerability triage, and exception records are linked.
  - Security update path, rollback evidence, user mitigation guidance, and update availability are documented.
  - Product security documentation, support-period rationale, end-of-support notice, and secure decommissioning guidance are linked where applicable.
- Blockers:
  - Direct-to-main commit path lacks explicit pre-merge review, protected-main approval, or exception evidence.
  - Missing product security scope inventory and qualified owner handoffs.
  - Missing threat model, CVD policy, vulnerability contact, advisory workflow, and support-period evidence.
  - Missing concrete SBOM artifact, vulnerability triage, security update evidence, and product security documentation.
  - Missing release evidence for migration approval, Kafka compatibility, sensitive-data-safe logging, and field-level authorization.
- Required approvals: Product owner, technical owner, product-security owner, platform owner, support owner, architecture owner, data/privacy owner, legal/compliance owner, market-access owner when applicable, business owner, and executive accountability owner for direct-to-main exception or approval policy.
- Expiry or review date: Before committing to `main` and again before production deployment.
- Environments or product versions approved: Development and test only until blockers are resolved.
- Emergency security update path: Signed security update through CI/CD with advisory, user mitigation guidance, vulnerability regression tests, and support owner approval.
- Emergency rollback path: Previous signed image plus compatible database state. Confirm whether the order table migration can be safely rolled forward or rolled back without losing delivery instruction data.

## 9. Action Plan

| Priority | Action | Owner | Due date | Evidence expected | Status |
| -------- | ------ | ----- | -------- | ----------------- | ------ |
| High | Add protected-main or equivalent auditable review control for the CheckoutService delivery-instructions change | Platform owner / product-security owner | Before production deployment | Branch protection, approval, or exception evidence | Open |
| High | Require cybersecurity and product-security pre-commit review for database migrations, Kafka contracts, sensitive fields, support workflows, and production-side-effect changes | Product security / architecture owner | Before committing to `main` | Review checklist and approval record | Open |
| High | Create CRA product security scope inventory for CheckoutService and delivery-instructions change | Product owner / product-security owner | Before release approval | Inventory with product scope, intended purpose, owners, update path, support-period signal, and evidence links | Open |
| High | Route product classification, economic-operator role, conformity assessment, CE marking, Article 14 reporting, and support-period interpretation to qualified owners | Legal / compliance / market-access / product owner | Before release approval | Owner decision record or explicit deferred decision | Open |
| High | Add threat model and secure-default review for delivery notes, Kafka propagation, support lookup, direct-to-main delivery, and foreseeable misuse | Product security / architecture owner | Before release approval | Threat model and secure-default checklist | Open |
| High | Add field-level authorization and sensitive-data-safe logging tests for delivery instruction data | CheckoutService owner / security owner | Before release approval | Test results and logging policy evidence | Open |
| High | Add database migration approval and Kafka schema version 3 compatibility evidence | CheckoutService owner / data owner / downstream owners | Before release approval | Migration test results, contract tests, rollback or forward-fix plan | Open |
| High | Attach SBOM, dependency scan, image scan, secret scan, and vulnerability triage records | Platform owner / product-security owner | Before production deployment | Evidence bundle and exception decisions | Open |
| High | Add vulnerability contact, CVD policy, security advisory workflow, and Article 14 reporting handoff | Product security / legal / support owner | Before production deployment | Policy, contact, advisory template, and escalation workflow | Open |
| Medium | Document signed security update path, update availability, user mitigation guidance, and rollback evidence | Platform owner / support owner | Before production deployment | Security update runbook and rollback rehearsal | Open |
| Medium | Add product security documentation, support-period rationale, end-of-support signaling, and secure decommissioning instructions where applicable | Product owner / support owner | Before product availability decision | Documentation index and support-period record | Open |
| Low | Schedule next CRA engineering review after deployment and first security update exercise | Product-security owner / support owner | After production validation | Review record and corrective actions | Open |

## 10. Final Notes

- Items requiring legal interpretation: Product-with-digital-elements scope, economic-operator role, substantial modification, Article 14 reporting obligations, applicability dates, conformity assessment, CE marking implications, and regulatory interpretation.
- Items requiring product classification or economic-operator role decision: CheckoutService component status, EU market availability, manufacturer or supplier responsibilities, important or critical product category signals, and remote data processing responsibility.
- Items requiring conformity assessment or CE marking review: Technical documentation expectations, declaration evidence, CE marking implications, and whether any notified body or certification route is needed.
- Items requiring Article 14 reporting interpretation: Actively exploited vulnerability and severe incident criteria, reporting endpoints, notification timing, and user notification obligations.
- Items requiring product security exception: Any release without protected-main or equivalent approval evidence, threat model, CVD policy, vulnerability triage, SBOM, security update path, sensitive-data-safe logging evidence, or support-period handoff.
- Items requiring architecture decision: Direct-to-main release policy, Kafka schema versioning, older-consumer compatibility, free-text note lookup pattern, database migration rollout, rollback limits, and downstream service ownership.
- Items requiring support or end-of-support review: Support-period statement, security update availability, customer advisory process, end-of-support notification, and secure decommissioning guidance.
- Items requiring procurement, supplier, or open-source steward review: Azure services, external identity provider, external payment gateway, email/SMS provider, Artifactory, Docker registry, CI/CD actions, Quarkus libraries, Maven plugins, and container base images.
- Items requiring product or business acceptance: Residual risk for direct-to-main delivery, delivery note privacy, product-security classification uncertainty, support workflow changes, possible checkout or delivery disruption, and customer-facing rollout timing.
- Next review trigger: Direct-to-main approval request, production deployment request, product availability decision, security update, schema or event contract change, provider change, vulnerability report, incident, support-period change, or end-of-support notice.
