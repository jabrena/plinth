# Comandos, Agentes y Skills para Java

<a href="https://trendshift.io/repositories/15013" target="_blank"><img src="https://trendshift.io/api/badge/repositories/15013" alt="jabrena%2Fcursor-rules-java | Trendshift" style="width: 250px; height: 55px;" width="250" height="55"/></a>

[![Stargazers over time](https://starchart.cc/jabrena/cursor-rules-java.svg?variant=adaptive)](https://starchart.cc/jabrena/cursor-rules-java)

[![CI Builds](https://github.com/jabrena/cursor-rules-java/actions/workflows/maven.yaml/badge.svg)](https://github.com/jabrena/cursor-rules-java/actions/workflows/maven.yaml)

> **Idiomas:** [English](./README.md) · [中文](./README_ZH.md)
>
> **Website:** https://jabrena.github.io/cursor-rules-java/
>
> **Apoya el proyecto:** [Sponsor to pay tokens](https://github.com/sponsors/jabrena)

## Objetivo

Un flujo de trabajo nativo de IA, con criterio propio, para evolucionar las prácticas modernas de `SDLC` en Java Enterprise mediante `Skills`, `Agents`, `Commands` y servidores `MCP` reutilizables.

## Empieza en 60 segundos

Instala todos los skills para tu agente preferido:

```bash
npx skills add jabrena/cursor-rules-java --skill '*' --agent cursor -y
```

Sustituye `cursor` por `claude-code`, `codex` o `github-copilot` cuando sea necesario. Consulta la [guía de 5 minutos](./documentation/guides/GETTING-STARTED-IN-5-MINUTES_ES.md) para instalar commands, agents y explorar otras opciones.

### Míralo en acción

Pide a tu agente:

```text
Usa @110-java-maven-best-practices para revisar este proyecto Maven.
Explica los hallazgos, aplica las mejoras aprobadas y valida el build.
```

El skill guía al agente mediante una revisión estructurada de Maven mientras tú mantienes el control sobre los cambios propuestos.

Te ayuda a responder los [Five whys](https://en.wikipedia.org/wiki/Five_whys) cuando tu equipo necesita evolucionar un producto o servicio basado en Java:

| PREGUNTA   | ROL                | ÁREA             | SOPORTE                                                                                                                                                                                                             |
| ---------- | ------------------ | ---------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| QUÉ / CUÁNDO | PO, BA, EA, SA, TL | Agile & Planning | `User Stories`, `GitHub Issues` y `Jira` |
| POR QUÉ    | EA, SL, TL         | Architecture     | `ADRs` y diagramas `UML` / `C4` / `ER` |
| CÓMO | SA, TL, SWE | Spec-Driven      | `AI Plan mode` y `OpenSpec` |
| CÓMO       | TL, SWE            | Java development | `Build system based on Maven`, `Design`, `Coding`, `Testing`, `Observability`, `Refactoring & JMH Benchmarking`, `Performance testing with JMeter`, `Profiling with Async profiler/OpenJDK tools`, `Documentation`, `Spring Boot`, `Quarkus`, `Micronaut`, `OpenAPI`, `WireMock` y `AGENTS.md` |

Una vez clara la idea, puedes implementarla de forma estructurada:

|               | Análisis / Diseño | Implementación | Operación |
| ------------- | ----------------- | -------------- | --------- |
| Commands      | `/create-issue` · [`/update-issue`](./.cursor/commands/update-issue.md) · `/explore-design` · `/create-adr` · `/create-diagram` · `/create-plan` · `/create-spec` · `/review-alignment` | [`/create-feature-branch`](./.cursor/commands/create-feature-branch.md) · [`/create-worktree`](./.cursor/commands/create-worktree.md) · [`/implement-issue`](./.cursor/commands/implement-issue.md) · [`/kill-port`](./.cursor/commands/kill-port.md) | [`/profile`](./.cursor/commands/profile.md) · [`/benchmark`](./.cursor/commands/benchmark.md) |
| [Agents](./documentation/guides/GETTING-STARTED-AGENTS_ES.md)        | `@robot-business-analyst` · `@robot-architect` · `@robot-tech-lead` | `@robot-tech-lead` · `@robot-no-java` · `@robot-java-coder` · `@robot-java-spring-boot-coder` · `@robot-java-quarkus-coder` · `@robot-java-micronaut-coder` | `@robot-java-performance` |
| [Skills](./documentation/guides/GETTING-STARTED-SKILLS_ES.md)        | [014-agile-user-story](https://www.skills.sh/jabrena/cursor-rules-java/014-agile-user-story) · [030-architecture-adr-general](https://www.skills.sh/jabrena/cursor-rules-java/030-architecture-adr-general) · [033-architecture-diagrams](https://www.skills.sh/jabrena/cursor-rules-java/033-architecture-diagrams) · [041-planning-plan-mode](https://www.skills.sh/jabrena/cursor-rules-java/041-planning-plan-mode) · [200-agents-md](https://www.skills.sh/jabrena/cursor-rules-java/200-agents-md) ... | [110-java-maven-best-practices](https://www.skills.sh/jabrena/cursor-rules-java/110-java-maven-best-practices) · [121-java-object-oriented-design](https://www.skills.sh/jabrena/cursor-rules-java/121-java-object-oriented-design) · [124-java-secure-coding](https://www.skills.sh/jabrena/cursor-rules-java/124-java-secure-coding) · [111-java-maven-dependencies](https://www.skills.sh/jabrena/cursor-rules-java/111-java-maven-dependencies) · [143-java-functional-exception-handling](https://www.skills.sh/jabrena/cursor-rules-java/143-java-functional-exception-handling) ... | [151-java-performance-jmeter](https://www.skills.sh/jabrena/cursor-rules-java/151-java-performance-jmeter) · [162-java-profiling-analyze](https://www.skills.sh/jabrena/cursor-rules-java/162-java-profiling-analyze) · [161-java-profiling-detect](https://www.skills.sh/jabrena/cursor-rules-java/161-java-profiling-detect) · [163-java-profiling-refactor](https://www.skills.sh/jabrena/cursor-rules-java/163-java-profiling-refactor) · [164-java-profiling-verify](https://www.skills.sh/jabrena/cursor-rules-java/164-java-profiling-verify) ... |
| [MCP Servers](./documentation/guides/THIRD-PARTIES.md)   | [Jbang-Quarkus-JDBC](https://github.com/quarkiverse/quarkus-mcp-servers/blob/main/jdbc/README.md) · [MongoDB](https://github.com/mongodb-js/mongodb-mcp-server) · [Serena-LSP](https://oraios.github.io/serena/01-about/000_intro.html) | [Jbang-Quarkus-JDBC](https://github.com/quarkiverse/quarkus-mcp-servers/blob/main/jdbc/README.md) · [MongoDB](https://github.com/mongodb-js/mongodb-mcp-server) · [JavaDocs](https://www.javadocs.dev/mcp) · [Serena-LSP](https://oraios.github.io/serena/01-about/000_intro.html) | [Jbang-Quarkus-JDBC](https://github.com/quarkiverse/quarkus-mcp-servers/blob/main/jdbc/README.md) · [MongoDB](https://github.com/mongodb-js/mongodb-mcp-server) · [Serena-LSP](https://oraios.github.io/serena/01-about/000_intro.html) · [Graphana](https://grafana.com/docs/grafana/latest/developer-resources/mcp/) |

## Entregables

El proyecto genera un conjunto de entregables al final de cualquier iteración.

| Inventario     | Instalación                                                                                    | Primeros pasos                                                                           |
| --------------- | -------------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------- |
| 1. [Commands](./documentation/guides/INVENTORY-COMMANDS-JAVA.md) | `@004-commands-installation` Instalar Commands en el proyecto | [`Commands`](./documentation/guides/COMMANDS.md) |
| 2. [Agents](./documentation/guides/INVENTORY-AGENTS-JAVA.md) | `@005-agents-installation` Instalar Agents en Cursor/Claude | [`Agents`](./documentation/guides/GETTING-STARTED-AGENTS_ES.md)     |
| 3. [Skills](./documentation/guides/INVENTORY-SKILLS-JAVA.md) | `npx skills add jabrena/cursor-rules-java --skill '*' --agent cursor -y` | [`Skills`](./documentation/guides/GETTING-STARTED-SKILLS_ES.md)     |

### Compatibilidad

Este proyecto es compatible con cualquier herramienta que admita `Commands`, `Agents`, `Skills`, `MCP Servers` y `AGENTS.md`.

## Primeros pasos en 5 minutos

Aprende a usar este proyecto siguiendo la guía rápida [Primeros pasos en 5 minutos](./documentation/guides/GETTING-STARTED-IN-5-MINUTES_ES.md).

### Migración desde las reglas heredadas

Los `System prompts/rules` actuales están deprecados y se eliminarán en `v0.16.0`. Si todavía los usas, revisa el [artículo de la versión 0.14.0](https://jabrena.github.io/cursor-rules-java/blog/2026/04/release-0.14.0.html).

## Validaciones de Skills

Cada push ejecuta las siguientes validaciones en [CI Builds](./.github/workflows/maven.yaml) para mantener la documentación y los skills generados correctos, consistentes y seguros:

| Nombre | Propósito |
| --- | --- |
| 1. [MarkdownValidator](./.github/scripts/MarkdownValidator.java) | Protege la capa de documentación al detectar desviaciones de parseo Markdown y fallos en enlaces remotos antes de las validaciones específicas de skills. |
| 2. [skill-check](https://github.com/thedaviddias/skill-check) | Confirma que cada skill generado cumple el contrato esperado de empaquetado, complementando los scanners centrados en comportamiento o riesgo de seguridad. |
| 3. [cisco-ai-skill-scanner](https://github.com/cisco-ai-defense/skill-scanner) de Cisco | Añade cobertura de seguridad orientada al comportamiento al buscar flujos de skills riesgosos que la validación estructural no puede ver. |
| 4. [SkillSpector](https://github.com/NVIDIA/SkillSpector) de NVIDIA | Aporta una revisión estática independiente de calidad y seguridad, útil para contrastar hallazgos con los otros scanners. |
| 5. [Snyk Agent Scan](https://github.com/snyk/agent-scan) de SNYK | Se centra en señales de cadena de suministro y riesgos de prompt en agent skills, añadiendo otra perspectiva de seguridad junto a Cisco y SkillSpector. |

## Limitaciones

### Falta de determinismo

Desde el principio, ten presente que los resultados de las interacciones con estos `Skills` y agents no son deterministas por el comportamiento de los modelos, pero puedes mitigarlo con objetivos claros y puntos de validación.

### No todos los modelos se comportan igual

Algunos skills interactivos requieren modelos `Premium` para uso interactivo; de lo contrario siguen una secuencia fija de pasos.

### Límites de las interacciones con modelos

Los modelos pueden generar código, pero no pueden ejecutarlo contra tus datos locales. Para cerrar esa brecha, algunos Skills incluyen scripts que ejecutas localmente.

### Los ingenieros de software deben permanecer en el proceso

Este proyecto apoya el trabajo de ingeniería de software, pero no sustituye el criterio profesional. Un ingeniero de software debe revisar, guiar y validar las decisiones, el código y los resultados generados por IA antes de utilizarlos.

### Acceso a datos corporativos

Actúa con precaución cuando un problema involucre bases de datos corporativas u otros datos sensibles de la organización. Antes de conceder acceso a un flujo de trabajo asistido por IA, evalúa los riesgos de autorización, privacidad, filtración, retención y modificación accidental de datos. Aplica acceso con privilegios mínimos, revisión humana, validación y monitorización. Consulta [OWASP GenAI Data Security Risks & Mitigations 2026](https://genai.owasp.org/resource/owasp-genai-data-security-risks-mitigations-2026/) y [The EU Artificial Intelligence Act](https://artificialintelligenceact.eu/).

## Contribuir

- Sigue la [guía de 5 minutos](./documentation/guides/GETTING-STARTED-IN-5-MINUTES_ES.md) y cuéntanos dónde puede mejorar la experiencia.
- [Explora el inventario de skills](./documentation/guides/INVENTORY-SKILLS-JAVA.md) y propón un flujo de trabajo Java que falte.
- [Abre una issue](https://github.com/jabrena/cursor-rules-java/issues) para informar de un problema o sugerir una mejora.
- Lee [CONTRIBUTING.md](./CONTRIBUTING.md) para mejorar un skill, agent, command o guía del proyecto.
- Da una estrella al repositorio si estos flujos ayudan a tus proyectos Java.

## Architecture Decision Records (ADR)

- Revisa el [índice de ADR](./documentation/adr/README.md) para la lista completa.

## Changelog

- Revisa el [CHANGELOG](./CHANGELOG.md) para más detalles.

## Java JEPs desde Java 8

Java usa JEPs (JDK Enhancement Proposals) para describir nuevas características del lenguaje y la plataforma. Este repositorio hace seguimiento de qué JEPs podrían mejorar los Skills y la guía aquí incluida.

- [Lista de JEPs](./documentation/jeps/All-JEPS.md)

## Recursos adicionales

Las charlas, artículos, enlaces de referencia, portales de skills y proyectos relacionados están en [Referencias del proyecto](./documentation/guides/PROJECT-REFERENCES_ES.md).

Desarrollado por personas con el apoyo de [Cursor](https://www.cursor.com/) y [Codex](https://openai.com/codex/), con ❤️ desde [Madrid](https://www.google.com/maps/place/Community+of+Madrid,+Madrid/@40.4983324,-6.3162283,8z/data=!3m1!4b1!4m6!3m5!1s0xd41817a40e033b9:0x10340f3be4bc880!8m2!3d40.4167088!4d-3.5812692!16zL20vMGo0eGc?entry=ttu&g_ep=EgoyMDI1MDgxOC4wIKXMDSoASAFQAw%3D%3D)
