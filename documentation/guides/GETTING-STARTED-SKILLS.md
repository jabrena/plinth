# Getting started for Skills for Java

If you are interested in getting the benefits of these `Skills for Java`, read this document.

## Concepts related to this project

### What is a Skill?

Agent Skills are a lightweight, open format for extending AI agent capabilities with specialized knowledge and workflows.

At its core, a skill is a folder containing a SKILL.md file. This file includes metadata (name and description, at minimum) and instructions that tell an agent how to perform a specific task. Skills can also bundle scripts, templates, and reference materials.

## How to install the Skills?

### Using a Registry: npx skills

Use the following commands to list and install the Skills:

```bash
#To install npx
brew install node
sudo apt install nodejs npm

# Install at User level
npx skills add jabrena/cursor-rules-java --global --agent cursor --all
npx skills add jabrena/cursor-rules-java --global --agent claude-code --skill '*' -y

npx skills remove --global --agent cursor -y
npx skills remove --global --agent claude-code --skill '*' -y
npx skills remove --global -y \
  $(jq -r '.skills | to_entries[] | select(.value.source == "jabrena/cursor-rules-java") | .key' ~/.agents/.skill-lock.json)

# Install at project level
npx skills --help
npx skills add jabrena/cursor-rules-java --list
npx skills add jabrena/cursor-rules-java --agent cursor --all
npx skills add jabrena/cursor-rules-java --agent claude-code --all
```

### Using Claude plugins

```bash
claude plugin marketplace add https://github.com/jabrena/cursor-rules-java
```

### Using Skillsjars

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

### Using Skill portals

- https://www.awesomeskills.dev/en/skill/jabrena-cursor-rules-java
- https://shyft.ai/skills/cursor-rules-java
