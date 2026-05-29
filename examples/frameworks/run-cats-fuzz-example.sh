#!/usr/bin/env bash
# Start a framework example, run CATS against examples/frameworks/openapi.yaml, then stop the app.
set -euo pipefail

FRAMEWORKS_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
FRAMEWORK="${1:-}"

usage() {
  cat <<'EOF'
Usage: run-cats-fuzz-example.sh <spring-boot|quarkus|micronaut>

Builds and starts the selected framework example on port 8080, runs CATS OpenAPI
fuzzing with examples/frameworks/openapi.yaml, and stops the app when finished.

Environment variables are forwarded to run-cats-fuzz.sh (for example CATS_REPORT_FORMAT).
REPORT_DIR defaults to cats/cats-report/<framework>.
EOF
}

if [[ -z "${FRAMEWORK}" || "${FRAMEWORK}" == "-h" || "${FRAMEWORK}" == "--help" ]]; then
  usage
  exit 0
fi

APP_DIR=""
JAR_PATH=""
HEALTH_URL=""
APP_PID=""

case "${FRAMEWORK}" in
  spring-boot)
    APP_DIR="${FRAMEWORKS_ROOT}/spring-boot"
    JAR_PATH="target/demo-0.0.1-SNAPSHOT.jar"
    HEALTH_URL="http://localhost:8080/actuator/health"
    ;;
  quarkus)
    APP_DIR="${FRAMEWORKS_ROOT}/quarkus"
    JAR_PATH="target/quarkus-app/quarkus-run.jar"
    HEALTH_URL="http://localhost:8080/q/health/ready"
    ;;
  micronaut)
    APP_DIR="${FRAMEWORKS_ROOT}/micronaut"
    JAR_PATH="target/my-app-0.1.jar"
    HEALTH_URL="http://localhost:8080/health"
    ;;
  *)
    echo "Unknown framework: ${FRAMEWORK}" >&2
    usage >&2
    exit 2
    ;;
esac

cleanup() {
  if [[ -n "${APP_PID}" ]] && kill -0 "${APP_PID}" 2>/dev/null; then
    kill "${APP_PID}" 2>/dev/null || true
    wait "${APP_PID}" 2>/dev/null || true
  fi
}
trap cleanup EXIT INT TERM

wait_for_port_free() {
  local port="${1}"
  local elapsed=0
  while lsof -nP -iTCP:"${port}" -sTCP:LISTEN >/dev/null 2>&1; do
    if (( elapsed >= 30 )); then
      echo "Port ${port} is still in use after ${elapsed}s." >&2
      exit 1
    fi
    sleep 1
    elapsed=$((elapsed + 1))
  done
}

echo "Building ${FRAMEWORK}..."
(
  cd "${APP_DIR}"
  ./mvnw --batch-mode --no-transfer-progress -q package -DskipTests
)

echo "Starting ${FRAMEWORK}..."
wait_for_port_free 8080
(
  cd "${APP_DIR}"
  exec java -jar "${JAR_PATH}"
) &
APP_PID=$!

export SERVER="${SERVER:-http://localhost:8080}"
export HEALTH_URL
export REPORT_DIR="${REPORT_DIR:-${FRAMEWORKS_ROOT}/cats/cats-report/${FRAMEWORK}}"

"${FRAMEWORKS_ROOT}/run-cats-fuzz.sh" --openapi openapi.yaml

echo "CATS fuzzing completed for ${FRAMEWORK}. Report: ${REPORT_DIR}"
