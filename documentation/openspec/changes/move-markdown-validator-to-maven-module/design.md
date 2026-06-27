## Context

Issue #941 asks for a dedicated Maven module for the Markdown validator because the current JBang script has grown in responsibility and CI runtime. The current script lives at `.github/scripts/MarkdownValidator.java` and is called from `.github/workflows/maven.yaml` with:

```bash
jbang .github/scripts/MarkdownValidator.java --verbose .
```

The repository currently has `skills-generator` and `site-generator` modules registered in the root `pom.xml`.

## Intended Behavior Change

Markdown validation remains available to contributors and CI, but the validator implementation becomes a Maven-owned Java component with a clearer design and faster execution. CI should continue to validate Markdown when Markdown files change, and local contributors should retain a JBang-compatible command path.

## Two-Step Change Strategy

### Step 1: Behavior-Preserving Preparation

Create the Maven module and move the existing validator behavior into it without intentionally changing validation rules, command options, target directories, exit-code semantics, or reported validation failures.

The existing script should become a thin JBang-compatible entry point, or equivalent wrapper, that calls the Maven module main class using `//SOURCES` as needed. This preserves the current contributor workflow while giving the implementation a proper module boundary.

Validation after Step 1:

- Run the new module tests or characterization tests for current parsing, remote-link filtering, fail-fast, default directory, missing directory, and exit-code behavior.
- Run the Maven build for the new module.
- Run the JBang entry point against a representative repository checkout.

### Step 2: Performance-Oriented Behavior Change

After the behavior-preserving extraction is validated, review the algorithm and introduce parallel execution where it is safe. Candidate areas include independent file validation and remote-link checks with shared caching. The implementation should consider Java structural concurrency where it improves cancellation, failure handling, and bounded execution.

The behavior change is the improved runtime profile, not a change to which Markdown errors are detected. Output ordering may be made deterministic if parallel execution would otherwise make reports difficult to compare.

Validation after Step 2:

- Run focused tests for parallel validation, shared remote-link caching, fail-fast behavior, and deterministic result aggregation.
- Run the Maven build for the validator module.
- Run the repository Markdown validation command.
- Compare runtime against the current CI baseline recorded in issue #941 where practical.

## Design Constraints

- Preserve the current command-line contract unless a compatibility change is explicitly documented.
- Keep JBang support; contributors should not lose the existing script-style invocation path.
- Keep CI behavior aligned with the existing `validate-markdown` job trigger.
- Use bounded parallelism and deterministic error aggregation.
- Preserve HTTP timeout behavior and interruption handling.
- Avoid broad root build changes beyond registering the new Maven module and wiring validation.

## Open Questions

- The final module directory and artifact id should be chosen during implementation. A descriptive name such as `markdown-validator` is expected unless a maintainer chooses a different naming convention.
- The target runtime improvement threshold is not specified beyond improving the reported 40-second baseline. Implementation should record before/after measurements when practical.
