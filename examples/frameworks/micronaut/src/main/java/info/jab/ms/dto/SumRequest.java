package info.jab.ms.dto;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotNull;

@Serdeable
@Introspected
public record SumRequest(
        @NotNull Integer param1,
        @NotNull Integer param2) {
}
