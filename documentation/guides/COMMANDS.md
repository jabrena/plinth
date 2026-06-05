# Project Commands Guide

Project commands are a set of workflow utilities that streamline the Java development process. They integrate with your editor and help you manage issues, branches, implementation, and verification in a structured way.

## Available Commands

The project provides four essential commands designed to work together:

### 1. `/update-issue-description`

**Purpose**: Update GitHub issues with structured content following agile templates

**When to use**:
- When creating or refining a user story on GitHub
- When updating issue requirements based on team feedback
- When formalizing acceptance criteria in an issue

**Usage**:
```bash
/update-issue-description 789
/update-issue-description 789 ./user-story.md
```

**Input**:
- Issue number (required)
- File path to content (optional)

**Output**:
- Updated GitHub issue
- Confirmation of changes

**Features**:
- Interactive content entry or file-based import
- Preserves issue metadata (labels, assignees, milestones)
- Validates markdown formatting
- Supports agile user story templates

**Example Workflow**:
```
1. Create GitHub issue #789 with initial description
2. Use /update-issue-description 789
3. Paste or reference the detailed user story content
4. Issue is updated with structured format
```

---

### 2. `/create-feature-branch`

**Purpose**: Create feature branches with proper naming conventions

**When to use**:
- When starting work on a new feature
- When fixing a bug that needs its own branch
- When creating an enhancement branch

**Usage**:
```bash
/create-feature-branch add-commands-system
/create-feature-branch fix-installation-bug main
```

**Input**:
- Feature name (required)
- Base branch (optional, defaults to main/develop)

**Output**:
- New feature branch created and checked out
- Confirmation message with branch name

**Naming Conventions**:
- **Features**: `feature/feature-name`
- **Fixes**: `fix/bug-description`
- **Enhancements**: `enhancement/enhancement-name`
- **Chores**: `chore/task-description`

**Example Workflow**:
```
1. /create-feature-branch add-commands-system
2. Branch created: feature/add-commands-system
3. Make your changes on this branch
4. Commit following conventional commit format
5. Push and create pull request
```

---

### 3. `/implement`

**Purpose**: Implement features with test-driven development approach

**When to use**:
- After specification is finalized
- When you have clear acceptance criteria
- When ready to write code with tests

**Usage**:
```bash
/implement
/implement ./specs/004-commands-installation.feature
```

**Input**:
- Optional specification file (Gherkin, user story, or task description)
- If not provided, you'll be prompted to paste content

**Output**:
- Implementation code
- Unit and integration tests
- Documentation updates
- Test execution report

**Supported Input Formats**:
- Gherkin feature files (.feature)
- User stories (Markdown with As/I want/So that)
- Task descriptions
- OpenSpec change proposals

**TDD Workflow**:
```
1. /implement ./features/my-feature.feature
2. Tests are generated and written first
3. Implementation code is written
4. All tests are executed
5. Code quality checks run
6. Implementation report is generated
```

**Quality Checks**:
- ✓ Code follows project conventions
- ✓ Tests cover acceptance criteria (>80% coverage)
- ✓ Build passes with no errors
- ✓ Documentation is updated
- ✓ Code style is consistent

---

### 4. `/verify`

**Purpose**: Verify implementation meets acceptance criteria and quality standards

**When to use**:
- After implementation is complete
- Before merging to main/develop
- As a quality gate check
- When validating pull request changes

**Usage**:
```bash
/verify
/verify ./features/004-commands-installation.feature
```

**Input**:
- Optional specification path
- If not provided, auto-detects specification

**Output**:
- Comprehensive verification report
- Test execution results
- Code quality metrics
- Coverage analysis
- Pass/fail status

**Verification Checklist**:
- [ ] All acceptance criteria scenarios pass
- [ ] Unit test coverage > 80%
- [ ] Integration tests pass
- [ ] No code quality issues
- [ ] Build succeeds
- [ ] Documentation updated
- [ ] No security warnings
- [ ] Performance acceptable

**Exit Codes**:
- **0**: All verifications passed ✓
- **1**: Some tests failed ✗
- **2**: Quality gates not met ⚠
- **3**: Build failed ✗

---

## Typical Development Workflow

Here's how to use all four commands together:

### Step 1: Define the Requirement
```bash
/update-issue-description 789
# Provide the user story and acceptance criteria
```

### Step 2: Create a Branch
```bash
/create-feature-branch add-my-feature
# Branch created: feature/add-my-feature
```

### Step 3: Implement the Solution
```bash
/implement ./features/my-feature.feature
# Tests are written and code is implemented
```

### Step 4: Verify Completeness
```bash
/verify ./features/my-feature.feature
# All checks pass, ready to merge
```

### Step 5: Push and Create PR
```bash
git push origin feature/add-my-feature
# Create pull request on GitHub
```

---

## Installation

The commands are installed using the `@004-commands-installation` skill.

### Option 1: Interactive Installation (Recommended)

```bash
@004-commands-installation
```

Then select your preferred directory:
- `.cursor/command` (VS Code Cursor)
- `.claude/commands` (Claude for Desktop)
- `.github/commands` (GitHub Actions)
- `.codex/commands` (Codex)

### Option 2: Skill Registration

If using a skill management tool:
```bash
npx skills add jabrena/cursor-rules-java --all --agent cursor
```

### Option 3: Manual Installation

Copy command files to your preferred directory:
```bash
mkdir -p .cursor/command
cp -r .agents/skills/004-commands-installation/resources/commands/* .cursor/command/
```

---

## Command Integration

The commands are designed to work together as a cohesive workflow:

```
Issue (GitHub) 
    ↓
/update-issue-description (structure requirement)
    ↓
/create-feature-branch (prepare workspace)
    ↓
/implement (write code with TDD)
    ↓
/verify (validate completeness)
    ↓
Pull Request (GitHub)
```

---

## Best Practices

### 1. Always Start with Clear Requirements
- Use `/update-issue-description` to formalize requirements
- Ensure acceptance criteria are clear and testable
- Get team alignment before starting implementation

### 2. Branch Strategy
- Use meaningful branch names following conventions
- Create one feature branch per issue/task
- Keep branches focused and reasonably scoped

### 3. Test-Driven Development
- Use `/implement` to generate tests from specifications
- Ensure tests cover acceptance criteria
- Maintain code coverage above 80%

### 4. Quality Gates
- Always run `/verify` before submitting pull requests
- Ensure all checks pass (tests, coverage, quality)
- Review verification reports for improvement areas

### 5. Documentation
- Keep acceptance criteria up to date
- Document non-obvious implementation decisions
- Link issues to pull requests

---

## Troubleshooting

### Command Not Found
- Ensure commands are installed using `@004-commands-installation`
- Check the correct directory is configured in your editor
- Verify command files exist in the installation directory

### Issue Update Fails
- Verify GitHub credentials are configured
- Check issue number is correct
- Ensure markdown content is valid

### Branch Creation Fails
- Verify you have Git configured
- Check base branch exists
- Ensure branch name follows conventions

### Implementation Issues
- Ensure specification is clear and complete
- Check test coverage is adequate
- Review code for quality issues
- Consult with team for architectural decisions

### Verification Fails
- Review failed test messages
- Check code coverage is above threshold
- Run build locally to validate
- Fix issues and re-run verification

---

## Related Documentation

- [Skills for Java](./GETTING-STARTED-SKILLS.md) - Learn about available skills
- [Agents for Java](./GETTING-STARTED-AGENTS.md) - Understand agent-driven workflows
- [GitHub Issues Guide](../guides/GITHUB-ISSUES.md) - Issue management practices

---

## Feedback and Contributions

If you have suggestions for improving these commands or want to add new ones:

1. Open an issue with your suggestion
2. Describe the workflow problem you're solving
3. Include examples of the new command in action
4. Follow [CONTRIBUTING.md](../../CONTRIBUTING.md) guidelines

---

## Version Information

- **Version**: 0.16.0-SNAPSHOT
- **License**: Apache-2.0
- **Project**: [cursor-rules-java](https://github.com/jabrena/cursor-rules-java)

---

Last updated: June 2026
