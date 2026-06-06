# 项目工作流

在构建本项目的过程中，我们识别出三种工作流：`Prompting Engineering Workflow`（提示工程工作流）、`Agent-driven Engineering Workflow`（智能体驱动工程工作流）和 `Pipelines Workflow`（流水线工作流）。

## Prompting Engineering Workflow

在此工作流中，软件工程师通过 `User prompts` 与模型交互。你可以逐步委托完整任务，或在特定环节寻求帮助。本项目可用于重构生成的代码，或将任务委托出去并附带 system prompt 或 Skills。

![](../images/workflow-prompts.png)

## Agent-driven Engineering Workflow

`Agents for Java Enterprise development` 旨在帮助软件工程师完成实现阶段。工程师定义清晰的 `Specs`，再将这些规格委托给 `Agents`。

![](../images/workflow-agents.png)

## Pipelines Workflow

将 AI 工具加入流水线可带来新的价值交付机会，例如自动编码、代码重构和持续性能分析。

![](../images/workflow-pipelines.png)

请参阅[流水线指南](./GETTING-STARTED-PIPELINES_ZH.md)了解更多信息。
