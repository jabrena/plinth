---
name: 703-technologies-fuzzing-testing
description: Use when you need to add or review fuzz testing for Java APIs with CATS — including contract-driven negative testing, malformed payload validation, boundary input exploration, CI integration, reproducible failures, and local execution guidance.
license: Apache-2.0
metadata:
  author: Juan Antonio Breña Moral
  version: 0.15.0
---
# Java fuzz testing with CATS

## Role

You are a Senior software engineer with extensive experience in API testing, contract validation, and CI quality gates

## Goal

Help developers design and implement CATS-based fuzz testing for Java APIs.

1. Define contract-driven fuzzing scope from the available OpenAPI specification and API contracts.
2. Prioritize negative and boundary scenarios (invalid types, missing required fields, malformed formats, and range violations).
3. Integrate CATS execution into CI and provide reproducible failure evidence.
4. Document local execution so contributors can run the same baseline checks before creating pull requests.
5. Provide a containerized local CATS installation option using a `cats/Dockerfile` and a `run-cats-fuzz.sh` runner script.
6. Review CATS reports with the user to surface contract violations, validation gaps, and reproducible failure evidence.

## Constraints

Before applying recommendations, ensure the project compiles and that the API contract source is available. Compilation failure is a blocking condition.

- **MANDATORY**: Run `./mvnw compile` or `mvn compile` before applying any change
- **PRECONDITION**: API contract input (OpenAPI or equivalent) must be available before configuring CATS
- **CRITICAL SAFETY**: If compilation fails, stop immediately and do not proceed
- **MANDATORY**: Regenerate skills after XML edits using `./mvnw clean install -pl skills-generator`
- **LOCAL INSTALLATION**: Prefer a `cats/Dockerfile` when contributors need repeatable local CATS execution without installing Java tooling or CATS directly on the host
- **VERIFY**: Run `./mvnw clean verify` or `mvn clean verify` after integrating fuzz testing changes

## Steps

### Step 1: Create CATS Dockerfile

Create `cats/Dockerfile` with the following content:

```dockerfile
# CATS OpenAPI fuzzer (https://github.com/Endava/cats) — uberjar on Temurin JRE.
ARG CATS_VERSION=13.8.0
FROM eclipse-temurin:25-jre-jammy

ARG CATS_VERSION
RUN mkdir -p /opt/cats \
&& apt-get update \
&& apt-get install -y --no-install-recommends curl ca-certificates \
&& rm -rf /var/lib/apt/lists/* \
&& curl -fsSL "https://github.com/Endava/cats/releases/download/cats-${CATS_VERSION}/cats_uberjar_${CATS_VERSION}.tar.gz" \
| tar -xz -C /opt/cats

WORKDIR /workspace
ENTRYPOINT ["java", "-jar", "/opt/cats/cats.jar"]

```

#### Step Constraints

- **MUST** copy the embedded template verbatim into `cats/Dockerfile`
- **DO NOT** modify the template unless the CATS version or base image must change

### Step 2: Create CATS runner script

Create `run-cats-fuzz.sh` in the project root and make it executable (`chmod +x run-cats-fuzz.sh`).

Copy the following script verbatim:

```bash
#!/usr/bin/env bash
# Contract-driven API fuzzing with CATS (https://endava.github.io/cats/).
# Runs CATS in Docker (see cats/Dockerfile). API must be reachable from the container.
set -euo pipefail

REPO_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CATS_DIR="${REPO_ROOT}/cats"

CONTRACT=""
SERVER="${SERVER:-http://localhost:8080}"
HEALTH_URL="${HEALTH_URL:-${SERVER}/actuator/health}"
CATS_VERSION="${CATS_VERSION:-13.8.0}"
CATS_IMAGE="${CATS_IMAGE:-local/cats:${CATS_VERSION}}"
CATS_REBUILD="${CATS_REBUILD:-0}"
CATS_REPORT_FORMAT="${CATS_REPORT_FORMAT:-HTML_JS}"
REPORT_DIR="${REPORT_DIR:-${CATS_DIR}/cats-report}"
WAIT_TIMEOUT_SEC="${WAIT_TIMEOUT_SEC:-60}"
CATS_COMPOSE_SERVICE="${CATS_COMPOSE_SERVICE:-}"
CATS_COMPOSE_NETWORK="${CATS_COMPOSE_NETWORK:-}"
OPENAPI_MODE=0
CATS_DOCKER_NETWORK=""
SERVER_DOCKER=""

usage() {
  cat <<'EOF'
Usage: run-cats-fuzz.sh --openapi <openapi-file>

Runs CATS OpenAPI fuzzers in Docker (cats/Dockerfile) against the given contract.
The API must already be listening (for example via docker compose up or ./mvnw spring-boot:run).

Arguments:
  <openapi-file>        Path to the OpenAPI specification (required with --openapi)

Environment variables:
  SERVER                API base URL (default: http://localhost:8080)
  HEALTH_URL            Readiness probe URL on the host (default: SERVER/actuator/health)
  CATS_VERSION          CATS release version (default: 13.8.0)
  CATS_IMAGE            Docker image tag (default: local/cats:CATS_VERSION)
  CATS_REBUILD          Set to 1 to force docker build (default: 0)
  CATS_REPORT_FORMAT    HTML_JS | HTML_ONLY | JUNIT (default: HTML_JS)
  REPORT_DIR            Output directory for reports (default: cats/cats-report)
  WAIT_TIMEOUT_SEC      Seconds to wait for HEALTH_URL (default: 60)
  CATS_COMPOSE_SERVICE  Compose service name for in-network fuzzing (optional)
  CATS_COMPOSE_NETWORK  Docker network name (auto-detected from compose when empty)

Options:
  --openapi             Validate responses against the OpenAPI contract (required)
  -h, --help            Show this help

When SERVER uses localhost, the script rewrites it to host.docker.internal inside
the container so CATS can reach an API running on the host.
EOF
}

parse_args() {
  while [[ $# -gt 0 ]]; do
    case "$1" in
      -h|--help)
        usage
        exit 0
        ;;
      --openapi)
        OPENAPI_MODE=1
        shift
        if [[ $# -eq 0 || "$1" == -* ]]; then
          echo "Missing OpenAPI file argument after --openapi" >&2
          usage >&2
          exit 2
        fi
        CONTRACT="$1"
        shift
        ;;
      *)
        echo "Unknown option: $1" >&2
        usage >&2
        exit 2
        ;;
    esac
  done
}

parse_args "$@"

if [[ "${OPENAPI_MODE}" != "1" ]]; then
  echo "Only --openapi mode is supported." >&2
  usage >&2
  exit 2
fi

if [[ -z "${CONTRACT}" ]]; then
  echo "OpenAPI file is required." >&2
  usage >&2
  exit 2
fi

to_workspace_path() {
  local abs_path="$1"
  if [[ "${abs_path}" != "${REPO_ROOT}"/* ]]; then
    echo "Path must be inside the repository: ${abs_path}" >&2
    exit 1
  fi
  echo "/workspace/${abs_path#"${REPO_ROOT}/"}"
}

detect_compose_network() {
  if [[ -n "${CATS_COMPOSE_NETWORK}" ]]; then
    CATS_DOCKER_NETWORK="${CATS_COMPOSE_NETWORK}"
    return 0
  fi
  [[ -n "${CATS_COMPOSE_SERVICE}" ]] || return 1
  if ! docker compose -f "${REPO_ROOT}/docker-compose.yaml" ps -q "${CATS_COMPOSE_SERVICE}" >/dev/null 2>&1; then
    return 1
  fi
  local container_id
  container_id="$(docker compose -f "${REPO_ROOT}/docker-compose.yaml" ps -q "${CATS_COMPOSE_SERVICE}" 2>/dev/null | head -1)"
  [[ -n "${container_id}" ]] || return 1
  CATS_DOCKER_NETWORK="$(docker inspect -f '{{range $k, $v := .NetworkSettings.Networks}}{{$k}}{{end}}' "${container_id}" 2>/dev/null | tr -d '[:space:]')"
  [[ -n "${CATS_DOCKER_NETWORK}" ]]
}

resolve_server_for_docker() {
  local scheme hostport port
  CATS_DOCKER_NETWORK=""
  if [[ "${SERVER}" =~ ^https?:// ]]; then
    scheme="${SERVER%%://*}"
    hostport="${SERVER#*://}"
    hostport="${hostport%%/*}"
    port="${hostport##*:}"
    if [[ "${port}" == "${hostport}" ]]; then
      port="80"
      [[ "${scheme}" == "https" ]] && port="443"
    fi
  else
    SERVER_DOCKER="${SERVER}"
    return
  fi

  if detect_compose_network; then
    SERVER_DOCKER="${scheme}://${CATS_COMPOSE_SERVICE}:${port}"
    return
  fi

  if [[ "${SERVER}" =~ ^https?://(localhost|127\.0\.0\.1)([:/]|$) ]]; then
    SERVER_DOCKER="${scheme}://host.docker.internal:${port}"
  else
    SERVER_DOCKER="${SERVER}"
  fi
}

health_url_for_docker() {
  if [[ "${HEALTH_URL}" =~ ^https?://(localhost|127\.0\.0\.1)([:/]|$) && -n "${CATS_DOCKER_NETWORK}" && -n "${CATS_COMPOSE_SERVICE}" ]]; then
    local scheme="${HEALTH_URL%%://*}"
    local hostport="${HEALTH_URL#*://}"
    local path="${hostport#*/}"
    hostport="${hostport%%/*}"
    [[ "${path}" == "${HEALTH_URL#*://}" ]] && path=""
    [[ -n "${path}" ]] && path="/${path}"
    local port="${hostport##*:}"
    [[ "${port}" == "${hostport}" ]] && port="8080"
    echo "${scheme}://${CATS_COMPOSE_SERVICE}:${port}${path}"
    return
  fi
  if [[ "${HEALTH_URL}" == *localhost* ]]; then
    echo "${HEALTH_URL//localhost/host.docker.internal}"
    return
  fi
  if [[ "${HEALTH_URL}" == *127.0.0.1* ]]; then
    echo "${HEALTH_URL//127.0.0.1/host.docker.internal}"
    return
  fi
  echo "${HEALTH_URL}"
}

warn_host_port_conflict() {
  local port="${1}"
  if [[ -n "${CATS_DOCKER_NETWORK}" ]]; then
    return 0
  fi
  if [[ "${SERVER}" =~ ^https?://(localhost|127\.0\.0\.1) ]]; then
    if lsof -nP -iTCP:"${port}" -sTCP:LISTEN 2>/dev/null | grep -qv 'com.docke'; then
      echo "Warning: a non-Docker process is listening on port ${port}." >&2
      echo "  CATS uses host.docker.internal from Docker; static servers on that port cause false errors." >&2
      echo "  Prefer: docker compose up, or set SERVER to the compose service URL." >&2
    fi
  fi
}

verify_cats_reachability() {
  local probe_url
  probe_url="$(health_url_for_docker)"
  local -a docker_args=(run --rm --entrypoint sh)
  if [[ -n "${CATS_DOCKER_NETWORK}" ]]; then
    docker_args+=(--network "${CATS_DOCKER_NETWORK}")
  else
    docker_args+=(--add-host=host.docker.internal:host-gateway)
  fi
  if ! docker "${docker_args[@]}" "${CATS_IMAGE}" \
    -c "curl -fsS '${probe_url}' >/dev/null"; then
    echo "CATS container cannot reach ${probe_url}." >&2
    if [[ "${SERVER_DOCKER}" == *host.docker.internal* ]]; then
      echo "Start the API with docker compose, or stop other listeners on the target port." >&2
    fi
    exit 1
  fi
}

ensure_docker() {
  if ! command -v docker >/dev/null 2>&1; then
    echo "Docker is required. Install Docker and ensure the daemon is running." >&2
    exit 1
  fi
}

validate_report_format() {
  case "${CATS_REPORT_FORMAT}" in
    HTML_JS|HTML_ONLY|JUNIT) ;;
    *)
      echo "Invalid CATS_REPORT_FORMAT: ${CATS_REPORT_FORMAT}" >&2
      echo "Supported: HTML_JS, HTML_ONLY, JUNIT" >&2
      exit 2
      ;;
  esac
}

ensure_cats_image() {
  if [[ "${CATS_REBUILD}" == "1" ]] || ! docker image inspect "${CATS_IMAGE}" >/dev/null 2>&1; then
    echo "Building CATS image ${CATS_IMAGE} from ${CATS_DIR}/Dockerfile..."
    docker build \
      --build-arg "CATS_VERSION=${CATS_VERSION}" \
      -t "${CATS_IMAGE}" \
      -f "${CATS_DIR}/Dockerfile" \
      "${CATS_DIR}"
  fi
}

run_cats_docker() {
  local -a docker_args=(run --rm -v "${REPO_ROOT}:/workspace" -w /workspace)
  if [[ -n "${CATS_DOCKER_NETWORK}" ]]; then
    docker_args+=(--network "${CATS_DOCKER_NETWORK}")
  else
    docker_args+=(--add-host=host.docker.internal:host-gateway)
  fi
  docker "${docker_args[@]}" "${CATS_IMAGE}" "$@"
}

wait_for_server() {
  local elapsed=0
  echo "Waiting for ${HEALTH_URL} (timeout ${WAIT_TIMEOUT_SEC}s)..."
  until curl -fsS "${HEALTH_URL}" >/dev/null 2>&1; do
    if (( elapsed >= WAIT_TIMEOUT_SEC )); then
      echo "API not reachable at ${HEALTH_URL}. Start it first, for example:" >&2
      echo "  docker compose up --build" >&2
      echo "  ./mvnw spring-boot:run" >&2
      exit 1
    fi
    sleep 2
    elapsed=$((elapsed + 2))
  done
  echo "API is up."
}

run_cats_openapi() {
  local -a args=(
    --contract="${CONTRACT_CONTAINER}"
    --server="${SERVER_DOCKER}"
    --reportFormat="${CATS_REPORT_FORMAT}"
    --output="${REPORT_CONTAINER}"
    --httpMethods=POST
    --ignoreResponseBodyCheck
    --skipFuzzers=CheckSecurityHeaders,DuplicateHeaders
    -H "Content-Type=application/json"
    -H "Accept=application/json"
  )

  echo "Running CATS OpenAPI fuzzers in Docker"
  echo "Image:    ${CATS_IMAGE}"
  echo "Server:   ${SERVER_DOCKER} (host: ${SERVER})"
  echo "Contract: ${CONTRACT_CONTAINER}"
  echo "Report:   ${REPORT_CONTAINER} (${CATS_REPORT_FORMAT})"
  run_cats_docker "${args[@]}"
}

server_port_from_url() {
  local hostport="${1#*://}"
  hostport="${hostport%%/*}"
  local port="${hostport##*:}"
  [[ "${port}" == "${hostport}" ]] && port="8080"
  echo "${port}"
}

[[ -f "${CATS_DIR}/Dockerfile" ]] || {
  echo "CATS Dockerfile not found: ${CATS_DIR}/Dockerfile" >&2
  exit 1
}

if [[ "${CONTRACT}" != /* ]]; then
  CONTRACT="${REPO_ROOT}/${CONTRACT}"
fi
CONTRACT="$(cd "$(dirname "${CONTRACT}")" && pwd)/$(basename "${CONTRACT}")"
[[ -f "${CONTRACT}" ]] || {
  echo "OpenAPI contract not found: ${CONTRACT}" >&2
  exit 1
}

REPORT_DIR="$(mkdir -p "${REPORT_DIR}" && cd "${REPORT_DIR}" && pwd)"
CONTRACT_CONTAINER="$(to_workspace_path "${CONTRACT}")"
REPORT_CONTAINER="$(to_workspace_path "${REPORT_DIR}")"
resolve_server_for_docker

validate_report_format
ensure_docker
ensure_cats_image
wait_for_server
warn_host_port_conflict "$(server_port_from_url "${SERVER}")"
verify_cats_reachability
run_cats_openapi

```
            
#### Step Constraints

- **MUST** copy the embedded script verbatim into `run-cats-fuzz.sh`
- **DO NOT** modify the template logic, structure, or features
- **ONLY** `--openapi` mode is supported; pass the OpenAPI file as the argument after the flag

### Step 3: Execute CATS fuzzing

Ask the user for the OpenAPI contract file path before running CATS.

Use a direct question such as: "What is the path to your OpenAPI specification file (for example `src/main/resources/openapi/openapi.yaml`)?"

**STOP HERE** and wait for the user to provide the contract path. **DO NOT** guess common locations, scan the repository, or run `./run-cats-fuzz.sh` until the user shares the path.

After the user responds:

1. Resolve the path relative to the project root when it is not absolute.
2. Confirm the file exists; if not, ask again and remain blocked until a valid path is provided.
3. Confirm the API is reachable (for example `./mvnw spring-boot:run`, `docker compose up`, or an already-running instance).
4. Run fuzzing with the user-provided contract:

```bash
./run-cats-fuzz.sh --openapi <user-provided-openapi-path>
```

5. Capture the command exit code and console output. If the run fails, report the error and stop before reviewing results.

#### Step Constraints

- **MUST** ask the user for the OpenAPI file location before execution
- **MUST NOT** proceed to Step 4 until the user provides a contract path and CATS completes (success or failure)
- **MUST NOT** substitute a default OpenAPI path without explicit user confirmation
- **MUST** verify the contract file exists on disk before running the script

### Step 4: Review CATS results

After a successful CATS run, review the generated report to discover API contract and robustness issues.

Default report location: `cats/cats-report/` (override with `REPORT_DIR` when set).

Review workflow:

1. Open `cats/cats-report/index.html` when `CATS_REPORT_FORMAT` is `HTML_JS` (default); otherwise inspect the report artifacts in `cats/cats-report/`.
2. Scan for failed fuzz cases, unexpected HTTP status codes, response schema mismatches, and missing validation on malformed inputs.
3. Group findings by category: malformed payload handling, missing required fields, boundary violations, wrong status codes, and response body contract drift.
4. For each issue, record the fuzzer name, request details, actual vs expected behaviour, and a minimal reproduction command or curl example.
5. Prioritize fixes: server errors (5xx) and contract violations first, then validation gaps and inconsistent 4xx handling.
6. Summarize total failures, highest-risk endpoints, and recommended next code or contract changes.

#### Step Constraints

- **MUST** base findings on CATS report output, not assumptions
- **MUST** include enough detail to reproduce each reported issue locally
- **MUST NOT** skip review when CATS reported failures or warnings


## Examples

### Table of contents

- Example 1: Install CATS locally with Docker

### Example 1: Install CATS locally with Docker

Title: Use a project-local Dockerfile so contributors can run CATS without installing it on the host
Description: Copy the embedded Dockerfile template from Step 1 into `cats/Dockerfile`, add `run-cats-fuzz.sh` from Step 2, ask the user for the OpenAPI path in Step 3, then review findings in Step 4.

**Good example:**

```bash
# Step 3: run only after the user provides the OpenAPI path
./run-cats-fuzz.sh --openapi src/main/resources/openapi/openapi.yaml

# Step 4: review cats/cats-report/index.html for failures
```

**Bad example:**

```bash
# Avoid: manual host install with version drift across contributors
curl -fsSL https://github.com/Endava/cats/releases/download/cats-13.8.0/cats_uberjar_13.8.0.tar.gz \
  | tar -xz -C /usr/local/bin
java -jar /usr/local/bin/cats.jar --contract openapi.yaml --server http://localhost:8080
```

## Output Format

- **ANALYZE** current API testing and CI workflow to identify where CATS fuzzing should run and what contract sources should be used
- **CATEGORIZE** fuzzing scope by scenario type: malformed input, missing fields, boundary limits, and schema constraint violations
- **IMPLEMENT** CI and local commands so CATS can run consistently with reproducible failure output
- **EXECUTE** `./run-cats-fuzz.sh --openapi` only after the user provides the contract path; wait for their answer before running
- **REVIEW** CATS report output under `cats/cats-report/` and summarize contract violations, validation gaps, and prioritized fixes
- **DOCUMENT** a Docker-based local installation path using `cats/Dockerfile` and `run-cats-fuzz.sh --openapi` with the contract file path, including image build and execution commands
- **EXPLAIN** what failed, why it failed, and how to reproduce findings locally
- **VALIDATE** that generated artifacts and build checks remain consistent after integration

## Safeguards

- **BLOCKING SAFETY CHECK**: Always validate compilation before introducing fuzzing changes
- **REPRODUCIBILITY**: Every CI failure must include enough detail for local reproduction
- **NON-REGRESSION**: Keep existing test suites and build phases unaffected by new fuzzing integration