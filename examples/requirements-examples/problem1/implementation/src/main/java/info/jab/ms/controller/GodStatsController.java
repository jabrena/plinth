package info.jab.ms.controller;

import info.jab.ms.dto.GodStatsResponse;
import info.jab.ms.exception.BadRequestException;
import info.jab.ms.service.GodAnalysisService;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class GodStatsController {

	private static final Logger log = LoggerFactory.getLogger(GodStatsController.class);

	private static final Set<String> ALLOWED_SOURCES = Set.of("greek", "roman", "nordic");

	private final GodAnalysisService godAnalysisService;

	public GodStatsController(GodAnalysisService godAnalysisService) {
		this.godAnalysisService = godAnalysisService;
	}

	@GetMapping("/gods/stats/sum")
	public GodStatsResponse getGodsStatsSum(
			@RequestParam(required = false) String filter,
			@RequestParam(required = false) String sources) {
		List<String> sourceKeys = validateAndParseSources(filter, sources);
		log.info("gods.stats.sum.request filter={} sources={}", filter, sources);
		GodStatsResponse response = new GodStatsResponse(godAnalysisService.sumForSources(filter, sourceKeys));
		log.info("gods.stats.sum.response sum={}", response.sum());
		return response;
	}

	private static List<String> validateAndParseSources(String filter, String sources) {
		if (filter == null) {
			throw new BadRequestException("Missing required query parameter: filter");
		}
		if (sources == null) {
			throw new BadRequestException("Missing required query parameter: sources");
		}
		if (filter.isEmpty()) {
			throw new BadRequestException("filter must be exactly one Unicode code point");
		}
		if (filter.codePointCount(0, filter.length()) != 1) {
			throw new BadRequestException("filter must be exactly one Unicode code point");
		}
		if (sources.isEmpty()) {
			throw new BadRequestException("sources must not be empty");
		}
		List<String> parts = Arrays.stream(sources.split(","))
				.map(String::trim)
				.filter(s -> !s.isEmpty())
				.toList();
		if (parts.isEmpty()) {
			throw new BadRequestException("sources must not be empty");
		}
		for (String part : parts) {
			if (!ALLOWED_SOURCES.contains(part)) {
				throw new BadRequestException("Invalid source name: " + part);
			}
		}
		return parts;
	}
}
