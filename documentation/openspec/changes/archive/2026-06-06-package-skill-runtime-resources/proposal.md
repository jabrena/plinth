## Why

Issue [#801](https://github.com/jabrena/cursor-rules-java/issues/801) identifies five runtime resources that are currently expanded into generated reference Markdown instead of being packaged as standalone Agent Skills files. Packaging them in `scripts/` and `assets/` keeps references concise and lets agents inspect, copy, or execute the original resources directly.

## What Changes

- Package the JMeter script for skill `151-java-performance-jmeter` under `scripts/`.
- Package both profiling scripts for skill `161-java-profiling-detect` under `scripts/`.
- Package the CATS runner under `scripts/` and its Dockerfile under `assets/` for skill `703-technologies-fuzzing-testing`.
- Declare source and target resource paths in `skills.xml` so resource ownership remains inventory-driven.
- Replace embedded resource bodies in the three generated references with valid relative links.
- Verify exact paths, contents, executable script permissions, and resource isolation in generator tests.

## Capabilities

### New Capabilities

- `skill-runtime-resources`: Defines deterministic standalone resource packaging for the five files scoped by issue #801.

### Modified Capabilities

None.

## Impact

- Affects the `skills.xml` inventory, `skills-generator` Java output modeling, test-driven packaging, and three XML skill references.
- Adds no external dependencies and does not change public Java APIs outside the generator module.
- Existing generated references become smaller; consumers migrate from embedded code blocks to the linked files in the same skill package.
- Local generation continues to target `.agents/skills`; public `skills/` output remains release-profile only.
