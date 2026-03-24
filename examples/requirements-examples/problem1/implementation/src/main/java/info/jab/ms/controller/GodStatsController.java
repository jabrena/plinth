package info.jab.ms.controller;

import info.jab.ms.dto.GodStatsResponse;
import info.jab.ms.service.GodAnalysisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1")
public class GodStatsController {

    private static final Logger log = LoggerFactory.getLogger(GodStatsController.class);
    private static final Set<String> VALID_SOURCES = Set.of("greek", "roman", "nordic");

    private final GodAnalysisService godAnalysisService;

    public GodStatsController(GodAnalysisService godAnalysisService) {
        this.godAnalysisService = godAnalysisService;
    }

    @GetMapping("/gods/stats/sum")
    public ResponseEntity<GodStatsResponse> getSum(
            @RequestParam(required = false) String filter,
            @RequestParam(required = false) String sources) {

        long start = System.currentTimeMillis();
        log.info("Incoming request: GET /api/v1/gods/stats/sum filter='{}' sources='{}'", filter, sources);

        validateFilter(filter);
        List<String> sourceList = validateAndParseSources(sources);

        BigInteger sum = godAnalysisService.computeSum(filter, sourceList);
        GodStatsResponse response = new GodStatsResponse(sum.toString());

        long duration = System.currentTimeMillis() - start;
        log.info("Response: sum='{}', duration={}ms", response.sum(), duration);

        return ResponseEntity.ok(response);
    }

    private void validateFilter(String filter) {
        if (filter == null || filter.isEmpty()) {
            throw new IllegalArgumentException("'filter' parameter is required and must be a single character");
        }
        if (filter.codePointCount(0, filter.length()) != 1) {
            throw new IllegalArgumentException("'filter' parameter must be exactly one character");
        }
    }

    private List<String> validateAndParseSources(String sources) {
        if (sources == null || sources.isBlank()) {
            throw new IllegalArgumentException("'sources' parameter is required");
        }
        List<String> sourceList = Arrays.stream(sources.split(","))
            .map(String::trim)
            .filter(s -> !s.isEmpty())
            .toList();
        if (sourceList.isEmpty()) {
            throw new IllegalArgumentException("'sources' parameter must contain at least one source");
        }
        for (String source : sourceList) {
            if (!VALID_SOURCES.contains(source.toLowerCase())) {
                throw new IllegalArgumentException("Invalid source: '" + source + "'. Valid sources are: greek, roman, nordic");
            }
        }
        return sourceList;
    }
}
