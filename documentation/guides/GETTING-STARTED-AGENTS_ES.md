# Primeros pasos con Agents for Java

Los agentes integrados separan análisis, arquitectura, liderazgo técnico e implementación. Instálalos con `@005-agents-installation`; las definiciones canónicas están en `skills-generator/src/main/resources/skill-references/assets/agents/`.

## Misiones de los agentes

| Agent | Misiones | Uso |
| --- | --- | --- |
| `robot-business-analyst` | Crear o refinar issues en GitHub/Jira.<br>Realizar revisiones de alineación, trazabilidad y preparación en modo solo lectura. | Usa `/create-issue` o `/review-alignment`. No implementa código ni corrige artefactos de forma silenciosa. |
| `robot-architect` | Explorar alternativas de diseño.<br>Crear ADRs.<br>Crear diagramas de arquitectura. | Usa `/explore-design`, `/create-adr` o `/create-diagram`. Entrega las restricciones aprobadas al tech lead. |
| `robot-tech-lead` | Crear planes de implementación.<br>Crear cambios OpenSpec.<br>Coordinar la entrega.<br>Seleccionar y delegar en agentes de implementación.<br>Controlar implementación y verificación. | Usa `/create-plan`, `/create-spec` o proporciona un plan/lista de tareas OpenSpec aprobada para la entrega. |
| `robot-java-coder` | Implementar trabajo Java y Maven independiente del framework. | Objetivo de delegación seleccionado por el tech lead. |
| `robot-spring-boot-coder` | Implementar trabajo Spring Boot. | Objetivo de delegación seleccionado por el tech lead. |
| `robot-quarkus-coder` | Implementar trabajo Quarkus. | Objetivo de delegación seleccionado por el tech lead. |
| `robot-micronaut-coder` | Implementar trabajo Micronaut. | Objetivo de delegación seleccionado por el tech lead. |

El business analyst, architect y tech lead no sustituyen a los coder agents. El tech lead selecciona un agente de implementación usando evidencias del repositorio y solo delega grupos en paralelo cuando las dependencias y la propiedad de archivos lo permiten.

## Migración

`robot-coordinator` se renombró a `robot-tech-lead`. No existe un alias de compatibilidad. Después de reinstalar el paquete:

1. Sustituye las menciones directas a `@robot-coordinator` por `@robot-tech-lead`.
2. Sustituye referencias a `robot-coordinator.md` por `robot-tech-lead.md`.
3. Mantén el modelo de delegación existente: los coder agents siguen siendo objetivos de implementación.

## Ejemplos

- `Using @robot-business-analyst, create a GitHub issue from these requirements.`
- `Using @robot-architect, explore design alternatives for issue #806.`
- `Using @robot-tech-lead, create an OpenSpec change directly from this approved issue.`
- `Using @robot-tech-lead, deliver the selected OpenSpec tasks and delegate each implementation group.`

Consulta [Flujos de trabajo del proyecto](GETTING-STARTED-WORKFLOWS_ES.md) para ver las rutas del ciclo de vida y la autoridad de los artefactos.
