# Maintenance

Some **User prompts** designed to help in the maintenance of this repository.

```bash
# Update @All-JEPS.md
Update @All-JEPS.md with JEPs about Java 25 from https://openjdk.org/jeps/0

# Prompt to update some cursor rules with ideas included in JEPS
Can you analyze the last Java version, Java 25 from @All-JEPS.md if exist some JEP that it could be possible to be added as example in one of the XML documents from generator project. Only analyze, not create any new example and show a summary from the analysis.

# Prompt to update the list
Review that the list doesn´t any broken link to @/.cursor with .md files

# Prompt to provide a release changelog
Can you update the current changelog for 0.10.0 comparing git commits in relation to 0.9.0 tag. Use  @https://keepachangelog.com/en/1.1.0/  rules

#Bump to a new snapshot
@resources/ update version to 0.12.0-SNAPSHOT and pom.xml and maven modules
```

## Release process

- [ ] Update changelog
- [ ] Remove SNAPSHOT from .xml, .md & pom.xml
- [ ] Last review in docs (Manual)
- [ ] Review git changes for hidden issues (Manual) https://github.com/jabrena/cursor-rules-java/compare/0.10.0...feature/release-0110
- [ ] Tag repository
- [ ] Create article
- [ ] Communicate in social media

---

```bash
# Prompt to provide a release changelog
Can you update the current changelog for 0.11.0 comparing git commits in relation to 0.10.0 tag. Use  @https://keepachangelog.com/en/1.1.0/  rules

# Prompt to update the project to a new version
Update xml files from @resources/ and update the version to 0.11.0 removing snapshot. Update @pom.xml with the new version 0.11.0 Generate system prompts again with ./mvnw clean install -pl system-prompts-generator

## Note: Refactor a bit more to include all pom.xml

## Tagging process
git tag --list
git tag 0.11.0
git push --tags
```
