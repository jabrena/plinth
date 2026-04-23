**User Story Core Details**

**Question 1**: What is a concise title or unique ID for this user story?

---

**Question 2**: Who is the primary user (persona) for this feature?

Options/examples:
- registered user
- administrator
- guest visitor
- Other (specify)

---

**Question 3**: What specific action does this user want to perform, or what goal do they want to accomplish with this feature?

---

**Question 4**: What is the main benefit or value the user will gain from this feature? Why is this important to them?

---

**Gherkin Feature File Details**

**Question 5**: What is a descriptive name for the overall feature these scenarios will cover?

This will be the `Feature:` line in the Gherkin file (e.g., "User Authentication Management").

---

**Question 6**: Are there any common setup steps (Given steps) that apply to ALL or most of the scenarios for this feature?

If yes, please list them. If no, proceed to the next question.

---

**Acceptance Criteria / Gherkin Scenarios**

**Instruction**: Now let's detail the acceptance criteria with concrete examples. Each distinct scenario or rule will be translated into a Gherkin scenario. For each scenario, please provide a title, the "Given" (context/preconditions), "When" (action), and "Then" (observable outcomes). Include specific data examples where applicable (e.g., input values, expected messages, JSON snippets).

**Question 7 (Scenario 1 - Main Success Path)**:
- Scenario Title: What's a brief title for this first scenario?
- Given: What's the context or precondition(s)?
- When: What specific action is performed?
- Then: What are the observable outcome(s)?
- Data Examples: Any specific data (inputs/outputs) for this scenario?

---

**Question 8**: Do you have another scenario to define?

Options:
- Yes - I'll ask the questions from Question 7 for each new scenario (alternative path, boundary condition, error case, or another rule). Continue until you indicate "No."
- No - Proceed to file naming.

---

**Note**: If the user answers "Yes" to Question 8, repeat the scenario questions (title, Given, When, Then, data examples) for each additional scenario. Continue until the user indicates "No more scenarios."

---

**File Naming and Linking**

**Question 9**: What should be the filename for the Markdown user story?

Example: `US-001_Login_Functionality.md`

---

**Question 10**: What should be the filename for the Gherkin feature file?

Example: `US-001_login_functionality.feature`

---

**Question 11**: What is the relative path from the user story Markdown file to the Gherkin feature file?

This ensures they can be linked correctly.

Examples:
- `../features/US-001_login_functionality.feature`
- `features/US-001_login_functionality.feature`

---

**Optional User Story Notes**

**Question 12**: Are there any other relevant details for the user story Markdown file?

Examples: links to mockups, specific technical constraints, or non-functional requirements.

---