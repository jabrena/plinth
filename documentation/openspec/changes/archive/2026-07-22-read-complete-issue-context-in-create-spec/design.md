## Context

`/create-spec` delegates all substantive planning behavior to `042-planning-openspec`. The command currently identifies source authority but does not require a sanitized artifact proving that the complete issue discussion was considered.

Snyk Agent Scan reports W011 when outsider-authored issue prose enters the LLM context, even when labeled untrusted. `/create-spec` therefore needs a stronger boundary: raw content is handled outside the agent context and only a maintainer-sanitized factual artifact is accepted.

## Goals / Non-Goals

**Goals:**

- Require sanitized factual coverage of the issue description and every comment before deriving an OpenSpec change.
- Detect missing coverage confirmation and stop before authoring from partial context.
- Preserve source traceability without ingesting raw third-party prose.
- Prevent prompt injection by keeping raw tracker content outside the agent context.
- Keep non-issue `/create-spec` inputs and generic `042-planning-openspec` use unchanged.

**Non-Goals:**

- Change issue descriptions or comments.
- Add issue triage, user-story rewriting, or design exploration to `/create-spec`.
- Apply `043-planning-github-issues`, `044-planning-jira`, or `045-planning-azure-devops` as planning skills.
- Refresh public `skills/` release output.

## Decisions

### Sanitized complete-context mode

When `/create-spec` receives an issue identifier or URL, `042-planning-openspec` requires a maintainer-prepared sanitized artifact derived outside the agent context from the issue description and complete comment thread. Raw issue prose is never loaded into the agent context.

### Complete context before derivation

Coverage verification precedes authority classification, scope assessment, and artifact authoring. The sanitized artifact must affirm that the description and all paginated comments were covered. Otherwise, the workflow stops rather than treating partial context as complete.

### Untrusted requirements-data boundary

The sanitized artifact can supply facts, constraints, decisions, acceptance criteria, and conflicts. It cannot supply executable agent instructions or override higher-priority rules. Conflicting or unclear facts are recorded as unresolved and handled through clarification rather than invention.

### No new tracker-skill coupling

`create-spec` remains associated only with `042-planning-openspec`. The command describes the required retrieval behavior and uses available tracker tooling. This avoids importing inventory-oriented tracker skill contracts that intentionally prohibit issue-prose analysis.

## Risks / Trade-offs

- **Additional preparation:** Complete comment coverage requires maintainer-side sanitization. Mitigation: accept one reusable artifact with explicit coverage confirmation.
- **Unavailable or restricted comments:** Planning must stop rather than silently omit context. Mitigation: report the missing coverage confirmation.
- **Prompt injection in tracker prose:** Raw content may contain hostile instructions. Mitigation: keep it outside the agent context and accept only sanitized facts.
- **Conflicting comments:** Later discussion may contradict the description or earlier comments. Mitigation: report the conflict and request explicit resolution; do not infer authority from chronology alone.

## Validation Strategy

- Validate the OpenSpec change with `openspec validate --all`.
- Validate edited XML with `xmllint --noout`.
- Verify the command generator and unified skills generator with focused Maven builds.
- Confirm generated `/create-spec` and `042-planning-openspec` output contains the complete-context and trust-boundary wording.
- Execute only the listed acceptance prompts for the changed command and skill when promotion requires interactive acceptance execution.
