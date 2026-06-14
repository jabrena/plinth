## ADDED Requirements

### Requirement: Digital Omnibus regulation skill

The repository MUST define `809-regulations-eu-digital-omnibus` as the EU Digital Omnibus simplification-impact skill for Java enterprise engineering review.

#### Scenario: Digital Omnibus skill identifier is standardized

- **GIVEN** maintainers implement Digital Omnibus guidance in generator sources
- **WHEN** they create or reference the skill in XML, inventories, OpenSpec artifacts, or generated local skill output
- **THEN** the identifier is `809-regulations-eu-digital-omnibus`
- **AND** source references include `https://commission.europa.eu/news-and-media/news/simpler-digital-rules-help-eu-businesses-grow-2025-11-19_en` and `https://digital-strategy.ec.europa.eu/en/policies/digital-rulebook`

#### Scenario: Digital Omnibus scope maps simplification impacts to engineering controls

- **GIVEN** a user asks to review a Java enterprise system for Digital Omnibus impacts
- **WHEN** the `809-regulations-eu-digital-omnibus` skill is applied
- **THEN** the guidance frames findings as proposal-stage simplification impacts and engineering controls rather than legal advice
- **AND** it addresses source-status checks, affected-regulation mapping, evidence inventory updates, change-control impacts, questionnaire or report-template update candidates, incident-reporting workflow consolidation, data-rights workflow impacts, AI governance timeline changes, compatibility with existing regulation skills, and escalation when proposal-stage language is ambiguous
- **AND** it recommends escalation to legal, compliance, privacy, security, risk, resilience, data-governance, or AI governance owners for legislative-status assessment, applicability, interpretation, and adoption decisions

### Requirement: Proposal-stage safeguards

The Digital Omnibus skill MUST prevent proposal-stage material from being treated as final settled regulation.

#### Scenario: Preserve regulation-specific authority

- **GIVEN** Digital Omnibus material may affect AI Act, GDPR, DORA, NIS2, Data Act, or other EU digital-rule guidance
- **WHEN** the `809-regulations-eu-digital-omnibus` skill is applied
- **THEN** it checks and reports the source status before recommending changes
- **AND** it does not replace regulation-specific review from `801`, `802`, `803`, `804`, `806`, or future regulation skills
- **AND** it does not silently relax controls, reduce escalation requirements, or rewrite conclusions from regulation-specific skills

### Requirement: Relationship to other regulation skills

The Digital Omnibus skill MUST complement existing and planned regulation skills without changing their workflows.

#### Scenario: Select Digital Omnibus as a cross-cutting overlay

- **GIVEN** a Java enterprise system may involve AI, privacy, cybersecurity, resilience, data access, or incident-reporting concerns
- **WHEN** an agent chooses regulation guidance
- **THEN** regulation-specific skills are used for regulation-specific review
- **AND** `809-regulations-eu-digital-omnibus` is used only when the primary concern is how Digital Omnibus simplification proposals may affect existing EU digital-rule evidence, timelines, reporting paths, or skill guidance
- **AND** multiple regulation skills may be used together when the same Java system crosses those concern boundaries

### Requirement: Generator registration

The Digital Omnibus skill source MUST be registered in the generator inventory so local skill generation emits it.

#### Scenario: Register Digital Omnibus regulation skill

- **WHEN** `skills-generator/src/main/resources/skills.xml` is inspected
- **THEN** skill id `809` registers reference `809-regulations-eu-digital-omnibus`

#### Scenario: Generate local Digital Omnibus skill

- **WHEN** `./mvnw clean install -pl skills-generator` is run
- **THEN** generated local skill output includes `.agents/skills/809-regulations-eu-digital-omnibus/SKILL.md`
- **AND** generated references contain no unresolved include markers or broken local reference paths

### Requirement: Source and generated-output boundaries

The implementation MUST edit XML sources and validate generated local skill output without directly editing generated legacy or release outputs.

#### Scenario: Preserve generated-output ownership

- **WHEN** implementation files are reviewed
- **THEN** `.cursor/rules/` is not edited directly
- **AND** public `skills/` release output is not edited manually
- **AND** public `skills/` is refreshed only through the release profile when release output is intentionally in scope
