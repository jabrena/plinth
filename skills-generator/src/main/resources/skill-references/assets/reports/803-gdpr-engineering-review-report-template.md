# GDPR Engineering Review Report

Use this template after the human has answered all four sections of `assets/questions/803-gdpr-engineering-review-questionnaire.md` and matching the relevant examples in `references/803-regulations-gdpr.md`.

This report is not legal advice. Use it as engineering evidence for legal, privacy, data protection officer, compliance, security, risk, architecture, and business-owner review.

The purpose of this report is to increase awareness of potential gaps in the system and create engineering evidence for qualified review. The response produced from this template does not represent legal advice, a legal opinion, or a final regulatory determination.

## 1. Review Context

- System, service, product, or platform:
- Repository or implementation path:
- Review date:
- Reviewers:
- Business owner:
- Technical owner:
- Privacy owner or data protection officer:
- Legal/compliance owner:
- Security/risk owner:
- Data governance owner:
- Source materials reviewed:

## 2. Personal-Data Processing Summary

- Processing type:
- Data subjects:
- Personal-data categories:
- Special-category or high-sensitivity data:
- Processing purposes:
- Primary users:
- Affected people or groups:
- Environments in scope:
- Data stores and derived stores:
- Vendors, processors, or subprocessors:
- Data transfer context:
- Rights workflows in scope:

## 3. Questionnaire Findings

- Material unanswered questions:
- Assumptions:
- Personal-data inventory gaps:
- Purpose or lawful-basis handoff gaps:
- Controller/processor role gaps:
- Minimization gaps:
- Rights workflow gaps:
- Retention or deletion gaps:
- Transfer or vendor-review gaps:
- Breach-readiness gaps:
- Release-readiness gaps:

## 4. GDPR Privacy Risk Classification

- Personal-data processing signals:
- Special-category, criminal offence, children's, or vulnerable-person data signals:
- Profiling, automated decision, or rights-impacting signals:
- Controller, processor, subprocessor, or joint-controller concerns:
- Third-country transfer or vendor-processing concerns:
- DPIA or privacy-review concerns:
- Security-of-processing concerns:
- Breach-response concerns:
- Classification conclusion for governance review:
- Required escalation:

## 5. Potential Violation Or Non-Compliance Mapping

This section is not a legal finding. Use it to list concrete potential GDPR violation or non-compliance signals from the reviewed evidence and route each item to qualified legal, privacy, data protection officer, compliance, security, risk, data-governance, architecture, vendor, or business-owner review. When no violation is confirmed, say so explicitly and keep open items as potential gaps.

| Potential violation or non-compliance signal | GDPR reference | Evidence from reviewed system | Current status | Required owner review | Engineering action |
| -------------------------------------------- | -------------- | ----------------------------- | -------------- | --------------------- | ------------------ |
| Missing lawful-basis, purpose-limitation, minimization, or transparency evidence | Articles 5-6 and Articles 12-14 | TBD | None identified / Potential gap / Confirmed concern | Legal / privacy / DPO / business owner | TBD |
| Missing personal-data records, controller/processor, vendor, or role evidence | Articles 28 and 30 | TBD | None identified / Potential gap / Confirmed concern | Legal / privacy / procurement / data governance | TBD |
| Missing privacy by design/default or field-level access controls | Article 25 | TBD | None identified / Potential gap / Confirmed concern | Privacy / security / architecture / product | TBD |
| Missing data-subject rights, retention, or deletion propagation evidence | Articles 15-22 | TBD | None identified / Potential gap / Confirmed concern | Privacy / DPO / product / platform | TBD |
| Missing security-of-processing or privacy-safe observability evidence | Article 32 | TBD | None identified / Potential gap / Confirmed concern | Security / privacy / SRE / risk | TBD |
| Missing breach-response, affected-data, or notification-handoff evidence | Articles 33-34 | TBD | None identified / Potential gap / Confirmed concern | Security / privacy / DPO / legal | TBD |
| Missing DPIA, transfer, or vendor-processing evidence | Article 35 and Chapter V / Articles 44-49 | TBD | None identified / Potential gap / Confirmed concern | Legal / privacy / DPO / procurement | TBD |

## 6. Engineering Controls

- Data inventory and data-flow evidence:
- Data minimization and DTO design:
- Purpose-specific processing controls:
- Field-level authorization and least privilege:
- Pseudonymization, tokenization, redaction, or masking:
- Privacy-safe logging and observability:
- Retention and deletion controls:
- Cache, index, export, and backup handling:
- Data-subject rights workflows:
- Transfer or vendor-review evidence:
- Security-of-processing controls:
- Breach detection, containment, and evidence:
- Testing and validation:

## 7. Evidence Inventory

- Personal-data inventory:
- Data-flow diagram:
- Purpose and lawful-basis handoff:
- DPIA or privacy review:
- Security review:
- Access-control evidence:
- Data-subject rights workflow:
- Retention policy:
- Deletion job evidence:
- Deletion propagation evidence:
- Transfer or vendor review:
- Breach response runbook:
- Monitoring dashboard or alert evidence:
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
- Data categories approved:
- Vendor or transfer constraints:

## 10. Action Plan

| Priority | Action | Owner | Due date | Evidence expected | Status |
| -------- | ------ | ----- | -------- | ----------------- | ------ |
| High     |        |       |          |                   | Open   |
| Medium   |        |       |          |                   | Open   |
| Low      |        |       |          |                   | Open   |

## 11. Final Notes

- Items requiring legal interpretation:
- Items requiring privacy or data protection officer decision:
- Items requiring security exception:
- Items requiring data governance decision:
- Items requiring vendor or transfer review:
- Items requiring architecture decision:
- Items requiring product or business acceptance:
- Next review trigger:
