# Tasks

## 1. Implementation Checklist

- [x] 1.1 Review issue #941 and capture the requested user story, acceptance criteria, and implementation notes.
- [x] 1.2 Inspect the current Markdown validator script, CI call site, and root Maven module registry.
- [x] 1.3 Create OpenSpec proposal, design, requirements, and task artifacts with source traceability.
- [ ] 1.4 Add a dedicated Maven module for the Markdown validator and register it in the root `pom.xml`.
- [ ] 1.5 Move the current validator implementation into the Maven module without changing validation behavior.
- [ ] 1.6 Add characterization or unit tests covering current command options, default target directories, Markdown parsing/rendering, remote-link filtering, fail-fast behavior, and exit-code semantics.
- [ ] 1.7 Keep `.github/scripts/MarkdownValidator.java` as a thin JBang-compatible entry point that calls the module main class using `//SOURCES` as needed.
- [ ] 1.8 Run the new module verification after extraction to prove behavior is preserved.
- [ ] 1.9 Review the validator algorithm and identify safe parallelization boundaries.
- [ ] 1.10 Implement bounded parallel validation where it improves runtime while keeping error aggregation deterministic.
- [ ] 1.11 Consider Java structural concurrency for coordinated file and remote-link validation work.
- [ ] 1.12 Add or update tests for parallel execution, shared remote-link caching, deterministic output, timeout handling, and fail-fast behavior.
- [ ] 1.13 Update `.github/workflows/maven.yaml` so CI uses the supported validator entry point.
- [ ] 1.14 Run the repository Markdown validation command and record whether it improves on the issue #941 baseline where practical.
- [ ] 1.15 Run the relevant Maven verification for the new module and any affected aggregator build.
- [ ] 1.16 Run `openspec validate --all`.
