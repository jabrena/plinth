# Getting Started in 5 minutes

> **Languages:** [Español](./GETTING-STARTED-IN-5-MINUTES_ES.md) · [中文](./GETTING-STARTED-IN-5-MINUTES_ZH.md)

This path gives first-time users a small, practical start before reading the full project documentation.

## 1. Install Node.js if needed

The Skills.sh CLI runs through `npx`, so you need Node.js available:

```bash
node --version
npx --version
```

If either command is missing, install Node.js with your operating system package manager.

## 2. Choose your agent and download the Skills

Install every skill from the [Skills.sh registry entry](https://skills.sh/jabrena/cursor-rules-java) for this project into the agent you use most often:

```bash
# Cursor
npx skills add jabrena/cursor-rules-java --skill '*' --agent cursor -y

# Claude Code
npx skills add jabrena/cursor-rules-java --skill '*' --agent claude-code -y

# Codex
npx skills add jabrena/cursor-rules-java --skill '*' --agent codex -y

# GitHub Copilot
npx skills add jabrena/cursor-rules-java --skill '*' --agent github-copilot -y
```

To install for every supported agent, use the CLI's all-agent mode:

```bash
npx skills add jabrena/cursor-rules-java --skill '*' --agent '*' -y
```

To inspect the available skills first:

```bash
npx skills add jabrena/cursor-rules-java --list
```

## 3. Install the project commands

After installing the skills, tell your agent which AI agent harness tool you use: `cursor`, `claude-code`, `codex`, or `github-copilot`.

```text
install @004-commands-installation cursor
install @004-commands-installation claude-code
install @004-commands-installation codex
install @004-commands-installation github-copilot
```

The installer copies the embedded project commands into the command directory for your tool. It supports command destinations for Cursor, Claude, Codex, and GitHub Copilot, and it asks before writing files.

Typical command destinations are:

- `.cursor/command`
- `.claude/commands`
- `.codex/commands`
- `.github/commands`

## 4. Install the project agents when your tool supports them

Commands and skills are enough to start. If you also want the predefined robot agents, tell your agent which AI agent harness tool you use: `cursor` or `claude-code`.

```text
install @005-agents-installation cursor
install @005-agents-installation claude-code
```

The installer supports `.cursor/agents` and `.claude/agents`.

## 5. Use the workflow

This project organizes AI-assisted Java work around four building blocks:

- `Commands` are the entry points, such as `/update-issue`, `/create-spec`, and `/implement-issue`.
- `Agents` define responsibilities, such as business analysis, architecture, technical leadership, Java implementation, or performance work.
- `Skills` provide focused Java, framework, testing, documentation, security, and observability practices.
- `MCP Servers` connect agents to external tools and project context when available.

A common path is:

```text
/update-issue -> /create-spec -> /implement-issue -> /profile or /benchmark
```

For documentation-only or planning work, you may stop after the issue, plan, specification, ADR, or diagram is complete.

## Go deeper

- [Commands inventory](./INVENTORY-COMMANDS-JAVA.md)
- [Agents getting started](./GETTING-STARTED-AGENTS.md)
- [Skills getting started](./GETTING-STARTED-SKILLS.md)
- [Project workflows](./GETTING-STARTED-WORKFLOWS.md)
- [Pipelines getting started](./GETTING-STARTED-PIPELINES.md)
