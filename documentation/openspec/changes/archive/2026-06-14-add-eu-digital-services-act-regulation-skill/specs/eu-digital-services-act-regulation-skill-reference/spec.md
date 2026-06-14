## ADDED Requirements

### Requirement: Digital Services Act regulation skill

The repository MUST define `807-regulations-eu-digital-services-act` as the EU Digital Services Act skill for Java enterprise engineering review.

#### Scenario: Digital Services Act skill identifier is standardized

- **GIVEN** maintainers implement Digital Services Act guidance in generator sources
- **WHEN** they create or reference the skill in XML, inventories, OpenSpec artifacts, or generated local skill output
- **THEN** the identifier is `807-regulations-eu-digital-services-act`
- **AND** the official source reference is `https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32022R2065`

#### Scenario: Digital Services Act scope maps online-platform concerns to engineering controls

- **GIVEN** a user asks to review a Java enterprise system for Digital Services Act concerns
- **WHEN** the `807-regulations-eu-digital-services-act` skill is applied
- **THEN** the guidance frames findings as engineering controls rather than legal advice
- **AND** it addresses content decision audit logs, moderation workflow state, notice intake and response tracking, recommender and ranking explanation evidence, ad transparency metadata, user controls, complaint and appeal workflows, risk assessment evidence, incident escalation, data access for auditors or researchers where applicable, and privacy-safe observability
- **AND** it recommends escalation to legal, compliance, trust-and-safety, privacy, security, product, or risk owners for intermediary classification, platform classification, very-large-online-platform status, illegal-content determinations, systemic-risk conclusions, and regulatory interpretation

### Requirement: Relationship to other regulation skills

The Digital Services Act skill MUST complement existing and planned regulation skills without changing their workflows.

#### Scenario: Select DSA for online-platform and transparency concerns

- **GIVEN** a Java enterprise system may involve AI, privacy, data, platform, advertising, cybersecurity, and transparency concerns
- **WHEN** an agent chooses regulation guidance
- **THEN** `801-regulations-eu-ai-act` is used for EU AI systems and AI-agent governance
- **AND** `803-regulations-gdpr` is used for EU personal-data processing and privacy controls
- **AND** `807-regulations-eu-digital-services-act` is used for online-platform, intermediary-service, content-moderation, recommender-transparency, ad-transparency, notice-and-action, user-redress, and systemic-risk evidence concerns
- **AND** multiple regulation skills may be used together when the same Java system crosses those concern boundaries

### Requirement: Generator registration

The Digital Services Act skill source MUST be registered in the generator inventory so local skill generation emits it.

#### Scenario: Register Digital Services Act regulation skill

- **WHEN** `skills-generator/src/main/resources/skills.xml` is inspected
- **THEN** skill id `807` registers reference `807-regulations-eu-digital-services-act`

#### Scenario: Generate local Digital Services Act skill

- **WHEN** `./mvnw clean install -pl skills-generator` is run
- **THEN** generated local skill output includes `.agents/skills/807-regulations-eu-digital-services-act/SKILL.md`
- **AND** generated references contain no unresolved include markers or broken local reference paths

### Requirement: Source and generated-output boundaries

The implementation MUST edit XML sources and validate generated local skill output without directly editing generated legacy or release outputs.

#### Scenario: Preserve generated-output ownership

- **WHEN** implementation files are reviewed
- **THEN** `.cursor/rules/` is not edited directly
- **AND** public `skills/` release output is not edited manually
- **AND** public `skills/` is refreshed only through the release profile when release output is intentionally in scope
