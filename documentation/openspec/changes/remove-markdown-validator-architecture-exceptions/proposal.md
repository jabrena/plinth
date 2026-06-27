## Why

The Markdown validator architecture test currently suppresses three dependency violations with explicit `ignoreDependency(...)` entries:

- `MarkdownValidator` depends directly on `MarkdownValidatorCommand`.
- `MarkdownValidatorCommand` depends directly on `FileSystemMarkdownFileFinder`.
- `MarkdownValidatorCommand` depends directly on `HttpClientRemoteLinkRequester`.

Those exceptions hide two design concerns: the CLI adapter performs outbound adapter wiring, and the executable entry point is not modeled as an explicit composition boundary. Removing the exceptions will make the module's onion architecture rule describe the intended structure directly.

## What Changes

- Refactor Markdown validator startup so object creation and adapter wiring live in an explicit composition/bootstrap boundary.
- Keep `MarkdownValidatorCommand` focused on CLI parsing, command execution, and reporting.
- Preserve existing command-line behavior, defaults, validation behavior, JBang usage, and Maven module entry point.
- Replace the current ArchUnit dependency exceptions with a rule structure that models the allowed composition boundary explicitly.

## Capabilities

### New Capabilities

None.

### Modified Capabilities

- `markdown-validator-module`: clarifies architecture boundaries for the Markdown validator module and requires the onion architecture test to pass without the current dependency-specific ignores.

## Source and Derivation

- Source artifact: maintainer request in this conversation to create an OpenSpec change from the architecture-test cleanup idea.
- Code context: `markdown-validator/src/test/java/info/jab/markdownvalidator/ArchitectureTest.java`.
- Code context: `markdown-validator/src/main/java/info/jab/markdownvalidator/MarkdownValidator.java`.
- Code context: `markdown-validator/src/main/java/info/jab/markdownvalidator/adapter/in/cli/MarkdownValidatorCommand.java`.
- Existing capability: `documentation/openspec/specs/markdown-validator-module/spec.md`.
- Derivation direction: maintainer cleanup idea plus current code dependencies -> OpenSpec change artifacts -> implementation refactoring and validation.

## Change Boundary Assessment

This is one reviewable change because the outcome is a single architecture cleanup for the Markdown validator module: remove dependency-specific ArchUnit exceptions while preserving existing CLI behavior. The work should not be split by technical layer because the design boundary, command behavior, and architecture test all describe one acceptance target.

## Impact

The change is expected to affect Markdown validator production code and tests only:

- `markdown-validator/src/main/java/info/jab/markdownvalidator/**`
- `markdown-validator/src/test/java/info/jab/markdownvalidator/**`
- `documentation/openspec/changes/remove-markdown-validator-architecture-exceptions/**`

No generated cursor rules, generated public skills, website output, or release artifacts are in scope.
