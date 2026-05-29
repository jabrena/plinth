# Java 智能体快速入门

如果你想使用本仓库提供的 **Agents for Java** 定义，请阅读本文档。它覆盖 [`.cursor/agents`](../../.cursor/agents) 下用于 Java Enterprise 交付工作流的精选智能体（不包括图表专家；见下方说明）。

## 与本项目相关的概念

### 什么是 Cursor Agent？

在 Cursor 中，**agent** 是一种可复用角色，以带 YAML frontmatter 的 Markdown 文件定义（`name`、`model`、`description`，以及可选的 `readonly` 等标志）。Cursor 会从 `.cursor/agents/` 加载这些文件。每个智能体文件都会说明角色、职责、约束，以及应使用哪些 Skills 或 rules，从而让模型在对应任务中保持一致的行为。

### 这些智能体如何协作

- **需求质量：** [robot-business-analyst](../../.cursor/agents/robot-business-analyst.md) 是 **read-only**：它会审查 user stories、计划和 ADRs 的一致性、缺口与可追溯性，适合在签署前使用，或在文档互相矛盾时使用。
- **编排：** [robot-coordinator](../../.cursor/agents/robot-coordinator.md) 读取实现计划（尤其是带 **Parallel** 列的表格），识别技术栈（Spring Boot、Quarkus、Micronaut 或普通 Java），并在一次协作中将工作**委托**给且仅给一个实现智能体。它本身不实现代码。
- **实现（仅作为委托目标）：** [robot-java-coder](../../.cursor/agents/robot-java-coder.md)、[robot-spring-boot-coder](../../.cursor/agents/robot-spring-boot-coder.md)、[robot-quarkus-coder](../../.cursor/agents/robot-quarkus-coder.md) 和 [robot-micronaut-coder](../../.cursor/agents/robot-micronaut-coder.md) 是 **coordinator** 会委托的专家。对于计划驱动的实现，不应直接用 `@` 提及它们；应从 [@robot-coordinator](../../.cursor/agents/robot-coordinator.md) 开始，让它选择正确技术栈并移交工作（包括 **Parallel** 分组）。它们会在需要时引用 **Skills for Java** 目录（例如 Spring Boot 的 `@301`-`@323`）。

**说明：** 仓库可能在 `.cursor/agents` 中包含其他智能体定义（例如架构或图表专家）。本指南只覆盖下面列出的 **Java 交付与 BA** 智能体。

### 对 Skills 和 System prompts 的依赖

这些智能体假设你可以附加 **Skills**（见 [Java Skills 快速入门](GETTING-STARTED-SKILLS_CN.md)）和/或 **Cursor rules**（见 [Getting started for System prompts for Java](GETTING-STARTED-SYSTEM-PROMPTS.md)），这样聊天中的 `@...` 引用才能解析。实现智能体会明确命名具体的 skill 前缀（例如 `@322-frameworks-spring-boot-testing-integration-tests`）。

## 如何安装 Agents？

Agents 与其他 Cursor 项目配置放在一起。安装方式与安装 **Cursor rules** 相同：将整个 `.cursor` 目录复制到你的 Java 仓库根目录（或使用你已有的 `.cursor/rules` 同步流程）。

- **从 Git clone 或 ZIP 安装：** 将 [`.cursor/agents`](../../.cursor/agents) 文件夹（或整个 `.cursor` 树）复制到你的项目中。
- **JBang 设置（rules + Cursor 目录结构）：** 如果你使用 [Getting started for System prompts for Java](GETTING-STARTED-SYSTEM-PROMPTS.md) 中描述的设置，请确保从本仓库同步时也保留或复制 `.cursor/agents`。

安装完成后，你应该能在项目的 `.cursor/agents/` 下看到 `robot-coordinator.md` 等文件。

## 智能体目录（Java 交付与 BA）

对于**从计划开始的实现**，你的入口点只能是 **[@robot-coordinator](../../.cursor/agents/robot-coordinator.md)**。它会识别技术栈并委托给正确的 `robot-*-coder`；在该工作流中，不要自行 `@` 提及 coder 智能体。

| Agent | 角色 | 如何使用 |
|-------|------|----------|
| [robot-business-analyst](../../.cursor/agents/robot-business-analyst.md) | BA 审查：stories、plan、ADRs；**readonly**。 | 进行需求审查时，**直接 @ 此智能体**（它不属于 coordinator 的实现委托链路）。 |
| [robot-coordinator](../../.cursor/agents/robot-coordinator.md) | 从 `*.plan.md` 协调工作；路由到一个特定框架的实现者；按 **Parallel** 分组拆分交接。 | 当你有结构化计划（适用时包含 **Parallel** / 执行顺序）并希望按顺序委托实现时，**@ 此智能体**。 |
| [robot-java-coder](../../.cursor/agents/robot-java-coder.md) | 普通 Java / Maven 实现专家。 | **仅作为委托目标**：当技术栈是普通 Java 或框架中立时，由 coordinator 分配该智能体。 |
| [robot-spring-boot-coder](../../.cursor/agents/robot-spring-boot-coder.md) | Spring Boot 实现（REST、Data JDBC、test slices 等）。 | **仅作为委托目标**：当技术栈是 Spring Boot 时，由 coordinator 分配该智能体。 |
| [robot-quarkus-coder](../../.cursor/agents/robot-quarkus-coder.md) | Quarkus 实现（Jakarta REST、CDI、Panache/JDBC、Quarkus tests）。 | **仅作为委托目标**：当技术栈是 Quarkus 时，由 coordinator 分配该智能体。 |
| [robot-micronaut-coder](../../.cursor/agents/robot-micronaut-coder.md) | Micronaut 实现（`@Controller`、Micronaut Data、`HttpClient` tests）。 | **仅作为委托目标**：当技术栈是 Micronaut 时，由 coordinator 分配该智能体。 |

## 在 Cursor 中使用你的第一个 Agent

1. 打开已经包含 `.cursor/agents` 的 Java 仓库（最好也已安装上文提到的 Skills / rules）。
2. 对于**实现**，**只提及 @robot-coordinator** 并附上计划；在消息中说明里程碑或约束。coordinator 的职责是委托给正确的 `robot-*-coder`（执行计划时不要自己 `@` 那些 coder）。
3. 对于**需求审查**，**直接提及 @robot-business-analyst**，并附上或粘贴 stories、plans 和 ADRs。

**示例：**

- *"Using @robot-coordinator, execute the plan at `path/to/PLAN-*.plan.md` and delegate implementation per Parallel groups."*
- *"Using @robot-business-analyst, review these files for traceability: [paste or attach stories, plan, ADRs]."*
