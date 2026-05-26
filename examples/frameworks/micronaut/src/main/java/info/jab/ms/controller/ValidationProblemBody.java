package info.jab.ms.controller;

import io.micronaut.serde.annotation.Serdeable;
import java.net.URI;
import java.util.List;

@Serdeable
public record ValidationProblemBody(
        URI type,
        String title,
        int status,
        String detail,
        URI instance,
        List<String> violations) {
}
