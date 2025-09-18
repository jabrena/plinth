# Developer notes

## Basic Build Commands

```bash
# Standard build and validation
./mvnw clean verify
./mvnw clean verify -pl system-prompts-generator
./mvnw clean install -pl system-prompts-generator

# Site generation
./mvnw clean generate-resources -pl site-generator -P site-update
```

## Development Servers

```bash
# Serve generated documentation
jwebserver -p 8000 -d "$(pwd)/docs"

# Serve presentation
jwebserver -p 8000 -d "$(pwd)/documentation/dvbe25/"
```

## Code Quality and Security Profiles

### Code Formatting with Spotless
```bash
# Check code formatting across all modules
./mvnw spotless:check

# Apply code formatting fixes
./mvnw spotless:apply

# Format specific module
./mvnw spotless:apply -pl system-prompts-generator
```

### Security Vulnerability Scanning
```bash
# Run OWASP dependency check for security vulnerabilities
./mvnw clean verify -Psecurity

# View security report
open target/dependency-check/dependency-check-report.html

# Run security scan on specific module
./mvnw clean verify -Psecurity -pl system-prompts-generator
```

### Static Code Analysis
```bash
# Run SpotBugs and PMD static analysis
./mvnw clean verify -Pfind-bugs

# Generate analysis reports
./mvnw site -Pfind-bugs

# View reports
open target/site/spotbugs.html
open target/site/pmd.html
```

### Version Management
```bash
# Check for plugin updates
./mvnw versions:display-plugin-updates

# Check for dependency updates
./mvnw versions:display-dependency-updates

# Check for property updates
./mvnw versions:display-property-updates
```

### Combined Quality Checks
```bash
# Run all quality checks together
./mvnw clean verify -Psecurity,find-bugs

# Full quality pipeline with formatting
./mvnw spotless:apply clean verify -Psecurity,find-bugs
```
