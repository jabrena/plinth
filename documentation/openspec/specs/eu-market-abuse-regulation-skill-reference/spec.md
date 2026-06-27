# eu-market-abuse-regulation-skill-reference Specification

## Purpose
TBD - created by archiving change add-eu-market-abuse-regulation-skill. Update Purpose after archive.
## Requirements
### Requirement: Market Abuse Regulation skill

The repository MUST define `811-regulations-eu-market-abuse-regulation` as the MAR skill for Java enterprise engineering review.

#### Scenario: MAR skill identifier is standardized

- **GIVEN** maintainers implement MAR guidance in generator sources
- **WHEN** they create or reference the skill in XML, inventories, OpenSpec artifacts, or generated local skill output
- **THEN** the identifier is `811-regulations-eu-market-abuse-regulation`
- **AND** the official source reference is `https://eur-lex.europa.eu/eli/reg/2014/596/oj/eng`

#### Scenario: MAR scope maps regulation concerns to engineering controls

- **GIVEN** a user asks to review a Java enterprise system for MAR concerns
- **WHEN** the `811-regulations-eu-market-abuse-regulation` skill is applied
- **THEN** the guidance frames findings as engineering controls rather than legal advice
- **AND** it addresses market-surveillance scope triage, suspicious order and transaction monitoring, market-data lineage, insider-list workflow evidence, disclosure workflow evidence, alert explainability, model/rule provenance, reviewer decisions, false-positive handling, investigation records, monitoring, and compliance escalation
- **AND** it recommends escalation to legal, compliance, market-surveillance, risk, product, operations, or accountable business owners for market-abuse classification, reportability, jurisdiction, enforcement interpretation, and final compliance decisions

### Requirement: Relationship to other regulation skills

The MAR skill MUST complement existing and planned regulation skills without changing their workflows.

#### Scenario: Select MAR for market-abuse engineering concerns

- **GIVEN** a Java enterprise system may involve investment services, trading, AI, privacy, market surveillance, and operational resilience concerns
- **WHEN** an agent chooses regulation guidance
- **THEN** `811-regulations-eu-market-abuse-regulation` is used for insider dealing, unlawful disclosure, market manipulation, suspicious order or transaction monitoring, market-surveillance evidence, or AI-assisted market-abuse detection concerns
- **AND** `810-regulations-eu-mifid-ii` is used for investment-service, algorithmic trading, order handling, suitability, appropriateness, governance, or investment-service audit evidence concerns
- **AND** `802-regulations-dora` is used for ICT operational resilience concerns
- **AND** multiple regulation skills may be used together when the same Java system crosses those concern boundaries

### Requirement: Generator registration

The MAR skill source MUST be registered in the generator inventory so local skill generation emits it.

#### Scenario: Register MAR regulation skill

- **WHEN** `skills-generator/src/main/resources/skills.xml` is inspected
- **THEN** skill id `811` registers reference `811-regulations-eu-market-abuse-regulation`

#### Scenario: Generate local MAR skill

- **WHEN** `./mvnw clean install -pl skills-generator` is run
- **THEN** generated local skill output includes `.agents/skills/811-regulations-eu-market-abuse-regulation/SKILL.md`
- **AND** generated references contain no unresolved include markers or broken local reference paths

### Requirement: Source and generated-output boundaries

The implementation MUST edit XML sources and validate generated local skill output without directly editing generated legacy or release outputs.

#### Scenario: Preserve generated-output ownership

- **WHEN** implementation files are reviewed
- **THEN** `.cursor/rules/` is not edited directly
- **AND** public `skills/` release output is not edited manually
- **AND** public `skills/` is refreshed only through the release profile when release output is intentionally in scope

