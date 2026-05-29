package info.jab.ms.controller;

import info.jab.ms.dto.SumRequest;
import info.jab.ms.dto.SumResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class SumController {

	@PostMapping("/sum")
	public SumResponse sum(@Valid @RequestBody SumRequest request) {
		return new SumResponse(request.param1() + request.param2());
	}

}
