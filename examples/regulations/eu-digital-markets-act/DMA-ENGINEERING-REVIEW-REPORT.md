# EU Digital Markets Act Engineering Review Report

Use this report as engineering evidence for legal, compliance, platform governance, product, privacy, security, risk, competition, executive accountability, architecture, and business-owner review.

This report is not legal advice. It identifies engineering controls and escalation points from the reviewed system evidence. Gatekeeper designation, core platform service scope, obligation applicability, consent interpretation, self-preferencing assessments, fair access terms, suspension or exemption questions, and regulatory interpretation require qualified owner review.

## 1. Review Context

- System or service name: CheckoutService delivery instructions change
- Repository, product, platform, or business service: Fictional Java Quarkus ecommerce platform
- Review date: 2026-06-14
- Reviewers: AI-assisted EU Digital Markets Act engineering review
- Business owner: Not identified in reviewed evidence
- Technical owner: Software engineers, tech leads, reviewers, and platform engineers are described, but no named CheckoutService owner is identified
- Platform owner: Platform engineers are described, but no named platform owner is identified
- Product owner: Not identified in reviewed evidence
- Data owner: Not identified in reviewed evidence
- Privacy owner: Not identified in reviewed evidence
- Security owner: Not identified in reviewed evidence
- Legal/compliance owner: Not identified in reviewed evidence
- Source materials reviewed:
  - `examples/diagrams/deployment/system-example-cicd-pr-model.md`
  - `examples/diagrams/deployment/expected-system-deployment.puml`
  - `examples/diagrams/deployment/checkout-service-feature-request.md`
  - `.agents/skills/808-regulations-eu-digital-markets-act/references/808-regulations-eu-digital-markets-act-chapters-summary.md`
  - `.agents/skills/808-regulations-eu-digital-markets-act/references/808-regulations-eu-digital-markets-act-engineering-examples.md`
  - `.agents/skills/808-regulations-eu-digital-markets-act/assets/reports/808-eu-digital-markets-act-engineering-review-report-template.md`

## 2. Platform And Core Service Context

- Service description: CheckoutService coordinates checkout saga steps for price validation, inventory reservation, payment authorization, order creation, delivery slot booking, PostgreSQL order state, saga state, and Kafka events.
- Possible core platform service signal: The evidence describes a customer-facing ecommerce platform with storefront, catalog, search, cart, checkout, delivery, identity, payment gateway integration, Kafka, and business delivery workflows. It does not confirm a DMA core platform service classification such as online intermediation, app store, search engine, social network, browser, operating system, messaging, cloud, or advertising service.
- Possible gatekeeper-scope signal: No gatekeeper designation, listed core platform service, active business-user count, active end-user count, or Commission designation evidence is present.
- Business-user journey: Not explicitly described. The platform may include sellers, advertisers, or third-party merchants, but the reviewed files only describe shoppers and internal teams.
- End-user journey: Shoppers authenticate, browse products, add products to cart, check out, pay, book delivery slots, and receive notifications.
- Deployment geography: Azure production environment is described; EU user geography and Member State scope are not specified.
- Environments in scope: Development, test, staging, and production.
- Platform dependencies: Azure Front Door, WAF, AKS, PostgreSQL, Kafka, Key Vault, Azure Monitor, App Insights, Log Analytics, Backup Vault, Artifactory, Docker registry, external identity provider, external payment gateway, and email/SMS provider.
- Open applicability questions:
  - Is the ecommerce platform part of a designated gatekeeper core platform service?
  - Does the platform act as an online intermediation service for business users selling to end users?
  - Are search, ranking, marketplace, advertising, identity, or payment-support features subject to DMA owner review?
  - Which legal, compliance, platform governance, product, privacy, security, risk, and competition owners must approve residual DMA risk?

## 3. DMA Engineering Scope

- Applications, APIs, jobs, and batch workloads: Web storefront BFF, API gateway, identity, catalog, search, cart, pricing, CheckoutService, payment adapter, inventory, delivery slot, notification worker, analytics pipeline, checkout saga logic, and deployment workflows.
- Data stores, queues, topics, indexes, and caches: User Profile DB, Product DB, Search Index, Cart DB, Pricing DB, Order DB, Saga Support PostgreSQL, Inventory DB, Delivery DB, Kafka topics `order.created`, `payment.authorized`, `stock.reserved`, `slot.booked`, `delivery.slot.requested`, `order.confirmed`, and compensation events.
- Business-user data access APIs: Not present in reviewed evidence.
- End-user portability or preference workflows: Not present in reviewed evidence.
- Interoperability interfaces: Kafka events and service-to-service lookup are described for internal services; third-party interoperability interfaces are not present.
- Ranking, search, recommendation, or advertising systems: Catalog and search services are described, but ranking policies, self-preferencing audit signals, and advertising metrics are not present.
- Consent, preference, and identity flows: Identity provider integration is described. Consent and preference evidence for data combination, delivery preferences, or data sharing is not present.
- Marketplace, app-store, browser, operating-system, messaging, cloud, or advertising features: Cloud-hosted ecommerce and search features are described. No app-store, browser, operating-system, messaging interoperability, or advertising transparency workflow is present.
- IAM, secrets, keys, and privileged operations: Azure Key Vault, managed identities, API gateway authentication, payment tokens, idempotency keys, database credentials, Kafka credentials, and deployment credentials.
- CI/CD workflows and deployment paths: Short-lived branch, pull request review and checks, CI/CD pipeline, Artifactory, Docker registry, signed images, SBOM and provenance metadata, GitOps/CD pipeline, automated environment promotion, migration safety checks, health checks, and smoke tests.
- Monitoring, alerting, and observability systems: Azure Monitor, App Insights, Log Analytics, structured logs, metrics, traces, correlation IDs, dashboards, alerts, health checks, and smoke tests.
- Documentation, compliance reports, and owner handoff records: Not present as DMA-specific records.

## 4. Potential Violation Or Non-Compliance Mapping

This section is not a legal finding. It lists concrete potential DMA violation or non-compliance signals from the reviewed delivery evidence and routes each item to qualified owner review. No violation is confirmed by this engineering review.

| Potential violation or non-compliance signal | DMA reference area | Associated official-source link | Evidence from reviewed system | Current status | Required owner review | Engineering action |
| -------------------------------------------- | ------------------ | ------------------------------- | ----------------------------- | -------------- | --------------------- | ------------------ |
| Unclear gatekeeper or core platform service scope | Applicability / designation / service listing | [Chapter I](https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32022R1925#cpt_I), [Chapter II](https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32022R1925#cpt_II), [Annex](https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32022R1925#anx) | Ecommerce production platform, search, identity, payment, delivery, and cloud-hosted services are described, but no designation or service-classification evidence is recorded | Potential gap | Legal / compliance / platform governance / product | Record qualified owner decision on gatekeeper and core platform service scope before applying DMA-specific release gates |
| Missing business-user data access and export evidence | Business-user data access / data portability | [Chapter III](https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32022R1925#cpt_III) | No business-user portal, data access API, export workflow, or authorised third-party access evidence is present | Potential gap | Product / data owner / compliance / privacy | If business users are in scope, define data access API contracts, export formats, consent boundaries, audit logs, and support workflow |
| Missing interoperability evidence | Interoperability / effective access / security preservation | [Chapter III](https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32022R1925#cpt_III) | Internal Kafka and service lookup are described, but no external interoperability request workflow, reference offer, or third-party compatibility evidence exists | Potential gap | Platform / security / compliance | Determine whether interoperability obligations are relevant; if so, document interface contracts, request handling, security controls, compatibility tests, and degradation monitoring |
| Missing consent and preference evidence | Consent / personal-data combination / preference handling | [Chapter III](https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32022R1925#cpt_III) | Delivery preferences and identity integration are described; no consent ledger, withdrawal evidence, neutral UI review, or purpose-bound data-sharing evidence is shown | Potential gap | Privacy / legal / product | Add consent and preference evidence where platform data is combined, cross-used, shared, or used for advertising, identity, or delivery preferences |
| Missing ranking or self-preferencing audit signals | Ranking / fair and non-discriminatory treatment / own-service treatment | [Chapter III](https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32022R1925#cpt_III) | Search service exists, but ranking policy version, own-service flags, experiments, sponsored placement, and fairness checks are not described | Potential gap | Product / competition / compliance / architecture | Add ranking audit events, policy versioning, experiment evidence, own-service treatment checks, and qualified self-preferencing review |
| Weak anti-circumvention and change-control evidence | Anti-circumvention / non-neutral choice / degraded quality / service fragmentation | [Chapter III](https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32022R1925#cpt_III) | Feature adds delivery instruction storage and Kafka schema changes; no DMA-specific review verifies that choices, access, exports, or interoperability are not degraded | Potential gap | Legal / compliance / platform governance / product | Add DMA review to release gates for ranking, access, consent, data export, interoperability, platform-default, and service-boundary changes |
| Incomplete compliance evidence handoff | Compliance demonstration / reporting / monitoring | [Chapter III](https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32022R1925#cpt_III), [Chapter V](https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32022R1925#cpt_V) | Logs, metrics, traces, SBOM, provenance, and deployment evidence are described, but no DMA compliance report, owner handoff, or evidence repository is present | Potential gap | Compliance / security / risk / executive accountability | Create evidence inventory for APIs, data flows, rankings, consent, exports, releases, and owner decisions |

## 5. Engineering Controls

- Interoperability interfaces: If third-party interoperability is in scope, publish versioned API contracts, request workflow, security model, compatibility tests, degradation checks, and audit events.
- Data access APIs: Define business-user and authorised-third-party access scopes, dataset lineage, personal-data boundaries, authorization, rate limits, export formats, error handling, and audit records.
- Business-user export workflows: Avoid manual database extracts. Provide request status, delivery channel, retention period, support path, schema version, and failure alerts.
- End-user portability workflows: If in scope, document export formats, continuous access where required, identity verification, privacy checks, and delivery evidence.
- Consent and preference evidence: Preserve specific opt-in, withdrawal, prompt history, retry suppression, neutral UI review, purpose binding, and privacy/legal owner handoff where data is combined or shared.
- Ranking and self-preferencing audit signals: Add ranking policy versions, experiment IDs, own-service result counts, third-party result counts, sponsored placement counts, fairness checks, and review triggers.
- Advertising transparency metrics: If ads are in scope, record advertiser and publisher metrics, fee data, measurement-tool access, authorization, and data-sharing consent boundaries.
- Fair and non-discriminatory access terms: Record access terms, termination conditions, exceptions, business-user communications, and dispute or support workflows.
- Anti-circumvention guardrails: Block designs that fragment service scope, make choices hard to exercise, degrade quality for users exercising rights, hide exports, or use UI design to subvert autonomy.
- Access control and least privilege: Verify field-level authorization for full delivery notes, business-user scopes, service-to-service lookup, Kafka publishing, Key Vault access, and observability writes.
- Evidence-safe logging: Exclude `delivery_instruction_note` from logs and Kafka payloads. Preserve correlation IDs, schema versions, order IDs, saga IDs, export job IDs, and audit event IDs.
- Monitoring, metrics, traces, and alerting: Add alerts for export failures, access-denied spikes, interoperability error rates, schema compatibility failures, ranking policy changes, consent ledger failures, and Kafka publish failures.
- Documentation and runbooks: Add runbooks for DMA evidence handoff, export support, interoperability request handling, ranking review, consent evidence, and release exceptions.
- Change control and release gates: Require pull request review, database migration tests, Kafka contract tests, privacy-safe logging tests, schema version compatibility tests, and qualified owner review for platform-scope changes.
- Compliance evidence handoff: Create a release evidence packet with source files reviewed, owner decisions, unresolved scope questions, API contracts, audit dashboards, and residual risk decisions.

Example business-user data access control:

```java
BusinessUserDataExport createExport(BusinessUserDataRequest request) {
    accessPolicy.requireBusinessUserScope(request.businessUserId(), request.requestedBy());
    dataCatalog.requireDocumentedLineage(request.datasetId(), "DMA business-user data access");
    if (request.includesPersonalData()) {
        consentLedger.requireOptIn(request.endUserId(), request.businessUserId(), ConsentPurpose.BUSINESS_USER_DATA_SHARING);
    }
    ExportJob job = exportJobs.scheduleContinuousExport(request.businessUserId(), request.datasetId(), request.format());
    auditLog.record(DmaDataAccessAuditEvent.created(request.businessUserId(), request.datasetId(), job.id()));
    return BusinessUserDataExport.accepted(job.id(), job.statusUrl());
}
```

Example ranking audit control:

```java
rankingAudit.record(new RankingAuditEvent(
    request.requestId(),
    context.rankingPolicyVersion(),
    results.ownServiceResultCount(),
    results.thirdPartyResultCount(),
    fairnessChecks.evaluate(results).status()
));
```

Example anti-circumvention release gate:

```java
ReleaseDecision evaluate(DmaEngineeringReview review) {
    var blockers = new ArrayList<String>();
    requirePresent(review.corePlatformServiceScope(), "core platform service scope evidence", blockers);
    requirePassing(review.interoperabilityContractTests(), "interoperability contract tests", blockers);
    requirePresent(review.dataAccessApiEvidence(), "business-user data access evidence", blockers);
    requirePassing(review.consentPreferenceAudit(), "consent and preference audit evidence", blockers);
    requirePassing(review.rankingFairnessReview(), "ranking and self-preferencing review", blockers);
    requirePresent(review.complianceHandoff(), "qualified owner handoff", blockers);
    return blockers.isEmpty()
        ? ReleaseDecision.approvedWithEvidence(review.serviceId(), review.evidenceReferences(), review.approvers())
        : ReleaseDecision.blocked(review.serviceId(), blockers, Escalation.required("legal", "compliance", "platform-governance", "product", "privacy", "security", "risk"));
}
```

## 6. Evidence Inventory

- Core platform service inventory: Not present.
- Business-user and end-user journey map: End-user shopper journey is present; business-user journey is not present.
- Active-user metric methodology: Not present.
- Interoperability interface contract: Not present for external interoperability.
- Data access API contract: Not present.
- Export workflow runbook: Not present.
- Consent and preference records: Not present.
- Ranking audit events: Not present.
- Advertising or search data evidence: Search service is described; search data access and advertising transparency evidence are not present.
- Access-control policy: API gateway authentication, managed identities, Key Vault, and private networking are described; DMA-specific business-user access policy is not present.
- Monitoring dashboards: Azure Monitor, App Insights, Log Analytics, dashboards, logs, traces, metrics, and alerts are described; dashboard IDs and DMA-specific alerts are not present.
- Alert routing: Not present.
- Compliance report or summary: Not present.
- Change approval: Pull request review and checks are described; explicit DMA review is not present.
- Release decision: Not present.

## 7. Residual Risks

- Residual risk: DMA applicability and core platform service scope are unknown.
- Impact: Controls may be over-applied, under-applied, or escalated too late if the ecommerce platform is part of a designated gatekeeper service.
- Likelihood: Unknown.
- Mitigation: Route gatekeeper designation, listed service scope, business-user role, end-user geography, and obligation applicability to legal, compliance, platform governance, product, and competition owners.
- Owner: Legal/compliance owner and platform governance owner.
- Acceptance decision: Required before relying on this report for production compliance decisions.
- Review date: Before production release if DMA scope is plausible.

- Residual risk: Delivery instructions may create data sharing, preference, and access-control evidence gaps.
- Impact: Over-broad event propagation, unclear preference handling, personal-data exposure, and weak business-user or support access controls.
- Likelihood: Medium, because the feature explicitly adds optional free text and changes Kafka events.
- Mitigation: Exclude notes from Kafka and logs, add field-level authorization, record preference handling, define service-to-service lookup controls, and review retention rules.
- Owner: CheckoutService owner, privacy owner, security owner, and data owner.
- Acceptance decision: Required before production release.
- Review date: During PR review and release readiness.

- Residual risk: Database migration and Kafka schema change may affect platform evidence and downstream services.
- Impact: Checkout disruption, incompatible consumers, incomplete observability, and weak release evidence for platform-scope controls.
- Likelihood: Medium.
- Mitigation: Require migration approval, Kafka schema compatibility tests, rollback or forward-fix plan, consumer readiness evidence, and post-deployment monitoring.
- Owner: Architecture owner, platform owner, CheckoutService owner, and downstream service owners.
- Acceptance decision: Required before production release.
- Review date: During PR review and release readiness.

## 8. Release Decision

- Decision: Conditionally blocked for production until platform-scope and privacy/security evidence is attached to the PR and release record.
- Conditions:
  - Qualified owners confirm whether the platform is in DMA gatekeeper or core platform service scope.
  - Business-user data access, export, ranking, and interoperability applicability are explicitly recorded.
  - Database migration tests prove nullable/default behavior and rollback or forward-fix readiness.
  - Kafka contract tests prove schema version 3 compatibility and older-consumer tolerance.
  - Privacy-safe logging tests prove `delivery_instruction_note` is not logged or emitted through Kafka.
  - Access-control tests prove full delivery notes are available only through authorised service-to-service lookup.
  - Monitoring covers schema failures, export/access failures if applicable, and delivery note lookup failures.
- Blockers:
  - No named legal/compliance, platform governance, product, privacy, security, or data owner.
  - No DMA scope decision or owner handoff.
  - No business-user data access, export, ranking, or interoperability evidence where those concerns may apply.
- Required approvals: Legal/compliance, platform governance, product, privacy, security, architecture, and service owner.
- Expiry or review date: Re-review when platform scope, search/ranking, advertising, business-user access, interoperability, or delivery-instruction data flows change.
- Environments approved: Development and test only until owner handoff is complete.
- Emergency rollback path: Previous validated image plus database forward-fix or rollback plan and Kafka schema compatibility checks.

## 9. Action Plan

| Priority | Action | Owner | Due date | Evidence expected | Status |
| -------- | ------ | ----- | -------- | ----------------- | ------ |
| High | Record DMA scope decision for gatekeeper, core platform service, business-user role, and affected obligations | Legal/compliance and platform governance | Before production release | Owner decision record linked to release | Open |
| High | Add privacy-safe logging, field-level authorization, migration, and Kafka contract tests for delivery instructions | CheckoutService and security owners | Before merge | Test results and PR evidence | Open |
| Medium | Define business-user data access, export, ranking, and interoperability applicability for the ecommerce platform | Product, data, architecture, and compliance owners | Before release readiness | Applicability matrix and evidence inventory | Open |
| Medium | Add observability for delivery note lookup, Kafka schema errors, export/access failures if applicable, and audit events | Platform and operations owners | Before production deployment | Dashboard IDs and alert rules | Open |
| Low | Create DMA evidence handoff runbook for future platform-scope changes | Compliance and platform owners | Next compliance planning cycle | Runbook and evidence repository path | Open |

## 10. Final Notes

- Items requiring legal interpretation: Gatekeeper designation, core platform service classification, obligation applicability, fair access terms, and regulatory interpretation.
- Items requiring platform governance decision: Whether checkout, search, identity, payment-support, marketplace, or cloud-hosted services are part of a listed core platform service.
- Items requiring product decision: Business-user journey, export expectations, preference UX, ranking policy review, and platform access terms.
- Items requiring privacy or consent review: Delivery instruction note, data sharing, data combination, opt-in requirements, preference history, and retention.
- Items requiring security exception: None approved by this report.
- Items requiring architecture decision: Kafka schema versioning, delivery-note lookup boundary, interoperability applicability, and data access API design.
- Items requiring competition or compliance review: Self-preferencing assessment, business-user access conditions, and DMA report evidence.
- Items requiring business acceptance: Release conditions and residual risk.
- Next review trigger: Any change to platform scope, business-user access, ranking, advertising, interoperability, consent, export, or delivery-instruction data flows.
