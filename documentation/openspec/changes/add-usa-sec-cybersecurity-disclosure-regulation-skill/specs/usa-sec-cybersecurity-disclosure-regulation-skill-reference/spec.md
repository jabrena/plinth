## ADDED Requirements

### Requirement: SEC cybersecurity disclosure regulation skill

The repository MUST define `833-regulations-usa-sec-cybersecurity-disclosure` as the USA SEC cybersecurity disclosure readiness skill for Java enterprise engineering review.

#### Scenario: SEC cybersecurity disclosure skill identifier is standardized

- **GIVEN** maintainers implement SEC cybersecurity disclosure readiness guidance in generator sources
- **WHEN** they create or reference the skill in XML, inventories, OpenSpec artifacts, or generated local skill output
- **THEN** the identifier is `833-regulations-usa-sec-cybersecurity-disclosure`
- **AND** the official source reference is `https://www.sec.gov/rules/2023/07/cybersecurity-risk-management-strategy-governance-and-incident-disclosure`

#### Scenario: SEC cybersecurity disclosure scope maps disclosure readiness concerns to engineering controls

- **GIVEN** a user asks to review a Java enterprise system for SEC cybersecurity disclosure readiness concerns
- **WHEN** the `833-regulations-usa-sec-cybersecurity-disclosure` skill is applied
- **THEN** the guidance frames findings as engineering controls and evidence readiness rather than legal advice
- **AND** it addresses incident classification handoff, severity and materiality escalation paths, governance reporting signals, cybersecurity risk management evidence, audit logs, incident timeline reconstruction, remediation tracking, third-party dependency evidence, board/executive reporting inputs, and disclosure-safe documentation
- **AND** it recommends escalation to legal, disclosure committee, compliance, investor relations, security, risk, audit, or executive owners for materiality, disclosure timing, public filing content, and securities-law interpretation

### Requirement: Relationship to other regulation skills

The SEC cybersecurity disclosure skill MUST complement existing or planned regulation skills without changing their workflows.

#### Scenario: Select SEC disclosure readiness for cybersecurity reporting concerns

- **GIVEN** a Java enterprise system may involve AI, privacy, operational resilience, cybersecurity, customer information, and disclosure concerns across EU, UK, and USA jurisdictions
- **WHEN** an agent chooses regulation guidance
- **THEN** `832-regulations-usa-nydfs-cybersecurity` is used for NYDFS cybersecurity program controls, cyber incident response, audit trails, third-party service-provider security, business continuity, disaster recovery, or certification evidence concerns
- **AND** `833-regulations-usa-sec-cybersecurity-disclosure` is used for cybersecurity disclosure readiness, material incident escalation handoff, governance evidence, public-company risk management reporting support, or disclosure-safe documentation concerns
- **AND** multiple regulation skills may be used together when the same Java system crosses those concern, sector, disclosure, or jurisdiction boundaries

### Requirement: Generator registration

The SEC cybersecurity disclosure skill source MUST be registered in the generator inventory so local skill generation emits it.

#### Scenario: Register SEC cybersecurity disclosure regulation skill

- **WHEN** `skills-generator/src/main/resources/skills.xml` is inspected
- **THEN** skill id `833` registers reference `833-regulations-usa-sec-cybersecurity-disclosure`

#### Scenario: Generate local SEC cybersecurity disclosure skill

- **WHEN** `./mvnw clean install -pl skills-generator` is run
- **THEN** generated local skill output includes `.agents/skills/833-regulations-usa-sec-cybersecurity-disclosure/SKILL.md`
- **AND** generated references contain no unresolved include markers or broken local reference paths

### Requirement: Source and generated-output boundaries

The implementation MUST edit XML sources and validate generated local skill output without directly editing generated legacy or release outputs.

#### Scenario: Preserve generated-output ownership

- **WHEN** implementation files are reviewed
- **THEN** `.cursor/rules/` is not edited directly
- **AND** public `skills/` release output is not edited manually
- **AND** public `skills/` is refreshed only through the release profile when release output is intentionally in scope
