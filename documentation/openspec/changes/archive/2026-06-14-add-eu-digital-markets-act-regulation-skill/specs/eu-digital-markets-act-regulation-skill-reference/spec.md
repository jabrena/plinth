## ADDED Requirements

### Requirement: Digital Markets Act regulation skill

The repository MUST define `808-regulations-eu-digital-markets-act` as the EU Digital Markets Act skill for Java enterprise engineering review.

#### Scenario: Digital Markets Act skill identifier is standardized

- **GIVEN** maintainers implement Digital Markets Act guidance in generator sources
- **WHEN** they create or reference the skill in XML, inventories, OpenSpec artifacts, or generated local skill output
- **THEN** the identifier is `808-regulations-eu-digital-markets-act`
- **AND** the official source reference is `https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32022R1925`

#### Scenario: Digital Markets Act scope maps gatekeeper-platform concerns to engineering controls

- **GIVEN** a user asks to review a Java enterprise system for Digital Markets Act concerns
- **WHEN** the `808-regulations-eu-digital-markets-act` skill is applied
- **THEN** the guidance frames findings as engineering controls rather than legal advice
- **AND** it addresses interoperability interfaces, data access APIs, consent and preference evidence, ranking and self-preferencing audit signals, business-user export workflows, anti-circumvention guardrails, access control, observability, change control, documentation, and compliance evidence handoff
- **AND** it recommends escalation to legal, compliance, platform governance, product, privacy, security, or risk owners for gatekeeper designation, core-platform-service classification, obligation applicability, self-preferencing determinations, and regulatory interpretation

### Requirement: Relationship to other regulation skills

The Digital Markets Act skill MUST complement existing and planned regulation skills without changing their workflows.

#### Scenario: Select DMA for gatekeeper-platform concerns

- **GIVEN** a Java enterprise system may involve AI, privacy, platform, marketplace, advertising, data access, and interoperability concerns
- **WHEN** an agent chooses regulation guidance
- **THEN** `803-regulations-gdpr` is used for EU personal-data processing and privacy controls
- **AND** `806-regulations-eu-data-act` is used for EU data access and portability concerns
- **AND** `807-regulations-eu-digital-services-act` is used for online-platform, content-moderation, recommender, advertising, and transparency concerns
- **AND** `808-regulations-eu-digital-markets-act` is used for gatekeeper-platform, core-platform-service, interoperability, business-user access, consent-dependent data combination, self-preferencing, and platform-control concerns
- **AND** multiple regulation skills may be used together when the same Java system crosses those concern boundaries

### Requirement: Generator registration

The Digital Markets Act skill source MUST be registered in the generator inventory so local skill generation emits it.

#### Scenario: Register Digital Markets Act regulation skill

- **WHEN** `plinth-skills-generator/src/main/resources/skills.xml` is inspected
- **THEN** skill id `808` registers reference `808-regulations-eu-digital-markets-act`

#### Scenario: Generate local Digital Markets Act skill

- **WHEN** `./mvnw clean install -pl plinth-skills-generator` is run
- **THEN** generated local skill output includes `.agents/skills/808-regulations-eu-digital-markets-act/SKILL.md`
- **AND** generated references contain no unresolved include markers or broken local reference paths

### Requirement: Source and generated-output boundaries

The implementation MUST edit XML sources and validate generated local skill output without directly editing generated legacy or release outputs.

#### Scenario: Preserve generated-output ownership

- **WHEN** implementation files are reviewed
- **THEN** `.cursor/rules/` is not edited directly
- **AND** public `skills/` release output is not edited manually
- **AND** public `skills/` is refreshed only through the release profile when release output is intentionally in scope
