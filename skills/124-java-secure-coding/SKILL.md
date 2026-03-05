---
name: 124-java-secure-coding
description: Use when you need to apply Java secure coding best practices — including validating untrusted inputs, defending against injection attacks with parameterized queries, minimizing attack surface via least privilege, applying strong cryptographic algorithms, handling exceptions securely without exposing sensitive data, managing secrets at runtime, avoiding unsafe deserialization, and encoding output to prevent XSS. Part of the skills-for-java project
metadata:
  author: Juan Antonio Breña Moral
  version: 0.12.0
---
# Java Secure coding guidelines

Identify and apply Java secure coding practices to reduce vulnerabilities, protect sensitive data, and harden application behaviour against common attack vectors.

**Prerequisites:** Run `./mvnw compile` or `mvn compile` before applying any changes. If compilation fails, **stop immediately** — do not proceed until the project is in a valid state.

**Core areas:** Input validation with type, length, format, and range checks; SQL/OS/LDAP injection defence via `PreparedStatement` and parameterized APIs; attack surface minimisation through least-privilege permissions and removal of unused features; strong cryptographic algorithms for hashing (passwords with BCrypt/Argon2), encryption (AES-GCM), and digital signatures while avoiding deprecated ciphers (MD5, SHA-1, DES); secure exception handling that logs diagnostic details internally while exposing only generic messages to clients; secrets management by loading credentials from environment variables or secret managers — never hardcoded; safe deserialization with strict allow-lists and preference for explicit DTOs over native Java serialization; output encoding to prevent XSS in rendered content.

**Scope:** The reference is organized by examples (with good/bad code patterns) for each core area. Apply recommendations based on applicable examples; validate compilation before changes and run `./mvnw clean verify` or `mvn clean verify` after applying improvements.

**Before applying changes:** Read the reference for detailed good/bad examples, constraints, and safeguards for each secure coding pattern.

## Reference

For detailed guidance, examples, and constraints, see [references/124-java-secure-coding.md](references/124-java-secure-coding.md).
