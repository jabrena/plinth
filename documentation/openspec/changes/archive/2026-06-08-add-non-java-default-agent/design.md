## Context

`robot-tech-lead` coordinates delivery by selecting a specialist implementation agent from the selected issue, plan, or OpenSpec task list. Its current fallback sends work that is not Spring Boot, Quarkus, or Micronaut to `robot-java-coder`. That mixes two cases: plain Java work and work that is not Java at all.

## Goals / Non-Goals

**Goals:**

- Provide a default `@robot-no-java` agent for work whose authoritative artifacts do not use Java.
- Keep `@robot-java-coder` as the route for plain Java, Maven, and Java/JVM framework-neutral work.
- Make tech lead routing explicit for Java, framework Java, ambiguous Java, and non-Java cases.
- Include the new agent in the embedded installation bundle and inventory template.

**Non-Goals:**

- Add framework-specific non-Java specialists.
- Teach `@robot-no-java` deep language-specific guidance for every ecosystem.
- Change the specialized Java coder responsibilities.

## Decisions

### Separate plain Java from non-Java

`robot-java-coder` remains the right delegate when the selected artifact uses Java, Maven, JVM terminology, or explicitly calls for framework-neutral Java. `robot-no-java` is used when the selected artifact names a non-Java stack or lacks a Java/JVM scope.

### Keep the fallback conservative

When the artifact is ambiguous, the tech lead should inspect authoritative sources first. If Java/Maven/JVM is evident, route to `robot-java-coder`. If no Java evidence exists, route to `robot-no-java` and ask it to implement with repository-local conventions rather than Java-specific skills.

### Keep the new agent generic

`robot-no-java` should be a general implementation agent for non-Java tasks. It must avoid pretending to be a Java specialist, discover the project toolchain from repository files, run appropriate existing validation commands, and report blockers when no safe toolchain is evident.

## Risks / Trade-offs

- [Risk] The fallback becomes too broad and hides missing specialist agents. -> Make the agent report the detected stack and limitations.
- [Risk] Ambiguous Java work is routed away from Java coders. -> Teach the tech lead to route to `robot-java-coder` when Java, Maven, or JVM evidence exists.
- [Risk] Installer inventory drifts from the actual agent bundle. -> Update XML includes, inventory assets, and focused tests together.

## Migration Plan

1. Add `robot-no-java.md` to canonical agent assets.
2. Update `robot-tech-lead.md` and `implement-issue.md` delegation references.
3. Update agent installer XML and inventory templates.
4. Update focused generator tests.
5. Validate XML, OpenSpec, Markdown, and focused generator behavior.
