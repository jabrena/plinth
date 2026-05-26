# Maintenance

Some **User prompts** designed to help in the maintenance of this repository.

## Begin a new release

### Bump a new Snapshot version

```bash
# Maven command to update the maven version to next minor version
./mvnw versions:set -DnewVersion=0.15.0
./mvnw versions:commit

#Bump to a new snapshot
@resources/ update version to 0.15.0 and pom.xml, maven modules and finally regenerate the skills
```

## Finish a release

```bash
# Update @All-JEPS.md
Update @All-JEPS.md with JEPs about Java 26 from https://openjdk.org/jeps/0

# Prompt to update some cursor rules with ideas included in JEPS
Can you analyze the last Java version, Java 26 from @All-JEPS.md if exist some JEP that it could be possible to be added as example in one of the XML documents from generator project. Only analyze, not create any new example and show a summary from the analysis.

# Prompt to update the list
Review that the list doesn´t any broken link to @/.cursor with .md files

# Prompt to provide a release changelog
Can you update the current changelog for 0.15.0 comparing git commits in relation to 0.14.0 tag. Use  @https://keepachangelog.com/en/1.1.0/  rules

#Bump to a new snapshot
@resources/ update version to 0.15.0-SNAPSHOT and pom.xml, maven modules and finally regenerate the skills

@skills-generator/src/main/resources/skill-references/assets/agents/robot-java-coder.md @skills-generator/src/main/resources/skill-references/assets/agents/robot-micronaut-coder.md @skills-generator/src/main/resources/skill-references/assets/agents/robot-quarkus-coder.md @skills-generator/src/main/resources/skill-references/assets/agents/robot-spring-boot-coder.md review @CHANGELOG.md if it is possible to add new capabilities added in this release
```

## Release process

- [ ] Update CHANGELOG.md
- [ ] Remove SNAPSHOT from .xml, .md & pom.xml
- [ ] Last review in docs (Manual)
- [ ] Review git changes for hidden issues (Manual) https://github.com/jabrena/cursor-rules-java/compare/0.12.0...feature/release-0130
- [ ] Verify if all features were tested propertly
- [ ] Review if Agents need to add more Skills
- [ ] Review Skill validation output
- [ ] Review Skill security validation
- [ ] Update Skills Registry
- [ ] Tag repository
- [ ] Create article
- [ ] Communicate in social media

---


```bash
# Review Skill registries
https://github.com/jabrena/cursor-rules-java
https://tessl.io/registry/skills/submit
npx tessl skill review ./skills/xxx
cd target && npx skills add jabrena/cursor-rules-java --all --agent cursor && cd ..
```

---

```bash
# Prompt to provide a release changelog
Can you update the current changelog for 0.14.0 comparing git commits in relation to 0.13.0 tag. Use  @https://keepachangelog.com/en/1.1.0/  rules

# Maven command to update the maven version to next minor version
./mvnw versions:set -DnewVersion=0.15.0-SNAPSHOT
./mvnw versions:commit

# Prompt to update the project to a new version
Update xml files from @resources/ and update the version to 0.15.0-SNAPSHOT removing Snapshot.
Update @pom.xml with the new version 0.15.0-SNAPSHOT Regenerate skills with ./mvnw clean install -pl skills-generator

Update md files from @resources/ and update the version to 0.15.0 removing Snapshot.
Update @pom.xml with the new version 0.15.0-SNAPSHOT Regenerate skills with ./mvnw clean install -pl skills-generator

## Note: Refactor a bit more to include all pom.xml

## Tagging process
git tag --list
git tag 0.15.0
git push --tags
```

---

## Add a new Skills

```bash
review if exist a new id in @skills-generator/src/main/resources/skills.xml to review compare with the content of @skills-generator/src/main/resources/skill-indexes and if exist add a new skill summary in @skills-generator/src/main/resources/skill-indexes. to elaborate the skill, review the `reference-list/reference` relation declared for that id in @skills-generator/src/main/resources/skills.xml. when finish, validate generation with ./mvnw clean install -pl skills-generator and validate the skill with npx skill-check skills
```
