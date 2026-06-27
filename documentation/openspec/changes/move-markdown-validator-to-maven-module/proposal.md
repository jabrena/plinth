## Why

GitHub issue [#941](https://github.com/jabrena/cursor-rules-java/issues/941) requests moving the Markdown validator from `.github/scripts/MarkdownValidator.java` into a dedicated Maven module.

The current validator is a CI-oriented JBang script that has grown beyond a small script. It validates generated and maintained Markdown content, checks remote links, and is invoked by the `validate-markdown` GitHub Actions job. The issue records a CI validation run taking around 40 seconds, which is too slow for this feedback loop.

## What Changes

- Add a dedicated Maven module for the Markdown validator.
- Move the validator implementation out of `.github/scripts/MarkdownValidator.java` while preserving the existing command-line behavior needed by contributors and CI.
- Keep JBang execution supported by making the Maven module main class directly runnable with `jbang`.
- Review the current algorithm and apply safe parallel execution where it improves validation time without making results nondeterministic.
- Consider Java structural concurrency for file and remote-link validation work.
- Update the GitHub Actions Markdown validation step to use the supported entry point.

## Capabilities

### New Capabilities

- `markdown-validator-module`: Adds a Maven-owned Markdown validator with CI and JBang compatibility.

### Modified Capabilities

None.

## Source and Derivation

- Source artifact: GitHub issue [#941](https://github.com/jabrena/cursor-rules-java/issues/941).
- Previous implementation: `.github/scripts/MarkdownValidator.java`.
- New implementation: `markdown-validator/src/main/java/info/jab/markdownvalidator/MarkdownValidator.java`.
- Current CI call site: `.github/workflows/maven.yaml`.
- Current Maven module registry: root `pom.xml`.
- Derivation direction: issue #941 -> OpenSpec change artifacts -> Maven module implementation -> CI/JBang validation.

## Change Boundary Assessment

This is one OpenSpec change because the requested outcome is atomic: move the Markdown validator into a Maven module while keeping the validator usable and faster. The preparation, behavior change, CI update, and validation are part of the same reviewable delivery boundary.

The change does not include unrelated documentation rewrites, generated site updates, generated cursor rule updates, or public `skills/` release output.

## Impact

The root Maven build, a new validator module, the existing Markdown validator entry point, and the Markdown validation workflow are affected. The implementation should avoid direct edits to generated `docs/`, generated `.cursor/rules/`, and public `skills/` output unless a separate source change explicitly requires regeneration.
