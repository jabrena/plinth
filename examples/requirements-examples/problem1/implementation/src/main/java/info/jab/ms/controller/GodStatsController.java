package info.jab.ms.controller;

import info.jab.ms.api.model.GodStatsSumResponse;
import info.jab.ms.exception.BadRequestException;
import info.jab.ms.service.GodAnalysisService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
class GodStatsController {

    private final GodAnalysisService godAnalysisService;

    GodStatsController(GodAnalysisService godAnalysisService) {
        this.godAnalysisService = godAnalysisService;
    }

    @GetMapping(path = "/api/v1/gods/stats/sum", produces = "application/json")
    ResponseEntity<GodStatsSumResponse> getGodsStatsSum(
        @RequestParam String filter,
        @RequestParam String sources
    ) {
        if (filter.length() != 1) {
            throw new BadRequestException("INVALID_FILTER",
                "Query parameter 'filter' must contain exactly one character.");
        }

        var sum = godAnalysisService.aggregateByFilter(filter, sources);
        return ResponseEntity.ok(new GodStatsSumResponse(sum));
    }
}
