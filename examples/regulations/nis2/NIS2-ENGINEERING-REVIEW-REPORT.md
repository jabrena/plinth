# NIS2 Engineering Review Report

Use this report as engineering evidence for legal, compliance, security, risk, resilience, business-continuity, procurement, executive accountability, architecture, and business-owner review.

This report is not legal advice. It identifies engineering controls and escalation points from the reviewed system evidence. Entity classification, member-state applicability, incident-reporting obligations, cybersecurity risk acceptance, and regulatory interpretation require qualified owner review.

## 1. Review Context

- System or service name: CheckoutService delivery instructions change
- Repository, product, platform, or business service: Fictional Java Quarkus ecommerce platform
- Review date: 2026-06-14
- Reviewers: AI-assisted NIS2 engineering review
- Business owner: Not identified in reviewed evidence
- Technical owner: Software engineers, tech leads, reviewers, and platform engineers are described, but no named CheckoutService owner is identified
- Security owner: Not identified in reviewed evidence
- Resilience or continuity owner: Not identified in reviewed evidence
- Legal/compliance owner: Not identified in reviewed evidence
- Source materials reviewed:
  - `examples/diagrams/deployment/system-example-cicd-pr-model.md`
  - `examples/diagrams/deployment/expected-system-deployment.puml`
  - `examples/diagrams/deployment/checkout-service-feature-request.md`
  - `.agents/skills/804-regulations-eu-nis2/references/804-regulations-eu-nis2.md`
  - `.agents/skills/804-regulations-eu-nis2/assets/reports/804-nis2-engineering-review-report-template.md`

## 2. Service And Sector Context

- Service description: CheckoutService coordinates checkout saga steps for price validation, inventory reservation, payment authorization, order creation, delivery slot booking, PostgreSQL order state, saga state, and Kafka events.
- Possible essential or important entity signal: The reviewed evidence describes customer-facing ecommerce checkout, payments, delivery, identity, managed cloud services, and production operation. It does not confirm NIS2 essential or important entity classification.
- Possible sector signal: Digital commerce, online service delivery, payment-adjacent workflows, cloud-hosted Java services, and managed platform dependencies. Legal and compliance owners must decide whether any covered sector or member-state implementation applies.
- Managed service provider or digital infrastructure signal: Azure Kubernetes Service, Azure PostgreSQL, Kafka, Key Vault, Azure Monitor, App Insights, Log Analytics, Artifactory, Docker registry, external identity provider, payment gateway, and email/SMS provider are in scope as provider or supply-chain dependencies.
- Deployment geography: Azure production environment is described; member-state geography is not specified.
- Environments in scope: Development, test, staging, and production.
- Critical users or affected organizations: Shoppers, support and operations teams, platform engineers, downstream inventory, delivery, notification, analytics, and payment workflows.
- Production dependencies: Azure Front Door, WAF, AKS ingress, Quarkus services, PostgreSQL stores, Kafka topics, Key Vault, Azure Monitor, App Insights, Log Analytics, Backup Vault, Artifactory, Docker registry, external identity, external payment, and notification provider.
- Open applicability questions:
  - Does the ecommerce platform provide an essential or important service under the applicable member-state NIS2 implementation?
  - Which entity owns incident-reporting obligations for payment, identity, delivery, and cloud-provider dependencies?
  - Are free-text delivery notes personal data requiring additional privacy, retention, and incident escalation controls?
  - Which legal, compliance, security, risk, resilience, procurement, and executive owners must approve residual cybersecurity risk?

## 3. Cybersecurity Scope

- Applications, APIs, jobs, and batch workloads: Web storefront BFF, API gateway, identity, catalog, search, cart, pricing, CheckoutService, payment adapter, inventory, delivery slot, notification worker, analytics pipeline, checkout saga logic, and deployment workflows.
- Data stores, queues, topics, and caches: User Profile DB, Product DB, Search Index, Cart DB, Pricing DB, Order DB, Saga Support PostgreSQL, Inventory DB, Delivery DB, Kafka topics `order.created`, `payment.authorized`, `stock.reserved`, `slot.booked`, `delivery.slot.requested`, `order.confirmed`, and compensation events.
- IAM, secrets, keys, and privileged operations: Azure Key Vault, managed identities, API gateway authentication enforcement, identity provider integration, payment tokens, idempotency keys, database credentials, Kafka credentials, and deployment credentials.
- CI/CD workflows and deployment paths: Short-lived branch, pull request review and checks, CI/CD pipeline, Artifactory, Docker registry, signed images, SBOM and provenance metadata, GitOps/CD pipeline, automated environment promotion, migration safety checks, health checks, and smoke tests.
- Third-party providers and SaaS dependencies: Azure platform services, external identity provider, external payment gateway, email/SMS provider, Artifactory, Docker registry, and observability services.
- Container images, build plugins, libraries, and runtime dependencies: Quarkus services, common Java libraries for correlation IDs, observability, HTTP error envelopes, Kafka event envelopes, schema versioning helpers, Quarkus test fixtures, Maven conventions, Docker images, and build pipeline tools.
- Monitoring, alerting, and observability systems: Azure Monitor, App Insights, Log Analytics, structured logs, metrics, traces, correlation IDs, dashboards, alerts, health checks, and post-deployment smoke tests.
- Operational runbooks and support model: Support and operations teams are described, but incident runbooks, severity criteria, escalation contacts, continuity procedures, restore evidence, and provider handoff playbooks are not present in the reviewed files.

## 4. Potential Violation Or Non-Compliance Mapping

This section is not a legal finding. It lists concrete potential NIS2 violation or non-compliance signals from the reviewed evidence and routes each item to qualified owner review. No violation is confirmed by this engineering review.

| Potential violation or non-compliance signal | NIS2 reference area | Evidence from reviewed system | Current status | Required owner review | Engineering action |
| -------------------------------------------- | ------------------- | ----------------------------- | -------------- | --------------------- | ------------------ |
| Unclear essential or important entity scope | Applicability / member-state implementation | Ecommerce production service, payment-adjacent checkout, delivery workflows, cloud-hosted Java platform, and managed providers are described, but no legal classification is recorded | Potential gap | Legal / compliance / business owner | Record entity classification decision, geography, service criticality, and accountable executive owner |
| Missing named owners for assets and controls | Cybersecurity risk-management measures | Technical actor groups are described, but no system owner, security owner, resilience owner, provider owner, or risk acceptance owner is named for CheckoutService | Potential gap | Security / risk / architecture / business owner | Create asset and service inventory with owners for CheckoutService, Order DB, Saga Support, Kafka topics, provider integrations, CI/CD, and Azure dependencies |
| Incomplete dependency and vulnerability evidence | Supply-chain security / provider dependencies | Pipeline runs dependency scanning, image scanning, secret scanning, SBOM, and provenance, but no triage workflow, exception owner, due dates, or provider review evidence is included | Potential gap | Security / procurement / platform owner | Link SBOM, vulnerability findings, provider register, remediation SLA, exception process, and verification evidence to the release record |
| Missing incident detection, escalation, or evidence | Incident handling / reporting handoff | Structured logs, metrics, traces, correlation IDs, dashboards, alerts, Azure Monitor, App Insights, and Log Analytics are described, but no severity model, incident workflow, reporting handoff, or evidence retention rule is shown | Potential gap | Security operations / legal / compliance | Add incident runbook, severity triage, evidence capture, containment steps, owner notification, and qualified reporting handoff |
| Untested backup, recovery, or continuity controls | Business continuity / crisis management | Backup Vault, rollback by previous image, compatible database state, migration checks, and health checks are described, but no restore test date, RTO/RPO, failover exercise, or continuity owner appears | Potential gap | Resilience / business-continuity owner | Add RTO/RPO, restore test evidence, backup age alerts, rollback rehearsal, migration rollback limits, and continuity exercise records |
| Weak access-control, cryptography, or secure configuration evidence | Access control / cryptography / secure operations | HTTPS, WAF, API gateway authentication, Key Vault, managed identities, private networking, and Azure Policy are described; MFA, privileged access review, encryption ownership, key rotation, and baseline hardening evidence are not shown | Potential gap | Security / platform owner | Record secure configuration baseline, privileged access review, key ownership, rotation evidence, encryption settings, and policy compliance |
| Database migration and Kafka message contract risks | Secure development / change control | Feature adds order columns and changes Kafka event schema to version 3 for `delivery.slot.requested`, `order.created`, and `order.confirmed` | Potential gap | Architecture / data owner / platform owner | Require migration approval, compatibility tests, schema registry or contract evidence, rollback plan, and consumer readiness evidence |
| Production side-effect risk from automated deployment | Change control / release governance | Pipeline automatically deploys through environments and production after checks pass | Potential gap | Platform / security / business owner | Gate production release on cybersecurity evidence, provider review, continuity evidence, and owner approval for the delivery-instructions change |

## 5. Engineering Controls

- Asset and service inventory: Add a CheckoutService inventory entry covering service owner, security owner, resilience owner, Order DB, Saga Support, Kafka topics, provider dependencies, secrets, environments, dashboards, alerts, recovery targets, and runbooks.
- Secure configuration and hardening: Preserve WAF, API gateway authentication, private networking, Key Vault, managed identities, Azure Policy, immutable images, and digest or validated-tag deployment. Add baseline evidence, privileged access review, MFA signal, key rotation, and environment configuration review.
- Vulnerability and dependency management: Use CI dependency scanning, image scanning, SBOM, provenance, and Artifactory records as starting evidence. Add triage status, severity, owner, remediation due date, exception approval, and verification record.
- Incident detection and escalation: Convert logs, metrics, traces, correlation IDs, dashboards, and alerts into an incident workflow with severity classification, containment actions, owner notification, reporting handoff, and post-incident corrective action.
- Evidence-safe logging: Exclude `delivery_instruction_note` from operational logs and Kafka payloads. Preserve correlation IDs, saga IDs, order IDs, schema versions, and event names without logging free-text delivery notes, access codes, secrets, tokens, or sensitive incident details.
- Monitoring and alerting: Add alerts for checkout failure spikes, Kafka publish failures, consumer schema errors, migration failures, payment gateway callback failures, delivery slot booking failures, backup age, and elevated retry or compensation rates.
- Backup, restore, and continuity: Record RTO/RPO for Order DB and Saga Support, backup schedule, last restore test, restore result, continuity owner, failover evidence, and repeat restore testing after the schema migration.
- Rollback and recovery: Keep rollback to previous validated image, add a database migration rollback or forward-fix plan, document schema compatibility with old application versions, and confirm Kafka consumers ignore new `deliveryInstructions` fields.
- Supply-chain security: Track Quarkus libraries, Maven plugins, common Java libraries, container base images, CI/CD actions, Artifactory, Docker registry, Azure services, identity provider, payment gateway, and email/SMS provider with owners, support contacts, scan evidence, and review status.
- Access control and least privilege: Verify service-to-service lookup for full delivery notes uses field-level authorization and least privilege. Review managed identity scopes for CheckoutService, Order DB, Kafka, Key Vault, and observability writes.
- Cryptography and key ownership: Confirm HTTPS ingress, service-to-database private connectivity, Kafka transport security, secret storage in Key Vault, key ownership, rotation schedule, and access auditability.
- Secure development and change control: Require pull request review, database migration test, Kafka contract test, privacy-safe logging test, schema version compatibility test, architecture review for data propagation, and release approval before production deployment.

Example incident escalation control:

```java
IncidentDecision escalateCheckoutIncident(SecurityIncident incident) {
    var severity = severityPolicy.classify(incident);
    var evidence = evidenceRecorder.capture(
        incident.traceId(),
        "CheckoutService",
        severity,
        incident.affectedAssets(),
        incident.suspectedVulnerabilityIds()
    );
    incidentWorkflow.notifyOwners(
        severity,
        List.of("checkout-owner", "security-operations", "risk-owner", "resilience-owner"),
        evidence.id()
    );
    return IncidentDecision.escalated(incident.id(), severity, evidence.id());
}
```

Example vulnerability evidence control:

```java
ReleaseDecision requireVulnerabilityClosure(ReleaseEvidence evidence) {
    if (!evidence.sbomPublished() || evidence.highFindingsWithoutOwner()) {
        return ReleaseDecision.blocked(
            "CheckoutService",
            List.of("SBOM or high-severity vulnerability triage evidence missing"),
            Escalation.required("security", "platform", "risk")
        );
    }
    return ReleaseDecision.approvedWithEvidence("CheckoutService", evidence.references(), evidence.approvers());
}
```

Example supply-chain review control:

```java
SupplyChainReview reviewProviderDependency(ProviderDependency provider) {
    return SupplyChainReview.require(
        provider.name(),
        provider.owner(),
        provider.supportContact(),
        provider.vulnerabilityMonitoringEvidence(),
        provider.continuityEvidence()
    );
}
```

Example secure release-policy control:

```java
ReleaseDecision evaluateCheckoutRelease(Nis2CybersecurityReview review) {
    var blockers = new ArrayList<String>();
    requirePresent(review.assetInventory(), "asset and service inventory", blockers);
    requirePassing(review.vulnerabilityReview(), "vulnerability review", blockers);
    requirePresent(review.incidentRunbook(), "incident escalation runbook", blockers);
    requirePassing(review.restoreTest(), "restore test after order schema migration", blockers);
    requirePassing(review.kafkaContractCompatibility(), "Kafka schema compatibility", blockers);
    requirePresent(review.rollbackPlan(), "rollback plan", blockers);
    return blockers.isEmpty()
        ? ReleaseDecision.approvedWithEvidence(review.serviceId(), review.evidenceReferences(), review.approvers())
        : ReleaseDecision.blocked(review.serviceId(), blockers, Escalation.required("security", "risk", "resilience", "service-owner"));
}
```

## 6. Evidence Inventory

- Asset inventory: Not present as a dedicated inventory; runtime topology and service responsibilities provide raw inputs.
- Dependency or SBOM evidence: Pipeline generates SBOM and provenance metadata; concrete SBOM reference not present.
- Vulnerability scan or triage evidence: Pipeline runs dependency and image scanning; triage records not present.
- Secure configuration baseline: WAF, private networking, Key Vault, managed identities, Azure Policy, immutable images, and validated tags are described; baseline evidence not present.
- Incident runbook: Not present.
- Monitoring dashboards: Azure Monitor, App Insights, Log Analytics, dashboards, logs, traces, metrics, and alerts are described; dashboard IDs and alert rules not present.
- Alert routing: Not present.
- Backup and restore evidence: Backup Vault and rollback concepts are described; restore test evidence not present.
- Continuity or failover exercise: Not present.
- Provider review: External identity, payment, notification, Azure, Artifactory, and registry dependencies are described; provider review records not present.
- Change approval: Pull request review and checks are described; explicit cybersecurity approval for the feature request is not present.
- Release decision: Not present.

## 7. Residual Risks

- Residual risk: Delivery instruction free-text handling may expose personal or sensitive operational details through logs, Kafka, support tools, or incident evidence if not controlled.
- Impact: Unauthorized disclosure, over-broad propagation to Kafka consumers, difficult incident reconstruction, and incomplete audit trail.
- Likelihood: Medium, because the feature explicitly introduces free text and event changes.
- Mitigation: Exclude notes from Kafka and logs, use field-level authorization for lookup, add privacy-safe logging tests, and review retention rules.
- Owner: CheckoutService owner, security owner, data/privacy owner, and legal/compliance owner.
- Acceptance decision: Required before production release.
- Review date: Before merging and before production deployment.

- Residual risk: Database migration and Kafka schema change may disrupt checkout, delivery, notification, or analytics during rollout.
- Impact: Checkout failure, delivery booking errors, stale consumers, compensation workflow increase, and customer-impacting incidents.
- Likelihood: Medium, because the feature changes persisted order structure and event contracts.
- Mitigation: Backward-compatible migration, consumer compatibility tests, schema version 3 contract evidence, rollback or forward-fix plan, and post-deployment monitoring.
- Owner: Architecture owner, CheckoutService owner, platform owner, and downstream service owners.
- Acceptance decision: Required before production release.
- Review date: During PR review and release readiness.

- Residual risk: NIS2 applicability, reporting obligations, provider responsibility, and member-state requirements are not determined by reviewed engineering evidence.
- Impact: Late compliance escalation, missed reporting handoff, or unapproved residual cybersecurity risk.
- Likelihood: Unknown.
- Mitigation: Route classification and reporting questions to legal, compliance, security, risk, resilience, procurement, and executive accountability owners.
- Owner: Legal/compliance and executive accountability owner.
- Acceptance decision: Required if the platform is relied on for covered essential or important services.
- Review date: Before production reliance.

## 8. Release Decision

- Decision: Conditionally blocked for production until cybersecurity evidence is attached to the PR and release record.
- Conditions:
  - Asset and service inventory names owners for CheckoutService, Order DB, Saga Support, Kafka topics, secrets, providers, and environments.
  - Database migration tests prove nullable/default behavior and rollback or forward-fix readiness.
  - Kafka contract tests prove schema version 3 compatibility and older-consumer tolerance.
  - Privacy-safe logging tests prove `delivery_instruction_note` is not logged or emitted through Kafka.
  - SBOM, vulnerability scan, image scan, secret scan, and triage evidence are linked.
  - Incident escalation, alert routing, backup/restore, and continuity evidence are linked.
  - Legal/compliance confirms whether NIS2 classification or reporting handoff review is required.
- Blockers:
  - Missing named system, security, resilience, legal/compliance, procurement, and executive accountability owners.
  - Missing incident runbook, severity model, alert routing, provider review, restore test evidence, and continuity exercise evidence.
  - Missing release evidence for migration approval, Kafka compatibility, vulnerability triage, and privacy-safe logging.
- Required approvals: Service owner, tech lead, security owner, resilience owner, platform owner, architecture owner, data/privacy owner, legal/compliance owner when applicable, and business owner.
- Expiry or review date: Before merge and again before production deployment.
- Environments approved: Development and test only until blockers are resolved.
- Emergency rollback path: Previous validated image plus compatible database state. Confirm whether the order table migration can be safely rolled forward or rolled back without losing delivery instruction data.

## 9. Action Plan

| Priority | Action | Owner | Due date | Evidence expected | Status |
| -------- | ------ | ----- | -------- | ----------------- | ------ |
| High | Create CheckoutService NIS2 asset and service inventory with owners, assets, providers, secrets, environments, recovery targets, dashboards, and runbooks | Technical owner / platform owner | Before PR approval | Inventory record linked to release | Open |
| High | Add migration approval and tests for nullable/default order columns and rollback or forward-fix behavior | CheckoutService owner / data owner | Before merge | Migration test results and release note | Open |
| High | Add Kafka schema version 3 contract tests and older-consumer compatibility evidence | CheckoutService owner / downstream service owners | Before merge | Contract test report and schema evidence | Open |
| High | Add privacy-safe logging tests proving delivery notes are not logged or broadcast to Kafka | Security owner / CheckoutService owner | Before merge | Test report and logging policy evidence | Open |
| High | Attach SBOM, dependency scan, image scan, secret scan, vulnerability triage, and exception records | Security owner / platform owner | Before production deployment | CI evidence bundle and triage ticket links | Open |
| High | Add incident runbook with severity triage, evidence capture, containment, owner notification, and reporting handoff | Security operations / legal-compliance owner | Before production deployment | Runbook and alert-routing evidence | Open |
| Medium | Add backup/restore and continuity evidence for Order DB and Saga Support after migration | Resilience owner / platform owner | Before production deployment | Restore test result, RTO/RPO, and rollback rehearsal | Open |
| Medium | Record provider and supply-chain review for Azure, identity provider, payment gateway, notification provider, Artifactory, Docker registry, and CI/CD actions | Procurement / security / platform owner | Before production deployment | Provider register and review decisions | Open |
| Medium | Confirm access-control and cryptography evidence for service-to-service note lookup, Key Vault, Kafka, PostgreSQL, and privileged deployment operations | Security owner / platform owner | Before production deployment | Access review and key ownership evidence | Open |
| Low | Schedule next NIS2 engineering review after deployment and first incident-readiness exercise | Security owner / resilience owner | After production validation | Review record and corrective actions | Open |

## 10. Final Notes

- Items requiring legal interpretation: Essential or important entity classification, member-state applicability, incident-reporting obligations, regulatory interpretation, and whether checkout delivery instructions affect regulated service obligations.
- Items requiring security exception: Any release without vulnerability triage, privacy-safe logging evidence, incident runbook, alert routing, access-control review, or secure configuration baseline.
- Items requiring architecture decision: Kafka schema versioning, older-consumer compatibility, free-text note lookup pattern, database migration rollout, rollback limits, and downstream service ownership.
- Items requiring resilience or continuity review: Backup schedule, restore test, RTO/RPO, rollback rehearsal, Kafka failure handling, saga recovery, and continuity owner approval.
- Items requiring procurement or provider review: Azure services, external identity provider, external payment gateway, email/SMS provider, Artifactory, Docker registry, CI/CD actions, and observability services.
- Items requiring product or business acceptance: Residual risk for delivery note privacy, support workflow changes, possible checkout or delivery disruption, and customer-facing rollout timing.
- Next review trigger: PR approval request, production deployment request, schema or event contract change, provider change, incident, vulnerability exception, or continuity test failure.
