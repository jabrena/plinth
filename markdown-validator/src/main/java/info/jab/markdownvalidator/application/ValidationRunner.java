package info.jab.markdownvalidator.application;

import info.jab.markdownvalidator.domain.FileValidationResult;
import java.nio.file.Path;
import java.util.List;

public interface ValidationRunner {
    List<FileValidationResult> validate(List<Path> markdownFiles, boolean verbose);
}
