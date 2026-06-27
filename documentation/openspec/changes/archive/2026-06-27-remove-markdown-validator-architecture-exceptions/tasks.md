# Tasks

## 1. Implementation Checklist

- [x] 1.1 Review the current `ArchitectureTest`, `MarkdownValidator`, and `MarkdownValidatorCommand` dependency graph.
- [x] 1.2 Run `./mvnw test -pl markdown-validator` to establish the current test baseline before refactoring.
- [x] 1.3 Add or adjust focused CLI characterization tests if existing tests do not cover command defaults, root handling, success exit codes, and failure exit codes.
- [x] 1.4 Keep composition wiring in the executable entry point for Markdown validator startup.
- [x] 1.5 Refactor `MarkdownValidatorCommand` to receive its application dependency instead of constructing concrete outbound adapters.
- [x] 1.6 Preserve the existing JBang-compatible `MarkdownValidator` entry point and update source directives if new production classes are added.
- [x] 1.7 Run `./mvnw test -pl markdown-validator` and confirm behavior is preserved after the preparatory refactoring.
- [x] 1.8 Remove the dependency-specific `ignoreDependency(...)` entries from `ArchitectureTest`.
- [x] 1.9 Update the architecture rule to check the adapter, application, and domain scaffold without allowing application-to-adapter dependencies.
- [x] 1.10 Run `./mvnw test -pl markdown-validator -Dtest=ArchitectureTest`.
- [x] 1.11 Run `./mvnw clean verify -pl markdown-validator`.
- [x] 1.12 Run `openspec validate --all` from `documentation/`.
