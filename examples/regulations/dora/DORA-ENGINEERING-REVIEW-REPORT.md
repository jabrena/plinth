# DORA Engineering Review Report

This report is not legal advice. Use it as engineering evidence for legal, compliance, security, risk, resilience, procurement, business-continuity, architecture, and business-owner review.

The purpose of this report is to increase awareness of potential operational-resilience gaps and create engineering evidence for qualified review. The response produced from this template does not represent legal advice, a legal opinion, or a final regulatory determination.

## 1. Review Context

- **System, service, product, or platform:** Ecommerce deployment example
- **Repository or implementation path:** `java-cursor-rules` example architecture materials
- **Review date:** 2026-06-14
- **Reviewers:** DORA engineering review using `802-regulations-dora`
- **Business owner:** Not identified in reviewed materials
- **Technical owner:** Not identified in reviewed materials
- **Operational owner:** Support and operations teams are described, but named operational ownership is not identified
- **Security/risk owner:** Not identified in reviewed materials
- **Resilience or business-continuity owner:** Not identified in reviewed materials
- **Procurement or vendor owner:** Not identified in reviewed materials
- **Source materials reviewed:**
  - `examples/diagrams/deployment/system-example.md`
  - `examples/diagrams/deployment/expected-system-deployment.puml`
  - `.agents/skills/802-regulations-dora/references/802-regulations-dora.md`
  - `.agents/skills/802-regulations-dora/assets/questions/802-dora-engineering-review-questionnaire.md`
  - `.agents/skills/802-regulations-dora/assets/reports/802-dora-engineering-review-report-template.md`

## 2. Operational Scope Summary

- **Business service supported:** Customer-facing ecommerce shopping, checkout, payment authorization, order confirmation, inventory reservation, delivery slot booking, notification, and analytics workflows.
- **Financial entity, important business service, or critical ICT context:** The reviewed material describes a conventional non-financial ecommerce service. Payment authorization and customer checkout create operational-resilience signals, but the material does not establish that the system is operated by a financial entity or supports a DORA-regulated important business service.
- **Primary users:** Shoppers, software engineers, tech leads and reviewers, platform engineers, and support and operations teams.
- **Affected customers, operations, or downstream systems:** Shoppers and downstream inventory, delivery, notification, analytics, payment, and order-status workflows may be affected by checkout, payment, event, or deployment failures.
- **Environments in scope:** Development, test, staging, and production Azure environments.
- **Deployment topology:** Azure Front Door and WAF route public HTTPS traffic to AKS ingress. Java Quarkus services run as containers in AKS. Stateful services use PostgreSQL and Kafka. Shared Azure services include Key Vault, Azure Monitor, Log Analytics, App Insights, Private DNS, Private Link, Azure Policy, and Backup Vault.
- **Data stores and stateful components:** PostgreSQL stores for profile, product, search, cart, pricing, order, saga, inventory, and delivery state; Kafka topics for domain events.
- **Message brokers, jobs, schedulers, or batch processes:** Kafka Backbone with topics such as `order.created`, `payment.authorized`, `stock.reserved`, and `slot.booked`; checkout saga workflow and event consumers.
- **Third-party ICT providers:** Azure platform services, external identity provider, external payment gateway, email/SMS provider, Artifactory, Docker registry, and the selected container registry.
- **Recovery expectations:** Rollback uses a previous validated image and compatible database state. Backups are represented through Azure Backup Vault. RTO, RPO, restore-test dates, failover targets, incident-drill evidence, and provider-exit expectations are not documented.

## 3. Questionnaire Findings

Acceptance-test note: the feature scenario requires questionnaire answers to be based only on information present in `examples/diagrams/deployment/system-example.md`. Unknown items below are intentionally not filled from assumptions outside that source. The questionnaire must be asked one question at a time in normal DORA skill usage; this report records the constrained evidence outcome for the acceptance scenario.

| Question | Answer based only on reviewed source material | Gap or note |
| -------- | -------------------------------------------- | ----------- |
| 1. Business or operational service | Conventional non-financial ecommerce business service with checkout, payment authorization, order, inventory, delivery, notification, and analytics workflows. | Financial-entity status is not described. |
| 2. System and operational ownership | Platform engineers and support and operations teams are described. Tech leads and reviewers inspect pull requests. | Named product, technical, security, risk, resilience, and procurement owners are not identified. |
| 3. Environments | Development, test, staging, and production Azure environments. | Disaster recovery or secondary-region environment is not described. |
| 4. Material ICT assets or dependencies | Java Quarkus services, PostgreSQL, Kafka, Key Vault, Azure platform services, CI/CD, Artifactory, Docker registry, external identity provider, external payment gateway, email/SMS provider, observability tooling. | Dependency ownership, criticality, contracts, SLAs, and exit paths are not described. |
| 5. Expected service criticality | Customer-facing ecommerce checkout and payment workflows indicate at least important business impact for the ecommerce operator. | Formal criticality assignment is not documented. |
| 6. Outage or degradation impact | Customer transactions could fail or be delayed; downstream systems could receive late, duplicated, or incorrect events; manual support may be required for order, payment, inventory, or delivery issues. | Regulator, audit, or risk reporting impact is not stated. |
| 7. Incident detection | Dashboards, logs, traces, alerts, Azure Monitor, App Insights, and Log Analytics are described; services emit structured logs, metrics, and traces with correlation IDs. | SLOs, alert thresholds, synthetic checks, severity classification, and business transaction monitoring are not documented. |
| 8. Escalation paths | Support and operations teams diagnose order, payment, inventory, and delivery issues. | On-call, security, business, risk, resilience, provider, customer communication, and post-incident review workflows are not documented. |
| 9. Incident evidence | Correlation IDs, logs, metrics, traces, alerts, dashboards, affected services, dependencies, and order-related identifiers are described. | Incident timelines, owner approvals, communication records, mitigation records, and corrective-action evidence are not documented. |
| 10. Reporting or regulator handoff obligations | Unknown. | Escalate to legal, compliance, risk, security, and resilience owners if the service supports a regulated entity or critical financial operation. |
| 11. Recovery targets | Rollback uses previous validated images and compatible database state. | RTO, RPO, availability target, capacity target, and manual workaround time are not defined. |
| 12. Backup and restore evidence | Azure Backup Vault is shown in the topology. | Backup schedule, monitoring, restore-test evidence, encryption review, access controls, and retention review are not documented. |
| 13. Continuity or failover controls | Rollback procedure is described at a high level through validated images and database compatibility. | Continuity plan, failover procedure, disaster recovery environment, multi-region capability, manual workaround, and resilience drills are not documented. |
| 14. Change-control evidence | Pull request review, CI checks, immutable artifacts, signing, SBOM, provenance, deployment validation, smoke tests, migration safety checks, and rollback decision are described. | Named release approvals, IAM/secrets change review, provider integration review, and production validation ownership are not documented. |
| 15. Resilience tests | Integration, contract, security, architecture, smoke, migration, performance, and deployment checks are described. | Backup restore tests, failover tests, dependency failure tests, provider outage simulations, and incident response drills are not documented. |
| 16. Known release blockers or gaps | Missing owner evidence, missing resilience ownership, untested restore or failover, missing provider evidence, and missing formal recovery targets. | No explicit release blocker list is provided in the source. |
| 17. Material third-party ICT providers | Azure, external identity provider, external payment gateway, email/SMS provider, Artifactory, Docker registry, and container registry. | Provider criticality and ownership are not documented. |
| 18. Third-party provider evidence | Provider roles are visible in the architecture. | SLA, support agreement, incident notification, data location, exit plan, concentration risk, subcontractor, and provider monitoring evidence are not documented. |
| 19. Current release decision | Unknown from DORA perspective. The source describes automated production deployment after required checks pass. | Block production reliance for regulated financial use until resilience owners approve documented gaps. |
| 20. Next steps | Create ICT inventory, define owners, add monitoring/alerting evidence, add incident escalation, verify backup and restore, add continuity/failover/rollback plan, review third-party ICT provider controls, run resilience tests, and complete release readiness evidence. | These actions should be prioritized before treating the service as DORA-relevant or operationally critical for a regulated financial entity. |

Material unanswered questions:

- Is the ecommerce platform operated by, or provided to, a financial entity?
- Are checkout, payment authorization, settlement, reconciliation, reporting, or fraud workflows connected to regulated financial operations?
- Who owns business service resilience, operational risk, security, compliance, provider contracts, and release-readiness decisions?
- What RTO, RPO, availability, capacity, and manual-workaround targets apply to checkout, payment, order, inventory, delivery, and notification flows?
- What restore, failover, provider-outage, dependency-failure, and incident-response tests have passed, and when?
- Which Azure, payment, identity, notification, artifact, registry, and observability providers are material or critical, and what SLAs, support paths, exit plans, and monitoring evidence exist?

## 4. DORA Operational Resilience Classification

- **Financial-entity or regulated-service signals:** Not established by the reviewed materials. The system is described as ecommerce, not as a bank, payment institution, investment firm, insurer, or other financial entity.
- **Important business service signals:** Customer-facing checkout, payment authorization, order confirmation, inventory reservation, and delivery slot booking could be important business services for the ecommerce operator.
- **Critical ICT function signals:** Quarkus services, PostgreSQL data stores, Kafka, Azure networking, Key Vault, Azure Monitor, Artifactory, container registry, and CD automation are material ICT assets. They are not classified as DORA critical ICT functions in the reviewed source.
- **Third-party ICT provider signals:** Azure, external identity, payment gateway, email/SMS provider, Artifactory, Docker registry, and container registry are provider dependencies that require ownership, monitoring, incident notification, and exit evidence if the service becomes DORA-relevant.
- **Incident reporting or regulator handoff concerns:** Unknown. Escalate if a financial entity uses the system for regulated services or if payment, reporting, customer, or operational outages trigger reporting obligations.
- **Outsourcing or provider criticality concerns:** Unknown. External payment, identity, cloud, messaging, data, registry, and observability dependencies should be reviewed by procurement, risk, security, resilience, and legal owners before production reliance by a regulated entity.
- **Applicability conclusion for governance review:** Based only on reviewed evidence, classify as a conventional non-financial ecommerce service with operational-resilience and third-party ICT dependency signals. Do not classify it as DORA-regulated without legal, compliance, and risk review.
- **Required escalation:** Legal/compliance for DORA applicability and reporting duties; risk/resilience for important service and recovery targets; security for incident evidence and secrets; procurement/provider owners for third-party ICT risk; architecture/platform for release gates and failover controls; business owners for service criticality and residual risk acceptance.

## 5. Engineering Controls

- **ICT asset and dependency inventory:** Create a maintained inventory for every Quarkus service, PostgreSQL data store, Kafka topic, Azure platform dependency, external provider, secret, artifact repository, container registry, deployment environment, owner, and recovery target.
- **Operational ownership and support model:** Assign business, technical, operational, security, risk, resilience, and provider owners for checkout, payment, order, inventory, delivery, notification, analytics, CI/CD, and platform services.
- **Monitoring, alerting, and observability:** Convert the described Azure Monitor, App Insights, Log Analytics, dashboards, logs, metrics, traces, and correlation IDs into service-level objectives, alert thresholds, business transaction checks, dependency health checks, and evidence retention.
- **Evidence-safe logging and traceability:** Preserve order, saga, event, correlation, causation, tenant, and market identifiers without logging credentials, secrets, payment tokens, personal data, or sensitive operational records unnecessarily.
- **Incident detection and severity classification:** Define severity criteria for checkout failure, payment provider outage, Kafka lag, duplicate or missing events, database degradation, deployment failure, identity outage, notification failure, and security monitoring gaps.
- **Incident escalation and communication:** Add on-call, business, security, risk, resilience, provider, customer communication, and post-incident review pathways with evidence records and corrective-action tracking.
- **Backup and restore controls:** Document backup schedules, backup monitoring, encryption and access control, retention, restore procedures, restore-test dates, restore results, and owners for all PostgreSQL stores and Kafka-recovery assumptions.
- **RTO/RPO and recovery evidence:** Define RTO, RPO, availability, capacity, and manual-workaround targets for checkout, order, payment, inventory, delivery, notification, and analytics flows.
- **Continuity, failover, rollback, or manual workaround:** Extend the existing immutable image rollback approach with database rollback compatibility, Kafka replay or compensation guidance, failover procedures, provider outage playbooks, and manual operational workarounds.
- **Resilience testing and incident drills:** Add backup restore tests, failover exercises, dependency-failure tests, provider outage simulations, incident response drills, capacity tests, data reconciliation, and replay tests.
- **Change-control evidence:** Preserve release approvals, database migration reviews, IAM and secrets change reviews, provider integration reviews, production validation, monitoring after change, rollback decisions, SBOM, provenance, and image-signing records.
- **Third-party ICT provider controls:** Record provider owner, contract owner, support path, SLA, data location, service-health monitoring, incident notification path, criticality, concentration risk, subcontractor review, and exit or portability plan.
- **Exit, portability, or provider-failure controls:** Add documented exit paths for external payment, identity, notification, registry, artifact, observability, and cloud-provider dependencies.

Relevant Java engineering patterns from the DORA reference:

- ICT inventory should capture service, business service, owner, resilience owner, runtime, data stores, external providers, recovery targets, and evidence.
- Incident routing should classify severity, capture evidence, notify owners, and create corrective actions instead of only logging exceptions.
- Recovery evidence should record backup schedule, RTO/RPO, restore-test result, rollback plan, and continuity owner.
- Release policy should block or condition production readiness when ICT inventory, incident runbook, restore test, provider review, or rollback plan evidence is missing.

## 6. Evidence Inventory

| Artifact | Current evidence from source material | Gap |
| -------- | ------------------------------------- | --- |
| ICT inventory | Services, PostgreSQL stores, Kafka topics, Azure services, external identity, payment, notification, Artifactory, Docker registry, and CD pipeline are described. | No formal inventory with owners, criticality, recovery targets, provider contacts, or evidence IDs. |
| Architecture or dependency diagram | `expected-system-deployment.puml` documents Azure edge, AKS, data services, shared platform services, external systems, and delivery pipeline. | No DORA-specific dependency criticality or failure-mode annotations. |
| Runbooks | Rollback is described at a high level. Operations use dashboards, logs, traces, and alerts. | No incident, recovery, continuity, provider outage, or failover runbooks are present in reviewed material. |
| Monitoring dashboards | Azure Monitor, App Insights, Log Analytics, dashboards, metrics, traces, logs, and alerts are described. | No dashboard IDs, alert rules, SLOs, thresholds, or evidence-retention policy. |
| Alert rules | Alerts are described generically. | No severity policy, alert ownership, paging path, or business transaction checks. |
| Incident workflow | Support and operations diagnose order, payment, inventory, and delivery issues. | No escalation, communication, incident timeline, provider handoff, post-incident review, or corrective-action workflow. |
| Backup policy | Azure Backup Vault appears in the topology. | No backup schedule, retention, monitoring, encryption review, access control, or restore procedure. |
| Restore test evidence | Not described. | Add restore-test evidence with date, result, duration, owner, and scope. |
| Failover or continuity test evidence | Not described. | Add failover, dependency outage, provider outage, and incident drill evidence. |
| Change records | Pull request review, CI checks, scans, signed artifacts, SBOM, provenance, and deployment checks are described. | Add named release approvals, change records, IAM/secrets review, provider change review, and production validation evidence. |
| Provider contracts, SLAs, or support contacts | Provider dependencies are visible. | No contracts, SLAs, support contacts, criticality ratings, or provider owners. |
| Provider monitoring or service-health evidence | Observability platform exists. | No provider-specific health dashboards, incident notification paths, or service-level evidence. |
| Risk acceptance or exception records | Not described. | Required for missing recovery, provider, owner, or testing evidence. |
| Approval records | Pull request reviews and checks are described. | No DORA resilience approval, provider approval, or residual-risk acceptance. |

## 7. Residual Risks

| Residual risk | Impact | Likelihood | Mitigation | Owner | Acceptance decision |
| ------------- | ------ | ---------- | ---------- | ----- | ------------------- |
| Financial-entity or DORA applicability is unresolved | Incorrect release posture or missing governance review | Medium | Legal/compliance classification and business-service criticality review | Legal / compliance | Must be reviewed before regulated use |
| Operational ownership gaps | Slow incident response and unclear accountability | Medium | Assign business, technical, operational, security, risk, resilience, and provider owners | Business / platform | Requires remediation |
| Untested backup, restore, failover, and continuity controls | Prolonged outage or data recovery failure | Medium | Run restore tests, failover drills, dependency-failure tests, and incident exercises | Resilience / SRE | Block regulated production reliance until evidenced |
| Provider criticality and exit paths are undocumented | Vendor outage may interrupt checkout, payment, identity, notification, or deployment | Medium | Provider inventory, SLAs, incident contacts, monitoring, concentration review, and exit plans | Procurement / risk | Requires governance acceptance |
| Incident evidence is incomplete | Weak reconstruction, reporting, or corrective action after major incident | Medium | Structured incident workflow, severity policy, timelines, communication records, and evidence retention | SRE / security | Requires remediation |
| Change-control evidence lacks resilience-specific approvals | Risky schema, IAM, provider, or deployment changes may reach production | Medium | Add resilience release gate and approvals for material changes | Platform / architecture | Requires policy update |

## 8. Release Decision

- **Decision:** Not approved for DORA-regulated or critical financial-service production reliance based only on reviewed evidence. Conditionally acceptable as a conventional ecommerce example after normal engineering validation, provided it is not operated as or for a regulated financial service.
- **Conditions:** Complete governance classification, owner assignment, ICT inventory, incident workflow, recovery targets, backup/restore evidence, continuity and failover tests, provider risk evidence, and resilience release approval before regulated production use.
- **Blockers:** Unknown DORA applicability, missing named owners, missing RTO/RPO, missing restore-test evidence, missing failover evidence, missing provider criticality records, missing SLAs/support contacts, missing incident escalation workflow, and missing resilience approval.
- **Required approvals:** Legal/compliance, risk/resilience, security, procurement/provider owner, platform/SRE, architecture, and business owner.
- **Expiry or review date:** Review within 90 days or earlier if a financial entity adopts the system, payment or settlement responsibilities expand, criticality changes, a material provider changes, or production incident evidence reveals a resilience gap.
- **Environments approved:** Development, test, and staging for engineering validation. Production use requires the conditions above if the service is business-critical or DORA-relevant.
- **Operational constraints:** Preserve current CI/CD validation, SBOM/provenance, signed immutable images, health checks, observability, and rollback controls while adding resilience evidence.
- **Provider constraints:** Do not treat Azure, external identity, payment gateway, email/SMS provider, Artifactory, Docker registry, or container registry as invisible dependencies; provider controls must be documented.

## 9. Action Plan

| Priority | Action | Owner | Due date | Evidence expected | Status |
| -------- | ------ | ----- | -------- | ----------------- | ------ |
| High | Determine whether the ecommerce platform is operated by or for a financial entity or supports a regulated important business service. | Legal / compliance / business owner | 2026-06-28 | Applicability assessment or documented escalation outcome | Open |
| High | Create a DORA-aware ICT inventory for services, databases, Kafka topics, Azure services, providers, secrets, CI/CD assets, environments, owners, and criticality. | Architecture / platform | 2026-07-05 | Approved ICT inventory with owner and criticality fields | Open |
| High | Assign business, technical, operational, security, risk, resilience, and procurement owners for customer checkout, payment, order, inventory, delivery, notification, platform, and provider dependencies. | Business owner / engineering leadership | 2026-07-05 | Ownership matrix and escalation contacts | Open |
| High | Define RTO, RPO, availability, capacity, and manual workaround targets for customer-facing and operational workflows. | Resilience / SRE / business owner | 2026-07-12 | Recovery target record linked to service inventory | Open |
| High | Verify backup and restore for PostgreSQL stores and document Kafka recovery or replay assumptions. | SRE / database owner | 2026-07-12 | Restore-test evidence with date, result, duration, and owner | Open |
| High | Document provider risk evidence for Azure, payment gateway, identity provider, notification provider, Artifactory, Docker registry, and container registry. | Procurement / risk / platform | 2026-07-19 | Provider register with SLA, support path, criticality, monitoring, and exit plan | Open |
| Medium | Add incident severity classification, escalation, communication handoff, provider notification, post-incident review, and corrective-action workflow. | SRE / security / resilience | 2026-07-19 | Incident runbook and evidence-retention checklist | Open |
| Medium | Add resilience release gate checks for ICT inventory, incident runbook, restore test, provider review, rollback plan, and residual-risk approval. | Platform / architecture | 2026-07-26 | CI/CD or release checklist evidence | Open |
| Medium | Run dependency-failure, provider-outage, failover, rollback, data reconciliation, and incident-response drills for checkout and payment flows. | Resilience / QA / SRE | 2026-08-02 | Drill reports with findings and corrective actions | Open |
| Low | Annotate the deployment diagram with criticality, owners, recovery targets, and provider dependency classifications after governance review. | Architecture | 2026-08-09 | Updated architecture evidence or ADR | Open |

## 10. Final Notes

- **Items requiring legal interpretation:** DORA applicability, financial-entity classification, incident reporting obligations, outsourcing interpretation, and provider criticality.
- **Items requiring compliance or risk decision:** Service criticality, residual risk acceptance, provider concentration risk, third-party ICT risk posture, and release readiness.
- **Items requiring resilience or business-continuity decision:** RTO/RPO, restore test cadence, failover strategy, continuity plan, manual workaround, and incident drill schedule.
- **Items requiring security exception:** Any missing secrets-management, logging, incident evidence, provider monitoring, identity, or access-control review.
- **Items requiring provider or procurement review:** Azure, external identity provider, external payment gateway, email/SMS provider, Artifactory, Docker registry, and container registry dependencies.
- **Items requiring architecture decision:** Whether to add DORA-specific release gates, how to document ICT inventory and provider dependencies, and how to represent resilience evidence in diagrams.
- **Items requiring product or business acceptance:** Residual risk from checkout, payment, order, inventory, delivery, and notification outages or delayed recovery.
- **Next review trigger:** Financial-entity adoption, EU regulated-service classification, material provider change, payment workflow expansion, new production environment, failed recovery test, major incident, or missing owner escalation.
