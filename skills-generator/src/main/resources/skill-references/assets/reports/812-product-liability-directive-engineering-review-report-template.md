# Product Liability Directive Engineering Review Report

Use this template after reviewing `references/812-regulations-eu-product-liability-directive-chapters-summary.md`, asking the needed questions from `assets/questions/812-product-liability-directive-engineering-review-questionnaire.md`, and matching the relevant examples from `references/812-regulations-eu-product-liability-directive-engineering-examples.md` for product-liability scope, generated instructions, AI-agent tool actions, automated updates, warnings, vulnerability handling, corrective updates, incident records, and release gates.

This report is not legal advice. Use it as engineering evidence for legal, compliance, product, product-safety, product-security, security, support, risk, architecture, executive accountability, and business-owner review.

The purpose of this report is to increase awareness of potential gaps in the system and create engineering evidence for qualified review. The response produced from this template does not represent legal advice, a legal opinion, a defectiveness determination, a compensable-damage determination, an economic-operator responsibility decision, or a final liability assessment.

Official source reference: Directive (EU) 2024/2853, `https://eur-lex.europa.eu/eli/dir/2024/2853/oj/eng`.

## 1. Review Context

- Product, service, component, related service, library, agent, plugin, or platform module:
- Repository or implementation path:
- Review date:
- Reviewers:
- Business owner:
- Product owner:
- Technical owner:
- Product safety owner:
- Product security owner:
- Support owner:
- Legal/compliance owner:
- Risk owner:
- Source materials reviewed:

## 2. Product-Liability Scope

- Product or component description:
- Possible software product signal:
- Possible related service signal:
- Possible generated-instruction signal:
- Possible AI-agent tool-action signal:
- Possible automated update, corrective update, or vulnerability handling signal:
- Intended purpose:
- Reasonably foreseeable use:
- Reasonably foreseeable misuse:
- Affected users, groups, operators, or consumers:
- Possible damage signals:
- Economic-operator role signals:
- Deployment geography:
- Environments in scope:
- Product versions, model versions, prompt versions, source versions, or update versions:
- Open applicability questions:

## 3. Engineering Evidence Reviewed

- Java applications, libraries, agents, plugins, APIs, jobs, or installers:
- Related services, product integrations, devices, APIs, queues, topics, caches, files, or remote processing:
- Prompts, RAG retrieval sources, source approval, model provenance, generated outputs, and evaluation records:
- AI-agent tool policies, tool action audit logs, preconditions, approvals, and rollback:
- Automated update, corrective update, vulnerability remediation, and advisory evidence:
- Warnings, assembly instructions, installation instructions, use instructions, maintenance instructions, support instructions, and generated instruction warnings:
- Hazard analysis, hazardous-scenario tests, foreseeable-misuse tests, and inter-connected product tests:
- Logging, monitoring, audit, incident, support, field report, and evidence reconstruction records:
- CI/CD workflows, build provenance, artifact signing, deployment paths, and release approvals:
- Owner handoffs and qualified review records:

## 4. Potential Violation Or Non-Compliance Mapping

This section is not a legal finding. Use it to list concrete potential Product Liability Directive violation or non-compliance signals from the reviewed evidence and route each item to qualified legal, compliance, product, product-safety, product-security, security, support, risk, architecture, or executive accountability review. When no violation is confirmed, say so explicitly and keep open items as potential gaps. Use the chapter and article references from `references/812-regulations-eu-product-liability-directive-chapters-summary.md`; add official-source links when one finding spans multiple Product Liability Directive areas.

| Potential violation or non-compliance signal | Product Liability Directive reference area | Associated official-source link | Evidence from reviewed system | Current status | Required owner review | Engineering action |
| -------------------------------------------- | ------------------------------------------ | ------------------------------- | ----------------------------- | -------------- | --------------------- | ------------------ |
| Unclear software product, related service, component, market placement, putting into service, substantial modification, or economic-operator role | Scope and definitions | Chapter I, Articles 2 and 4, https://eur-lex.europa.eu/eli/dir/2024/2853/oj/eng | TBD | None identified / Potential gap / Confirmed concern | Legal / compliance / product / safety owner | TBD |
| Missing intended use, reasonably foreseeable use, foreseeable misuse, user group, inter-connected product, or safety-purpose evidence | Defectiveness assessment circumstances | Chapter II, Article 7, https://eur-lex.europa.eu/eli/dir/2024/2853/oj/eng | TBD | None identified / Potential gap / Confirmed concern | Product / product-safety / architecture owner | TBD |
| Missing generated instruction, RAG source, model provenance, AI-agent tool-action, learning behavior, or human oversight evidence | Software, AI behavior, instructions, learning, and manufacturer control | Chapter I, Articles 4 and 7; Chapter II, Article 11, https://eur-lex.europa.eu/eli/dir/2024/2853/oj/eng | TBD | None identified / Potential gap / Confirmed concern | Product / safety / AI governance / legal owner | TBD |
| Missing warnings, assembly, installation, use, maintenance, generated instruction, limitation, support, or update guidance | Presentation, labelling, warnings, and instructions | Chapter II, Article 7, https://eur-lex.europa.eu/eli/dir/2024/2853/oj/eng | TBD | None identified / Potential gap / Confirmed concern | Product / safety / support / legal owner | TBD |
| Missing automated update, corrective update, rollback, vulnerability handling, safety update, or user notification evidence | Software updates, lack of safety updates, cybersecurity-relevant safety requirements, and interventions | Chapter II, Articles 7, 11, and 13, https://eur-lex.europa.eu/eli/dir/2024/2853/oj/eng | TBD | None identified / Potential gap / Confirmed concern | Product safety / product security / support owner | TBD |
| Missing incident, support, audit, disclosure-ready, trade-secret-safe, or understandable evidence reconstruction | Disclosure of evidence, burden of proof, and technical complexity | Chapter II, Articles 9 and 10, https://eur-lex.europa.eu/eli/dir/2024/2853/oj/eng | TBD | None identified / Potential gap / Confirmed concern | Legal / compliance / safety / support owner | TBD |
| Missing long-lived product version, component, supplier, support, update, substantial-modification, or release records | Multiple operators, recourse, limitation, and expiry periods | Chapter III, Articles 12-17, https://eur-lex.europa.eu/eli/dir/2024/2853/oj/eng | TBD | None identified / Potential gap / Confirmed concern | Legal / compliance / product / risk owner | TBD |

## 5. Engineering Controls

- Product-liability scope inventory:
- Intended use and reasonably foreseeable use documentation:
- Foreseeable misuse and hazardous-scenario testing:
- Product-safety requirements and cybersecurity-relevant safety controls:
- Generated instruction governance:
- RAG source governance:
- Prompt and model provenance:
- AI-agent tool-action allowlist, approvals, and rollback:
- Human oversight and support escalation:
- Automated update safety assessment:
- Corrective update and rollback procedure:
- Vulnerability intake, triage, advisory, and safety impact assessment:
- Warnings and instruction versioning:
- User notification and support guidance:
- Incident, field report, and near-miss evidence:
- Audit logging and evidence reconstruction:
- Sensitive-data, trade-secret, and exploit-detail redaction:
- Release readiness and owner approvals:

## 6. Evidence Inventory

- Product-liability scope inventory:
- Product classification or economic-operator handoff:
- Hazard analysis:
- Hazardous-scenario test evidence:
- Generated instruction test evidence:
- RAG source approval evidence:
- Prompt, model, retrieval, and tool policy versions:
- AI-agent action audit evidence:
- Human approval records:
- Warning and instruction review:
- Automated update and rollback evidence:
- Corrective update evidence:
- Vulnerability handling and advisory evidence:
- Incident or support evidence:
- Disclosure-ready evidence package:
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
- Emergency corrective update path:
- Emergency rollback path:
- User notification path:

## 9. Action Plan

| Priority | Action | Owner | Due date | Evidence expected | Status |
| -------- | ------ | ----- | -------- | ----------------- | ------ |
| High     |        |       |          |                   | Open   |
| Medium   |        |       |          |                   | Open   |
| Low      |        |       |          |                   | Open   |

## 10. Final Notes

- Items requiring legal interpretation:
- Items requiring defectiveness, compensable-damage, or causal-link review:
- Items requiring product classification or economic-operator role decision:
- Items requiring jurisdiction, limitation-period, expiry-period, or transitional-scope review:
- Items requiring disclosure, trade-secret, or confidentiality review:
- Items requiring product-safety exception:
- Items requiring product-security or vulnerability handling review:
- Items requiring architecture decision:
- Items requiring support, corrective update, recall, or field intervention review:
- Items requiring product or business acceptance:
- Next review trigger:
