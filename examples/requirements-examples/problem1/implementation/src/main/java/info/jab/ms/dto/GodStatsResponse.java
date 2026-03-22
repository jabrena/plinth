package info.jab.ms.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response DTO for god statistics containing the calculated sum.
 * The sum is returned as a string to handle large BigInteger values.
 */
public record GodStatsResponse(
    @JsonProperty("sum") String sum
) {
}