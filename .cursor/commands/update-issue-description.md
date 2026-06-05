# update-issue-description

## Purpose
Update a GitHub issue description with structured content (user story, acceptance criteria, resources).

## Usage
```
/update-issue-description <issue-number> [<file-path>]
```

## Parameters
- `<issue-number>`: GitHub issue number to update (required)
- `<file-path>`: Path to file containing issue content (optional, reads from stdin if omitted)

## Frameworks
- GitHub API
- GitHub CLI (gh)

## Output
- Updated issue body with structured formatting
- Confirmation of successful update
