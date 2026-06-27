# MiFID II Engineering Review Questionnaire

IMPORTANT: Use these questions as an evidence checklist. Complete answers from trusted local project evidence or maintainer-approved sanitized facts. Mark missing facts as `Unknown` instead of inventing answers.

Evidence rules:

1. Work through Question 1 through Question 20 in order.
2. Record the selected answer and the trusted evidence reference that supports it.
3. Use maintainer-approved sanitized facts only for gaps that local evidence does not answer.
4. Redact passwords, API keys, tokens, session IDs, private keys, connection strings, credentials, trading credentials, broker secrets, venue secrets, client confidential information, and confidential trading strategy details as `[REDACTED_SECRET]` or `[REDACTED_SENSITIVE]` before storing or repeating the answer. Record only the type, affected component, and control gap.
5. Mark unresolved items as `Unknown` and include them in the escalation section.
6. Do **not** start final classification or the engineering report until all 20 questions have an evidence-backed answer or an `Unknown` marker.
7. If evidence indicates investment advice, portfolio management, order-execution evidence, dealing on own account, algorithmic-trading governance, direct-electronic-access governance, retail-client impact, unclear client classification, missing suitability or appropriateness evidence, missing best-execution evidence, or transaction-reporting concerns, record it as a compliance escalation item before release recommendations.
8. Do **not** include raw secrets, credentials, tokens, keys, session IDs, private keys, connection strings, trading credentials, broker secrets, venue secrets, client confidential information, or confidential trading strategy in notes, evidence inventories, summaries, or reports.

The first review output after reading reference materials should summarize the trusted evidence sources and list any questionnaire items that remain `Unknown`.

---

Use this questionnaire before recommending controls for a Java enterprise system that supports investment services, investment activities, financial instruments, client onboarding, advisory workflows, portfolio management, order-handling evidence, execution evidence, algorithmic-trading governance, market-access governance, transaction evidence, or regulated record keeping.

**Purpose:**

- Resolve gaps about investment-service scope, financial instruments, clients, order-evidence flows, trading-venue evidence, algorithm governance, records, and owners.
- Identify MiFID II engineering signals without making legal conclusions.
- Map investment-service concerns to engineering controls and reviewable evidence.
- Decide which owners must review the system before production use.

This questionnaire is not legal advice. Escalate regulated-service classification, investment-firm status, jurisdiction, client-impact decisions, advice classification, best-execution interpretation, algorithmic trading duties, transaction reporting duties, and regulatory interpretation questions to qualified legal, compliance, risk, product, operations, trading, market-structure, data-protection, security, or accountable business owners.

---

## Section 1: Map Investment-Service Scope

Questions 1-5. Complete each item from trusted evidence or mark it `Unknown`.

**Question 1**: What type of MiFID II-relevant system or workflow is being reviewed?

Options:

- Client onboarding, classification, entitlement, or product-governance workflow
- Investment advice, recommendation, or suitability workflow
- Appropriateness, knowledge, experience, warning, or disclosure workflow
- Order reception, transmission, routing-evidence, execution-evidence, aggregation, allocation, cancellation, or correction workflow
- Portfolio management or discretionary management workflow
- Algorithmic-trading governance, smart-order-routing evidence, quoting evidence, hedging evidence, or market-making evidence workflow
- Trading venue, broker, market data, direct electronic access, or market-connectivity integration
- Transaction, audit, record-keeping, reporting, monitoring, or compliance evidence workflow
- No investment-service concern expected (specify)
- Other (specify)
- Unknown

**Question 2**: Which possible investment services or activities are in scope?

Options (select all that apply):

- Reception and transmission of orders
- Execution of orders on behalf of clients
- Dealing on own account
- Portfolio management
- Investment advice
- Underwriting or placement evidence for financial instruments
- Operation of a trading facility
- Ancillary services, custody, safekeeping, research, or foreign exchange connected to investment services
- No investment service or activity expected
- Unknown

**Question 3**: Which financial instruments or product types are in scope?

Options (select all that apply):

- Shares, bonds, exchange-traded funds, or transferable securities
- Money-market instruments
- Units in collective investment undertakings or funds
- Options, futures, swaps, forwards, or other derivatives
- Commodity derivatives, emission allowances, or related derivatives
- Structured products, contracts for difference, or leveraged products
- Crypto-asset or tokenized instrument that may require separate classification
- Product type is not documented
- Unknown

**Question 4**: Which client or counterparty categories are affected?

Options (select all that apply):

- Retail clients
- Professional clients
- Eligible counterparties
- Opt-up or opt-down client classification workflow
- Prospective clients or applicants
- Internal traders, advisers, portfolio managers, operations users, or support users
- No external client impact expected
- Client category is not documented
- Unknown

**Question 5**: Which owners have reviewed the investment-service scope?

Options (select all that apply):

- Product or business owner
- Technical owner or architect
- Compliance owner
- Legal owner
- Risk owner
- Trading or market-structure owner
- Operations owner
- Security or data-protection owner
- No owner review yet
- Unknown

---

## Section 2: Classify Client, Product, And Advice Signals

Questions 6-10. Complete each item from trusted evidence or mark it `Unknown`.

**Question 6**: Are client classification and entitlement decisions documented?

Options:

- Client classification is documented and owner-approved
- Client classification is derived from approved onboarding evidence
- Eligible-counterparty or professional-client treatment requires review
- Opt-up or opt-down workflow exists and is documented
- Entitlement rules exist but owner approval is unclear
- Client classification is not documented
- Not applicable
- Unknown

**Question 7**: Are suitability, appropriateness, or client-warning workflows involved?

Options (select all that apply):

- Suitability assessment for advice or portfolio management
- Appropriateness assessment for product access
- Knowledge and experience capture
- Investment objectives, risk tolerance, or loss capacity capture
- Warning or acknowledgement workflow
- Suitability report or client-facing recommendation record
- No suitability or appropriateness workflow expected
- Unknown

**Question 8**: Are product governance, target market, or distribution controls involved?

Options (select all that apply):

- Product target-market decision is documented
- Product restriction, channel restriction, or jurisdiction restriction is documented
- Product approval owner has reviewed the change
- Product catalog or entitlement service enforces restrictions
- Product governance evidence is missing or outdated
- No product governance concern expected
- Unknown

**Question 9**: Are client communications, disclosures, conflicts, inducements, or complaints involved?

Options (select all that apply):

- Client communication or disclosure content changes
- Conflict-of-interest evidence is required
- Inducement, fee, or cost disclosure evidence is required
- Complaint intake, routing, or evidence workflow is involved
- Client-facing record or report is generated
- No such workflow expected
- Unknown

**Question 10**: Which scope decisions require qualified owner interpretation before release?

Options (select all that apply):

- Regulated-service classification
- Investment-firm status
- Jurisdiction or cross-border service question
- Client category or client-impact decision
- Advice or personal recommendation classification
- Product target-market or distribution restriction
- Best-execution policy interpretation
- Algorithmic-trading or direct-electronic-access governance obligation
- Transaction reporting, record keeping, or retention interpretation
- No interpretation gap identified
- Unknown

---

## Section 3: Classify Order, Execution, And Trading Signals

Questions 11-15. Complete each item from trusted evidence or mark it `Unknown`.

**Question 11**: Does the system record or review order, execution, allocation, cancellation, or correction evidence?

Options (select all that apply):

- Order entry or order validation
- Order-routing evidence or execution-venue-selection evidence
- Aggregation or allocation
- Cancellation, amendment, correction, or bust workflow
- Execution confirmation or client report
- Transaction reconstruction or replay
- No order lifecycle workflow expected
- Unknown

**Question 12**: Is best-execution or client-instruction evidence required?

Options (select all that apply):

- Execution policy version is recorded
- Client instruction is captured
- Venue selection decision is recorded
- Execution quality metrics are captured
- Best-execution monitoring or review evidence exists
- Evidence is incomplete or owner approval is unclear
- Not applicable
- Unknown

**Question 13**: Are algorithmic-trading governance, high-frequency-trading evidence, market-making evidence, or direct-electronic-access governance involved?

Options (select all that apply):

- Algorithmic-trading governance logic
- Smart-order-routing evidence or automated-venue-selection evidence
- High-frequency trading technique signal
- Market making, quoting, hedging, or liquidity provision
- Direct electronic access or sponsored access
- Pre-trade-risk-control evidence, throttles, circuit breakers, or emergency-stop evidence
- No algorithmic-trading or direct-electronic-access governance expected
- Unknown

**Question 14**: Are trading venue, broker, market data, clearing, or reporting integration evidence involved?

Options (select all that apply):

- Regulated market, MTF, OTF, systematic internaliser, broker, or venue gateway
- Trading protocol, REST, messaging, or file-based integration evidence
- Market data feed or reference data feed
- Clearing, settlement, custody, or safekeeping integration
- Transaction reporting, publication, or reconciliation integration
- Third-party SaaS, broker, data vendor, or outsourced provider
- No external trading integration expected
- Unknown

**Question 15**: Which trading-control evidence is documented and tested?

Options (select all that apply):

- Pre-trade risk checks
- Order throttling or order-to-trade controls
- Circuit breaker or emergency-stop evidence
- Market data stale-feed protection
- Algorithm inventory and owner approval
- Simulation, market replay, stress, or failover tests
- Trading incident escalation and runbooks
- Controls are missing, untested, or ownerless
- Not applicable
- Unknown

---

## Section 4: Apply Evidence, Record-Keeping, And Release Controls

Questions 16-20. Complete each item from trusted evidence or mark it `Unknown`.

**Question 16**: Which audit and record-keeping evidence exists?

Options (select all that apply):

- Client classification, advice, suitability, or appropriateness records
- Order, execution, allocation, cancellation, or correction evidence records
- Transaction reporting or reconciliation evidence
- Algorithm deployment, approval, and monitoring records
- Clock synchronisation and timestamp precision evidence
- Immutable or tamper-evident audit trail
- Retention policy and retrieval or replay workflow
- Evidence is incomplete, mutable, or hard to retrieve
- Unknown

**Question 17**: Which observability and monitoring controls exist?

Options (select all that apply):

- Metrics, logs, traces, dashboards, and alerts for regulated workflows
- Evidence-safe logging with secret and personal-data redaction
- Trading-control, order-evidence, algorithm-governance, venue-evidence, or report-failure alerts
- Compliance or operations monitoring dashboard
- Incident escalation and post-incident review workflow
- Complaint or client-impact escalation workflow
- Monitoring is incomplete or ownerless
- Unknown

**Question 18**: Which change-control and release controls are required?

Options (select all that apply):

- Pull request review and protected branch controls
- Compliance, risk, trading, operations, or legal approval gate
- Algorithm inventory and deployment approval
- Database migration approval and rollback evidence
- Kafka, trading-protocol, API, file, or reporting schema compatibility check
- Feature flag, canary, rollback, or emergency stop control
- Production release record and owner signoff
- Direct-to-main change path or missing pre-merge review concern
- Unknown

**Question 19**: Which data-protection, secrecy, or confidentiality controls are needed?

Options (select all that apply):

- Personal data minimization and purpose limitation
- Client confidential information protection
- Trading strategy or algorithm confidentiality
- Broker, venue, or market data credential protection
- Access control and segregation of duties
- Evidence redaction for reports and tickets
- Retention, deletion, archive, or legal hold controls
- No additional controls expected
- Unknown

**Question 20**: What should block or condition release?

Options (select all that apply):

- Unclear regulated-service scope or investment-firm status
- Unclear client category, advice classification, or client-impact decision
- Missing suitability, appropriateness, product governance, or disclosure evidence
- Missing order-handling, best-execution, or client-instruction evidence
- Missing algorithmic-trading governance, market-access governance, emergency-stop, or risk-control evidence
- Missing timestamp, record-keeping, retention, replay, or audit evidence
- Missing compliance, legal, risk, trading, operations, security, or data-protection owner approval
- Critical controls are documented and release can proceed with conditions
- No blocker identified
- Unknown
