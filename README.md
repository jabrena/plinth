# Enhanced Java Documentation Rule with ADR Support

This enhanced version of the `170-java-documentation.xml` rule combines traditional Java documentation generation with Architecture Decision Record (ADR) creation capabilities, inspired by the agile ADR template and conversational assistant approach.

## Features

### 1. Traditional Java Documentation
- **Comprehensive Javadoc Generation**: Automated creation of detailed Javadoc comments for classes, methods, and fields
- **Package Documentation**: Support for package-info.java files with architectural context
- **Best Practices Enforcement**: Follows Oracle's Javadoc conventions and industry standards
- **Template-Based Generation**: Consistent documentation structure across projects

### 2. Architecture Decision Records (ADRs)
- **Conversational ADR Creation**: Interactive prompts guide users through the decision-making process
- **Structured Templates**: Based on agile ADR template with sections for context, decision, and consequences
- **Status Management**: Support for ADR lifecycle (proposed, accepted, implemented, deprecated, superseded)
- **Cross-Reference Integration**: Links between code documentation and architectural decisions

### 3. Integration Capabilities
- **Unified Documentation**: Seamless integration between Javadoc and ADRs
- **Consistency Management**: Tools to maintain alignment between code and architectural documentation
- **File Organization**: Structured approach to organizing documentation artifacts

## Usage

### Generating Java Documentation

The rule automatically triggers when working with Java files or when documentation-related keywords are detected:

```java
/**
 * Service for managing user authentication and authorization.
 * 
 * <p>This service implements OAuth 2.0 authentication flow and provides
 * role-based access control for the application.</p>
 * 
 * <p><strong>Thread Safety:</strong> This class is thread-safe and can be
 * used concurrently by multiple threads.</p>
 * 
 * @author Development Team
 * @since 1.0
 * @see UserRepository
 * @see SecurityConfig
 */
public class AuthenticationService {
    // Implementation follows architectural decision documented in ADR-003.
}
```

### Creating Architecture Decision Records

When architectural decisions need to be documented, the conversational assistant guides you through:

1. **Context Gathering**: Understanding the problem and stakeholders
2. **Decision Drivers**: Identifying requirements and constraints
3. **Options Analysis**: Evaluating alternatives
4. **Decision Rationale**: Documenting the chosen option and reasoning
5. **Consequences**: Capturing expected outcomes and risks

#### Example ADR Output

```markdown
# ADR-003: Adopt Spring Security for Authentication

## Status
Accepted

## Context
The application requires a robust authentication and authorization system 
to protect sensitive user data and ensure compliance with security standards.

### Problem Statement
We need to implement user authentication that supports multiple authentication 
methods while maintaining security best practices and regulatory compliance.

### Decision Drivers
- Security compliance requirements (GDPR, SOX)
- Support for OAuth 2.0 and SAML
- Integration with existing user directory
- Development team expertise
- Long-term maintainability

## Considered Options
1. Custom authentication implementation
2. Spring Security framework
3. Apache Shiro
4. Third-party authentication service

## Decision
We will adopt Spring Security framework for our authentication needs.

### Rationale
Spring Security provides comprehensive security features, excellent documentation, 
and strong community support. It integrates well with our Spring Boot application 
and offers the flexibility needed for our authentication requirements.

## Consequences

### Positive
- Proven security framework with regular updates
- Comprehensive feature set (OAuth, SAML, JWT)
- Strong community and documentation
- Good integration with Spring ecosystem

### Negative
- Learning curve for team members unfamiliar with Spring Security
- Potential over-engineering for simple use cases
- Framework dependency and vendor lock-in

### Neutral
- Configuration complexity requires careful planning
- Regular updates needed to maintain security

## Implementation Notes
- Configure Spring Security using Java configuration
- Implement custom UserDetailsService for user directory integration
- Set up OAuth 2.0 client registration for external providers

## Follow-up Actions
- [ ] Create Spring Security configuration
- [ ] Implement custom authentication provider
- [ ] Set up integration tests
- [ ] Update documentation

---
**Date:** 2025-09-15
**Stakeholders:** Development Team, Security Team, Product Owner
**Status:** Accepted
```

## File Organization

The enhanced rule promotes a structured approach to documentation:

```
project/
├── docs/
│   ├── adrs/
│   │   ├── README.md                     # ADR index
│   │   ├── adr-001-technology-stack.md
│   │   ├── adr-002-database-choice.md
│   │   └── adr-003-authentication.md
│   └── api/
│       └── javadoc/                      # Generated Javadoc
├── src/
│   └── main/
│       └── java/
│           ├── package-info.java         # Package documentation
│           └── com/example/
│               └── service/
│                   └── AuthenticationService.java
```

## Integration with Development Workflow

### Build Integration
- Integrate ADR generation into build process
- Validate documentation completeness
- Generate unified documentation site

### Git Hooks
- Remind developers to update documentation
- Validate ADR status consistency
- Check for missing architectural decisions

### IDE Templates
- Quick ADR creation templates
- Javadoc snippet generation
- Cross-reference helpers

## Best Practices

### For Javadoc
- Write from the user's perspective
- Keep documentation current with code changes
- Use active voice and present tense
- Include usage examples for public APIs

### For ADRs
- Capture decisions when they're fresh
- Focus on the "why" rather than the "what"
- Update ADR status as decisions evolve
- Keep ADRs concise but comprehensive
- Link related decisions and code

### For Integration
- Reference ADRs in relevant code documentation
- Maintain consistency between documentation types
- Create cross-references between related decisions
- Archive obsolete decisions appropriately

## Conversational Prompts

The rule includes guided prompts to help create comprehensive ADRs:

1. **Context**: "What specific problem or challenge are you addressing?"
2. **Drivers**: "What are the key requirements and constraints?"
3. **Options**: "What alternatives did you consider?"
4. **Decision**: "What option did you choose and why?"
5. **Consequences**: "What are the expected benefits and risks?"

## Customization

The XML rule can be customized by:
- Modifying templates for specific organizational needs
- Adding custom ADR statuses
- Adjusting conversational prompts
- Extending cross-reference patterns
- Customizing file organization structure

## Version History

- **v2.0**: Added ADR generation capabilities with conversational assistant
- **v1.0**: Basic Java documentation generation (original rule)

This enhanced rule bridges the gap between code-level documentation and architectural decision documentation, providing a comprehensive approach to technical documentation in Java projects.