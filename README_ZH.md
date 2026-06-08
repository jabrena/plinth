# Java 技能与智能体

<a href="https://trendshift.io/repositories/15013" target="_blank"><img src="https://trendshift.io/api/badge/repositories/15013" alt="jabrena%2Fcursor-rules-java | Trendshift" style="width: 250px; height: 55px;" width="250" height="55"/></a>

[![Stargazers over time](https://starchart.cc/jabrena/cursor-rules-java.svg?variant=adaptive)](https://starchart.cc/jabrena/cursor-rules-java)

[![CI Builds](https://github.com/jabrena/cursor-rules-java/actions/workflows/maven.yaml/badge.svg)](https://github.com/jabrena/cursor-rules-java/actions/workflows/maven.yaml)

> **语言：** [English](./README.md) · [Español](./README_ES.md)
>
> **Website:** https://jabrena.github.io/cursor-rules-java/
>
> **支持项目：** [Sponsor to pay tokens](https://github.com/sponsors/jabrena)

## 目标

一套带有明确观点的 AI 原生工作流，通过可复用的 `Skills`、`Agents`、`Commands` 与 `MCP servers`，持续演进现代 Java 企业级 `SDLC` 实践。

当你的团队需要演进基于 Java 的产品或服务时，本项目可帮助你回答 [五个为什么](https://en.wikipedia.org/wiki/Five_whys)：

| 问题       | 角色               | 领域             | 支持内容                                                                                                                                                                                                             |
| ---------- | ------------------ | ---------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| 什么 / 何时 | PO, BA, EA, SA, TL | Agile & Planning | `User Stories`、`GitHub Issues` 与 `Jira` |
| 为什么     | EA, SL, TL         | Architecture     | `ADRs` 与 `UML` / `C4` / `ER` 图表 |
| 如何 | SA, TL, SWE | Spec-Driven      | `AI Plan mode` 与 `OpenSpec` |
| 如何       | TL, SWE            | Java development | 基于 Maven 的构建系统、`Design`、`Coding`、`Testing`、`Observability`、`Refactoring & JMH Benchmarking`、JMeter 性能测试、Async profiler/OpenJDK 工具性能分析、`Documentation`、`Spring Boot`、`Quarkus`、`Micronaut`、`OpenAPI`、`WireMock` 与 `AGENTS.md` |

想法清晰后，你可以用结构化方式实现它：

|               | 分析 / 设计 | 实现 | 运维 |
| ------------- | ----------------- | -------------- | --------- |
| Commands      | `/create-issue` · `/create-worktree` · `/explore-design` · `/create-adr` · `/create-diagram` · `/create-plan` · `/create-spec` · `/review-alignment` | [`/create-feature-branch`](./.cursor/commands/create-feature-branch.md) · [`/implement-issue`](./.cursor/commands/implement-issue.md) | [`/verify`](./.cursor/commands/verify.md) · [`/kill-port`](./.cursor/commands/kill-port.md) |
| [Agents](./documentation/guides/GETTING-STARTED-AGENTS_ZH.md)        | `@robot-business-analyst` · `@robot-architect` · `@robot-tech-lead` | `@robot-tech-lead` · `@robot-java-coder` · `@robot-spring-boot-coder` · `@robot-quarkus-coder` · `@robot-micronaut-coder` |  |
| [Skills](./documentation/guides/GETTING-STARTED-SKILLS_ZH.md)        | [014-agile-user-story](https://www.skills.sh/jabrena/cursor-rules-java/014-agile-user-story) · [030-architecture-adr-general](https://www.skills.sh/jabrena/cursor-rules-java/030-architecture-adr-general) · [031-architecture-adr-functional-requirements](https://www.skills.sh/jabrena/cursor-rules-java/031-architecture-adr-functional-requirements) · [033-architecture-diagrams](https://www.skills.sh/jabrena/cursor-rules-java/033-architecture-diagrams) · [041-planning-plan-mode](https://www.skills.sh/jabrena/cursor-rules-java/041-planning-plan-mode) | [110-java-maven-best-practices](https://www.skills.sh/jabrena/cursor-rules-java/110-java-maven-best-practices) · [121-java-object-oriented-design](https://www.skills.sh/jabrena/cursor-rules-java/121-java-object-oriented-design) · [124-java-secure-coding](https://www.skills.sh/jabrena/cursor-rules-java/124-java-secure-coding) · [111-java-maven-dependencies](https://www.skills.sh/jabrena/cursor-rules-java/111-java-maven-dependencies) · [143-java-functional-exception-handling](https://www.skills.sh/jabrena/cursor-rules-java/143-java-functional-exception-handling) | [151-java-performance-jmeter](https://www.skills.sh/jabrena/cursor-rules-java/151-java-performance-jmeter) · [162-java-profiling-analyze](https://www.skills.sh/jabrena/cursor-rules-java/162-java-profiling-analyze) · [161-java-profiling-detect](https://www.skills.sh/jabrena/cursor-rules-java/161-java-profiling-detect) · [163-java-profiling-refactor](https://www.skills.sh/jabrena/cursor-rules-java/163-java-profiling-refactor) · [164-java-profiling-verify](https://www.skills.sh/jabrena/cursor-rules-java/164-java-profiling-verify) |
| [MCP Servers](./documentation/guides/THIRD-PARTIES.md)   | [JDBC](https://github.com/quarkiverse/quarkus-mcp-servers/blob/main/jdbc/README.md) | [JavaDocs](https://www.javadocs.dev/mcp) · [Serena](https://oraios.github.io/serena/01-about/000_intro.html) | [Graphana](https://grafana.com/docs/grafana/latest/developer-resources/mcp/) |

## 交付物

项目会在每次迭代结束时生成一组交付物。

| 清单     | 安装                                                                                    | 快速入门                                                                           |
| --------------- | -------------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------- |
| 1. [Commands](./documentation/guides/INVENTORY-COMMANDS-JAVA.md) | `@004-commands-installation` 在项目中安装 Commands | [`Commands`](./documentation/guides/COMMANDS.md) |
| 2. [Agents](./documentation/guides/INVENTORY-AGENTS-JAVA.md) | `@005-agents-installation` 在 Cursor/Claude 中安装 Agents | [`Agents`](./documentation/guides/GETTING-STARTED-AGENTS_ZH.md)     |
| 3. [Skills](./documentation/guides/INVENTORY-SKILLS-JAVA.md) | `npx skills add jabrena/cursor-rules-java --all --agent cursor` | [`Skills`](./documentation/guides/GETTING-STARTED-SKILLS_ZH.md)     |

**⚠️ 注意：** 若你仍在使用本项目的 System prompts/rules，请阅读[相关文章](https://jabrena.github.io/cursor-rules-java/blog/2026/04/release-0.14.0.html)。当前的 `System prompts/rules` 将在下一版本（v0.16.0）中移除。

### 兼容性

本项目兼容任何支持 `Commands`、`Agents`、`Skills`、`MCP Servers` 与 `AGENTS.md` 的工具。

## 工作流

请参阅[项目工作流指南](./documentation/guides/GETTING-STARTED-WORKFLOWS_ZH.md)，了解提示工程、智能体驱动工程和流水线工作流。

## 局限性

### 缺乏确定性

从一开始就要意识到，由于模型行为的原因，与这些 `Skills` 和 agents 交互的结果并非确定性的；但你可以通过明确目标和验证检查点来降低影响。

### 并非所有模型表现一致

部分交互式 skills 需要 `Premium` 模型才能进行交互式使用；否则会按固定步骤顺序执行。

### 与模型交互的限制

模型可以生成代码，但无法针对你的本地数据执行代码。为弥补这一差距，部分 Skills 包含可在本地运行的脚本。

### 软件工程师必须参与其中

本项目用于支持软件工程工作，但不能替代工程判断。在使用 AI 生成的决策、代码和结果之前，必须由软件工程师进行审查、指导和验证。

### 访问企业数据

当问题涉及企业数据库或其他组织敏感数据时，请谨慎使用。在向 AI 辅助工作流授予访问权限之前，应评估授权、隐私、数据泄露、数据保留和意外修改等风险，并实施最小权限访问、人工审查、验证和监控。请参阅 [OWASP GenAI Data Security Risks & Mitigations 2026](https://genai.owasp.org/resource/owasp-genai-data-security-risks-mitigations-2026/)。

## 贡献

请参阅 [CONTRIBUTING.md](./CONTRIBUTING.md)，了解约定、生成器工作流、测试以及如何提交 pull request。

## Architecture Decision Records (ADR)

- 查看 [ADR 索引](./documentation/adr/README.md) 获取完整列表。

## 更新日志

- 查看 [CHANGELOG](./CHANGELOG.md) 了解详情。

## 自 Java 8 起的 Java JEP

Java 使用 JEP（JDK Enhancement Proposals）描述新的语言与平台特性。本仓库跟踪哪些 JEP 可能改进此处的 Skills 与指导内容。

- [JEP 列表](./documentation/jeps/All-JEPS.md)

## 聚会、会议、工作坊与文章

### Codemotion / Madrid (2026/04/20 - 11:00 - 12:30)

- [Taller técnico sobre Cursor para el desarrollo con Java](https://conferences.codemotion.com/madrid/speakers/)

### W-JAX / Munich (2025/11/06 - 10:30 - 11:30)

- [https://jax.de/generative-ai-ecosystem/cursor-ai-101-java-enterprise/](https://jax.de/generative-ai-ecosystem/cursor-ai-101-java-enterprise/)

### Devoxx BE / Antwerp (2025/10/07 - 18:20 - 18:50)

- [https://m.devoxx.com/events/dvbe25/talks/4715/the-power-of-cursor-rules-in-java-enterprise-development](https://m.devoxx.com/events/dvbe25/talks/4715/the-power-of-cursor-rules-in-java-enterprise-development)

### Madrid Jug / Madrid (2025/05/06 - 19:00)

- [https://www.meetup.com/es-ES/madridjug/events/307458529/](https://www.meetup.com/es-ES/madridjug/events/307458529/)

### 博客

- [Delegating Java tasks to Supervised AI Dev Pipelines](https://www.javaadvent.com/2025/12/delegating-java-tasks-to-supervised-ai-dev-pipelines.html)
- [https://vibekode.it/blog/cursor-ai-developer-cloud-platform/](https://vibekode.it/blog/cursor-ai-developer-cloud-platform/)
- [https://www.linkedin.com/pulse/september-rest-story-jvm-weekly-vol-146-artur-skowro%C5%84ski-82lif/?trackingId=wbWPSL65TpCCbdg5ksAWjw%3D%3D](https://www.linkedin.com/pulse/september-rest-story-jvm-weekly-vol-146-artur-skowro%C5%84ski-82lif/?trackingId=wbWPSL65TpCCbdg5ksAWjw%3D%3D)
- [https://virtuslab.com/blog/ai/providing-library-documentation/](https://virtuslab.com/blog/ai/providing-library-documentation/)
- https://github.com/the911fund/skill-of-skills
- https://blog.csdn.net/weixin_42526249/article/details/161176209
- https://juejin.cn/post/7632095808490340392

## 参考资料

- [https://www.cursor.com/](https://www.cursor.com/)
- [https://cursor.com/cli](https://cursor.com/cli)
- [https://www.anthropic.com/claude-code](https://www.anthropic.com/claude-code)
- [https://github.com/features/copilot](https://github.com/features/copilot)
- [https://cursor.com/docs/cli/github-actions](https://cursor.com/docs/cli/github-actions)
- [https://code.claude.com/docs/en/github-actions](https://code.claude.com/docs/en/github-actions)
- [https://agents.md/](https://agents.md/)
- [https://agentskills.io/home](https://agentskills.io/home)
- [https://microsoft.github.io/language-server-protocol/](https://microsoft.github.io/language-server-protocol/)
- [https://openspec.dev/](https://openspec.dev/)
- [https://skills.sh/jabrena/cursor-rules-java](https://skills.sh/jabrena/cursor-rules-java)
- [https://tessl.io/registry/skills/github/jabrena/cursor-rules-java](https://tessl.io/registry/skills/github/jabrena/cursor-rules-java)
- https://claudskills.com/author/jabrena/
- https://agent-skills.cc/zh/skills/jabrena-cursor-rules-java
- https://shyft.ai/skills/cursor-rules-java
- https://lobehub.com/skills?q=cursor-rules-java
- https://www.awesomeskills.dev/es/skill/jabrena-cursor-rules-java
- https://github.com/laolaoshiren/claude-code-skills-zh
- https://github.com/LessUp/awesome-cursorrules-zh
- [https://github.com/vercel-labs/skills/issues](https://github.com/vercel-labs/skills/issues)
- [https://openjdk.org/jeps/0](https://openjdk.org/jeps/0)
- [https://jbake.org/docs/latest/](https://jbake.org/docs/latest/)
- https://developers.redhat.com/blog/2016/12/09/spring-cloud-for-microservices-compared-to-kubernetes

## 其他项目

- [https://github.com/jabrena/pml](https://github.com/jabrena/pml)
- [https://github.com/jabrena/cursor-rules-java](https://github.com/jabrena/cursor-rules-java)
- https://github.com/jabrena/ai-agent-harness-monitor-cli
- [https://github.com/jabrena/setup-cli](https://github.com/jabrena/setup-cli)

Powered by [Cursor](https://www.cursor.com/) & [Codex](https://openai.com/codex/) with ❤️ from [Madrid](https://www.google.com/maps/place/Community+of+Madrid,+Madrid/@40.4983324,-6.3162283,8z/data=!3m1!4b1!4m6!3m5!1s0xd41817a40e033b9:0x10340f3be4bc880!8m2!3d40.4167088!4d-3.5812692!16zL20vMGo0eGc?entry=ttu&g_ep=EgoyMDI1MDgxOC4wIKXMDSoASAFQAw%3D%3D)
