package info.jab.ms.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(SumController.class)
@Import(ValidationExceptionHandler.class)
class SumControllerValidationTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void sumReturns400WhenParam1IsMissing() throws Exception {
		mockMvc.perform(post("/api/v1/sum")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"param2\": 1}"))
				.andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
				.andExpect(jsonPath("$.type").value("https://example.com/problems/validation-error"))
				.andExpect(jsonPath("$.title").value("Validation Error"))
				.andExpect(jsonPath("$.status").value(400))
				.andExpect(jsonPath("$.violations[0]").value("param1: must not be null"));
	}

	@Test
	void sumReturns400WhenParam2IsNull() throws Exception {
		mockMvc.perform(post("/api/v1/sum")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"param1\": 1, \"param2\": null}"))
				.andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
				.andExpect(jsonPath("$.type").value("https://example.com/problems/validation-error"))
				.andExpect(jsonPath("$.title").value("Validation Error"))
				.andExpect(jsonPath("$.status").value(400))
				.andExpect(jsonPath("$.violations[0]").value("param2: must not be null"));
	}

	@Test
	void sumReturns200WhenRequestIsValid() throws Exception {
		mockMvc.perform(post("/api/v1/sum")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"param1\": 10, \"param2\": 32}"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.result").value(42));
	}

}
