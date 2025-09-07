---
# These are optional metadata elements. Feel free to remove any of them.
status: "adopted"
date: 2025-07-08
decision-makers: Juan Antônio Breña Moral - Project Lead
consulted: Not necessary - output maintains compatibility with previous versions while improving consistency
informed:
---

# Generate Cursor Rules from XML Files

## Context and Problem Statement

The Java Cursor Rules project was facing significant challenges with manual maintenance of cursor rules across multiple files. The manual approach was becoming difficult to maintain and lacked consistency across rule files. There was a clear need for a unified definition approach that could ensure uniform structure and enable automated generation of markdown files with consistent frontmatter.

The current situation involved scattered rule definitions without standardization, making it difficult to maintain consistency and requiring significant manual effort for updates and modifications.

## Decision Drivers

* **Maintainability**: Reducing manual maintenance overhead of cursor rule files
* **Consistency**: Ensuring uniform structure and formatting across all rule files
* **Standardization**: Using XSD for unified rule definitions with schema validation
* **Automation**: Leveraging XSL transformations for consistent markdown generation with frontmatter

## Considered Options

* **Option 1**: Continue with manual maintenance of cursor rules (status quo)
* **Option 2**: Generate cursor rules from XML files using XSD/XSL transformations
* **Option 3**: PromptML-based approach using Domain Specific Language for prompts

## Decision Outcome

Chosen option: "Generate cursor rules from XML files using XSD/XSL transformations", because it provides the best fit for a Java-oriented project, uses proven and mature technology stack (XML/XSD/XSL), addresses all core maintenance and consistency issues, and enables full automation of the rule generation process.

### Consequences

* Good, because maintenance is now much better and more consistent across all generated files
* Good, because the unified XSD schema ensures standardized rule definitions
* Good, because XSL transformations provide automated and consistent markdown generation
* Good, because it fits naturally into the Java ecosystem and existing toolchain
* Good, because it reduces manual errors and improves development velocity

### Confirmation

The implementation has been successfully adopted and is confirmed to be working as intended through the following indicators:

* **Consistency Validation**: All generated cursor rules now follow a uniform structure and format
* **Maintainability Assessment**: Rule modifications and updates are significantly easier to implement
* **Feature Discovery**: It is now much easier to infer if existing rules require new features or enhancements
* **Quality Assurance**: The XSD validation ensures all rule definitions meet the established schema requirements

## Pros and Cons of the Options

### Option 1: Continue with Manual Maintenance

Manual creation and maintenance of cursor rule files using direct markdown editing.

* Good, because it provides direct control over file content and formatting
* Good, because it requires no additional toolchain or build process dependencies
* Bad, because it leads to inconsistencies across multiple rule files
* Bad, because it requires significant manual effort for updates and maintenance
* Bad, because it is prone to human errors and formatting mistakes
* Bad, because it doesn't scale well as the number of rules grows

### Option 2: Generate cursor rules from XML files using XSD/XSL

XML-based rule definitions with XSD schema validation and XSL transformations for markdown generation.

* Good, because it ensures consistency across all generated rule files
* Good, because it fits well with Java ecosystem and existing project structure
* Good, because it uses mature and proven technology stack (XML/XSD/XSL)
* Good, because it enables automation and reduces manual maintenance overhead
* Good, because XSD provides schema validation and enforces rule structure
* Good, because it supports easy modifications and updates through XML editing
* Neutral, because it requires understanding of XML/XSD/XSL technologies
* Neutral, because it adds a generation step to the build process

### Option 3: PromptML-based approach

Using PromptML Domain Specific Language for defining prompts and rules.

* Good, because it provides a specialized DSL designed for prompt engineering
* Good, because it offers structured approach to prompt definition
* Bad, because it doesn't align well with the Java-oriented project ecosystem
* Bad, because it would require additional Python dependencies and toolchain
* Bad, because it adds complexity without clear benefits over XML approach
* Bad, because it's less mature compared to established XML technologies

## More Information

The implementation includes:

* XML rule definitions following established XSD schema in the `/generator` module
* XSL transformations for generating consistent markdown files with frontmatter
* Integration with the existing Java-based project structure and build process

This decision has been successfully implemented and adopted, demonstrating improved maintainability and consistency in cursor rule management. The approach provides a solid foundation for future rule expansion and modification.
