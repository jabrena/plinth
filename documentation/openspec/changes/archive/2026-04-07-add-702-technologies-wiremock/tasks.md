# Tasks: Add 702 Technologies — WireMock Best Practices

- [x] Add `skills-generator/src/main/resources/system-prompts/702-technologies-wiremock.xml` following PML schema and repository conventions for system prompts.
- [x] Add `skills-generator/src/main/resources/skills/702-skill.xml` with `id="702-technologies-wiremock"` aligned to the system prompt scope and triggers.
- [x] Register skill id `702` in `skills-generator/src/main/resources/skill-inventory.json` and `skills-generator/src/main/resources/system-prompt-inventory.json`.
- [x] Run `./mvnw clean verify -pl skills-generator` and `./mvnw clean install -pl skills-generator`; confirm generated `skills/` artifacts include the new skill where applicable.
- [x] Run `npx skill-check skills` if generated skills are updated in the working tree.
