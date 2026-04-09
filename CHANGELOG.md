# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [0.14.0] 2026-04-12

### Added

- **Skills**:
  - INVEST validation added to agile user-story workflow in `@014-agile-user-story` (#633)
  - Maven Central search guidance skill (`@114-java-maven-search`) (#605)
  - OpenSpec adoption for project change management in `@042-planning-openspec` (#620, #621, #616)
  - GitHub issue management workflow support`@043-planning-github` (#607)
  - Jira planning support and guidance improvements in `@044-planning-jira` (#631, #641, #642)
  - OpenAPI guidance skill `@701-technologies-openapi` (#635)
  - WireMock guidance skill `@702-technologies-wiremock` (#636)
  - Fuzz testing skill `@703-technologies-fuzzing-testing` and CATS example coverage (#634, #646)
  - Cursor and Claude plugin support documentation/workflow (#622)

- **Rules:**
  - Created an ADR to drop support for Rules in favor of Skills.

- **Examples:**
  - Reduce CI Pipeline times, removing non essential examples

## [0.13.0] 2026-03-30

### Added

- **Skills**:
  - ER diagram support in architecture diagrams guidance (`@033-architecture-diagrams`) (#511)
  - Plan mode skill enhancements (`@040-planning-plan-mode`) (#488)
  - JIB coverage and improved skill summaries (`@112-java-maven-plugins`) (#522)
  - Skill for Java testing strategies (`@130-java-testing-strategies`) (#523)
  - Spring Boot support (`@301-frameworks-spring-boot-core`, `@302-frameworks-spring-boot-rest`, `@311-frameworks-spring-jdbc`, `@312-frameworks-spring-data-jdbc`, `@313-frameworks-spring-db-migrations-flyway`, `@321-frameworks-spring-boot-testing-unit-tests`, `@322-frameworks-spring-boot-testing-integration-tests`, `@323-frameworks-spring-boot-testing-acceptance-tests`) (#502)
  - Quarkus support (`@401-frameworks-quarkus-core`, `@402-frameworks-quarkus-rest`, `@411-frameworks-quarkus-jdbc`, `@412-frameworks-quarkus-panache`, `@413-frameworks-quarkus-db-migrations-flyway`, `@421-frameworks-quarkus-testing-unit-tests`, `@422-frameworks-quarkus-testing-integration-tests`, `@423-frameworks-quarkus-testing-acceptance-tests`) (#560)
  - Micronaut support (`@501-frameworks-micronaut-core`, `@502-frameworks-micronaut-rest`, `@511-frameworks-micronaut-jdbc`, `@512-frameworks-micronaut-data`, `@513-frameworks-micronaut-db-migrations-flyway`, `@521-frameworks-micronaut-testing-unit-tests`, `@522-frameworks-micronaut-testing-integration-tests`, `@523-frameworks-micronaut-testing-acceptance-tests`) (#573, #584)

- **Agents**:
  - Business Analyst (#547)
  - Coordinator agent (#547)
  - Java agent (#547)
  - Spring Boot agent (#502)
  - Quarkus agent (#560)
  - Micronaut agent (#573, #584)

### Changed

- **PML & generators**:
  - Migrated XML rule sources to **PML Schema 0.7.0**; generator validates against https://jabrena.github.io/pml/schemas/0.7.0/pml.xsd (#525).

- **Skills**:
  - Improved the way to activate Skills (#574).
  - Reduced complexity for ADR discovery on functional requirements (`@031-architecture-adr-functional-requirements`) (#532).
  - C4 model guidance limited to levels 1–3 (`@033-architecture-diagrams`) (#504).
  - Improved `@113-java-maven-documentation` (#529).

### Removed

- Structural Concurrency guidance removed from `@125-java-concurrency` while the feature remains in preview (#530).

## [0.12.0] 2026-03-08

### Added

- **Skills & Agent system**:
  - SKILL generator for Cursor agent skills (#421).
  - Skills support evolution (v1–v4) with skilljars and validation pipeline (#416, #418, #420, #423, #424).
  - Improved skill generation and minimum requirements (#417, #427, #451).

- **New Cursor Rules**:
  - `@132-java-testing-integration-testing` with WireMock support (#443).
  - Arch Unit support in `@111-java-maven-dependencies` (#445).

- **Maven rules & docs**:
  - Centralized version management and best practices for multi-module POM in `@110-java-maven-best-practices` (#441).
  - Cyclomatic complexity analysis support in `@112-java-maven-plugins` (#439).
  - `DEVELOPER.md` and plugin catalog in `@113-java-maven-documentation` (#438).
  - Minimum Maven compiler support in `@112-java-maven-plugins` (#433).

- **Documentation & ADRs**:
  - System prompt to generate AGENTS.md files: `@173-java-agents` (#436).
  - Split Java documentation into ADR capabilities in `@170-java-documentation` and `@171-java-adr` (#455).

### Changed

- **PML migration**:
  - Migrated XML rule sources to PML Schema 0.5.0; generator validates against https://jabrena.github.io/pml/schemas/0.5.0/pml.xsd.

## [0.11.0] 2025-09-29

### Added

- **Behaviours**:
  - Added Tone variations to consultative and progressive learning behaviours (#330).
  - Added Tone to Interactive prompts

- **Website**:
  - Added Website to communicate about System prompts for Java. https://jabrena.github.io/cursor-rules-java/

- **Educational content**:
  - New "System Prompts for Java" course (#335). https://jabrena.github.io/cursor-rules-java/courses/system-prompts-java/index.html

- **Documentation & Diagrams**:
  - ADR-003 documenting JBake website generation strategy (#326).
  - Added UML State Machine diagram support to documentation rule `@170-java-documentation` (#310).

- Added initial support for AGENTS.md https://agents.md/

### Changed

- **Platform & Build**:
  - Upgraded project to Java 25; updated Maven Java version in builds (#315, #343).

- **Cursor rules**:
  - Extensive refinements across multiple rules (dependencies, plugins, secure coding, concurrency, exception handling, functional programming, documentation, diagrams) (#309, #341).
  - Decoupled system prompts from behaviours and added learning behaviour; reorganized generator templates and inventory (#309).
  - All examples use single imports without using wildcard *
  - Updated profiling Script to add support for Async-profiler 4.1 https://github.com/async-profiler/async-profiler/releases/tag/v4.1
  - Improved JFR support

### Removed

- **Cursor rules**:
  - Removed `@123-java-general-guidelines` (superseded by specialized rules) (#301).

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
- **Comprehensive User Guide**: Added `SYSTEM-PROMPTS-JAVA.md` as a complete reference guide for all cursor rules
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

[0.14.0]: https://github.com/jabrena/cursor-rules-java/compare/0.13.0...0.14.0
[0.13.0]: https://github.com/jabrena/cursor-rules-java/compare/0.12.0...0.13.0
[0.12.0]: https://github.com/jabrena/cursor-rules-java/compare/0.11.0...0.12.0
[0.11.0]: https://github.com/jabrena/cursor-rules-java/compare/0.10.0...0.11.0
[0.10.0]: https://github.com/jabrena/cursor-rules-java/compare/0.9.0...0.10.0
[0.9.0]: https://github.com/jabrena/cursor-rules-java/compare/0.8.0...0.9.0
[0.8.0]: https://github.com/jabrena/cursor-rules-java/compare/0.7.0...0.8.0
[0.7.0]: https://github.com/jabrena/cursor-rules-java/compare/0.6.0...0.7.0
[0.6.0]: https://github.com/jabrena/cursor-rules-java/compare/0.5.0...0.6.0
[0.5.0]: https://github.com/jabrena/cursor-rules-java/compare/0.4.0...0.5.0
[0.4.0]: https://github.com/jabrena/cursor-rules-java/compare/0.3.0...0.4.0
[0.3.0]: https://github.com/jabrena/cursor-rules-java/compare/0.2.0...0.3.0
[0.2.0]: https://github.com/jabrena/cursor-rules-java/compare/0.1.0...0.2.0
[0.1.0]: https://github.com/jabrena/cursor-rules-java/releases/tag/0.1.0
