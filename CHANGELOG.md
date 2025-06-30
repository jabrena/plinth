# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [0.7.0] - 2025-06-30

### Added

- **Java Profiling Support**: Added comprehensive profiling cursor rules (#81, #88, #91)
  - `@161-java-profiling-detect` for detecting performance issues
  - `@162-java-profiling-analyze` for analyzing profiling results  
  - `@164-java-profiling-compare` for comparing profiling data
- **Java Checklist Guide**: Added `@100-java-checklist-guide` cursor rule to help developers use cursor rules effectively (#59)
- **Maven Documentation**: Added `@112-java-maven-documentation` cursor rule to generate README-DEV.md from existing pom.xml files
- **Maven Dependencies & Plugins**: Added `@111-java-maven-deps-and-plugins` cursor rule for better Maven dependency management
- **Performance Testing**: Added `@151-java-performance-jmeter` cursor rule for JMeter-based performance testing (#97)
- **Cloud Examples**: Added serverless examples with native image support
  - AWS Lambda Hello World example with GraalVM native image configuration
  - Azure Function Hello World example with GraalVM configuration and Azure-specific setup
- **Maven Demo**: Added complete Maven demo project with Euler problem examples and proper testing structure
- **Quarkus Demo**: Added Quarkus framework example with profiling support and Docker configurations
- **Performance Examples**: Added specialized demo projects (#82, #86, #95)
  - Spring Boot comprehensive demo with film query service, PostgreSQL integration, and full testing suite
  - Spring Boot Memory Leak Demo with profiling tools and detailed analysis documentation
  - Spring Boot Performance Bottleneck Demo with CPU profiling and optimization examples
  - Spring Boot JMeter Demo for performance testing integration
- **Template System**: Added comprehensive template files to support cursor rule generation
  - Java checklist templates for systematic development approaches
  - Maven dependencies and plugins templates for project setup
  - Performance testing script templates
  - Profiling script templates for application analysis
- **Documentation**: Added extensive documentation and diagrams
  - Cursor interaction sequence diagrams

### Changed

- **Rule Organization**: Reorganized cursor rule numbering system for better categorization
- **Documentation**: Significantly improved README and development guides (#74, #90)
- **Maven Plugins**: Improved cursor rules for Maven plugins with better examples and guidance (#54, #56)
- **Modularization**: Improved project structure to make cursor rules more modular (#105)

### Removed

- **Cache Files**: Removed Maven cache files that were not useful for daily development work (#44)
- **Logging**: Removed unnecessary MDC behavior that added complexity without clear criteria (#89)
- **Redundant Rules**: Removed or consolidated several cursor rules for better organization
  - Removed `@122-java-integration-testing` (consolidated into other testing rules)
  - Removed framework-specific rules to separate repositories:
    - `@301-framework-spring-boot` (moved to separate Spring Boot rules project) (#105)
    - `@304-java-rest-api-design` (moved to separate Spring Boot rules project)
    - `@500-sql` (moved to Spring Boot rules)

## [0.6.0] 30/5/2025

### Added

- Added a new cursor rules about Maven dependencies & plugins

### Changed

- Removed Cursor rules about Books for clarity
- Moved the Cursor rule about Acceptance criterias as part of the repository about [Agile](https://github.com/jabrena/cursor-rules-agile)
- Increased consistency in all cursor rules, now all examples use asserts from AssertJ

## [0.5.0] 20/05/2025

### Added

- Added new cursor rules (Maven, Acceptance Criteria, Object oriented design, Type Design, Secure codign guidelines, REST API Design)
- Added template for future cursor rules
- Added JEP inventory

### Changed

- Updated all cursor rules

## [0.4.0] 27/04/2025

### Added

- Added new cursor rules (Refactoring, Unit Testing & Integration testing)

### Changed

- Updated all cursor rules

## [0.3.0] 06/04/2025

### Added

- Added new cursor rules (SQL, Logging, Modern Java features)

### Changed

- Updated cursor rules (Java, Effective Java, Concurrency, Functional programming, Data Oriented programming Pragmatic Unit Testing, Spring Boot & Quarkus)


## [0.2.0] 01/03/2025

### Added

- Added new cursor rules (Pragmatic Unit Testing, Quarkus)

### Changed

- Updated cursor rules (Java, Effective Java, Concurrency, Functional programming, Data Oriented programming & Spring Boot)

## [0.1.0] 09/02/2025

### Added

- Added initial cursor rules (Java, Effective Java, Concurrency, Functional programming, Data Oriented programming & Spring Boot)