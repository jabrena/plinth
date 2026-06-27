package info.jab.markdownvalidator.domain;

import java.nio.file.Path;

public record ValidationError(Path file, int lineNumber, String message) {}
