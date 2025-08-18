# Java Documentation Implementation Guide

## README.md Generation

### Single Module Project

For single module projects, generate a comprehensive README.md in the project root with the following structure:

```markdown
# [Project Name]

## Software Description

[Analyze the entire src/main/java directory and provide a comprehensive description of the software, including:
- Main purpose and functionality
- Key classes and their responsibilities
- Architecture patterns used
- Dependencies and integrations
- Entry points (main classes, controllers, etc.)]

## Getting Started

### Prerequisites
- Java [version]
- Maven [version]

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
./mvnw spring-boot:run  # For Spring Boot projects
# or
java -jar target/[artifact-name].jar
```

## Configuration

[If configuration files are found, document the key configuration options]

## API Documentation

[If REST controllers or web services are detected, provide API overview]

## Contributing

Please follow the existing code style and include tests for any new functionality.

## License

[Include license information if LICENSE file exists]
```

### Multi-Module Project

For multi-module Maven projects:

1. **Root README.md**: Overview of the entire project with links to module-specific documentation
2. **Module-specific README.md**: Each module gets its own README.md with detailed information about that module

#### Root README.md Structure:
```markdown
# [Project Name]

## Software Description

[High-level project overview and architecture]

## Modules

- **[module-1]**: [Brief description] ([link to module README](module-1/README.md))
- **[module-2]**: [Brief description] ([link to module README](module-2/README.md))

[Rest of common sections...]
```

#### Module README.md Structure:
```markdown
# [Module Name]

## Software Description

[Detailed analysis of this specific module, including:
- Module's purpose within the larger project
- Key classes and their responsibilities
- Module-specific dependencies
- Integration points with other modules]

[Module-specific getting started, configuration, etc.]
```

## package-info.java Generation

For each package found in src/main/java, generate or update package-info.java files:

### Basic Level
```java
/**
 * [Package name] - [Brief description of package purpose]
 *
 * This package contains [main functionality description].
 * Key classes: [list main classes with brief descriptions]
 */
package [package.name];
```

### Detailed Level
```java
/**
 * [Package name] - [Comprehensive description]
 *
 * <h2>Purpose</h2>
 * [Detailed explanation of what this package does]
 *
 * <h2>Main Components</h2>
 * <ul>
 * <li>{@link ClassName1} - [Description and purpose]</li>
 * <li>{@link ClassName2} - [Description and purpose]</li>
 * </ul>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * // Example code showing typical usage
 * }</pre>
 *
 * <h2>Dependencies</h2>
 * [Description of external dependencies this package uses]
 *
 * @since [version]
 * @author [author info if available]
 */
package [package.name];
```

### Minimal Level
```java
/**
 * [Brief one-line description of package purpose].
 */
package [package.name];
```

## Implementation Strategy

1. **Codebase Analysis**: Use codebase_search to understand project structure, main classes, and functionality
2. **File Detection**: Check for existing README.md and package-info.java files
3. **Content Generation**: Based on analysis, generate appropriate documentation
4. **Merge Strategy**: If files exist, follow user preference for handling (overwrite/merge/backup)
5. **Validation**: Ensure generated documentation is accurate and helpful

## File Handling Options

### Overwrite
- Replace existing file content completely
- Create backup with .backup extension before overwriting

### Add New Information
- Parse existing README.md to identify existing sections
- Add missing sections without duplicating existing content
- For package-info.java, enhance existing documentation if present

### Create Backup
- Always create [filename].backup before making changes
- Useful for preserving original content while updating

### Skip Existing
- Only create documentation for files that don't already exist
- Safe option to avoid modifying existing documentation
