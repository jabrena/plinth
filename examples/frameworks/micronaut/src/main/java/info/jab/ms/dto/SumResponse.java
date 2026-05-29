package info.jab.ms.dto;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record SumResponse(Integer result) {
}
