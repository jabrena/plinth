## ADDED Requirements

### Requirement: Data Act regulation skill

The repository MUST define `806-regulations-eu-data-act` as the EU Data Act skill for Java enterprise engineering review.

#### Scenario: Data Act skill identifier is standardized

- **GIVEN** maintainers implement Data Act guidance in generator sources
- **WHEN** they create or reference the skill in XML, inventories, OpenSpec artifacts, or generated local skill output
- **THEN** the identifier is `806-regulations-eu-data-act`
- **AND** the official source reference is `https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32023R2854`

#### Scenario: Data Act scope maps data access and portability concerns to engineering controls

- **GIVEN** a user asks to review a Java enterprise system for Data Act concerns
- **WHEN** the `806-regulations-eu-data-act` skill is applied
- **THEN** the guidance frames findings as engineering controls rather than legal advice
- **AND** it addresses data inventory, access authorization, portability APIs, export formats, interoperability, metadata, audit logs, cloud-switching support, non-personal data safeguards, trade-secret or sensitive-data handoff, data-sharing request workflows, contract evidence, and operational controls for data access requests
- **AND** it recommends escalation to legal, compliance, privacy, data governance, security, product, or risk owners for data-holder status, user entitlement, contract interpretation, trade-secret disclosure boundaries, international access restrictions, and regulatory interpretation

### Requirement: Relationship to other regulation skills

The Data Act skill MUST complement existing and planned regulation skills without changing their workflows.

#### Scenario: Select Data Act for data access and portability concerns

- **GIVEN** a Java enterprise system may involve AI, privacy, non-personal data, cloud, platform, cybersecurity, and resilience concerns
- **WHEN** an agent chooses regulation guidance
- **THEN** `803-regulations-gdpr` is used for EU personal-data processing and privacy controls
- **AND** `806-regulations-eu-data-act` is used for EU data access, data sharing, data portability, interoperability, cloud switching, and non-personal data governance concerns
- **AND** multiple regulation skills may be used together when the same Java system crosses those concern boundaries

### Requirement: Generator registration

The Data Act skill source MUST be registered in the generator inventory so local skill generation emits it.

#### Scenario: Register Data Act regulation skill

- **WHEN** `plinth-skills-generator/src/main/resources/skills.xml` is inspected
- **THEN** skill id `806` registers reference `806-regulations-eu-data-act`

#### Scenario: Generate local Data Act skill

- **WHEN** `./mvnw clean install -pl plinth-skills-generator` is run
- **THEN** generated local skill output includes `.agents/skills/806-regulations-eu-data-act/SKILL.md`
- **AND** generated references contain no unresolved include markers or broken local reference paths

### Requirement: Source and generated-output boundaries

The implementation MUST edit XML sources and validate generated local skill output without directly editing generated legacy or release outputs.

#### Scenario: Preserve generated-output ownership

- **WHEN** implementation files are reviewed
- **THEN** `.cursor/rules/` is not edited directly
- **AND** public `skills/` release output is not edited manually
- **AND** public `skills/` is refreshed only through the release profile when release output is intentionally in scope
