# EU Data Act Engineering Review Report

Use this template after reviewing `../../references/806-regulations-eu-data-act-chapters-summary.md` and matching the relevant examples from `../../references/806-regulations-eu-data-act-engineering-examples.md` for data inventory, access authorization, portability APIs, export formats, metadata, audit logs, cloud switching, non-personal data safeguards, trade-secret handoffs, data-sharing request workflows, contract evidence, and operational controls.

This report is not legal advice. Use it as engineering evidence for legal, compliance, privacy, data governance, security, product, procurement, cloud, risk, provider, architecture, and business-owner review.

The purpose of this report is to increase awareness of potential gaps in the system and create engineering evidence for qualified review. The response produced from this template does not represent legal advice, a legal opinion, or a final regulatory determination.

## 1. Review Context

- System or service name:
- Repository, product, platform, or business service:
- Review date:
- Reviewers:
- Business owner:
- Product owner:
- Technical owner:
- Data owner:
- Privacy owner:
- Security owner:
- Cloud or provider owner:
- Legal/compliance owner:
- Source materials reviewed:

## 2. Data Act Scope Context

- Service description:
- Possible connected-product or related-service signal:
- Possible data holder signal:
- Possible user, data recipient, or third-party recipient signal:
- Possible data processing service or cloud-switching signal:
- Possible data-space, interoperability, or smart-contract signal:
- Deployment geography:
- Environments in scope:
- Critical users, customers, or affected organizations:
- Production dependencies:
- Open applicability questions:

## 3. Data Access And Portability Scope

- Applications, APIs, jobs, and batch workloads:
- Datasets, metadata, schemas, and catalog entries:
- Data stores, queues, topics, object stores, and caches:
- Product data, related service data, exportable data, and digital assets:
- Personal data, non-personal data, mixed datasets, trade secrets, or commercially sensitive data:
- IAM, access-control rules, secrets, keys, and privileged operations:
- Data-sharing request workflows and support operations:
- Export, portability, and interoperability mechanisms:
- Cloud-switching, provider-exit, and erasure workflows:
- Monitoring, alerting, audit logs, and evidence systems:

## 4. Potential Violation Or Non-Compliance Mapping

This section is not a legal finding. Use it to list concrete potential EU Data Act violation or non-compliance signals from the reviewed evidence and route each item to qualified legal, compliance, privacy, data governance, security, product, procurement, cloud, risk, provider, or business-owner review. When no violation is confirmed, say so explicitly and keep open items as potential gaps. Use the official-source chapter links from `../../references/806-regulations-eu-data-act-chapters-summary.md`; add more official-source links when one finding spans multiple Data Act areas.

| Potential violation or non-compliance signal | EU Data Act reference area | Associated official-source link | Evidence from reviewed system | Current status | Required owner review | Engineering action |
| -------------------------------------------- | -------------------------- | ------------------------------- | ----------------------------- | -------------- | --------------------- | ------------------ |
| Unclear Data Act applicability or role scope | Applicability / role classification | [Chapter I](https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32023R2854#cpt_I) | TBD | None identified / Potential gap / Confirmed concern | Legal / compliance / data governance / product owner | TBD |
| Missing data inventory or metadata evidence | User access / metadata / interoperability | [Chapter II](https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32023R2854#cpt_II), [Chapter VIII](https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32023R2854#cpt_VIII) | TBD | None identified / Potential gap / Confirmed concern | Data governance / architecture / product owner | TBD |
| Weak access authorization or request workflow | User access / third-party sharing | [Chapter II](https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32023R2854#cpt_II) | TBD | None identified / Potential gap / Confirmed concern | Security / privacy / legal / data owner | TBD |
| Missing machine-readable export or portability evidence | Portability / exportable data | [Chapter II](https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32023R2854#cpt_II), [Chapter VI](https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32023R2854#cpt_VI) | TBD | None identified / Potential gap / Confirmed concern | Product / data governance / architecture / cloud owner | TBD |
| Incomplete trade-secret or sensitive-data handoff | Trade secrets / confidentiality / technical protection | [Chapter II](https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32023R2854#cpt_II), [Chapter III](https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32023R2854#cpt_III) | TBD | None identified / Potential gap / Confirmed concern | Legal / product / data governance / security | TBD |
| Missing cloud-switching support or exportable data register | Switching between data processing services | [Chapter VI](https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32023R2854#cpt_VI) | TBD | None identified / Potential gap / Confirmed concern | Cloud owner / provider owner / procurement / legal | TBD |
| Weak non-personal data and international access safeguards | International access / non-personal data | [Chapter VII](https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32023R2854#cpt_VII) | TBD | None identified / Potential gap / Confirmed concern | Legal / compliance / security / cloud owner | TBD |
| Incomplete contract or complaint evidence | Contract terms / enforcement / complaints | [Chapter IV](https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32023R2854#cpt_IV), [Chapter IX](https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32023R2854#cpt_IX) | TBD | None identified / Potential gap / Confirmed concern | Legal / procurement / product / business owner | TBD |

## 5. Engineering Controls

- Data inventory and owner map:
- Access authorization and entitlement checks:
- Data-sharing request workflow:
- User and recipient role evidence:
- Purpose limitation and request minimization:
- Portability APIs:
- Machine-readable export formats:
- Metadata, schema, vocabulary, and catalog evidence:
- Interoperability and quality-of-service evidence:
- Audit logs and evidence-safe logging:
- Cloud-switching and provider-exit support:
- Exportable data and digital asset register:
- Secure transfer and erasure controls:
- Non-personal data safeguards:
- Mixed personal and non-personal data privacy handoff:
- Trade-secret or commercially sensitive data handoff:
- Contract and compensation evidence:
- Complaint, dispute, refusal, or suspension routing:
- Release gate and change-control evidence:

## 6. Evidence Inventory

- Data inventory:
- Metadata catalog:
- API contract:
- Export format specification:
- Schema or event contract:
- Access-control tests:
- Request workflow evidence:
- Audit-log evidence:
- Trade-secret review:
- Privacy or mixed-dataset review:
- Non-personal data classification:
- International access safeguard evidence:
- Cloud-switching runbook:
- Exportable data register:
- Erasure evidence:
- Contract or terms evidence:
- Complaint or dispute workflow:
- Release decision:

## 7. Residual Risks

- Residual risk:
- Impact:
- Likelihood:
- Mitigation:
- Owner:
- Acceptance decision:
- Review date:

## 8. Release Decision

- Decision:
- Conditions:
- Blockers:
- Required approvals:
- Expiry or review date:
- Environments approved:
- Emergency rollback path:
- Data access or export disablement path:

## 9. Action Plan

| Priority | Action | Owner | Due date | Evidence expected | Status |
| -------- | ------ | ----- | -------- | ----------------- | ------ |
| High     |        |       |          |                   | Open   |
| Medium   |        |       |          |                   | Open   |
| Low      |        |       |          |                   | Open   |

## 10. Final Notes

- Items requiring legal interpretation:
- Items requiring data-governance decision:
- Items requiring privacy review:
- Items requiring security exception:
- Items requiring architecture decision:
- Items requiring cloud or provider review:
- Items requiring procurement or contract review:
- Items requiring product or business acceptance:
- Next review trigger:
