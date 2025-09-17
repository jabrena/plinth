# Contributor Quickstart Guide

## Project overview

A collection of `System prompts` for Java Enterprise development.

### Repository Layout

```
cursor-rules-java/
├── .cursor/rules/                    # 🎯 25 System prompts for Java Enterprise development (main output)
├── system-prompts-generator/         # 🏗️ Java transformation engine (XML → Markdown cursor rules)
│   ├── src/main/resources/          # XML rule definitions + XSLT stylesheets
│   └── src/test/java/              # Automated testing for rule generation
├── examples/                        # 🧪 9 comprehensive demo projects
│   ├── spring-boot-demo/           # Complete agile workflow with requirements
│   ├── spring-boot-*-demo/         # Performance, memory leak, JMeter demos
│   ├── quarkus-demo/               # Supersonic Subatomic Java framework
│   ├── aws-lambda-hello-world/     # Serverless Java on AWS
│   ├── azure-function-hello-world/ # Serverless Java on Azure
│   └── maven-demo*/                # Testing scenarios (working + broken)
├── site-generator/                  # 📚 JBake-powered educational website generator
│   ├── content/                    # Blog posts, courses, documentation
│   ├── templates/                  # 18 FreeMarker templates for different content types
│   └── assets/                     # CSS, JS, images for website
├── documentation/                   # 📖 Comprehensive documentation ecosystem
│   ├── adr/                        # Architectural Decision Records (3 ADRs)
│   ├── reviews/                    # AI tool compatibility matrices & evaluations
│   ├── jeps/                       # Java Enhancement Proposals tracking
│   ├── dvbe25/                     # 🎤 Conference presentation (Reveal.js)
│   └── excalidraw/                 # System diagrams and visual documentation
└── docs/                           # 🌐 Generated static website (GitHub Pages)
    ├── courses/java-generics/      # 5-module structured learning course
    ├── blog/                       # Technical articles and release notes
    └── tags/                       # 23 topic-based content organization
```

## Modules

### 🎯 .cursor/rules/
The main output of the project - a collection of 25 system prompts for Java Enterprise development. These Markdown files are automatically generated from XML sources and provide AI coding assistance across build systems, design patterns, testing, profiling, and documentation.

### 🏗️ system-prompts-generator/
Java transformation engine that converts XML rule definitions into Markdown cursor rules using XSLT. Features a 4-stage functional pipeline with XInclude support for modularity, automated testing, and Maven integration for deployment to `.cursor/rules/`.

### 🧪 examples/
Nine comprehensive demo projects showcasing different Java frameworks and scenarios:
- **Spring Boot demos**: Complete agile workflows, performance testing, memory leak analysis, JMeter integration
- **Quarkus demo**: Supersonic Subatomic Java framework demonstration
- **Cloud demos**: AWS Lambda and Azure Functions serverless examples
- **Maven demos**: Both working and intentionally broken projects for testing cursor rules

### 📚 site-generator/
JBake-powered static site generator that transforms Markdown content into a professional documentation website. Features 18 specialized FreeMarker templates, structured learning courses (like the 5-module Java Generics course), blog system with tagging, and automated deployment to GitHub Pages.

### 📖 documentation/
Comprehensive documentation ecosystem including:
- **ADRs**: 3 Architectural Decision Records documenting technical choices
- **Reviews**: AI tool compatibility matrices and evaluations
- **JEPs**: Java Enhancement Proposals tracking for language evolution
- **Conference Materials**: Reveal.js presentation for conferences and meetups
- **Visual Documentation**: Excalidraw diagrams and system visualizations

### 🌐 docs/
Generated static website deployed to GitHub Pages, containing structured courses, technical blog posts, and topic-based navigation with 23 different tags for content organization.

## Development Guidelines

- **Rule Editing**: Don't update files in `.cursor/rules` directly - find the XML source files in `system-prompts-generator/src/main/resources/`
- **Java Version**: The project is based on Java 24
- **Build Workflow**: Use the XML-to-Markdown transformation pipeline to generate cursor rules from XML sources
- **Testing**: Always run tests before promoting changes to ensure rule generation works correctly

## The Comprehensive Documentation Ecosystem

The repository includes a sophisticated documentation framework with multiple specialized components:

### 1. Architectural Decision Records (ADRs)
- **Location**: `documentation/adr/`
- **Purpose**: Formal documentation of technical decisions with rationale and consequences
- **Content**: 3 ADRs covering major architectural choices:
  - XML-based rule generation approach
  - Manual scope configuration
  - Website generation strategy

### 2. Tool Compatibility & Review System
- **Location**: `documentation/reviews/`
- **Purpose**: Systematic evaluation of AI coding assistants
- **Content**: Detailed comparison matrices of 6+ AI tools (Cursor, JetBrains, GitHub Copilot, etc.)
- **Metrics**: Usability, response speed, reasoning, model support, context windows

### 3. Java Enhancement Proposals (JEPs) Tracking
- **Location**: `documentation/jeps/`
- **Purpose**: Continuous monitoring of Java language evolution from Java 8 to Java 25
- **Integration**: Analysis of how new JEPs could improve existing cursor rules
- **Value**: Keeps the project current with Java language developments

## System Architecture

### The Transformation Pipeline

The repository implements a sophisticated XML-to-Markdown transformation system that powers the entire cursor rules generation process through a multi-stage pipeline:

#### Core Architecture
- **XML Schema-Based Rule Definitions**: 25+ structured XML files defining complete cursor rules following a common vocabulary defined by [pml.xsd](https://jabrena.github.io/pml/schemas/0.1.0-SNAPSHOT/pml.xsd) (see [system-prompts-generator/src/main/resources/](system-prompts-generator/src/main/resources/))
- **XSLT Transformation Engine**: Single unified stylesheet (`cursor-rules.xsl`) converting XML to Markdown
- **Java Processing Pipeline**: 4-stage functional pipeline with XInclude support for modularity
- **Modular Fragment System**: 13 reusable templates and automation scripts

#### Transformation Flow
```
XML Rule Definitions → XInclude Processing → XSLT Transformation → Markdown Cursor Rules
```

#### Key Benefits
- **Consistency at Scale**: All 25+ cursor rules follow identical structure and formatting
- **Domain-Specific Language**: Structured approach to prompt engineering with reusable components
- **Enterprise Maintainability**: Single source of truth with automated testing and deployment
- **Build Integration**: Maven-driven automation with automatic deployment to `.cursor/rules/`

This pipeline essentially functions as a **compiler for cursor rules**, transforming high-level XML specifications into deployable Markdown files while ensuring consistency, maintainability, and scalability across the entire rule set.

### Site Generation & Public Documentation System

The repository includes a comprehensive **JBake-powered static site generator** that transforms Markdown content into a professional documentation website deployed at `https://jabrena.github.io/cursor-rules-java/`.

#### Architecture & Integration
- **Technology Stack**: JBake 2.7.0-rc.7 with FreeMarker templates and Maven integration
- **Build Integration**: Dedicated Maven module (`site-generator`) with specialized profile (`site-update`)
- **Output Location**: Generates static HTML/CSS/JS files in the `docs/` directory for GitHub Pages deployment
- **Content Pipeline**: Markdown → JBake → Static HTML → GitHub Pages

#### Content Organization
- **Blog System**: Technical articles with date-based organization and tagging
  - Release announcements and changelogs
  - Technical deep-dives (e.g., "The Three-Node Quality Framework for AI Prompts")
  - Feature explanations and tutorials
- **Course Generation**: Structured learning modules with progressive complexity
  - **Java Generics Course**: 5-module comprehensive course with assessments
  - **Module Structure**: Foundations → Wildcards → Advanced → Real-world → Assessment
  - **Interactive Elements**: FAQ sections, code examples, and practical exercises
- **Tag-Based Navigation**: Semantic organization by topics (generics, type-safety, performance, etc.)

#### Template System
- **18 Specialized Templates**: Custom FreeMarker templates for different content types
- **Responsive Design**: Bootstrap-based responsive layout with custom styling
- **Content Types**: Posts, courses, pages, archives, tag listings, and RSS feeds
- **Dynamic CSS Generation**: Template-driven CSS compilation for consistent theming

#### Build Commands
```bash
# Generate and deploy the website
./mvnw clean generate-resources -pl site-generator -P site-update

# Local development server
jwebserver -p 8000 -d "$(pwd)/docs"
```

#### Key Features
- **Automated Course Generation**: Transforms cursor rule content into structured learning paths
- **SEO Optimization**: Proper meta tags, sitemaps, and RSS feeds
- **Analytics Integration**: Google Analytics and social media integration
- **Asset Management**: Optimized handling of images, CSS, and JavaScript resources

#### Content Strategy
The site serves multiple purposes:
1. **Public Documentation**: User-facing guides and tutorials
2. **Educational Content**: Structured courses for learning Java concepts
3. **Project Updates**: Release notes and development progress
4. **Community Engagement**: Blog posts and technical articles

This system transforms the repository from a simple rule collection into a **comprehensive educational platform** that supports both immediate cursor rule usage and long-term Java learning objectives.

## Build/Test Commands

- Java 24 required

```bash
./mvnw clean verify
./mvnw clean verify -pl system-prompts-generator
./mvnw clean install -pl system-prompts-generator
./mvnw clean generate-resources -pl site-generator -P site-update
```

## Common Tasks

- Adding new cursor rule: Edit XML in system-prompts-generator/src/main/resources/
- Updating website: Modify content in site-generator/content/
- Running examples: Each example has its own README-DEV.md

## File Editing Guidelines
- Don't edit .cursor/rules/ directly - edit XML sources in system-prompts-generator/src/main/resources/
- Always run tests before promoting changes

## Commit Messages and Pull Requests

- Follow the [Chris Beams](http://chris.beams.io/posts/git-commit/) style for
  commit messages.
- Every pull request should answer:
  - **What changed?**
  - **Why?**
  - **Breaking changes?**
- Comments should be complete sentences and end with a period.

## References

- https://agents.md/
