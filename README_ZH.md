# Java 命令、智能体与技能

<a href="https://trendshift.io/repositories/15013" target="_blank"><img src="https://trendshift.io/api/badge/repositories/15013" alt="jabrena%2Fcursor-rules-java | Trendshift" style="width: 250px; height: 55px;" width="250" height="55"/></a>

[![Stargazers over time](https://starchart.cc/jabrena/cursor-rules-java.svg?variant=adaptive)](https://starchart.cc/jabrena/cursor-rules-java)

[![CI Builds](https://github.com/jabrena/cursor-rules-java/actions/workflows/maven.yaml/badge.svg)](https://github.com/jabrena/cursor-rules-java/actions/workflows/maven.yaml)

> **语言：** [English](./README.md) · [Español](./README_ES.md)
>
> **Website:** https://jabrena.github.io/cursor-rules-java/
>
> **支持项目：** [Sponsor to pay tokens](https://github.com/sponsors/jabrena)

**弃用通知：** 当前的 `System prompts/rules` 已弃用，并将在 `v0.16.0` 中移除。如果你仍在使用它们，请阅读 [0.14.0 版本文章](https://jabrena.github.io/cursor-rules-java/blog/2026/04/release-0.14.0.html)。

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
| Commands      | `/create-issue` · [`/update-issue`](./.cursor/commands/update-issue.md) · `/explore-design` · `/create-adr` · `/create-diagram` · `/create-plan` · `/create-spec` · `/review-alignment` | [`/create-feature-branch`](./.cursor/commands/create-feature-branch.md) · [`/create-worktree`](./.cursor/commands/create-worktree.md) · [`/implement-issue`](./.cursor/commands/implement-issue.md) · [`/kill-port`](./.cursor/commands/kill-port.md) | [`/profile`](./.cursor/commands/profile.md) · [`/benchmark`](./.cursor/commands/benchmark.md) |
| [Agents](./documentation/guides/GETTING-STARTED-AGENTS_ZH.md)        | `@robot-business-analyst` · `@robot-architect` · `@robot-tech-lead` | `@robot-tech-lead` · `@robot-java-coder` · `@robot-java-spring-boot-coder` · `@robot-java-quarkus-coder` · `@robot-java-micronaut-coder` | `@robot-java-performance` |
| [Skills](./documentation/guides/GETTING-STARTED-SKILLS_ZH.md)        | [014-agile-user-story](https://www.skills.sh/jabrena/cursor-rules-java/014-agile-user-story) · [030-architecture-adr-general](https://www.skills.sh/jabrena/cursor-rules-java/030-architecture-adr-general) · [033-architecture-diagrams](https://www.skills.sh/jabrena/cursor-rules-java/033-architecture-diagrams) · [041-planning-plan-mode](https://www.skills.sh/jabrena/cursor-rules-java/041-planning-plan-mode) · [200-agents-md](https://www.skills.sh/jabrena/cursor-rules-java/200-agents-md) ... | [110-java-maven-best-practices](https://www.skills.sh/jabrena/cursor-rules-java/110-java-maven-best-practices) · [121-java-object-oriented-design](https://www.skills.sh/jabrena/cursor-rules-java/121-java-object-oriented-design) · [124-java-secure-coding](https://www.skills.sh/jabrena/cursor-rules-java/124-java-secure-coding) · [111-java-maven-dependencies](https://www.skills.sh/jabrena/cursor-rules-java/111-java-maven-dependencies) · [143-java-functional-exception-handling](https://www.skills.sh/jabrena/cursor-rules-java/143-java-functional-exception-handling) ... | [151-java-performance-jmeter](https://www.skills.sh/jabrena/cursor-rules-java/151-java-performance-jmeter) · [162-java-profiling-analyze](https://www.skills.sh/jabrena/cursor-rules-java/162-java-profiling-analyze) · [161-java-profiling-detect](https://www.skills.sh/jabrena/cursor-rules-java/161-java-profiling-detect) · [163-java-profiling-refactor](https://www.skills.sh/jabrena/cursor-rules-java/163-java-profiling-refactor) · [164-java-profiling-verify](https://www.skills.sh/jabrena/cursor-rules-java/164-java-profiling-verify) ... |
| [MCP Servers](./documentation/guides/THIRD-PARTIES.md)   | [Jbang-Quarkus-JDBC](https://github.com/quarkiverse/quarkus-mcp-servers/blob/main/jdbc/README.md) · [MongoDB](https://github.com/mongodb-js/mongodb-mcp-server) · [Serena-LSP](https://oraios.github.io/serena/01-about/000_intro.html) | [Jbang-Quarkus-JDBC](https://github.com/quarkiverse/quarkus-mcp-servers/blob/main/jdbc/README.md) · [MongoDB](https://github.com/mongodb-js/mongodb-mcp-server) · [JavaDocs](https://www.javadocs.dev/mcp) · [Serena-LSP](https://oraios.github.io/serena/01-about/000_intro.html) | [Jbang-Quarkus-JDBC](https://github.com/quarkiverse/quarkus-mcp-servers/blob/main/jdbc/README.md) · [MongoDB](https://github.com/mongodb-js/mongodb-mcp-server) · [Serena-LSP](https://oraios.github.io/serena/01-about/000_intro.html) · [Graphana](https://grafana.com/docs/grafana/latest/developer-resources/mcp/) |

## 交付物

项目会在每次迭代结束时生成一组交付物。

| 清单     | 安装                                                                                    | 快速入门                                                                           |
| --------------- | -------------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------- |
| 1. [Commands](./documentation/guides/INVENTORY-COMMANDS-JAVA.md) | `@004-commands-installation` 在项目中安装 Commands | [`Commands`](./documentation/guides/COMMANDS.md) |
| 2. [Agents](./documentation/guides/INVENTORY-AGENTS-JAVA.md) | `@005-agents-installation` 在 Cursor/Claude 中安装 Agents | [`Agents`](./documentation/guides/GETTING-STARTED-AGENTS_ZH.md)     |
| 3. [Skills](./documentation/guides/INVENTORY-SKILLS-JAVA.md) | `npx skills add jabrena/cursor-rules-java --all --agent cursor` | [`Skills`](./documentation/guides/GETTING-STARTED-SKILLS_ZH.md)     |

### 兼容性

本项目兼容任何支持 `Commands`、`Agents`、`Skills`、`MCP Servers` 与 `AGENTS.md` 的工具。

## 5 分钟快速入门

按照快速指南 [5 分钟快速入门](./documentation/guides/GETTING-STARTED-IN-5-MINUTES_ZH.md) 学习如何使用本项目。

## Skill 验证

每次 push 都会在 [CI Builds](./.github/workflows/maven.yaml) 中运行以下验证检查，以保持文档和生成的 skills 正确、一致且安全：

| 名称 | 用途 |
| --- | --- |
| 1. [MarkdownValidator](./.github/scripts/MarkdownValidator.java) | 保护文档层，在运行 skill 专项检查之前发现 Markdown 解析漂移和远程链接故障。 |
| 2. [skill-check](https://github.com/thedaviddias/skill-check) | 确认每个生成的 skill 符合预期的打包约定，补充更关注行为或安全风险的扫描器。 |
| 3. [cisco-ai-skill-scanner](https://github.com/cisco-ai-defense/skill-scanner) by Cisco | 提供面向行为的安全覆盖，发现结构校验无法识别的高风险 skill 流程。 |
| 4. [SkillSpector](https://github.com/NVIDIA/SkillSpector) by NVIDIA | 提供独立的静态质量和安全审查，便于与其他扫描器的发现进行对照。 |
| 5. [Snyk Agent Scan](https://github.com/snyk/agent-scan) by SNYK | 聚焦 agent skill 的供应链和 prompt 风险信号，与 Cisco 和 SkillSpector 一起提供另一种安全视角。 |

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

当问题涉及企业数据库或其他组织敏感数据时，请谨慎使用。在向 AI 辅助工作流授予访问权限之前，应评估授权、隐私、数据泄露、数据保留和意外修改等风险，并实施最小权限访问、人工审查、验证和监控。请参阅 [OWASP GenAI Data Security Risks & Mitigations 2026](https://genai.owasp.org/resource/owasp-genai-data-security-risks-mitigations-2026/) 和 [The EU Artificial Intelligence Act](https://artificialintelligenceact.eu/)。

## 贡献

请参阅 [CONTRIBUTING.md](./CONTRIBUTING.md)，了解约定、生成器工作流、测试以及如何提交 pull request。

## Architecture Decision Records (ADR)

- 查看 [ADR 索引](./documentation/adr/README.md) 获取完整列表。

## 更新日志

- 查看 [CHANGELOG](./CHANGELOG.md) 了解详情。

## 自 Java 8 起的 Java JEP

Java 使用 JEP（JDK Enhancement Proposals）描述新的语言与平台特性。本仓库跟踪哪些 JEP 可能改进此处的 Skills 与指导内容。

- [JEP 列表](./documentation/jeps/All-JEPS.md)

## 更多资源

演讲、文章、参考链接、skill 门户和相关项目请参阅[项目参考资料](./documentation/guides/PROJECT-REFERENCES_ZH.md)。

由人类开发，并得到 [Cursor](https://www.cursor.com/) 与 [Codex](https://openai.com/codex/) 的支持，带着来自 [Madrid](https://www.google.com/maps/place/Community+of+Madrid,+Madrid/@40.4983324,-6.3162283,8z/data=!3m1!4b1!4m6!3m5!1s0xd41817a40e033b9:0x10340f3be4bc880!8m2!3d40.4167088!4d-3.5812692!16zL20vMGo0eGc?entry=ttu&g_ep=EgoyMDI1MDgxOC4wIKXMDSoASAFQAw%3D%3D) 的 ❤️
