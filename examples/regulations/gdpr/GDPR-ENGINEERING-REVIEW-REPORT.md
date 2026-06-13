# GDPR Engineering Review Report

This report is not legal advice. Use it as engineering evidence for legal, privacy, data protection officer, compliance, security, risk, architecture, data-governance, and business-owner review.

The purpose of this report is to increase awareness of potential privacy engineering gaps and create evidence for qualified review. The response produced from this template does not represent legal advice, a legal opinion, or a final regulatory determination.

## 1. Review Context

- **System, service, product, or platform:** Ecommerce deployment example
- **Repository or implementation path:** `java-cursor-rules` example architecture materials
- **Review date:** 2026-06-14
- **Reviewers:** GDPR engineering review using `803-regulations-gdpr`
- **Business owner:** Not identified in reviewed materials
- **Technical owner:** Not identified in reviewed materials
- **Privacy owner or data protection officer:** Not identified in reviewed materials
- **Legal/compliance owner:** Not identified in reviewed materials
- **Security/risk owner:** Not identified in reviewed materials
- **Data governance owner:** Not identified in reviewed materials
- **Source materials reviewed:**
  - `examples/diagrams/deployment/system-example.md`
  - `examples/diagrams/deployment/expected-system-deployment.puml`
  - `.agents/skills/803-regulations-gdpr/references/803-regulations-gdpr.md`
  - `.agents/skills/803-regulations-gdpr/assets/questions/803-gdpr-engineering-review-questionnaire.md`
  - `.agents/skills/803-regulations-gdpr/assets/reports/803-gdpr-engineering-review-report-template.md`

## 2. Personal-Data Processing Summary

- **Processing type:** Customer account, profile, cart, checkout, payment authorization, order, delivery booking, notification, support/operations diagnostics, and telemetry processing.
- **Data subjects:** Shoppers are directly in scope. Software engineers, platform engineers, support operators, and internal reviewers may also appear in delivery, support, audit, and operational records.
- **Personal-data categories:** Authentication and session metadata, profile links, customer or account identifiers, cart contents, order identifiers, delivery address or time-slot records, payment intent/provider token references, notification contact channels, support and operational diagnostics tied to order or customer activity, tenant or market identifiers, IP/security telemetry where collected.
- **Special-category or high-sensitivity data:** No special-category, criminal offence, children's, or vulnerable-person data is explicitly described. Payment-related records and authentication/security telemetry are high-sensitivity engineering concerns and require security/privacy review.
- **Processing purposes:** Account authentication, service delivery, cart and checkout processing, payment authorization, order confirmation, inventory reservation, delivery booking, transactional notifications, analytics, audit, security, observability, and incident diagnosis.
- **Primary users:** Shoppers, support and operations teams, software engineers, tech leads and reviewers, and platform engineers.
- **Affected people or groups:** Shoppers are affected by account, checkout, payment, delivery, notification, analytics, and support workflows. Internal staff may be affected by engineering and operational telemetry.
- **Environments in scope:** Development, test, staging, and production Azure environments.
- **Data stores and derived stores:** User Profile DB, Cart DB, Order DB, Delivery DB, Saga Support, PostgreSQL service stores, PostgreSQL full-text search index, Kafka topics, Azure Monitor, App Insights, Log Analytics, backups, Docker/artifact metadata, and deployment evidence.
- **Vendors, processors, or subprocessors:** Azure platform services, external identity provider, external payment gateway, email/SMS provider, Artifactory, Docker/container registry, and observability tooling.
- **Data transfer context:** Azure runtime and external providers are described, but region, controller/processor roles, subprocessors, and transfer mechanisms are not documented.
- **Rights workflows in scope:** Access, rectification, erasure, restriction, objection, portability, and consent/preference update should be assessed for customer profile, order, cart, delivery, notification, analytics, logs, caches, search indexes, exports, backups, and provider-held records.

## 3. Questionnaire Findings

Acceptance-test note: the feature scenario requires questionnaire answers to be based only on information present in `examples/diagrams/deployment/system-example.md`. Unknown items below are intentionally not filled from assumptions outside that source. In normal GDPR skill usage, the questionnaire must be asked one question at a time before implementation review; this report records the constrained evidence outcome for the acceptance scenario.

| Question | Answer based only on reviewed source material | Gap or note |
| -------- | -------------------------------------------- | ----------- |
| 1. Personal-data processing type | User account/customer profile, payment/checkout, support/communication, telemetry/monitoring tied to a person, analytics, and service delivery processing. | No formal processing inventory is present. |
| 2. Data subjects | Shoppers; internal engineers, platform engineers, tech leads, reviewers, support, and operations users may appear in operational evidence. | Data subject scope is not formally documented. |
| 3. Personal data categories | Direct identifiers/profile links, authentication/session metadata, order/cart/delivery records, payment authorization references, notification contact channels, telemetry, and operational identifiers. | Exact fields, schemas, and payloads are not documented. |
| 4. Processing purposes | Account access, checkout, payment, order, delivery, notifications, analytics, audit, security, and operations. | Purpose-to-field mapping is missing. |
| 5. Data locations | PostgreSQL service databases, Kafka topics, search index, logs, metrics, traces, App Insights, Log Analytics, backups, external providers, Artifactory/registry/deployment metadata. | Caches, exports, data lake, and retention locations are not fully specified. |
| 6. Owner review | Platform, support, operations, tech leads, and reviewers are described as roles. | Named product, technical, privacy, legal, security, compliance, risk, data-governance, vendor, and procurement approvals are missing. |
| 7. Lawful or governance basis | Unknown. | Escalate lawful basis and purpose limitation to legal, privacy, DPO, compliance, and business owners. |
| 8. Special-category or high-sensitivity data | No special-category, criminal offence, children's, or vulnerable-person data is identified. Payment, authentication, and security telemetry are sensitive engineering data. | Confirm whether any special-category or children's data can appear in free text, support, delivery, analytics, or notifications. |
| 9. Profiling, automated decision, or rights impact | Checkout validates prices, promotions, inventory, delivery constraints, and payment authorization. Analytics updates dashboards and forecasts. | Profiling, fraud, pricing, or eligibility impact is not documented; escalate if automated decisions affect people. |
| 10. Controller, processor, or subprocessor roles | Unknown. | Roles for the ecommerce operator, Azure, payment gateway, identity provider, notification provider, and observability tools are not documented. |
| 11. Transfers or vendor processing | Azure, external identity, external payment gateway, email/SMS provider, Artifactory, Docker/container registry, and observability tooling are involved. | Regions, subprocessors, data processing agreements, and transfer mechanisms are not documented. |
| 12. DPIA, privacy review, or security review | Security scans and runtime security controls are described. | DPIA, privacy review, and data protection review are not documented. |
| 13. Minimization controls | Purpose-specific DTOs and event payload minimization are required for customer, order, cart, delivery, payment, notification, analytics, and logs. | Current DTOs, schemas, and event payloads are not available for review. |
| 14. Access-control controls | Field-level authorization, purpose-specific scopes, tenant/market isolation, least privilege, admin access review, audit trail, and non-production masking are required. | Fine-grained access policy and audit evidence are not documented. |
| 15. Data-subject rights workflows | Access, rectification, erasure, restriction, objection, portability, and consent/preference update are potentially required for customer profile, order, notification, analytics, and provider data. | Rights orchestration and deletion propagation are not documented. |
| 16. Retention and deletion controls | Retention periods, deletion jobs, cache/index removal, Kafka/event retention, backup policy, export expiration, tombstones, and downstream deletion propagation are required. | Retention schedule and deletion evidence are missing. |
| 17. Privacy-safe observability controls | Redacted payloads, hashed subject identifiers, limited log retention, secure log access, and breach evidence without excessive personal data are required. | Current logging, metrics, and trace field policy is not documented. |
| 18. Security-of-processing controls | HTTPS, WAF, private networking, Key Vault, managed identities, scans, immutable images, SBOM/provenance, and observability are described. | Field encryption, pseudonymization, audit policy, access reviews, and data masking are not fully documented. |
| 19. Privacy evidence | Data-flow and deployment diagrams exist. CI/CD and security evidence are described. | Personal-data inventory, lawful-basis handoff, DPIA/privacy review, retention, rights, transfer, and breach evidence are missing. |
| 20. Breach-response evidence | Logs, metrics, traces, alerts, Azure Monitor, App Insights, and Log Analytics are described. | Containment runbook, privacy/security escalation, affected data identification, notification handoff, and corrective-action workflow are missing. |
| 21. Current release decision | Unknown from GDPR perspective. Automated production deployment after checks is described. | Production personal-data processing should be blocked or conditioned until owner review and privacy evidence are complete. |
| 22. Next steps | Create personal-data inventory, minimize DTOs/events/logs, add field-level authorization, rights workflow, retention/deletion controls, transfer/vendor/DPIA evidence, privacy-safe logging, breach evidence, and release-readiness approvals. | Required before relying on the system for production personal-data processing. |

Material unanswered questions:

- What exact personal data fields are collected, persisted, emitted, logged, indexed, exported, backed up, and sent to vendors?
- Which lawful basis, purposes, retention periods, controller/processor roles, and data-governance owners apply?
- Which providers receive personal data, in which regions, under which agreements, and with which transfer mechanisms?
- How are data-subject rights fulfilled across databases, Kafka topics, search indexes, logs, analytics, notifications, external providers, and backups?
- What DPIA, privacy review, security review, breach-response, and release-readiness evidence exists?

## 4. GDPR Privacy Risk Classification

- **Personal-data processing signals:** Strong. Customer profile, authentication, cart, checkout, payment, order, delivery, notification, analytics, logs, traces, and support operations involve or may involve personal data.
- **Special-category, criminal offence, children's, or vulnerable-person data signals:** Not identified in the reviewed materials. Unknown free-text, support, analytics, or notification payloads require validation.
- **Profiling, automated decision, or rights-impacting signals:** Possible. Price validation, promotions, payment authorization, delivery slot allocation, analytics, and forecasts could affect shoppers if rules or automated decisions are tied to individuals.
- **Controller, processor, subprocessor, or joint-controller concerns:** Unknown. The ecommerce operator and external identity, payment, notification, cloud, artifact, registry, and observability providers require role documentation.
- **Third-country transfer or vendor-processing concerns:** Unknown. External providers and Azure services are in scope, but regions, subprocessors, and transfer mechanisms are not documented.
- **DPIA or privacy-review concerns:** Unknown. A privacy review or DPIA assessment should be escalated because the system processes customer profiles, payment-related records, delivery records, telemetry, analytics, and vendor integrations.
- **Security-of-processing concerns:** Existing security signals include HTTPS, WAF, private networking, Key Vault, managed identities, static and dependency scans, image scans, SBOM/provenance, observability, and rollback. Field-level authorization, pseudonymization, log redaction, access review, and data masking evidence are missing.
- **Breach-response concerns:** Observability exists, but breach evidence, affected data category mapping, escalation path, containment runbook, and legal/privacy notification handoff are not described.
- **Classification conclusion for governance review:** Classify as personal-data processing with payment, authentication, delivery, notification, telemetry, analytics, vendor, transfer, retention, and breach-readiness signals. Do not treat the system as GDPR-ready without qualified owner review.
- **Required escalation:** Legal/privacy/DPO for lawful basis, role, transfer, DPIA, rights, and retention interpretation; security/risk for access, encryption, logging, incident, and breach evidence; data governance for inventory, minimization, lineage, and deletion; architecture/platform for implementation controls; procurement/vendor owners for third-party processing.

## 5. Engineering Controls

- **Data inventory and data-flow evidence:** Create a maintained inventory for customer profile, authentication, cart, checkout, payment, order, saga, inventory, delivery, notification, analytics, logs, traces, metrics, backups, exports, and provider data.
- **Data minimization and DTO design:** Use purpose-specific DTOs and event contracts. Do not expose persistence entities or broad service payloads at API boundaries, Kafka events, analytics exports, or operational logs.
- **Purpose-specific processing controls:** Map each field to service-delivery, security, audit, legal/compliance, notification, analytics, or support purposes. Block fields with no documented purpose and owner.
- **Field-level authorization and least privilege:** Enforce requester, purpose, tenant/market, role, and service-account checks before returning or exporting customer, payment, order, delivery, profile, or support data.
- **Pseudonymization, tokenization, redaction, or masking:** Tokenize or hash subject identifiers in logs and traces, preserve payment tokens instead of card data, mask non-production personal data, and restrict access to raw identifiers.
- **Privacy-safe logging and observability:** Avoid request/response payload logging, credentials, tokens, addresses, free-text messages, payment details, and broad profile data. Log correlation IDs, order IDs, subject hashes, action, outcome, and trace IDs where needed.
- **Retention and deletion controls:** Define retention for profile, cart, order, payment references, delivery, notifications, analytics, Kafka events, logs, traces, search indexes, exports, and backups. Add scheduled deletion, tombstone events, cache/index removal, export expiration, and evidence records.
- **Cache, index, export, and backup handling:** Treat search indexes, Kafka topics, Azure Monitor/App Insights/Log Analytics, Backup Vault, registry metadata, Artifactory metadata, and generated reports as reviewable derived stores.
- **Data-subject rights workflows:** Implement authenticated and authorized access, rectification, erasure, restriction, objection, portability, and preference update orchestration across owned services and providers.
- **Transfer or vendor-review evidence:** Document provider, data categories, purpose, region, subprocessor list, role, contract, transfer review, security review, retention, deletion, and incident notification path.
- **Security-of-processing controls:** Keep HTTPS, WAF, private networking, Key Vault, managed identities, scans, immutable artifacts, SBOM/provenance, and rollback. Add field-level authorization, access reviews, audit trails, encryption evidence, non-production masking, and secrets/data exposure tests.
- **Breach detection, containment, and evidence:** Define data-category mapping, incident severity, containment runbook, owner escalation, affected subject assessment support, notification handoff, and post-incident corrective actions.
- **Testing and validation:** Add tests for DTO minimization, field authorization, deletion propagation, retention jobs, log redaction, provider payload minimization, rights workflows, and breach-evidence capture.

Relevant Java engineering examples from the GDPR reference:

```java
public record CustomerSummaryResponse(
    String customerId,
    String displayName,
    String supportTier
) {
}
```

Use purpose-specific DTOs instead of returning full persistence entities for profile, order, delivery, payment, notification, or support data.

```java
public RightsRequestResult fulfillErasure(RightsRequest request) {
    authorization.verifyRequester(request.subjectId(), request.requester());
    List<DataLocation> locations = dataMap.locationsForSubject(request.subjectId());
    DeletionResult result = deletionExecutor.execute(deletionPlanner.plan(request.subjectId(), locations));
    audit.recordRightsRequest(request.id(), request.subjectId(), result.summary());
    downstreamNotifier.publishDeletionCompleted(request.subjectId(), result.deletedLocations());
    return RightsRequestResult.completed(request.id(), result.deletedLocations());
}
```

Rights workflows should locate every store and derived store before reporting completion.

```java
auditLogger.info(
    "privacy_event eventId={} subjectHash={} action={} outcome={} traceId={}",
    event.id(),
    subjectHasher.hash(event.subjectId()),
    event.action(),
    event.outcome(),
    event.traceId()
);
```

Privacy-safe observability should preserve investigation value without logging raw payloads, credentials, addresses, free text, or payment data.

```java
public CustomerProfileResponse viewProfile(CustomerId customerId, Requester requester, Purpose purpose) {
    CustomerProfile profile = customerRepository.getRequired(customerId);
    FieldPolicyDecision decision = fieldPolicy.evaluate(requester, purpose, profile);
    audit.recordPersonalDataAccess(customerId, requester.id(), purpose, decision.allowedFields(), decision.deniedFields());
    return CustomerProfileResponse.from(profile, decision.allowedFields());
}
```

Field-level authorization should be explicit, auditable, and purpose-aware before returning customer data.

## 6. Evidence Inventory

| Artifact | Current evidence from source material | Gap |
| -------- | ------------------------------------- | --- |
| Personal-data inventory | Services, databases, Kafka topics, external providers, logs, traces, metrics, and backups are described. | No field-level inventory with categories, purposes, owners, processors, retention, and deletion paths. |
| Data-flow diagram | `expected-system-deployment.puml` shows Azure edge, AKS, data services, external providers, and delivery pipeline. | No privacy-specific data-flow annotations, data categories, or transfer regions. |
| Purpose and lawful-basis handoff | Business and technical processing purposes are inferable from ecommerce workflow. | Lawful basis, purpose owner, and legal/privacy handoff are not documented. |
| DPIA or privacy review | Not described. | Required owner decision is missing. |
| Security review | WAF, private networking, Key Vault, managed identities, scans, SBOM, provenance, and rollback are described. | Field-level authorization, pseudonymization, privacy-safe logging, access review, and masking evidence are missing. |
| Access-control evidence | API gateway enforces authentication; managed identities protect secrets access. | Fine-grained authorization, admin access review, audit trail, and least-privilege evidence are missing. |
| Data-subject rights workflow | Identity service manages user consent. | Access, rectification, erasure, restriction, objection, portability, preference propagation, and provider coordination are not documented. |
| Retention policy | Not described. | Retention periods and legal hold exceptions are missing. |
| Deletion job evidence | Not described. | Deletion jobs, tombstones, and completion evidence are missing. |
| Deletion propagation evidence | Kafka events and downstream services are described. | Propagation to caches, search indexes, topics, analytics, exports, providers, logs, and backups is not documented. |
| Transfer or vendor review | External identity, payment, notification, Azure, artifact, registry, and observability dependencies are described. | Provider roles, regions, subprocessors, contracts, data processing agreements, and transfer mechanisms are missing. |
| Breach response runbook | Logs, metrics, traces, dashboards, alerts, Azure Monitor, App Insights, and Log Analytics are described. | Containment, evidence capture, affected data classification, privacy/security escalation, notification handoff, and corrective action are missing. |
| Monitoring dashboard or alert evidence | Operations use dashboards, logs, traces, and alerts. | Dashboard IDs, alert rules, data minimization, access control, and retention are not documented. |
| Approval records | Pull request reviews and checks are described. | Privacy, legal, DPO, security, data-governance, and residual-risk approvals are missing. |

## 7. Residual Risks

| Residual risk | Impact | Likelihood | Mitigation | Owner | Acceptance decision |
| ------------- | ------ | ---------- | ---------- | ----- | ------------------- |
| Personal-data inventory is incomplete | Unknown collection, exposure, retention, or deletion obligations | High | Create field-level data inventory and data-flow map | Data governance / architecture | Must be remediated before production privacy approval |
| Lawful basis, role, DPIA, and transfer decisions are missing | Incorrect governance posture or blocked release | High | Legal/privacy/DPO review and documented decisions | Legal / privacy / DPO | Requires qualified review |
| Rights workflows do not cover derived stores | Incomplete access, correction, erasure, or portability fulfillment | Medium | Implement rights orchestration across databases, Kafka, logs, search, analytics, backups, and vendors | Product / platform | Requires remediation |
| Logs, traces, or analytics may contain excessive personal data | Privacy exposure, breach impact, and unnecessary retention | Medium | Redaction, hashing, payload suppression, retention limits, and secure access | Security / SRE | Requires control evidence |
| Vendor and transfer evidence is missing | Unreviewed provider processing or third-country transfer risk | Medium | Provider register, DPA/subprocessor review, region mapping, and transfer assessment | Procurement / legal | Requires governance acceptance |
| Retention and deletion are not documented | Excessive storage and failure to remove data | Medium | Retention schedule, deletion jobs, tombstones, and propagation evidence | Data governance / engineering | Requires remediation |
| Breach response lacks privacy handoff | Slow or incomplete incident investigation and notification support | Medium | Incident runbook, affected data mapping, escalation path, and evidence retention | Security / privacy | Requires remediation |

## 8. Release Decision

- **Decision:** Not approved for GDPR-ready production personal-data processing based only on reviewed evidence. Conditionally acceptable as an architecture example or engineering validation artifact, provided production personal-data processing is gated by privacy, legal, security, data-governance, and business-owner review.
- **Conditions:** Complete personal-data inventory, purpose/lawful-basis handoff, controller/processor role review, DPIA/privacy review decision, vendor and transfer evidence, retention/deletion controls, rights workflows, privacy-safe observability, breach evidence, and owner approvals.
- **Blockers:** Missing field-level data inventory, missing lawful-basis handoff, missing role documentation, missing DPIA/privacy review, missing retention/deletion policy, missing rights workflow, missing transfer/vendor evidence, missing privacy-safe logging evidence, and missing breach-response handoff.
- **Required approvals:** Legal, privacy/DPO, compliance, security/risk, data governance, procurement/vendor owner, architecture/platform, product/business owner, and technical owner.
- **Expiry or review date:** Review within 90 days or earlier if personal-data fields, providers, regions, analytics, AI features, logs, exports, backups, rights workflows, or payment/delivery processing materially change.
- **Environments approved:** Development, test, and staging with synthetic or minimized data only until the missing privacy evidence is complete.
- **Data categories approved:** No approval for production personal-data categories is granted by this report.
- **Vendor or transfer constraints:** Do not send personal data to external identity, payment, notification, observability, cloud, artifact, registry, analytics, support, or AI providers without documented provider, role, region, retention, security, and transfer review.

## 9. Action Plan

| Priority | Action | Owner | Due date | Evidence expected | Status |
| -------- | ------ | ----- | -------- | ----------------- | ------ |
| High | Create a GDPR personal-data inventory for profile, auth, cart, checkout, payment, order, delivery, notification, analytics, logs, traces, metrics, Kafka, search, backups, exports, and providers. | Data governance / architecture | 2026-06-28 | Approved inventory with fields, categories, purposes, owners, stores, retention, and deletion paths | Open |
| High | Document lawful-basis handoff, controller/processor roles, DPIA/privacy review decision, and required owner approvals. | Legal / privacy / DPO / compliance | 2026-06-28 | Governance decision record and escalation outcomes | Open |
| High | Define retention, deletion, tombstone, cache/index removal, Kafka/event retention, backup handling, and deletion-propagation controls. | Platform / data owners | 2026-07-05 | Retention schedule, deletion workflow, and evidence records | Open |
| High | Implement or document data-subject rights orchestration across owned services and external providers. | Product / engineering | 2026-07-05 | Rights workflow design, authorization rules, audit evidence, and test cases | Open |
| High | Review vendors, subprocessors, regions, transfer mechanisms, DPAs, retention, deletion, and incident notification paths. | Procurement / legal / security | 2026-07-12 | Vendor register and transfer-review evidence | Open |
| Medium | Add privacy-safe logging, tracing, metrics, and audit event controls. | SRE / security | 2026-07-12 | Redaction rules, subject hashing, access controls, retention limits, and tests | Open |
| Medium | Add field-level authorization and purpose-specific DTO/event payload minimization. | Engineering / architecture | 2026-07-19 | Code, tests, review checklist, and API/event contract evidence | Open |
| Medium | Add breach-response runbook with affected data mapping, containment, escalation, notification handoff, and corrective-action workflow. | Security / privacy / SRE | 2026-07-19 | Runbook, drill result, and evidence-retention policy | Open |
| Low | Annotate the deployment diagram with privacy data categories, owners, processors, regions, retention, and deletion propagation after governance review. | Architecture | 2026-08-02 | Updated diagram or ADR linked to inventory | Open |

## 10. Final Notes

- **Items requiring legal interpretation:** Lawful basis, controller/processor roles, jurisdiction, transfer mechanism, DPIA requirement, data-subject rights interpretation, retention obligations, and breach notification requirements.
- **Items requiring privacy or data protection officer decision:** Personal-data inventory approval, DPIA/privacy review outcome, special-category confirmation, rights workflow adequacy, minimization, and residual-risk acceptance.
- **Items requiring security exception:** Any logging of raw personal data, broad production log access, weak encryption, unmasked non-production data, unreviewed payment/authentication data handling, or missing breach evidence.
- **Items requiring data governance decision:** Field ownership, purpose mapping, retention schedule, deletion propagation, backup handling, exports, analytics, and data lineage.
- **Items requiring vendor or transfer review:** Azure, external identity provider, payment gateway, email/SMS provider, Artifactory, Docker/container registry, observability tooling, analytics, support, and any future AI/model provider.
- **Items requiring architecture decision:** Rights orchestration pattern, deletion/tombstone propagation, privacy event schema, field-level policy enforcement, and where privacy evidence is stored.
- **Items requiring product or business acceptance:** Reduced field collection, retention limits, rights workflow scope, release conditions, vendor constraints, and residual risk from delayed privacy controls.
- **Next review trigger:** New personal-data category, new provider or region, production data use in non-production, analytics/export expansion, AI or RAG integration, support free-text processing, failed deletion, breach event, or material checkout/payment/delivery workflow change.
