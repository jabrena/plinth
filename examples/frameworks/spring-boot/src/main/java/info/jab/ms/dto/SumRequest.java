package info.jab.ms.dto;

import jakarta.validation.constraints.NotNull;

public record SumRequest(
		@NotNull Integer param1,
		@NotNull Integer param2) {
}
