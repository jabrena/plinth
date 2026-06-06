## ADDED Requirements

### Requirement: Scoped runtime resource packaging

The skills generator SHALL package only the five runtime resources assigned to skills `151-java-performance-jmeter`, `161-java-profiling-detect`, and `703-technologies-fuzzing-testing`.

#### Scenario: Read ownership from the skill inventory

- **WHEN** the generator loads a skill descriptor from `skills.xml`
- **THEN** its resource source and target paths come from that skill's `resource-list`
- **AND** `SkillsGenerator` contains no skill-specific resource mapping

#### Scenario: Generate skill-owned resources

- **WHEN** the generator builds any of the three scoped skills
- **THEN** each resource is written at its specified `scripts/` or `assets/` path
- **AND** its content matches the source classpath resource

#### Scenario: Isolate resources by skill

- **WHEN** the generator builds scoped and unrelated skills
- **THEN** each scoped resource appears only in its owning skill
- **AND** unrelated skills receive no runtime resources from this change

### Requirement: Linked reference documentation

Generated references for the scoped skills SHALL link to standalone resources instead of embedding complete script or Dockerfile bodies.

#### Scenario: Resolve links from references

- **WHEN** a generated reference links to a packaged runtime resource
- **THEN** the link uses `../scripts/...` or `../assets/...`
- **AND** the target exists relative to the generated `references/` directory

#### Scenario: Preserve instructions

- **WHEN** embedded resource bodies are replaced with links
- **THEN** the reference retains instructions describing where and how to use each resource
- **AND** existing workflow safeguards remain present

### Requirement: Executable scripts

Generated files under `scripts/` MUST be executable when the build filesystem supports executable permissions.

#### Scenario: Generate shell scripts on a POSIX filesystem

- **WHEN** the target skill resources are written
- **THEN** every generated `.sh` file under `scripts/` has its executable bit set
