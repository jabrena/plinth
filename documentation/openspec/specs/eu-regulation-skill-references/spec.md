# eu-regulation-skill-references Specification

## Purpose
TBD - created by archiving change add-eu-regulation-skills. Update Purpose after archive.
## Requirements
### Requirement: DORA regulation skill

The repository MUST define `802-regulations-dora` as the EU Digital Operational Resilience Act skill for Java enterprise engineering review.

#### Scenario: DORA skill identifier is standardized

- **GIVEN** maintainers implement DORA guidance in generator sources
- **WHEN** they create or reference the skill in XML, inventories, OpenSpec artifacts, or generated local skill output
- **THEN** the identifier is `802-regulations-dora`
- **AND** the official source reference is `https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32022R2554`

#### Scenario: DORA scope maps regulation concerns to engineering controls

- **GIVEN** a user asks to review a Java enterprise system for DORA concerns
- **WHEN** the `802-regulations-dora` skill is applied
- **THEN** the guidance frames findings as engineering controls rather than legal advice
- **AND** it addresses ICT risk management, operational resilience, incident preparation, backup and recovery, business continuity, resilience testing, monitoring, logging, third-party ICT provider risk, change control, and reviewable operational evidence
- **AND** it recommends escalation to legal, compliance, security, risk, resilience, or business-continuity owners for applicability and regulatory interpretation

### Requirement: GDPR regulation skill

The repository MUST define `803-regulations-gdpr` as the EU General Data Protection Regulation skill for Java enterprise engineering review.

#### Scenario: GDPR skill identifier is standardized

- **GIVEN** maintainers implement GDPR guidance in generator sources
- **WHEN** they create or reference the skill in XML, inventories, OpenSpec artifacts, or generated local skill output
- **THEN** the identifier is `803-regulations-gdpr`
- **AND** the official source reference is `https://eur-lex.europa.eu/legal-content/EN/TXT/HTML/?uri=CELEX:32016R0679`

#### Scenario: GDPR scope maps privacy concerns to engineering controls

- **GIVEN** a user asks to review a Java enterprise system for GDPR concerns
- **WHEN** the `803-regulations-gdpr` skill is applied
- **THEN** the guidance frames findings as engineering controls rather than legal advice
- **AND** it addresses personal-data inventory, data minimization, purpose limitation, privacy by design, security of processing, privacy-safe logging, retention and deletion, data-subject rights workflows, controller/processor boundaries, third-country transfer review, DPIA escalation, breach-response evidence, and data-governance ownership
- **AND** it recommends escalation to legal, privacy, data protection officer, security, compliance, or risk owners for lawful-basis, jurisdiction, transfer, DPIA, and regulatory interpretation decisions

### Requirement: Relationship to EU AI Act skill

The new EU regulation skills MUST complement `801-regulations-eu-ai-act` without changing its AI Act workflow.

#### Scenario: Select the right EU regulation skill

- **GIVEN** a Java enterprise system may involve AI, operational resilience, and personal-data concerns
- **WHEN** an agent chooses regulation guidance
- **THEN** `801-regulations-eu-ai-act` is used for AI systems, AI agents, RAG, LLM workflows, tool calling, or model-driven decision support
- **AND** `802-regulations-dora` is used for ICT risk, operational resilience, financial-entity, critical ICT service, or third-party ICT provider concerns
- **AND** `803-regulations-gdpr` is used for personal-data processing, privacy controls, data-subject rights, data transfers, retention, or breach-preparation concerns
- **AND** multiple regulation skills may be used together when the same Java system crosses those concern boundaries

### Requirement: 801 structure alignment

The DORA and GDPR skill implementations MUST use `801-regulations-eu-ai-act` as the format and structure baseline while adapting the content to each regulation.

#### Scenario: Align skill index structure with 801

- **GIVEN** maintainers implement `802-regulations-dora` or `803-regulations-gdpr`
- **WHEN** their skill index XML files are inspected
- **THEN** each skill follows the `801-regulations-eu-ai-act` index structure for metadata, title, goal, constraints, triggers, and workflow steps
- **AND** each workflow starts with authoritative reference material before implementation review
- **AND** each workflow includes regulation-specific scope or classification guidance, implementation evidence review, engineering-control recommendations, escalation guidance, and report-oriented conclusions

#### Scenario: Align reference structure with 801

- **GIVEN** maintainers implement `802-regulations-dora` or `803-regulations-gdpr`
- **WHEN** their skill reference XML files are inspected
- **THEN** each reference follows the `801-regulations-eu-ai-act` reference structure for metadata, role, goal, examples, output-format, and safeguards
- **AND** examples translate regulation concerns into Java enterprise architecture and delivery controls
- **AND** output-format and safeguards remain regulation-specific while preserving the same review posture as `801`: source-first, not legal advice, explicit escalation, evidence-driven findings, and prioritized engineering actions

### Requirement: Questionnaire and report assets

The DORA and GDPR skill implementations MUST provide questionnaire and report assets equivalent to the `801-regulations-eu-ai-act` formal review workflow.

#### Scenario: DORA review assets are generated with the skill

- **GIVEN** maintainers implement `802-regulations-dora`
- **WHEN** local skills are generated
- **THEN** `.agents/skills/802-regulations-dora/assets/questions/802-dora-engineering-review-questionnaire.md` exists
- **AND** `.agents/skills/802-regulations-dora/assets/reports/802-dora-engineering-review-report-template.md` exists
- **AND** the generated `802-regulations-dora/SKILL.md` workflow reads those assets before implementation review
- **AND** the DORA questionnaire requires one-question-at-a-time human answers before code analysis or report generation
- **AND** the DORA report template records review context, operational scope, questionnaire findings, resilience classification, engineering controls, evidence inventory, residual risks, release decision, and prioritized actions

#### Scenario: GDPR review assets are generated with the skill

- **GIVEN** maintainers implement `803-regulations-gdpr`
- **WHEN** local skills are generated
- **THEN** `.agents/skills/803-regulations-gdpr/assets/questions/803-gdpr-engineering-review-questionnaire.md` exists
- **AND** `.agents/skills/803-regulations-gdpr/assets/reports/803-gdpr-engineering-review-report-template.md` exists
- **AND** the generated `803-regulations-gdpr/SKILL.md` workflow reads those assets before implementation review
- **AND** the GDPR questionnaire requires one-question-at-a-time human answers before code analysis or report generation
- **AND** the GDPR report template records review context, personal-data processing summary, questionnaire findings, privacy risk classification, engineering controls, evidence inventory, residual risks, release decision, and prioritized actions

#### Scenario: Regulation references include Java technical examples

- **GIVEN** maintainers implement `802-regulations-dora` or `803-regulations-gdpr`
- **WHEN** their skill reference XML files are inspected
- **THEN** each reference includes Java examples that translate regulation concerns into implementation patterns
- **AND** DORA examples include Java operational resilience or release-policy controls
- **AND** GDPR examples include Java personal-data minimization, rights workflow, privacy-safe logging, or field-level authorization controls

### Requirement: Generator registration

The DORA and GDPR skill sources MUST be registered in the generator inventory so local skill generation emits them.

#### Scenario: Register EU regulation skills

- **WHEN** `skills-generator/src/main/resources/skills.xml` is inspected
- **THEN** skill id `802` registers reference `802-regulations-dora`
- **AND** skill id `803` registers reference `803-regulations-gdpr`
- **AND** skill id `802` registers DORA questionnaire and report template resources
- **AND** skill id `803` registers GDPR questionnaire and report template resources

#### Scenario: Generate local skills

- **WHEN** `./mvnw clean install -pl skills-generator` is run
- **THEN** generated local skill output includes `.agents/skills/802-regulations-dora/SKILL.md`
- **AND** generated local skill output includes `.agents/skills/803-regulations-gdpr/SKILL.md`
- **AND** generated local skill output includes questionnaire and report assets for `802-regulations-dora`
- **AND** generated local skill output includes questionnaire and report assets for `803-regulations-gdpr`
- **AND** generated references contain no unresolved include markers or broken local reference paths

### Requirement: Source and generated-output boundaries

The implementation MUST edit XML sources and validate generated local skill output without directly editing generated legacy or release outputs.

#### Scenario: Preserve generated-output ownership

- **WHEN** implementation files are reviewed
- **THEN** `.cursor/rules/` is not edited directly
- **AND** public `skills/` release output is not edited manually
- **AND** public `skills/` is refreshed only through the release profile when release output is intentionally in scope

### Requirement: 801-803 split-reference alignment

The implemented EU regulation skills `801-regulations-eu-ai-act`, `802-regulations-dora`, and `803-regulations-gdpr` MUST be structurally aligned with the split-reference pattern introduced by `804-regulations-eu-nis2`.

#### Scenario: Generated skills expose separate summary and examples references

- **GIVEN** maintainers align EU regulation skills `801-803`
- **WHEN** `./mvnw clean install -pl skills-generator` is run
- **THEN** generated local skill output for `801-regulations-eu-ai-act` includes a dedicated regulation chapter or article summary reference
- **AND** generated local skill output for `801-regulations-eu-ai-act` includes a dedicated Java engineering examples reference
- **AND** generated local skill output for `802-regulations-dora` includes a dedicated regulation chapter or article summary reference
- **AND** generated local skill output for `802-regulations-dora` includes a dedicated Java engineering examples reference
- **AND** generated local skill output for `803-regulations-gdpr` includes a dedicated regulation chapter or article summary reference
- **AND** generated local skill output for `803-regulations-gdpr` includes a dedicated Java engineering examples reference
- **AND** generated references contain no unresolved include markers or broken local reference paths

#### Scenario: Skill workflows read references in a predictable order

- **WHEN** generated `SKILL.md` files for `801`, `802`, and `803` are inspected
- **THEN** each workflow first reads the regulation chapter or article summary
- **AND** each workflow reads the Java engineering examples reference before implementation review
- **AND** each workflow reads the questionnaire asset when the skill uses a questionnaire
- **AND** each workflow reads the engineering review report template before producing conclusions
- **AND** each workflow preserves the skill's existing regulatory scope, owner escalation, and "not legal advice" constraints

#### Scenario: Report templates include linked regulation mapping

- **WHEN** report templates for `801`, `802`, and `803` are inspected
- **THEN** each template includes a potential violation or non-compliance mapping section
- **AND** each mapping section includes regulation reference areas or chapter/article labels
- **AND** each mapping section includes official-source links where stable links are available
- **AND** each mapping section routes legal applicability, classification, regulatory interpretation, reporting, risk acceptance, privacy, resilience, or compliance decisions to qualified owners instead of asserting legal conclusions

#### Scenario: Gherkin scenarios validate new reference structure

- **WHEN** Gherkin acceptance scenarios for `801`, `802`, and `803` are inspected
- **THEN** each changed scenario expects the generated skill to read its chapter or article summary reference
- **AND** each changed scenario expects the generated skill to read its Java engineering examples reference
- **AND** each changed scenario preserves the existing report output path under `examples/regulations`
- **AND** each changed scenario continues to require findings grounded only in the reviewed example files

#### Scenario: Preserve generated-output ownership

- **WHEN** implementation files are reviewed
- **THEN** XML sources under `skills-generator/src/main/resources` are the source of truth for skill changes
- **AND** `.cursor/rules/` is not edited directly
- **AND** public `skills/` release output is not edited manually
- **AND** public `skills/` is refreshed only through the release profile when release output is intentionally in scope

