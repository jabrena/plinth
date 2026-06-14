## ADDED Requirements

### Requirement: NYDFS cybersecurity regulation skill

The repository MUST define `832-regulations-usa-nydfs-cybersecurity` as the USA NYDFS 23 NYCRR 500 cybersecurity skill for Java enterprise engineering review.

#### Scenario: NYDFS cybersecurity skill identifier is standardized

- **GIVEN** maintainers implement NYDFS cybersecurity guidance in generator sources
- **WHEN** they create or reference the skill in XML, inventories, OpenSpec artifacts, or generated local skill output
- **THEN** the identifier is `832-regulations-usa-nydfs-cybersecurity`
- **AND** the official source reference is `https://www.dfs.ny.gov/system/files/documents/2023/03/23NYCRR500_0.pdf`

#### Scenario: NYDFS cybersecurity scope maps regulation concerns to engineering controls

- **GIVEN** a user asks to review a Java enterprise system for NYDFS cybersecurity concerns
- **WHEN** the `832-regulations-usa-nydfs-cybersecurity` skill is applied
- **THEN** the guidance frames findings as engineering controls rather than legal advice
- **AND** it addresses cybersecurity governance, asset inventory, risk assessment, MFA, identity and access management, least privilege, secure configuration, encryption, vulnerability management, application security, logging and monitoring, incident response, business continuity and disaster recovery evidence, third-party service-provider security, audit trails, and certification evidence
- **AND** it recommends escalation to legal, compliance, cybersecurity, risk, audit, or executive accountability owners for covered-entity status, exemption status, certification obligations, applicability, and regulatory interpretation

### Requirement: Relationship to other regulation skills

The NYDFS cybersecurity skill MUST complement existing or planned regulation skills without changing their workflows.

#### Scenario: Select NYDFS for cybersecurity program concerns

- **GIVEN** a Java enterprise system may involve AI, privacy, operational resilience, cybersecurity, customer information, and disclosure concerns across EU, UK, and USA jurisdictions
- **WHEN** an agent chooses regulation guidance
- **THEN** `831-regulations-usa-glba-safeguards` is used for customer-information safeguards, GLBA information security programs, financial institution service-provider security, or safeguards evidence concerns
- **AND** `832-regulations-usa-nydfs-cybersecurity` is used for NYDFS cybersecurity program controls, cyber incident response, audit trails, third-party service-provider security, business continuity, disaster recovery, or certification evidence concerns
- **AND** `833-regulations-usa-sec-cybersecurity-disclosure` is used for cybersecurity disclosure readiness, material incident escalation handoff, governance evidence, public-company risk management reporting support, or disclosure-safe documentation concerns
- **AND** multiple regulation skills may be used together when the same Java system crosses those concern, sector, disclosure, or jurisdiction boundaries

### Requirement: Generator registration

The NYDFS cybersecurity skill source MUST be registered in the generator inventory so local skill generation emits it.

#### Scenario: Register NYDFS cybersecurity regulation skill

- **WHEN** `skills-generator/src/main/resources/skills.xml` is inspected
- **THEN** skill id `832` registers reference `832-regulations-usa-nydfs-cybersecurity`

#### Scenario: Generate local NYDFS cybersecurity skill

- **WHEN** `./mvnw clean install -pl skills-generator` is run
- **THEN** generated local skill output includes `.agents/skills/832-regulations-usa-nydfs-cybersecurity/SKILL.md`
- **AND** generated references contain no unresolved include markers or broken local reference paths

### Requirement: Source and generated-output boundaries

The implementation MUST edit XML sources and validate generated local skill output without directly editing generated legacy or release outputs.

#### Scenario: Preserve generated-output ownership

- **WHEN** implementation files are reviewed
- **THEN** `.cursor/rules/` is not edited directly
- **AND** public `skills/` release output is not edited manually
- **AND** public `skills/` is refreshed only through the release profile when release output is intentionally in scope
