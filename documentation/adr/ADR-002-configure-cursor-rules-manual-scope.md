---
# These are optional metadata elements. Feel free to remove any of them.
status: "implemented"
date: "2025-07-10"
decision-makers: "Project Lead"
consulted: "N/A"
informed: "Public via README"
---

# Configure All Cursor Rules with Manual Scope

## Context and Problem Statement

Multiple cursor rules were being applied automatically in the development environment, which caused significant performance degradation. This automatic activation led to less deterministic results and increased latencies in answers and processing from the cursor platform. The degraded performance was negatively impacting the development experience and productivity.

## Decision Drivers

* Performance degradation caused by automatic application of multiple cursor rules
* Need for more deterministic and predictable results
* Requirement to improve development experience and reduce latencies

## Considered Options

* Manual scoping - Configure all cursor rules to require explicit manual activation
* Keep automatic activation - Maintain the current automatic rule application system

## Decision Outcome

Chosen option: "Manual scoping", because it provides significant performance improvement by ensuring only one cursor rule manually added to the context impacts the context window, eliminating the performance degradation caused by multiple automatically activated rules.

### Consequences

* Good, because significant performance improvement with reduced latencies
* Good, because more predictable and deterministic results
* Good, because better control over context window usage
* Good, because improved user experience
* Good, because reduced resource consumption
* Good, because better debugging and troubleshooting capability
* Good, because more focused rule application
* Good, because easier maintenance
* Bad, because requires explicit action by software engineers
* Bad, because introduces some manual overhead in the development workflow

### Confirmation

Response time measurements will be used to validate that the manual scoping approach delivers the expected performance improvements. Regular monitoring of cursor platform response times will confirm the effectiveness of this decision.

## Pros and Cons of the Options

### Manual scoping

This approach requires developers to explicitly activate cursor rules when needed, ensuring only one rule impacts the context at a time.

* Good, because only one cursor rule manually added to context impacts the context window
* Good, because eliminates performance degradation from multiple simultaneous rules
* Good, because provides complete control over which rules are active
* Good, because results in more predictable behavior
* Bad, because requires explicit action by software engineers
* Bad, because introduces manual overhead, though not dramatic

### Keep automatic activation

This maintains the current system where cursor rules are automatically applied based on context.

* Good, because provides convenience with no manual intervention required
* Good, because maintains familiar workflow for developers
* Bad, because multiple cursor rules enabled simultaneously degrade performance
* Bad, because leads to less deterministic results
* Bad, because increases latencies in cursor platform responses

## More Information

This decision addresses performance issues documented in the cursor-rules-sandbox project, specifically regarding message length and processing overhead when multiple rules are active simultaneously. The implementation will include comprehensive documentation explaining how to manually activate and use cursor rules effectively.

The decision will be communicated publicly through the project README to ensure all contributors understand the new approach and can effectively utilize the manual scoping system.
