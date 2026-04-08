package info.jab.ms.algorithm;

import java.math.BigInteger;
import java.util.Objects;
import org.springframework.stereotype.Component;

@Component
public final class UnicodeAggregator {

    public BigInteger toBigInteger(String value) {
        Objects.requireNonNull(value, "value must not be null");
        return value.codePoints()
            .mapToObj(BigInteger::valueOf)
            .reduce(BigInteger.ZERO, BigInteger::add);
    }
}
