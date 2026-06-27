# Tasks

## 1. Implementation Checklist

- [ ] 1.1 Review issue #881 and the current generated `.agents/skills/114-java-maven-search/SKILL.md`.
- [ ] 1.2 Review `skills-generator/src/main/resources/skill-indexes/114-skill.xml` and `skills-generator/src/main/resources/skill-references/114-java-maven-search.xml`.
- [ ] 1.3 Confirm whether the generator supports emitting two focused reference files for one skill without broader generator changes.
- [ ] 1.4 Preserve existing covered workflows while separating project-local update guidance from Maven Central search guidance.
- [ ] 1.5 Update the top-level skill source so `114-java-maven-search` is clearly described as a router for the two workflows.
- [ ] 1.6 Make project-local update guidance the default for outdated dependency, plugin update, property version bump, and own-`pom.xml` update requests.
- [ ] 1.7 Make Maven Central search guidance explicit for Central search, coordinate verification, version browsing, artifact URL construction, and artifact download requests.
- [ ] 1.8 Remove or reword guidance that suggests ingesting or summarizing raw remote POM, metadata XML, artifact descriptions, or repository HTML.
- [ ] 1.9 Validate changed XML files with `xmllint --noout`.
- [ ] 1.10 Run `./mvnw clean install -pl skills-generator`.
- [ ] 1.11 Inspect generated local `.agents/skills/114-java-maven-search/SKILL.md`.
- [ ] 1.12 Inspect generated local reference output for routing clarity and raw remote metadata safeguards.
- [ ] 1.13 Check `skills-generator/src/test/resources/gherkin/skills/acceptance-tests-prompts-skills.md` for a `114-java-maven-search` acceptance prompt.
- [ ] 1.14 Execute only the listed `114-java-maven-search` acceptance prompt when the prompt exists and generated local output changes; otherwise record the skipped prompt reason.
- [ ] 1.15 Run focused `skills-generator` verification for the changed XML and generated output.
- [ ] 1.16 Run `openspec validate --all`.
