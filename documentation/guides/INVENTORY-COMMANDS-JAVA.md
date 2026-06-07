# Embedded Commands Inventory

## Goal

Provide a quick checklist of the embedded commands available for installation in this repository.

## Embedded commands

| Command | SDLC phase | Primary purpose |
| --- | --- | --- |
| `/update-issue-description` | Analysis / Design | Update GitHub issues with structured user story, acceptance criteria, and resource content. |
| `/create-feature-branch` | Analysis / Design to Implementation | Create and switch to a conventionally named branch for repository-backed analysis, design, or implementation. |
| `/create-issue` | Analysis | Create or refine a structured issue in GitHub or Jira. |
| `/create-worktree` | Analysis / Design to Implementation | Create an isolated branch and linked worktree for parallel work. |
| `/explore-design` | Design | Compare technical approaches and obtain an approved design direction. |
| `/create-adr` | Design | Record an architectural decision, alternatives, rationale, and consequences. |
| `/create-diagram` | Design | Create a focused architecture or design diagram from approved artifacts. |
| `/create-plan` | Analysis / Design | Create or refine an executable technical implementation plan. |
| `/create-spec` | Analysis / Design | Create or update one or more validated OpenSpec changes. |
| `/review-alignment` | Analysis / Design | Review available artifacts for traceability, consistency, completeness, and readiness. |
| `/implement` | Implementation | Execute feature implementation with a test-driven development workflow. |
| `/verify` | Operation | Verify implementation completeness and quality against requirements. |
| `/kill-port` | Operation | Free a localhost port by stopping the process listening on it. |

## Installation target options

- `.github/commands`
- `.claude/commands`
- `.cursor/command`
- `.codex/commands`
