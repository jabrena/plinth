# Acceptance Test Prompts for Commands

## /implement-issue (Spring-boot)

```bash
execute @skills-generator/src/test/resources/gherkin/commands/implement-issue.feature
and verify that acceptance-tests passes.
```

## /implement-issue (Quarkus)

```bash
execute @skills-generator/src/test/resources/gherkin/commands/implement-issue.feature
and verify that acceptance-tests passes. 
Implement it but using Quarkus, not Spring boot as the default requirement.
```

## /implement-issue (Micronaut)

```bash
execute @skills-generator/src/test/resources/gherkin/commands/implement-issue.feature
and verify that acceptance-tests passes. 
Implement it but using Micronaut, not Spring boot as the default requirement.
```