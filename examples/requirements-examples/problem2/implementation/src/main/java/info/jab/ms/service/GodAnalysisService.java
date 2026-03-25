package info.jab.ms.service;

import info.jab.ms.client.GreekGodsClient;
import info.jab.ms.client.WikipediaClient;
import info.jab.ms.model.GodAnalysisResponse;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

@ApplicationScoped
public class GodAnalysisService {

    private static final Logger LOG = Logger.getLogger(GodAnalysisService.class);

    @RestClient
    GreekGodsClient greekGodsClient;

    @RestClient
    WikipediaClient wikipediaClient;

    public GodAnalysisResponse findGodsWithMostLiterature() {
        List<String> gods = greekGodsClient.getGreekGods();
        LOG.debugf("Fetched %d gods from Greek API", gods.size());

        Map<String, Long> counts = new ConcurrentHashMap<>();

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<CompletableFuture<Void>> futures = gods.stream()
                .map(god -> CompletableFuture.runAsync(() -> {
                    try {
                        String body = wikipediaClient.getWikipediaPage(god);
                        long count = body == null ? 0L : (long) body.length();
                        LOG.debugf("God %s: %d chars", god, count);
                        counts.put(god, count);
                    } catch (Exception e) {
                        LOG.warnf("Wikipedia lookup failed for %s: %s", god, e.getMessage());
                        counts.put(god, 0L);
                    }
                }, executor))
                .toList();

            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        }

        long max = counts.values().stream().mapToLong(Long::longValue).max().orElse(0L);
        List<String> winners = counts.entrySet().stream()
            .filter(e -> e.getValue() == max)
            .map(Map.Entry::getKey)
            .sorted()
            .toList();

        LOG.infof("Winners: %s with %d chars", winners, max);
        return new GodAnalysisResponse(winners, max);
    }
}
