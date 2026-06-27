## Context

Issue #941 asks for a dedicated Maven module for the Markdown validator because the current JBang script has grown in responsibility and CI runtime. The previous script lived at `.github/scripts/MarkdownValidator.java` and was called from `.github/workflows/maven.yaml` with:

```bash
jbang .github/scripts/MarkdownValidator.java --verbose .
```

The repository currently has `skills-generator` and `site-generator` modules registered in the root `pom.xml`.

## Intended Behavior Change

Markdown validation remains available to contributors and CI, but the validator implementation becomes a Maven-owned Java component with a clearer design and faster execution. CI should continue to validate Markdown when Markdown files change, and local contributors should run the Maven module main class directly with JBang.

## Two-Step Change Strategy

### Step 0: Local Timing Baseline

Before changing the validator implementation, record the current local runtime for the existing command:

```bash
jbang .github/scripts/MarkdownValidator.java --verbose .
```

Run from a representative clean checkout and capture the command, machine context when useful, elapsed time, and whether remote-link network conditions affected the result. This local baseline complements the CI baseline from issue #941 and becomes the before value for the implementation PR.

### Step 1: Behavior-Preserving Preparation

Create the Maven module and move the existing validator behavior into it without intentionally changing validation rules, command options, target directories, exit-code semantics, or reported validation failures.

The Maven module main class should become the supported JBang-compatible entry point. This removes the old checked-in wrapper while giving the implementation a proper module boundary.

Validation after Step 1:

- Run the new module tests or characterization tests for current parsing, remote-link filtering, default directory, missing directory, and exit-code behavior.
- Run the Maven build for the new module.
- Run the JBang entry point against a representative repository checkout.

### Step 2: Performance-Oriented Behavior Change

After the behavior-preserving extraction is validated, review the algorithm and introduce parallel execution where it is safe. Candidate areas include independent file validation and remote-link checks with shared caching. The implementation should consider Java structural concurrency where it improves cancellation, failure handling, and bounded execution.

The behavior change is the improved runtime profile, not a change to which Markdown errors are detected. Output ordering may be made deterministic if parallel execution would otherwise make reports difficult to compare.

Validation after Step 2:

- Run focused tests for parallel validation, shared remote-link caching, and deterministic result aggregation.
- Run the Maven build for the validator module.
- Run the repository Markdown validation command.
- Record the post-change local runtime using the same local timing approach used before implementation.
- Compare runtime against the current CI baseline recorded in issue #941 where practical.

## Design Constraints

- Preserve the current command-line contract unless a compatibility change is explicitly documented.
- Keep JBang support through `markdown-validator/src/main/java/info/jab/markdownvalidator/MarkdownValidator.java`.
- Keep CI behavior aligned with the existing `validate-markdown` job trigger.
- Use bounded parallelism and deterministic error aggregation.
- Preserve HTTP timeout behavior and interruption handling.
- Avoid broad root build changes beyond registering the new Maven module and wiring validation.

## Open Questions

- The final module directory and artifact id should be chosen during implementation. A descriptive name such as `markdown-validator` is expected unless a maintainer chooses a different naming convention.
- The target runtime improvement threshold is not specified beyond improving the reported 40-second baseline. Implementation should record before/after measurements when practical.
