#!/usr/bin/env bash
set -euo pipefail

# Lives next to README.md: creates .agents/skills -> ../skills (repo skills/)

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT="$SCRIPT_DIR"
AGENTS_DIR="$ROOT/.agents"
LINK_PATH="$AGENTS_DIR/skills"
SKILLS_DIR="$ROOT/skills"

usage() {
  echo "Usage: $(basename "$0") -c | -d" >&2
  echo "  -c  Create .agents/ and symlink .agents/skills to the skills/ directory" >&2
  echo "  -d  Remove .agents/skills; remove .agents/ if it becomes empty" >&2
  exit 1
}

cmd_create() {
  if [[ ! -d "$SKILLS_DIR" ]]; then
    echo "error: skills directory not found: $SKILLS_DIR" >&2
    exit 1
  fi
  mkdir -p "$AGENTS_DIR"
  if [[ -L "$LINK_PATH" ]]; then
    echo "error: symlink already exists: $LINK_PATH" >&2
    exit 1
  fi
  if [[ -e "$LINK_PATH" ]]; then
    echo "error: path already exists (not a symlink): $LINK_PATH" >&2
    exit 1
  fi
  ln -s "../skills" "$LINK_PATH"
  echo "Created $LINK_PATH -> ../skills"
}

cmd_delete() {
  if [[ -L "$LINK_PATH" ]]; then
    rm "$LINK_PATH"
    echo "Removed symlink $LINK_PATH"
  elif [[ -e "$LINK_PATH" ]]; then
    echo "error: $LINK_PATH exists but is not a symlink; not removing" >&2
    exit 1
  else
    echo "Nothing to remove: $LINK_PATH does not exist"
  fi
  if [[ -d "$AGENTS_DIR" ]] && [[ -z "$(ls -A "$AGENTS_DIR" 2>/dev/null || true)" ]]; then
    rmdir "$AGENTS_DIR"
    echo "Removed empty directory $AGENTS_DIR"
  fi
}

case "${1:-}" in
  -c) cmd_create ;;
  -d) cmd_delete ;;
  *) usage ;;
esac
