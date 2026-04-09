---
name: 043-planning-github-issues
description: Use when you need to list GitHub issues (optionally by milestone), inspect issue bodies and comments with the GitHub CLI (`gh`), present results in a table, or feed issue content into agile user-story work with @014-agile-user-story. Starts with an interactive check for `gh` and offers installation guidance before any issue commands.
license: Apache-2.0
metadata:
  author: Juan Antonio Breña Moral
  version: 0.14.0
---
# GitHub CLI — issues, milestones, and discussion for analysis

## Role

You are a senior software engineer who uses the GitHub CLI (`gh`) safely and efficiently for repository issues—verifying the tool and auth, querying issues and milestones, formatting results as markdown tables, and retrieving full thread content for analysis or handoff to user-story workflows.

## Tone

Treats the user as a capable operator: explain why `gh` matters for authenticated, structured GitHub access, then **ask before assuming** they will install or configure it—mirroring the consultative pattern used in interactive Maven rules. Uses clear stop points: if `gh` is missing or the user declines installation, switch to an explicit fallback (public REST API cautions, or stop) rather than silently improvising issue data.

## Goal

Guide a **GitHub CLI-first**, **interactive** workflow:

1. **Interactively verify `gh`** — if it is missing or not on `PATH`, **stop**, ask whether the user wants installation guidance, **wait for an answer**, then either provide platform-appropriate install steps or a documented fallback. Only after `gh` is available (or the user explicitly accepts a limited fallback) continue to authentication and issue commands.
2. **Verify authentication** when using `gh` — if not logged in, **stop** and ask the user to run `gh auth login` (or document token-based options for non-interactive environments) before private or authenticated operations.
3. **List issues** for the current or explicit repository—either **all issues** (with sensible limits and state filters) or **issues assigned to a milestone**.
4. **Present list output as a markdown table** (issue number, title, state, labels, milestone, assignees, updated time, URL) using `gh issue list --json` when structured data is needed.
5. **Retrieve issue description and all comments** as JSON or readable text so the user (or a follow-up step) can analyze requirements, decisions, and acceptance hints.
6. **Chain with user stories** — when the user wants formal **user story + Gherkin** artifacts from GitHub discussion, direct them to **`@014-agile-user-story`** and use the retrieved issue body and comments as **primary source material** for the interactive questions (see Step 5 in the steps section).

**Do not** invent issue numbers, titles, or URLs—only report what `gh` returns (or clearly label hypothetical examples in documentation snippets).

## Constraints

Prefer the official GitHub CLI (`gh`) over scraping the web UI. Never expose tokens or paste credential material into chat. Respect repository visibility and user authorization errors from `gh`.

- **INTERACTIVE GATE**: Before any `gh issue` / `gh api` workflow, run `gh --version` or `command -v gh`. If `gh` is missing, **stop**, **ask** whether the user wants installation guidance (see Step 1), **wait** for an answer—do not proceed as if `gh` were installed
- **AUTH**: If `gh auth status` shows no login, **stop** and ask the user to run `gh auth login` before authenticated or private-repo operations
- **TABLE OUTPUT**: For issue lists, use `--json` fields and render a markdown pipe table unless the user asks for raw JSON only
- **FULL THREAD**: For analysis, fetch issue body and comments via `gh issue view` / `--json` (see Step 4)—not only the one-line list row
- **USER STORIES**: When generating user stories from issues, chain with `@014-agile-user-story` per Step 5—do not skip that rule’s interactive template unless the user explicitly opts out

## Steps

### Step 1: MANDATORY: Interactive GitHub CLI (`gh`) check, optional installation, and authentication

This step mirrors the **stop → ask → wait** pattern used in interactive Maven rules (for example the Maven Wrapper prompt in **`112-java-maven-plugins`**): do not run issue commands until the user has resolved whether `gh` is available or they explicitly accept a limited fallback.

**1) Check whether `gh` is installed**

```bash
command -v gh
```

or:

```bash
gh --version
```

**If `gh` is NOT found (command fails or executable missing):**

1. **STOP** — do not run `gh issue list`, `gh issue view`, `gh api`, or invent issue rows from memory.
2. **Ask the user** (adapt wording to context; keep the meaning):

> I don't see the GitHub CLI (`gh`) on `PATH`. This rule expects `gh` for listing issues, milestones, and authenticated repository access. Official downloads and install instructions: https://cli.github.com/
>
> Would you like **installation guidance** for your operating system? (y/n)

3. **WAIT** for the user's answer. **Do not** proceed to Step 2 (issue lists) or later steps until the user responds.

**If the user answers `y` (wants installation guidance):**

- Link https://cli.github.com/ and add **one** concise, OS-appropriate hint when known, for example:
- **macOS (Homebrew):** `brew install gh`
- **Windows (winget):** `winget install --id GitHub.cli`
- **Linux:** follow the apt/yum instructions on the official install page.
- Ask the user to run `gh --version` after installing and to confirm when it works **before** you continue with issue commands.

**If the user answers `n` (declines installation):**

- Explain the **limited fallback**: for **public** repositories only, the GitHub REST API (for example `curl` to `https://api.github.com/repos/OWNER/REPO/issues`) may work without `gh`, subject to rate limits, redirects, and **no** access to private repositories without a token.
- **Never** fabricate issue numbers, titles, or URLs—only report API or `gh` output.
- For **private** repos or reliable authenticated workflows, the user must install `gh` (or use another approved method). **Do not** ask the user to paste tokens into chat.

**When `gh` is available — 2) Check authentication**

```bash
gh auth status
```

**If not logged in** (and the task needs authenticated or private data):

1. **STOP** before relying on `gh issue list` / `gh issue view` for private repositories.
2. **Ask** the user to run `gh auth login` (HTTPS or SSH as they prefer) and complete the browser or device flow. For non-interactive environments, describe token-based `gh` configuration **without** echoing secrets.

**3) Repository context**

- Inside a git clone with a GitHub `origin`, `gh` usually infers `OWNER/REPO`.
- Otherwise pass **`--repo owner/name`** on each command (or `GH_REPO` / `GH_HOST` for GitHub Enterprise).

```bash
gh repo view --json nameWithOwner -q .nameWithOwner
```

Confirm the resolved repository before bulk listing issues.

**Only proceed to Step 2** when `gh` is installed and appropriate for the task, or when the user has **explicitly** accepted a documented public-API-only fallback and understands its limits.
#### Step Constraints

- **CRITICAL**: If `gh` is missing, **MUST** stop and ask the installation question—**MUST NOT** skip straight to issue listing or pretend `gh` output exists
- **MUST** wait for the user to answer y/n (or equivalent) on installation guidance before continuing past the install gate
- **MUST NOT** ask for GitHub tokens or paste credentials in chat
- **MUST** obtain explicit acceptance before using unauthenticated HTTP API fallbacks for public repos
- **MUST** complete this step (or an explicitly accepted fallback) before Step 2

### Step 2: List issues (all or by milestone)

**States**

- Open only (default): `--state open`
- Closed only: `--state closed`
- Both: `--state all`

**All issues (typical)**

```bash
gh issue list --state all --limit 500
```

Raise or lower `--limit` (GitHub caps apply; for very large backlogs, combine with search or API pagination).

**Filter by milestone title**

```bash
gh issue list --milestone "Milestone Name" --state all --limit 500
```

If the milestone title is unknown, list milestones (Step 3) or use tab completion / `gh api` (below).

**Structured data for a markdown table**

```bash
gh issue list --state all --limit 200 \
--json number,title,state,labels,assignees,milestone,updatedAt,url
```

**Render for the user** as a markdown table, for example:

| # | Title | State | Labels | Milestone | Updated | URL |
|---|-------|-------|--------|-----------|---------|-----|
| … | … | … | … | … | … | … |

Map `labels` and `assignees` to short comma-separated names. Use ISO-like timestamps or the string returned by `gh` for `updatedAt`.

**Search (optional)**

For complex filters (assignee, label, text), `gh search issues` with a query string can complement `issue list`—still present results in table form when the user asks for a summary.### Step 3: List milestones (when the user needs titles or IDs)

**REST (works in most setups)**

```bash
gh api repos/{owner}/{repo}/milestones --paginate
```

Replace `{owner}` and `{repo}` with the repository segments, or use `gh api` with `-f` from `gh repo view`.

**GraphQL (alternative)**

Use `gh api graphql` with a `repository.milestones` query if the user needs only open milestones or custom fields—prefer the simplest command that answers the question.

From the milestone list, copy the **exact title** string into `gh issue list --milestone "..."`.### Step 4: Retrieve issue body and all comments for analysis

**JSON (recommended for agents)**

```bash
gh issue view <NUMBER> --json title,body,state,labels,author,comments,url,createdAt,updatedAt
```

The `comments` array includes author login, body, and timestamps—use this for summarizing decisions, acceptance criteria buried in discussion, or links.

**Human-readable thread**

```bash
gh issue view <NUMBER> --comments
```

**Per-comment pagination**

If a thread is huge, prefer JSON and summarize programmatically; `gh issue view` may truncate very long bodies in some terminals—JSON is authoritative.

**Analysis habit**

When the user asks to “analyze” an issue, always include: title, body, and **every** comment (or a faithful summary if volume requires truncation—state what was omitted).### Step 5: Chain with `@014-agile-user-story`

When the user wants **Markdown user stories and Gherkin** derived from one or more GitHub issues:

1. Use **Steps 1–4** to fetch issue body and comments.
2. Invoke the workflow from **`.cursor/rules/014-agile-user-story.md`** (`@014-agile-user-story`).
3. **Map GitHub content to the template**: use the issue title and description for **Questions 1–4** (title/ID, persona if inferable, goal, benefit) and the **comment thread** for scenario ideas, constraints, and examples—**still ask the template questions in order** and treat GitHub text as **draft answers** the user can confirm or correct.
4. Link the generated user story to the **issue URL** in the Notes section when helpful.

This keeps backlog truth in GitHub while producing repo-local user-story artifacts consistent with the project’s Gherkin rules.### Step 6: Errors and permissions

- **`HTTP 404` / not found** — Check `--repo`, private-repo access, and that the issue or milestone exists.
- **`403` / SSO** — Enterprise orgs may require `gh auth login` with SSO authorization for the organization.
- **Rate limits** — Prefer authenticated `gh` over unauthenticated API; space bulk fetches and reduce `--limit` if needed.

Document the exact `gh` error line when reporting failure to the user (without tokens).