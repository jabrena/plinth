# Primeros pasos con Agents for Java

Si quieres usar las definiciones de **Agents for Java** incluidas en este repositorio, lee este documento. Cubre los agentes seleccionados en [`.cursor/agents`](../../.cursor/agents) que se usan en flujos de entrega Java Enterprise (excluyendo el especialista en diagramas; consulta la nota más abajo).

## Conceptos relacionados con este proyecto

### ¿Qué es un Cursor Agent?

En Cursor, un **agent** es una persona reutilizable definida como un archivo Markdown con frontmatter YAML (`name`, `model`, `description` y flags opcionales como `readonly`). Cursor carga estos archivos desde `.cursor/agents/`. Cada archivo de agente define el rol, las responsabilidades, las restricciones y qué Skills o rules aplicar, para que el modelo se comporte de forma consistente en ese tipo de tarea.

### Cómo encajan estos agentes

- **Calidad de requisitos:** [robot-business-analyst](../../.cursor/agents/robot-business-analyst.md) es **read-only**: revisa user stories, planes y ADRs para detectar alineación, brechas y trazabilidad. Es útil antes de la aprobación o cuando los documentos no coinciden.
- **Orquestación:** [robot-coordinator](../../.cursor/agents/robot-coordinator.md) lee planes de implementación (especialmente tablas con una columna **Parallel**), identifica el stack (Spring Boot, Quarkus, Micronaut o Java plano) y **delega** el trabajo a exactamente un agente de implementación durante la intervención. No implementa código por sí mismo.
- **Implementación (solo por delegación):** [robot-java-coder](../../.cursor/agents/robot-java-coder.md), [robot-spring-boot-coder](../../.cursor/agents/robot-spring-boot-coder.md), [robot-quarkus-coder](../../.cursor/agents/robot-quarkus-coder.md) y [robot-micronaut-coder](../../.cursor/agents/robot-micronaut-coder.md) son los especialistas a los que el **coordinator** delega. No deberías mencionarlos directamente con `@` para una implementación guiada por plan: empieza con [@robot-coordinator](../../.cursor/agents/robot-coordinator.md) para que elija el stack correcto y derive el trabajo (incluidos los grupos **Parallel**). Referencian el catálogo **Skills for Java** (por ejemplo `@301`-`@323` para Spring Boot) cuando corresponde.

**Nota:** El repositorio puede incluir otras definiciones de agentes en `.cursor/agents` (por ejemplo especialistas de arquitectura o diagramas). Esta guía cubre solo los agentes de **entrega Java y BA** listados a continuación.

### Dependencias con Skills y System prompts

Los agentes asumen que puedes adjuntar **Skills** (consulta [Primeros pasos con Skills for Java](GETTING-STARTED-SKILLS_ES.md)) y/o **Cursor rules** (consulta [Getting started for System prompts for Java](GETTING-STARTED-SYSTEM-PROMPTS.md)) para que las referencias `@...` se resuelvan en el chat. Los agentes de implementación nombran prefijos concretos de skills (por ejemplo `@322-frameworks-spring-boot-testing-integration-tests`).

## ¿Cómo instalar los Agents?

Los agents viven junto al resto de la configuración de Cursor del proyecto. Instálalos igual que instalas las **Cursor rules**: copia todo el directorio `.cursor` en la raíz de tu repositorio Java (o usa el mismo flujo que ya uses para `.cursor/rules`).

- **Desde un clon Git o ZIP:** Copia la carpeta [`.cursor/agents`](../../.cursor/agents) (o todo el árbol `.cursor`) en tu proyecto.
- **Configuración con JBang (rules + estructura Cursor):** Si usas la configuración descrita en [Getting started for System prompts for Java](GETTING-STARTED-SYSTEM-PROMPTS.md), asegúrate de que tu proceso también conserva o copia `.cursor/agents` al sincronizar desde este repositorio.

Después de la instalación deberías ver archivos como `robot-coordinator.md` dentro de `.cursor/agents/` en tu proyecto.

## Catálogo de agentes (entrega Java y BA)

Para la **implementación desde un plan**, tu punto de entrada es solo **[@robot-coordinator](../../.cursor/agents/robot-coordinator.md)**. Identifica el stack y delega al `robot-*-coder` correcto; no menciones con `@` a los agentes coder por tu cuenta en ese flujo.

| Agent | Rol | Cómo usarlo |
|-------|-----|-------------|
| [robot-business-analyst](../../.cursor/agents/robot-business-analyst.md) | Revisión BA: stories, plan y ADRs; **readonly**. | **Menciona este agente directamente con @** para revisión de requisitos (no forma parte de la delegación de implementación del coordinator). |
| [robot-coordinator](../../.cursor/agents/robot-coordinator.md) | Coordina trabajo desde `*.plan.md`; enruta a un implementador específico por framework; separa entregas por grupo **Parallel**. | **Menciona este agente con @** cuando tengas un plan estructurado (con **Parallel** / orden de ejecución cuando aplique) y quieras delegar la implementación en orden. |
| [robot-java-coder](../../.cursor/agents/robot-java-coder.md) | Especialista de implementación Java plano / Maven. | **Solo objetivo de delegación**: el coordinator asigna este agente cuando el stack es Java plano o neutral respecto al framework. |
| [robot-spring-boot-coder](../../.cursor/agents/robot-spring-boot-coder.md) | Implementación Spring Boot (REST, Data JDBC, test slices, etc.). | **Solo objetivo de delegación**: el coordinator asigna este agente cuando el stack es Spring Boot. |
| [robot-quarkus-coder](../../.cursor/agents/robot-quarkus-coder.md) | Implementación Quarkus (Jakarta REST, CDI, Panache/JDBC, tests de Quarkus). | **Solo objetivo de delegación**: el coordinator asigna este agente cuando el stack es Quarkus. |
| [robot-micronaut-coder](../../.cursor/agents/robot-micronaut-coder.md) | Implementación Micronaut (`@Controller`, Micronaut Data, tests con `HttpClient`). | **Solo objetivo de delegación**: el coordinator asigna este agente cuando el stack es Micronaut. |

## Usar tu primer Agent en Cursor

1. Abre tu repositorio Java, que ya debe tener `.cursor/agents` (y preferiblemente Skills / rules como se indicó antes).
2. Para **implementación**, **menciona solo @robot-coordinator** y adjunta el plan; describe hitos o restricciones en el mensaje. La responsabilidad del coordinator es delegar al `robot-*-coder` correcto (no menciones tú a esos coders con `@` para ejecutar el plan).
3. Para **revisión de requisitos**, **menciona @robot-business-analyst** directamente y adjunta o pega stories, planes y ADRs.

**Ejemplos:**

- *"Using @robot-coordinator, execute the plan at `path/to/PLAN-*.plan.md` and delegate implementation per Parallel groups."*
- *"Using @robot-business-analyst, review these files for traceability: [paste or attach stories, plan, ADRs]."*
