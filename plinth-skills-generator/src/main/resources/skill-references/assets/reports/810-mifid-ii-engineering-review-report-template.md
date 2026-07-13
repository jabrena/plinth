# MiFID II Engineering Review Report

Use this template after reviewing `../../references/810-regulations-eu-mifid-ii-chapters-summary.md`, answering or explicitly deferring `../questions/810-mifid-ii-engineering-review-questionnaire.md`, and matching the relevant examples from `../../references/810-regulations-eu-mifid-ii-engineering-examples.md` for investment-service scope, client classification, suitability, appropriateness, order-handling evidence, best-execution evidence, algorithmic-trading governance, record keeping, monitoring, and compliance evidence handoff.

This report is not legal advice. Use it as engineering evidence for legal, compliance, risk, product, operations, trading, market-structure, data-protection, security, architecture, executive accountability, and business-owner review.

The purpose of this report is to increase awareness of potential gaps in the system and create engineering evidence for qualified review. The response produced from this template does not represent legal advice, a legal opinion, an investment-firm classification, an investment-service determination, a jurisdictional determination, or a final regulatory decision.

## 1. Review Context

- System or service name:
- Repository, product, platform, or business service:
- Review date:
- Reviewers:
- Business owner:
- Product owner:
- Technical owner:
- Trading or market-structure owner:
- Operations owner:
- Risk owner:
- Security or data-protection owner:
- Legal/compliance owner:
- Source materials reviewed:

## 2. Investment-Service Context

- Service description:
- Possible investment-service or investment-activity signal:
- Possible investment-firm or trading-venue signal:
- Financial instruments or product types:
- Client or counterparty categories:
- Advisory, portfolio, order-evidence, execution-evidence, trading-governance, reporting, or record-keeping workflow:
- Deployment geography:
- Environments in scope:
- External venues, brokers, data vendors, or reporting providers:
- Open applicability questions:

## 3. Questionnaire Findings

- Questions answered:
- Questions explicitly deferred:
- Unknown answers requiring owner follow-up:
- Secrets or credentials redacted:
- Regulated-service scope escalation:
- Client-impact escalation:
- Trading or algorithmic trading escalation:
- Record-keeping, reporting, or retention escalation:
- Release blockers or conditions:

## 4. MiFID II Engineering Scope

- Applications, APIs, jobs, batch workloads, gateways, and adapters:
- Data stores, queues, topics, files, indexes, caches, and reporting stores:
- Client onboarding, classification, entitlement, and product-governance workflows:
- Suitability, appropriateness, advice, disclosure, warning, and report workflows:
- Order entry, validation, routing-evidence, execution-evidence, aggregation, allocation, cancellation, and correction workflows:
- Best-execution, client-instruction, venue-selection evidence, and execution-quality evidence:
- Algorithmic-trading governance, smart-order-routing evidence, market-making evidence, direct-electronic-access governance, and market-access governance controls:
- Clock synchronisation, timestamp precision, retention, replay, and reconstruction:
- IAM, secrets, keys, trading credentials, and privileged operations:
- CI/CD workflows and deployment paths:
- Monitoring, alerting, observability, incident, complaint, and escalation systems:
- Documentation, compliance reports, owner approvals, and handoff records:

## 5. Potential Violation Or Non-Compliance Mapping

This section is not a legal finding. Use it to list concrete potential MiFID II violation or non-compliance signals from the reviewed evidence and route each item to qualified legal, compliance, risk, product, operations, trading, market-structure, data-protection, security, architecture, or executive accountability review. When no violation is confirmed, say so explicitly and keep open items as potential gaps. Use the official-source links from `../../references/810-regulations-eu-mifid-ii-chapters-summary.md`; add more links when one finding spans multiple MiFID II areas.

| Potential violation or non-compliance signal | MiFID II reference area | Associated official-source link | Evidence from reviewed system | Current status | Required owner review | Engineering action |
| -------------------------------------------- | ----------------------- | ------------------------------- | ----------------------------- | -------------- | --------------------- | ------------------ |
| Unclear investment-service scope, investment-firm status, financial-instrument classification, client category, jurisdiction, or exemption | Scope / definitions / exemptions / annexes | [Title I and Annexes](https://eur-lex.europa.eu/eli/dir/2014/65/oj/eng) | TBD | None identified / Potential gap / Confirmed concern | Legal / compliance / product / risk | TBD |
| Missing client classification, product governance, suitability, appropriateness, advice, warning, disclosure, or client-report evidence | Investor protection / client information / suitability / appropriateness | [Title II](https://eur-lex.europa.eu/eli/dir/2014/65/oj/eng) | TBD | None identified / Potential gap / Confirmed concern | Product / compliance / legal / risk | TBD |
| Missing order-handling, client instruction, aggregation, allocation, cancellation, correction, best-execution, venue-selection, or execution-quality evidence | Best execution / client order handling / transaction evidence | [Title II](https://eur-lex.europa.eu/eli/dir/2014/65/oj/eng) | TBD | None identified / Potential gap / Confirmed concern | Trading / operations / compliance / architecture | TBD |
| Missing algorithmic-trading governance, market-access governance, pre-trade-risk evidence, throttle evidence, circuit-breaker evidence, emergency-stop evidence, simulation, deployment approval, or monitoring evidence | Algorithmic trading / organisational requirements / systems resilience | [Title II and Title III](https://eur-lex.europa.eu/eli/dir/2014/65/oj/eng) | TBD | None identified / Potential gap / Confirmed concern | Trading / risk / operations / compliance / security | TBD |
| Missing timestamp precision, clock synchronisation, immutable audit trail, record retention, replay, or reconstruction evidence | Record keeping / business clocks / auditability | [Article 50 and related record-keeping areas](https://eur-lex.europa.eu/eli/dir/2014/65/oj/eng) | TBD | None identified / Potential gap / Confirmed concern | Operations / compliance / architecture / security | TBD |
| Missing trading venue, broker, data vendor, reporting provider, transaction reporting, publication, reconciliation, or correction evidence | Regulated markets / data reporting / provider integrations | [Title III and Title V](https://eur-lex.europa.eu/eli/dir/2014/65/oj/eng) | TBD | None identified / Potential gap / Confirmed concern | Operations / reporting / compliance / legal | TBD |
| Incomplete monitoring, incident, complaint, change-control, release approval, owner handoff, or competent-authority evidence path | Supervision / competent authorities / sanctions / governance evidence | [Titles VI-VIII](https://eur-lex.europa.eu/eli/dir/2014/65/oj/eng) | TBD | None identified / Potential gap / Confirmed concern | Compliance / risk / operations / executive accountability | TBD |

## 6. Engineering Controls

- Investment-service scope inventory:
- Client classification and entitlement records:
- Product governance and target-market evidence:
- Suitability, appropriateness, advice, warning, and client-report workflows:
- Order lifecycle evidence audit:
- Client instruction capture:
- Best-execution policy and execution-quality metrics:
- Aggregation, allocation, cancellation, correction, and reconstruction:
- Algorithm inventory and deployment approval:
- Pre-trade-risk evidence:
- Throttling, circuit breakers, and emergency-stop evidence:
- Market data stale-feed protection:
- Trading venue, broker, and reporting provider controls:
- Clock synchronisation and timestamp precision:
- Immutable or tamper-evident audit trail:
- Retention, retrieval, replay, and legal hold:
- Evidence-safe logging:
- Access control and segregation of duties:
- Monitoring, metrics, traces, dashboards, and alerting:
- Incident, complaint, and client-impact escalation:
- Change control and release gates:
- Compliance evidence handoff:

## 7. Evidence Inventory

- Scope review or owner classification record:
- Architecture decision or product design record:
- Client classification evidence:
- Product governance or target-market evidence:
- Suitability or appropriateness evidence:
- Advice, disclosure, warning, or client report:
- Order event and execution evidence:
- Best-execution review evidence:
- Algorithm inventory:
- Algorithm testing, simulation, market replay, or stress evidence:
- Emergency-stop, throttle, or circuit-breaker test evidence:
- Clock synchronisation evidence:
- Retention, replay, reconstruction, or audit evidence:
- Transaction reporting or reconciliation evidence:
- Access-control policy:
- Monitoring dashboards:
- Alert routing:
- Incident, complaint, or escalation runbook:
- Change approval:
- Release decision:

## 8. Residual Risks

- Residual risk:
- Impact:
- Likelihood:
- Mitigation:
- Owner:
- Acceptance decision:
- Review date:

## 9. Release Decision

- Decision:
- Conditions:
- Blockers:
- Required approvals:
- Expiry or review date:
- Environments approved:
- Emergency stop path:
- Emergency rollback path:

## 10. Action Plan

| Priority | Action | Owner | Due date | Evidence expected | Status |
| -------- | ------ | ----- | -------- | ----------------- | ------ |
| High     |        |       |          |                   | Open   |
| Medium   |        |       |          |                   | Open   |
| Low      |        |       |          |                   | Open   |

## 11. Final Notes

- Items requiring legal interpretation:
- Items requiring investment-service or investment-firm classification:
- Items requiring jurisdiction or cross-border review:
- Items requiring client category, advice, suitability, or appropriateness decision:
- Items requiring best-execution or order-handling review:
- Items requiring algorithmic-trading governance, direct-electronic-access governance, or market-access governance review:
- Items requiring transaction reporting, record keeping, retention, or data-protection review:
- Items requiring security exception:
- Items requiring architecture decision:
- Items requiring trading, operations, risk, or compliance review:
- Items requiring product or business acceptance:
- Next review trigger:
