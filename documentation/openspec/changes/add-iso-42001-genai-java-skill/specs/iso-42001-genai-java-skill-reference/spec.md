## ADDED Requirements

### Requirement: ISO/IEC 42001 GenAI Java skill

The repository MUST define `813-regulations-iso-42001` as the ISO/IEC 42001 skill for GenAI-oriented Java engineering review.

#### Scenario: ISO/IEC 42001 skill identifier is standardized

- **GIVEN** maintainers implement ISO/IEC 42001 guidance in generator sources
- **WHEN** they create or reference the skill in XML, inventories, OpenSpec artifacts, or generated local skill output
- **THEN** the identifier is `813-regulations-iso-42001`
- **AND** the official source references include `https://www.iso.org/home/insights-news/resources/iso-42001-explained-what-it-is.html` and `https://www.iso.org/standard/42001`

#### Scenario: Java developer applies ISO/IEC 42001 guidance to GenAI development

- **GIVEN** a Java enterprise developer is designing or reviewing a GenAI-enabled Java system
- **WHEN** the `813-regulations-iso-42001` skill is applied
- **THEN** the skill explains how ISO/IEC 42001 applies to AI management practices in Java software delivery
- **AND** the skill provides practical review guidance for prompts, model usage, generated code, dependencies, data handling, and AI-enabled business logic
- **AND** the guidance frames findings as engineering controls and evidence gaps rather than legal advice, certification advice, audit conclusions, or final conformity decisions

#### Scenario: ISO/IEC 42001 scope maps GenAI risks to engineering controls

- **GIVEN** the ISO/IEC 42001 skill is generated from the project XML sources
- **WHEN** the skill is reviewed for GenAI software delivery risk coverage
- **THEN** it addresses hallucinated code, insecure generated implementations, generated dependency and supply-chain contamination, IP leakage, confidentiality breaches, regulatory non-compliance risk, and biased generated business logic
- **AND** each risk is connected to actionable Java engineering practices and qualified owner escalation where needed

### Requirement: Relationship to other regulation skills

The ISO/IEC 42001 skill MUST complement existing regulation skills without changing their workflows.

#### Scenario: Select ISO/IEC 42001 for AI management system engineering evidence

- **GIVEN** a Java system may involve AI management, EU AI Act, privacy, cybersecurity, product, or operational concerns
- **WHEN** an agent chooses regulation guidance
- **THEN** `813-regulations-iso-42001` is used for AI management system practices, GenAI software delivery risk identification, evidence, controls, and lifecycle governance
- **AND** `801-regulations-eu-ai-act` is used for EU AI Act classification, prohibited or high-risk AI, transparency, general-purpose AI, or EU AI Act governance concerns
- **AND** `803-regulations-gdpr` is used for personal-data processing in prompts, logs, telemetry, RAG sources, model interactions, retention, or privacy rights workflows
- **AND** `805-regulations-eu-cyber-resilience-act` is used for product cybersecurity and vulnerability-handling concerns
- **AND** multiple regulation skills may be used together when the same Java system crosses those concern boundaries

### Requirement: Generator registration

The ISO/IEC 42001 skill source MUST be registered in the generator inventory so local skill generation emits it.

#### Scenario: Register ISO/IEC 42001 regulation skill

- **WHEN** `skills-generator/src/main/resources/skills.xml` is inspected
- **THEN** skill id `813` registers reference `813-regulations-iso-42001`

#### Scenario: Generate local ISO/IEC 42001 skill

- **WHEN** `./mvnw clean install -pl skills-generator` is run
- **THEN** generated local skill output includes `.agents/skills/813-regulations-iso-42001/SKILL.md`
- **AND** generated references contain no unresolved include markers or broken local reference paths

### Requirement: Source and generated-output boundaries

The implementation MUST edit XML sources and validate generated local skill output without directly editing generated legacy or release outputs.

#### Scenario: Preserve generated-output ownership

- **WHEN** implementation files are reviewed
- **THEN** `.cursor/rules/` is not edited directly
- **AND** public `skills/` release output is not edited manually
- **AND** public `skills/` is refreshed only through the release profile when release output is intentionally in scope
