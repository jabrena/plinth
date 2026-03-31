package info.jab.ms.algorithm;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigInteger;
import org.junit.jupiter.api.Test;

class UnicodeAggregatorTest {

	@Test
	void zeusExampleMatchesPinnedDecimalConcatenation() {
		assertThat(UnicodeAggregator.nameToBigInteger("Zeus")).isEqualTo(new BigInteger("90101117115"));
	}

	@Test
	void sumFilteredMatchesLowercaseFilterAgainstUppercaseNNames() {
		var names = java.util.List.of("Nike", "Nemesis", "Neptun", "Njord");
		assertThat(UnicodeAggregator.sumFiltered("n", names).toString()).isEqualTo("78179288397447443426");
	}

	@Test
	void sumFilteredReturnsZeroWhenNoNameMatches() {
		assertThat(UnicodeAggregator.sumFiltered("n", java.util.List.of("zeus", "apollo"))).isEqualTo(BigInteger.ZERO);
	}
}
