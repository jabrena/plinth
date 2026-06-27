## ADDED Requirements

### Requirement: MiFID II regulation skill

The repository MUST define `810-regulations-eu-mifid-ii` as the MiFID II skill for Java enterprise engineering review.

#### Scenario: MiFID II skill identifier is standardized

- **GIVEN** maintainers implement MiFID II guidance in generator sources
- **WHEN** they create or reference the skill in XML, inventories, OpenSpec artifacts, or generated local skill output
- **THEN** the identifier is `810-regulations-eu-mifid-ii`
- **AND** the official source reference is `https://eur-lex.europa.eu/eli/dir/2014/65/oj/eng`

#### Scenario: MiFID II scope maps regulation concerns to engineering controls

- **GIVEN** a user asks to review a Java enterprise system for MiFID II concerns
- **WHEN** the `810-regulations-eu-mifid-ii` skill is applied
- **THEN** the guidance frames findings as engineering controls rather than legal advice
- **AND** it addresses investment-service scope triage, algorithmic trading controls, order handling, client classification, suitability and appropriateness workflows, audit evidence, monitoring, operational controls, record keeping, and owner handoff
- **AND** it recommends escalation to legal, compliance, risk, product, operations, or accountable business owners for regulated-service classification, investment-firm status, jurisdiction, client-impact decisions, and legal interpretation

### Requirement: Relationship to other regulation skills

The MiFID II skill MUST complement existing and planned regulation skills without changing their workflows.

#### Scenario: Select MiFID II for investment-service engineering concerns

- **GIVEN** a Java enterprise system may involve investment services, trading, AI, privacy, market surveillance, and operational resilience concerns
- **WHEN** an agent chooses regulation guidance
- **THEN** `810-regulations-eu-mifid-ii` is used for investment-service, algorithmic trading, order handling, suitability, appropriateness, governance, or investment-service audit evidence concerns
- **AND** `811-regulations-eu-market-abuse-regulation` is used for insider dealing, market manipulation, suspicious order or transaction monitoring, disclosure, or market-surveillance evidence concerns
- **AND** `802-regulations-dora` is used for ICT operational resilience concerns
- **AND** multiple regulation skills may be used together when the same Java system crosses those concern boundaries

### Requirement: Generator registration

The MiFID II skill source MUST be registered in the generator inventory so local skill generation emits it.

#### Scenario: Register MiFID II regulation skill

- **WHEN** `skills-generator/src/main/resources/skills.xml` is inspected
- **THEN** skill id `810` registers reference `810-regulations-eu-mifid-ii`

#### Scenario: Generate local MiFID II skill

- **WHEN** `./mvnw clean install -pl skills-generator` is run
- **THEN** generated local skill output includes `.agents/skills/810-regulations-eu-mifid-ii/SKILL.md`
- **AND** generated references contain no unresolved include markers or broken local reference paths

### Requirement: Source and generated-output boundaries

The implementation MUST edit XML sources and validate generated local skill output without directly editing generated legacy or release outputs.

#### Scenario: Preserve generated-output ownership

- **WHEN** implementation files are reviewed
- **THEN** `.cursor/rules/` is not edited directly
- **AND** public `skills/` release output is not edited manually
- **AND** public `skills/` is refreshed only through the release profile when release output is intentionally in scope
