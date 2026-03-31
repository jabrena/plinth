package info.jab.ms.controller;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import info.jab.ms.service.GodAnalysisService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = GodStatsController.class)
@Import(GlobalExceptionHandler.class)
class GodStatsControllerErrorHandlingTest {

	@MockitoBean
	private GodAnalysisService godAnalysisService;

	@Autowired
	private MockMvc mockMvc;

	@Test
	void missingFilterReturns400WithMessage() throws Exception {
		mockMvc.perform(get("/api/v1/gods/stats/sum").param("sources", "greek,roman,nordic"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value(containsString("filter")));
	}

	@Test
	void missingSourcesReturns400WithMessage() throws Exception {
		mockMvc.perform(get("/api/v1/gods/stats/sum").param("filter", "n"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value(containsString("sources")));
	}

	@Test
	void emptyFilterReturns400WithMessage() throws Exception {
		mockMvc.perform(get("/api/v1/gods/stats/sum")
						.param("filter", "")
						.param("sources", "greek,roman,nordic"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").exists());
	}

	@Test
	void multiCharFilterReturns400WithMessage() throws Exception {
		mockMvc.perform(get("/api/v1/gods/stats/sum")
						.param("filter", "abc")
						.param("sources", "greek,roman,nordic"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").exists());
	}

	@Test
	void emptySourcesReturns400WithMessage() throws Exception {
		mockMvc.perform(get("/api/v1/gods/stats/sum").param("filter", "n").param("sources", ""))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").exists());
	}

	@Test
	void invalidSourceNamesReturn400WithMessage() throws Exception {
		mockMvc.perform(get("/api/v1/gods/stats/sum")
						.param("filter", "n")
						.param("sources", "invalid,unknown"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value(containsString("Invalid source")));
	}
}
