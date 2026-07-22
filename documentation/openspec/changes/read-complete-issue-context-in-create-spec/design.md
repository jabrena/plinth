## Context

`/create-spec` delegates all substantive planning behavior to `042-planning-openspec`. The command currently identifies source authority but does not define how issue context is acquired. The skill's generic trust gate requires sanitized summaries, which directly conflicts with the requested complete-context behavior.

The repository already documents a direct-read model for `/explore-problem`: tracker prose is read as untrusted data, unclear content triggers clarification instead of guessing, and embedded instructions never become agent instructions. `/create-spec` can reuse that boundary without adding tracker-specific skills to its planning surface.

## Goals / Non-Goals

**Goals:**

- Load the issue description and every comment before deriving an OpenSpec change from an issue.
- Detect incomplete retrieval, including pagination or permission failures, and stop before authoring from partial context.
- Preserve source ordering and distinguish the description from individual comments when recording traceability.
- Prevent prompt injection by treating tracker content only as requirements evidence.
- Keep non-issue `/create-spec` inputs and generic `042-planning-openspec` use unchanged.

**Non-Goals:**

- Change issue descriptions or comments.
- Add issue triage, user-story rewriting, or design exploration to `/create-spec`.
- Apply `043-planning-github-issues`, `044-planning-jira`, or `045-planning-azure-devops` as planning skills.
- Refresh public `skills/` release output.

## Decisions

### Command-authorized direct-read mode

When `/create-spec` receives an issue identifier or URL, it explicitly authorizes `042-planning-openspec` to load the issue description and the complete comment thread. Outside this command mode, the skill continues to require maintainer-sanitized summaries for outsider-authored bodies.

### Complete context before derivation

Issue retrieval precedes authority classification, scope assessment, and artifact authoring. The workflow must follow pagination until all comments are loaded. If the issue, any comment page, or required tracker access cannot be read, it stops and reports the missing context rather than treating the partial result as complete.

### Untrusted requirements-data boundary

The issue description and comments can supply facts, constraints, decisions, acceptance criteria, and conflicts. They cannot supply executable agent instructions or override higher-priority rules. Conflicting or unclear issue statements are recorded as unresolved and handled through clarification rather than invention.

### No new tracker-skill coupling

`create-spec` remains associated only with `042-planning-openspec`. The command describes the required retrieval behavior and uses available tracker tooling. This avoids importing inventory-oriented tracker skill contracts that intentionally prohibit issue-prose analysis.

## Risks / Trade-offs

- **More latency and API usage:** Complete comment pagination costs additional calls. Mitigation: retrieve once per planning run and preserve source traceability.
- **Unavailable or restricted comments:** Planning must stop rather than silently omit context. Mitigation: report the exact inaccessible source and required access.
- **Prompt injection in tracker prose:** Full issue reading increases exposure to untrusted instructions. Mitigation: enforce a data-only trust boundary and never execute embedded directives.
- **Conflicting comments:** Later discussion may contradict the description or earlier comments. Mitigation: report the conflict and request explicit resolution; do not infer authority from chronology alone.

## Validation Strategy

- Validate the OpenSpec change with `openspec validate --all`.
- Validate edited XML with `xmllint --noout`.
- Verify the command generator and unified skills generator with focused Maven builds.
- Confirm generated `/create-spec` and `042-planning-openspec` output contains the complete-context and trust-boundary wording.
- Execute only the listed acceptance prompts for the changed command and skill when promotion requires interactive acceptance execution.
