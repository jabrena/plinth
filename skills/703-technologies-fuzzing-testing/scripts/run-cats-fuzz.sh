#!/usr/bin/env bash
# Contract-driven API fuzzing with a trusted local CATS image.
# Runs CATS in Docker (see cats/Dockerfile). API must be reachable from the container.
set -euo pipefail

REPO_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CATS_DIR="${REPO_ROOT}/cats"

CONTRACT=""
SERVER="${SERVER:-http://localhost:8080}"
HEALTH_URL="${HEALTH_URL:-${SERVER}/actuator/health}"
CATS_IMAGE="${CATS_IMAGE:-local/cats:trusted}"
CATS_JAR="${CATS_JAR:-${CATS_DIR}/cats.jar}"
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
  CATS_IMAGE            Docker image tag (default: local/cats:trusted)
  CATS_JAR              Trusted CATS jar in build context (default: cats/cats.jar)
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
  local -a docker_args=(run --rm --read-only --cap-drop ALL --security-opt no-new-privileges --entrypoint sh)
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
    if [[ ! -f "${CATS_JAR}" ]]; then
      echo "Trusted CATS jar not found: ${CATS_JAR}" >&2
      echo "Place a verified CATS jar in cats/cats.jar or set CATS_JAR to a verified local jar." >&2
      exit 1
    fi
    local cats_jar_name
    cats_jar_name="$(basename "${CATS_JAR}")"
    if [[ "$(cd "$(dirname "${CATS_JAR}")" && pwd)" != "${CATS_DIR}" ]]; then
      echo "CATS_JAR must be inside ${CATS_DIR} so Docker can copy it from the build context." >&2
      exit 1
    fi
    echo "Building CATS image ${CATS_IMAGE} from ${CATS_DIR}/Dockerfile..."
    docker build \
      --build-arg "CATS_JAR=${cats_jar_name}" \
      -t "${CATS_IMAGE}" \
      -f "${CATS_DIR}/Dockerfile" \
      "${CATS_DIR}"
  fi
}

run_cats_docker() {
  local -a docker_args=(run --rm --cap-drop ALL --security-opt no-new-privileges --mount "type=bind,source=${REPO_ROOT},target=/workspace" -w /workspace)
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
