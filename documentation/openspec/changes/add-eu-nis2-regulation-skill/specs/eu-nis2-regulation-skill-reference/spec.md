## ADDED Requirements

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
- **THEN** skill id `804` registers reference `804-regulations-eu-nis2`

#### Scenario: Generate local NIS2 skill

- **WHEN** `./mvnw clean install -pl skills-generator` is run
- **THEN** generated local skill output includes `.agents/skills/804-regulations-eu-nis2/SKILL.md`
- **AND** generated references contain no unresolved include markers or broken local reference paths

### Requirement: Source and generated-output boundaries

The implementation MUST edit XML sources and validate generated local skill output without directly editing generated legacy or release outputs.

#### Scenario: Preserve generated-output ownership

- **WHEN** implementation files are reviewed
- **THEN** `.cursor/rules/` is not edited directly
- **AND** public `skills/` release output is not edited manually
- **AND** public `skills/` is refreshed only through the release profile when release output is intentionally in scope
