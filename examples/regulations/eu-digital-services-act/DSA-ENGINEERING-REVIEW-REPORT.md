# Digital Services Act Engineering Review Report

Use this report as engineering evidence for legal, compliance, trust-and-safety, privacy, security, product, risk, audit, research-access, architecture, and business-owner review.

This report is not legal advice. It identifies engineering controls and escalation points from the reviewed system evidence. Intermediary classification, platform classification, VLOP or VLOSE status, illegal-content determinations, advertising or recommender interpretation, audit or researcher access duties, systemic-risk conclusions, and regulatory interpretation require qualified owner review.

## 1. Review Context

- System or service name: CheckoutService delivery instructions change
- Repository, product, platform, or business service: Fictional Java Quarkus ecommerce platform
- Review date: 2026-06-14
- Reviewers: AI-assisted Digital Services Act engineering review
- Business owner: Not identified in reviewed evidence
- Technical owner: Software engineers, tech leads, reviewers, and platform engineers are described, but no named CheckoutService owner is identified
- Product owner: Not identified in reviewed evidence
- Trust-and-safety owner: Not identified in reviewed evidence
- Privacy owner: Not identified in reviewed evidence
- Security owner: Not identified in reviewed evidence
- Legal/compliance owner: Not identified in reviewed evidence
- Risk or audit owner: Not identified in reviewed evidence
- Research-access owner, where applicable: Not identified in reviewed evidence
- Source materials reviewed:
  - `examples/diagrams/deployment/system-example-cicd-pr-model.md`
  - `examples/diagrams/deployment/expected-system-deployment.puml`
  - `examples/diagrams/deployment/checkout-service-feature-request.md`
  - `.agents/skills/807-regulations-eu-digital-services-act/references/807-regulations-eu-digital-services-act-chapters-summary.md`
  - `.agents/skills/807-regulations-eu-digital-services-act/references/807-regulations-eu-digital-services-act-engineering-examples.md`
  - `.agents/skills/807-regulations-eu-digital-services-act/assets/reports/807-eu-digital-services-act-engineering-review-report-template.md`

## 2. Service And Platform Context

- Service description: CheckoutService coordinates checkout saga steps for price validation, inventory reservation, payment authorization, order creation, delivery slot booking, PostgreSQL order state, saga state, and Kafka events.
- Possible intermediary-service signal: The reviewed evidence describes a customer-facing ecommerce platform and web storefront, but it does not confirm that CheckoutService or the platform offers an intermediary service under the Digital Services Act.
- Possible hosting-service signal: The feature stores shopper-provided delivery instructions for order fulfillment, but the reviewed evidence does not show public dissemination of recipient-provided information.
- Possible online-platform or marketplace signal: Ecommerce, catalog, search, checkout, payment, delivery, and shopper interactions are present. The reviewed evidence does not confirm third-party trader listings, user-generated public content, or marketplace platform classification.
- Possible online-search-engine signal: A product Search Service exists, but the reviewed evidence does not show a general online search engine. Product ranking or search explanations may still be product governance evidence.
- Possible advertising or recommender signal: No advertising, promoted listing, personalization, or recommender workflow is described in the reviewed files.
- Possible VLOP or VLOSE signal: No active-recipient count, designation, or very-large-platform evidence is present.
- Deployment geography: Azure production environment is described; EU recipient geography and Member State targeting are not specified.
- Environments in scope: Development, test, staging, and production.
- Active-recipient or user-scale evidence: Not present.
- User, trader, or third-party content flows: Shopper-provided delivery instructions are persisted with orders. Product catalog ownership, trader listing source, user reviews, public posts, ads, and marketplace seller flows are not described.
- Open applicability questions:
  - Does the ecommerce platform qualify as an intermediary service, hosting service, online platform, or marketplace under the Digital Services Act?
  - Are products listed by the platform itself, internal catalog owners, or third-party traders?
  - Does the Search Service use ranking, recommender, personalization, or sponsored placement logic that needs user-facing explanation or ad transparency evidence?
  - Are there notice-and-action, moderation, complaint, appeal, trader traceability, or transparency-reporting workflows outside the reviewed files?
  - Could the platform meet VLOP or VLOSE thresholds, and who owns active-recipient measurement?

## 3. DSA Engineering Scope

- Applications, APIs, jobs, and batch workloads: Web storefront BFF, API gateway, identity, catalog, search, cart, pricing, CheckoutService, payment adapter, inventory, delivery slot, notification worker, analytics pipeline, checkout saga logic, and deployment workflows.
- User-generated content, trader listings, product data, ads, or third-party information: Shopper delivery instructions are user-provided order data. Product data exists, but the reviewed evidence does not identify third-party trader listings, user reviews, public content, or ads.
- Notice intake, authority orders, and response workflows: Not present in reviewed evidence.
- Content decision audit logs and moderation workflow state: Not present; no content moderation workflow is described.
- Complaint, appeal, out-of-court dispute, and reinstatement workflows: Not present in reviewed evidence.
- Recommender, ranking, search, personalization, and user-control workflows: Product search exists; ranking parameters, sort controls, personalization, and user-facing explanation evidence are not present.
- Advertising transparency metadata and repository evidence: Not present; no ads or promoted listings are described.
- Marketplace trader traceability and compliance-by-design controls: Not present; no trader onboarding or seller listing evidence is described.
- Risk assessment, audit, crisis response, and data access workflows where applicable: Not present; VLOP/VLOSE scope is not established.
- Logs, metrics, traces, dashboards, and privacy-safe observability: Azure Monitor, App Insights, Log Analytics, structured logs, metrics, traces, correlation IDs, dashboards, alerts, health checks, and smoke tests are described. The feature explicitly requires free-text delivery notes to be excluded from operational logs.
- Operational runbooks and support model: Support and operations teams are described, but DSA-specific notice, moderation, appeal, transparency, audit, or researcher-access runbooks are not present.

## 4. Potential Violation Or Non-Compliance Mapping

This section is not a legal finding. It lists concrete potential Digital Services Act violation or non-compliance signals from the reviewed evidence and routes each item to qualified owner review. No violation is confirmed by this engineering review.

| Potential violation or non-compliance signal | DSA reference area | Associated official-source link | Evidence from reviewed system | Current status | Required owner review | Engineering action |
| -------------------------------------------- | ------------------ | ------------------------------- | ----------------------------- | -------------- | --------------------- | ------------------ |
| Unclear intermediary, hosting, online-platform, marketplace, or online-search scope | Applicability and definitions | [Chapter I](https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32022R2065#cpt_I) | Ecommerce storefront, product search, checkout, and shopper delivery instructions are described, but DSA classification is not recorded | Potential gap | Legal / compliance / product owner | Record DSA scope decision, EU recipient geography, content flows, trader model, and accountable owners |
| Missing DSA owner model | Due diligence governance evidence | [Chapter III](https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32022R2065#cpt_III) | No legal, compliance, trust-and-safety, privacy, product, risk, audit, or research-access owners are named | Potential gap | Business / product / legal / compliance | Add DSA scope inventory with owner handoffs for platform, search, catalog, checkout, support, and observability |
| Missing notice intake and response tracking if hosting duties apply | Notice-and-action controls | [Chapter III, Section 2](https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32022R2065#cpt_III) | No notice-and-action, authority order, statement-of-reasons, or trusted flagger workflow is described | Potential gap if hosting or platform duties apply | Trust-and-safety / legal / security | Add notice intake, validation, triage, action, response, statement-of-reasons, and redress tracking where applicable |
| Missing complaint and appeal workflow evidence if online platform duties apply | Online platform redress controls | [Chapter III, Section 3](https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32022R2065#cpt_III) | Support and operations teams are described, but complaint, appeal, dispute, reinstatement, and moderation reversal evidence is not present | Potential gap if online platform duties apply | Trust-and-safety / product / legal | Define appeal state, deadlines, decisions, reinstatement actions, notifications, and evidence retention where applicable |
| Missing product search or ranking explanation evidence | Recommender and ranking transparency | [Article 27](https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32022R2065#cpt_III) | Product Search Service exists; ranking parameters, sort controls, personalization status, and user-facing explanations are not shown | Potential gap if recommender duties apply | Product / search-platform / legal / privacy | Record search and ranking parameters, user controls, personalization status, and explanation snapshots |
| Missing ad transparency metadata if promoted listings or ads exist | Advertising transparency | [Article 26](https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32022R2065#cpt_III) | No advertising or sponsored listing workflow is described | Open question | Product / ads owner / legal / privacy | Confirm whether ads or sponsored placements exist; if yes, attach ad labels, payer, beneficiary, targeting parameters, and guardrail evidence |
| Missing trader traceability if marketplace duties apply | Marketplace trader controls | [Chapter III, Section 4](https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32022R2065#cpt_III) | Product catalog is described, but trader onboarding, seller identity, and listing completeness evidence are not shown | Open question | Marketplace / legal / compliance | Confirm product source model; add trader traceability and compliance-by-design evidence if third-party traders can sell |
| Missing VLOP/VLOSE evidence if platform scale applies | Systemic-risk obligations | [Chapter III, Section 5](https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32022R2065#cpt_III) | No active-recipient count, designation, risk assessment, audit, ad repository, data access, or researcher-access evidence is present | Open question | Legal / risk / audit / research-access / executive accountability | Add active-recipient measurement and VLOP/VLOSE review; if applicable, create risk, audit, mitigation, crisis, transparency, and governed data-access evidence |
| Privacy-safe observability gap for free-text delivery notes | Evidence and confidentiality controls | [Chapter IV](https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32022R2065#cpt_IV) | The feature requires `delivery_instruction_note` to be excluded from logs and Kafka; tests are requested but not shown in reviewed evidence | Potential gap | Security / privacy / platform owner | Add privacy-safe logging tests, redaction policy, and observability review for delivery instruction data |
| Database migration and Kafka contract change may affect platform evidence | Engineering change control | [Chapter III](https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32022R2065#cpt_III) | Feature adds order columns and changes Kafka event schema to version 3 for `delivery.slot.requested`, `order.created`, and `order.confirmed` | Potential gap | Architecture / platform / product owner | Require migration approval, Kafka compatibility evidence, and release records before production deployment |

## 5. Engineering Controls

- Scope and service inventory: Add a DSA inventory entry for storefront, catalog, search, cart, checkout, delivery, notification, support, analytics, and any marketplace, moderation, ad, recommender, or trader workflows.
- Authority order and information request tracking: If DSA hosting or intermediary duties apply, add order receipt, legal basis, content locator, territorial scope, response time, user notification, and redress evidence.
- Notice intake and response tracking: If hosting or online platform duties apply, add notice validation, triage, deduplication, action, response, statement-of-reasons, trusted flagger, and misuse tracking.
- Content decision audit logs: If product listings, user content, reviews, ads, or accounts can be restricted, record decision source, policy reference, evidence ID, action, user notification, appeal availability, and workflow state.
- Moderation workflow state: Define queues, states, deadlines, ownership, reversals, reinstatement, and redacted evidence storage where moderation exists.
- Statement-of-reasons records: Create structured records for removals, disabling, demotion, suspension, account restrictions, or marketplace listing restrictions where applicable.
- User notification and redress: Add user-facing notifications, support escalation, appeal eligibility, and out-of-court dispute information where applicable.
- Complaint, appeal, dispute, and reinstatement workflows: Preserve appeal state, reviewer group, outcome, reversal, reinstatement action, and notifications.
- Trusted flagger routing and misuse protections: Add priority routing, abuse detection, repeated manifestly unfounded notice handling, and owner approval if trusted flagger workflows exist.
- Recommender and ranking explanation evidence: Record search and ranking parameters for product discovery, personalization status, sort controls, explanation snapshots, and user controls.
- User controls and profiling-related alternatives where applicable: Add controls for sort, personalization, profiling-free alternatives where applicable, and minor-protection review if minors can use the storefront.
- Advertising transparency metadata: If ads or sponsored listings exist, record ad label, payer, beneficiary, main targeting parameters, sensitive-profiling guardrails, and repository readiness where applicable.
- Marketplace trader traceability: If third-party traders sell through the platform, record trader identity, contact, payment, registration, verification, listing completeness, suspension, and consumer notification evidence.
- Transparency reporting: Add jobs or records for moderation actions, notices, complaints, automated tools, active-recipient counts, and other required metrics where applicable.
- Minor-protection and online interface controls: Review dark-pattern, interface design, default privacy, safety, and security controls if minors or vulnerable users may use the platform.
- Risk assessment and mitigation where applicable: If VLOP/VLOSE scope may apply, create risk assessment, mitigation tracker, crisis response, independent audit, compliance function, ad repository, and data access evidence.
- Data access for auditors or vetted researchers where applicable: Use governed, scoped, logged, redacted, revocable access rather than ad hoc production database dumps.
- Incident escalation and crisis-response evidence: Add DSA incident runbooks for notice workflow failure, moderation outage, ad metadata failure, ranking explanation defects, or transparency reporting defects.
- Privacy-safe observability: Exclude `delivery_instruction_note` from logs and Kafka payloads; preserve correlation IDs, order IDs, saga IDs, schema versions, and event names without logging free-text notes, access codes, secrets, tokens, or sensitive reports.

## 6. Evidence Inventory

- DSA scope inventory: Not present.
- Terms, policies, and user-facing explanations: Not present.
- Notice intake records: Not present.
- Authority order records: Not present.
- Content decision audit records: Not present.
- Statement-of-reasons records: Not present.
- Complaint and appeal records: Not present.
- Recommender or ranking explanation records: Product Search Service exists; explanation evidence is not present.
- User-control settings: Not present.
- Advertising metadata and repository records: Not present.
- Trader traceability evidence: Not present.
- Transparency report jobs or outputs: Not present.
- Risk assessment and mitigation records: Not present.
- Audit reports or audit plans: Not present.
- Researcher or authority data-access controls: Not present.
- Incident runbooks and alert routing: General logs, metrics, traces, dashboards, and alerts are described; DSA-specific runbooks are not present.
- Privacy-safe logging policy and tests: Feature requires delivery note log exclusion; tests are requested but not present.
- Release decision: Not present.

## 7. Residual Risks

- Residual risk: DSA scope is unknown for the ecommerce platform and related product search, catalog, checkout, and support workflows.
- Impact: Late discovery of notice, transparency, redress, trader traceability, recommender, ad, or VLOP/VLOSE evidence obligations.
- Likelihood: Medium, because the reviewed system is customer-facing ecommerce but lacks classification evidence.
- Mitigation: Record DSA scope decision, business model, EU geography, content flows, trader model, active-recipient count evidence, and accountable owners.
- Owner: Legal/compliance, product owner, trust-and-safety owner, and business owner.
- Acceptance decision: Required before treating the platform as DSA out of scope.
- Review date: Before production reliance on the delivery-instructions change.

- Residual risk: Product search or ranking may lack user-facing explanation and user-control evidence if recommender or platform transparency duties apply.
- Impact: Incomplete transparency evidence and weak owner review for product discovery behavior.
- Likelihood: Unknown, because ranking behavior is not described.
- Mitigation: Record ranking parameters, sort controls, personalization status, explanation snapshots, and product owner approval.
- Owner: Product owner, search-platform owner, legal/compliance owner, and privacy owner.
- Acceptance decision: Required if online platform or recommender duties apply.
- Review date: Before production deployment.

- Residual risk: Delivery instruction free text may spread through logs, Kafka, support tools, or incident evidence.
- Impact: Privacy exposure and unreliable governance evidence.
- Likelihood: Medium, because the feature explicitly introduces free text and event changes.
- Mitigation: Exclude notes from Kafka and logs, use field-level authorization for note lookup, add privacy-safe logging tests, and review retention rules.
- Owner: CheckoutService owner, privacy owner, security owner, and legal/compliance owner.
- Acceptance decision: Required before production release.
- Review date: Before merge and before production deployment.

## 8. Release Decision

- Decision: Conditionally blocked for DSA production reliance until DSA scope and owner handoffs are recorded. If qualified owners confirm the change does not involve DSA-regulated platform, hosting, marketplace, advertising, recommender, or VLOP/VLOSE workflows, DSA-specific blockers can be closed with that evidence.
- Conditions:
  - DSA scope inventory confirms whether the ecommerce platform has intermediary, hosting, online platform, marketplace, search, advertising, recommender, or VLOP/VLOSE concerns.
  - Product search and ranking behavior is reviewed for explanation and user-control evidence.
  - Delivery instruction notes are excluded from Kafka and operational logs.
  - Database migration tests and Kafka schema version 3 compatibility tests are linked to the release.
  - Legal/compliance and product owners confirm whether notice, moderation, complaint, appeal, trader traceability, ad transparency, or transparency reporting controls are needed.
- Blockers:
  - Missing DSA scope classification and owners.
  - Missing evidence for product search/ranking explanation and user controls.
  - Missing privacy-safe logging tests for free-text delivery notes.
  - Missing release evidence for database migration approval and Kafka compatibility.
- Required approvals: Service owner, tech lead, product owner, platform owner, privacy owner, security owner, legal/compliance owner, trust-and-safety owner when applicable, and business owner.
- Expiry or review date: Before merge and again before production deployment.
- Environments approved: Development and test only until blockers are resolved.
- Emergency rollback path: Previous validated image plus compatible database state. Confirm whether the order table migration can be safely rolled forward or rolled back without losing delivery instruction data.

## 9. Action Plan

| Priority | Action | Owner | Due date | Evidence expected | Status |
| -------- | ------ | ----- | -------- | ----------------- | ------ |
| High | Create DSA scope inventory covering intermediary, hosting, online platform, marketplace, search, recommender, advertising, trader, active-recipient, and VLOP/VLOSE signals | Product owner / legal-compliance owner | Before PR approval | DSA scope record linked to release | Open |
| High | Add privacy-safe logging and Kafka contract tests proving free-text delivery notes are not logged or broadcast | CheckoutService owner / security owner | Before merge | Test report and logging policy evidence | Open |
| High | Add migration approval and tests for nullable/default order columns and rollback or forward-fix behavior | CheckoutService owner / data owner | Before merge | Migration test results and release note | Open |
| High | Add Kafka schema version 3 compatibility evidence for `delivery.slot.requested`, `order.created`, and `order.confirmed` | CheckoutService owner / downstream service owners | Before merge | Contract test report and schema evidence | Open |
| Medium | Record product search and ranking explanation evidence, user controls, personalization status, and owner approval | Search-platform owner / product owner | Before production deployment | Ranking explanation snapshot and user-control record | Open |
| Medium | Confirm whether ads, promoted listings, third-party traders, user reviews, notices, moderation, complaints, or appeals exist outside the reviewed files | Product owner / trust-and-safety owner | Before production deployment | Capability inventory and owner decisions | Open |
| Medium | Define DSA incident escalation for notice, moderation, ranking, ad, transparency, or reporting workflow failures if those capabilities exist | Trust-and-safety / security operations | Before production deployment | Runbook and alert-routing evidence | Open |
| Low | Schedule next DSA engineering review after any marketplace, ad, recommender, review, moderation, or EU-scale change | Product owner / legal-compliance owner | After production validation | Review trigger record | Open |

## 10. Final Notes

- Items requiring legal interpretation: Intermediary, hosting, online platform, marketplace, online search engine, VLOP/VLOSE, illegal-content, advertising, recommender, audit, researcher-access, and regulatory interpretation.
- Items requiring platform or intermediary classification: Ecommerce platform business model, product source model, EU recipient targeting, and active-recipient count.
- Items requiring illegal-content or policy interpretation: None confirmed in reviewed files; required if product listings, user reviews, seller content, or delivery notes become subject to moderation.
- Items requiring advertising or recommender interpretation: Product search/ranking, promoted listings, personalization, and sponsored placement status.
- Items requiring VLOP/VLOSE, systemic-risk, audit, or researcher-access review: Active-recipient count and designation status are unknown.
- Items requiring privacy or security review: Delivery instruction note handling, logs, Kafka payloads, support tooling, retention, and field-level authorization.
- Items requiring product or business acceptance: DSA scope decision, search/ranking transparency, delivery note user experience, and release timing.
- Next review trigger: PR approval request, production deployment request, marketplace or trader capability, ad or promoted listing capability, recommender change, user-generated public content, moderation workflow, active-recipient threshold review, database schema change, Kafka event contract change, or privacy-safe observability failure.
