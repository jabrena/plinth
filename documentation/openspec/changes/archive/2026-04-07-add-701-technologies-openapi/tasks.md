# Tasks: Add 701 Technologies — OpenAPI Best Practices

- [x] Add `plinth-skills-generator/src/main/resources/skill-references/701-technologies-openapi.xml` following PML schema and repository conventions for system prompts.
- [x] Add `plinth-skills-generator/src/main/resources/skill-indexes/701-skill.xml` with `id="701-technologies-openapi"` aligned to the system prompt scope and triggers.
- [x] Register skill id `701` in `plinth-skills-generator/src/main/resources/skill-inventory.json` and `plinth-skills-generator/src/main/resources/system-prompt-inventory.json`.
- [x] Run `./mvnw clean verify -pl plinth-skills-generator` and `./mvnw clean install -pl plinth-skills-generator`; confirm generated `skills/` artifacts include the new skill where applicable.
- [x] Run `npx skill-check skills` if generated skills are updated in the working tree.
