## ADDED Requirements

### Requirement: UK GDPR regulation skill

The repository MUST define `822-regulations-uk-gdpr` as the UK GDPR skill for Java enterprise engineering review.

#### Scenario: UK GDPR skill identifier is standardized

- **GIVEN** maintainers implement UK GDPR guidance in generator sources
- **WHEN** they create or reference the skill in XML, inventories, OpenSpec artifacts, or generated local skill output
- **THEN** the identifier is `822-regulations-uk-gdpr`
- **AND** the official source reference is `https://ico.org.uk/for-organisations/uk-gdpr-guidance-and-resources/`

#### Scenario: UK GDPR scope maps privacy concerns to engineering controls

- **GIVEN** a user asks to review a Java enterprise system for UK GDPR concerns
- **WHEN** the `822-regulations-uk-gdpr` skill is applied
- **THEN** the guidance frames findings as engineering controls rather than legal advice
- **AND** it addresses personal-data inventory, data minimization, lawful-basis handoff, purpose limitation, privacy by design, security of processing, privacy-safe logging, retention and deletion, data-subject rights workflows, controller/processor boundaries, international transfer review, DPIA escalation, breach-response evidence, and data-governance ownership
- **AND** it recommends escalation to legal, privacy, data protection officer, security, compliance, or risk owners for lawful-basis, jurisdiction, transfer, DPIA, and regulatory interpretation decisions

### Requirement: Relationship to other regulation skills

The UK GDPR skill MUST complement existing or planned regulation skills without changing their workflows.

#### Scenario: Select UK GDPR for ICO personal-data concerns

- **GIVEN** a Java enterprise system may involve AI, operational resilience, and personal-data concerns across EU and UK jurisdictions
- **WHEN** an agent chooses regulation guidance
- **THEN** `801-regulations-eu-ai-act` is used for EU AI systems, AI agents, RAG, LLM workflows, tool calling, or model-driven decision support
- **AND** `803-regulations-gdpr` is used for EU personal-data processing, privacy controls, data-subject rights, data transfers, retention, or breach-preparation concerns
- **AND** `821-regulations-uk-operational-resilience` is used for UK important business services, impact tolerances, scenario testing, dependency mapping, FCA/PRA-style operational resilience, or third-party ICT risk concerns
- **AND** `822-regulations-uk-gdpr` is used for UK personal-data processing, ICO guidance, privacy controls, data-subject rights, data transfers, retention, or breach-preparation concerns
- **AND** multiple regulation skills may be used together when the same Java system crosses those concern or jurisdiction boundaries

### Requirement: Generator registration

The UK GDPR skill source MUST be registered in the generator inventory so local skill generation emits it.

#### Scenario: Register UK GDPR regulation skill

- **WHEN** `skills-generator/src/main/resources/skills.xml` is inspected
- **THEN** skill id `822` registers reference `822-regulations-uk-gdpr`

#### Scenario: Generate local UK GDPR skill

- **WHEN** `./mvnw clean install -pl skills-generator` is run
- **THEN** generated local skill output includes `.agents/skills/822-regulations-uk-gdpr/SKILL.md`
- **AND** generated references contain no unresolved include markers or broken local reference paths

### Requirement: Source and generated-output boundaries

The implementation MUST edit XML sources and validate generated local skill output without directly editing generated legacy or release outputs.

#### Scenario: Preserve generated-output ownership

- **WHEN** implementation files are reviewed
- **THEN** `.cursor/rules/` is not edited directly
- **AND** public `skills/` release output is not edited manually
- **AND** public `skills/` is refreshed only through the release profile when release output is intentionally in scope
