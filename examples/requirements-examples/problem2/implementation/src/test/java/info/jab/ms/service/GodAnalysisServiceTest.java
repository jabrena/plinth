package info.jab.ms.service;

import info.jab.ms.client.GreekGodsClient;
import info.jab.ms.client.WikipediaClient;
import info.jab.ms.model.GodAnalysisResponse;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@QuarkusTest
@Tag("integration-test")
public class GodAnalysisServiceTest {

    @InjectMock
    @RestClient
    GreekGodsClient greekGodsClient;

    @InjectMock
    @RestClient
    WikipediaClient wikipediaClient;

    @Inject
    GodAnalysisService godAnalysisService;

    @BeforeEach
    void setUp() {
        // Reset any static state if needed
    }

    @Test
    void shouldReturnSingleWinnerWithMostCharacters() {
        // Given
        when(greekGodsClient.getGreekGods()).thenReturn(List.of("Ares", "Zeus"));
        when(wikipediaClient.getWikipediaPage("Ares")).thenReturn("a".repeat(100));
        when(wikipediaClient.getWikipediaPage("Zeus")).thenReturn("z".repeat(200));

        // When
        GodAnalysisResponse result = godAnalysisService.findGodsWithMostLiterature();

        // Then
        assertThat(result.gods()).containsExactly("Zeus");
        assertThat(result.characterCount()).isEqualTo(200L);
    }

    @Test
    void shouldReturnAllTiedWinnersSortedNaturally() {
        // Given
        when(greekGodsClient.getGreekGods()).thenReturn(List.of("Zeus", "Ares", "Athena"));
        when(wikipediaClient.getWikipediaPage("Zeus")).thenReturn("z".repeat(150));
        when(wikipediaClient.getWikipediaPage("Ares")).thenReturn("a".repeat(150));
        when(wikipediaClient.getWikipediaPage("Athena")).thenReturn("t".repeat(100));

        // When
        GodAnalysisResponse result = godAnalysisService.findGodsWithMostLiterature();

        // Then - tied winners should be sorted alphabetically
        assertThat(result.gods()).containsExactly("Ares", "Zeus");
        assertThat(result.characterCount()).isEqualTo(150L);
    }

    @Test
    void shouldReturnThreeWayTieSortedNaturally() {
        // Given
        when(greekGodsClient.getGreekGods()).thenReturn(List.of("Zeus", "Ares", "Athena"));
        when(wikipediaClient.getWikipediaPage("Zeus")).thenReturn("content");
        when(wikipediaClient.getWikipediaPage("Ares")).thenReturn("content");
        when(wikipediaClient.getWikipediaPage("Athena")).thenReturn("content");

        // When
        GodAnalysisResponse result = godAnalysisService.findGodsWithMostLiterature();

        // Then - all tied, sorted alphabetically
        assertThat(result.gods()).containsExactly("Ares", "Athena", "Zeus");
        assertThat(result.characterCount()).isEqualTo(7L);
    }

    @Test
    void shouldTreatWikipediaFailuresAsZeroCount() {
        // Given
        when(greekGodsClient.getGreekGods()).thenReturn(List.of("Zeus", "Ares"));
        when(wikipediaClient.getWikipediaPage("Zeus")).thenReturn("z".repeat(100));
        when(wikipediaClient.getWikipediaPage("Ares")).thenThrow(new RuntimeException("Wikipedia timeout"));

        // When
        GodAnalysisResponse result = godAnalysisService.findGodsWithMostLiterature();

        // Then - Zeus wins, Ares gets 0 due to failure
        assertThat(result.gods()).containsExactly("Zeus");
        assertThat(result.characterCount()).isEqualTo(100L);
    }

    @Test
    void shouldTreatNullWikipediaResponseAsZeroCount() {
        // Given
        when(greekGodsClient.getGreekGods()).thenReturn(List.of("Zeus", "Ares"));
        when(wikipediaClient.getWikipediaPage("Zeus")).thenReturn("z".repeat(100));
        when(wikipediaClient.getWikipediaPage("Ares")).thenReturn(null);

        // When
        GodAnalysisResponse result = godAnalysisService.findGodsWithMostLiterature();

        // Then - Zeus wins, Ares gets 0 for null response
        assertThat(result.gods()).containsExactly("Zeus");
        assertThat(result.characterCount()).isEqualTo(100L);
    }

    @Test
    void shouldHandleAllWikipediaFailuresGracefully() {
        // Given
        when(greekGodsClient.getGreekGods()).thenReturn(List.of("Zeus", "Ares"));
        when(wikipediaClient.getWikipediaPage(any())).thenThrow(new RuntimeException("All Wikipedia down"));

        // When
        GodAnalysisResponse result = godAnalysisService.findGodsWithMostLiterature();

        // Then - all get 0, return all sorted
        assertThat(result.gods()).containsExactly("Ares", "Zeus");
        assertThat(result.characterCount()).isEqualTo(0L);
    }

    @Test
    void shouldHandleEmptyGodsListFromGreekAPI() {
        // Given
        when(greekGodsClient.getGreekGods()).thenReturn(List.of());

        // When
        GodAnalysisResponse result = godAnalysisService.findGodsWithMostLiterature();

        // Then
        assertThat(result.gods()).isEmpty();
        assertThat(result.characterCount()).isEqualTo(0L);
    }

    @Test
    void shouldPropagateGreekAPIFailures() {
        // Given
        when(greekGodsClient.getGreekGods()).thenThrow(new RuntimeException("Greek API down"));

        // When/Then - Greek failures should propagate (not handled like Wikipedia)
        assertThatThrownBy(() -> godAnalysisService.findGodsWithMostLiterature())
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Greek API down");
    }

    @Test
    void shouldProcessGodsInParallel() {
        // Given - large list to test parallel processing
        List<String> manyGods = List.of("Zeus", "Ares", "Athena", "Apollo", "Artemis", "Hera", "Poseidon", "Hades");
        when(greekGodsClient.getGreekGods()).thenReturn(manyGods);
        
        // Set up different character counts
        when(wikipediaClient.getWikipediaPage("Zeus")).thenReturn("z".repeat(800));
        when(wikipediaClient.getWikipediaPage("Ares")).thenReturn("a".repeat(100));
        when(wikipediaClient.getWikipediaPage("Athena")).thenReturn("t".repeat(200));
        when(wikipediaClient.getWikipediaPage("Apollo")).thenReturn("p".repeat(300));
        when(wikipediaClient.getWikipediaPage("Artemis")).thenReturn("r".repeat(400));
        when(wikipediaClient.getWikipediaPage("Hera")).thenReturn("h".repeat(500));
        when(wikipediaClient.getWikipediaPage("Poseidon")).thenReturn("o".repeat(600));
        when(wikipediaClient.getWikipediaPage("Hades")).thenReturn("d".repeat(700));

        // When
        long startTime = System.currentTimeMillis();
        GodAnalysisResponse result = godAnalysisService.findGodsWithMostLiterature();
        long duration = System.currentTimeMillis() - startTime;

        // Then
        assertThat(result.gods()).containsExactly("Zeus");
        assertThat(result.characterCount()).isEqualTo(800L);
        
        // Should be reasonably fast due to parallel execution
        // This is a rough check - in real parallel execution this should be much faster
        // than sequential processing of 8 gods
        assertThat(duration).isLessThan(1000L); // Should complete within 1 second
    }
}