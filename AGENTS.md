# Contributor Quickstart Guide

## Project overview

A collection of `System prompts` for Java Enterprise development.

### Repository Layout

- `.cursor/rules`: a collection of `System prompts` for Java Enterprise development. The main outcome of this project.
- `system-prompts-generator`: a Java project designed to build the System prompts based on XML documents.
- `examples`: a collection of Java examples designed to test with the different System prompts.
- `site-generator`: JBake project designed to autogenerate public site in `docs` folder
- `documentation`: Diverse documentation about the project

## Build and test commands

- Improve current tests to generate better System prompts encoded in Markdown format
- Develop or Refactor current System prompts with new Examples

When you add new code, format the code before testing:

```bash
./mvnw clean spotless:apply
```

Run the tests:

```bash
./mvnw clean verify -pl system-prompts-generator
```

If the tests passes, you could promote the changes to `.cursor/rules`:

```bash
./mvnw clean install -pl system-prompts-generator
```

## General Guidance

- Don´t update any file from the path `.cursor/rules`, find the XML file in the path `generator/src/main/resources`.
- The project is based on Java 24.

## Commit Messages and Pull Requests

- Follow the [Chris Beams](http://chris.beams.io/posts/git-commit/) style for
  commit messages.
- Every pull request should answer:
  - **What changed?**
  - **Why?**
  - **Breaking changes?**
- Comments should be complete sentences and end with a period.
