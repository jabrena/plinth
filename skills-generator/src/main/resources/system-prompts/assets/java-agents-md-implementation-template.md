# AGENTS.md Implementation Guide

## Output Structure

Generate an `AGENTS.md` file in the project root with the following structure.
Use "# Contributor Quickstart Guide" or "# Agent Quickstart Guide" as the main heading (or a custom title if the user provides one).
Map each section from the user's answers in Step 1:

### Your role (from Question 1)

```markdown

## Your role

You are [role/expertise from Q1].

- [Bullet points from user's selection or custom answer]
```

### Tech stack (from Question 2)

```markdown

## Tech stack
- **Language:** [from Q2, e.g., Java 17, Java 21]
- **Build:** [from Q2, e.g., Maven, Gradle]
- **Frameworks:** [from Q2 if provided]
- [Additional stack items from Q2]
```

### File structure (from Question 3)

```markdown

## File structure
- `[path]` – [description, READ/WRITE indication]
- [Additional entries from Q3]
```

Use **READ only** or **WRITE here** to indicate editability of each path.

### Commands (from Question 4)

Document each command with a comment and the command. Use a bash code block:

- Section title: `## Commands`
- Fenced code block with `bash` syntax
- Each command preceded by `# description` comment

### Git workflow (from Question 5)

```markdown

## Git workflow
- [Requirements from Q5, e.g., Chris Beams style, Conventional Commits]
- [PR checklist items if applicable]
- [Comment style requirements]
```

### Boundaries (from Question 6)

```markdown

## Boundaries
- ✅ **Always do:** [from Q6]
- ⚠️ **Ask first:** [from Q6]
- 🚫 **Never do:** [from Q6]
```

## File Handling Strategy

**Before writing AGENTS.md:**

1. **Check if AGENTS.md exists** in the project root.
2. **If it exists:** Ask user: "AGENTS.md already exists. Overwrite, merge with existing content, or create backup first? (overwrite/merge/backup)"
3. **If overwrite:** Replace file completely.
4. **If merge:** Parse existing sections, add missing sections, preserve user customizations.
5. **If backup:** Save original as `AGENTS.md.backup` before any changes.

## Implementation Checklist

1. **Collect answers** from all 6 questions in Step 1.
2. **Confirm understanding** with user before generating.
3. **Generate AGENTS.md** at project root using the structure above.
4. **Validate** that all sections are properly formatted and complete.
