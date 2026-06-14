# GDPR Engineering Review Report

This report is not legal advice. Use it as engineering evidence for legal, privacy, data protection officer, compliance, security, risk, architecture, data-governance, and business-owner review.

The review applies `803-regulations-gdpr` to the checkout delivery-instructions feature using only the source materials listed below. Questionnaire answers are constrained to those materials for acceptance-test purposes; in normal delivery, qualified owners should answer the questionnaire interactively before production approval.

## 1. Review Context

- **System, service, product, or platform:** Ecommerce CheckoutService delivery-instructions change
- **Repository or implementation path:** `java-cursor-rules` example architecture materials
- **Review date:** 2026-06-14
- **Reviewers:** GDPR engineering review using `803-regulations-gdpr`
- **Business owner:** Not identified in reviewed materials
- **Technical owner:** Tech leads and reviewers are described, but no named owner is identified
- **Privacy owner or data protection officer:** Not identified in reviewed materials
- **Legal/compliance owner:** Not identified in reviewed materials
- **Security/risk owner:** Not identified in reviewed materials
- **Data governance owner:** Not identified in reviewed materials
- **Source materials reviewed:**
  - `examples/diagrams/deployment/system-example-cicd-pr-model.md`
  - `examples/diagrams/deployment/expected-system-deployment.puml`
  - `examples/diagrams/deployment/checkout-service-feature-request.md`
  - `.agents/skills/803-regulations-gdpr/references/803-regulations-gdpr.md`
  - `.agents/skills/803-regulations-gdpr/assets/questions/803-gdpr-engineering-review-questionnaire.md`
  - `.agents/skills/803-regulations-gdpr/assets/reports/803-gdpr-engineering-review-report-template.md`

## 2. Personal-Data Processing Summary

- **Processing type:** Customer checkout, order, delivery, payment-adjacent, notification, operational telemetry, and service-delivery processing.
- **Data subjects:** Shoppers are directly in scope. Internal engineers, reviewers, platform engineers, support, and operations users may appear in development, audit, and operational evidence.
- **Personal-data categories:** Customer/order identifiers, delivery slot identifiers, delivery preferences, delivery drop-off location, access-code requirement flag, contactless-delivery flag, free-text delivery instruction note, cart/order/payment workflow references, tenant or market identifiers, correlation and causation identifiers, notification channels, logs, metrics, traces, and support diagnostics.
- **Special-category or high-sensitivity data:** No special-category, criminal offence, children's, or vulnerable-person data is explicitly described. The free-text delivery note is high-risk personal data because shoppers can enter sensitive content. Payment workflow references and authentication/security telemetry are also high-sensitivity engineering concerns.
- **Processing purposes:** Checkout confirmation, delivery-slot booking, order creation, notification, order-status exposure, inventory coordination, payment authorization flow coordination, support diagnostics, audit, security, and operational monitoring.
- **Primary users:** Shoppers, CheckoutService, delivery service, order service, notification service, analytics pipeline, support/operations, software engineers, tech leads, reviewers, and platform engineers.
- **Affected people or groups:** Shoppers are affected by checkout, delivery, notification, support, analytics, and order-status processing. Internal users may be affected by audit and operational telemetry.
- **Environments in scope:** Development, test, staging, and production Azure environments.
- **Data stores and derived stores:** `orders` table in Order DB, Saga Support store, Kafka topics `delivery.slot.requested`, `order.created`, and `order.confirmed`, downstream delivery/order/notification/analytics stores, PostgreSQL service databases, search index where order status is exposed, logs, metrics, traces, Azure Monitor, App Insights, Log Analytics, backups, Artifactory, Docker registry, SBOM/provenance, and deployment evidence.
- **Vendors, processors, or subprocessors:** Azure platform services, external identity provider, external payment gateway, email/SMS provider, Artifactory, Docker/container registry, and observability tooling.
- **Data transfer context:** Azure runtime and external providers are described, but regions, controller/processor roles, subprocessors, and transfer mechanisms are not documented.
- **Rights workflows in scope:** Access, rectification, erasure, restriction, objection, portability, and consent/preference update should be assessed for order and delivery-instruction data across databases, Kafka topics, logs, analytics, notifications, search, backups, and providers.

## 3. Questionnaire Findings

| Question | Answer based only on reviewed source material | Gap or note |
| -------- | -------------------------------------------- | ----------- |
| 1. Personal-data processing type | User account/customer profile, payment/checkout, support/communication, telemetry/monitoring tied to a person, analytics, and service delivery processing. | No formal processing inventory is present. |
| 2. Data subjects | Shoppers; internal engineers, platform engineers, tech leads, reviewers, support, and operations users may appear in operational evidence. | Data subject scope is not formally documented. |
| 3. Personal data categories | Direct identifiers/profile links, authentication/session metadata, order/cart/delivery records, delivery-instruction fields, free-text delivery note, payment authorization references, notification contact channels, telemetry, and operational identifiers. | Exact DTOs, schemas, log fields, and downstream payloads are not documented. |
| 4. Processing purposes | Account access, checkout, payment, order, delivery, notifications, analytics, audit, security, support, and operations. | Purpose-to-field mapping is missing for each delivery-instruction field. |
| 5. Data locations | Order DB, Saga Support, Kafka topics, downstream service stores, search index, logs, metrics, traces, App Insights, Log Analytics, backups, external providers, Artifactory/registry/deployment metadata. | Caches, exports, data lake, support tools, and retention locations are not fully specified. |
| 6. Owner review | Platform, support, operations, tech leads, and reviewers are described as roles. | Named product, technical, privacy, legal, security, compliance, risk, data-governance, vendor, and procurement approvals are missing. |
| 7. Lawful or governance basis | Unknown. | Escalate lawful basis and purpose limitation to legal, privacy, DPO, compliance, and business owners. |
| 8. Special-category or high-sensitivity data | No special-category, criminal offence, children's, or vulnerable-person data is identified. Free-text delivery notes can contain sensitive personal data. Payment, authentication, and security telemetry are sensitive engineering data. | Validate whether notes can include special-category data, access codes, addresses, or vulnerable-person information. |
| 9. Profiling, automated decision, or rights impact | Checkout validates prices, promotions, inventory, delivery constraints, and payment authorization. Analytics updates dashboards and forecasts. | Profiling, pricing, fraud, or eligibility impact is not documented; escalate if automated decisions affect people. |
| 10. Controller, processor, or subprocessor roles | Unknown. | Roles for the ecommerce operator, Azure, payment gateway, identity provider, notification provider, and observability tools are not documented. |
| 11. Transfers or vendor processing | Azure, external identity, external payment gateway, email/SMS provider, Artifactory, Docker/container registry, and observability tooling are involved. | Regions, subprocessors, data processing agreements, and transfer mechanisms are not documented. |
| 12. DPIA, privacy review, or security review | PR review, CI tests, SAST, dependency scanning, secret scanning, image scanning, SBOM, and provenance are described. | DPIA, privacy review, and data protection review are not documented. |
| 13. Minimization controls | Limit database fields, purpose-specific DTOs, avoid emitting free-text notes to Kafka, publish `notePresent`, and use service-to-service lookup with field-level authorization if full note access is needed. | Implementation code and contracts are not available for verification. |
| 14. Access-control controls | Field-level authorization, purpose-specific scopes, tenant/market isolation, least privilege, admin access review, audit trail, and non-production masking are required. | Fine-grained access policy and audit evidence are not documented. |
| 15. Data-subject rights workflows | Access, rectification, erasure, restriction, objection, portability, and consent/preference update are potentially required for order and delivery-instruction data. | Rights orchestration and deletion propagation are not documented. |
| 16. Retention and deletion controls | Retention periods, scheduled deletion, legal hold handling, tombstones, cache/index removal, Kafka/event retention, backup policy, export expiration, and downstream deletion propagation are required. | Retention schedule and deletion evidence are missing. |
| 17. Privacy-safe observability controls | Redacted payloads, hashed subject identifiers, no free-text note logging, limited log retention, secure log access, and breach evidence without excessive personal data are required. | Current logging, metrics, and trace field policy is not documented. |
| 18. Security-of-processing controls | HTTPS, WAF, private networking, Key Vault, managed identities, scans, immutable images, SBOM/provenance, and observability are described. | Field encryption, pseudonymization, audit policy, access reviews, and data masking are not fully documented. |
| 19. Privacy evidence | Data-flow and deployment diagrams exist. CI/CD and security evidence are described. The feature request documents minimization rules for Kafka. | Personal-data inventory, lawful-basis handoff, DPIA/privacy review, retention, rights, transfer, and breach evidence are missing. |
| 20. Breach-response evidence | Logs, metrics, traces, alerts, Azure Monitor, App Insights, and Log Analytics are described. | Containment runbook, privacy/security escalation, affected data identification, notification handoff, and corrective-action workflow are missing. |
| 21. Current release decision | Unknown from GDPR perspective. PR review and automated production deployment after checks are described. | Production release should be conditional until owner review and privacy evidence are complete. |
| 22. Next steps | Create personal-data inventory, minimize DTOs/events/logs, add field-level authorization, rights workflow, retention/deletion controls, transfer/vendor/DPIA evidence, privacy-safe logging, breach evidence, and release-readiness approvals. | Required before relying on the feature for production personal-data processing. |

Material unanswered questions:

- Which exact fields are accepted at the API boundary, persisted in `orders`, emitted to Kafka, logged, indexed, exported, backed up, and sent to vendors?
- Which lawful basis, purposes, retention periods, controller/processor roles, and data-governance owners apply to delivery-instruction data?
- Which services may retrieve the full free-text note, under which purpose, authorization, audit, and retention policy?
- How are rights requests and deletion propagated to Order DB, Saga Support, Kafka topics, delivery/notification/analytics stores, logs, indexes, backups, and providers?
- What DPIA, privacy review, security review, breach-response, and release-readiness evidence exists before production rollout?

## 4. GDPR Privacy Risk Classification

- **Personal-data processing signals:** Strong. The feature adds shopper-provided delivery-instruction data to checkout persistence and Kafka message contracts.
- **Special-category, criminal offence, children's, or vulnerable-person data signals:** Not identified in structured fields, but free-text notes can contain sensitive personal data, access details, health or vulnerability information, or household information.
- **Profiling, automated decision, or rights-impacting signals:** Possible. Delivery-slot booking, checkout validation, analytics, and order-status exposure can affect shoppers if rules or decisions are tied to individuals.
- **Controller, processor, subprocessor, or joint-controller concerns:** Unknown. The ecommerce operator and external identity, payment, notification, cloud, artifact, registry, and observability providers require role documentation.
- **Third-country transfer or vendor-processing concerns:** Unknown. External providers and Azure services are in scope, but regions, subprocessors, and transfer mechanisms are not documented.
- **DPIA or privacy-review concerns:** Privacy review should be required because the change introduces free-text personal data, database migration, Kafka contract changes, downstream processing, and production delivery through CI/CD.
- **Security-of-processing concerns:** Existing controls include HTTPS, WAF, private networking, Key Vault, managed identities, CI scans, image scans, SBOM/provenance, immutable artifacts, and rollback. Missing evidence includes field-level authorization, redaction, pseudonymization, access review, data masking, and privacy tests.
- **Breach-response concerns:** Observability exists, but breach evidence, affected data category mapping, escalation path, containment runbook, and legal/privacy notification handoff are not described.
- **Classification conclusion for governance review:** Classify as personal-data processing with free-text, delivery, database migration, Kafka message, downstream processing, vendor, transfer, retention, deletion, and breach-readiness signals. Do not treat the feature as GDPR-ready without qualified owner review.
- **Required escalation:** Legal/privacy/DPO for lawful basis, role, transfer, DPIA, rights, and retention interpretation; security/risk for access, encryption, logging, incident, and breach evidence; data governance for inventory, minimization, lineage, and deletion; architecture/platform for database migration and Kafka compatibility; product/business for purpose and feature acceptance; procurement/vendor owners for third-party processing.

## 5. Potential Violation Or Non-Compliance Mapping

No confirmed GDPR violation is identified from the reviewed source material. The items below are potential non-compliance signals or evidence gaps that require qualified review before treating the checkout delivery-instructions feature as production-ready personal-data processing.

| Potential violation or non-compliance signal | GDPR reference | Evidence from reviewed system | Current status | Required owner review | Engineering action |
| -------------------------------------------- | -------------- | ----------------------------- | -------------- | --------------------- | ------------------ |
| Missing lawful-basis, purpose-limitation, minimization, or transparency evidence | Articles 5-6 and Articles 12-14 | The feature adds delivery-instruction fields and a free-text note, but lawful basis, purpose-to-field mapping, transparency notices, and field owners are not documented. | Potential gap | Legal / privacy / DPO / business owner | Document lawful-basis handoff, purposes, transparency obligations, and field-level minimization decisions for every delivery-instruction field. |
| Missing personal-data records, controller/processor, vendor, or role evidence | Articles 28 and 30 | Azure, identity, payment, notification, artifact, registry, and observability providers are described, but controller/processor roles, subprocessors, DPAs, regions, and records of processing are not documented. | Potential gap | Legal / privacy / procurement / data governance | Create records of processing and provider role evidence, including processors, subprocessors, regions, retention, and vendor contacts. |
| Missing privacy by design/default or field-level access controls | Article 25 | The feature requests controlled values, length limits, Kafka minimization, and service-to-service lookup for the full note, but implementation, field-level authorization, and default access policy are not available. | Potential gap | Privacy / security / architecture / product | Add purpose-specific DTOs, default-deny note access, field-level authorization, and audit evidence for full-note lookups. |
| Missing data-subject rights, retention, or deletion propagation evidence | Articles 15-22 | Rights workflows, retention periods, deletion jobs, tombstones, Kafka/event retention, cache/index removal, backup handling, and downstream propagation are not documented. | Potential gap | Privacy / DPO / product / platform | Implement rights orchestration and retention/deletion propagation across Order DB, Saga Support, Kafka, logs, analytics, search, backups, and vendors. |
| Missing security-of-processing or privacy-safe observability evidence | Article 32 | HTTPS, WAF, private networking, Key Vault, managed identities, scans, SBOM, provenance, and rollback exist, but field encryption, pseudonymization, masking, access reviews, redaction, and privacy-safe logging evidence are missing. | Potential gap | Security / privacy / SRE / risk | Add privacy-safe logging tests, redaction controls, hashed subject identifiers, least-privilege access, non-production masking, and access review evidence. |
| Missing breach-response, affected-data, or notification-handoff evidence | Articles 33-34 | Observability exists, but affected personal-data category mapping, containment runbook, privacy/security escalation, notification handoff, and corrective-action workflow are not described. | Potential gap | Security / privacy / DPO / legal | Add breach runbook, affected-data mapping, evidence capture, notification decision handoff, and corrective-action tracking. |
| Missing DPIA, transfer, or vendor-processing evidence | Article 35 and Chapter V / Articles 44-49 | The change introduces free-text personal data, downstream processing, vendors, Azure runtime, and external providers, but DPIA decision, transfer mechanisms, regions, and vendor reviews are missing. | Potential gap | Legal / privacy / DPO / procurement | Complete DPIA/privacy-review decision and transfer/vendor evidence before production personal-data processing. |

## 6. Engineering Controls

- **Data inventory and data-flow evidence:** Add `deliveryInstructions` fields to the personal-data inventory with categories, source, purpose, owner, store, event usage, retention, deletion path, and downstream consumers.
- **Data minimization and DTO design:** Keep structured fields narrow. Use controlled values for `dropOffLocation`, booleans for `accessCodeRequired` and `contactlessDelivery`, length-limit `note`, and avoid broad order entity exposure.
- **Purpose-specific processing controls:** Map each delivery-instruction field to checkout, delivery booking, notification, support, or order-status purpose. Block fields with no documented purpose and owner.
- **Field-level authorization and least privilege:** Require explicit purpose and service authorization before reading the free-text note. Prefer `notePresent` in Kafka and a service-to-service lookup for full note access.
- **Pseudonymization, tokenization, redaction, or masking:** Hash or tokenize subject identifiers in logs and traces, mask non-production data, and redact delivery note content from logs, metrics, traces, and error reports.
- **Privacy-safe logging and observability:** Add tests proving the free-text note is not logged or emitted into Kafka payloads. Log only correlation ID, order ID, action, outcome, trace ID, and redacted or hashed subject references.
- **Retention and deletion controls:** Define retention for structured delivery fields, free-text notes, Kafka event metadata, logs, traces, analytics, order-status projections, notifications, and backups. Add deletion propagation and tombstone evidence.
- **Cache, index, export, and backup handling:** Treat order-status search, analytics, support exports, Kafka topics, observability stores, and backups as derived stores requiring retention and deletion handling.
- **Data-subject rights workflows:** Ensure access, rectification, erasure, restriction, objection, portability, and preference workflows can locate delivery-instruction data across primary and derived stores.
- **Transfer or vendor-review evidence:** Document whether delivery-instruction data reaches external delivery, notification, payment, identity, observability, cloud, support, analytics, or AI/model providers.
- **Security-of-processing controls:** Preserve pipeline security controls and add field-level authorization, migration review, schema compatibility checks, non-production masking, audit trails, and least-privilege service accounts.
- **Breach detection, containment, and evidence:** Define affected data categories, evidence capture, containment steps, privacy/security escalation, notification handoff, and corrective-action workflow for delivery-instruction exposure.
- **Testing and validation:** Add database migration tests, API validation tests, order aggregate mapping tests, Kafka schema version `3` contract tests, old-consumer compatibility tests, field-level authorization tests, privacy-safe logging tests, retention/deletion tests, and rights-workflow tests.

Relevant Java engineering examples:

```java
public record DeliveryInstructionsRequest(
    DropOffLocation dropOffLocation,
    boolean accessCodeRequired,
    boolean contactlessDelivery,
    String note
) {
}
```

Use a purpose-specific request DTO instead of accepting a broad order entity or arbitrary map.

```java
public record DeliveryInstructionsEvent(
    DropOffLocation dropOffLocation,
    boolean accessCodeRequired,
    boolean contactlessDelivery,
    boolean notePresent
) {
}
```

Publish non-free-text delivery context in Kafka and keep full note access behind authorized service lookup.

```java
auditLogger.info(
    "delivery_instructions_updated orderId={} subjectHash={} notePresent={} traceId={}",
    orderId.value(),
    subjectHasher.hash(customerId.value()),
    instructions.notePresent(),
    traceId
);
```

Preserve breach and audit value without logging raw delivery-note text.

## 7. Evidence Inventory

| Artifact | Current evidence from source material | Gap |
| -------- | ------------------------------------- | --- |
| Personal-data inventory | Services, databases, Kafka topics, delivery-instruction fields, external providers, logs, traces, metrics, and backups are described. | No field-level inventory with categories, purposes, owners, processors, retention, and deletion paths. |
| Data-flow diagram | `expected-system-deployment.puml` shows Azure edge, AKS, data services, external providers, and delivery pipeline. | No privacy-specific data-flow annotations, data categories, or transfer regions. |
| Purpose and lawful-basis handoff | Business and technical purposes are described for checkout and delivery handoff. | Lawful basis, purpose owner, and legal/privacy handoff are not documented. |
| DPIA or privacy review | Not described. | Required owner decision is missing for free-text personal-data processing. |
| Security review | WAF, private networking, Key Vault, managed identities, scans, SBOM, provenance, PR checks, and rollback are described. | Field-level authorization, pseudonymization, privacy-safe logging, access review, and masking evidence are missing. |
| Access-control evidence | API gateway enforces authentication; managed identities protect secrets access. | Fine-grained note access, admin access review, audit trail, and least-privilege evidence are missing. |
| Data-subject rights workflow | Identity service manages user consent. | Access, rectification, erasure, restriction, objection, portability, delivery-note update/delete, and provider coordination are not documented. |
| Retention policy | Feature says future retention and privacy rules must treat the note as personal data. | Concrete retention periods and legal hold exceptions are missing. |
| Deletion job evidence | Not described. | Deletion jobs, tombstones, and completion evidence are missing. |
| Deletion propagation evidence | Kafka events and downstream services are described. | Propagation to downstream stores, topics, analytics, logs, search, notifications, providers, and backups is not documented. |
| Transfer or vendor review | External identity, payment, notification, Azure, artifact, registry, and observability dependencies are described. | Provider roles, regions, subprocessors, contracts, data processing agreements, and transfer mechanisms are missing. |
| Breach response runbook | Logs, metrics, traces, dashboards, alerts, Azure Monitor, App Insights, and Log Analytics are described. | Containment, evidence capture, affected data classification, privacy/security escalation, notification handoff, and corrective action are missing. |
| Monitoring dashboard or alert evidence | Operations use dashboards, logs, traces, and alerts. | Dashboard IDs, alert rules, data minimization, access control, and retention are not documented. |
| Approval records | Pull request reviews and checks are described. | Privacy, legal, DPO, security, data-governance, and residual-risk approvals are missing. |

## 8. Residual Risks

| Residual risk | Impact | Likelihood | Mitigation | Owner | Acceptance decision |
| ------------- | ------ | ---------- | ---------- | ----- | ------------------- |
| Delivery-note free text may contain excessive or sensitive personal data | Increased privacy exposure and breach impact | High | Limit length, validate content, redact logs, exclude from Kafka, restrict lookup, and train support/ops workflows | Product / privacy / engineering | Must be remediated before production privacy approval |
| Personal-data inventory is incomplete | Unknown collection, exposure, retention, or deletion obligations | High | Create field-level inventory and data-flow map | Data governance / architecture | Must be remediated before production privacy approval |
| Lawful basis, role, DPIA, and transfer decisions are missing | Incorrect governance posture or blocked release | High | Legal/privacy/DPO review and documented decisions | Legal / privacy / DPO | Requires qualified review |
| Rights workflows do not cover derived stores | Incomplete access, correction, erasure, or portability fulfillment | Medium | Implement rights orchestration across databases, Kafka, logs, search, analytics, backups, and vendors | Product / platform | Requires remediation |
| Logs, traces, or analytics may contain delivery notes | Privacy exposure, breach impact, and unnecessary retention | Medium | Redaction, hashing, payload suppression, retention limits, secure access, and tests | Security / SRE | Requires control evidence |
| Kafka schema rollout may expose or retain unnecessary personal data | Broad consumer access and difficult deletion | Medium | Publish only structured non-free-text fields, keep compatibility tests, and review topic retention | Architecture / platform | Requires remediation |
| Vendor and transfer evidence is missing | Unreviewed provider processing or third-country transfer risk | Medium | Provider register, DPA/subprocessor review, region mapping, and transfer assessment | Procurement / legal | Requires governance acceptance |
| Breach response lacks privacy handoff | Slow or incomplete incident investigation and notification support | Medium | Incident runbook, affected data mapping, escalation path, and evidence retention | Security / privacy | Requires remediation |

## 9. Release Decision

- **Decision:** Approved for development and PR-based validation only. Not approved as GDPR-ready for production personal-data processing until privacy, legal, security, data-governance, and business-owner evidence is complete.
- **Conditions:** Complete personal-data inventory, lawful-basis handoff, controller/processor role review, DPIA/privacy review decision, vendor and transfer evidence, retention/deletion controls, rights workflows, field-level authorization, privacy-safe observability, database migration review, Kafka schema compatibility evidence, breach evidence, and owner approvals.
- **Blockers:** Missing field-level inventory, missing lawful-basis handoff, missing role documentation, missing privacy review/DPIA decision, missing retention/deletion policy, missing rights workflow, missing transfer/vendor evidence, missing privacy-safe logging evidence, missing breach-response handoff, and missing explicit note-access policy.
- **Required approvals:** Legal, privacy/DPO, compliance, security/risk, data governance, procurement/vendor owner, architecture/platform, product/business owner, and technical owner.
- **Expiry or review date:** Review within 90 days or earlier if personal-data fields, providers, regions, analytics, AI features, logs, exports, backups, rights workflows, or checkout/delivery/payment workflows materially change.
- **Environments approved:** Development, test, and staging with synthetic or minimized data only until missing privacy evidence is complete.
- **Data categories approved:** No approval for production free-text delivery-note processing is granted by this report.
- **Vendor or transfer constraints:** Do not send delivery-instruction personal data to external identity, payment, notification, observability, cloud, artifact, registry, analytics, support, or AI providers without documented provider, role, region, retention, security, and transfer review.

## 10. Action Plan

| Priority | Action | Owner | Due date | Evidence expected | Status |
| -------- | ------ | ----- | -------- | ----------------- | ------ |
| High | Create a GDPR personal-data inventory for delivery-instruction fields across CheckoutService, Order DB, Saga Support, Kafka topics, downstream services, logs, traces, analytics, backups, and providers. | Data governance / architecture | 2026-06-28 | Approved inventory with fields, categories, purposes, owners, stores, retention, and deletion paths | Open |
| High | Document lawful-basis handoff, controller/processor roles, DPIA/privacy review decision, and required owner approvals for delivery-instruction processing. | Legal / privacy / DPO / compliance | 2026-06-28 | Governance decision record and escalation outcomes | Open |
| High | Implement field-level authorization for full delivery-note lookup and exclude raw note text from Kafka events, logs, metrics, traces, and error reports. | Engineering / security | 2026-07-05 | Code, tests, access policy, audit evidence, and redaction rules | Open |
| High | Define retention, deletion, tombstone, Kafka/event retention, backup handling, and downstream deletion-propagation controls for delivery-instruction data. | Platform / data owners | 2026-07-05 | Retention schedule, deletion workflow, and evidence records | Open |
| High | Add database migration review and Kafka schema version `3` compatibility evidence for old and new consumers. | Architecture / platform | 2026-07-05 | Migration tests, contract tests, rollout plan, and rollback notes | Open |
| Medium | Add rights-workflow orchestration for access, rectification, erasure, restriction, objection, and portability across primary and derived stores. | Product / engineering | 2026-07-12 | Rights workflow design, authorization rules, audit evidence, and tests | Open |
| Medium | Review vendors, subprocessors, regions, transfer mechanisms, DPAs, retention, deletion, and incident notification paths. | Procurement / legal / security | 2026-07-12 | Vendor register and transfer-review evidence | Open |
| Medium | Add breach-response runbook with affected data mapping, containment, escalation, notification handoff, and corrective-action workflow. | Security / privacy / SRE | 2026-07-19 | Runbook, drill result, and evidence-retention policy | Open |
| Low | Annotate the deployment diagram with personal-data categories, owners, processors, regions, retention, and deletion propagation after governance review. | Architecture | 2026-08-02 | Updated diagram or ADR linked to inventory | Open |

## 11. Final Notes

- **Items requiring legal interpretation:** Lawful basis, controller/processor roles, jurisdiction, transfer mechanism, DPIA requirement, data-subject rights interpretation, retention obligations, and breach notification requirements.
- **Items requiring privacy or data protection officer decision:** Personal-data inventory approval, DPIA/privacy review outcome, free-text delivery-note handling, special-category confirmation, rights workflow adequacy, minimization, and residual-risk acceptance.
- **Items requiring security exception:** Any logging of raw delivery notes, broad production log access, weak encryption, unmasked non-production data, unreviewed payment/authentication data handling, or missing breach evidence.
- **Items requiring data governance decision:** Field ownership, purpose mapping, retention schedule, deletion propagation, backup handling, exports, analytics, Kafka retention, and data lineage.
- **Items requiring vendor or transfer review:** Azure, external identity provider, payment gateway, email/SMS provider, Artifactory, Docker/container registry, observability tooling, analytics, support, and any future AI/model provider.
- **Items requiring architecture decision:** Rights orchestration pattern, deletion/tombstone propagation, privacy event schema, field-level note lookup, Kafka schema compatibility, and where privacy evidence is stored.
- **Items requiring product or business acceptance:** Controlled drop-off values, note length limit, exclusion of note text from events/logs, field-level access restrictions, retention limits, release conditions, vendor constraints, and residual risk from delayed privacy controls.
- **Next review trigger:** New personal-data category, expanded note usage, new downstream consumer, new provider or region, production data use in non-production, analytics/export expansion, AI or RAG integration, failed deletion, breach event, or material checkout/payment/delivery workflow change.
