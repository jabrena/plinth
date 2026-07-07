# Primeros pasos con Skills for Java

Si te interesa aprovechar estos `Skills for Java`, lee este documento.

## Conceptos relacionados con este proyecto

### ¿Qué es un Skill?

Agent Skills es un formato abierto y ligero para ampliar las capacidades de los agentes de IA con conocimiento especializado y flujos de trabajo.

En esencia, un skill es una carpeta que contiene un archivo SKILL.md. Este archivo incluye metadatos (como mínimo, nombre y descripción) e instrucciones que indican a un agente cómo realizar una tarea concreta. Los skills también pueden incluir scripts, plantillas y materiales de referencia.

## ¿Cómo instalar los Skills?

### Usando un Registry: npx skills

Usa los siguientes comandos para listar e instalar los Skills:

```bash
#To install npx
brew install node
sudo apt install nodejs npm

# Install at User level
npx skills add jabrena/plinth --global --agent cursor --all
npx skills add jabrena/plinth --global --agent claude-code --skill '*' -y

npx skills remove --global --agent cursor -y
npx skills remove --global --agent claude-code --skill '*' -y
npx skills remove --global -y \
  $(jq -r '.skills | to_entries[] | select(.value.source == "jabrena/plinth") | .key' ~/.agents/.skill-lock.json)

# Install at project level
npx skills --help
npx skills add jabrena/plinth --list
npx skills add jabrena/plinth --agent cursor --all
npx skills add jabrena/plinth --agent claude-code --all
```

### Usando Claude plugins

```bash
claude plugin marketplace add https://github.com/jabrena/plinth
```

### Usando Skillsjars

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

Extrae los Skills en un directorio compatible con tu herramienta de IA, como Cursor, Claude Code o Github Copilot:

```bash
./mvnw skillsjars:extract -Ddir=.agents/skills
./mvnw skillsjars:extract -Ddir=.claude/skills
./mvnw skillsjars:extract -Ddir=.github/skills
```

### Usando portales de Skills

- https://www.awesomeskills.dev/en/skill/jabrena-cursor-rules-java
- https://shyft.ai/skills/cursor-rules-java
