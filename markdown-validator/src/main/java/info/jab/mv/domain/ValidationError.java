package info.jab.mv.domain;

import java.nio.file.Path;

public record ValidationError(Path file, int lineNumber, String message) {}
