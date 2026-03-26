package info.jab.ms.gods.controller;

import info.jab.ms.gods.service.GreekGodsService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GreekGodsController.class)
class GreekGodsControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private GreekGodsService greekGodsService;

	@Test
	void getGreekGods_returnsJsonArrayFromService() throws Exception {
		when(greekGodsService.findAllNamesOrdered()).thenReturn(List.of("Athena", "Zeus"));

		mockMvc.perform(get("/api/v1/gods/greek").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[0]").value("Athena"))
				.andExpect(jsonPath("$[1]").value("Zeus"));

		verify(greekGodsService).findAllNamesOrdered();
	}

	@Test
	void getGreekGods_returnsEmptyArrayWhenNoGods() throws Exception {
		when(greekGodsService.findAllNamesOrdered()).thenReturn(List.of());

		mockMvc.perform(get("/api/v1/gods/greek"))
				.andExpect(status().isOk())
				.andExpect(content().json("[]"));
	}
}
