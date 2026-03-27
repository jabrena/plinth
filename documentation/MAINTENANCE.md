# Maintenance

Some **User prompts** designed to help in the maintenance of this repository.

```bash
# Update @All-JEPS.md
Update @All-JEPS.md with JEPs about Java 26 from https://openjdk.org/jeps/0

# Prompt to update some cursor rules with ideas included in JEPS
Can you analyze the last Java version, Java 26 from @All-JEPS.md if exist some JEP that it could be possible to be added as example in one of the XML documents from generator project. Only analyze, not create any new example and show a summary from the analysis.

# Prompt to update the list
Review that the list doesn´t any broken link to @/.cursor with .md files

# Prompt to provide a release changelog
Can you update the current changelog for 0.13.0 comparing git commits in relation to 0.12.0 tag. Use  @https://keepachangelog.com/en/1.1.0/  rules

#Bump to a new snapshot
@resources/ update version to 0.12.0-SNAPSHOT and pom.xml and maven modules
```

## Release process

- [ ] Update CHANGELOG.md
- [ ] Remove SNAPSHOT from .xml, .md & pom.xml
- [ ] Last review in docs (Manual)
- [ ] Review git changes for hidden issues (Manual) https://github.com/jabrena/cursor-rules-java/compare/0.12.0...feature/release-0130
- [ ] Review Skill validation output
- [ ] Tag repository
- [ ] Create article
- [ ] Communicate in social media

---

```bash
# Prompt to provide a release changelog
Can you update the current changelog for 0.13.0 comparing git commits in relation to 0.12.0 tag. Use  @https://keepachangelog.com/en/1.1.0/  rules

# Maven command to update the maven version to next minor version
./mvnw versions:set -DnewVersion=0.13.0-SNAPSHOT
./mvnw versions:commit

# Prompt to update the project to a new version
Update xml files from @resources/ and update the version to 0.13.0 removing Snapshot. Update @pom.xml with the new version 0.13.0 Generate system prompts again with ./mvnw clean install -pl system-prompts-generator

Update md files from @resources/ and update the version to 0.13.0 removing Snapshot. Update @pom.xml with the new version 0.13.0 Generate system prompts again with ./mvnw clean install -pl skils-generator

## Note: Refactor a bit more to include all pom.xml

## Tagging process
git tag --list
git tag 0.13.0
git push --tags
```

---

## Add a new Skills

```bash
review if exist a new id in @skills-generator/src/main/resources/skill-inventory.json to review compare with the content of @skills-generator/src/main/resources/skills and if exist add a new skill summary in @skills-generator/src/main/resources/skills . to elaborate the skill review the content of the id with @system-prompts-generator/src/main/resources/system-prompts when finish, validate generation with @/Users/jabrena/.cursor/projects/Users-jabrena-IdeaProjects-java-cursor-rules/terminals/1.txt:69-70 and validate the skill with npx skill-check skills
```
