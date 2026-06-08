## Context

Issue #808 closes an operation lifecycle gap by introducing profiling and performance-testing workflows for post-implementation performance work. The project already includes JMeter, Gatling, Maven/JMH guidance, and profiling lifecycle skills. The missing layer is a small command and agent contract that composes those skills, records reproducible evidence, and prevents unsupported performance claims.

## Goals / Non-Goals

**Goals:**

- Provide `/profile` and `/benchmark` command contracts with clear inputs, owner, workflow, outputs, and safeguards.
- Add `@robot-java-performance` as a coordinator for profiling and performance-testing workflows.
- Keep `151`, `152`, and `161`-`164` authoritative for substantive behavior.
- Delegate application-code optimizations to existing Java, Spring Boot, Quarkus, or Micronaut coder agents.
- Preserve traceability from baseline metadata through evidence, recommendation, delegation, repeated measurement, and final outcome.
- Document JMeter, Gatling, and JMH boundaries and reproducibility expectations.

**Non-Goals:**

- Let the performance engineer directly implement application-code optimizations.
- Claim performance improvement from non-equivalent or non-reproducible runs.
- Replace general implementation verification with performance comparison.
- Edit generated `skills/`, `.cursor/rules/`, or `docs/` directly.

## Decisions

### Use one operation coordinator

`@robot-java-performance` owns performance workflow coordination, not application implementation. It establishes the baseline, selects profiling or benchmark skills, ranks findings, asks for user approval before optimization, delegates code work to the appropriate coder agent, and verifies the result with equivalent measurement.

### Keep command assets concise

`/profile` and `/benchmark` should state contracts, accepted inputs, workflow shape, outputs, and safeguards. They must not duplicate the full JMeter, Gatling, JMH, or profiling skill instructions because the existing skills remain the behavioral authority.

### Select tools by boundary

Use JMeter or Gatling for HTTP/API load and performance tests, prefer Gatling when scenario modeling and reports are central, and use JMH for isolated JVM method or component microbenchmarks. The selected workflow must record rationale, environment, workload, warm-up, duration, concurrency, thresholds, and result artifacts.

### Require equivalent comparison

Before/after performance verification requires equivalent workload, runtime command, environment assumptions, measurement method, and threshold context. Non-equivalent runs may be reported as observations but not as validated improvement.

### Split implementation only when useful

The source work can be parallelized by ownership: commands, agent, skill alignment, and documentation/integration. If split into branches or child changes, command, agent, and skill-alignment work can proceed before documentation, while documentation waits for final names and contracts.

## Risks / Trade-offs

- [Risk] Command assets duplicate skill content and drift. -> Keep commands thin and reference authoritative skills.
- [Risk] Benchmark output is misread as optimization proof. -> Require equivalence checks and explicit limitations before reporting improvement.
- [Risk] Profiling recommendations lead to unapproved code changes. -> Require user approval before optimization delegation.
- [Risk] Documentation churn begins before source behavior stabilizes. -> Gate README and localized guide updates until command and agent contracts are final.

## Migration Plan

1. Add command and agent source assets under `skills-generator/src/main/resources/`.
2. Align skill inputs/outputs only where required by the command and agent contracts.
3. Update installer, inventory, focused tests, README, and localized guides.
4. Regenerate local skills for validation without refreshing public `skills/` unless preparing an intentional release.
5. Run XML, Maven, Markdown, skill-check, scanner, and OpenSpec validation appropriate to the final changed files.

## Open Questions

- Confirm whether JMH guidance remains exposed only through Maven skills or needs an explicit command-level reference in the inventory.
- Confirm whether implementation should remain one branch or split into child branches using the ownership boundaries above.
