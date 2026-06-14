## ADDED Requirements

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
