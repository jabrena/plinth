package info.jab.ms.algorithm;

import java.math.BigInteger;
import java.util.List;

public class UnicodeAggregator {

    public BigInteger aggregate(List<String> names, String filter) {
        int filterCodePoint = filter.codePointAt(0);
        return names.stream()
            .filter(name -> !name.isEmpty() && name.codePointAt(0) == filterCodePoint)
            .map(this::toUnicodeDigitString)
            .map(BigInteger::new)
            .reduce(BigInteger.ZERO, BigInteger::add);
    }

    private String toUnicodeDigitString(String name) {
        StringBuilder sb = new StringBuilder();
        name.codePoints().forEach(cp -> sb.append(cp));
        return sb.toString();
    }
}
