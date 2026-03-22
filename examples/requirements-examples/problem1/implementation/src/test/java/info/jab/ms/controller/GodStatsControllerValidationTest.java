package info.jab.ms.controller;

import info.jab.ms.client.GodDataClient;
import info.jab.ms.config.GodApiProperties;
import info.jab.ms.service.GodAnalysisService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Web slice test: loads only the web layer (no full application context, no WireMock).
 * {@link GodDataClient} is mocked so validation and happy-path math use the same stub data as WireMock JSON fixtures.
 */
@WebMvcTest(controllers = GodStatsController.class)
@Import({GlobalExceptionHandler.class, GodAnalysisService.class})
@EnableConfigurationProperties(GodApiProperties.class)
@TestPropertySource(properties = {
        "god-api.sources.greek.url=http://localhost/stub/greek",
        "god-api.sources.roman.url=http://localhost/stub/roman",
        "god-api.sources.nordic.url=http://localhost/stub/nordic"
})
@Tag("validation-test")
class GodStatsControllerValidationTest {

    /** Mirrors {@code __files/greek-gods.json} */
    private static final List<String> GREEK_NAMES = List.of(
            "Zeus", "Hera", "Poseidon", "Demeter", "Ares", "Athena", "Apollo", "Artemis",
            "Hephaestus", "Aphrodite", "Hermes", "Dionysus", "Hades", "Hypnos", "Nike", "Janus",
            "Nemesis", "Iris", "Hecate", "Tyche");
    /** Mirrors {@code __files/roman-gods.json} */
    private static final List<String> ROMAN_NAMES = List.of("Venus", "Mars", "Neptun", "Mercury", "Pluto", "Jupiter");
    /** Mirrors {@code __files/nordic-gods.json} */
    private static final List<String> NORDIC_NAMES = List.of(
            "Baldur", "Freyja", "Heimdall", "Frigga", "Hel", "Loki", "Njord", "Odin", "Thor", "Tyr");

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GodDataClient godDataClient;

    @BeforeEach
    void stubSuccessfulFetches() {
        when(godDataClient.fetchGodNames(anyString(), eq("greek"))).thenReturn(GREEK_NAMES);
        when(godDataClient.fetchGodNames(anyString(), eq("roman"))).thenReturn(ROMAN_NAMES);
        when(godDataClient.fetchGodNames(anyString(), eq("nordic"))).thenReturn(NORDIC_NAMES);
    }

    @Test
    void shouldReturnBadRequestForInvalidFilterLength() throws Exception {
        mockMvc.perform(get("/api/v1/gods/stats/sum")
                        .param("filter", "ab")
                        .param("sources", "greek,roman,nordic"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Filter must be exactly one character"));
    }

    @Test
    void shouldReturnBadRequestForEmptyFilter() throws Exception {
        mockMvc.perform(get("/api/v1/gods/stats/sum")
                        .param("filter", "")
                        .param("sources", "greek,roman,nordic"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Filter must be exactly one character"));
    }

    @Test
    void shouldReturnBadRequestForInvalidSource() throws Exception {
        mockMvc.perform(get("/api/v1/gods/stats/sum")
                        .param("filter", "n")
                        .param("sources", "greek,invalid,nordic"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Invalid source: 'invalid'. Valid sources are: [greek, roman, nordic]"));
    }

    @Test
    void shouldReturnBadRequestForDuplicateSources() throws Exception {
        mockMvc.perform(get("/api/v1/gods/stats/sum")
                        .param("filter", "n")
                        .param("sources", "greek,roman,greek"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Duplicate sources are not allowed"));
    }

    @Test
    void shouldReturnBadRequestForEmptySources() throws Exception {
        mockMvc.perform(get("/api/v1/gods/stats/sum")
                        .param("filter", "n")
                        .param("sources", "   "))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Sources parameter cannot be null or empty"));
    }

    @Test
    void shouldHandleSourcesWithWhitespace() throws Exception {
        mockMvc.perform(get("/api/v1/gods/stats/sum")
                        .param("filter", "n")
                        .param("sources", " greek , roman , nordic "))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.sum").value("78179288397447443426"));
    }

    @Test
    void shouldHandleCaseInsensitiveSources() throws Exception {
        mockMvc.perform(get("/api/v1/gods/stats/sum")
                        .param("filter", "n")
                        .param("sources", "GREEK,Roman,nordic"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.sum").value("78179288397447443426"));
    }

    @Test
    void shouldHandlePartialSources() throws Exception {
        mockMvc.perform(get("/api/v1/gods/stats/sum")
                        .param("filter", "n")
                        .param("sources", "greek,roman"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.sum").value("78179210291336329326"));
    }
}
