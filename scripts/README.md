# Markdown Validator

A JBang script that validates markdown files from `.cursor/rules` and `.cursor/rules/templates` directories.

## Features

This validator checks for common markdown issues:

- **Document Structure**: Ensures documents start with H1 headings and have proper heading hierarchy
- **Link Validation**: Checks for broken relative links and cursor rule references (mdc: links)
- **Content Quality**: Detects trailing whitespace, TODO/FIXME comments, and empty content
- **Code Blocks**: Validates code block syntax and language specifications
- **Tables**: Checks table structure consistency and empty cells

## Installation

The script requires JBang to be installed. Install JBang:

```bash
curl -Ls https://sh.jbang.dev | bash -s - app setup
```

## Usage

### Basic Usage

```bash
jbang scripts/MarkdownValidator.java [OPTIONS] <root-directory>
```

### Examples

```bash
# Validate markdown files in current directory
jbang scripts/MarkdownValidator.java .

# Verbose output with detailed validation information
jbang scripts/MarkdownValidator.java --verbose .

# Stop on first error (fail-fast mode)
jbang scripts/MarkdownValidator.java --fail-fast .
```

### Options

- `-v, --verbose`: Enable verbose output showing each file being processed
- `-f, --fail-fast`: Stop validation on the first error found
- `-h, --help`: Show help message
- `-V, --version`: Show version information

## Integration with CI/CD

The script is integrated into the GitHub Actions workflow and runs automatically on push events. It validates all markdown files before running other build steps.

### Workflow Integration

The validation is configured as a dependency for other jobs:

```yaml
jobs:
  validate-markdown:
    name: Validate Markdown Files
    runs-on: ubuntu-latest
    steps:
      # ... setup steps ...
      - name: Validate Markdown Files
        run: jbang scripts/MarkdownValidator.java --verbose .

  generate-cursor-rules:
    needs: validate-markdown
    # ... other job steps ...
```

## Exit Codes

- `0`: All markdown files are valid
- `1`: Validation errors found
- `2`: Invalid arguments or runtime errors

## Validation Rules

### Document Structure
- Documents should start with an H1 heading
- Headings should not be empty
- Documents should contain at least one heading

### Links and References
- Relative file links are checked for existence
- Cursor rule references (mdc: links) are validated
- Empty link text is flagged as an error

### Content Quality
- Trailing whitespace is detected and reported
- TODO/FIXME comments are flagged as potentially incomplete content
- Reference-style links are checked for completeness

### Code Blocks
- Empty code blocks are reported as errors
- Unknown language specifications are flagged
- Both fenced and indented code blocks are validated

### Tables
- Column count consistency across rows
- Empty cells (except in headers) are reported
- Table structure validation using CommonMark extensions

## Dependencies

The script uses the following libraries:
- `info.picocli:picocli:4.7.5` - Command line argument parsing
- `org.commonmark:commonmark:0.21.0` - Markdown parsing
- `org.commonmark:commonmark-ext-gfm-tables:0.21.0` - GitHub Flavored Markdown tables

These dependencies are automatically resolved by JBang when the script runs.