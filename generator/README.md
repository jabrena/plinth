# Cursor Rules Generator

## Software Description

The Cursor Rules Generator is a specialized Java application designed to transform XML-based rule definitions into Markdown Cursor (MDC) files for the Cursor AI code editor. This tool serves as a critical component in the Java Cursor Rules ecosystem, enabling the automated generation of comprehensive AI coding assistance rules from structured XML specifications.

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

# The generated .mdc files will be available in the target directory
# and automatically copied to the .cursor/rules directory during install phase
./mvnw install
```
