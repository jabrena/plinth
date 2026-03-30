package info.jab.ms.service;

import info.jab.ms.algorithm.UnicodeAggregator;
import info.jab.ms.client.GodDataClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
public class GodAnalysisService {

    private static final Logger log = LoggerFactory.getLogger(GodAnalysisService.class);

    private final GodDataClient godDataClient;
    private final UnicodeAggregator unicodeAggregator;

    public GodAnalysisService(GodDataClient godDataClient) {
        this.godDataClient = godDataClient;
        this.unicodeAggregator = new UnicodeAggregator();
    }

    public BigInteger computeSum(String filter, List<String> sources) {
        log.debug("Computing sum for filter='{}', sources={}", filter, sources);
        List<String> allNames = new ArrayList<>();
        for (String source : sources) {
            List<String> names = switch (source.toLowerCase()) {
                case "greek" -> godDataClient.fetchGreekGods();
                case "roman" -> godDataClient.fetchRomanGods();
                case "nordic" -> godDataClient.fetchNordicGods();
                default -> {
                    log.warn("Unknown source: {}", source);
                    yield List.of();
                }
            };
            log.debug("Source '{}' returned {} names", source, names.size());
            allNames.addAll(names);
        }
        BigInteger sum = unicodeAggregator.aggregate(allNames, filter);
        log.info("Computed sum for filter='{}': {}", filter, sum);
        return sum;
    }
}
