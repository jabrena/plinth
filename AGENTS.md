# Contributor Quickstart Guide

## Project overview

A collection of `System prompts` for Java Enterprise development.

### Repository Layout

- `./cursor/rules`: a collection of `System prompts` for Java Enterprise development. The main outcome of this project.
- `generator`: a Java project designed to build the System prompts based on XML documents.
- `examples`: a collection of Java examples designed to test with the different System prompts.

## Build and test commands

The Java development is located in the folder `generator` and the main activities on this development are:

- Improve current tests to generate better System prompts encoded in Markdown format
- Develop or Refactor current System prompts with new Examples

When you add new code, format the code before testing:

```bash
./mvnw clean spotless:apply
```

Run the tests:

```bash
./mvnw clean verify
```

If the tests passes, you could promote the changes to `./cursor/rules`:

```bash
./mvnw clean install
```

## General Guidance

- Don´t update any file from the path `./cursor/rules`, find the XML file in the path `generator/src/main/resources`.
- The project is based on Java 24.

## Commit Messages and Pull Requests

- Follow the [Chris Beams](http://chris.beams.io/posts/git-commit/) style for
  commit messages.
- Every pull request should answer:
  - **What changed?**
  - **Why?**
  - **Breaking changes?**
- Comments should be complete sentences and end with a period.
