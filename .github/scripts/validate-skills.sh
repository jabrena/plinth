#!/usr/bin/env bash
# Validate SKILL.md files in .agents/skills against the Agent Skills specification.
# Uses skill-check (https://github.com/thedaviddias/skill-check).
#
# Usage:
#   .github/scripts/validate-skills.sh [options]
#
# Options are passed through to skill-check. Examples:
#   --fix                  Apply auto-fixes
#   --format html          Generate HTML report
#   --no-security-scan    Skip security scanning (faster)
set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
REPO_ROOT="$(cd "$SCRIPT_DIR/../.." && pwd)"
SKILLS_DIR="$REPO_ROOT/.agents/skills"

if [[ ! -d "$SKILLS_DIR" ]]; then
  echo "Error: .agents/skills directory not found at $SKILLS_DIR"
  exit 1
fi

cd "$REPO_ROOT"
npx skill-check@latest .agents/skills "$@"
