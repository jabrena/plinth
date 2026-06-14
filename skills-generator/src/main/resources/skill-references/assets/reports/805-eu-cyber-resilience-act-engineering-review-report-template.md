# EU Cyber Resilience Act Engineering Review Report

Use this template after reviewing `references/805-regulations-eu-cyber-resilience-act-chapters-summary.md` and matching the relevant examples from `references/805-regulations-eu-cyber-resilience-act-engineering-examples.md` for product security scope, secure-by-design controls, vulnerability handling, coordinated disclosure, security updates, SBOM evidence, product documentation, support-period signaling, and release gates.

This report is not legal advice. Use it as engineering evidence for legal, compliance, product, product-security, security, support, market-access, risk, architecture, executive accountability, and business-owner review.

The purpose of this report is to increase awareness of potential gaps in the system and create engineering evidence for qualified review. The response produced from this template does not represent legal advice, a legal opinion, a conformity assessment, a CE marking decision, or a final regulatory determination.

## 1. Review Context

- Product, service, component, library, agent, plugin, or platform module:
- Repository or implementation path:
- Review date:
- Reviewers:
- Business owner:
- Product owner:
- Technical owner:
- Product security owner:
- Support owner:
- Legal/compliance owner:
- Market-access or conformity owner:
- Source materials reviewed:

## 2. Product Security Scope

- Product or component description:
- Possible product-with-digital-elements signal:
- Possible remote data processing signal:
- Possible important or critical product signal:
- Intended purpose:
- Reasonably foreseeable use or misuse:
- Economic-operator role signals:
- Deployment geography:
- Environments in scope:
- Users, integrators, or affected organizations:
- Support-period signal:
- Security update delivery path:
- Open applicability questions:

## 3. Engineering Evidence Reviewed

- Java applications, libraries, agents, plugins, APIs, jobs, or installers:
- Data stores, queues, topics, caches, files, or remote processing:
- Authentication, authorization, IAM, secrets, keys, and privileged operations:
- CI/CD workflows, build provenance, artifact signing, and deployment paths:
- Third-party components, open-source dependencies, containers, and generated clients:
- SBOM, dependency scan, image scan, and vulnerability triage evidence:
- Threat model, secure-by-design, secure defaults, and attack-surface evidence:
- Logging, monitoring, security event, and incident evidence:
- Coordinated disclosure, vulnerability contact, advisory, and reporting handoff evidence:
- Security update, rollback, and support-period evidence:
- Product security documentation and user instructions:

## 4. Potential Violation Or Non-Compliance Mapping

This section is not a legal finding. Use it to list concrete potential Cyber Resilience Act violation or non-compliance signals from the reviewed evidence and route each item to qualified legal, compliance, product, product-security, security, support, market-access, risk, architecture, or executive accountability review. When no violation is confirmed, say so explicitly and keep open items as potential gaps. Use the chapter links from `references/805-regulations-eu-cyber-resilience-act-chapters-summary.md`; add more official-source links when one finding spans multiple CRA areas.

| Potential violation or non-compliance signal | CRA reference area | Associated official-source link | Evidence from reviewed system | Current status | Required owner review | Engineering action |
| -------------------------------------------- | ------------------ | ------------------------------- | ----------------------------- | -------------- | --------------------- | ------------------ |
| Unclear product-with-digital-elements scope, important product category, critical product category, or economic-operator role | Scope / product categories / economic-operator obligations | Chapter I, Chapter II | TBD | None identified / Potential gap / Confirmed concern | Legal / compliance / product / market-access owner | TBD |
| Missing secure-by-design, threat model, secure default, authentication, authorization, cryptography, or sensitive-data-safe logging evidence | Essential cybersecurity requirements for product properties | Chapter I, Annex I | TBD | None identified / Potential gap / Confirmed concern | Product security / architecture / security owner | TBD |
| Missing vulnerability handling, coordinated disclosure, advisory, user notification, or Article 14 reporting handoff evidence | Manufacturer obligations / reporting / vulnerability handling | Chapter II, Annex I | TBD | None identified / Potential gap / Confirmed concern | Product security / legal / compliance / support owner | TBD |
| Missing secure update mechanism, rollback, automatic update default, update advisory, or update availability evidence | Security updates / vulnerability remediation / user instructions | Chapter II, Annex II | TBD | None identified / Potential gap / Confirmed concern | Product security / support / platform owner | TBD |
| Missing dependency, third-party component, open-source due diligence, SBOM, or vulnerability triage evidence | Component due diligence / SBOM / technical documentation | Chapter II, Annex VII | TBD | None identified / Potential gap / Confirmed concern | Product security / platform / procurement / risk owner | TBD |
| Missing product security documentation, secure operation instructions, support-period disclosure, end-of-support signaling, or secure decommissioning guidance | User information / technical documentation / support period | Annex II, Annex VII | TBD | None identified / Potential gap / Confirmed concern | Product / support / legal / market-access owner | TBD |
| Missing conformity assessment, EU declaration, CE marking, or market-surveillance evidence handoff | Conformity / CE marking / technical documentation / market surveillance | Chapter III, Chapter V | TBD | None identified / Potential gap / Confirmed concern | Legal / compliance / market-access / product owner | TBD |

## 5. Engineering Controls

- Product security scope inventory:
- Secure-by-design development:
- Threat modeling:
- Secure defaults and hardening:
- Attack-surface reduction:
- Authentication and authorization:
- Cryptography and key ownership:
- Data minimization and secure data removal:
- Sensitive-data-safe logging and monitoring:
- Vulnerability intake and triage:
- Coordinated vulnerability disclosure:
- Security advisory and user notification:
- Security update mechanism:
- Rollback and update availability:
- Dependency and SBOM evidence:
- Third-party component and open-source due diligence:
- Product security documentation:
- Secure installation, operation, update, and decommissioning instructions:
- Support-period and end-of-support signaling:
- Release readiness and owner approvals:

## 6. Evidence Inventory

- Product security scope inventory:
- Product classification or economic-operator handoff:
- Threat model:
- Secure default configuration:
- Authentication and authorization review:
- Cryptography review:
- Logging and monitoring review:
- Vulnerability disclosure policy:
- Vulnerability contact address:
- Vulnerability scan or triage evidence:
- Security advisory evidence:
- Security update and rollback evidence:
- Dependency or SBOM evidence:
- Technical documentation:
- User instructions:
- Support-period rationale:
- End-of-support notice:
- Conformity or CE marking owner handoff:
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
- Environments or product versions approved:
- Emergency security update path:
- Emergency rollback path:

## 9. Action Plan

| Priority | Action | Owner | Due date | Evidence expected | Status |
| -------- | ------ | ----- | -------- | ----------------- | ------ |
| High     |        |       |          |                   | Open   |
| Medium   |        |       |          |                   | Open   |
| Low      |        |       |          |                   | Open   |

## 10. Final Notes

- Items requiring legal interpretation:
- Items requiring product classification or economic-operator role decision:
- Items requiring conformity assessment or CE marking review:
- Items requiring Article 14 reporting interpretation:
- Items requiring product security exception:
- Items requiring architecture decision:
- Items requiring support or end-of-support review:
- Items requiring procurement, supplier, or open-source steward review:
- Items requiring product or business acceptance:
- Next review trigger:
