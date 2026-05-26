package info.jab.ms.dto;

import jakarta.validation.constraints.NotNull;

public record SumRequest(
        @NotNull(message = "must not be null") Integer param1,
        @NotNull(message = "must not be null") Integer param2) {
}
