package info.jab.ms.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import info.jab.ms.api.model.PantheonSource;
import info.jab.ms.exception.BadRequestException;
import java.math.BigInteger;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

class GodAnalysisServiceTest {

    @Test
    void shouldParseSourcesAsUniqueSet() {
        var service = new GodAnalysisService(source -> List.of());

        var sources = service.parseSources("greek, roman, greek");

        assertThat(sources).containsExactlyInAnyOrder(PantheonSource.GREEK, PantheonSource.ROMAN);
    }

    @Test
    void shouldRejectUnsupportedSource() {
        var service = new GodAnalysisService(source -> List.of());

        assertThatThrownBy(() -> service.parseSources("greek,egyptian"))
            .isInstanceOf(BadRequestException.class)
            .hasMessageContaining("unsupported value: 'egyptian'");
    }

    @Test
    void shouldApplyCaseSensitiveFirstCharacterFilter() {
        var service = new GodAnalysisService(source -> List.of(
            new GodData("nyx", BigInteger.TEN),
            new GodData("Nyx", BigInteger.ONE)
        ));

        var lowercase = service.aggregateByFilter("n", "greek");
        var uppercase = service.aggregateByFilter("N", "greek");

        assertThat(lowercase).isEqualTo("10");
        assertThat(uppercase).isEqualTo("1");
    }

    @Test
    void shouldAggregateWithBigIntegerAndDeterministicMergeAcrossRuns() {
        var service = new GodAnalysisService(source -> switch (source) {
            case GREEK -> List.of(new GodData("nyx", new BigInteger("9999999999999999999")));
            case ROMAN -> List.of(new GodData("neptune", new BigInteger("8888888888888888888")));
            case NORDIC -> List.of(new GodData("njord", new BigInteger("7777777777777777777")));
        });

        var first = service.aggregateByFilter("n", "greek,roman,nordic");
        var second = service.aggregateByFilter("n", "nordic,greek,roman");

        assertThat(first).isEqualTo("26666666666666666664");
        assertThat(second).isEqualTo(first);
    }

    @Test
    void shouldFallbackToEmptySourceContributionWhenSourceFails() {
        var service = new GodAnalysisService(source -> {
            if (Set.of(PantheonSource.ROMAN).contains(source)) {
                throw new IllegalStateException("simulated-upstream-failure");
            }
            return List.of(new GodData("nyx", BigInteger.valueOf(5)));
        });

        var result = service.aggregateByFilter("n", "greek,roman");

        assertThat(result).isEqualTo("5");
    }

    @Test
    void shouldKeepA1HappyPathValue() {
        var service = new GodAnalysisService(new InMemoryPantheonDataSource());

        var result = service.aggregateByFilter("n", "greek,roman,nordic");

        assertThat(result).isEqualTo("78179288397447443426");
    }
}
