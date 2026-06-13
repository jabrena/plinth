## ADDED Requirements

### Requirement: GLBA Safeguards regulation skill

The repository MUST define `806-regulations-usa-glba-safeguards` as the USA GLBA Safeguards Rule skill for Java enterprise engineering review.

#### Scenario: GLBA Safeguards skill identifier is standardized

- **GIVEN** maintainers implement GLBA Safeguards guidance in generator sources
- **WHEN** they create or reference the skill in XML, inventories, OpenSpec artifacts, or generated local skill output
- **THEN** the identifier is `806-regulations-usa-glba-safeguards`
- **AND** the official source reference is `https://www.ftc.gov/legal-library/browse/rules/safeguards-rule`

#### Scenario: GLBA Safeguards scope maps regulation concerns to engineering controls

- **GIVEN** a user asks to review a Java enterprise system for GLBA Safeguards concerns
- **WHEN** the `806-regulations-usa-glba-safeguards` skill is applied
- **THEN** the guidance frames findings as engineering controls rather than legal advice
- **AND** it addresses customer-information inventory, risk assessment evidence, access controls, encryption, secure development, change management, logging and monitoring, vulnerability management, incident response, service-provider oversight, retention and disposal, and reviewable safeguards evidence
- **AND** it recommends escalation to legal, compliance, privacy, security, risk, or vendor-management owners for covered-entity status, customer-information classification, applicability, and regulatory interpretation

### Requirement: NYDFS cybersecurity regulation skill

The repository MUST define `807-regulations-usa-nydfs-cybersecurity` as the USA NYDFS 23 NYCRR 500 cybersecurity skill for Java enterprise engineering review.

#### Scenario: NYDFS cybersecurity skill identifier is standardized

- **GIVEN** maintainers implement NYDFS cybersecurity guidance in generator sources
- **WHEN** they create or reference the skill in XML, inventories, OpenSpec artifacts, or generated local skill output
- **THEN** the identifier is `807-regulations-usa-nydfs-cybersecurity`
- **AND** the official source reference is `https://www.dfs.ny.gov/system/files/documents/2023/03/23NYCRR500_0.pdf`

#### Scenario: NYDFS cybersecurity scope maps regulation concerns to engineering controls

- **GIVEN** a user asks to review a Java enterprise system for NYDFS cybersecurity concerns
- **WHEN** the `807-regulations-usa-nydfs-cybersecurity` skill is applied
- **THEN** the guidance frames findings as engineering controls rather than legal advice
- **AND** it addresses cybersecurity governance, asset inventory, risk assessment, MFA, identity and access management, least privilege, secure configuration, encryption, vulnerability management, application security, logging and monitoring, incident response, business continuity and disaster recovery evidence, third-party service-provider security, audit trails, and certification evidence
- **AND** it recommends escalation to legal, compliance, cybersecurity, risk, audit, or executive accountability owners for covered-entity status, exemption status, certification obligations, applicability, and regulatory interpretation

### Requirement: SEC cybersecurity disclosure regulation skill

The repository MUST define `808-regulations-usa-sec-cybersecurity-disclosure` as the USA SEC cybersecurity disclosure readiness skill for Java enterprise engineering review.

#### Scenario: SEC cybersecurity disclosure skill identifier is standardized

- **GIVEN** maintainers implement SEC cybersecurity disclosure readiness guidance in generator sources
- **WHEN** they create or reference the skill in XML, inventories, OpenSpec artifacts, or generated local skill output
- **THEN** the identifier is `808-regulations-usa-sec-cybersecurity-disclosure`
- **AND** the official source reference is `https://www.sec.gov/rules/2023/07/cybersecurity-risk-management-strategy-governance-and-incident-disclosure`

#### Scenario: SEC cybersecurity disclosure scope maps disclosure readiness concerns to engineering controls

- **GIVEN** a user asks to review a Java enterprise system for SEC cybersecurity disclosure readiness concerns
- **WHEN** the `808-regulations-usa-sec-cybersecurity-disclosure` skill is applied
- **THEN** the guidance frames findings as engineering controls and evidence readiness rather than legal advice
- **AND** it addresses incident classification handoff, severity and materiality escalation paths, governance reporting signals, cybersecurity risk management evidence, audit logs, incident timeline reconstruction, remediation tracking, third-party dependency evidence, board/executive reporting inputs, and disclosure-safe documentation
- **AND** it recommends escalation to legal, disclosure committee, compliance, investor relations, security, risk, audit, or executive owners for materiality, disclosure timing, public filing content, and securities-law interpretation

### Requirement: Relationship to other regulation skills

The new USA regulation skills MUST complement existing or planned regulation skills without changing their workflows.

#### Scenario: Select the right regulation skill

- **GIVEN** a Java enterprise system may involve AI, privacy, operational resilience, cybersecurity, and disclosure concerns across EU, UK, and USA jurisdictions
- **WHEN** an agent chooses regulation guidance
- **THEN** `801-regulations-eu-ai-act` is used for EU AI systems, AI agents, RAG, LLM workflows, tool calling, or model-driven decision support
- **AND** `802-regulations-dora` is used for EU ICT risk, operational resilience, financial-entity, critical ICT service, or third-party ICT provider concerns
- **AND** `803-regulations-gdpr` is used for EU personal-data processing, privacy controls, data-subject rights, data transfers, retention, or breach-preparation concerns
- **AND** `804-regulations-uk-operational-resilience` is used for UK important business services, impact tolerances, scenario testing, dependency mapping, FCA/PRA-style operational resilience, or third-party ICT risk concerns
- **AND** `805-regulations-uk-gdpr` is used for UK personal-data processing, ICO guidance, privacy controls, data-subject rights, data transfers, retention, or breach-preparation concerns
- **AND** `806-regulations-usa-glba-safeguards` is used for customer-information safeguards, GLBA information security programs, financial institution service-provider security, or safeguards evidence concerns
- **AND** `807-regulations-usa-nydfs-cybersecurity` is used for NYDFS cybersecurity program controls, cyber incident response, audit trails, third-party service-provider security, business continuity, disaster recovery, or certification evidence concerns
- **AND** `808-regulations-usa-sec-cybersecurity-disclosure` is used for cybersecurity disclosure readiness, material incident escalation handoff, governance evidence, public-company risk management reporting support, or disclosure-safe documentation concerns
- **AND** multiple regulation skills may be used together when the same Java system crosses those concern, sector, disclosure, or jurisdiction boundaries

### Requirement: Generator registration

The GLBA Safeguards, NYDFS cybersecurity, and SEC cybersecurity disclosure readiness skill sources MUST be registered in the generator inventory so local skill generation emits them.

#### Scenario: Register USA regulation skills

- **WHEN** `skills-generator/src/main/resources/skills.xml` is inspected
- **THEN** skill id `806` registers reference `806-regulations-usa-glba-safeguards`
- **AND** skill id `807` registers reference `807-regulations-usa-nydfs-cybersecurity`
- **AND** skill id `808` registers reference `808-regulations-usa-sec-cybersecurity-disclosure`

#### Scenario: Generate local skills

- **WHEN** `./mvnw clean install -pl skills-generator` is run
- **THEN** generated local skill output includes `.agents/skills/806-regulations-usa-glba-safeguards/SKILL.md`
- **AND** generated local skill output includes `.agents/skills/807-regulations-usa-nydfs-cybersecurity/SKILL.md`
- **AND** generated local skill output includes `.agents/skills/808-regulations-usa-sec-cybersecurity-disclosure/SKILL.md`
- **AND** generated references contain no unresolved include markers or broken local reference paths

### Requirement: Source and generated-output boundaries

The implementation MUST edit XML sources and validate generated local skill output without directly editing generated legacy or release outputs.

#### Scenario: Preserve generated-output ownership

- **WHEN** implementation files are reviewed
- **THEN** `.cursor/rules/` is not edited directly
- **AND** public `skills/` release output is not edited manually
- **AND** public `skills/` is refreshed only through the release profile when release output is intentionally in scope
