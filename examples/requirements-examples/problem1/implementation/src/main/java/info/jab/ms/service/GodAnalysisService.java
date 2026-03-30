package info.jab.ms.service;

import info.jab.ms.algorithm.UnicodeAggregator;
import info.jab.ms.client.GodDataClient;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class GodAnalysisService {

	private static final Logger log = LoggerFactory.getLogger(GodAnalysisService.class);

	private final GodDataClient godDataClient;
	private final Executor godAnalysisExecutor;

	public GodAnalysisService(
			GodDataClient godDataClient, @Qualifier("godAnalysisExecutor") Executor godAnalysisExecutor) {
		this.godDataClient = godDataClient;
		this.godAnalysisExecutor = godAnalysisExecutor;
	}

	/**
	 * @param filter single Unicode code point (caller validates)
	 * @param pantheonKeys non-empty list of known pantheon keys
	 */
	public String sumForSources(String filter, List<String> pantheonKeys) {
		List<CompletableFuture<List<String>>> futures = pantheonKeys.stream()
				.map(key -> CompletableFuture.supplyAsync(() -> godDataClient.fetchNames(key), godAnalysisExecutor))
				.toList();
		List<String> merged = new ArrayList<>();
		for (CompletableFuture<List<String>> future : futures) {
			merged.addAll(future.join());
		}
		BigInteger sum = UnicodeAggregator.sumFiltered(filter, merged);
		log.info(
				"gods.analysis.sum filter={} sources={} nameCount={} sum={}",
				filter,
				pantheonKeys,
				merged.size(),
				sum.toString());
		return sum.toString();
	}
}
