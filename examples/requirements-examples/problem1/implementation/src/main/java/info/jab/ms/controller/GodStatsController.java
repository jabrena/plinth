package info.jab.ms.controller;

import info.jab.ms.client.GodDataClient;
import info.jab.ms.config.GodApiProperties;
import info.jab.ms.dto.GodStatsResponse;
import info.jab.ms.service.GodAnalysisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * REST controller for god statistics operations.
 * Provides endpoints for calculating aggregated statistics from mythology data sources.
 */
@RestController
@RequestMapping("/api/v1")
public class GodStatsController {

    private static final Logger logger = LoggerFactory.getLogger(GodStatsController.class);

    private final GodAnalysisService godAnalysisService;
    private final GodDataClient godDataClient;
    private final GodApiProperties godApiProperties;

    public GodStatsController(GodAnalysisService godAnalysisService,
                               GodDataClient godDataClient,
                               GodApiProperties godApiProperties) {
        this.godAnalysisService = godAnalysisService;
        this.godDataClient = godDataClient;
        this.godApiProperties = godApiProperties;
    }

    /**
     * Calculate the sum of Unicode values for god names from specified sources.
     *
     * @param filter  Case-sensitive filter for god names (single character)
     * @param sources Comma-separated list of mythology sources (greek,roman,nordic)
     * @return JSON response containing the calculated sum as a string
     */
    @GetMapping("/gods/stats/sum")
    public ResponseEntity<GodStatsResponse> calculateSum(
            @RequestParam(value = "filter", defaultValue = "") String filter,
            @RequestParam(value = "sources", defaultValue = "greek,roman,nordic") String sources) {

        String requestId = UUID.randomUUID().toString();
        long startTime = System.currentTimeMillis();

        MDC.put("requestId", requestId);
        MDC.put("operation", "calculateSum");
        MDC.put("timestamp", Instant.now().toString());

        try {
            validateInputParameters(filter, sources, requestId);

            List<String> sourcesList = parseAndValidateSources(sources, requestId);

            logger.info("Request started - requestId: {}, filter: '{}', sources: {}, sourcesCount: {}",
                    requestId, filter, sourcesList, sourcesList.size());

            List<String> godNames = getGodNamesForSources(sourcesList, requestId);

            String calculatedSum = godAnalysisService.calculateSum(godNames, filter);

            logger.info("Service calculation completed - requestId: {}, godNamesCount: {}, filter: '{}', result: '{}'",
                    requestId, godNames.size(), filter, calculatedSum);

            GodStatsResponse response = new GodStatsResponse(calculatedSum);
            long duration = System.currentTimeMillis() - startTime;

            logger.info("Request completed successfully - requestId: {}, sum: '{}', duration: {}ms",
                    requestId, calculatedSum, duration);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            logger.error("Request failed - requestId: {}, error: {}, duration: {}ms",
                    requestId, e.getMessage(), duration, e);
            throw e;
        } finally {
            MDC.clear();
        }
    }

    private void validateInputParameters(String filter, String sources, String requestId) {
        if (filter == null) {
            throw new IllegalArgumentException("Filter parameter cannot be null");
        }

        if (filter.isEmpty() || filter.codePointCount(0, filter.length()) != 1) {
            logger.warn("Invalid filter - requestId: {}, filter: '{}', length: {}",
                    requestId, filter, filter.length());
            throw new IllegalArgumentException("Filter must be exactly one character");
        }

        if (sources == null || sources.trim().isEmpty()) {
            throw new IllegalArgumentException("Sources parameter cannot be null or empty");
        }
    }

    private List<String> parseAndValidateSources(String sources, String requestId) {
        List<String> sourcesList = new ArrayList<>(Arrays.asList(sources.split(",")));
        List<String> validSources = Arrays.asList("greek", "roman", "nordic");

        for (int i = 0; i < sourcesList.size(); i++) {
            String source = sourcesList.get(i).trim().toLowerCase();
            sourcesList.set(i, source);

            if (!validSources.contains(source)) {
                logger.warn("Invalid source specified - requestId: {}, source: '{}', validSources: {}",
                        requestId, source, validSources);
                throw new IllegalArgumentException("Invalid source: '" + source + "'. Valid sources are: " + validSources);
            }
        }

        if (sourcesList.size() != sourcesList.stream().distinct().count()) {
            logger.warn("Duplicate sources detected - requestId: {}, sources: {}", requestId, sourcesList);
            throw new IllegalArgumentException("Duplicate sources are not allowed");
        }

        return sourcesList;
    }

    private List<String> getGodNamesForSources(List<String> sources, String requestId) {
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            Map<String, CompletableFuture<List<String>>> futures = new LinkedHashMap<>();
            for (String source : sources) {
                String url = godApiProperties.getSources().get(source).getUrl();
                futures.put(source, CompletableFuture.supplyAsync(
                        () -> godDataClient.fetchGodNames(url, source), executor
                ));
            }
            List<String> allNames = new ArrayList<>();
            for (Map.Entry<String, CompletableFuture<List<String>>> entry : futures.entrySet()) {
                try {
                    List<String> names = entry.getValue().get();
                    allNames.addAll(names);
                    logger.info("Source '{}' contributed {} names - requestId: {}", entry.getKey(), names.size(), requestId);
                } catch (Exception e) {
                    logger.warn("Source '{}' failed - requestId: {}, error: {}", entry.getKey(), requestId, e.getMessage());
                }
            }
            return allNames;
        }
    }
}
