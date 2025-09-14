IMPORTANT: You MUST ask these questions in the exact order and wording shown here. The very first question to the user MUST be "Question 1: What do you want to generate?". Do not ask any other questions prior to it.

Scope Selection

Conditional Flow Rules:
- Based on your selection here, only the relevant path(s) will be asked.
- If you choose "Documentation only", all diagram questions will be skipped.
- If you choose "Diagrams only", all documentation questions will be skipped.
- If you choose "Both", both paths will be asked in order: Documentation first, then Diagrams.

---

**Question 1**: What do you want to generate?

Options:
- Documentation only (README.md, package-info.java, Javadoc)
- Diagrams only (UML Sequence/Class/State-machine, C4)
- Both Documentation and Diagrams
- Skip

---

Documentation Path (README.md & package-info.java & Javadoc)

Conditional Flow Rules:
- Ask Documentation Path questions only if you selected "Documentation only" or "Both Documentation and Diagrams" in Question 1.
- If a question is not applicable due to a prior "Skip" in this path, do not ask it and continue to the Diagrams Path (if selected).

---

**Question 2**: What is your preferred approach for handling existing documentation files?

Options:
- Overwrite existing files (replace content completely)
- Add new information (merge with existing content intelligently)
- Create backup before modifying (save original as .backup)
- Skip files that already exist

---

**Question 3**: Which documentation files would you like to generate or update?

Options:
- README.md (project overview and usage instructions)
- package-info.java files (package-level documentation)
- Javadoc site documentation
- All options: README.md, package-info.java & Javadoc files
- Skip

---
**Question 4**: For README.md generation, what sections would you like to include?
Ask this question only if you selected "README.md", "Both README.md and package-info.java files", or "All options: README.md, package-info.java & Javadoc files" in Question 3.

Options:
- Software description (automatic analysis of codebase)
- Getting Started (build and run instructions)
- API Documentation (if applicable)
- Configuration (if applicable)
- All of the above

---

**Question 5**: For package-info.java generation, what level of detail do you prefer?
Ask this question only if you selected "package-info.java files", "Both README.md and package-info.java files", or "All options: README.md, package-info.java & Javadoc files" in Question 3.

Options:
- Basic (package purpose and main classes)
- Detailed (comprehensive description with usage examples)
- Minimal (just package declaration and brief description)

---

**Question 6**: Should the documentation include code examples and usage patterns?
Ask this question only if you did not select "Skip" in Question 3.

Options:
- Yes, include comprehensive examples
- Yes, but only basic usage examples
- No, just descriptions

---

**Question 7**: What documentation style do you prefer?
Ask this question only if you did not select "Skip" in Question 3.

Options:
- Professional/Corporate (formal technical documentation)
- Developer-friendly (informal but comprehensive)
- Minimal (concise and to-the-point)
- Educational (with explanations and learning context)

---

**Question 8**: Would you like to enhance Java source files with Javadoc comments?

Options:
- Yes, add or improve Javadoc on public classes and methods
- Yes, but only for public APIs (exported/public packages)
- No, skip Javadoc source enhancement

---

**Question 9**: Would you like to generate Javadoc site documentation?
Ask this question only if you selected "Javadoc site documentation" in Question 3.

Options:
- Yes, generate Javadoc (mvn javadoc:javadoc)
- Yes, generate Javadoc and include in Maven Site (mvn clean site)
- No, skip Javadoc site generation

---

Diagrams Path

Conditional Flow Rules:
- Ask Diagrams Path questions only if you selected "Diagrams only" or "Both Documentation and Diagrams" in Question 1.
- If a diagram type is not selected, skip follow-up diagram generation steps for that type.

---

**Question 10**: Which diagram(s) would you like to generate?

Options:
- UML sequence diagrams
- UML class diagrams
- UML state-machine diagrams
- C4 model diagrams (Context, Container & Component diagrams)
- All diagrams
- Skip

---

**Question 11**: For UML state-machine diagrams, which types would you like to generate?
Ask this question only if you selected "UML state-machine diagrams" or "All diagrams" in Question 10.

Options:
- Entity lifecycles (domain object state transitions like Order, User, Document)
- Business workflows (process state machines like approval, payment, shipping)
- System behaviors (component operational states like connections, jobs, transactions)
- User interactions (UI component state transitions like forms, wizards, dialogs)
- All state machine types
- Skip

---
