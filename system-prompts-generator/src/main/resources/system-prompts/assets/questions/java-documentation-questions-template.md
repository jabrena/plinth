IMPORTANT: You MUST ask these questions in the exact order and wording shown here. The very first question to the user MUST be "Question 1: What documentation do you want to generate?". Do not ask any other questions prior to it.

Documentation Selection

Conditional Flow Rules:
- Based on your selection here, only the relevant documentation generation steps will be executed.
- If you choose "Skip", no documentation will be generated.
- Each documentation type has its own conditional follow-up questions.

---

**Question 1**: What documentation do you want to generate?

Options:
- README.md (project overview and usage instructions)
- package-info.java files (package-level documentation)
- Javadoc enhancement (improve existing Javadoc comments)
- ADR (Architecture Decision Record) - interactive generation
- All options: README.md, package-info.java & Javadoc files
- Skip

---

**Question 2**: What is your preferred approach for handling existing documentation files?

Options:
- Overwrite existing files (replace content completely)
- Add new information (merge with existing content intelligently)
- Create backup before modifying (save original as .backup)
- Skip files that already exist

---

**Question 3**: For README.md generation, what sections would you like to include?
Ask this question only if you selected "README.md" or "All options: README.md, package-info.java & Javadoc files" in Question 1.

Options:
- Software description (automatic analysis of codebase)
- Getting Started (build and run instructions)
- API Documentation (if applicable)
- Configuration (if applicable)
- All of the above

---

**Question 4**: For package-info.java generation, what level of detail do you prefer?
Ask this question only if you selected "package-info.java files" or "All options: README.md, package-info.java & Javadoc files" in Question 1.

Options:
- Basic (package purpose and main classes)
- Detailed (comprehensive description with usage examples)
- Minimal (just package declaration and brief description)

---

**Question 5**: For Javadoc enhancement, what scope would you like to cover?
Ask this question only if you selected "Javadoc enhancement" or "All options: README.md, package-info.java & Javadoc files" in Question 1.

Options:
- All public classes and methods
- Only public APIs (exported/public packages)
- Specific packages or classes (I'll specify which ones)
- Skip

---

**Question 6**: For Javadoc enhancement, what level of detail do you prefer?
Ask this question only if you selected "Javadoc enhancement" or "All options: README.md, package-info.java & Javadoc files" in Question 1 and did not select "Skip" in Question 5.

Options:
- Basic (method/class purpose and parameters)
- Detailed (comprehensive descriptions with usage examples)
- Minimal (just missing @param, @return, @throws tags)

---

**Question 7**: Should the documentation include code examples and usage patterns?

Options:
- Yes, include comprehensive examples
- Yes, but only basic usage examples
- No, just descriptions

---

**Question 8**: What documentation style do you prefer?

Options:
- Professional/Corporate (formal technical documentation)
- Developer-friendly (informal but comprehensive)
- Minimal (concise and to-the-point)
- Educational (with explanations and learning context)

---

**Question 9**: Would you like to generate Javadoc site documentation?
Ask this question only if you selected "Javadoc enhancement" or "All options: README.md, package-info.java & Javadoc files" in Question 1.

Options:
- Yes, generate Javadoc HTML (mvn javadoc:javadoc)
- Yes, generate Javadoc and include in Maven Site (mvn clean site)
- No, skip Javadoc site generation

---

**Question 10**: For ADR generation, where would you like to store the ADR files?
Ask this question only if you selected "ADR (Architecture Decision Record) - interactive generation" in Question 1.

Options:
- documentation/adr/ (recommended standard location)
- docs/adr/ (alternative standard location)
- adr/ (root level directory)
- Custom path (I'll specify the location)

---
