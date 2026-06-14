# DORA Engineering Review Report

Use this template after reviewing `references/802-regulations-dora-chapters-summary.md`, the human has answered all four sections of `assets/questions/802-dora-engineering-review-questionnaire.md`, and the relevant examples from `references/802-regulations-dora-engineering-examples.md` have been matched.

This report is not legal advice. Use it as engineering evidence for legal, compliance, security, risk, resilience, procurement, business-continuity, architecture, and business-owner review.

The purpose of this report is to increase awareness of potential gaps in the system and create engineering evidence for qualified review. The response produced from this template does not represent legal advice, a legal opinion, or a final regulatory determination.

Do not include raw secrets, credentials, passwords, API keys, tokens, session IDs, private keys, or connection strings in this report. Replace secret values with `[REDACTED_SECRET]` and document only the secret type, affected component, evidence location, owner, and remediation needed.

## 1. Review Context

- System, service, product, or platform:
- Repository or implementation path:
- Review date:
- Reviewers:
- Business owner:
- Technical owner:
- Operational owner:
- Security/risk owner:
- Resilience or business-continuity owner:
- Procurement or vendor owner:
- Source materials reviewed:

## 2. Operational Scope Summary

- Business service supported:
- Financial entity, important business service, or critical ICT context:
- Primary users:
- Affected customers, operations, or downstream systems:
- Environments in scope:
- Deployment topology:
- Data stores and stateful components:
- Message brokers, jobs, schedulers, or batch processes:
- Third-party ICT providers:
- Recovery expectations:

## 3. Questionnaire Findings

- Material unanswered questions:
- Assumptions:
- Ownership gaps:
- ICT inventory gaps:
- Incident-readiness gaps:
- Recovery or continuity gaps:
- Third-party provider gaps:
- Release-readiness gaps:

## 4. DORA Operational Resilience Classification

- Financial-entity or regulated-service signals:
- Important business service signals:
- Critical ICT function signals:
- Third-party ICT provider signals:
- Incident reporting or regulator handoff concerns:
- Outsourcing or provider criticality concerns:
- Applicability conclusion for governance review:
- Required escalation:

## 5. Potential Violation Or Non-Compliance Mapping

This section is not a legal finding. Use it to list concrete potential DORA violation or non-compliance signals from the reviewed evidence and route each item to qualified legal, compliance, security, risk, resilience, procurement, business-continuity, architecture, or business-owner review. When no violation is confirmed, say so explicitly and keep open items as potential gaps. Use the chapter links from `references/802-regulations-dora-chapters-summary.md`; add more official-source links when one finding spans multiple DORA areas.

| Potential violation or non-compliance signal | DORA reference area | Associated official-source link | Redacted evidence from reviewed system | Current status | Required owner review | Engineering action |
| -------------------------------------------- | ------------------- | ------------------------------- | ------------------------------------- | -------------- | --------------------- | ------------------ |
| Applicability, regulated-service, or important-function uncertainty | General provisions / scope | [Chapter I](https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32022R2554#cpt_I) | TBD | None identified / Potential gap / Confirmed concern | Legal / compliance / risk / business owner | TBD |
| Missing ICT risk-management framework, ownership, or asset inventory evidence | ICT risk management / Articles 5-16 | [Chapter II](https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32022R2554#cpt_II) | TBD | None identified / Potential gap / Confirmed concern | Risk / resilience / security / platform | TBD |
| Missing incident classification, reporting, or evidence path | ICT-related incident management and reporting / Articles 17-23 | [Chapter III](https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32022R2554#cpt_III) | TBD | None identified / Potential gap / Confirmed concern | Compliance / security / SRE / resilience | TBD |
| Missing digital operational resilience testing evidence | Digital operational resilience testing / Articles 24-27 | [Chapter IV](https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32022R2554#cpt_IV) | TBD | None identified / Potential gap / Confirmed concern | Resilience / SRE / QA / risk | TBD |
| Missing ICT third-party provider risk, contract, monitoring, or exit evidence | ICT third-party risk management / Articles 28-44 | [Chapter V](https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32022R2554#cpt_V) | TBD | None identified / Potential gap / Confirmed concern | Procurement / risk / legal / platform | TBD |
| Incorrect, incomplete, or unsupported operational-resilience information | Information sharing / supervision and enforcement / Articles 45-56 | [Chapter VI](https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32022R2554#cpt_VI), [Chapter VII](https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32022R2554#cpt_VII) | TBD | None identified / Potential gap / Confirmed concern | Legal / compliance / risk owner | TBD |

## 6. Engineering Controls

- ICT asset and dependency inventory:
- Operational ownership and support model:
- Monitoring, alerting, and observability:
- Evidence-safe logging and traceability:
- Incident detection and severity classification:
- Incident escalation and communication:
- Backup and restore controls:
- RTO/RPO and recovery evidence:
- Continuity, failover, rollback, or manual workaround:
- Resilience testing and incident drills:
- Change-control evidence:
- Third-party ICT provider controls:
- Exit, portability, or provider-failure controls:

## 7. Evidence Inventory

Only include redacted evidence references. Do not paste raw request payloads, logs, screenshots, configuration values, credentials, tokens, keys, or connection strings.

- ICT inventory:
- Architecture or dependency diagram:
- Runbooks:
- Monitoring dashboards:
- Alert rules:
- Incident workflow:
- Backup policy:
- Restore test evidence:
- Failover or continuity test evidence:
- Change records:
- Provider contracts, SLAs, or support contacts:
- Provider monitoring or service-health evidence:
- Risk acceptance or exception records:
- Approval records:

## 8. Residual Risks

- Residual risk:
- Impact:
- Likelihood:
- Mitigation:
- Owner:
- Acceptance decision:

## 9. Release Decision

- Decision:
- Conditions:
- Blockers:
- Required approvals:
- Expiry or review date:
- Environments approved:
- Operational constraints:
- Provider constraints:

## 10. Action Plan

| Priority | Action | Owner | Due date | Evidence expected | Status |
| -------- | ------ | ----- | -------- | ----------------- | ------ |
| High     |        |       |          |                   | Open   |
| Medium   |        |       |          |                   | Open   |
| Low      |        |       |          |                   | Open   |

## 11. Final Notes

- Items requiring legal interpretation:
- Items requiring compliance or risk decision:
- Items requiring resilience or business-continuity decision:
- Items requiring security exception:
- Items requiring provider or procurement review:
- Items requiring architecture decision:
- Items requiring product or business acceptance:
- Next review trigger:
