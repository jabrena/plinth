## Context

Issue #890 provides a generated trigger count table and identifies skills with trigger counts from zero to four. The repository already treats `skills/` as generated release output and `.agents/skills/` as local generated output, so the source of truth for this change is the skill index XML under `plinth-skills-generator/src/main/resources/skill-indexes/`.

## Intended Behavior Change

Every generated skill should have at least five meaningful trigger phrases in its source `<triggers>` section. A meaningful trigger phrase describes a plausible user request that should select the skill, uses domain-specific language for that skill, and is not merely a duplicate or filler variation.

## Two-Step Sequencing

### Step 1: Make The Change Easy

Before changing trigger content, confirm the affected skill list from issue #890 and ensure the inventory test can report trigger counts clearly. If the existing `SkillTriggerInventoryTest` only reports counts without failing, update or add focused test coverage so the minimum trigger requirement is explicit.

Validation after Step 1:

- Run the focused `plinth-skills-generator` test that reports or validates trigger counts.
- Confirm the test either characterizes the current failing inventory or reports the affected skills clearly.

### Step 2: Make The Behavior Change

Update affected skill index XML files so every skill has at least five meaningful triggers. Keep each trigger tied to the skill's actual purpose, framework, technology, regulation, or workflow. Avoid copy/paste filler such as repeating the same phrase with only punctuation changes.

Validation after Step 2:

- Validate every changed XML file with `xmllint --noout`.
- Run `./mvnw clean install -pl plinth-skills-generator` to regenerate local `.agents/skills`.
- Run the focused trigger inventory test.
- Inspect representative generated `SKILL.md` files for updated trigger wording.
- Run `./mvnw clean verify -pl plinth-skills-generator`.
- Run `openspec validate --all`.

## Hamburger Slice Assessment

This request is broad because it affects many skill families. The layers are:

- Inventory source: issue #890 trigger counts and local trigger-count test output.
- XML source updates: skill index files under `plinth-skills-generator/src/main/resources/skill-indexes/`.
- Meaningfulness policy: domain-specific, non-duplicate trigger wording.
- Generator output: local `.agents/skills` regenerated from XML.
- Validation: XML well-formedness, focused trigger inventory test, module verification, and OpenSpec validation.

If this had to ship tomorrow, the smallest useful version would update only existing skills below five triggers and add focused regression coverage for the minimum count. It would defer advanced semantic-quality linting, automated duplicate clustering, public release output refresh, and unrelated skill description rewrites.

## Affected Skills In First Batch

Issue #890 identifies these skills as below five triggers:

`001-commands-inventory`, `002-agents-inventory`, `003-skills-inventory`, `004-commands-installation`, `005-agents-installation`, `013-agile-feature`, `014-agile-user-story`, `031-architecture-adr-functional-requirements`, `032-architecture-adr-non-functional-requirements`, `043-planning-github-issues`, `110-java-maven-best-practices`, `112-java-maven-plugins`, `124-java-secure-coding`, `125-java-concurrency`, `126-java-exception-handling`, `128-java-generics`, `130-java-testing-strategies`, `131-java-testing-unit-testing`, `132-java-testing-integration-testing`, `133-java-testing-acceptance-tests`, `141-java-refactoring-with-modern-features`, `142-java-functional-programming`, `143-java-functional-exception-handling`, `151-java-performance-jmeter`, `152-java-performance-gatling`, `161-java-profiling-detect`, `170-java-documentation`, `181-java-observability-logging`, `182-java-observability-metrics-micrometer`, `183-java-observability-tracing-opentelemetry`, `200-agents-md`, `300-frameworks-spring-boot-create-project`, `301-frameworks-spring-boot-core`, `302-frameworks-spring-boot-rest`, `311-frameworks-spring-jdbc`, `312-frameworks-spring-data-jdbc`, `313-frameworks-spring-db-migrations-flyway`, `314-frameworks-spring-kafka`, `315-frameworks-spring-mongodb`, `316-frameworks-spring-mongodb-migrations-mongock`, `321-frameworks-spring-boot-testing-unit-tests`, `322-frameworks-spring-boot-testing-integration-tests`, `323-frameworks-spring-boot-testing-acceptance-tests`, `400-frameworks-quarkus-create-project`, `401-frameworks-quarkus-core`, `402-frameworks-quarkus-rest`, `411-frameworks-quarkus-jdbc`, `412-frameworks-quarkus-panache`, `413-frameworks-quarkus-db-migrations-flyway`, `414-frameworks-quarkus-kafka`, `415-frameworks-quarkus-mongodb`, `416-frameworks-quarkus-mongodb-migrations-mongock`, `421-frameworks-quarkus-testing-unit-tests`, `423-frameworks-quarkus-testing-acceptance-tests`, `500-frameworks-micronaut-create-project`, `501-frameworks-micronaut-core`, `502-frameworks-micronaut-rest`, `511-frameworks-micronaut-jdbc`, `512-frameworks-micronaut-data`, `513-frameworks-micronaut-db-migrations-flyway`, `514-frameworks-micronaut-kafka`, `515-frameworks-micronaut-mongodb`, `516-frameworks-micronaut-mongodb-migrations-mongock`, `521-frameworks-micronaut-testing-unit-tests`, `522-frameworks-micronaut-testing-integration-tests`, `523-frameworks-micronaut-testing-acceptance-tests`, and `703-technologies-fuzzing-testing`.

## Open Questions

- Should duplicate trigger detection become a separate quality rule after the minimum-count issue is fixed?
- Should public `skills/` release output be refreshed in the implementation change, or should this remain local-generation-only until a later release profile run?
