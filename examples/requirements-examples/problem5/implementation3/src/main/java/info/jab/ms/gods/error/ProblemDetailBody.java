package info.jab.ms.gods.error;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record ProblemDetailBody(String type, String title, int status) {}
