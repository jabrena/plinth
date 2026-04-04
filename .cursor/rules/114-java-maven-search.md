---
name: 114-java-maven-search
description: Provides comprehensive guidance for searching and retrieving Maven components from Maven Central Repository (https://repo1.maven.org/maven2/). Use when the user needs to find, verify, or retrieve Maven dependencies, check component versions, analyze dependency trees, or work with Maven coordinates (groupId, artifactId, version), POMs, JARs, sources, or Javadoc.
license: Apache-2.0
metadata:
  author: Juan Antonio Breña Moral
  version: 0.14.0-SNAPSHOT
---
# Maven Central search and coordinates

## Role

You are a Senior software engineer with extensive experience in Java software development, Maven repositories, and dependency resolution

## Goal

Guide search and retrieval of artifacts from **Maven Central** using the public **Search API** and **direct repository URLs**. Help the user discover coordinates, inspect version history via `maven-metadata.xml`, download POMs and JARs, reason about transitive dependencies from POMs, and present results as `groupId:artifactId:version` with verifiable links.

**Apply this guidance when the user mentions:** searching for Maven dependencies or components; finding or verifying coordinates; version history or latest versions; downloading JAR, POM, sources, or Javadoc; Maven Central or repository URLs; dependency trees or transitive dependencies; or keywords such as groupId, artifactId, repository, artifact (including Chinese phrases about Maven 依赖, 坐标, 版本, 中央仓库, 传递依赖, 依赖树).


## Constraints

Prefer authoritative sources: Maven Central Search API responses, `maven-metadata.xml`, and POM files from `repo1.maven.org`. Do not invent coordinates; confirm existence via API or HTTP before asserting availability.

- **VERIFY**: Confirm coordinates exist (Search API or successful metadata/POM fetch) before recommending them for production use
- **STABLE VERSIONS**: Prefer non-SNAPSHOT release versions unless the user explicitly needs snapshots
- **FORMAT**: Present Maven coordinates as `groupId:artifactId:version` and URL-encode query parameters in Search API calls
- **INTEGRITY**: When providing direct downloads, mention that checksum files (`.sha1`, `.md5`, `.asc`) live alongside artifacts when the user needs verification

## Steps

### Step 1: Recognize the task and search pattern

Classify the request before choosing a tool:

| Intent | Approach |
|--------|----------|
| Find libraries by name or keyword | Maven Central Search API — text query |
| Resolve exact GAV or G:A | Search API with `g:` / `a:` / `v:` or combined `AND` |
| Latest or all versions for a fixed G:A | `maven-metadata.xml` under the artifact directory |
| Inspect dependencies | Fetch and parse the POM for that version |
| Download binary, sources, or Javadoc | Direct URL under `.../{version}/` |

**Search API base:** `https://search.maven.org/solrsearch/select`

**Repository base:** `https://repo1.maven.org/maven2/`

**Path rule:** groupId segments become directories (`com.google.guava` → `com/google/guava`); artifactId is its own path segment; version is the next segment; files are named `{artifactId}-{version}.{ext}`.### Step 2: Query the Maven Central Search API

Use HTTP GET with query parameters:

- **`q`** — Solr query. Examples: `g:com.google.guava AND a:guava`, `guava` (broad text), `v:33.0.0`
- **`rows`** — Page size (default 20, max 200)
- **`start`** — Offset for pagination
- **`wt`** — `json` (typical) or `xml`
- **`core`** — Often `gav` (default for GAV-oriented search)

**Example — search by coordinates:**

```text
https://search.maven.org/solrsearch/select?q=g:com.google.guava+AND+a:guava&rows=20&wt=json
```

**Example — search by keyword:**

```text
https://search.maven.org/solrsearch/select?q=guava&rows=20&wt=json
```

Parse the JSON `response.docs[]` for `g`, `a`, `latestVersion` (or per-doc version fields as returned), and any description fields present. For official Search API documentation and evolution, see Sonatype Central Search API docs: https://central.sonatype.com/search-api/### Step 3: Read version history with maven-metadata.xml

For a known `groupId` and `artifactId`, version lists and `latest` / `release` hints are published at:

```text
https://repo1.maven.org/maven2/{groupPath}/{artifactId}/maven-metadata.xml
```

**Example:**

```text
https://repo1.maven.org/maven2/com/google/guava/guava/maven-metadata.xml
```

Use this when the user asks for “all versions”, “latest release”, or to compare version lines. Parent POMs may also publish metadata one level up when applicable to that layout.### Step 4: Build direct artifact URLs

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
https://repo1.maven.org/maven2/com/google/guava/guava/33.0.0/guava-33.0.0.pom
https://repo1.maven.org/maven2/com/google/guava/guava/33.0.0/guava-33.0.0.jar
https://repo1.maven.org/maven2/com/google/guava/guava/33.0.0/guava-33.0.0-sources.jar
https://repo1.maven.org/maven2/com/google/guava/guava/33.0.0/guava-33.0.0-javadoc.jar
```

Optional: checksums alongside artifacts (e.g. `.jar.sha1`, `.pom.sha1`) for verification.### Step 5: Analyze dependencies from a POM

To reason about **direct** and **transitive** dependencies:

1. Download the resolved POM for the chosen GAV (Step 4).
2. Read `<dependencies>`, `<dependencyManagement>`, and parent `<parent>` (may imply import BOMs or inherited dependencyManagement).
3. Explain that the **full transitive tree** for a project is best obtained with Maven (`mvn dependency:tree`) or Gradle equivalent on the **consumer** project — a single POM on Central does not replace resolver mediation, exclusions, or profiles.

Call out `<scope>`, `<optional>true</optional>`, and `<classifier>` when they affect what appears on the classpath.### Step 6: Validate and present results

**Validation habits:**

- **groupId** — Usually reverse-DNS style (e.g. `com.example`); avoid guessing unpublished groups.
- **artifactId** — Lowercase with hyphens is conventional; must match directory and file prefix.
- **version** — Prefer stable releases; treat `SNAPSHOT` as moving targets tied to snapshot repositories.

**Output expectations:**

- Always give coordinates as **`groupId:artifactId:version`** when a full GAV is known.
- For search hits, tabulate `groupId`, `artifactId`, `version` (or `latestVersion` from API), plus short description if available.
- Include clickable HTTPS links to Search UI (`https://search.maven.org/`) or direct `repo1.maven.org` paths when useful.
- Mention naming conventions reference: https://maven.apache.org/guides/mini/guide-naming-conventions.html

If the user’s environment supports **MCP or tooling** for Maven Central (e.g. dependency intelligence servers), prefer those tools for live lookups when available, in addition to the URLs above.### Step 7: Quick task recipes

**Task A — Search by name:** `q=<keyword>` on the Search API.

**Task B — Search by G and A:** `q=g:<groupId> AND a:<artifactId>`.

**Task C — Version list / latest:** GET `maven-metadata.xml` for that G:A path.

**Task D — Download artifact:** Construct URL from Step 4 after confirming the version exists.

**Task E — Dependency insight:** GET the POM, list direct dependencies; recommend `mvn dependency:tree` on the user’s project for the resolved graph.### Step 8: Keywords and resources

**English keywords:** maven, maven central, maven repository, dependency, artifact, coordinates, groupId, artifactId, version, POM, JAR, transitive dependencies, dependency tree, search, metadata.

**Resources:**

- Central repository: https://repo1.maven.org/maven2/
- Search UI: https://search.maven.org/
- Search API (Sonatype): https://central.sonatype.com/search-api/
- Naming conventions: https://maven.apache.org/guides/mini/guide-naming-conventions.html