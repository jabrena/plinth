# Getting Started with Agents for Java

The embedded agents separate analysis, architecture, technical leadership, and implementation. Install them with `@005-agents-installation`; the canonical definitions live under `skills-generator/src/main/resources/skill-references/assets/agents/`.

## Agent Missions

| Agent | Missions | Usage |
| --- | --- | --- |
| `robot-business-analyst` | Create or refine GitHub/Jira issues.<br>Perform read-only alignment, traceability, and readiness reviews. | Use `/create-issue` or `/review-alignment`. It does not implement code or silently correct artifacts. |
| `robot-architect` | Explore design alternatives.<br>Create ADRs.<br>Create architecture diagrams. | Use `/explore-design`, `/create-adr`, or `/create-diagram`. It hands approved constraints to the tech lead. |
| `robot-tech-lead` | Create implementation plans.<br>Create OpenSpec changes.<br>Coordinate delivery.<br>Select and delegate to implementation agents.<br>Track implementation and verification. | Use `/create-plan`, `/create-spec`, or provide an approved plan/OpenSpec task list for delivery. |
| `robot-java-coder` | Implement framework-neutral Java and Maven work. | Delegation target selected by the tech lead. |
| `robot-spring-boot-coder` | Implement Spring Boot work. | Delegation target selected by the tech lead. |
| `robot-quarkus-coder` | Implement Quarkus work. | Delegation target selected by the tech lead. |
| `robot-micronaut-coder` | Implement Micronaut work. | Delegation target selected by the tech lead. |

The business analyst, architect, and tech lead do not replace coder agents. The tech lead selects one implementation agent from repository evidence and delegates parallel groups only when dependencies and file ownership permit it.

## Migration

`robot-coordinator` was renamed to `robot-tech-lead`. There is no compatibility alias. After reinstalling the bundle:

1. Replace direct `@robot-coordinator` mentions with `@robot-tech-lead`.
2. Replace references to `robot-coordinator.md` with `robot-tech-lead.md`.
3. Keep the existing delegation model: coder agents remain implementation targets.

## Examples

- `Using @robot-business-analyst, create a GitHub issue from these requirements.`
- `Using @robot-architect, explore design alternatives for issue #806.`
- `Using @robot-tech-lead, create an OpenSpec change directly from this approved issue.`
- `Using @robot-tech-lead, deliver the selected OpenSpec tasks and delegate each implementation group.`

See [Project Workflows](GETTING-STARTED-WORKFLOWS.md) for lifecycle paths and artifact authority.
