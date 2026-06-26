# Acceptance Test Prompts for Skills

## Notes when you execute the Acceptance tests

If you observe issues in the execution of any skill, 
maybe it is an opportunity to improve it.

Ask the model to review the execution and propose improvement on it.
Later make a PR to submit the improvement.

```bash
After the execution of the gherkin file, 
do you see an opportunity to improve this skill?
```

---

## 001-commands-inventory

```bash
execute @skills-generator/src/test/resources/gherkin/skills/001-commands-inventory.feature
and verify that acceptance-tests passes.
```

## 002-agents-inventory

```bash
execute @skills-generator/src/test/resources/gherkin/skills/002-agents-inventory.feature
and verify that acceptance-tests passes.
```

## 003-skills-inventory

```bash
execute @skills-generator/src/test/resources/gherkin/skills/003-skills-inventory.feature
and verify that acceptance-tests passes.
```

## 004-commands-installation

```bash
execute @skills-generator/src/test/resources/gherkin/skills/004-commands-installation.feature
and verify that acceptance-tests passes.
```

## 005-agents-installation

```bash
execute @skills-generator/src/test/resources/gherkin/skills/005-agents-installation.feature
and verify that acceptance-tests passes.
```

## 033-architecture-diagrams

```bash
execute @skills-generator/src/test/resources/gherkin/skills/033-architecture-diagrams.feature
and verify that acceptance-tests passes.
```

## 034-architecture-design-exploration

```bash
execute @skills-generator/src/test/resources/gherkin/skills/034-architecture-design-exploration.feature
and verify that acceptance-tests passes.
```

## 042-planning-openspec

```bash
execute @skills-generator/src/test/resources/gherkin/skills/042-planning-openspec.feature
and verify that acceptance-tests passes.
```

## 051-design-two-steps-methods

```bash
execute @skills-generator/src/test/resources/gherkin/skills/051-design-two-steps-methods.feature
and verify that acceptance-tests passes.
```

## 052-design-hamburger-method

```bash
execute @skills-generator/src/test/resources/gherkin/skills/052-design-hamburger-method.feature
and verify that acceptance-tests passes.
```

## 053-design-simple-rules

```bash
execute @skills-generator/src/test/resources/gherkin/skills/053-design-simple-rules.feature
and verify that acceptance-tests passes.
```

## 110-java-maven-best-practices

```bash
execute @skills-generator/src/test/resources/gherkin/skills/110-java-maven-best-practices.feature
and verify that acceptance-tests passes.
```

## 111-java-maven-dependencies

```bash
execute @skills-generator/src/test/resources/gherkin/skills/111-java-maven-dependencies.feature
and verify that acceptance-tests passes.
```

## 112-java-maven-plugins

```bash
execute @skills-generator/src/test/resources/gherkin/skills/112-java-maven-plugins.feature
and verify that acceptance-tests passes.
```

## 113-java-maven-documentation

```bash
execute @skills-generator/src/test/resources/gherkin/skills/113-java-maven-documentation.feature
and verify that acceptance-tests passes.
```

## 114-java-maven-search

```bash
execute @skills-generator/src/test/resources/gherkin/skills/114-java-maven-search.feature
and verify that acceptance-tests passes.
```

## 123-java-design-patterns

```bash
execute @skills-generator/src/test/resources/gherkin/skills/123-java-design-patterns.feature
and verify that acceptance-tests passes.
```

## 152-java-performance-gatling

```bash
execute @skills-generator/src/test/resources/gherkin/skills/152-java-performance-gatling.feature
and verify that acceptance-tests passes.
```

## 300-frameworks-spring-boot-create-project

```bash
execute @skills-generator/src/test/resources/gherkin/skills/300-frameworks-spring-boot-create-project.feature
and verify that acceptance-tests passes.
```

## 305-frameworks-spring-boot-modulith

```bash
execute @skills-generator/src/test/resources/gherkin/skills/305-frameworks-spring-boot-modulith.feature
and verify that acceptance-tests passes.
```

## 311-frameworks-spring-jdbc

```bash
execute @skills-generator/src/test/resources/gherkin/skills/311-frameworks-spring-jdbc.feature
and verify that acceptance-tests passes.
```

## 313-frameworks-spring-db-migrations-flyway

```bash
execute @skills-generator/src/test/resources/gherkin/skills/313-frameworks-spring-db-migrations-flyway.feature
and verify that acceptance-tests passes.
```

## 316-frameworks-spring-mongodb-migrations-mongock

```bash
execute @skills-generator/src/test/resources/gherkin/skills/316-frameworks-spring-mongodb-migrations-mongock.feature
and verify that acceptance-tests passes.
```

## 323-frameworks-spring-boot-testing-acceptance-tests

```bash
execute @skills-generator/src/test/resources/gherkin/skills/323-frameworks-spring-boot-testing-acceptance-tests.feature
and verify that acceptance-tests passes.
```

## 400-frameworks-quarkus-create-project

```bash
execute @skills-generator/src/test/resources/gherkin/skills/400-frameworks-quarkus-create-project.feature
and verify that acceptance-tests passes.
```

## 413-frameworks-quarkus-db-migrations-flyway

```bash
execute @skills-generator/src/test/resources/gherkin/skills/413-frameworks-quarkus-db-migrations-flyway.feature
and verify that acceptance-tests passes.
```

## 416-frameworks-quarkus-mongodb-migrations-mongock

```bash
execute @skills-generator/src/test/resources/gherkin/skills/416-frameworks-quarkus-mongodb-migrations-mongock.feature
and verify that acceptance-tests passes.
```

## 500-frameworks-micronaut-create-project

```bash
execute @skills-generator/src/test/resources/gherkin/skills/500-frameworks-micronaut-create-project.feature
and verify that acceptance-tests passes.
```

## 513-frameworks-micronaut-db-migrations-flyway

```bash
execute @skills-generator/src/test/resources/gherkin/skills/513-frameworks-micronaut-db-migrations-flyway.feature
and verify that acceptance-tests passes.
```

## 514-frameworks-micronaut-kafka

```bash
execute @skills-generator/src/test/resources/gherkin/skills/514-frameworks-micronaut-kafka.feature
and verify that acceptance-tests passes.
```

## 516-frameworks-micronaut-mongodb-migrations-mongock

```bash
execute @skills-generator/src/test/resources/gherkin/skills/516-frameworks-micronaut-mongodb-migrations-mongock.feature
and verify that acceptance-tests passes.
```

## 704-technologies-sql

```bash
execute @skills-generator/src/test/resources/gherkin/skills/704-technologies-sql.feature
and verify that acceptance-tests passes.
```

## 705-technologies-nosql-mongodb

```bash
execute @skills-generator/src/test/resources/gherkin/skills/705-technologies-nosql-mongodb.feature
and verify that acceptance-tests passes.
```

## 706-technologies-containers-docker

```bash
execute @skills-generator/src/test/resources/gherkin/skills/706-technologies-containers-docker.feature
and verify that acceptance-tests passes.
```

## 801-regulations-eu-ai-act

```bash
execute @skills-generator/src/test/resources/gherkin/skills/801-regulations-eu-ai-act.feature
and verify that acceptance-tests passes.
```

## 802-regulations-dora

```bash
execute @skills-generator/src/test/resources/gherkin/skills/802-regulations-dora.feature
and verify that acceptance-tests passes.
```

## 803-regulations-gdpr

```bash
execute @skills-generator/src/test/resources/gherkin/skills/803-regulations-gdpr.feature
and verify that acceptance-tests passes.
```

## 804-regulations-eu-nis2

```bash
execute @skills-generator/src/test/resources/gherkin/skills/804-regulations-eu-nis2.feature
and verify that acceptance-tests passes.
```

## 805-regulations-eu-cyber-resilience-act

```bash
execute @skills-generator/src/test/resources/gherkin/skills/805-regulations-eu-cyber-resilience-act.feature
and verify that acceptance-tests passes.
```

## 806-regulations-eu-data-act

```bash
execute @skills-generator/src/test/resources/gherkin/skills/806-regulations-eu-data-act.feature
and verify that acceptance-tests passes.
```

## 807-regulations-eu-digital-services-act

```bash
execute @skills-generator/src/test/resources/gherkin/skills/807-regulations-eu-digital-services-act.feature
and verify that acceptance-tests passes.
```

## 808-regulations-eu-digital-markets-act

```bash
execute @skills-generator/src/test/resources/gherkin/skills/808-regulations-eu-digital-markets-act.feature
and verify that acceptance-tests passes.
```
