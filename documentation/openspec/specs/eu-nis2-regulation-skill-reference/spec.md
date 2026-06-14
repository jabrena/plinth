# eu-nis2-regulation-skill-reference Specification

## Purpose
TBD - created by archiving change add-eu-nis2-regulation-skill. Update Purpose after archive.
## Requirements
### Requirement: NIS2 regulation skill

The repository MUST define `804-regulations-eu-nis2` as the EU NIS2 cybersecurity skill for Java enterprise engineering review.

#### Scenario: NIS2 skill identifier is standardized

- **GIVEN** maintainers implement NIS2 guidance in generator sources
- **WHEN** they create or reference the skill in XML, inventories, OpenSpec artifacts, or generated local skill output
- **THEN** the identifier is `804-regulations-eu-nis2`
- **AND** the official source reference is `https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32022L2555`

#### Scenario: NIS2 scope maps cybersecurity concerns to engineering controls

- **GIVEN** a user asks to review a Java enterprise system for NIS2 concerns
- **WHEN** the `804-regulations-eu-nis2` skill is applied
- **THEN** the guidance frames findings as engineering controls rather than legal advice
- **AND** it addresses cybersecurity risk management, asset and service inventory, dependency mapping, secure configuration, vulnerability handling, logging and monitoring, incident detection and escalation, backup and recovery, business continuity, supply-chain security, access control, cryptography, secure development, and change control
- **AND** it recommends escalation to legal, compliance, security, risk, resilience, business-continuity, or executive accountability owners for entity classification, member-state applicability, incident-reporting obligations, and regulatory interpretation
- **AND** it reports conclusions and actions using the NIS2 engineering review report template

### Requirement: Relationship to other regulation skills

The NIS2 skill MUST complement existing and planned regulation skills without changing their workflows.

#### Scenario: Select NIS2 for cybersecurity risk-management concerns

- **GIVEN** a Java enterprise system may involve AI, privacy, resilience, product security, platform, and cybersecurity concerns
- **WHEN** an agent chooses regulation guidance
- **THEN** `801-regulations-eu-ai-act` is used for EU AI systems, AI agents, RAG, LLM workflows, tool calling, or model-driven decision support
- **AND** `802-regulations-dora` is used for EU ICT risk, operational resilience, financial-entity, critical ICT service, or third-party ICT provider concerns
- **AND** `803-regulations-gdpr` is used for EU personal-data processing, privacy controls, data-subject rights, data transfers, retention, or breach-preparation concerns
- **AND** `804-regulations-eu-nis2` is used for EU cybersecurity risk-management, critical-sector, essential-entity, important-entity, managed-service-provider, supply-chain security, or incident-escalation concerns
- **AND** multiple regulation skills may be used together when the same Java system crosses those concern boundaries

### Requirement: Generator registration

The NIS2 skill source MUST be registered in the generator inventory so local skill generation emits it.

#### Scenario: Register NIS2 regulation skill

- **WHEN** `skills-generator/src/main/resources/skills.xml` is inspected
- **THEN** skill id `804` declares explicit skill id `804-regulations-eu-nis2`
- **AND** skill id `804` registers reference `804-regulations-eu-nis2-chapters-summary`
- **AND** skill id `804` registers reference `804-regulations-eu-nis2-engineering-examples`
- **AND** skill id `804` packages `assets/reports/804-nis2-engineering-review-report-template.md`

#### Scenario: Generate local NIS2 skill

- **WHEN** `./mvnw clean install -pl skills-generator` is run
- **THEN** generated local skill output includes `.agents/skills/804-regulations-eu-nis2/SKILL.md`
- **AND** generated local skill output includes `.agents/skills/804-regulations-eu-nis2/references/804-regulations-eu-nis2-chapters-summary.md`
- **AND** generated local skill output includes `.agents/skills/804-regulations-eu-nis2/references/804-regulations-eu-nis2-engineering-examples.md`
- **AND** generated local skill output includes `.agents/skills/804-regulations-eu-nis2/assets/reports/804-nis2-engineering-review-report-template.md`
- **AND** generated references contain no unresolved include markers or broken local reference paths

### Requirement: Acceptance scenarios

The NIS2 skill MUST have Gherkin acceptance scenarios for the same delivery modes used by adjacent regulation skills.

#### Scenario: Validate NIS2 PR-based delivery review scenario

- **WHEN** `skills-generator/src/test/resources/gherkin/804-regulations-eu-nis2.feature` is inspected
- **THEN** it includes an acceptance-test scenario for a Java system developed and released through the described CI/CD pull-request pipeline
- **AND** the requested report output path is under `examples/regulations/nis2`
- **AND** the scenario expects the skill to read `references/804-regulations-eu-nis2-chapters-summary.md`, `references/804-regulations-eu-nis2-engineering-examples.md`, and `assets/reports/804-nis2-engineering-review-report-template.md`

#### Scenario: Validate NIS2 direct-to-main delivery review scenario

- **WHEN** `skills-generator/src/test/resources/gherkin/804-regulations-eu-nis2.feature` is inspected
- **THEN** it includes an acceptance-test scenario for a Java system committed directly to main and released through the described CI/CD pipeline
- **AND** the requested report output path is under `examples/regulations/nis2`
- **AND** the scenario expects the skill to escalate missing pre-merge review, protected-main bypass, cybersecurity risk-management gaps, and ambiguous NIS2 applicability to qualified owners

### Requirement: Acceptance report evidence

The NIS2 skill MUST include reviewable example reports produced from the acceptance scenarios.

#### Scenario: Validate pull-request delivery NIS2 report evidence

- **WHEN** `examples/regulations/nis2/NIS2-ENGINEERING-REVIEW-REPORT.md` is inspected
- **THEN** it identifies the reviewed pull-request based CI/CD system description, deployment diagram, feature request, generated chapters summary, generated engineering examples, and report template as source materials
- **AND** it scopes service context, possible essential or important entity signals, sector signals, owners, assets, data stores, IAM, CI/CD, providers, runtime dependencies, monitoring, alerting, and operational runbooks
- **AND** it maps potential NIS2 violation or non-compliance signals to directive topic areas without asserting a legal finding
- **AND** it recommends engineering controls for asset inventory, secure configuration, vulnerability management, incident escalation, evidence-safe logging, monitoring, backup, restore, continuity, rollback, supply-chain security, access control, cryptography, database migration approval, Kafka schema compatibility, and change approval
- **AND** it routes entity classification, member-state applicability, incident-reporting obligations, cybersecurity risk acceptance, and regulatory interpretation to qualified owners

#### Scenario: Validate direct-to-main NIS2 report evidence

- **WHEN** `examples/regulations/nis2/NIS2-DIRECT-MAIN-ENGINEERING-REVIEW-REPORT.md` is inspected
- **THEN** it identifies the reviewed direct-to-main CI/CD system description, deployment diagram, feature request, generated chapters summary, generated engineering examples, and report template as source materials
- **AND** it maps direct-to-main cybersecurity risk signals including missing pre-merge review, protected-main bypass, ownerless assets, incomplete dependency evidence, missing incident escalation, untested continuity, database migration risk, Kafka message contract risk, and production side-effect risk
- **AND** it recommends controls for pre-commit review, main-branch protection, cybersecurity change-control evidence, asset inventory, secure configuration, vulnerability management, incident escalation, backup and restore verification, supply-chain monitoring, access control, cryptography, migration approval, Kafka compatibility, and release approval
- **AND** it blocks or conditions production release until direct-to-main delivery has equivalent cybersecurity review evidence and qualified owner handoffs

### Requirement: Source and generated-output boundaries

The implementation MUST edit XML sources and validate generated local skill output without directly editing generated legacy or release outputs.

#### Scenario: Preserve generated-output ownership

- **WHEN** implementation files are reviewed
- **THEN** `.cursor/rules/` is not edited directly
- **AND** public `skills/` release output is not edited manually
- **AND** public `skills/` is refreshed only through the release profile when release output is intentionally in scope

