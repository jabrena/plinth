# Market Abuse Regulation Engineering Review Report

Use this template after reviewing `../../references/811-regulations-eu-market-abuse-regulation-chapters-summary.md`, completing `../questions/811-market-abuse-regulation-engineering-review-questionnaire.md`, and matching the relevant examples from `../../references/811-regulations-eu-market-abuse-regulation-engineering-examples.md` for suspicious order and transaction monitoring, market-data lineage, insider-list workflows, disclosure workflows, alert explainability, model and rule provenance, reviewer decisions, false-positive handling, investigation records, and compliance evidence handoff.

This report is not legal advice. Use it as engineering evidence for legal, compliance, market-surveillance, product, risk, operations, data, security, audit, executive accountability, architecture, and business-owner review.

The purpose of this report is to increase awareness of potential gaps in the system and create engineering evidence for qualified review. The response produced from this template does not represent legal advice, a legal opinion, an insider dealing determination, an unlawful disclosure determination, a market manipulation determination, a suspicious order or transaction reportability decision, a jurisdiction decision, or a final regulatory determination.

## 1. Review Context

- System or service name:
- Repository, product, platform, or business service:
- Review date:
- Reviewers:
- Business owner:
- Technical owner:
- Product owner:
- Market-surveillance owner:
- Data owner:
- Security owner:
- Risk owner:
- Operations owner:
- Legal/compliance owner:
- Source materials reviewed:

## 2. MAR Scope Context

- Service description:
- Possible MAR scope signal:
- Financial instruments, venues, benchmarks, commodities, or emission allowances:
- Trading, order, transaction, quote, or market-data journeys:
- Issuer, disclosure, insider-list, market-sounding, or PDMR workflows:
- Deployment geography:
- Environments in scope:
- Platform dependencies:
- Open applicability questions:

## 3. Market-Surveillance Engineering Scope

- Applications, APIs, jobs, and batch workloads:
- Data stores, queues, topics, indexes, and caches:
- Order, transaction, quote, and market-data feeds:
- Rule engines, scenario definitions, thresholds, and model versions:
- AI, ML, GenAI, or scoring workflows:
- Alert triage, reviewer decisions, false-positive handling, and escalation:
- Investigation records and STOR handoff workflows:
- Insider-list, inside-information access, and disclosure workflows:
- IAM, secrets, keys, and privileged operations:
- CI/CD workflows and deployment paths:
- Monitoring, alerting, and observability systems:
- Documentation, compliance reports, and owner handoff records:

## 4. Questionnaire Summary

- Evidence sources used:
- Questions answered from trusted evidence:
- Questions marked `Unknown`:
- High-priority escalations:
- Redactions applied:

## 5. Potential Violation Or Non-Compliance Mapping

This section is not a legal finding. Use it to list concrete potential Market Abuse Regulation violation or non-compliance signals from the reviewed evidence and route each item to qualified legal, compliance, market-surveillance, product, risk, operations, data, security, audit, or executive accountability review. When no violation is confirmed, say so explicitly and keep open items as potential gaps. Use the official-source links from `../../references/811-regulations-eu-market-abuse-regulation-chapters-summary.md`; add more links when one finding spans multiple MAR areas.

| Potential violation or non-compliance signal | MAR reference area | Associated official-source link | Evidence from reviewed system | Current status | Required owner review | Engineering action |
| -------------------------------------------- | ------------------ | ------------------------------- | ----------------------------- | -------------- | --------------------- | ------------------ |
| Unclear financial-instrument, venue, issuer, benchmark, commodity, emission allowance, or jurisdiction scope | Subject matter / scope / definitions | [Chapter 1](https://eur-lex.europa.eu/eli/reg/2014/596/oj/eng) | TBD | None identified / Potential gap / Confirmed concern | Legal / compliance / market surveillance / product | TBD |
| Missing or weak suspicious order and transaction monitoring evidence | Prevention and detection of market abuse / STOR arrangements | [Chapter 2](https://eur-lex.europa.eu/eli/reg/2014/596/oj/eng) | TBD | None identified / Potential gap / Confirmed concern | Market surveillance / compliance / operations / technology | TBD |
| Missing market manipulation scenario coverage or alert explainability | Market manipulation / prohibition of market manipulation | [Chapter 2](https://eur-lex.europa.eu/eli/reg/2014/596/oj/eng) | TBD | None identified / Potential gap / Confirmed concern | Market surveillance / compliance / risk / model owner | TBD |
| Missing insider dealing, inside-information access, or unlawful disclosure controls | Inside information / insider dealing / unlawful disclosure | [Chapter 2](https://eur-lex.europa.eu/eli/reg/2014/596/oj/eng) | TBD | None identified / Potential gap / Confirmed concern | Legal / compliance / market surveillance / security | TBD |
| Missing disclosure, delayed disclosure, insider-list, or managers' transaction evidence | Disclosure requirements / insider lists / managers' transactions | [Chapter 3](https://eur-lex.europa.eu/eli/reg/2014/596/oj/eng) | TBD | None identified / Potential gap / Confirmed concern | Legal / compliance / issuer disclosure / operations | TBD |
| Missing model or rule provenance, threshold approval, or rollback evidence | Article 16 arrangements / surveillance governance / change control | [Chapter 2](https://eur-lex.europa.eu/eli/reg/2014/596/oj/eng) | TBD | None identified / Potential gap / Confirmed concern | Market surveillance / model risk / compliance / technology | TBD |
| Missing reviewer decision trail, false-positive rationale, investigation record, or STOR handoff | Detection, investigation, and compliance evidence handoff | [Chapter 2](https://eur-lex.europa.eu/eli/reg/2014/596/oj/eng), [Chapter 4](https://eur-lex.europa.eu/eli/reg/2014/596/oj/eng) | TBD | None identified / Potential gap / Confirmed concern | Market surveillance / compliance / legal / audit | TBD |
| Incomplete evidence safety, secrecy, data protection, or competent-authority response controls | Competent authorities / professional secrecy / data protection | [Chapter 4](https://eur-lex.europa.eu/eli/reg/2014/596/oj/eng) | TBD | None identified / Potential gap / Confirmed concern | Legal / compliance / security / data protection / risk | TBD |
| Direct-to-main, emergency, or unreviewed production change affecting surveillance evidence | Change control / sanctions and governance exposure | [Chapter 5](https://eur-lex.europa.eu/eli/reg/2014/596/oj/eng) | TBD | None identified / Potential gap / Confirmed concern | Compliance / market surveillance / technology / executive accountability | TBD |

## 6. Engineering Controls

- Suspicious order and transaction monitoring coverage:
- Market-data lineage and replayability:
- Alert explainability:
- Rule provenance and threshold governance:
- Model provenance, evaluation, approval, and rollback:
- Reviewer decisions and false-positive handling:
- Investigation records and STOR handoff:
- Inside-information access controls:
- Insider-list workflow evidence:
- Disclosure and delayed-disclosure workflow evidence:
- PDMR or managers' transaction workflow evidence:
- Access control and least privilege:
- Evidence-safe logging:
- Monitoring, metrics, traces, and alerting:
- Documentation and runbooks:
- Change control and release gates:
- Compliance evidence handoff:

## 7. Evidence Inventory

- MAR scope and owner map:
- Instrument, venue, issuer, benchmark, commodity, or emission allowance inventory:
- Order, transaction, quote, and market-data lineage:
- Scenario, rule, threshold, and model inventory:
- Alert explanation samples:
- Reviewer decision and false-positive records:
- Investigation case and STOR handoff records:
- Insider-list records:
- Disclosure workflow records:
- Access-control policy:
- Monitoring dashboards:
- Alert routing:
- Test, backtest, replay, or shadow-mode evidence:
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
- Emergency rollback path:

## 10. Action Plan

| Priority | Action | Owner | Due date | Evidence expected | Status |
| -------- | ------ | ----- | -------- | ----------------- | ------ |
| High     |        |       |          |                   | Open   |
| Medium   |        |       |          |                   | Open   |
| Low      |        |       |          |                   | Open   |

## 11. Final Notes

- Items requiring legal interpretation:
- Items requiring market-surveillance decision:
- Items requiring compliance decision:
- Items requiring product or business decision:
- Items requiring model risk or data decision:
- Items requiring security exception:
- Items requiring architecture decision:
- Items requiring audit or executive accountability review:
- Next review trigger:
