## ADDED Requirements

### Requirement: GLBA Safeguards regulation skill

The repository MUST define `831-regulations-usa-glba-safeguards` as the USA GLBA Safeguards Rule skill for Java enterprise engineering review.

#### Scenario: GLBA Safeguards skill identifier is standardized

- **GIVEN** maintainers implement GLBA Safeguards guidance in generator sources
- **WHEN** they create or reference the skill in XML, inventories, OpenSpec artifacts, or generated local skill output
- **THEN** the identifier is `831-regulations-usa-glba-safeguards`
- **AND** the official source reference is `https://www.ftc.gov/legal-library/browse/rules/safeguards-rule`

#### Scenario: GLBA Safeguards scope maps regulation concerns to engineering controls

- **GIVEN** a user asks to review a Java enterprise system for GLBA Safeguards concerns
- **WHEN** the `831-regulations-usa-glba-safeguards` skill is applied
- **THEN** the guidance frames findings as engineering controls rather than legal advice
- **AND** it addresses customer-information inventory, risk assessment evidence, access controls, encryption, secure development, change management, logging and monitoring, vulnerability management, incident response, service-provider oversight, retention and disposal, and reviewable safeguards evidence
- **AND** it recommends escalation to legal, compliance, privacy, security, risk, or vendor-management owners for covered-entity status, customer-information classification, service-provider obligations, applicability, and regulatory interpretation

### Requirement: Relationship to other regulation skills

The GLBA Safeguards skill MUST complement existing or planned regulation skills without changing their workflows.

#### Scenario: Select GLBA Safeguards for customer-information safeguards concerns

- **GIVEN** a Java enterprise system may involve AI, privacy, operational resilience, cybersecurity, customer information, and disclosure concerns across EU, UK, and USA jurisdictions
- **WHEN** an agent chooses regulation guidance
- **THEN** `831-regulations-usa-glba-safeguards` is used for customer-information safeguards, GLBA information security programs, financial institution service-provider security, or safeguards evidence concerns
- **AND** `832-regulations-usa-nydfs-cybersecurity` is used for NYDFS cybersecurity program controls, cyber incident response, audit trails, third-party service-provider security, business continuity, disaster recovery, or certification evidence concerns
- **AND** multiple regulation skills may be used together when the same Java system crosses those concern, sector, disclosure, or jurisdiction boundaries

### Requirement: Generator registration

The GLBA Safeguards skill source MUST be registered in the generator inventory so local skill generation emits it.

#### Scenario: Register GLBA Safeguards regulation skill

- **WHEN** `skills-generator/src/main/resources/skills.xml` is inspected
- **THEN** skill id `831` registers reference `831-regulations-usa-glba-safeguards`

#### Scenario: Generate local GLBA Safeguards skill

- **WHEN** `./mvnw clean install -pl skills-generator` is run
- **THEN** generated local skill output includes `.agents/skills/831-regulations-usa-glba-safeguards/SKILL.md`
- **AND** generated references contain no unresolved include markers or broken local reference paths

### Requirement: Source and generated-output boundaries

The implementation MUST edit XML sources and validate generated local skill output without directly editing generated legacy or release outputs.

#### Scenario: Preserve generated-output ownership

- **WHEN** implementation files are reviewed
- **THEN** `.cursor/rules/` is not edited directly
- **AND** public `skills/` release output is not edited manually
- **AND** public `skills/` is refreshed only through the release profile when release output is intentionally in scope
