## ADDED Requirements

### Requirement: Explore problem command produces a Functional Specification issue comment

The command bundle SHALL provide an `/explore-problem <issue-url>` command that evaluates the target issue through five points of view — problem framing, root cause analysis, assumption analysis, context mapping, and quality attribute discovery — and posts the resulting Functional Specification as a new comment on the issue at `<issue-url>`, containing one section per point of view. The Functional Specification is not written to a repository file.

#### Scenario: Produce and post a Functional Specification comment from an issue URL

- **GIVEN** a user is in a Plinth repository with a reachable issue at `<issue-url>`
- **WHEN** the user invokes `/explore-problem <issue-url>`
- **THEN** the command drafts a Functional Specification
- **AND** the draft contains a Problem Framing section with Problem statement, Current state, Desired state, Stakeholders, and Success criteria
- **AND** the draft contains a Root Cause Analysis section with findings using Five Whys, Fishbone, Current Reality Tree, and constraint identification
- **AND** the draft contains an Assumption Analysis section with Assumptions, Unknowns, and Validation plan
- **AND** the draft contains a Context Mapping section with Existing systems, Integrations, Ownership, and External dependencies
- **AND** the draft contains a Quality Attribute Discovery section identifying and prioritizing the quality attributes the future solution must satisfy
- **AND**, once confirmed (see "Confirm before posting the Functional Specification comment"), the command posts the draft as a new comment on the issue at `<issue-url>`

### Requirement: Consume prior User Story context

`/explore-problem` MUST consume the User Story content already produced for the target issue (e.g. by `/update-issue` with `014-agile-user-story`, without Gherkin) when present, rather than re-deriving the same facts from scratch.

#### Scenario: Issue already has a User Story

- **GIVEN** the target issue's description already contains a User Story produced by `/update-issue`
- **WHEN** the user invokes `/explore-problem <issue-url>`
- **THEN** the command uses that User Story as input context for the five points of view
- **AND** does not ask the user to restate facts the User Story already provides clearly

### Requirement: Ask clarifying questions for vague or ambiguous content

For each of the five points of view, `/explore-problem` MUST ask the user clarifying questions when the issue's content is vague or ambiguous from that point of view's perspective, before writing that point of view's section, instead of inventing the missing detail.

#### Scenario: Vague content for one point of view

- **GIVEN** the target issue's content is vague or ambiguous for the Root Cause Analysis point of view
- **AND** the target issue's content is clear for the other four points of view
- **WHEN** the user invokes `/explore-problem <issue-url>`
- **THEN** the command asks clarifying questions specific to root cause analysis before writing the Root Cause Analysis section
- **AND** the command does not ask clarifying questions for the four points of view whose content is already clear
- **AND** the command does not invent root cause findings without the user's clarifying answers

#### Scenario: Ambiguous content resolved by the user

- **GIVEN** the command has asked a clarifying question for a specific point of view
- **WHEN** the user answers the clarifying question
- **THEN** the command uses the sanitized answer to write that point of view's section
- **AND** does not overwrite the answer with an invented alternative

### Requirement: Confirm before posting the Functional Specification comment

`/explore-problem` MUST show the complete drafted Functional Specification to the user and MUST NOT post it as a comment on the issue without explicit user confirmation, matching this repository's existing write-safety convention for commands that modify a tracker issue (e.g. `/update-issue`).

#### Scenario: User reviews the draft before it is posted

- **GIVEN** the command has drafted all five Functional Specification sections
- **WHEN** the draft is complete
- **THEN** the command presents the full draft to the user before posting anything to the issue tracker
- **AND** does not post the comment until the user explicitly confirms

### Requirement: Support GitHub, Jira, and Azure DevOps issue URLs

`/explore-problem` MUST support `<issue-url>` values from GitHub, Jira, and Azure DevOps, matching `/update-issue`'s tracker scope, by reusing the existing `043-planning-github-issues`, `044-planning-jira`, and `045-planning-azure-devops` skills to read the issue and post the Functional Specification comment on the corresponding tracker.

#### Scenario: Issue URL from any of the three supported trackers

- **GIVEN** `<issue-url>` is a valid GitHub, Jira, or Azure DevOps issue/work-item URL
- **WHEN** the user invokes `/explore-problem <issue-url>`
- **THEN** the command reads the issue from the tracker identified by the URL's own shape, without requiring a separate tracker argument
- **AND** posts the confirmed Functional Specification as a comment on that same tracker

### Requirement: Validate required argument

`/explore-problem` MUST require a single `<issue-url>` argument and MUST present usage guidance when it is missing.

#### Scenario: Missing issue URL

- **WHEN** the user invokes `/explore-problem` without arguments
- **THEN** the command fails fast
- **AND** prints usage guidance including `/explore-problem <issue-url>`

### Requirement: Fail gracefully when the issue cannot be read

`/explore-problem` MUST fail gracefully when the target issue cannot be read from its tracker.

#### Scenario: Issue URL is unreachable or unknown

- **GIVEN** `<issue-url>` does not resolve to a readable issue
- **WHEN** the user invokes `/explore-problem <issue-url>`
- **THEN** the command reports that the issue could not be read
- **AND** does not post a Functional Specification comment claiming completeness
- **AND** does not invent problem-framing, root-cause, assumption, context, or quality-attribute content in place of the unreadable issue
