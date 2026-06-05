# verify

## Purpose
Verify implementation completeness and quality against requirements.

## Usage
```
/verify [<scope>]
```

## Scope Options
- `build` - Verify build succeeds
- `tests` - Run and verify test suite
- `coverage` - Check code coverage against targets
- `quality` - Run quality gates (linting, analysis)
- `all` - Run all verifications (default)

## Exit Codes
- `0` - All verifications passed
- `1` - Test failures detected
- `2` - Quality gate failures
- `3` - Build failures

## Frameworks
- Maven
- JUnit 5
- JaCoCo (coverage)
- SpotBugs (quality)

## Output
- Detailed verification report
- Pass/fail status for each check
- Failure details and remediation guidance
- Coverage metrics summary
