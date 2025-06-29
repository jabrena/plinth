# XML DTD Building Blocks for System Prompt Modeling

## Overview

This document describes an XML Document Type Definition (DTD) designed to model system prompts in a structured, reusable way. The DTD is based on analysis of Java Cursor Rules and provides building blocks that can abstract and replicate the patterns found in markdown-based cursor rules using XML.

## Motivation

System prompts (cursor rules) currently exist as markdown files with varying structures. This XML DTD provides:

- **Standardization**: Consistent structure across all system prompts
- **Validation**: Automatic validation of prompt structure and content
- **Tooling**: Enable automated generation, transformation, and analysis
- **Abstraction**: Clear separation between content and presentation
- **Modularity**: Reusable components and templates
- **Machine Readability**: Structured data that can be processed programmatically

## Building Blocks Analysis

### Core Pattern Analysis from Cursor Rules

After analyzing the existing cursor rules, the following patterns emerged:

1. **Metadata Pattern**: Frontmatter with description, file globs, and configuration
2. **Role-Based System Characterization**: Defining AI behavior and expertise
3. **Structured Guidelines**: Numbered rules with explanations and examples
4. **Interactive Questioning**: Conditional questions based on user needs
5. **Template-Driven Content**: Reusable code and configuration templates
6. **Workflow-Based Instructions**: Step-by-step processes with validation
7. **Example-Driven Learning**: Good/bad code examples with explanations
8. **Cross-Referencing**: Links to other rules and templates

## XML DTD Building Blocks

### 1. Root Element: `system-prompt`

```xml
<!ELEMENT system-prompt (metadata, header, system-characterization, description, principles?, table-of-contents?, content-sections+)>
<!ATTLIST system-prompt
    id CDATA #REQUIRED
    version CDATA #IMPLIED>
```

**Purpose**: Root container for the entire system prompt
**Attributes**: 
- `id`: Unique identifier (e.g., "java-maven-best-practices")
- `version`: Optional version tracking

**Maps to**: Overall cursor rule structure

### 2. Metadata Section

```xml
<!ELEMENT metadata (description, globs?, always-apply?, tags?)>
<!ELEMENT description (#PCDATA)>
<!ELEMENT globs (#PCDATA)>
<!ELEMENT always-apply (#PCDATA)>
<!ELEMENT tags (tag*)>
<!ELEMENT tag (#PCDATA)>
```

**Purpose**: Configuration and classification information
**Maps to**: Markdown frontmatter in `.mdc` files

**Example**:
```xml
<metadata>
    <description>Maven Best Practices for Java Development</description>
    <globs>pom.xml</globs>
    <always-apply>false</always-apply>
    <tags>
        <tag>maven</tag>
        <tag>java</tag>
        <tag>build-system</tag>
    </tags>
</metadata>
```

### 3. System Characterization

```xml
<!ELEMENT system-characterization (role-definition, expertise-area*)>
<!ELEMENT role-definition (#PCDATA)>
<!ELEMENT expertise-area (#PCDATA)>
```

**Purpose**: Define AI role and expertise domains
**Maps to**: "System prompt characterization" sections in cursor rules

**Example**:
```xml
<system-characterization>
    <role-definition>You are a Senior software engineer with extensive experience in Java software development</role-definition>
    <expertise-area>Maven build system</expertise-area>
    <expertise-area>Java dependency management</expertise-area>
</system-characterization>
```

### 4. Content Sections (Main Body)

The `content-sections` element contains different types of structured content:

#### 4.1 Rule Section

```xml
<!ELEMENT rule-section (rule-header, rule-description, code-examples?)>
<!ELEMENT code-examples (good-example*, bad-example*)>
<!ELEMENT good-example (code-block, explanation?)>
<!ELEMENT bad-example (code-block, explanation?)>
```

**Purpose**: Guidelines with good/bad examples
**Maps to**: Numbered rules in cursor rules like "Rule 1: Effective Dependency Management"

#### 4.2 Question Section

```xml
<!ELEMENT question-section (question-header, question-description?, question-items+)>
<!ATTLIST question-section
    type (single | multiple | conditional) #IMPLIED
    dependency CDATA #IMPLIED>
<!ELEMENT question-items (question-item*)>
<!ELEMENT question-item (option-text, option-description?)>
```

**Purpose**: Interactive questions for gathering user requirements
**Maps to**: Question templates like `java-maven-questions-template.md`

#### 4.3 Template Section

```xml
<!ELEMENT template-section (template-header, template-description?, template-content)>
<!ELEMENT template-content (code-block | parameter-list | placeholder*)>
<!ELEMENT parameter-list (parameter*)>
<!ELEMENT parameter (param-name, param-description, param-default?)>
<!ELEMENT placeholder (#PCDATA)>
<!ATTLIST placeholder name CDATA #REQUIRED>
```

**Purpose**: Reusable code and configuration templates
**Maps to**: Template files like `java-maven-plugins-template.md`

#### 4.4 Workflow Section

```xml
<!ELEMENT workflow-section (workflow-header, workflow-description?, workflow-steps)>
<!ELEMENT workflow-steps (workflow-step*)>
<!ELEMENT workflow-step (step-header, step-description, step-content?, prerequisites?, validation?)>
<!ATTLIST workflow-step
    number CDATA #IMPLIED
    type (mandatory | optional | conditional) #IMPLIED
    dependency CDATA #IMPLIED>
```

**Purpose**: Step-by-step processes and procedures
**Maps to**: Multi-step workflows in rules like profiling and build processes

#### 4.5 Instruction Section

```xml
<!ELEMENT instruction-section (instruction-header, instruction-description?, instruction-rules)>
<!ELEMENT instruction-rules (instruction-rule*)>
<!ELEMENT instruction-rule (#PCDATA | emphasis | code-inline)*>
<!ATTLIST instruction-rule
    type (mandatory | optional | conditional) #IMPLIED
    priority (high | medium | low) #IMPLIED>
```

**Purpose**: AI behavior instructions and constraints
**Maps to**: "Instructions for AI" sections in cursor rules

## Benefits of XML DTD Approach

### 1. Structure Validation
- Automatic validation of system prompt structure
- Ensures all required elements are present
- Validates element relationships and nesting

### 2. Content Organization
- Clear separation of different content types
- Hierarchical organization of information
- Consistent structure across all prompts

### 3. Tooling Enablement
- Automatic generation from templates
- Transformation to different output formats (HTML, PDF, Markdown)
- Schema-aware editing tools

### 4. Modularity and Reusability
- Template-based content generation
- Shared building blocks across prompts
- Parameter substitution and customization

### 5. Machine Processing
- Programmatic analysis of prompt content
- Automated quality checks and metrics
- Integration with CI/CD pipelines

## Conclusion

This XML DTD provides a comprehensive framework for modeling system prompts in a structured, maintainable way. By abstracting the patterns found in cursor rules, it enables:

- Consistent structure across all prompts
- Automated validation and quality assurance
- Tool-based generation and transformation
- Machine-readable prompt definitions
- Modular and reusable components

The building blocks identified cover all major patterns found in the existing cursor rules while providing extensibility for future needs. 