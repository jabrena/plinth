# eu-product-liability-directive-regulation-skill-reference Specification

## Purpose
TBD - created by archiving change add-eu-product-liability-directive-regulation-skill. Update Purpose after archive.
## Requirements
### Requirement: Product Liability Directive regulation skill

The repository MUST define `812-regulations-eu-product-liability-directive` as the Product Liability Directive skill for Java enterprise engineering review.

#### Scenario: Product Liability Directive skill identifier is standardized

- **GIVEN** maintainers implement Product Liability Directive guidance in generator sources
- **WHEN** they create or reference the skill in XML, inventories, OpenSpec artifacts, or generated local skill output
- **THEN** the identifier is `812-regulations-eu-product-liability-directive`
- **AND** the official source reference is `https://eur-lex.europa.eu/eli/dir/2024/2853/oj/eng`

#### Scenario: Product Liability Directive scope maps regulation concerns to engineering controls

- **GIVEN** a user asks to review a Java software product or AI-enabled product for Product Liability Directive concerns
- **WHEN** the `812-regulations-eu-product-liability-directive` skill is applied
- **THEN** the guidance frames findings as engineering controls and evidence gaps rather than legal advice
- **AND** it addresses software/AI product scope triage, generated outputs, generated instructions, AI-agent tool actions, automated updates, warnings and instructions for use, safety validation, model/version provenance, update history, incident records, audit logs, vulnerability handling, corrective updates, and product-safety escalation
- **AND** it recommends escalation to legal, compliance, product, safety, security, risk, support, or accountable business owners for defect determinations, compensable-damage questions, economic-operator responsibility, jurisdiction, and final liability assessment

#### Scenario: GenAI maintenance assistant trigger is recognized

- **GIVEN** a Spring Boot product includes a RAG-based GenAI maintenance assistant for industrial machinery
- **AND** generated repair instructions could contribute to injury, property damage, unsafe configuration, defective updates, or security compromise
- **WHEN** the Product Liability Directive skill is applied
- **THEN** it treats the scenario as product-liability-relevant engineering review scope
- **AND** it recommends product-safety review, human oversight where appropriate, source governance, hazardous-scenario testing, corrective update procedures, incident evidence, and qualified owner escalation

### Requirement: Relationship to other regulation skills

The Product Liability Directive skill MUST complement existing regulation skills without changing their workflows.

#### Scenario: Select Product Liability Directive for software and AI product-liability evidence

- **GIVEN** a Java product may involve AI, product cybersecurity, operational security, privacy, generated instructions, automated updates, and safety concerns
- **WHEN** an agent chooses regulation guidance
- **THEN** `812-regulations-eu-product-liability-directive` is used for software or AI product-liability evidence, generated instructions, defective update, warning, incident, or product-safety escalation concerns
- **AND** `801-regulations-eu-ai-act` is used for AI-system classification, high-risk AI, transparency, general-purpose model, or AI-agent governance concerns
- **AND** `805-regulations-eu-cyber-resilience-act` is used for product cybersecurity and vulnerability-handling concerns
- **AND** multiple regulation skills may be used together when the same Java product crosses those concern boundaries

### Requirement: Generator registration

The Product Liability Directive skill source MUST be registered in the generator inventory so local skill generation emits it.

#### Scenario: Register Product Liability Directive regulation skill

- **WHEN** `skills-generator/src/main/resources/skills.xml` is inspected
- **THEN** skill id `812` registers reference `812-regulations-eu-product-liability-directive`

#### Scenario: Generate local Product Liability Directive skill

- **WHEN** `./mvnw clean install -pl skills-generator` is run
- **THEN** generated local skill output includes `.agents/skills/812-regulations-eu-product-liability-directive/SKILL.md`
- **AND** generated references contain no unresolved include markers or broken local reference paths

### Requirement: Source and generated-output boundaries

The implementation MUST edit XML sources and validate generated local skill output without directly editing generated legacy or release outputs.

#### Scenario: Preserve generated-output ownership

- **WHEN** implementation files are reviewed
- **THEN** `.cursor/rules/` is not edited directly
- **AND** public `skills/` release output is not edited manually
- **AND** public `skills/` is refreshed only through the release profile when release output is intentionally in scope

