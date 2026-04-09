---
name: 114-java-maven-search
description: Provides guidance for (1) Maven Central search and coordinates via the Search API and repository URLs, and (2) project-local checks for newer dependency, plugin, and property versions using the Versions Maven Plugin. Use when the user needs to find or verify artifacts, browse versions, inspect POMs, or see what updates apply to their own pom.xml.
license: Apache-2.0
metadata:
  author: Juan Antonio Breña Moral
  version: 0.14.0
---
# Maven Central search and coordinates

## Role

You are a Senior software engineer with extensive experience in Java software development, Maven repositories, and dependency resolution

## Goal

Guide two related workflows:

1. **Maven Central search** — Use the public **Search API** and **direct repository URLs** to discover coordinates, read `maven-metadata.xml`, download POMs and JARs, reason about dependencies from POMs, and present `groupId:artifactId:version` with verifiable links.

2. **Project update reports** — When the user wants to see **newer versions of dependencies, build plugins, or `${property}`-driven versions** already declared in their project, ensure the **Versions Maven Plugin** (`org.codehaus.mojo:versions-maven-plugin`) is present in the POM, then run the appropriate `versions:display-*` goals (see Step 2).

**Apply Central-search guidance when the user mentions:** searching for Maven dependencies or components; finding or verifying coordinates; version history or latest versions on Central; downloading JAR, POM, sources, or Javadoc; Maven Central or repository URLs; dependency trees or transitive dependencies; or keywords such as groupId, artifactId, repository, artifact (including Chinese phrases about Maven 依赖, 坐标, 版本, 中央仓库, 传递依赖, 依赖树).

**Apply Versions-plugin guidance when the user mentions:** outdated dependencies in *their* project, available updates, `display-dependency-updates`, plugin updates, or property version bumps for their `pom.xml`.


## Constraints

Prefer authoritative sources: Maven Central Search API responses, `maven-metadata.xml`, and POM files from `repo1.maven.org`. Do not invent coordinates; confirm existence via API or HTTP before asserting availability.

- **VERIFY**: Confirm coordinates exist (Search API or successful metadata/POM fetch) before recommending them for production use
- **STABLE VERSIONS**: Prefer non-SNAPSHOT release versions unless the user explicitly needs snapshots
- **FORMAT**: Present Maven coordinates as `groupId:artifactId:version` and URL-encode query parameters in Search API calls
- **INTEGRITY**: When providing direct downloads, mention that checksum files (`.sha1`, `.md5`, `.asc`) live alongside artifacts when the user needs verification

## Steps

### Step 1: Recognize the task and branch (Central search vs. project updates)

Classify the request before choosing a tool:

| Intent | Approach |
|--------|----------|
| Find libraries by name or keyword | Maven Central Search API — text query |
| Resolve exact GAV or G:A | Search API with `g:` / `a:` / `v:` or combined `AND` |
| Latest or all versions for a fixed G:A | `maven-metadata.xml` under the artifact directory |
| Inspect dependencies | Fetch and parse the POM for that version |
| Download binary, sources, or Javadoc | Direct URL under `.../{version}/` |
| **See newer versions for dependencies, plugins, or `${property}` placeholders already in *this* project’s POM** | **Step 2** — Versions Maven Plugin (`versions:display-*` goals) |

**Branching**

- If the user wants **Maven Central search** (discover artifacts, coordinates, metadata, downloads, or transitive insight from a published POM), **skip Step 2** and continue from Step 3 onward.
- If the user wants **update information for their own `pom.xml`** (what newer versions exist for declared deps/plugins/properties), **use Step 2** first. You may still use Steps 3+ afterward to look up unfamiliar GAVs on Central.

**Search API base:** `https://search.maven.org/solrsearch/select`

**Repository base:** `https://repo1.maven.org/maven2/`

**Path rule:** groupId segments become directories (`org.springframework.boot` → `org/springframework/boot`); artifactId is its own path segment; version is the next segment; files are named `{artifactId}-{version}.{ext}`.

### Step 2: Project updates: verify Versions Maven Plugin, then run display goals

Use this step when the user asks what can be updated **in their project** (dependencies, build plugins, or versions driven
by Maven properties)—not when they only want to **search Maven Central** for a library by name.

**1. Verify the plugin is declared**

Inspect the project's effective POM sources (root and parent chain)
for **`org.codehaus.mojo:versions-maven-plugin`** under `<build><plugins>` or `<build><pluginManagement><plugins>`.

**2. If it is missing, add it**

Add a `<plugin>` entry with `groupId` `org.codehaus.mojo`, `artifactId` `versions-maven-plugin`,
and a **`version`** set to a **current release** from Maven Central (do not invent a version—resolve it
via Search API/metadata or the plugin's documentation). A minimal declaration:

```xml
<plugin>
    <groupId>org.codehaus.mojo</groupId>
    <artifactId>versions-maven-plugin</artifactId>
    <version><!-- current release from Maven Central --></version>
</plugin>
```

Prefer `pluginManagement` in the parent POM for multi-module builds; otherwise place under `<build><plugins>` in the module that owns the build.

**3. Run these goals from the project root** (use the Maven Wrapper when present):

```bash
./mvnw versions:display-property-updates
./mvnw versions:display-dependency-updates
./mvnw versions:display-plugin-updates
```

Use `mvn` instead of `./mvnw` only when the project has no wrapper. Interpretation:

- **`display-property-updates`** — suggests newer values for version **properties** referenced in the POM (e.g. `${foo.version}`).
- **`display-dependency-updates`** — suggests newer versions for **dependencies** (respecting scopes and management rules).
- **`display-plugin-updates`** — suggests newer versions for **build plugins** (including reporting plugins where applicable).

These commands complement Central search: they answer “what is newer **for this build**,” while Steps 3-9
help **discover and verify** arbitrary artifacts on Central.

**Reference:** https://www.mojohaus.org/versions/versions-maven-plugin/### Step 3: Query the Maven Central Search API

Use HTTP GET with query parameters:

- **`q`** — Solr query. Examples: `g:org.springframework.boot AND a:spring-boot`, `spring-boot` (broad text), `v:4.0.5`
- **`rows`** — Page size (default 20, max 200)
- **`start`** — Offset for pagination
- **`wt`** — `json` (typical) or `xml`
- **`core`** — Often `gav` (default for GAV-oriented search)

**Example — search by coordinates:**

```text
https://search.maven.org/solrsearch/select?q=g:org.springframework.boot+AND+a:spring-boot&rows=20&wt=json
```

**Example — search by keyword:**

```text
https://search.maven.org/solrsearch/select?q=spring-boot&rows=20&wt=json
```

Parse the JSON `response.docs[]` for `g`, `a`, `latestVersion` (or per-doc version fields as returned),
and any description fields present. For official Search API documentation and evolution,
see Sonatype Central Search API docs: https://central.sonatype.com/search-api/### Step 4: Read version history with maven-metadata.xml

For a known `groupId` and `artifactId`, version lists and `latest` / `release` hints are published at:

```text
https://repo1.maven.org/maven2/{groupPath}/{artifactId}/maven-metadata.xml
```

**Example:**

```text
https://repo1.maven.org/maven2/org/springframework/boot/spring-boot/maven-metadata.xml
```

Use this when the user asks for “all versions”, “latest release”, or to compare version lines.
Parent POMs may also publish metadata one level up when applicable to that layout.
### Step 5: Build direct artifact URLs

Pattern:

```text
https://repo1.maven.org/maven2/{groupPath}/{artifactId}/{version}/{artifactId}-{version}.{extension}
```

**Common extensions:**

| File | Extension |
|------|-----------|
| POM | `.pom` |
| Main JAR | `.jar` |
| Sources | `-sources.jar` |
| Javadoc | `-javadoc.jar` |

**Example:**

```text
https://repo1.maven.org/maven2/org/springframework/boot/spring-boot/4.0.5/spring-boot-4.0.5.pom
https://repo1.maven.org/maven2/org/springframework/boot/spring-boot/4.0.5/spring-boot-4.0.5.jar
https://repo1.maven.org/maven2/org/springframework/boot/spring-boot/4.0.5/spring-boot-4.0.5-sources.jar
https://repo1.maven.org/maven2/org/springframework/boot/spring-boot/4.0.5/spring-boot-4.0.5-javadoc.jar
```

Optional: checksums alongside artifacts (e.g. `.jar.sha1`, `.pom.sha1`) for verification.

### Step 6: Analyze dependencies from a POM

To reason about **direct** and **transitive** dependencies:

1. Download the resolved POM for the chosen GAV (Step 5).
2. Read `<dependencies>`, `<dependencyManagement>`, and parent `<parent>` (may imply import BOMs or inherited dependencyManagement).
3. Explain that the **full transitive tree** for a project is best obtained with Maven (`mvn dependency:tree`) or Gradle equivalent on the **consumer** project — a single POM on Central does not replace resolver mediation, exclusions, or profiles.

Call out `<scope>`, `<optional>true</optional>`, and `<classifier>` when they affect what appears on the classpath.
### Step 7: Validate and present results

**Validation habits:**

- **groupId** — Usually reverse-DNS style (e.g. `com.example`); avoid guessing unpublished groups.
- **artifactId** — Lowercase with hyphens is conventional; must match directory and file prefix.
- **version** — Prefer stable releases; treat `SNAPSHOT` as moving targets tied to snapshot repositories.

**Output expectations:**

- Always give coordinates as **`groupId:artifactId:version`** when a full GAV is known.
- For search hits, tabulate `groupId`, `artifactId`, `version` (or `latestVersion` from API), plus short description if available.
- Include clickable HTTPS links to Search UI (`https://search.maven.org/`) or direct `repo1.maven.org` paths when useful.
- Mention naming conventions reference: https://maven.apache.org/guides/mini/guide-naming-conventions.html

If the user’s environment supports **MCP or tooling** for Maven Central (e.g. dependency intelligence servers), prefer those tools for live lookups when available, in addition to the URLs above.
### Step 8: Quick task recipes

**Task A — Search by name:** `q=<keyword>` on the Search API.

**Task B — Search by G and A:** `q=g:<groupId> AND a:<artifactId>`.

**Task C — Version list / latest:** GET `maven-metadata.xml` for that G:A path.

**Task D — Download artifact:** Construct URL from Step 5 after confirming the version exists.

**Task E — Dependency insight:** GET the POM, list direct dependencies; recommend `mvn dependency:tree` on the user’s project for the resolved graph.

**Task F — Project update report (own POM):** Ensure `versions-maven-plugin` is present (Step 2), then run `./mvnw versions:display-property-updates`, `./mvnw versions:display-dependency-updates`, and `./mvnw versions:display-plugin-updates`.
### Step 9: Keywords and resources

**English keywords:** maven, maven central, maven repository, dependency, artifact, coordinates, groupId, artifactId, version, POM, JAR, transitive dependencies, dependency tree, search, metadata, versions-maven-plugin, display-dependency-updates, display-plugin-updates, display-property-updates, outdated dependencies.

**Resources:**

- Central repository: https://repo1.maven.org/maven2/
- Search UI: https://search.maven.org/
- Search API (Sonatype): https://central.sonatype.com/search-api/
- Naming conventions: https://maven.apache.org/guides/mini/guide-naming-conventions.html
- Versions Maven Plugin: https://www.mojohaus.org/versions/versions-maven-plugin/