# EU Data Act Engineering Review Report

Use this report as engineering evidence for legal, compliance, privacy, data governance, security, product, procurement, cloud, provider, architecture, and business-owner review.

This report is not legal advice. It identifies engineering controls and escalation points from the reviewed system evidence. Applicability, data holder status, user entitlement, data recipient roles, trade-secret decisions, contract interpretation, cloud-switching obligations, international access restrictions, and regulatory interpretation require qualified owner review.

## 1. Review Context

- System or service name: CheckoutService delivery instructions change
- Repository, product, platform, or business service: Fictional Java Quarkus ecommerce platform
- Review date: 2026-06-14
- Reviewers: AI-assisted EU Data Act engineering review
- Business owner: Not identified in reviewed evidence
- Product owner: Not identified in reviewed evidence
- Technical owner: Software engineers, tech leads, reviewers, and platform engineers are described, but no named CheckoutService owner is identified
- Data owner: Not identified in reviewed evidence
- Privacy owner: Not identified in reviewed evidence
- Security owner: Not identified in reviewed evidence
- Cloud or provider owner: Not identified in reviewed evidence
- Legal/compliance owner: Not identified in reviewed evidence
- Source materials reviewed:
  - `examples/diagrams/deployment/system-example-cicd-pr-model.md`
  - `examples/diagrams/deployment/expected-system-deployment.puml`
  - `examples/diagrams/deployment/checkout-service-feature-request.md`
  - `.agents/skills/806-regulations-eu-data-act/references/806-regulations-eu-data-act-chapters-summary.md`
  - `.agents/skills/806-regulations-eu-data-act/references/806-regulations-eu-data-act-engineering-examples.md`
  - `.agents/skills/806-regulations-eu-data-act/assets/reports/806-eu-data-act-engineering-review-report-template.md`

## 2. Data Act Scope Context

- Service description: CheckoutService coordinates checkout saga steps for price validation, inventory reservation, payment authorization, order creation, delivery slot booking, PostgreSQL order state, saga state, and Kafka events.
- Possible connected-product or related-service signal: Not confirmed. The reviewed evidence describes ecommerce checkout and delivery data, not a connected product or related service.
- Possible data holder signal: The platform stores and emits checkout, order, delivery, and saga data. Whether the organization is a Data Act data holder requires legal and data-governance review.
- Possible user, data recipient, or third-party recipient signal: Shoppers, support teams, downstream inventory, delivery, notification, analytics, external payment, identity, and notification providers may receive or influence data flows. Entitlement and recipient roles are not documented.
- Possible data processing service or cloud-switching signal: Azure Kubernetes Service, Azure PostgreSQL, Kafka, Key Vault, Azure Monitor, App Insights, Log Analytics, Artifactory, Docker registry, and external SaaS providers create cloud and provider-dependency signals. Whether the platform provides a data processing service requires qualified review.
- Possible data-space, interoperability, or smart-contract signal: Kafka events and service APIs create interoperability signals. No data-space or smart-contract evidence is present.
- Deployment geography: Azure production environment is described; EU establishment, customer geography, and member-state scope are not specified.
- Environments in scope: Development, test, staging, and production.
- Critical users, customers, or affected organizations: Shoppers, support and operations teams, platform engineers, downstream inventory, delivery, notification, analytics, payment, identity, and cloud-provider workflows.
- Production dependencies: Azure Front Door, WAF, AKS ingress, Quarkus services, PostgreSQL stores, Kafka topics, Key Vault, Azure Monitor, App Insights, Log Analytics, Backup Vault, Artifactory, Docker registry, external identity, external payment, and notification provider.
- Open applicability questions:
  - Does the platform, product, or service fall within Data Act scope for any EU users, customers, connected-product data, related-service data, or data processing service obligations?
  - Who is the data holder, user, data recipient, third-party recipient, customer, cloud provider, and data owner for checkout and delivery instruction data?
  - Are delivery instructions personal data, non-personal data, mixed data, trade secrets, or commercially sensitive operational data in any workflow?
  - Which legal, compliance, privacy, data governance, security, product, procurement, cloud, provider, and business owners must approve access, sharing, export, and retention decisions?

## 3. Data Access And Portability Scope

- Applications, APIs, jobs, and batch workloads: Web storefront BFF, API gateway, identity, catalog, search, cart, pricing, CheckoutService, payment adapter, inventory, delivery slot, notification worker, analytics pipeline, checkout saga logic, and deployment workflows.
- Datasets, metadata, schemas, and catalog entries: Order records, delivery instruction note, delivery window, delivery contact phone, delivery access code, saga state, Kafka event schemas, order event metadata, and operational observability records. No data catalog or metadata owner is present.
- Data stores, queues, topics, object stores, and caches: User Profile DB, Product DB, Search Index, Cart DB, Pricing DB, Order DB, Saga Support PostgreSQL, Inventory DB, Delivery DB, Kafka topics `order.created`, `payment.authorized`, `stock.reserved`, `slot.booked`, `delivery.slot.requested`, `order.confirmed`, and compensation events.
- Product data, related service data, exportable data, and digital assets: Not classified in reviewed evidence. Checkout and delivery data may be exportable for user access, support, analytics, or provider switching if qualified owners decide Data Act scope applies.
- Personal data, non-personal data, mixed datasets, trade secrets, or commercially sensitive data: Delivery instruction fields may include personal or sensitive operational details. Order and delivery data may also include non-personal operational data. Classification is not documented.
- IAM, access-control rules, secrets, keys, and privileged operations: Azure Key Vault, managed identities, API gateway authentication enforcement, identity provider integration, payment tokens, idempotency keys, database credentials, Kafka credentials, and deployment credentials.
- Data-sharing request workflows and support operations: Not present. Support teams are mentioned, but no intake, entitlement, approval, refusal, suspension, deletion, or complaint workflow is documented.
- Export, portability, and interoperability mechanisms: Kafka event schema versioning and APIs exist; no user export API, machine-readable export format, metadata catalog, bulk download, or portability SLA is documented.
- Cloud-switching, provider-exit, and erasure workflows: Backup Vault and rollback concepts are described; provider exit, exportable data register, secure transfer, customer retrieval period, and erasure evidence are not present.
- Monitoring, alerting, audit logs, and evidence systems: Azure Monitor, App Insights, Log Analytics, structured logs, metrics, traces, correlation IDs, dashboards, alerts, health checks, and post-deployment smoke tests are described; data access audit-log requirements are not present.

## 4. Potential Violation Or Non-Compliance Mapping

This section is not a legal finding. It lists concrete potential EU Data Act violation or non-compliance signals from the reviewed evidence and routes each item to qualified owner review. No violation is confirmed by this engineering review.

| Potential violation or non-compliance signal | EU Data Act reference area | Associated official-source link | Evidence from reviewed system | Current status | Required owner review | Engineering action |
| -------------------------------------------- | -------------------------- | ------------------------------- | ----------------------------- | -------------- | --------------------- | ------------------ |
| Unclear Data Act applicability and role scope | Applicability / definitions / role classification | [Chapter I](https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32023R2854#cpt_I) | Ecommerce checkout, delivery, cloud-hosted Java services, Kafka events, and provider dependencies are described, but Data Act applicability, data holder status, user roles, and recipient roles are not recorded | Potential gap | Legal / compliance / data governance / product owner | Record Data Act applicability decision, role map, EU geography, dataset scope, and accountable owners |
| Missing data inventory and metadata evidence | User access / metadata / interoperability | [Chapter II](https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32023R2854#cpt_II), [Chapter VIII](https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32023R2854#cpt_VIII) | Order DB, Saga Support, Kafka topics, delivery instruction fields, and observability data are described, but no data inventory, metadata catalog, schema owner, or data-quality evidence is shown | Potential gap | Data governance / architecture / product owner | Create data inventory for checkout, delivery, saga, Kafka, support, analytics, and observability datasets with metadata, owner, format, retention, and access rules |
| Weak access authorization and request workflow | User access / third-party sharing | [Chapter II](https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32023R2854#cpt_II) | API gateway authentication is described, but no Data Act request intake, entitlement check, recipient verification, purpose capture, refusal, suspension, or deletion workflow is shown | Potential gap | Security / privacy / legal / data owner | Add data access request workflow with minimum necessary verification, authorization, purpose, recipient role, approval, refusal, suspension, deletion, and audit evidence |
| Missing machine-readable export and portability evidence | Portability / exportable data | [Chapter II](https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32023R2854#cpt_II), [Chapter VI](https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32023R2854#cpt_VI) | Kafka event schemas exist, but no user export API, bulk download, structured export format, export metadata, or cloud customer export register is documented | Potential gap | Product / data governance / architecture / cloud owner | Define export formats, API contracts, schemas, metadata, quality-of-service expectations, and test evidence for eligible checkout and delivery datasets |
| Incomplete trade-secret or sensitive-data handoff | Trade secrets / confidentiality / technical protection | [Chapter II](https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32023R2854#cpt_II), [Chapter III](https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32023R2854#cpt_III) | Delivery access codes, delivery notes, payment-adjacent data, provider information, and operational details may require confidentiality decisions, but no field classification or handoff is shown | Potential gap | Legal / product / data governance / security | Classify fields, identify trade-secret or sensitive-data candidates, define masking, access protocols, confidentiality measures, and owner decision records |
| Missing cloud-switching and provider-exit evidence | Switching between data processing services | [Chapter VI](https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32023R2854#cpt_VI) | Azure services, managed databases, Kafka, registry, observability, and SaaS providers are in scope, but no exportable data register, provider exit runbook, secure transfer plan, retrieval period, or erasure evidence is shown | Potential gap | Cloud owner / provider owner / procurement / legal | Add cloud-switching inventory, exportable data and digital asset register, provider exit runbook, secure transfer test, continuity plan, and erasure evidence |
| Weak non-personal data and international access safeguards | International access / non-personal data | [Chapter VII](https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32023R2854#cpt_VII) | EU data residency, non-personal data classification, and international governmental access handling are not documented | Potential gap | Legal / compliance / security / cloud owner | Record data residency, classify non-personal data, document international access request intake, legal review, minimum-data extraction, customer notification, and audit evidence |
| Database migration and Kafka message contract risks | Interoperability / metadata / release evidence | [Chapter VIII](https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32023R2854#cpt_VIII) | Feature adds order columns and changes Kafka event schema to version 3 for `delivery.slot.requested`, `order.created`, and `order.confirmed` | Potential gap | Architecture / data owner / platform owner | Require migration approval, schema compatibility tests, metadata updates, event contract documentation, rollback plan, and downstream consumer readiness evidence |

## 5. Engineering Controls

- Data inventory and owner map: Add a CheckoutService data inventory covering Order DB, Saga Support, Kafka topics, delivery instruction fields, support workflows, analytics consumers, observability data, data owners, product owners, privacy owners, security owners, and cloud/provider owners.
- Access authorization and entitlement checks: Define who can request, view, export, or share checkout and delivery instruction data. Include authentication, authorization, purpose capture, recipient role, least privilege, refusal, suspension, revocation, and deletion evidence.
- Data-sharing request workflow: Add intake, validation, approval, legal/privacy/data-governance handoff, response deadline, secure transfer, audit logging, support playbook, and complaint or dispute routing.
- User and recipient role evidence: Record whether shoppers, support users, downstream services, providers, or third-party recipients have access rights or contractual access paths.
- Purpose limitation and request minimization: Collect only the minimum verification data needed to execute and secure the request; avoid broad retention of request verification logs.
- Portability APIs: Define eligible datasets, request endpoints, bulk export, real-time access where technically feasible, API quality of service, throttling, retry behavior, and support workflow.
- Machine-readable export formats: Provide structured exports such as JSON or CSV with schema version, metadata, timestamp, dataset scope, and use restrictions.
- Metadata, schema, vocabulary, and catalog evidence: Publish schema entries for delivery instruction fields and Kafka event version 3, including field meaning, data classification, retention, owner, and compatibility rules.
- Interoperability and quality-of-service evidence: Link OpenAPI, AsyncAPI, schema registry, Kafka contract tests, consumer compatibility tests, and API performance expectations.
- Audit logs and evidence-safe logging: Record request ID, subject ID, recipient role, dataset, purpose, approval/refusal, export ID, schema version, and erasure confirmation without logging delivery notes, access codes, tokens, secrets, or payment details.
- Cloud-switching and provider-exit support: Add exportable data register, provider exit runbook, secure transfer plan, continuity controls, retrieval period, and erasure evidence for Azure services, PostgreSQL, Kafka, observability, registry, and SaaS providers when in scope.
- Non-personal data safeguards: Classify non-personal operational data, record EU residency where relevant, document international access intake, and route transfer requests to legal and cloud owners.
- Mixed personal and non-personal data privacy handoff: Coordinate delivery instruction fields with privacy review and GDPR controls before export, event propagation, support access, or analytics use.
- Trade-secret or commercially sensitive data handoff: Classify provider, operational, fraud, payment, logistics, and proprietary fields. Use masking, confidentiality agreements, access protocols, and qualified owner decisions.
- Contract and compensation evidence: Link data access terms, provider terms, customer terms, compensation assumptions where relevant, cloud switching terms, and non-binding model term review when available.
- Complaint, dispute, refusal, or suspension routing: Route disputes and complaints to legal, compliance, data governance, privacy, security, and product owners with evidence from the request workflow.
- Release gate and change-control evidence: Require pull request review, database migration test, Kafka contract test, privacy-safe logging test, data inventory update, export and metadata review, and owner approval before production deployment.

Example access authorization control:

```java
DataAccessDecision decide(DataAccessRequest request) {
    var subject = subjectVerifier.verifyMinimumNecessary(request.subject());
    var scope = catalog.resolve(request.datasetId());
    var entitlement = entitlementPolicy.evaluate(subject, scope, request.requestedPurpose());
    if (entitlement.requiresOwnerReview()) {
        return DataAccessDecision.holdForReview(
            request.id(),
            Escalation.required("legal", "data-governance", "privacy", "security"),
            entitlement.reason()
        );
    }
    auditLog.recordDecision(request.id(), subject.id(), scope.id(), entitlement.status());
    return entitlement.allowed()
        ? DataAccessDecision.approved(request.id(), scope.exportProfile())
        : DataAccessDecision.refused(request.id(), entitlement.reason());
}
```

Example export format control:

```java
DataExportPackage exportData(ApprovedDataAccess access) {
    var schema = schemaRegistry.current(access.exportProfile().schemaId());
    var export = exporter.create(
        access.datasetId(),
        access.exportProfile().format(),
        schema.version(),
        ExportMetadata.of(schema.uri(), access.exportProfile().qualityOfService())
    );
    auditLog.recordExportCreated(access.requestId(), export.id(), export.format(), schema.version());
    return export;
}
```

Example release gate control:

```java
ReleaseDecision evaluateDataActRelease(DataActEngineeringReview review) {
    var blockers = new ArrayList<String>();
    requirePresent(review.dataInventory(), "data inventory and owner map", blockers);
    requirePassing(review.accessAuthorizationTests(), "access authorization tests", blockers);
    requirePresent(review.exportFormatEvidence(), "machine-readable export format evidence", blockers);
    requirePresent(review.metadataCatalogEntry(), "metadata and schema evidence", blockers);
    requirePassing(review.auditLogVerification(), "data access audit-log verification", blockers);
    requirePresent(review.tradeSecretHandoff(), "trade-secret and sensitive-data handoff", blockers);
    requirePresent(review.cloudSwitchingPlan(), "cloud-switching or non-applicability evidence", blockers);
    return blockers.isEmpty()
        ? ReleaseDecision.approvedWithEvidence(review.serviceId(), review.evidenceReferences(), review.approvers())
        : ReleaseDecision.blocked(review.serviceId(), blockers, Escalation.required("legal", "data-governance", "privacy", "security", "cloud-owner"));
}
```

## 6. Evidence Inventory

- Data inventory: Not present as a dedicated inventory; runtime topology and feature request provide raw inputs.
- Metadata catalog: Not present.
- API contract: API gateway and service paths are described, but Data Act access API contract is not present.
- Export format specification: Not present.
- Schema or event contract: Kafka schema version 3 is referenced; concrete schema registry evidence is not present.
- Access-control tests: API authentication is described; field-level access and data export authorization tests are not present.
- Request workflow evidence: Not present.
- Audit-log evidence: Structured logging and correlation IDs are described; data access audit-log evidence is not present.
- Trade-secret review: Not present.
- Privacy or mixed-dataset review: Not present.
- Non-personal data classification: Not present.
- International access safeguard evidence: Not present.
- Cloud-switching runbook: Not present.
- Exportable data register: Not present.
- Erasure evidence: Not present.
- Contract or terms evidence: Not present.
- Complaint or dispute workflow: Not present.
- Release decision: Not present.

## 7. Residual Risks

- Residual risk: Delivery instruction fields may expose personal, sensitive, or operational details through logs, Kafka events, exports, support screens, analytics, or provider integrations if not classified and controlled.
- Impact: Unauthorized disclosure, over-broad sharing, incorrect export behavior, difficult erasure, and incomplete owner handoff.
- Likelihood: Medium, because the feature explicitly introduces free text, contact phone, access codes, and event changes.
- Mitigation: Classify fields, exclude sensitive notes from Kafka and logs, apply field-level authorization, add data inventory and export rules, and review retention and erasure.
- Owner: CheckoutService owner, data owner, privacy owner, security owner, product owner, and legal/compliance owner.
- Acceptance decision: Required before production release.
- Review date: Before merging and before production deployment.

- Residual risk: Database migration and Kafka schema version 3 may create incomplete metadata, incompatible consumers, or undocumented export semantics.
- Impact: Broken downstream consumers, incorrect data sharing, inconsistent exports, and weak interoperability evidence.
- Likelihood: Medium, because the feature changes persisted order structure and event contracts.
- Mitigation: Add migration tests, schema compatibility tests, metadata catalog entries, export format evidence, consumer readiness evidence, rollback or forward-fix plan, and post-deployment monitoring.
- Owner: Architecture owner, data owner, CheckoutService owner, platform owner, and downstream service owners.
- Acceptance decision: Required before production release.
- Review date: During PR review and release readiness.

- Residual risk: Data Act applicability, role mapping, contract interpretation, cloud-switching obligations, and international access safeguards are not determined by reviewed engineering evidence.
- Impact: Late compliance escalation, unapproved data sharing, unsupported access requests, or weak cloud exit evidence.
- Likelihood: Unknown.
- Mitigation: Route role classification, access rights, contract, trade-secret, provider-exit, and non-personal data questions to qualified owners.
- Owner: Legal/compliance, data governance, privacy, cloud, procurement, and business owners.
- Acceptance decision: Required if Data Act scope is confirmed.
- Review date: Before production reliance.

## 8. Release Decision

- Decision: Conditionally blocked for production until Data Act engineering evidence is attached to the PR and release record.
- Conditions:
  - Data inventory names owners for CheckoutService, Order DB, Saga Support, Kafka topics, delivery instruction fields, providers, and environments.
  - Legal/data-governance records whether Data Act scope, data holder role, user entitlement, recipient role, or cloud-switching obligations apply.
  - Database migration tests prove nullable/default behavior and rollback or forward-fix readiness.
  - Kafka contract tests prove schema version 3 compatibility and older-consumer tolerance.
  - Metadata catalog and export format evidence cover new delivery instruction fields.
  - Access-control and audit-log tests prove request and field-level controls.
  - Privacy-safe logging tests prove delivery notes and access codes are not logged or broadcast to Kafka.
  - Cloud-switching or non-applicability evidence is recorded for provider dependencies.
- Blockers:
  - Missing named product, data, privacy, security, legal/compliance, cloud, procurement, and business owners.
  - Missing data inventory, metadata catalog, request workflow, export format, audit-log evidence, trade-secret handoff, and cloud-switching evidence.
  - Missing release evidence for migration approval, Kafka compatibility, field classification, and privacy-safe logging.
- Required approvals: Service owner, tech lead, data owner, privacy owner, security owner, product owner, cloud owner, architecture owner, legal/compliance owner when applicable, and business owner.
- Expiry or review date: Before merge and again before production deployment.
- Environments approved: Development and test only until blockers are resolved.
- Emergency rollback path: Previous validated image plus compatible database state. Confirm whether the order table migration can be safely rolled forward or rolled back without losing delivery instruction data.
- Data access or export disablement path: Feature flag or configuration gate for delivery instruction export and third-party sharing until owner-approved controls are live.

## 9. Action Plan

| Priority | Action | Owner | Due date | Evidence expected | Status |
| -------- | ------ | ----- | -------- | ----------------- | ------ |
| High | Create CheckoutService Data Act data inventory with owners, datasets, metadata, Kafka topics, providers, classifications, access paths, retention, and export rules | Data owner / technical owner | Before PR approval | Inventory record linked to release | Open |
| High | Add owner handoff for Data Act applicability, data holder status, user entitlement, recipient roles, trade-secret boundaries, cloud-switching obligations, and contract interpretation | Legal / data governance / product owner | Before PR approval | Owner decision record | Open |
| High | Add field classification and privacy-safe handling for delivery note, contact phone, access code, and delivery window | Privacy owner / security owner / data owner | Before merge | Classification record and test evidence | Open |
| High | Add migration approval and tests for nullable/default order columns and rollback or forward-fix behavior | CheckoutService owner / data owner | Before merge | Migration test results and release note | Open |
| High | Add Kafka schema version 3 contract tests and metadata catalog updates | CheckoutService owner / downstream service owners | Before merge | Contract test report and metadata entry | Open |
| High | Add data access audit-log tests for request ID, subject, recipient role, purpose, dataset, export ID, refusal, suspension, deletion, and erasure confirmation | Security owner / platform owner | Before production deployment | Audit test report | Open |
| Medium | Define machine-readable export formats and API quality-of-service evidence for eligible checkout and delivery datasets | Product owner / architecture owner | Before production deployment | OpenAPI/export spec and test evidence | Open |
| Medium | Record cloud-switching or non-applicability evidence for Azure, PostgreSQL, Kafka, observability, registry, and SaaS provider data | Cloud owner / procurement / legal | Before production deployment | Exportable data register and exit runbook | Open |
| Medium | Add non-personal data and international access safeguard workflow for provider-held EU data | Legal / compliance / cloud owner | Before production deployment | Request intake and legal review workflow | Open |
| Low | Schedule next Data Act engineering review after deployment and first data access request exercise | Data governance / product owner | After production validation | Review record and corrective actions | Open |

## 10. Final Notes

- Items requiring legal interpretation: Data Act applicability, data holder status, user entitlement, data recipient roles, public-sector exceptional need, contract interpretation, cloud-switching obligations, international access restrictions, database-right questions, and regulatory interpretation.
- Items requiring data-governance decision: Dataset ownership, metadata catalog, export scope, field classifications, retention, deletion, and data-sharing request workflow.
- Items requiring privacy review: Delivery notes, contact phone, access code, mixed personal and non-personal data, support access, logs, analytics, and exports.
- Items requiring security exception: Any release without access-control evidence, audit logs, privacy-safe logging, secure transfer, erasure evidence, or trade-secret handoff.
- Items requiring architecture decision: Kafka schema versioning, older-consumer compatibility, export API shape, metadata strategy, database migration rollout, rollback limits, and cloud-switching runbook.
- Items requiring cloud or provider review: Azure services, external identity provider, external payment gateway, email/SMS provider, Artifactory, Docker registry, CI/CD actions, observability services, exportable data register, and provider exit.
- Items requiring procurement or contract review: Customer data access terms, provider terms, cloud switching terms, compensation assumptions where relevant, and standard contractual clause alignment when available.
- Items requiring product or business acceptance: Residual risk for delivery note handling, user access scope, third-party sharing, support workflow changes, and customer-facing rollout timing.
- Next review trigger: PR approval request, production deployment request, data access API change, export format change, schema or event contract change, provider change, public-sector request workflow, international access request, complaint, or data incident.
