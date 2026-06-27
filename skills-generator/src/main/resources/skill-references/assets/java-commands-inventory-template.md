# Embedded Commands Inventory

## Goal

Provide a quick checklist of the embedded commands available for installation in this repository.

## Embedded commands

| Command | SDLC phase | Primary purpose |
| --- | --- | --- |
| `/update-issue` | Analysis / Design | Update existing GitHub or Jira issues with structured user story, acceptance criteria, and resource content. |
| `/create-feature-branch` | Analysis / Design to Implementation | Create and switch to a conventionally named branch for repository-backed analysis, design, or implementation. |
| `/create-worktree` | Analysis / Design to Implementation | Create an isolated branch and linked worktree for parallel work. |
| `/explore-design` | Design | Compare technical approaches and obtain an approved design direction. |
| `/create-adr` | Design | Record an architectural decision, alternatives, rationale, and consequences. |
| `/create-diagram` | Design | Create a focused architecture or design diagram from approved artifacts. |
| `/create-spec` | Analysis / Design | Create or update one or more validated OpenSpec changes. |
| `/review-alignment` | Analysis / Design | Review available artifacts for traceability, consistency, completeness, and readiness. |
| `/review-breaking-changes` | Analysis / Design | Review a plan or OpenSpec artifact for compatibility and migration risks before implementation or release. |
| `/implement-issue` | Implementation | Deliver an issue from an approved plan or validated OpenSpec task list through framework-aware delegation. |
| `/profile` | Operation | Coordinate Java profiling from baseline detection through verified optimization. |
| `/benchmark` | Operation | Select and coordinate JMeter, Gatling, or JMH performance workflows. |

## Installation target options

- `.github/commands`
- `.claude/commands`
- `.cursor/command`
- `.codex/commands`
