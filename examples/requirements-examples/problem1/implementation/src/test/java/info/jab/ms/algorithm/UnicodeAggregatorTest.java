package info.jab.ms.algorithm;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigInteger;
import org.junit.jupiter.api.Test;

class UnicodeAggregatorTest {

    private final UnicodeAggregator unicodeAggregator = new UnicodeAggregator();

    @Test
    void shouldConvertStringToDeterministicBigIntegerSum() {
        var result = unicodeAggregator.toBigInteger("nA");

        assertThat(result).isEqualTo(BigInteger.valueOf('n' + 'A'));
    }

    @Test
    void shouldHandleNonAsciiCharacters() {
        var result = unicodeAggregator.toBigInteger("ñ");

        assertThat(result).isEqualTo(BigInteger.valueOf("ñ".codePointAt(0)));
    }

    @Test
    void shouldRejectNullInput() {
        assertThatThrownBy(() -> unicodeAggregator.toBigInteger(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("value must not be null");
    }
}
