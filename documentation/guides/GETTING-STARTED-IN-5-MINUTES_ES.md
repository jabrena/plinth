# Primeros pasos en 5 minutos

> **Idiomas:** [English](./GETTING-STARTED-IN-5-MINUTES.md) · [中文](./GETTING-STARTED-IN-5-MINUTES_ZH.md)

Esta ruta ofrece a los usuarios nuevos un inicio pequeño y práctico antes de leer toda la documentación del proyecto.

## 1. Instala Node.js si lo necesitas

La CLI de Skills.sh se ejecuta mediante `npx`, así que necesitas tener Node.js disponible:

```bash
node --version
npx --version
```

Si falta alguno de los comandos, instala Node.js con el gestor de paquetes de tu sistema operativo.

## 2. Elige tu agente y descarga los Skills

Instala todos los skills desde la [entrada del registry de Skills.sh](https://skills.sh/jabrena/cursor-rules-java) de este proyecto en el agente que uses con más frecuencia:

```bash
# Cursor
npx skills add jabrena/cursor-rules-java --skill '*' --agent cursor -y

# Claude Code
npx skills add jabrena/cursor-rules-java --skill '*' --agent claude-code -y

# Codex
npx skills add jabrena/cursor-rules-java --skill '*' --agent codex -y

# GitHub Copilot
npx skills add jabrena/cursor-rules-java --skill '*' --agent github-copilot -y
```

Para instalar en todos los agentes soportados, usa el modo multiagente de la CLI:

```bash
npx skills add jabrena/cursor-rules-java --skill '*' --agent '*' -y
```

Para revisar primero los skills disponibles:

```bash
npx skills add jabrena/cursor-rules-java --list
```

## 3. Instala los commands del proyecto

Después de instalar los skills, indica a tu agente qué AI agent harness usas: `cursor`, `claude-code`, `codex` o `github-copilot`.

```text
install @004-commands-installation cursor
install @004-commands-installation claude-code
install @004-commands-installation codex
install @004-commands-installation github-copilot
```

El instalador copia los commands embebidos del proyecto en el directorio de commands de tu herramienta. Soporta destinos para Cursor, Claude, Codex y GitHub Copilot, y pregunta antes de escribir archivos.

Los destinos habituales para commands son:

- `.cursor/command`
- `.claude/commands`
- `.codex/commands`
- `.github/commands`

## 4. Instala los agents del proyecto cuando tu herramienta los soporte

Commands y skills son suficientes para empezar. Si también quieres usar los robot agents predefinidos, indica a tu agente qué AI agent harness usas: `cursor` o `claude-code`.

```text
install @005-agents-installation cursor
install @005-agents-installation claude-code
```

El instalador soporta `.cursor/agents` y `.claude/agents`.

## 5. Usa el workflow

Este proyecto organiza el trabajo Java asistido por IA alrededor de cuatro bloques:

- `Commands` son los puntos de entrada, como `/update-issue`, `/create-spec` y `/implement-issue`.
- `Agents` definen responsabilidades, como análisis de negocio, arquitectura, liderazgo técnico, implementación Java o trabajo de rendimiento.
- `Skills` aportan prácticas enfocadas para Java, frameworks, testing, documentación, seguridad y observabilidad.
- `MCP Servers` conectan los agentes con herramientas externas y contexto del proyecto cuando están disponibles.

Una ruta habitual es:

```text
/update-issue -> /create-spec -> /implement-issue -> /profile or /benchmark
```

Para trabajo solo de documentación o planificación, puedes detenerte cuando el issue, plan, especificación, ADR o diagrama esté completo.

## Profundiza

- [Inventario de commands](./INVENTORY-COMMANDS-JAVA.md)
- [Primeros pasos con agents](./GETTING-STARTED-AGENTS_ES.md)
- [Primeros pasos con skills](./GETTING-STARTED-SKILLS_ES.md)
- [Workflows del proyecto](./GETTING-STARTED-WORKFLOWS_ES.md)
- [Primeros pasos con pipelines](./GETTING-STARTED-PIPELINES_ES.md)
