## ADDED Requirements

### Requirement: UK operational resilience regulation skill

The repository MUST define `821-regulations-uk-operational-resilience` as the UK operational resilience skill for Java enterprise engineering review.

#### Scenario: UK operational resilience skill identifier is standardized

- **GIVEN** maintainers implement UK operational resilience guidance in generator sources
- **WHEN** they create or reference the skill in XML, inventories, OpenSpec artifacts, or generated local skill output
- **THEN** the identifier is `821-regulations-uk-operational-resilience`
- **AND** the official source references include `https://www.fca.org.uk/firms/operational-resilience`, `https://www.fca.org.uk/publication/consultation/cp19-32.pdf`, and `https://www.bankofengland.co.uk/-/media/boe/files/prudential-regulation/consultation-paper/2019/building-operational-resilience-impact-tolerances-for-important-business-services.pdf`

#### Scenario: UK operational resilience scope maps regulation concerns to engineering controls

- **GIVEN** a user asks to review a Java enterprise system for UK operational resilience concerns
- **WHEN** the `821-regulations-uk-operational-resilience` skill is applied
- **THEN** the guidance frames findings as engineering controls rather than legal advice
- **AND** it addresses important business services, impact tolerances, dependency mapping, severe-but-plausible scenario testing, third-party ICT risk, monitoring, incident response, backup and recovery, business continuity, change control, rollback, and reviewable operational evidence
- **AND** it recommends escalation to legal, compliance, operational resilience, security, risk, resilience, or business-continuity owners for applicability and regulatory interpretation

### Requirement: Relationship to other regulation skills

The UK operational resilience skill MUST complement existing or planned regulation skills without changing their workflows.

#### Scenario: Select UK operational resilience for FCA/PRA-style resilience concerns

- **GIVEN** a Java enterprise system may involve AI, operational resilience, and personal-data concerns across EU and UK jurisdictions
- **WHEN** an agent chooses regulation guidance
- **THEN** `801-regulations-eu-ai-act` is used for EU AI systems, AI agents, RAG, LLM workflows, tool calling, or model-driven decision support
- **AND** `802-regulations-dora` is used for EU ICT risk, operational resilience, financial-entity, critical ICT service, or third-party ICT provider concerns
- **AND** `821-regulations-uk-operational-resilience` is used for UK important business services, impact tolerances, scenario testing, dependency mapping, FCA/PRA-style operational resilience, or third-party ICT risk concerns
- **AND** multiple regulation skills may be used together when the same Java system crosses those concern or jurisdiction boundaries

### Requirement: Generator registration

The UK operational resilience skill source MUST be registered in the generator inventory so local skill generation emits it.

#### Scenario: Register UK operational resilience regulation skill

- **WHEN** `skills-generator/src/main/resources/skills.xml` is inspected
- **THEN** skill id `821` registers reference `821-regulations-uk-operational-resilience`

#### Scenario: Generate local UK operational resilience skill

- **WHEN** `./mvnw clean install -pl skills-generator` is run
- **THEN** generated local skill output includes `.agents/skills/821-regulations-uk-operational-resilience/SKILL.md`
- **AND** generated references contain no unresolved include markers or broken local reference paths

### Requirement: Source and generated-output boundaries

The implementation MUST edit XML sources and validate generated local skill output without directly editing generated legacy or release outputs.

#### Scenario: Preserve generated-output ownership

- **WHEN** implementation files are reviewed
- **THEN** `.cursor/rules/` is not edited directly
- **AND** public `skills/` release output is not edited manually
- **AND** public `skills/` is refreshed only through the release profile when release output is intentionally in scope
