# EU AI Act Engineering Review Report

Use this template after the human has answered all four sections of `assets/questions/801-eu-ai-act-risk-questionnaire.md` and matching the relevant examples in `references/801-regulations-eu-ai-act.md`.

This report is not legal advice. Use it as engineering evidence for legal, compliance, privacy, security, risk, architecture, and business-owner review.

The purpose of this report is to increase awareness of potential gaps in the system and create engineering evidence for qualified review. The response produced from this template does not represent legal advice, a legal opinion, or a final regulatory determination.

## 1. Review Context

- System or capability name:
- Repository, service, product, or platform:
- Review date:
- Reviewers:
- Business owner:
- Technical owner:
- Legal/compliance owner:
- Privacy/security owner:
- Source materials reviewed:

## 2. Capability Summary

- Capability type:
- AI system, decision-support workflow, RAG system, generated-artifact pipeline, general-purpose model integration, or AI agent:
- Intended purpose:
- Primary users:
- Affected people or groups:
- Deployment geography:
- Environments in scope:
- Enterprise systems accessed:
- Tool actions available:
- Human-in-the-loop status:

## 3. Questionnaire Findings

- Material unanswered questions:
- Assumptions:
- Capability gaps:
- Risk-classification gaps:
- Engineering-control gaps:
- Owner or approval gaps:
- Release-readiness gaps:

## 4. EU AI Act Risk Classification

- Prohibited-practice signals:
- Annex III high-risk signals:
- Annex I product or sector signals:
- General-purpose AI model concerns:
- Sensitive data or regulated record concerns:
- Transparency obligation concerns:
- Real-world testing or sandbox concerns:
- Post-market monitoring concerns:
- Classification conclusion:
- Required escalation:

## 5. Potential Violation Or Non-Compliance Mapping

This section is not a legal finding. Use it to list concrete potential EU AI Act violation or non-compliance signals from the reviewed evidence and route each item to qualified legal, compliance, privacy, security, risk, or business-owner review. When no violation is confirmed, say so explicitly and keep open items as potential gaps.

| Potential violation or non-compliance signal | EU AI Act reference | Evidence from reviewed system | Current status | Required owner review | Engineering action |
| -------------------------------------------- | ------------------- | ----------------------------- | -------------- | --------------------- | ------------------ |
| Prohibited-practice signal | Chapter II / Article 5 | TBD | None identified / Potential gap / Confirmed concern | Legal / compliance / privacy / security | TBD |
| High-risk classification or missing high-risk evidence | Chapter III / Articles 6-49 / Annex III | TBD | None identified / Potential gap / Confirmed concern | Legal / compliance / risk / business owner | TBD |
| Missing transparency or AI-origin disclosure | Chapter IV / Article 50 | TBD | None identified / Potential gap / Confirmed concern | Legal / compliance / product owner | TBD |
| Missing general-purpose model documentation | Chapter V / Articles 51-56 / Annexes XI-XIII | TBD | None identified / Potential gap / Confirmed concern | Legal / compliance / model owner / security | TBD |
| Missing monitoring, incident, or corrective-action evidence | Chapter IX / Articles 72-94 | TBD | None identified / Potential gap / Confirmed concern | Legal / compliance / SRE / operations | TBD |
| Incorrect, incomplete, or unsupported regulatory information | Chapter XII / Articles 99-101 | TBD | None identified / Potential gap / Confirmed concern | Legal / compliance / risk owner | TBD |

## 6. Engineering Controls

- Human oversight and approval gates:
- Tool-access restrictions:
- Least-privilege controls:
- Audit evidence:
- Data governance:
- RAG source governance:
- Model/provider documentation:
- Prompt-injection and retrieval-safety controls:
- Testing and validation:
- Monitoring:
- Incident response:
- Rollback, disablement, withdrawal, or recall path:

## 7. Evidence Inventory

- Classification note:
- Risk management record:
- Technical documentation:
- Data lineage and source inventory:
- Model documentation:
- Test evidence:
- Security review:
- Privacy or data protection assessment:
- Fundamental rights impact assessment:
- Approval record:
- Monitoring dashboard or alert evidence:
- Operational runbook:

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
- Tool scopes approved:

## 10. Action Plan

| Priority | Action | Owner | Due date | Evidence expected | Status |
| -------- | ------ | ----- | -------- | ----------------- | ------ |
| High     |        |       |          |                   | Open   |
| Medium   |        |       |          |                   | Open   |
| Low      |        |       |          |                   | Open   |

## 11. Final Notes

- Items requiring legal interpretation:
- Items requiring architecture decision:
- Items requiring security exception:
- Items requiring product or business acceptance:
- Next review trigger:
