# Developer commands

## Essential maven commands

```bash
# Analyze dependencies
./mvnw dependency:tree
./mvnw dependency:analyze
./mvnw dependency:resolve

./mvnw clean validate -U
./mvnw buildplan:list-plugin
./mvnw buildplan:list-phase
./mvnw help:all-profiles
./mvnw help:active-profiles
./mvnw license:third-party-report

# Clean the project
./mvnw clean

# Run unit tests
./mvnw clean test

# Run integration tests
./mvnw clean verify

# Clean and package in one command
./mvnw clean package

# Check for dependency updates
./mvnw versions:display-property-updates
./mvnw versions:display-dependency-updates
./mvnw versions:display-plugin-updates

# Generate project reports
./mvnw site
jwebserver -p 8005 -d "$(pwd)/target/site/"

# Run blog in local
./mvnw clean generate-resources jbake:inline -pl site-generator -P local-preview

# Promote blog changes to Prod
./mvnw clean generate-resources -pl site-generator -P site-update
```

## Skill generation workflows

The skill XML sources are generated into `skills-generator/target/skills` during the
`skills-generator` test lifecycle. Maven then exposes two different copy workflows:

```bash
# Local workflow: generate skills and copy them to .agents/skills
# This does not update the public skills/ release output.
./mvnw clean install -pl skills-generator

# Optional direct local copy after target/skills already exists
./mvnw resources:copy-resources@copy-skills -pl skills-generator

# Release workflow: clean and refresh the public skills/ directory intentionally
./mvnw clean install -pl skills-generator -P release
```

Use `.agents/skills` for local agent testing after XML edits. Use
`-P release` only when the generated skill changes are ready to become
part of the public release output under `skills/`. The destination directory can
be overridden for verification with `-Dskills.output.directory=...`.

## Submodules

This is a multi-module project. The following modules are declared in the root `pom.xml`.

| Module | Artifact ID | Commands | Description |
|--------|-------------|----------|-------------|
| skills-generator | cursor-rules-java-skills-generator | `./mvnw clean verify -pl skills-generator`<br>`./mvnw clean install -pl skills-generator`<br>`./mvnw clean install -pl skills-generator -P release` | Unified XML → skills generator: produces agent skills into `skills-generator/target/skills`, copies local output into `.agents/skills`, and refreshes `skills/` only through the explicit `release` profile. |
| site-generator | cursor-rules-java-site | `./mvnw clean verify -pl site-generator`<br>`./mvnw clean generate-resources jbake:inline -pl site-generator -P local-preview`<br>`./mvnw clean generate-resources -pl site-generator -P site-update` | JBake-based static site generator for documentation and GitHub Pages. |

## Maven Profiles

The following profiles are declared in this project. Activate them with `-P <profileId>`.

| Profile ID | Command | Activation | Description |
|------------|---------|------------|-------------|
| security | `./mvnw clean verify -P security` | manual | Runs OWASP dependency-check-maven to scan for known vulnerabilities; fails on CVSS ≥ 7. |
| find-bugs | `./mvnw clean verify -P find-bugs` | manual | Runs PMD and SpotBugs static analysis with max effort and low threshold. |
| release | `./mvnw clean install -pl skills-generator -P release` | manual | Cleans and refreshes the public `skills/` release output instead of copying generated skills to `.agents/skills`. |
| local-preview | `./mvnw clean generate-resources jbake:inline -pl site-generator -P local-preview` | manual | Generates site to `target/docs-local` and serves it locally (site-generator). |
| site-update | `./mvnw clean generate-resources -pl site-generator -P site-update` | manual | Regenerates the static site into `docs/` for GitHub Pages (site-generator). |

## Plugin Goals Reference

The following sections list useful goals for each plugin configured in this project's pom.xml.

### maven-compiler-plugin

| Goal | Description |
|------|-------------|
| `./mvnw compiler:compile` | Compile main source files |
| `./mvnw compiler:testCompile` | Compile test source files |

### maven-surefire-plugin

| Goal | Description |
|------|-------------|
| `./mvnw surefire:test` | Run unit tests |
| `./mvnw surefire:help` | Display help information |

### maven-resources-plugin

| Goal | Description |
|------|-------------|
| `./mvnw resources:resources` | Copy main resources to output directory |
| `./mvnw resources:testResources` | Copy test resources to output directory |
| `./mvnw resources:copy-resources@copy-skills -pl skills-generator` | Copy already generated skills from `skills-generator/target/skills` into `.agents/skills` for local agent use |

### maven-antrun-plugin

The `skills-generator` module uses this plugin inside the `release` profile to
clean `skills/` before the install-phase `copy-skills` execution refreshes the
public release output.

### maven-clean-plugin

| Goal | Description |
|------|-------------|
| `./mvnw clean:clean` | Delete the build output directory |

### jbake-maven-plugin

| Goal | Description |
|------|-------------|
| `./mvnw jbake:generate` | Generate static site with JBake |
| `./mvnw jbake:inline` | Generate and serve site locally |

### maven-enforcer-plugin

| Goal | Description |
|------|-------------|
| `./mvnw enforcer:enforce` | Execute enforcer rules |
| `./mvnw enforcer:display-info` | Display current platform information |

### spotless-maven-plugin

| Goal | Description |
|------|-------------|
| `./mvnw spotless:apply` | Apply formatting fixes |
| `./mvnw spotless:check` | Check formatting and fail on violations |

### versions-maven-plugin

| Goal | Description |
|------|-------------|
| `./mvnw versions:display-dependency-updates` | Show available dependency updates |
| `./mvnw versions:display-plugin-updates` | Show available plugin updates |
| `./mvnw versions:display-property-updates` | Show available property updates |
| `./mvnw versions:use-latest-releases` | Update dependencies to latest releases |
| `./mvnw versions:set -DnewVersion=X.Y.Z` | Set the project version |

### spotbugs-maven-plugin

| Goal | Description |
|------|-------------|
| `./mvnw spotbugs:check` | Run SpotBugs analysis and fail on bugs |
| `./mvnw spotbugs:spotbugs` | Generate a SpotBugs report |
| `./mvnw spotbugs:gui` | Launch the SpotBugs GUI |

### maven-pmd-plugin

| Goal | Description |
|------|-------------|
| `./mvnw pmd:check` | Run PMD analysis and fail on violations |
| `./mvnw pmd:pmd` | Generate a PMD report |
| `./mvnw pmd:cpd-check` | Run copy-paste detection and fail on duplicates |

### dependency-check-maven

| Goal | Description |
|------|-------------|
| `./mvnw dependency-check:check` | Scan dependencies for known vulnerabilities |
| `./mvnw dependency-check:aggregate` | Aggregate scan for multi-module projects |
