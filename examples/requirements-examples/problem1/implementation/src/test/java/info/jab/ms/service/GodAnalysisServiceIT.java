package info.jab.ms.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@DisplayName("GodAnalysisService IT")
class GodAnalysisServiceIT {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /** Same JSON arrays as WireMock {@code bodyFileName} stubs under {@code src/test/resources/__files/}. */
    private static final List<String> FIXTURE_PATHS = List.of(
            "__files/greek-gods.json",
            "__files/roman-gods.json",
            "__files/nordic-gods.json");

    private GodAnalysisService godAnalysisService;

    @BeforeEach
    void setUp() {
        godAnalysisService = new GodAnalysisService();
    }

    @Test
    @DisplayName("Should calculate expected sum for all sources with filter='n'")
    void shouldCalculateExpectedSumForAllSourcesWithFilterN() {
        // Given: the real 36 god names from all three mythology APIs with filter='n'
        // filter='n' normalized to 'N': matches Nike, Nemesis (Greek), Neptun (Roman), Njord (Nordic)
        List<String> allNames = createRealGodNames();
        String filter = "n";

        // When
        String result = godAnalysisService.calculateSum(allNames, filter);

        // Then: Nike+Nemesis+Neptun+Njord = 78179288397447443426
        assertThat(result).isEqualTo("78179288397447443426");
    }

    @Test
    @DisplayName("Should return zero when no names match the filter character")
    void shouldReturnZeroWhenNoNamesMatchFilter() {
        // Given: names where none start with 'x' (or 'X')
        List<String> names = List.of("Zeus", "Apollo", "Thor");
        String filter = "x";

        // When
        String result = godAnalysisService.calculateSum(names, filter);

        // Then
        assertThat(result).isEqualTo("0");
    }

    @Test
    @DisplayName("Should handle service-level error conditions gracefully")
    void shouldHandleServiceLevelErrorConditionsGracefully() {
        // Given: edge case inputs
        List<String> emptyList = List.of();
        List<String> nullContainingList = java.util.Arrays.asList("Zeus", null, "Apollo");
        List<String> emptyStringList = List.of("Zeus", "", "Apollo");

        // When & Then: should handle gracefully without exceptions
        assertThat(godAnalysisService.calculateSum(emptyList, "z")).isEqualTo("0");
        assertThat(godAnalysisService.calculateSum(nullContainingList, "z")).isNotNull();
        assertThat(godAnalysisService.calculateSum(emptyStringList, "z")).isNotNull();
    }

    /**
     * All god names as returned by the stubbed mythology APIs — loaded from the same JSON fixtures
     * WireMock serves via {@code bodyFileName} (see {@code mappings/*.json}).
     */
    private static List<String> createRealGodNames() {
        ClassLoader cl = GodAnalysisServiceIT.class.getClassLoader();
        List<String> combined = new ArrayList<>();
        for (String path : FIXTURE_PATHS) {
            try (InputStream in = cl.getResourceAsStream(path)) {
                assertThat(in)
                        .as("classpath resource %s (keep in sync with WireMock __files)", path)
                        .isNotNull();
                combined.addAll(OBJECT_MAPPER.readValue(in, new TypeReference<List<String>>() {}));
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to read " + path, e);
            }
        }
        return List.copyOf(combined);
    }
}
