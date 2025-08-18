# Cursor Rules Generator

## Software Description

The Cursor Rules Generator is a specialized Java application designed to transform XML-based rule definitions into Markdown Cursor (MDC) files for the Cursor AI code editor. This tool serves as a critical component in the Java Cursor Rules ecosystem, enabling the automated generation of comprehensive AI coding assistance rules from structured XML specifications.

### Main Purpose and Functionality

The application functions as an XML/XSLT transformation engine that processes rule definition files and converts them into properly formatted cursor rules. It implements a robust transformation pipeline that ensures consistent, validated output while maintaining strict adherence to the defined schema specifications.

### Key Classes and Their Responsibilities

- **CursorRulesGenerator**: The core transformation engine that orchestrates the entire rule generation process. This class implements functional programming principles with immutable data structures and pure functions for reliable, predictable transformations.

- **TransformationSources**: An immutable record that encapsulates XML and XSLT input streams, ensuring thread-safe resource management throughout the transformation pipeline.

- **ValidationErrorHandler**: A specialized error handler that provides comprehensive reporting for XSD validation issues, enabling precise identification and resolution of schema violations.

### Architecture Patterns Used

The application employs several key architectural patterns:

- **Pipeline Pattern**: The transformation process follows a clear pipeline structure with distinct stages for resource loading, validation, transformation, and output generation.
- **Functional Programming**: Extensive use of Optional types, pure functions, and immutable data structures ensures reliable, side-effect-free operations.
- **Strategy Pattern**: Configurable XSD validation and XSLT transformation allow for flexible rule processing based on different schema requirements.
- **Builder Pattern**: Resource loading and SAX source creation utilize builder-like patterns for clean, readable configuration.

### Dependencies and Integrations

The application utilizes standard Java XML processing capabilities including:
- **XML Processing**: DOM and SAX parsers for XInclude processing and validation
- **XSLT Transformation**: javax.xml.transform for rule generation
- **XSD Validation**: Schema validation ensuring rule definition integrity
- **Logging**: SLF4J with Logback for comprehensive operation tracking
- **Testing**: JUnit 5 with AssertJ for comprehensive test coverage including parameterized testing across all rule definitions

### Entry Points

The primary entry point is the `CursorRulesGenerator.generate()` method, which accepts XML rule definitions, XSLT transformation templates, and optional schema files to produce validated MDC output. The application supports both automated batch processing and individual rule generation workflows.

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
