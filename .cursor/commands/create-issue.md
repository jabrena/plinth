# create-issue

## Purpose
Create or refine a structured project issue before analysis and design work begins.

## Usage
```text
/create-issue [<source>] [<tracker>]
```

## Accepted Inputs
- A problem statement, feature request, or existing draft issue
- Optional supporting discussion, user story, or acceptance criteria
- Optional tracker selection: GitHub or Jira

## Owning Agent
`@robot-business-analyst`

## Associated Skills
- `043-planning-github-issues`
- `044-planning-jira`
- `014-agile-user-story` when user-story refinement is required

## Workflow
1. Confirm the problem, project user, desired outcome, and business value.
2. Convert the available information into a concise, testable issue.
3. Present the proposed title and body for approval.
4. Create or update the issue in the selected tracker.
5. Report the issue identifier and URL.

## Output
- Structured issue title and description
- Acceptance criteria or user-story scenario when applicable
- Created or updated GitHub/Jira issue reference

## Safeguards
- Do not invent requirements or acceptance criteria.
- Do not expose tracker credentials or tokens.
- Do not overwrite an existing issue body without showing the proposed change.
