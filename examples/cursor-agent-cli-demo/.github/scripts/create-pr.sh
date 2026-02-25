#!/bin/bash
set -e

# Script to create PR with changes (equivalent to lines 47-108 of agent-on-demand.yml)

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Get repository info - prefer GITHUB_REPOSITORY env var (from GitHub Actions), otherwise parse from git remote
if [ -n "$GITHUB_REPOSITORY" ]; then
    GITHUB_REPO="$GITHUB_REPOSITORY"
    echo -e "${GREEN}Repository (from GITHUB_REPOSITORY): $GITHUB_REPO${NC}"
else
    REPO_URL=$(git remote get-url origin 2>/dev/null || echo "")
    if [ -z "$REPO_URL" ]; then
        echo -e "${RED}Error: No git remote 'origin' found and GITHUB_REPOSITORY not set${NC}"
        exit 1
    fi
    
    # Extract owner/repo from various URL formats
    if [[ "$REPO_URL" =~ github\.com[:/]([^/]+)/([^/]+)(\.git)?$ ]]; then
        REPO_OWNER="${BASH_REMATCH[1]}"
        REPO_NAME="${BASH_REMATCH[2]%.git}"
        GITHUB_REPO="$REPO_OWNER/$REPO_NAME"
    else
        echo -e "${RED}Error: Could not parse repository from remote URL: $REPO_URL${NC}"
        exit 1
    fi
    echo -e "${GREEN}Repository (from git remote): $GITHUB_REPO${NC}"
fi

# Get tokens from environment
GITHUB_TOKEN="${GITHUB_TOKEN:-}"
PAT_TOKEN="${PAT_TOKEN:-}"
AUTH_TOKEN="${PAT_TOKEN:-$GITHUB_TOKEN}"

if [ -z "$AUTH_TOKEN" ]; then
    echo -e "${RED}Error: GITHUB_TOKEN or PAT_TOKEN must be set${NC}"
    exit 1
fi

# Get default branch - prefer GITHUB_BASE_REF or use gh CLI, otherwise parse from git
if [ -n "$GITHUB_BASE_REF" ]; then
    DEFAULT_BRANCH="$GITHUB_BASE_REF"
    echo -e "${GREEN}Default branch (from GITHUB_BASE_REF): $DEFAULT_BRANCH${NC}"
elif command -v gh &> /dev/null && [ -n "$GITHUB_REPO" ]; then
    DEFAULT_BRANCH=$(gh repo view "$GITHUB_REPO" --json defaultBranchRef -q .defaultBranchRef.name 2>/dev/null || echo "main")
    echo -e "${GREEN}Default branch (from gh CLI): $DEFAULT_BRANCH${NC}"
else
    DEFAULT_BRANCH=$(git symbolic-ref refs/remotes/origin/HEAD 2>/dev/null | sed 's@^refs/remotes/origin/@@' || echo "main")
    echo -e "${GREEN}Default branch (from git): $DEFAULT_BRANCH${NC}"
fi

# Validate DEFAULT_BRANCH is not empty
if [ -z "$DEFAULT_BRANCH" ]; then
    echo -e "${RED}Error: Could not determine default branch${NC}"
    exit 1
fi

# Step 1: Configure Git (if not already configured)
echo -e "\n${YELLOW}=== Configuring Git ===${NC}"
GIT_USER_NAME=$(git config --global user.name 2>/dev/null || echo "")
GIT_USER_EMAIL=$(git config --global user.email 2>/dev/null || echo "")

if [ -z "$GIT_USER_NAME" ]; then
    GIT_USER_NAME="${GITHUB_ACTOR:-$(whoami)}"
    git config --global user.name "$GIT_USER_NAME"
    echo "Set git user.name to: $GIT_USER_NAME"
else
    echo "Git user.name already configured: $GIT_USER_NAME"
fi

if [ -z "$GIT_USER_EMAIL" ]; then
    GIT_USER_EMAIL="${GITHUB_ACTOR:-$(whoami)}@users.noreply.github.com"
    git config --global user.email "$GIT_USER_EMAIL"
    echo "Set git user.email to: $GIT_USER_EMAIL"
else
    echo "Git user.email already configured: $GIT_USER_EMAIL"
fi

# Step 2: Commit changes in feature branch
echo -e "\n${YELLOW}=== Creating feature branch and committing changes ===${NC}"
BRANCH_NAME="cursor-agent/$(date +%Y%m%d-%H%M%S)"
echo "Branch name: $BRANCH_NAME"

# Show current branch and status
CURRENT_BRANCH=$(git rev-parse --abbrev-ref HEAD 2>/dev/null || echo "unknown")
echo "Current branch: $CURRENT_BRANCH"
echo "Working directory status:"
git status --short || true

# Ensure we have the default branch fetched for comparison
echo "Fetching default branch: $DEFAULT_BRANCH"
git fetch origin "$DEFAULT_BRANCH:$DEFAULT_BRANCH" 2>/dev/null || git fetch origin "$DEFAULT_BRANCH" 2>/dev/null || true

# Create the feature branch from current state (preserves any uncommitted changes)
# This matches the original workflow logic: git checkout -B "$BRANCH_NAME"
git checkout -B "$BRANCH_NAME"

# Stage and commit any changes (matching original workflow logic)
git add -A
git commit -m "feat: Hello World Java program for PR" || echo "No changes to commit"

# Push branch
echo "Pushing branch to origin..."
git push origin "$BRANCH_NAME" || {
    echo -e "${RED}Error: Failed to push branch. Make sure you have push access.${NC}"
    exit 1
}

# Verify branch exists on remote
echo "Verifying branch exists on remote..."
sleep 2  # Give GitHub a moment to process
if ! git ls-remote --heads origin "$BRANCH_NAME" | grep -q "$BRANCH_NAME"; then
    echo -e "${YELLOW}Warning: Branch may not be visible on remote yet${NC}"
fi

# Export BRANCH_NAME for GitHub Actions (if GITHUB_ENV is set)
if [ -n "$GITHUB_ENV" ]; then
    echo "BRANCH_NAME=$BRANCH_NAME" >> "$GITHUB_ENV"
fi

# Step 3: Create PR
echo -e "\n${YELLOW}=== Creating Pull Request ===${NC}"
API_BASE="https://api.github.com/repos/$GITHUB_REPO"

# Use jq to properly construct JSON payload (prevents JSON injection and ensures proper formatting)
if command -v jq &> /dev/null; then
    JSON_PAYLOAD=$(jq -n \
        --arg title "feat: Hello World Java program for PR" \
        --arg body "Automated PR created by cursor-agent workflow" \
        --arg head "$BRANCH_NAME" \
        --arg base "$DEFAULT_BRANCH" \
        '{title: $title, body: $body, head: $head, base: $base}')
else
    # Fallback to inline JSON (original workflow approach)
    JSON_PAYLOAD="{\"title\":\"feat: Hello World Java program for PR\",\"body\":\"Automated PR created by cursor-agent workflow\",\"head\":\"$BRANCH_NAME\",\"base\":\"$DEFAULT_BRANCH\"}"
fi

# Debug: Show the payload being sent
echo "PR payload: $JSON_PAYLOAD"

RESPONSE=$(curl -s -w "\n%{http_code}" -X POST \
    -H "Accept: application/vnd.github+json" \
    -H "Authorization: Bearer $AUTH_TOKEN" \
    -H "X-GitHub-Api-Version: 2022-11-28" \
    -H "Content-Type: application/json" \
    "$API_BASE/pulls" \
    -d "$JSON_PAYLOAD")

HTTP_CODE=$(echo "$RESPONSE" | tail -n1)
BODY=$(echo "$RESPONSE" | sed '$d')

if [ "$HTTP_CODE" = "201" ]; then
    PR_NUMBER=$(echo "$BODY" | jq -r '.number')
    PR_URL=$(echo "$BODY" | jq -r '.html_url')
    echo -e "${GREEN}Created PR #$PR_NUMBER at $PR_URL${NC}"
    
    # Export PR_NUMBER for GitHub Actions (if GITHUB_ENV is set)
    if [ -n "$GITHUB_ENV" ]; then
        echo "PR_NUMBER=$PR_NUMBER" >> "$GITHUB_ENV"
    fi
    
    # Step 4: Post PR comment
    echo -e "\n${YELLOW}=== Posting comment on PR ===${NC}"
    if command -v jq &> /dev/null; then
        COMMENT_PAYLOAD=$(jq -n --arg body "Docs updated" '{body: $body}')
    else
        COMMENT_PAYLOAD="{\"body\":\"Docs updated\"}"
    fi
    COMMENT_RESPONSE=$(curl -s -w "\n%{http_code}" -X POST \
        -H "Accept: application/vnd.github+json" \
        -H "Authorization: Bearer $AUTH_TOKEN" \
        -H "X-GitHub-Api-Version: 2022-11-28" \
        -H "Content-Type: application/json" \
        "$API_BASE/issues/$PR_NUMBER/comments" \
        -d "$COMMENT_PAYLOAD")
    
    COMMENT_HTTP_CODE=$(echo "$COMMENT_RESPONSE" | tail -n1)
    if [ "$COMMENT_HTTP_CODE" = "201" ]; then
        echo -e "${GREEN}Posted comment on PR #$PR_NUMBER${NC}"
    else
        echo -e "${YELLOW}Warning: Failed to post comment (HTTP $COMMENT_HTTP_CODE)${NC}"
    fi
else
    echo -e "${RED}Failed to create PR (HTTP $HTTP_CODE)${NC}"
    
    # Extract and display error message from GitHub API response
    if command -v jq &> /dev/null && [ -n "$BODY" ]; then
        ERROR_MSG=$(echo "$BODY" | jq -r '.message // .errors[0].message // empty' 2>/dev/null || echo "")
        if [ -n "$ERROR_MSG" ] && [ "$ERROR_MSG" != "null" ]; then
            echo -e "${RED}GitHub API Error: $ERROR_MSG${NC}"
        fi
        # Show full error details for debugging
        echo -e "${YELLOW}Full API response:${NC}"
        echo "$BODY" | jq '.' 2>/dev/null || echo "$BODY"
    else
        echo -e "${YELLOW}API Response:${NC}"
        echo "$BODY"
    fi
    
    if [ "$HTTP_CODE" = "403" ]; then
        echo -e "${RED}Error: Enable 'Allow GitHub Actions to create and approve pull requests' in repository settings, or use PAT_TOKEN${NC}"
    elif [ "$HTTP_CODE" = "422" ]; then
        echo -e "${RED}Error: Validation failed. Common causes:${NC}"
        echo "  - Branch '$BRANCH_NAME' doesn't exist on remote"
        echo "  - No differences between '$BRANCH_NAME' and '$DEFAULT_BRANCH'"
        echo "  - Branch name is invalid or already exists"
        echo ""
        echo "Checking branch status..."
        git ls-remote --heads origin "$BRANCH_NAME" && echo "✓ Branch exists on remote" || echo "✗ Branch NOT found on remote"
        git log --oneline "$DEFAULT_BRANCH..$BRANCH_NAME" 2>/dev/null | head -5 || echo "No commits difference found"
    fi
    
    echo ""
    echo "Create manually: https://github.com/$GITHUB_REPO/compare/$DEFAULT_BRANCH...$BRANCH_NAME"
    exit 1
fi

echo -e "\n${GREEN}✓ Script completed successfully${NC}"

