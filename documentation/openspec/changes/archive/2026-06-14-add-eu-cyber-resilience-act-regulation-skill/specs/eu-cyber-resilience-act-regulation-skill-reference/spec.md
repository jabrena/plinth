## ADDED Requirements

### Requirement: Cyber Resilience Act regulation skill

The repository MUST define `805-regulations-eu-cyber-resilience-act` as the EU Cyber Resilience Act skill for Java enterprise engineering review.

#### Scenario: Cyber Resilience Act skill identifier is standardized

- **GIVEN** maintainers implement Cyber Resilience Act guidance in generator sources
- **WHEN** they create or reference the skill in XML, inventories, OpenSpec artifacts, or generated local skill output
- **THEN** the identifier is `805-regulations-eu-cyber-resilience-act`
- **AND** the official source reference is `https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32024R2847`

#### Scenario: Cyber Resilience Act scope maps product-security concerns to engineering controls

- **GIVEN** a user asks to review a Java enterprise system or product for Cyber Resilience Act concerns
- **WHEN** the `805-regulations-eu-cyber-resilience-act` skill is applied
- **THEN** the guidance frames findings as engineering controls rather than legal advice
- **AND** it addresses secure-by-design controls, threat modeling, secure defaults, vulnerability management, coordinated disclosure, security update mechanisms, dependency and SBOM evidence, cryptography, authentication and authorization, sensitive-data-safe logging, product security documentation, end-of-support signaling, and release readiness
- **AND** it recommends escalation to legal, compliance, product, security, risk, or executive accountability owners for product classification, economic-operator role, conformity assessment, CE marking implications, and regulatory interpretation

### Requirement: Relationship to other regulation skills

The Cyber Resilience Act skill MUST complement existing and planned regulation skills without changing their workflows.

#### Scenario: Select CRA for product-security concerns

- **GIVEN** a Java enterprise system may involve AI, privacy, resilience, product security, platform, data, and cybersecurity concerns
- **WHEN** an agent chooses regulation guidance
- **THEN** `804-regulations-eu-nis2` is used for EU cybersecurity risk-management and critical-sector concerns
- **AND** `805-regulations-eu-cyber-resilience-act` is used for products with digital elements, secure-by-design, vulnerability handling, coordinated disclosure, security updates, product security documentation, or SBOM evidence concerns
- **AND** multiple regulation skills may be used together when the same Java system crosses those concern boundaries

### Requirement: Generator registration

The Cyber Resilience Act skill source MUST be registered in the generator inventory so local skill generation emits it.

#### Scenario: Register Cyber Resilience Act regulation skill

- **WHEN** `plinth-skills-generator/src/main/resources/skills.xml` is inspected
- **THEN** skill id `805` registers reference `805-regulations-eu-cyber-resilience-act`

#### Scenario: Generate local Cyber Resilience Act skill

- **WHEN** `./mvnw clean install -pl plinth-skills-generator` is run
- **THEN** generated local skill output includes `.agents/skills/805-regulations-eu-cyber-resilience-act/SKILL.md`
- **AND** generated references contain no unresolved include markers or broken local reference paths

### Requirement: Source and generated-output boundaries

The implementation MUST edit XML sources and validate generated local skill output without directly editing generated legacy or release outputs.

#### Scenario: Preserve generated-output ownership

- **WHEN** implementation files are reviewed
- **THEN** `.cursor/rules/` is not edited directly
- **AND** public `skills/` release output is not edited manually
- **AND** public `skills/` is refreshed only through the release profile when release output is intentionally in scope
