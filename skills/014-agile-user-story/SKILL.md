---
name: 014-agile-user-story
description: Guides the creation of agile user stories and Gherkin feature files. Use when the user wants to create a user story, write acceptance criteria, define Gherkin scenarios, or author BDD feature files. Part of the skills-for-java project
license: Apache-2.0
metadata:
  author: Juan Antonio Breña Moral
  version: 0.13.0-SNAPSHOT
---
# Create Agile User Stories and Gherkin Feature Files

Guide the agent to ask targeted questions to gather details for a user story and its Gherkin acceptance criteria, then generate a Markdown user story and a separate Gherkin `.feature` file. **This is an interactive SKILL**.

**What is covered in this Skill?**

- User story core details: title, persona, goal, benefit
- Gherkin feature file: Feature name, background steps, scenarios
- Acceptance criteria: Given / When / Then with data examples
- File naming and linking between user story and feature file

## Workflow

1. **Information Gathering** — Ask 12 questions one-by-one in strict order: user story core details (title, persona, goal, benefit), Gherkin feature details (feature name, background steps), acceptance criteria scenarios (Given/When/Then with data examples for each scenario), and file naming/linking details. Repeat scenario questions for additional scenarios.
2. **Generate artifacts** — Produce a Markdown user story file and a separate Gherkin `.feature` file based on collected responses. Tag exactly one scenario with `@acceptance-test` (happy path) and all others with `@integration-test`.
3. **Output checklist** — Verify: user story has title/role/goal/benefit, links to Gherkin file, feature file has proper scenarios with Given/When/Then, tags are correct, and complex data uses docstrings or Example tables.

## Quick Reference

User story and Gherkin template format:

```markdown
# User Story: US-001 Login Functionality

**As a** registered user
**I want to** log in with my credentials
**So that** I can access my personalized dashboard

## Acceptance Criteria
See: features/US-001_login_functionality.feature
```

```gherkin
Feature: User Authentication

@acceptance-test
Scenario: Successful login with valid credentials
  Given a registered user with username "alice"
  When the user submits valid credentials
  Then the user is redirected to the dashboard
```

## Constraints

Before generating artifacts, gather all required information through structured questions. Use exact wording from the template and wait for user responses.

- **MANDATORY**: Ask questions from the template one-by-one in strict order before generating any artifacts
- **MUST**: Read the reference template fresh and use exact wording—do not use cached questions
- **MUST**: Wait for user response after each question or block before proceeding
- **MUST**: Repeat scenario questions for each additional scenario when user indicates more scenarios

## When to use this skill

- Create a user story
- Write a user story
- I need to write a user story

## Reference

For detailed guidance, examples, and constraints, see [references/014-agile-user-story.md](references/014-agile-user-story.md).
