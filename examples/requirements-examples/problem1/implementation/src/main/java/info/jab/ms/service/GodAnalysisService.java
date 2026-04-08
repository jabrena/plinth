package info.jab.ms.service;

import info.jab.ms.api.model.PantheonSource;
import info.jab.ms.exception.BadRequestException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GodAnalysisService {

    private static final Logger log = LoggerFactory.getLogger(GodAnalysisService.class);

    private final PantheonDataSource pantheonDataSource;
    private final Executor domainExecutor;

    @Autowired
    public GodAnalysisService(PantheonDataSource pantheonDataSource) {
        this(pantheonDataSource, CompletableFuture.delayedExecutor(0, TimeUnit.MILLISECONDS));
    }

    GodAnalysisService(PantheonDataSource pantheonDataSource, Executor domainExecutor) {
        this.pantheonDataSource = pantheonDataSource;
        this.domainExecutor = domainExecutor;
    }

    public String aggregateByFilter(String filter, String sources) {
        var parsedSources = parseSources(sources);
        var selectedFilter = filter.charAt(0);

        var futures = parsedSources.stream()
            .sorted(Comparator.comparing(Enum::name))
            .map(source -> CompletableFuture.supplyAsync(() -> fetchSourceData(source), domainExecutor))
            .toList();

        var sum = futures.stream()
            .map(CompletableFuture::join)
            .flatMap(List::stream)
            .filter(godData -> godData.name().startsWith(String.valueOf(selectedFilter)))
            .map(GodData::score)
            .reduce(BigInteger.ZERO, BigInteger::add);

        return sum.toString();
    }

    Set<PantheonSource> parseSources(String sources) {
        return Arrays.stream(sources.split(","))
            .map(String::trim)
            .map(this::parseSingleSource)
            .collect(Collectors.toSet());
    }

    private PantheonSource parseSingleSource(String value) {
        try {
            return PantheonSource.fromValue(value);
        } catch (IllegalArgumentException exception) {
            throw new BadRequestException(
                "INVALID_SOURCE",
                "Query parameter 'sources' contains unsupported value: '" + value + "'."
            );
        }
    }

    private List<GodData> fetchSourceData(PantheonSource source) {
        log.info("source_execution_started source={}", source.getValue());
        try {
            var result = List.copyOf(pantheonDataSource.fetch(source));
            log.info("source_execution_completed source={} size={}", source.getValue(), result.size());
            return result;
        } catch (RuntimeException ex) {
            log.warn("source_execution_fallback source={} reason={}", source.getValue(), ex.getMessage());
            return List.of();
        }
    }
}
