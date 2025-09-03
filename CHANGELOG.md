# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [0.10.0] 2025-09-04

### Added

- **New Cursor Rules**:
  - `@127-java-exception-handling`: Comprehensive cursor rule for Java exception handling with best practices and examples
  - `@128-java-generics`: Advanced cursor rule covering Java generics patterns, bounded types, and type erasure workarounds
  - `@170-java-documentation`: Specialized cursor rule for generating comprehensive Java documentation with C4 diagrams and UML

- **Enhanced Development Tools & Scripts**:
  - JMH (Java Microbenchmark Harness) support for Maven projects without modules
  - Enhanced Maven plugins cursor rule with comprehensive JMH integration

- Tested the project for Cursor, Cursor CLI, Claude Code, GitHub Copilot & JetBrains Junie. Further information at https://github.com/jabrena/cursor-rules-java/blob/main/docs/reviews/review-20250829.md

### Changed

- **File Extension Migration**:
  - **Breaking Change**: Renamed all cursor rule files from `.mdc` to `.md` extension for better readability

- **Generator System Enhancements**:
  - Added remote XML schema fetching capabilities (https://github.com/jabrena/pml)

## [0.9.0] 2025-07-22

### Added

- **Version Control for Cursor Rules**:
  - All cursor rules include version control

- **New Cursor Rules**:
  - `@127-java-functional-exception-handling`: Comprehensive cursor rule for handling exceptions in functional programming style with Either & Optional
  - `@111-java-maven-dependencies`: Focused cursor rule for Maven dependency management
  - `@112-java-maven-plugins`: Dedicated cursor rule for Maven plugins management

- **Enhanced Documentation & Getting Started Guide**:
  - New `GETTING-STARTED.md`: comprehensive guide for new users
  - `docs/articles/prompt-quality-framework.md`: Framework for evaluating prompt quality and cursor rule effectiveness

- **Performance & Profiling Enhancements**:
  - Enhanced JMeter integration with improved scripts and detailed performance analysis reports

- **Project Examples & Templates**:
  - `examples/maven-demo-ko/`: New negative example project demonstrating common Maven pitfalls
  - Enhanced generator template system with modular fragments
  - New behavioral templates for consultative interaction patterns

### Changed

- **Cursor Rules Architecture Overhaul**:
  - **Externalized Behavior**: Moved common behavioral patterns from individual rules to shared templates for consistency
  - **Enhanced Structure**: All cursor rules now include explicit sections for:
    - Detailed constraints and preconditions
    - Standardized output format specifications
    - Comprehensive safeguards and verification steps
  - **Consultative Approach**: Reinforced interactive, consultative methodology across all rules

- **Rule Organization & Refinement**:
  - Split `@111-java-maven-deps-and-plugins` into two focused, specialized rules for better clarity
  - Simplified and refined questioning approach in Maven-related cursor rules

- **Quality & Consistency Improvements**:
  - Standardized all cursor rules with enhanced constraints, output formats, and safeguards
  - Improved template system with shared behavioral patterns and reduced duplication
  - Enhanced XML schema validation and XSL transformation consistency

### Removed

- **Template Consolidation**:
  - Removed redundant template files that were consolidated into the generator system:
    - `java-checklist-template.md`
    - `java-maven-deps-template.md`
    - `java-maven-plugins-template.md`
    - `java-performance-script-template.md`
  - Removed old combined `@111-java-maven-deps-and-plugins.md` (split into separate rules)
  - Removed `@100-java-checklist-guide.md` (replaced with cursor rules list)

## [0.8.0] 2025-07-11

### Added

- **XML-Based Generation System**: Implemented comprehensive XML/XSL transformation system for generating cursor rules
  - New `generator` module with XML schema validation (pml.xsd)
  - XSL transformations for consistent markdown generation
  - Automated generation of cursor rules from XML definitions
- **Architecture Decision Records (ADRs)**: Added formal documentation for architectural decisions
  - ADR-001: Generate cursor rules from XML files
  - ADR-002: Configure cursor rules with manual scope
- **Comprehensive User Guide**: Added `CURSOR-RULES-JAVA.md` as a complete reference guide for all cursor rules
- **Build Infrastructure Improvements**:
  - JBang script for markdown validation
  - GitHub workflow artifact upload for generated cursor rules

### Changed

- **Cursor Rule Interaction Model**: All cursor rules now use consultative approach instead of prescriptive
  - Present 2-3 solution options with pros/cons for each improvement
  - Wait for user choice before implementing changes
  - Interactive approach with clear problem identification
- **Manual Scope Configuration**: All cursor rules configured with manual scope by design to improve performance
  - Eliminates automatic activation that caused performance degradation
  - Requires explicit user activation for better control and deterministic results
- **Enhanced Safeguards**: All cursor rules now include verification commands (`mvn clean verify` or `./mvnw clean verify`)

### Technical Improvements

- **Schema Validation**: Implemented XSD schema validation for all XML cursor rule definitions
- **Build Process**: Enhanced build pipeline with XML validation and automated rule generation
- **Code Quality**: Improved code formatting and consistency across all generated cursor rules
- **Error Handling**: Better error handling and validation in generation process

## [0.7.0] 2025-06-30

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

- Added new cursor rules about Maven dependencies & plugins

### Changed

- Removed Cursor rules about Books for clarity
- Moved the Cursor rule about acceptance criteria as part of the repository about [Agile](https://github.com/jabrena/cursor-rules-agile)
- Increased consistency in all cursor rules, now all examples use asserts from AssertJ

## [0.5.0] 20/05/2025

### Added

- Added new cursor rules (Maven, Acceptance Criteria, Object-oriented design, Type Design, Secure coding guidelines, REST API Design)
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

- Updated cursor rules (Java, Effective Java, Concurrency, Functional programming, Data-Oriented programming, Pragmatic Unit Testing, Spring Boot & Quarkus)


## [0.2.0] 01/03/2025

### Added

- Added new cursor rules (Pragmatic Unit Testing, Quarkus)

### Changed

- Updated cursor rules (Java, Effective Java, Concurrency, Functional programming, Data-Oriented programming & Spring Boot)

## [0.1.0] 09/02/2025

### Added

- Added initial cursor rules (Java, Effective Java, Concurrency, Functional programming, Data-Oriented programming & Spring Boot)
