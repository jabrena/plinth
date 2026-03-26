package info.jab.ms.gods.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GlobalExceptionHandlerTest {

	private MockMvc mockMvc;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(new ThrowingController())
				.setControllerAdvice(new GlobalExceptionHandler())
				.build();
	}

	@Test
	void dataAccessException_returnsProblemJson() throws Exception {
		mockMvc.perform(get("/test/data-access"))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.status").value(500))
				.andExpect(jsonPath("$.title").value("Database access failed"))
				.andExpect(jsonPath("$.type").value("urn:problem:greek-gods:data-access"));
	}

	@Test
	void cannotCreateTransaction_returnsProblemJson() throws Exception {
		mockMvc.perform(get("/test/transaction"))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.status").value(500))
				.andExpect(jsonPath("$.title").value("Database access failed"));
	}

	@Test
	void problemResponseUsesProblemJsonContentType() throws Exception {
		mockMvc.perform(get("/test/data-access"))
				.andExpect(status().isInternalServerError())
				.andExpect(result ->
						MediaType.parseMediaType("application/problem+json")
								.equals(result.getResponse().getContentType()));
	}

	@RestController
	static class ThrowingController {

		@GetMapping("/test/data-access")
		void dataAccess() {
			throw new DataIntegrityViolationException("simulated");
		}

		@GetMapping("/test/transaction")
		void transaction() {
			throw new CannotCreateTransactionException("simulated", new RuntimeException("cause"));
		}
	}
}
