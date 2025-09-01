# Cursor Rules Generator

## Software Description

The Cursor Rules Generator is a specialized Java application designed to transform XML-based rule definitions into Markdown Cursor (MDC) files for the Cursor AI code editor. This tool serves as a critical component in the Java Cursor Rules ecosystem, enabling the automated generation of comprehensive AI coding assistance rules from structured XML specifications.

## What is the structure of a System prompt?

According to the documentation from [Google Gemini](https://drive.google.com/file/d/1AbaBYbEa_EbPelsT40-vj64L-2IwUJHy/view), [Anthropic Claude](https://docs.anthropic.com/en/docs/build-with-claude/prompt-engineering/overview) & [OpenAI ChatGPT](https://chatgpt.com/share/686d1066-9e40-800b-ac7f-cc8df7e4c7d0), a prompt could be structured in the following way:

- Metadata
- Role
- Context
- Goal
- Tone
- Constraints
- Instructions
- Examples
- Output format
- Safeguards

With this structure in mind, the project uses an XML Schema to define the way that all System prompts are generated for Cursor AI. If you are interested in this area, you could review [the Schema](./generator/src/main/resources/pml.xsd).

**Note:** It is not necessary to add all parts in a prompt.

## Getting Started

### Prerequisites

- Java 24
- Maven 3.9.10

### Building the Project

```bash
./mvnw clean compile
```

### Running Tests

```bash
./mvnw test
```

### Running the Application

```bash
# Generate all cursor rules
./mvnw clean package

# The generated .md files will be available in the target directory
# and automatically copied to the .cursor/rules directory during install phase
./mvnw install
```
