package info.jab.ms.gods.controller;

import info.jab.ms.gods.service.GreekGodsService;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/gods")
public class GreekGodsController {

	private final GreekGodsService greekGodsService;

	public GreekGodsController(GreekGodsService greekGodsService) {
		this.greekGodsService = greekGodsService;
	}

	@GetMapping(value = "/greek", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<String> getGreekGods() {
		return greekGodsService.findAllNamesOrdered();
	}
}
