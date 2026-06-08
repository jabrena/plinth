## Why

The tech lead currently falls back to `@robot-java-coder` when a selected issue, plan, or OpenSpec task list does not match Spring Boot, Quarkus, or Micronaut. That is appropriate for plain Java or Maven work, but it is not a safe default for issues that are explicitly non-Java or have no Java/JVM scope. A neutral fallback agent keeps routing honest and prevents Java-specific assumptions from leaking into non-Java work.

## What Changes

- Add `robot-no-java.md` as a non-Java/default delegation target.
- Update `robot-tech-lead.md` so non-Java issue/plan/spec work routes to `@robot-no-java`.
- Keep plain Java, Maven, and framework-neutral Java work routed to `@robot-java-coder`.
- Update the embedded agent installer XML and embedded agent inventory assets to include the new agent.
- Update command/agent contracts and focused generator tests so the new routing is covered.

## Capabilities

### New Capabilities

None.

### Modified Capabilities

- `analysis-design-agents`: Adds a non-Java fallback delegation target to the embedded agent bundle and tech lead routing.

## Source and Derivation

- Source artifact: user request in this conversation to route issue/plan/spec work that does not use Java to `robot-no-java.md`.
- Derivation direction: user request -> OpenSpec change -> generator XML/assets/tests.

## Impact

Canonical agent assets, command assets, agent installer XML, inventory templates, and focused generator tests are affected. Generated `skills/`, `.agents/skills/`, and installed `.cursor` outputs are not the source of truth for this change.
