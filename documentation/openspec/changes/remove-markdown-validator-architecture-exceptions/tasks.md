# Tasks

## 1. Implementation Checklist

- [ ] 1.1 Review the current `ArchitectureTest`, `MarkdownValidator`, and `MarkdownValidatorCommand` dependency graph.
- [ ] 1.2 Run `./mvnw test -pl markdown-validator` to establish the current test baseline before refactoring.
- [ ] 1.3 Add or adjust focused CLI characterization tests if existing tests do not cover command defaults, root handling, success exit codes, and failure exit codes.
- [ ] 1.4 Introduce an explicit composition/bootstrap boundary for Markdown validator startup.
- [ ] 1.5 Refactor `MarkdownValidatorCommand` to receive its application dependency instead of constructing concrete outbound adapters.
- [ ] 1.6 Preserve the existing JBang-compatible `MarkdownValidator` entry point and update source directives if new production classes are added.
- [ ] 1.7 Run `./mvnw test -pl markdown-validator` and confirm behavior is preserved after the preparatory refactoring.
- [ ] 1.8 Remove the dependency-specific `ignoreDependency(...)` entries from `ArchitectureTest`.
- [ ] 1.9 Update the architecture rule to model the explicit composition/bootstrap boundary without allowing application-to-adapter dependencies.
- [ ] 1.10 Run `./mvnw test -pl markdown-validator -Dtest=ArchitectureTest`.
- [ ] 1.11 Run `./mvnw clean verify -pl markdown-validator`.
- [ ] 1.12 Run `openspec validate --all` from `documentation/`.
