package info.jab.ms.service;

import info.jab.ms.client.GodDataClient;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigInteger;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@Tag("unit-test")
@ExtendWith(MockitoExtension.class)
class GodAnalysisServiceTest {

    @Mock
    GodDataClient godDataClient;

    @InjectMocks
    GodAnalysisService godAnalysisService;

    @Test
    void happyPath_filterLowercaseN_returnsCorrectSum() {
        // Given
        when(godDataClient.fetchGreekGods()).thenReturn(List.of("nike", "zeus"));

        // When
        // "nike" → code points [110, 105, 107, 101] → "110105107101"
        BigInteger result = godAnalysisService.computeSum("n", List.of("greek"));

        // Then
        assertThat(result).isEqualTo(new BigInteger("110105107101"));
    }

    @Test
    void caseSensitiveFilter_uppercaseN_returnsZero() {
        // Given: all sources return names starting with lowercase 'n' — but filter is uppercase 'N' (code point 78)
        when(godDataClient.fetchGreekGods()).thenReturn(List.of("nike"));
        when(godDataClient.fetchRomanGods()).thenReturn(List.of("neptune"));
        when(godDataClient.fetchNordicGods()).thenReturn(List.of("njord"));

        // When
        BigInteger result = godAnalysisService.computeSum("N", List.of("greek", "roman", "nordic"));

        // Then: no god name starts with uppercase 'N' (code point 78), so sum is zero
        assertThat(result).isEqualTo(BigInteger.ZERO);
    }

    @Test
    void emptySourceResults_returnsZero() {
        // Given
        when(godDataClient.fetchGreekGods()).thenReturn(List.of());
        when(godDataClient.fetchRomanGods()).thenReturn(List.of());
        when(godDataClient.fetchNordicGods()).thenReturn(List.of());

        // When
        BigInteger result = godAnalysisService.computeSum("n", List.of("greek", "roman", "nordic"));

        // Then
        assertThat(result).isEqualTo(BigInteger.ZERO);
    }

    @Test
    void singleSource_onlyGreek_computesCorrectly() {
        // Given: only greek source returns names; roman and nordic are empty
        // "nemesis" → code points [110,101,109,101,115,105,115] → "110101109101115105115"
        when(godDataClient.fetchGreekGods()).thenReturn(List.of("nemesis"));
        when(godDataClient.fetchRomanGods()).thenReturn(List.of());
        when(godDataClient.fetchNordicGods()).thenReturn(List.of());

        // When
        BigInteger result = godAnalysisService.computeSum("n", List.of("greek", "roman", "nordic"));

        // Then
        assertThat(result).isEqualTo(new BigInteger("110101109101115105115"));
    }

    @Test
    void unicodeAlgorithmCorrectness_singleName() {
        // Given: "nike" has code points n=110, i=105, k=107, e=101 → digit string "110105107101"
        when(godDataClient.fetchGreekGods()).thenReturn(List.of("nike"));

        // When
        BigInteger result = godAnalysisService.computeSum("n", List.of("greek"));

        // Then
        assertThat(result).isEqualTo(new BigInteger("110105107101"));
    }
}
