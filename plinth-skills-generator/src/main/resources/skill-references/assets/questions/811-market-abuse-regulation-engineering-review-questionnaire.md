# Market Abuse Regulation Engineering Review Questionnaire

IMPORTANT: Use these questions as an evidence checklist. Complete answers from trusted local project evidence or maintainer-approved sanitized facts. Mark missing facts as `Unknown` instead of inventing answers.

Evidence rules:

1. Work through Question 1 through Question 22 in order.
2. Record the selected answer and the trusted evidence reference that supports it.
3. Use maintainer-approved sanitized facts only for gaps that local evidence does not answer.
4. Redact passwords, API keys, tokens, session IDs, private keys, connection strings, credentials, inside information values, client identifiers, trading strategy details, and investigation-sensitive content as `[REDACTED_SECRET]` or `[REDACTED_SENSITIVE]` before storing or repeating the answer. Record only the type, affected component, and control gap.
5. Mark unresolved items as `Unknown` and include them in the escalation section.
6. Do **not** start final classification or the engineering report until all 22 questions have an evidence-backed answer or an `Unknown` marker.
7. If evidence indicates production trading impact without owner review, missing suspicious order or transaction monitoring, unreviewed alert suppression, missing insider-list control, or unmanaged inside-information access, record a high-priority escalation before release recommendations.
8. Do **not** include raw secrets, credentials, tokens, keys, session IDs, private keys, connection strings, inside information, client identifiers, trading strategy details, or investigation-sensitive content in notes, evidence inventories, summaries, or reports.

The first review output after reading reference materials should summarize the trusted evidence sources and list any questionnaire items that remain `Unknown`.

Use this questionnaire before recommending controls for a Java enterprise system that supports trading, order routing, transaction monitoring, market surveillance, disclosure workflows, insider-list workflows, AI-assisted market-abuse detection, alert triage, or investigation evidence.

**Purpose:**

- Resolve gaps about MAR scope, trading flows, data sources, surveillance coverage, disclosure workflows, insider-list workflows, reviewer decisions, and owners.
- Identify Market Abuse Regulation engineering signals without making legal conclusions.
- Map market-surveillance concerns to engineering controls and reviewable evidence.
- Decide which owners must review the system before production use.

This questionnaire is not legal advice. Escalate insider dealing classification, unlawful disclosure classification, market manipulation classification, suspicious order or transaction reportability, disclosure-delay decisions, market-sounding interpretation, accepted market practices, sanctions, jurisdiction, and regulatory interpretation to qualified legal, compliance, market-surveillance, risk, product, operations, data, security, audit, or accountable business owners.

---

## Section 1: Map Market-Surveillance Scope

Questions 1-6. Complete each item from trusted evidence or mark it `Unknown`.

**Question 1**: What type of MAR-relevant capability is being reviewed?

Options:

- Order management, execution, or routing service
- Transaction monitoring or suspicious order and transaction surveillance
- Market-data ingestion, enrichment, or replay
- Issuer disclosure or delayed-disclosure workflow
- Insider-list or inside-information access workflow
- Market-sounding workflow
- PDMR or managers' transaction notification support
- AI, ML, GenAI, or rule-based market-abuse detection
- Investigation case management or regulator evidence export
- Conventional software with no MAR-relevant capability identified
- Other (specify)
- Unknown

**Question 2**: Which instruments, markets, or assets are in scope?

Options (select all that apply):

- Financial instruments admitted to trading or requested for admission to trading
- Regulated market, MTF, OTF, or related trading venue
- Derivatives, OTC instruments, or instruments linked to traded instruments
- Benchmarks
- Spot commodity contracts linked to financial instruments
- Emission allowances or auctioned products
- Issuer securities or debt instruments
- No instrument or venue scope identified
- Other (specify)
- Unknown

**Question 3**: Which events or data flows does the system process?

Options (select all that apply):

- Orders, cancellations, amendments, quotes, or indications of interest
- Executions, allocations, transactions, or positions
- Market data, reference data, benchmarks, or instrument master data
- News, disclosures, issuer events, or inside-information workflow records
- Insider-list entries or access-control events
- Alerts, cases, reviewer decisions, false-positive reasons, or STOR handoff records
- Model, rule, threshold, feature, or training data lineage
- Logs, metrics, traces, recordings, chat, email, or voice evidence
- Unknown

**Question 4**: Which owners have reviewed or should review the capability?

Options (select all that apply):

- Business owner
- Technical owner
- Product owner
- Market-surveillance owner
- Compliance owner
- Legal owner
- Risk owner
- Operations owner
- Data owner
- Security owner
- Audit owner
- No owner review yet
- Unknown

**Question 5**: What is the deployment and operational geography?

Options (select all that apply):

- EU trading venue, issuer, branch, customer, or market participant connection
- Third-country system affecting EU-traded instruments or EU market participants
- Cross-border data, surveillance, investigation, or regulator evidence flow
- Production trading environment
- Non-production or shadow-mode environment
- No EU or MAR-relevant nexus identified
- Unknown or unclear territorial connection

**Question 6**: Which release path is expected?

Options:

- Pull request with code review, CI/CD, and controlled release
- Direct-to-main change with CI/CD
- Emergency change or break-glass release
- Configuration-only rule or threshold change
- Model retraining, model promotion, or feature-set change
- Data migration, schema migration, or Kafka contract change
- Manual operational change
- Unknown

---

## Section 2: Classify Surveillance and Evidence Signals

Questions 7-14. Complete each item from trusted evidence or mark it `Unknown`.

**Question 7**: Does the system detect or route suspicious order or transaction signals?

Options:

- Yes, rule-based detection
- Yes, ML or GenAI-assisted detection
- Yes, hybrid rule and model detection
- Yes, manual reviewer intake only
- No suspicious order or transaction monitoring identified
- Unknown

**Question 8**: Which potential market-abuse signal families are covered?

Options (select all that apply):

- Insider dealing or suspicious trading around inside information
- Unlawful disclosure indicators
- Market manipulation through false or misleading signals
- Spoofing, layering, quote stuffing, wash trades, or matched orders
- Marking the close, ramping, abusive squeeze, or price positioning
- Benchmark manipulation
- Cross-venue or cross-instrument manipulation
- Dissemination of false or misleading information
- Coverage is not documented
- Unknown

**Question 9**: Is alert explainability available to reviewers?

Options:

- Scenario, rule version, thresholds, factors, and market-data lineage are visible
- Model version, top factors, feature-set version, and confidence are visible
- Free-text GenAI summaries are used with source references and reviewer controls
- Reviewer sees only a score or label
- No reviewer-facing explanation exists
- Unknown

**Question 10**: Are model and rule provenance controls documented?

Options (select all that apply):

- Rule source and rule version
- Threshold change history
- Feature-set version and feature definitions
- Training data lineage and evaluation evidence
- Model version, model card, and approval record
- Shadow-mode or backtesting evidence
- Rollback plan
- No provenance controls identified
- Unknown

**Question 11**: Are reviewer decisions and false-positive outcomes preserved?

Options (select all that apply):

- Reviewer identity and role
- Decision timestamp
- Evidence references reviewed
- False-positive reason code
- Escalation decision or STOR handoff status
- Supervisor or compliance approval where required
- Retention policy
- Decisions can be changed without audit trail
- Unknown

**Question 12**: Are inside-information and insider-list controls in scope?

Options (select all that apply):

- Inside-information project or event inventory
- Access-control evidence
- Insider-list entries with identity, reason, access time, and update time
- Written acknowledgement workflow
- Retention of insider-list records
- Disclosure-delay decision evidence
- No inside-information or insider-list controls in scope
- Unknown

**Question 13**: Are disclosure, managers' transaction, or market-sounding workflows in scope?

Options (select all that apply):

- Public disclosure of inside information
- Delayed disclosure workflow
- PDMR or managers' transaction notification support
- Investment recommendation or statistics dissemination
- Market-sounding workflow
- Media or public dissemination workflow
- No disclosure or market-sounding workflow in scope
- Unknown

**Question 14**: Are data protection, secrecy, and evidence safety controls documented?

Options (select all that apply):

- Least-privilege access to surveillance and investigation records
- Redaction of personal data, client data, inside information, and business secrets
- Encryption and key-management evidence
- Retention and legal hold controls
- Audit trail for evidence export
- Cross-border transfer review
- No evidence-safety controls identified
- Unknown

---

## Section 3: Review Implementation Evidence

Questions 15-19. Complete each item from trusted evidence or mark it `Unknown`.

**Question 15**: Which implementation artifacts are available for review?

Options (select all that apply):

- Java services, controllers, handlers, repositories, or batch jobs
- SQL migrations, schemas, database views, or retention jobs
- Kafka topics, event schemas, consumers, or producers
- Rule definitions, model configuration, feature definitions, or prompt templates
- Dashboards, metrics, traces, logs, alerts, or runbooks
- Tests, backtesting reports, model evaluations, or quality gates
- Architecture documentation, ADRs, release records, or owner approvals
- Unknown

**Question 16**: Which production side effects can the change cause?

Options (select all that apply):

- Changes order, transaction, or market-data ingestion
- Changes alert generation, suppression, scoring, ranking, or assignment
- Changes reviewer state transitions or case lifecycle
- Changes insider-list, disclosure, or market-sounding evidence
- Changes regulator-facing report or export content
- Changes data retention, deletion, redaction, or evidence access
- Changes only documentation with no runtime effect
- Unknown

**Question 17**: Which validation evidence exists?

Options (select all that apply):

- Unit tests
- Integration tests
- Contract tests for events or APIs
- Backtesting or replay against historical market data
- Shadow-mode comparison
- False-positive and false-negative review
- Security and access-control tests
- Operational readiness or runbook test
- No validation evidence identified
- Unknown

**Question 18**: Which observability controls exist?

Options (select all that apply):

- Alert volume, latency, error, and coverage metrics
- Data freshness, missing interval, and feed quality metrics
- Rule/model drift or threshold-change dashboards
- Reviewer queue and decision metrics
- Escalation and STOR handoff metrics
- Audit event monitoring
- Incident alerting
- No observability controls identified
- Unknown

**Question 19**: Which change-control evidence exists?

Options (select all that apply):

- Pull request review
- Protected branch or release approval
- Configuration approval
- Rule or threshold approval
- Model risk or model governance approval
- Compliance or market-surveillance approval
- Database migration or event-contract approval
- Rollback plan
- Direct-to-main or unreviewed change
- Unknown

---

## Section 4: Owner Handoff and Release Readiness

Questions 20-22. Complete each item from trusted evidence or mark it `Unknown`.

**Question 20**: Which questions require qualified owner review before release?

Options (select all that apply):

- Whether conduct is insider dealing
- Whether conduct is unlawful disclosure
- Whether conduct is market manipulation
- Whether an alert is reportable as a suspicious order or transaction
- Whether disclosure can be delayed
- Whether a market sounding or accepted market practice applies
- Whether jurisdiction or venue scope is confirmed
- Whether sanctions, regulator reporting, or enforcement response may apply
- No qualified owner review need identified
- Unknown

**Question 21**: What release decision is supported by evidence?

Options:

- Ready for production with documented owner approvals
- Ready for non-production or shadow-mode only
- Conditional release with blockers tracked
- Not ready because critical MAR evidence is missing
- Not ready because qualified owner review is pending
- Not ready because direct-to-main or unreviewed production change risk exists
- Unknown

**Question 22**: What follow-up evidence must be produced?

Options (select all that apply):

- Scope and owner map
- Market-data lineage and coverage report
- Alert explainability evidence
- Rule or model provenance record
- Reviewer decision and false-positive report
- Insider-list or disclosure workflow evidence
- Observability dashboard
- Test, backtest, replay, or shadow-mode evidence
- Release approval and rollback record
- Owner handoff or compliance review note
- Unknown
