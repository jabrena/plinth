# EU Digital Omnibus Engineering Review Report

Use this report as engineering evidence for legal, compliance, privacy, security, risk, resilience, data-governance, AI governance, product, architecture, business-owner, and executive accountability review.

This report is not legal advice. It identifies proposal-stage Digital Omnibus simplification impacts, engineering controls, and escalation points from the reviewed system evidence. Legislative status, applicability, interpretation, adoption decisions, and any relaxation of regulation-specific controls require qualified owner review.

## 1. Review Context

- System or service name: CheckoutService delivery instructions change
- Repository, product, platform, or business service: Fictional Java Quarkus ecommerce platform
- Review date: 2026-06-14
- Reviewers: AI-assisted Digital Omnibus engineering review
- Business owner: Not identified in reviewed evidence
- Technical owner: Software engineers, tech leads, reviewers, and platform engineers are described, but no named CheckoutService owner is identified
- Legal/compliance owner: Not identified in reviewed evidence
- Privacy owner: Not identified in reviewed evidence
- Security owner: Not identified in reviewed evidence
- Risk or resilience owner: Not identified in reviewed evidence
- Data-governance owner: Not identified in reviewed evidence
- AI governance owner: Not identified in reviewed evidence
- Source materials reviewed:
  - `examples/diagrams/deployment/system-example-cicd-pr-model.md`
  - `examples/diagrams/deployment/expected-system-deployment.puml`
  - `examples/diagrams/deployment/checkout-service-feature-request.md`
  - `.agents/skills/809-regulations-eu-digital-omnibus/references/809-regulations-eu-digital-omnibus-source-summary.md`
  - `.agents/skills/809-regulations-eu-digital-omnibus/references/809-regulations-eu-digital-omnibus-engineering-examples.md`
  - `.agents/skills/809-regulations-eu-digital-omnibus/assets/reports/809-eu-digital-omnibus-engineering-review-report-template.md`

## 2. Source-Status Record

- Official source checked: https://commission.europa.eu/news-and-media/news/simpler-digital-rules-help-eu-businesses-grow-2025-11-19_en
- Official source checked: https://digital-strategy.ec.europa.eu/en/policies/digital-rulebook
- Source date or last update shown: Digital Rulebook source shows last update 7 May 2026; source summary records November 2025 Digital Omnibus proposal context
- Engineering review date: 2026-06-14
- Observed source-status wording: Digital Omnibus is treated as a Commission simplification package with proposed regulations; the Digital Rulebook source says the November Digital Omnibus proposal is being discussed by co-legislators
- Proposal-stage assumptions: No reviewed evidence confirms that simplification proposals are adopted or applicable to this ecommerce platform
- Source-status owner: Legal/compliance owner not identified
- Open source-status questions:
  - Has legal/compliance confirmed current Digital Omnibus legislative status for this organization?
  - Which proposed changes, if any, are approved for adoption in internal engineering workflows?
  - Can any incident, privacy, data, AI, or cybersecurity evidence be simplified without weakening existing controls?

## 3. Affected-Regulation Scope

- AI Act impact signals: No AI system is described in the CheckoutService feature request; AI governance impact remains a watch item for platform-level rulebook changes.
- GDPR or ePrivacy impact signals: The feature introduces free-text delivery instructions that may contain personal data or sensitive delivery details.
- DORA impact signals: The platform uses payment-adjacent and cloud-provider workflows, but financial-entity scope is not established by reviewed evidence.
- NIS2 impact signals: The reviewed system includes cloud-hosted production services, provider dependencies, incident evidence, Kafka contracts, and database migrations; NIS2 applicability is not established by reviewed evidence.
- Data Act or data-governance impact signals: The feature changes persisted order data and event contracts; data-governance records may need update.
- Cybersecurity reporting impact signals: Existing incident routing, breach reporting, and provider handoffs are not present in reviewed evidence.
- Platform-to-business or digital-platform impact signals: The system is an ecommerce platform, but P2B applicability is not established.
- Existing regulation skills reviewed: Digital Omnibus source summary states that `801`, `802`, `803`, and `804` remain regulation-specific authorities for their domains.
- Existing controls that must remain unchanged: GDPR privacy and breach controls, NIS2 cybersecurity incident controls, DORA resilience controls where applicable, AI Act controls where applicable, data-governance controls, release gates, and evidence retention.
- Open applicability or interpretation questions: Legislative status, affected regulation applicability, incident-reporting consolidation, data-rights workflow changes, and whether any report-template simplification can be adopted.

## 4. Potential Impact Or Non-Compliance Mapping

This section is not a legal finding. It lists concrete Digital Omnibus proposal-stage impact signals and engineering update candidates from the reviewed evidence. No violation is confirmed by this engineering review.

| Potential impact or non-compliance signal | Affected regulation area | Associated official source link | Evidence from reviewed system | Current status | Required owner review | Engineering action |
| ----------------------------------------- | ------------------------ | ------------------------------- | ----------------------------- | -------------- | --------------------- | ------------------ |
| Source status not recorded before workflow changes | Digital Omnibus source-status governance | https://digital-strategy.ec.europa.eu/en/policies/digital-rulebook | No legal-status or regulatory-change record is shown for CheckoutService release evidence | Potential gap | Legal / compliance | Add source-status record to release and governance evidence before any simplification-based workflow change |
| Incident-reporting consolidation assumed without approval | NIS2 / DORA / cybersecurity incident reporting | https://commission.europa.eu/news-and-media/news/simpler-digital-rules-help-eu-businesses-grow-2025-11-19_en | Logs, metrics, traces, dashboards, and alerts are described, but no severity model, incident workflow, reporting handoff, or owner approval is present | Update candidate | Security operations / resilience / legal / compliance | Identify single-entry reporting as a candidate only; preserve existing NIS2, DORA, GDPR, and provider handoffs until approved |
| Data-rights workflow impact from free-text delivery instructions | GDPR / ePrivacy / Data Act / data governance | https://digital-strategy.ec.europa.eu/en/policies/digital-rulebook | Feature adds `delivery_instruction_note` and requires sensitive free text to be excluded from Kafka events and logs | Potential gap | Privacy / legal / data-governance | Add privacy-safe logging tests, retention review, data-access controls, data-rights workflow impact review, and data inventory update |
| AI governance timeline watch item | AI Act | https://digital-strategy.ec.europa.eu/en/policies/digital-rulebook | No AI component is described for CheckoutService, but platform governance may track AI Act timeline changes from Digital Omnibus proposals | None identified | AI governance / legal / product owner | Record no direct AI impact for this change and route platform-level AI timeline updates to AI governance owners |
| Existing regulation-skill conclusion could be rewritten without owner decision | AI Act / GDPR / DORA / NIS2 / Data Act | https://digital-strategy.ec.europa.eu/en/policies/digital-rulebook | No owner decision exists to change GDPR, NIS2, DORA, AI Act, or data-governance controls for this release | Potential gap | Legal / compliance / security / privacy / risk | Preserve existing regulation-specific controls and add compatibility check before report templates or questionnaires are updated |
| Database migration and Kafka contract changes may need data-governance evidence updates | Data Act / data governance / cybersecurity change control | https://digital-strategy.ec.europa.eu/en/policies/digital-rulebook | Feature adds order columns and changes Kafka event schema to version 3 for delivery-related events | Update candidate | Data-governance / architecture / security | Link schema migration, event contract, rollback, downstream consumer, and data inventory evidence to the release record |

## 5. Engineering Controls

- Source-status checks: Add a Digital Omnibus source-status record with official URLs, observed status wording, review date, source-status owner, and proposal-stage assumptions.
- Affected-regulation mapping: Map the CheckoutService change to GDPR/ePrivacy, NIS2, DORA watch items, Data Act/data-governance, cybersecurity reporting, and AI Act watch items where evidence supports it.
- Evidence inventory updates: Update data inventory, Kafka event inventory, report-template inventory, incident runbook inventory, owner inventory, and release evidence inventory.
- Change-control impact records: Require explicit release evidence when a team relies on proposed simplification to modify privacy, incident, data, or governance controls.
- Questionnaire update candidates: Add candidate questions for Digital Omnibus source status, owner approval, affected-regulation mapping, and existing-skill compatibility.
- Report-template update candidates: Add affected regulation area and associated official source link columns to any Digital Omnibus impact report.
- Incident-reporting workflow consolidation: Treat single-entry reporting as a candidate only; preserve existing security, privacy, resilience, legal, and provider reporting handoffs.
- Data-rights workflow impact review: Review whether delivery instructions affect privacy notices, access controls, retention, deletion, support access, and data-subject or data-rights evidence.
- AI governance timeline review: Record no direct AI component in the reviewed feature; route platform-level AI Act timeline updates to AI governance owners.
- Existing regulation skill compatibility: Keep `801`, `802`, `803`, and `804` conclusions intact unless owners approve updated interpretation and evidence.
- Ambiguity escalation: Escalate proposal-stage ambiguity, missing source status, missing owner decisions, and any proposed control relaxation.

## 6. Evidence Inventory

- Official source-status record: Missing for this release; needs creation.
- Affected-regulation map: Missing as a dedicated artifact; raw evidence suggests GDPR/ePrivacy, NIS2, DORA watch items, Data Act/data governance, and cybersecurity reporting touchpoints.
- Existing regulation-skill outputs: No linked `801`, `802`, `803`, or `804` outputs are included in the release evidence.
- Policy or control inventory: Not present.
- Questionnaire inventory: Not present.
- Report-template inventory: Not present.
- Incident runbook: Not present.
- Breach or cybersecurity reporting workflow: Not present.
- Data-rights workflow: Not present.
- AI governance roadmap: No direct AI impact identified; roadmap not present.
- Provider or supply-chain review: Provider dependencies are described; review records are not present.
- Release gate or change-control evidence: Pull request review and checks are described; Digital Omnibus-specific change-control evidence is not present.
- Owner approval record: Not present.

## 7. Residual Risks

- Residual risk: Teams may treat Digital Omnibus simplification as approval to remove or weaken privacy, incident, cybersecurity, data-governance, or release evidence.
- Impact: Premature control relaxation, unresolved legal status, incomplete breach or incident handoff, and unsupported audit evidence.
- Likelihood: Medium, because the feature introduces privacy-sensitive free text and governance-impacting data changes.
- Mitigation: Add source-status record, affected-regulation mapping, owner approvals, and compatibility checks with regulation-specific skills.
- Owner: Legal/compliance, privacy, security, data-governance, resilience, and platform owners.
- Acceptance decision: Required before simplifying any workflow or report template.
- Review date: Before PR approval and before production deployment.

- Residual risk: Delivery instruction data may affect data-rights workflows, support tooling, logs, retention, and Kafka event governance.
- Impact: Personal data disclosure, over-broad data propagation, weak deletion or access evidence, and incident reconstruction gaps.
- Likelihood: Medium, because the feature explicitly introduces free text and event contract changes.
- Mitigation: Exclude notes from Kafka and logs, add field-level authorization, update data inventory, and review privacy and data-rights workflow impacts.
- Owner: CheckoutService owner, privacy owner, legal/compliance owner, security owner, and data-governance owner.
- Acceptance decision: Required before production release.
- Review date: During PR review and release readiness.

## 8. Adoption Or Release Decision

- Decision: Conditionally blocked for production until Digital Omnibus source-status evidence, affected-regulation mapping, and owner handoffs are added to the release record.
- Conditions:
  - Source-status record includes official links, review date, proposal-stage wording, and legal/compliance owner.
  - Affected-regulation map covers privacy, cybersecurity incident reporting, data governance, DORA/NIS2 watch items, AI Act watch item, and existing skill compatibility.
  - Privacy-safe logging, data inventory, Kafka contract, and migration evidence are attached.
  - Incident and breach reporting workflows remain unchanged unless approved by qualified owners.
  - Existing regulation-skill controls remain in force unless owner decisions approve updates.
- Blockers:
  - Missing named legal/compliance, privacy, security, data-governance, resilience, and AI governance owners.
  - Missing Digital Omnibus source-status record and affected-regulation map.
  - Missing owner decision for incident-reporting consolidation or report-template changes.
- Required approvals: Service owner, tech lead, legal/compliance owner, privacy owner, security owner, data-governance owner, resilience owner, platform owner, and AI governance owner for AI timeline assumptions.
- Expiry or review date: Before merge and again before production deployment.
- Environments or workflows approved: Development and test only until blockers are resolved.
- Rollback or reversion path: Revert any questionnaire, report-template, incident-workflow, data-rights, or release-gate change that was based on unapproved Digital Omnibus assumptions.

## 9. Action Plan

| Priority | Action | Owner | Due date | Evidence expected | Status |
| -------- | ------ | ----- | -------- | ----------------- | ------ |
| High | Create Digital Omnibus source-status record with official links, observed status, review date, and legal/compliance owner | Legal/compliance owner | Before PR approval | Source-status evidence linked to release | Open |
| High | Add affected-regulation map for GDPR/ePrivacy, NIS2, DORA watch items, Data Act/data governance, cybersecurity reporting, and AI Act watch item | Compliance / architecture owner | Before PR approval | Mapping table linked to release | Open |
| High | Preserve existing GDPR, NIS2, DORA, AI Act, and data-governance controls until owner decisions approve changes | Legal / privacy / security / risk owners | Before production deployment | Owner approval or no-change record | Open |
| High | Add privacy-safe logging, data inventory, retention, and data-rights workflow impact evidence for delivery instructions | Privacy / data-governance owner | Before merge | Tests, inventory update, workflow impact note | Open |
| Medium | Add incident-reporting consolidation candidate with current NIS2, DORA, GDPR, provider, and internal handoffs preserved | Security operations / resilience owner | Before production deployment | Runbook change candidate and owner decision | Open |
| Medium | Add report-template and questionnaire update candidates with affected regulation area and associated official source link columns | Compliance / documentation owner | Before production deployment | Candidate diff or backlog item | Open |
| Low | Record AI governance no-direct-impact decision and platform-level timeline watch item | AI governance owner | Before production deployment | AI governance note | Open |

## 10. Final Notes

- Items requiring legislative-status assessment: Whether Digital Omnibus proposals are adopted, amended, rejected, or still under discussion.
- Items requiring legal interpretation: Applicability to ecommerce checkout, incident-reporting consolidation, privacy workflow changes, and data-governance changes.
- Items requiring compliance review: Existing skill compatibility, report-template changes, questionnaire updates, and evidence inventory changes.
- Items requiring privacy review: Delivery instruction free text, logging, retention, access, deletion, support workflow, and breach handoff.
- Items requiring security or resilience review: Incident workflow consolidation, provider handoffs, alert evidence, and release gates.
- Items requiring data-governance review: Order schema changes, Kafka event contracts, data inventory, and downstream consumer evidence.
- Items requiring AI governance review: No direct AI impact found; platform-level AI Act timeline monitoring remains a governance task.
- Items requiring architecture decision: Kafka contract compatibility, database migration rollout, data propagation boundaries, and source-status release gate.
- Items requiring product or business acceptance: Any release delay from unresolved owner approvals or data-rights workflow changes.
- Next review trigger: PR approval request, production deployment request, Digital Omnibus source-status change, incident-workflow update, data-rights workflow update, report-template change, or regulation-specific skill update.
