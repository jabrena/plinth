## Context

The Markdown validator module follows an onion-style architecture with domain, application, inbound CLI adapter, outbound filesystem adapter, and outbound HTTP adapter packages. The current `ArchitectureTest` uses ArchUnit's `onionArchitecture()` rule but suppresses three dependencies.

The suppressed dependencies reveal two responsibility issues:

- The CLI command builds outbound adapter implementations, so the inbound adapter knows concrete outbound adapters.
- The executable entry point depends on the CLI adapter without a named bootstrap or composition boundary in the architecture rule.

The refactoring should make those responsibilities explicit while preserving command-line behavior.

## Decisions

### Composition Boundary

Introduce or clarify a composition/bootstrap boundary for Markdown validator startup. This boundary owns concrete object creation and wires together:

- `FileSystemMarkdownFileFinder`
- `HttpClientRemoteLinkRequester`
- application validators and services
- the CLI command object consumed by Picocli

The composition boundary may depend on inbound and outbound adapters because its role is to assemble the executable application. That permission must be visible in the architecture test as a named rule or layer, not as dependency-specific `ignoreDependency(...)` exceptions.

### CLI Command Responsibility

Keep `MarkdownValidatorCommand` focused on:

- Picocli options and parameters
- invoking `MarkdownValidationService`
- rendering the validation report through the CLI reporter
- translating validation results into exit codes

`MarkdownValidatorCommand` must not instantiate concrete outbound adapter implementations. It may receive the application service or ports through constructor injection.

### Entry Point Compatibility

Preserve the documented main class and JBang-compatible entry point from the existing `markdown-validator-module` specification. The refactoring may delegate from `MarkdownValidator` to a named composition object, but users and CI must still be able to run the supported entry point.

### Architecture Rule Shape

Remove the dependency-specific `ignoreDependency(...)` entries from `ArchitectureTest`. If a composition boundary remains necessary, represent it explicitly in the test through a named package/layer decision so the rule explains the architecture instead of hiding individual dependencies.

### Two-Step Change Sequence

Use the two-step method:

1. Behavior-preserving preparation: move object creation out of the CLI command and introduce the composition boundary without changing command behavior.
2. Architecture enforcement change: remove the dependency-specific ignores and update the architecture test to describe the allowed composition boundary explicitly.

## Validation Strategy

- Run `./mvnw test -pl markdown-validator` after the behavior-preserving refactoring.
- Run `./mvnw test -pl markdown-validator -Dtest=ArchitectureTest` after removing the ignores.
- Run `./mvnw clean verify -pl markdown-validator` before promoting the implementation.
- Run `openspec validate --all` for the OpenSpec artifacts.

## Risks

- ArchUnit's onion architecture helper may not model composition roots directly. If so, the implementation should use a clear named architecture rule that preserves onion constraints for domain, application, and adapters while explicitly allowing bootstrap composition.
- The JBang source directives in `MarkdownValidator.java` must stay aligned with any moved or added production classes.

## Open Questions

None for the specification. Implementation may choose the exact package and class names for the composition boundary as long as the architecture rule and supported entry point remain clear.
