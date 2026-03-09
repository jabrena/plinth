---
name: 014-agile-create-user-story
description: Guides the creation of agile user stories and Gherkin feature files. Use when the user wants to create a user story, write acceptance criteria, define Gherkin scenarios, or author BDD feature files. Part of the skills-for-java project
---

# Create Agile User Stories

Guides the agent to ask targeted questions to gather details for a user story and its Gherkin acceptance criteria, then generate a Markdown user story and a separate Gherkin `.feature` file.

---

## Phase 1: Information Gathering

Acknowledge the request and inform the user that you need to ask some questions. Ask the following, waiting for input after each block or as appropriate.

### User Story Core Details

1. **Title/ID:** "What is a concise title or unique ID for this user story?"
2. **User Role (Persona):** "Who is the primary user (persona) for this feature? (e.g., 'registered user', 'administrator', 'guest visitor')"
3. **Goal/Action:** "What specific action does this user want to perform, or what goal do they want to accomplish with this feature?"
4. **Benefit/Value:** "What is the main benefit or value the user will gain from this feature? Why is this important to them?"

### Gherkin Feature File Details

5. **Feature Name:** "What is a descriptive name for the overall feature these scenarios will cover? (This will be the `Feature:` line in the Gherkin file, e.g., 'User Authentication Management')."
6. **(Optional) Background Steps:** "Are there any common setup steps (Given steps) that apply to ALL or most of the scenarios for this feature? If so, please list them."

### Acceptance Criteria / Gherkin Scenarios

Inform the user: "Now, let's detail the acceptance criteria with concrete examples. Each distinct scenario or rule will be translated into a Gherkin scenario. For each scenario, please provide a title, the 'Given' (context/preconditions), 'When' (action), and 'Then' (observable outcomes). Include specific data examples where applicable (e.g., input values, expected messages, JSON snippets)."

7. **Scenario 1 (e.g., Main Success Path):**
   - Scenario Title: "What's a brief title for this first scenario?"
   - Given: "What's the context or precondition(s)?"
   - When: "What specific action is performed?"
   - Then: "What are the observable outcome(s)?"
   - Data Examples: "Any specific data (inputs/outputs) for this scenario?"

8. **Additional Scenarios:**
   - Ask: "Do you have another scenario to define (e.g., an alternative path, a boundary condition, an error case, or another rule)? (Yes/No)"
   - If Yes, repeat the questions from step 7 for each new scenario. Continue until the user indicates 'No' more scenarios.

### File Naming and Linking

9. **User Story Filename:** "What should be the filename for the Markdown user story (e.g., `US-001_Login_Functionality.md`)?"
10. **Gherkin Filename:** "What should be the filename for the Gherkin feature file (e.g., `US-001_login_functionality.feature`)?"
11. **Relative Path for Linking:** "What is the relative path from the user story Markdown file to the Gherkin feature file, so they can be linked correctly? (e.g., `../features/US-001_login_functionality.feature` or `features/US-001_login_functionality.feature`)."

### Optional User Story Notes

12. **Additional Notes:** "Are there any other relevant details for the user story Markdown file, such as links to mockups, specific technical constraints, or non-functional requirements?"

---

## Phase 2: Artifact Content Generation

Once all information is gathered, inform the user you will now generate the content for the two files. Provide the content for each file clearly separated.

### User Story Markdown File

Format the user story using this template:

```markdown
# User Story: [Title/ID]

**As a** [User Role]
**I want to** [Goal/Action]
**So that** [Benefit/Value]

## Acceptance Criteria

See: [Relative path to Gherkin file]

## Notes

[Additional notes if provided]
```

### Gherkin Feature File

Format the Gherkin file with:

```gherkin
Feature: [Feature Name]
  [Optional background steps if provided]

  Scenario: [Scenario Title]
    Given [context/preconditions]
    When [action]
    Then [observable outcomes]
```

**Data examples:** Use docstrings for JSON/XML or tables for structured data when the user provides complex examples.

---

## Output Checklist

Before finalizing:

- [ ] User story has title, role, goal, benefit
- [ ] User story links to the Gherkin feature file
- [ ] Gherkin file has Feature line and descriptive scenarios
- [ ] Each scenario has Given, When, Then
- [ ] Complex data uses docstrings or Example tables
