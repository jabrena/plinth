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
```

## Plugin Goals Reference

The following sections list useful goals for each plugin configured in this project's pom.xml.

### maven-enforcer-plugin


| Goal                           | Description                          |
| ------------------------------ | ------------------------------------ |
| `./mvnw enforcer:enforce`      | Execute enforcer rules               |
| `./mvnw enforcer:display-info` | Display current platform information |


### spotless-maven-plugin


| Goal                    | Description                             |
| ----------------------- | --------------------------------------- |
| `./mvnw spotless:apply` | Apply formatting fixes                  |
| `./mvnw spotless:check` | Check formatting and fail on violations |


### versions-maven-plugin


| Goal                                         | Description                            |
| -------------------------------------------- | -------------------------------------- |
| `./mvnw versions:display-dependency-updates` | Show available dependency updates      |
| `./mvnw versions:display-plugin-updates`     | Show available plugin updates          |
| `./mvnw versions:display-property-updates`   | Show available property updates        |
| `./mvnw versions:use-latest-releases`        | Update dependencies to latest releases |
| `./mvnw versions:set -DnewVersion=X.Y.Z`     | Set the project version                |


### spotbugs-maven-plugin


| Goal                       | Description                            |
| -------------------------- | -------------------------------------- |
| `./mvnw spotbugs:check`    | Run SpotBugs analysis and fail on bugs |
| `./mvnw spotbugs:spotbugs` | Generate a SpotBugs report             |
| `./mvnw spotbugs:gui`      | Launch the SpotBugs GUI                |


### maven-pmd-plugin


| Goal                   | Description                                     |
| ---------------------- | ----------------------------------------------- |
| `./mvnw pmd:check`     | Run PMD analysis and fail on violations         |
| `./mvnw pmd:pmd`       | Generate a PMD report                           |
| `./mvnw pmd:cpd-check` | Run copy-paste detection and fail on duplicates |


### dependency-check-maven


| Goal                                  | Description                                              |
| ------------------------------------- | -------------------------------------------------------- |
| `./mvnw dependency-check:check`       | Run OWASP dependency check and fail on CVSS threshold    |
| `./mvnw dependency-check:aggregate`   | Aggregate vulnerability report for multi-module projects |
| `./mvnw dependency-check:update-only` | Update the local NVD database only                       |
| `./mvnw dependency-check:purge`       | Delete the local NVD database cache                      |


### maven-compiler-plugin


| Goal                          | Description               |
| ----------------------------- | ------------------------- |
| `./mvnw compiler:compile`     | Compile main source files |
| `./mvnw compiler:testCompile` | Compile test source files |


### maven-surefire-plugin


| Goal                   | Description              |
| ---------------------- | ------------------------ |
| `./mvnw surefire:test` | Run unit tests           |
| `./mvnw surefire:help` | Display help information |


### maven-resources-plugin


| Goal                             | Description                             |
| -------------------------------- | --------------------------------------- |
| `./mvnw resources:resources`     | Copy main resources to output directory |
| `./mvnw resources:testResources` | Copy test resources to output directory |


### maven-clean-plugin


| Goal                 | Description                       |
| -------------------- | --------------------------------- |
| `./mvnw clean:clean` | Delete the build output directory |


### jbake-maven-plugin


| Goal                    | Description                                           |
| ----------------------- | ----------------------------------------------------- |
| `./mvnw jbake:generate` | Generate the static site with JBake                   |
| `./mvnw jbake:inline`   | Generate and serve the site with a built-in server    |
| `./mvnw jbake:watch`    | Watch for source changes and regenerate automatically |


## Maven Profiles

The following profiles are declared in this project. Activate them with `-P <profileId>`.


| Profile ID      | Command                                                                 | Activation | Description                                                                                                              |
| ---------------- | ----------------------------------------------------------------------- | ---------- | ------------------------------------------------------------------------------------------------------------------------ |
| `security`       | `./mvnw clean verify -P security`                                       | manual     | Runs OWASP Dependency-Check against all scopes and fails the build on any vulnerability with a CVSS score ≥ 7.           |
| `find-bugs`   | `./mvnw clean verify -P find-bugs`                                  | manual     | Runs SpotBugs (Max effort, Low threshold) and PMD static analysis, failing the build on any detected issue.              |
| `site-update` | `./mvnw clean generate-resources -pl site-generator -P site-update` | manual     | Cleans the `docs/` folder, generates the GitHub Pages static site with JBake, and copies conference assets into `docs/`. |
| `local-preview`  | `./mvnw clean generate-resources jbake:inline -pl site-generator -P local-preview` | manual     | Generates the site to `target/docs-local`, copies images, and serves at http://localhost:8820/ for local preview.       |


## Submodules

This is a multi-module project. The following modules are declared in the root `pom.xml`.


| Module                     | Artifact ID                          | Commands                                                                                                     | Description                                                                                                          |
| -------------------------- | ------------------------------------ | ------------------------------------------------------------------------------------------------------------ | -------------------------------------------------------------------------------------------------------------------- |
| `system-prompts-generator` | `cursor-rules-java-generator`        | `./mvnw clean verify -pl system-prompts-generator` `./mvnw clean install -pl system-prompts-generator`       | Transforms XML rule sources via XInclude and XSLT into Markdown cursor rules, then deploys them to `.cursor/rules/`. |
| `skills-generator`         | `cursor-rules-java-skills-generator` | `./mvnw clean verify -pl skills-generator` `./mvnw clean install -pl skills-generator`                       | Generates agent skills from the cursor rules and deploys them to `skills/`.                                          |
| `site-generator`           | `cursor-rules-java-site`             | `./mvnw clean verify -pl site-generator`                                                                       | Generates the project's GitHub Pages static website using JBake. Use the `site-update` profile to publish to `docs/`. |


