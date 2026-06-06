# Java Skills 快速入门

如果你希望使用这些 `Skills for Java` 带来的能力，请阅读本文档。

## 与本项目相关的概念

### 什么是 Skill？

Agent Skills 是一种轻量、开放的格式，用于用专门知识和工作流扩展 AI 智能体能力。

从核心上看，一个 skill 是一个包含 SKILL.md 文件的文件夹。该文件至少包含名称和描述等元数据，并包含指令，用来告诉智能体如何执行某项具体任务。Skills 也可以附带脚本、模板和参考材料。

## 如何安装 Skills？

### 使用 Registry：npx skills

使用以下命令列出并安装 Skills：

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

### 使用 Claude plugins

```bash
claude plugin marketplace add https://github.com/jabrena/cursor-rules-java
```

### 使用 Skillsjars

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

将 Skills 提取到与 Cursor、Claude Code 或 Github Copilot 等 AI 工具兼容的目录：

```bash
./mvnw skillsjars:extract -Ddir=.agents/skills
./mvnw skillsjars:extract -Ddir=.claude/skills
./mvnw skillsjars:extract -Ddir=.github/skills
```

### 使用 Skill portals

- https://www.awesomeskills.dev/en/skill/jabrena-cursor-rules-java
- https://shyft.ai/skills/cursor-rules-java
