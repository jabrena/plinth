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
| [Agents](./documentation/guides/GETTING-STARTED-AGENTS_ES.md)        | `@robot-business-analyst` · `@robot-architect` · `@robot-tech-lead` | `@robot-tech-lead` · `@robot-java-coder` · `@robot-java-spring-boot-coder` · `@robot-java-quarkus-coder` · `@robot-java-micronaut-coder` | `@robot-java-performance` |
| [Skills](./documentation/guides/GETTING-STARTED-SKILLS_ES.md)        | [014-agile-user-story](https://www.skills.sh/jabrena/cursor-rules-java/014-agile-user-story) · [030-architecture-adr-general](https://www.skills.sh/jabrena/cursor-rules-java/030-architecture-adr-general) · [033-architecture-diagrams](https://www.skills.sh/jabrena/cursor-rules-java/033-architecture-diagrams) · `034-architecture-design-exploration` · [041-planning-plan-mode](https://www.skills.sh/jabrena/cursor-rules-java/041-planning-plan-mode) · `042-planning-openspec` ... | [110-java-maven-best-practices](https://www.skills.sh/jabrena/cursor-rules-java/110-java-maven-best-practices) · [121-java-object-oriented-design](https://www.skills.sh/jabrena/cursor-rules-java/121-java-object-oriented-design) · [124-java-secure-coding](https://www.skills.sh/jabrena/cursor-rules-java/124-java-secure-coding) · [111-java-maven-dependencies](https://www.skills.sh/jabrena/cursor-rules-java/111-java-maven-dependencies) · [143-java-functional-exception-handling](https://www.skills.sh/jabrena/cursor-rules-java/143-java-functional-exception-handling) ... | [151-java-performance-jmeter](https://www.skills.sh/jabrena/cursor-rules-java/151-java-performance-jmeter) · [162-java-profiling-analyze](https://www.skills.sh/jabrena/cursor-rules-java/162-java-profiling-analyze) · [161-java-profiling-detect](https://www.skills.sh/jabrena/cursor-rules-java/161-java-profiling-detect) · [163-java-profiling-refactor](https://www.skills.sh/jabrena/cursor-rules-java/163-java-profiling-refactor) · [164-java-profiling-verify](https://www.skills.sh/jabrena/cursor-rules-java/164-java-profiling-verify) ... |
| [MCP Servers](./documentation/guides/THIRD-PARTIES.md)   | [JDBC](https://github.com/quarkiverse/quarkus-mcp-servers/blob/main/jdbc/README.md) | [JavaDocs](https://www.javadocs.dev/mcp) · [Serena](https://oraios.github.io/serena/01-about/000_intro.html) | [Graphana](https://grafana.com/docs/grafana/latest/developer-resources/mcp/) |

## Entregables

El proyecto genera un conjunto de entregables al final de cualquier iteración.

| Inventario     | Instalación                                                                                    | Primeros pasos                                                                           |
| --------------- | -------------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------- |
| 1. [Commands](./documentation/guides/INVENTORY-COMMANDS-JAVA.md) | `@004-commands-installation` Instalar Commands en el proyecto | [`Commands`](./documentation/guides/COMMANDS.md) |
| 2. [Agents](./documentation/guides/INVENTORY-AGENTS-JAVA.md) | `@005-agents-installation` Instalar Agents en Cursor/Claude | [`Agents`](./documentation/guides/GETTING-STARTED-AGENTS_ES.md)     |
| 3. [Skills](./documentation/guides/INVENTORY-SKILLS-JAVA.md) | `npx skills add jabrena/cursor-rules-java --all --agent cursor` | [`Skills`](./documentation/guides/GETTING-STARTED-SKILLS_ES.md)     |

**⚠️ Nota:** Si sigues usando los System prompts/rules de este proyecto, revisa [el artículo](https://jabrena.github.io/cursor-rules-java/blog/2026/04/release-0.14.0.html). Los `System prompts/rules` actuales se eliminarán en la próxima versión (v0.16.0).

### Compatibilidad

Este proyecto es compatible con cualquier herramienta que admita `Commands`, `Agents`, `Skills`, `MCP Servers` y `AGENTS.md`.

## Primeros pasos en 5 minutos

Aprende a usar este proyecto siguiendo la guía rápida [Primeros pasos en 5 minutos](./documentation/guides/GETTING-STARTED-IN-5-MINUTES_ES.md).

## Flujos de trabajo

Consulta la [guía de flujos de trabajo del proyecto](./documentation/guides/GETTING-STARTED-WORKFLOWS_ES.md) para conocer los flujos de prompting, ingeniería dirigida por agents y pipelines.

## Validaciones de Skills

Cada push ejecuta validaciones enfocadas en skills dentro de [CI Builds](./.github/workflows/maven.yaml) para mantener la calidad y la corrección:

- `skill-check` valida la estructura y los metadatos de los `SKILL.md` generados con `npx skill-check@latest .agents/skills --no-security-scan --format github`.
- `cisco-ai-skill-scanner` ejecuta un análisis de comportamiento con la política strict y falla ante hallazgos de severidad alta.
- SkillSpector genera un informe Markdown sin LLM para una inspección adicional de calidad de los skills y lo sube como artefacto del workflow.
- Snyk Agent Scan analiza la cadena de suministro de `.agents/skills` generados para detectar riesgos de seguridad en agent skills con `uvx snyk-agent-scan@latest scan .agents/skills --ci --no-bootstrap --dangerously-run-mcp-servers`; CI requiere el secret `SNYK_TOKEN`.

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

Consulta [CONTRIBUTING.md](./CONTRIBUTING.md) para convenciones, flujos del generador, pruebas y cómo abrir un pull request.

## Architecture Decision Records (ADR)

- Revisa el [índice de ADR](./documentation/adr/README.md) para la lista completa.

## Changelog

- Revisa el [CHANGELOG](./CHANGELOG.md) para más detalles.

## Java JEPs desde Java 8

Java usa JEPs (JDK Enhancement Proposals) para describir nuevas características del lenguaje y la plataforma. Este repositorio hace seguimiento de qué JEPs podrían mejorar los Skills y la guía aquí incluida.

- [Lista de JEPs](./documentation/jeps/All-JEPS.md)

## Meetups, conferencias, talleres y artículos

### Codemotion / Madrid (2026/04/20 - 11:00 - 12:30)

- [Taller técnico sobre Cursor para el desarrollo con Java](https://conferences.codemotion.com/madrid/speakers/)

### W-JAX / Munich (2025/11/06 - 10:30 - 11:30)

- [https://jax.de/generative-ai-ecosystem/cursor-ai-101-java-enterprise/](https://jax.de/generative-ai-ecosystem/cursor-ai-101-java-enterprise/)

### Devoxx BE / Antwerp (2025/10/07 - 18:20 - 18:50)

- [https://m.devoxx.com/events/dvbe25/talks/4715/the-power-of-cursor-rules-in-java-enterprise-development](https://m.devoxx.com/events/dvbe25/talks/4715/the-power-of-cursor-rules-in-java-enterprise-development)

### Madrid Jug / Madrid (2025/05/06 - 19:00)

- [https://www.meetup.com/es-ES/madridjug/events/307458529/](https://www.meetup.com/es-ES/madridjug/events/307458529/)

### Blogs

- [Delegating Java tasks to Supervised AI Dev Pipelines](https://www.javaadvent.com/2025/12/delegating-java-tasks-to-supervised-ai-dev-pipelines.html)
- [https://vibekode.it/blog/cursor-ai-developer-cloud-platform/](https://vibekode.it/blog/cursor-ai-developer-cloud-platform/)
- [https://www.linkedin.com/pulse/september-rest-story-jvm-weekly-vol-146-artur-skowro%C5%84ski-82lif/?trackingId=wbWPSL65TpCCbdg5ksAWjw%3D%3D](https://www.linkedin.com/pulse/september-rest-story-jvm-weekly-vol-146-artur-skowro%C5%84ski-82lif/?trackingId=wbWPSL65TpCCbdg5ksAWjw%3D%3D)
- [https://virtuslab.com/blog/ai/providing-library-documentation/](https://virtuslab.com/blog/ai/providing-library-documentation/)
- https://github.com/the911fund/skill-of-skills
- https://blog.csdn.net/weixin_42526249/article/details/161176209
- https://juejin.cn/post/7632095808490340392

## Referencias

**Infraestructura de AI Agents:**

- [https://www.cursor.com/](https://www.cursor.com/)
- [https://cursor.com/cli](https://cursor.com/cli)
- [https://www.anthropic.com/claude-code](https://www.anthropic.com/claude-code)
- [https://github.com/features/copilot](https://github.com/features/copilot)
- [https://cursor.com/docs/cli/github-actions](https://cursor.com/docs/cli/github-actions)
- [https://code.claude.com/docs/en/github-actions](https://code.claude.com/docs/en/github-actions)

**Estándares:**

- [https://agents.md/](https://agents.md/)
- [https://agentskills.io/home](https://agentskills.io/home)
- [https://microsoft.github.io/language-server-protocol/](https://microsoft.github.io/language-server-protocol/)
- [https://openspec.dev/](https://openspec.dev/)

**Portales de Skills:**

- [https://skills.sh/jabrena/cursor-rules-java](https://skills.sh/jabrena/cursor-rules-java)
- [https://github.com/vercel-labs/skills/issues](https://github.com/vercel-labs/skills/issues)
- [https://tessl.io/registry/skills/github/jabrena/cursor-rules-java](https://tessl.io/registry/skills/github/jabrena/cursor-rules-java)
- [https://claudskills.com/author/jabrena/](https://claudskills.com/author/jabrena/)
- [https://agent-skills.cc/zh/skills/jabrena-cursor-rules-java](https://agent-skills.cc/zh/skills/jabrena-cursor-rules-java)
- [https://shyft.ai/skills/cursor-rules-java](https://shyft.ai/skills/cursor-rules-java)
- [https://lobehub.com/skills?q=cursor-rules-java](https://lobehub.com/skills?q=cursor-rules-java)
- [https://www.awesomeskills.dev/es/skill/jabrena-cursor-rules-java](https://www.awesomeskills.dev/es/skill/jabrena-cursor-rules-java)
- [https://github.com/laolaoshiren/claude-code-skills-zh](https://github.com/laolaoshiren/claude-code-skills-zh)
- [https://github.com/LessUp/awesome-cursorrules-zh](https://github.com/LessUp/awesome-cursorrules-zh)
- [https://www.learn-skills.dev/en/for/jabrena](https://www.learn-skills.dev/en/for/jabrena)
- [https://skillsllm.com/skill/cursor-rules-java](https://skillsllm.com/skill/cursor-rules-java)

**Java:**

- [https://openjdk.org/jeps/0](https://openjdk.org/jeps/0)
- [https://jbake.org/docs/latest/](https://jbake.org/docs/latest/)
- [https://developers.redhat.com/blog/2016/12/09/spring-cloud-for-microservices-compared-to-kubernetes](https://developers.redhat.com/blog/2016/12/09/spring-cloud-for-microservices-compared-to-kubernetes)

## Otros desarrollos

- [https://github.com/jabrena/pml](https://github.com/jabrena/pml)
- [https://github.com/jabrena/cursor-rules-java](https://github.com/jabrena/cursor-rules-java)
- https://github.com/jabrena/ai-agent-harness-monitor-cli
- [https://github.com/jabrena/setup-cli](https://github.com/jabrena/setup-cli)

Desarrollado con 2 manos humanas, [Cursor](https://www.cursor.com/) & [Codex](https://openai.com/codex/) con ❤️ desde [Madrid](https://www.google.com/maps/place/Community+of+Madrid,+Madrid/@40.4983324,-6.3162283,8z/data=!3m1!4b1!4m6!3m5!1s0xd41817a40e033b9:0x10340f3be4bc880!8m2!3d40.4167088!4d-3.5812692!16zL20vMGo0eGc?entry=ttu&g_ep=EgoyMDI1MDgxOC4wIKXMDSoASAFQAw%3D%3D)
