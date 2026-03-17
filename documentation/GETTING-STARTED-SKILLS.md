# Getting started for Skills for Java

If you are interested in getting the benefits of these `Skills for Java`, read this document.

## Concepts related to this project

### What is a Skill?

Agent Skills are a lightweight, open format for extending AI agent capabilities with specialized knowledge and workflows.

At its core, a skill is a folder containing a SKILL.md file. This file includes metadata (name and description, at minimum) and instructions that tell an agent how to perform a specific task. Skills can also bundle scripts, templates, and reference materials.

## How to install the Skills?

### npx skills

Use the following commands to list and install the Skills:

```bash
#To install npx
brew install node
sudo apt install nodejs npm

npx skills --help
npx skills add jabrena/cursor-rules-java --list
npx skills add jabrena/cursor-rules-java --all --agent cursor
```

### skillsjars

https://www.skillsjars.com/
https://github.com/skillsjars/skillsjars-maven-plugin

```xml
<build>
    <plugins>
        <plugin>
            <groupId>com.skillsjars</groupId>
            <artifactId>maven-plugin</artifactId>
            <version>0.0.5</version>
            <dependencies>
                <!-- Your SkillsJars -->
                <dependency>
                    <groupId>com.skillsjars</groupId>
                    <artifactId>SKILLJAR_ARTIFACT_ID</artifactId>
                    <version>SKILLJAR_VERSION</version>
                </dependency>
            </dependencies>
        </plugin>
    </plugins>
</build>
```

Extract Skills in a directory compatible with your AI Tool like Cursor, Claude Code or Github Copilot:

```bash
./mvnw skillsjars:extract -Ddir=.agents/skills
./mvnw skillsjars:extract -Ddir=.claude/skills
./mvnw skillsjars:extract -Ddir=.github/skills
```
