# DORA Engineering Review Report

Use this template after the human has answered all four sections of `assets/questions/802-dora-engineering-review-questionnaire.md` and matching the relevant examples in `references/802-regulations-dora.md`.

This report is not legal advice. Use it as engineering evidence for legal, compliance, security, risk, resilience, procurement, business-continuity, architecture, and business-owner review.

The purpose of this report is to increase awareness of potential gaps in the system and create engineering evidence for qualified review. The response produced from this template does not represent legal advice, a legal opinion, or a final regulatory determination.

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

This section is not a legal finding. Use it to list concrete potential DORA violation or non-compliance signals from the reviewed evidence and route each item to qualified legal, compliance, security, risk, resilience, procurement, business-continuity, architecture, or business-owner review. When no violation is confirmed, say so explicitly and keep open items as potential gaps.

| Potential violation or non-compliance signal | DORA reference | Evidence from reviewed system | Current status | Required owner review | Engineering action |
| -------------------------------------------- | -------------- | ----------------------------- | -------------- | --------------------- | ------------------ |
| Applicability, regulated-service, or important-function uncertainty | Articles 1-2 | TBD | None identified / Potential gap / Confirmed concern | Legal / compliance / risk / business owner | TBD |
| Missing ICT risk-management framework, ownership, or asset inventory evidence | Chapter II / Articles 5-16 | TBD | None identified / Potential gap / Confirmed concern | Risk / resilience / security / platform | TBD |
| Missing incident classification, reporting, or evidence path | Chapter III / Articles 17-23 | TBD | None identified / Potential gap / Confirmed concern | Compliance / security / SRE / resilience | TBD |
| Missing digital operational resilience testing evidence | Chapter IV / Articles 24-27 | TBD | None identified / Potential gap / Confirmed concern | Resilience / SRE / QA / risk | TBD |
| Missing ICT third-party provider risk, contract, monitoring, or exit evidence | Chapter V / Articles 28-44 | TBD | None identified / Potential gap / Confirmed concern | Procurement / risk / legal / platform | TBD |
| Incorrect, incomplete, or unsupported operational-resilience information | Chapters VI-VII / Articles 45-56 | TBD | None identified / Potential gap / Confirmed concern | Legal / compliance / risk owner | TBD |

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
