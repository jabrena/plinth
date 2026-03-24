---
name: 124-java-secure-coding
description: Use when you need to apply Java secure coding best practices — including validating untrusted inputs, defending against injection attacks with parameterized queries, minimizing attack surface via least privilege, applying strong cryptographic algorithms, handling exceptions securely without exposing sensitive data, managing secrets at runtime, avoiding unsafe deserialization, and encoding output to prevent XSS. Part of the skills-for-java project
license: Apache-2.0
metadata:
  author: Juan Antonio Breña Moral
  version: 0.13.0-SNAPSHOT
---
# Java Secure coding guidelines

Identify and apply Java secure coding practices to reduce vulnerabilities, protect sensitive data, and harden application behaviour against common attack vectors.

**What is covered in this Skill?**

- Input validation: type, length, format, and range checks
- SQL/OS/LDAP injection defence via `PreparedStatement` and parameterized APIs
- Attack surface minimisation: least-privilege permissions, removal of unused features
- Strong cryptography: BCrypt/Argon2 for passwords, AES-GCM for encryption, digital signatures; avoid deprecated ciphers (MD5, SHA-1, DES)
- Secure exception handling: log diagnostic details internally, expose only generic messages to clients
- Secrets management: load credentials from environment variables or secret managers — never hardcoded
- Safe deserialization: strict allow-lists, prefer explicit DTOs over native Java serialization
- Output encoding to prevent XSS in rendered content

**Scope:** The reference is organized by examples (good/bad code patterns) for each core area. Apply recommendations based on applicable examples.

## Workflow

1. **Compile first** — Run `./mvnw compile` or `mvn compile`. If compilation fails, stop immediately.
2. **Read the reference** — Review the detailed good/bad examples for each secure coding area before making changes.
3. **Identify vulnerabilities** — Analyze Java code for security issues: injection risks, weak cryptography, hardcoded secrets, unsafe deserialization, missing input validation, and information leakage.
4. **Apply fixes incrementally** — Implement secure coding improvements one area at a time, validating after each change.
5. **Verify** — Run `./mvnw clean verify` to confirm all tests pass and no regressions were introduced.

## Quick Reference

SQL injection prevention — use `PreparedStatement` instead of string concatenation:

```java
// GOOD: Parameterized query prevents SQL injection
String query = "SELECT * FROM orders WHERE customer_id = ?";
try (PreparedStatement pstmt = connection.prepareStatement(query)) {
    pstmt.setString(1, customerId);  // safely bound
    ResultSet rs = pstmt.executeQuery();
}

// BAD: String concatenation allows SQL injection
String query = "SELECT * FROM orders WHERE customer_id = '" + customerId + "'";
Statement stmt = connection.createStatement();
stmt.executeQuery(query);  // attacker can inject: '; DROP TABLE orders; --
```

## Constraints

Before applying any secure coding changes, ensure the project compiles. If compilation fails, stop immediately — do not proceed until resolved. After applying improvements, run full verification.

- **MANDATORY**: Run `./mvnw compile` or `mvn compile` before applying any changes
- **SAFETY**: If compilation fails, stop immediately — do not proceed until the project is in a valid state
- **VERIFY**: Run `./mvnw clean verify` or `mvn clean verify` after applying improvements
- **BEFORE APPLYING**: Read the reference for detailed good/bad examples, constraints, and safeguards for each secure coding pattern

## When to use this skill

- Review Java code for secure coding

## Reference

For detailed guidance, examples, and constraints, see [references/124-java-secure-coding.md](references/124-java-secure-coding.md).
