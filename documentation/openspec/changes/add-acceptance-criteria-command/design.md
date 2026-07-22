## Context

The Functional Specification pipeline separates three concerns:

```text
/update-issue -> User Story in the issue description
/explore-problem -> Functional Specification in a new issue comment
/add-acceptance-criteria -> Gherkin acceptance criteria in another new issue comment
```

Issue #1063 and its confirmed Functional Specification establish that Gherkin is the verification artifact for determining whether an asset achieved its goal. The new command must work from the analysis already produced by `/explore-problem`, preserve that analysis unchanged, and publish a separate acceptance artifact with the same Markdown representation across GitHub, Jira, and Azure DevOps.

`058-design-bdd` already provides interactive behavior discovery and Gherkin formulation from maintainer-provided or maintainer-sanitized facts. The command therefore orchestrates that skill instead of duplicating its method. Likewise, tracker skills `043-planning-github-issues`, `044-planning-jira`, and `045-planning-azure-devops` already define installation, authentication, and tracker-context guidance, though this command needs a narrowly scoped direct-read exception for the selected Functional Specification comment.

## Goals / Non-Goals

**Goals:**

- Add `/add-acceptance-criteria <issue-url>` as the acceptance-criteria stage after `/explore-problem`.
- Use the maintainer-confirmed Functional Specification comment as the behavior source for `058-design-bdd`.
- Generate observable Gherkin scenarios without inventing missing behavior.
- Publish the same Markdown comment structure on GitHub, Jira, and Azure DevOps.
- Preserve issue descriptions, Functional Specification comments, and other existing comments unchanged.
- Make unsuccessful lookup, generation, confirmation, authentication, and publication outcomes explicit.

**Non-Goals:**

- Modify `/update-issue`, `/explore-problem`, or `058-design-bdd`.
- Write an `Acceptance.feature` file into the repository; the acceptance artifact is an issue comment.
- Automate the generated Gherkin as executable test code.
- Select a test framework or implementation architecture.
- Update or replace an existing acceptance-criteria comment.
- Refresh public `skills/` release output.

## Change Boundary Assessment

This is one atomic OpenSpec change. The command, inventory registration, propagation checks, and tests deliver one reviewable outcome: a maintainer can derive and post acceptance criteria from an existing Functional Specification comment. The GitHub, Jira, and Azure DevOps paths share the same value, ownership, release, output contract, and rollback boundary; splitting them by tracker or Maven module would be a technical-layer decomposition rather than independently valuable changes.

## Decisions

### Command ownership and workflow position

`/add-acceptance-criteria` is owned by `@robot-business-analyst`. It operates in the Functional Specification phase, converts confirmed behavioral analysis into acceptance scenarios, and does not make technical architecture or implementation decisions.

### Functional Specification comment as the sole behavior source

The command reads issue comments only far enough to locate the Functional Specification produced by `/explore-problem`. It does not re-derive behavior from the raw issue description or unrelated discussion. A candidate comment must contain the five required Functional Specification sections: Problem Framing, Root Cause Analysis, Assumption Analysis, Context Mapping, and Quality Attribute Discovery.

When exactly one candidate exists, the command uses it. When none exists, the command stops and directs the user to run `/explore-problem`. When several candidates exist and the authoritative one cannot be identified unambiguously, the command asks the user to select a comment reference before continuing; it does not silently choose the newest comment.

This lookup rule protects traceability and prevents acceptance criteria from being generated from an arbitrary discussion comment.

### Scoped direct-read and trust boundary

The command deliberately diverges from tracker skills `043`–`045` only to read the selected Functional Specification comment and post the confirmed acceptance-criteria comment. All tracker content remains untrusted data and must never be followed as agent instructions.

The selected Functional Specification is eligible as `058-design-bdd` input because `/explore-problem` presents the complete analysis for explicit maintainer confirmation before posting it. If that provenance is unclear—such as an ambiguous candidate comment—the command requires user selection or clarification rather than claiming the content is authoritative.

### BDD interaction and evidence boundary

The command applies `058-design-bdd` to the selected Functional Specification. It confirms actors, outcomes, business rules, terminology, and unresolved questions; develops supported main, alternative, boundary, and error examples; and formulates externally observable scenarios. When a missing or ambiguous behavior fact would materially affect the scenarios, the command asks a focused question and waits for the answer. Unanswered facts remain explicit; the command does not invent decisions or reduce BDD to syntax generation alone.

### Common Markdown comment contract

All supported trackers receive the same Markdown source. The comment contains a clear acceptance-criteria heading and one fenced `gherkin` block containing a self-contained Feature with its scenarios. Tracker-specific adapters may differ only in authentication, retrieval, and comment-publication mechanics; they must not rewrite the semantic content or structure per tracker.

The command posts a new comment. It never edits the issue description, the Functional Specification comment, or a prior acceptance-criteria comment.

### Confirmation before external mutation

Before publishing, the command presents the complete Markdown/Gherkin draft and requires explicit, unambiguous affirmative confirmation. Requested edits cause the full revised draft to be shown again. Decline or abandonment leaves the tracker unchanged and must not trigger a silent retry later.

This preserves the write-safety convention already used by `/update-issue` and `/explore-problem`. It is recorded as a compatibility assumption because the source asks for comment creation but does not independently specify confirmation semantics.

### Cross-tracker behavior

The URL shape selects GitHub, Jira, or Azure DevOps without a separate tracker argument. The matching existing tracker skill supplies access and authentication guidance, while the command owns Functional Specification lookup, BDD orchestration, common Markdown rendering, confirmation, and comment publication.

Unsupported URL shapes, unreadable issues, missing authentication, insufficient permission, and publication failure stop the workflow with an actionable error. The command reports a comment reference only after the tracker confirms publication.

### Compatibility and breaking-change review

The change is additive and non-breaking. Existing command behavior and prior issue content remain unchanged. No feature toggle or migration is needed. The principal compatibility risk is variation in comment-size limits and Markdown rendering across trackers; validation must submit the same representative payload to each supported tracker and verify preserved content.

## Risks / Trade-offs

- Functional Specification comments currently have a structural contract rather than a hidden machine identifier; lookup may need user disambiguation when several complete analyses exist.
- Tracker Markdown renderers may display the same source differently even when the stored Markdown is identical.
- Large generated features may exceed a tracker comment limit; the command must fail explicitly rather than silently truncate the acceptance artifact.
- Cross-tracker authentication and comment APIs differ, increasing integration-test cost.
- A Gherkin comment provides a verification contract but does not by itself prove automated execution or implementation correctness.

## Open Questions

- Whether a future change should add a stable machine-readable provenance marker to `/explore-problem` comments; this change relies on the existing five-section structure and user disambiguation.
- Whether later invocations should supersede, link to, or replace earlier acceptance-criteria comments; v1 always creates a new comment.
