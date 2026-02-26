IMPORTANT: You MUST ask these questions in the exact order and wording shown here. The very first question to the user MUST be "Question 1: What diagrams do you want to generate?". Do not ask any other questions prior to it.

Diagrams Selection

Conditional Flow Rules:
- Based on your selection here, only the relevant diagram generation steps will be executed.
- If you choose "Skip", no diagrams will be generated.
- Each diagram type has its own conditional follow-up questions.

---

**Question 1**: What diagrams do you want to generate?

Options:
- UML sequence diagrams
- UML class diagrams
- UML state-machine diagrams
- C4 model diagrams (Context, Container & Component diagrams)
- All diagrams
- Skip

---

**Question 2**: For UML sequence diagrams, which types would you like to generate?
Ask this question only if you selected "UML sequence diagrams" or "All diagrams" in Question 1.

Options:
- Main application flows (user journeys, authentication, core features)
- API interactions (REST endpoints, request/response patterns)
- Complex business logic flows (multi-step processes, transactions)
- All sequence diagram types
- Skip

---

**Question 3**: For UML class diagrams, which scope would you like to cover?
Ask this question only if you selected "UML class diagrams" or "All diagrams" in Question 1.

Options:
- All packages (complete project structure)
- Core business logic packages only
- Specific packages (I'll specify which ones)
- Skip

---

**Question 4**: For UML class diagrams, what level of detail do you prefer?
Ask this question only if you selected "UML class diagrams" or "All diagrams" in Question 1 and did not select "Skip" in Question 3.

Options:
- High-level (classes and relationships only)
- Detailed (include key methods and attributes)
- Full detail (all public methods, attributes, and annotations)

---

**Question 5**: For C4 model diagrams, which levels would you like to generate?
Ask this question only if you selected "C4 model diagrams" or "All diagrams" in Question 1.

Options:
- Complete C4 model (Context, Container, Component, and Code levels)
- High-level diagrams only (Context and Container)
- Detailed diagrams only (Component and Code)
- Skip

---

**Question 6**: For UML state-machine diagrams, which types would you like to generate?
Ask this question only if you selected "UML state-machine diagrams" or "All diagrams" in Question 1.

Options:
- Entity lifecycles (domain object state transitions like Order, User, Document)
- Business workflows (process state machines like approval, payment, shipping)
- System behaviors (component operational states like connections, jobs, transactions)
- User interactions (UI component state transitions like forms, wizards, dialogs)
- All state machine types
- Skip

---

**Question 7**: How would you like to organize the generated diagram files?

Options:
- Single directory (all diagrams in /diagrams folder)
- Organized by type (separate folders for each diagram type)
- Organized by package/domain (group related diagrams together)
- Integrated with existing documentation structure

---

**Question 8**: What file format would you prefer for the diagrams?

Options:
- PlantUML source files (.puml) only
- PlantUML with markdown documentation
- Both PlantUML and generated images (requires PlantUML rendering)
- Integrated into existing documentation files

---

**Question 9**: Would you like to include explanatory documentation with the diagrams?

Options:
- Yes, comprehensive explanations for each diagram
- Yes, brief descriptions and usage notes
- No, just the diagrams
- Integrate explanations into existing documentation

---