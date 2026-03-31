package info.jab.ms.algorithm;

import java.math.BigInteger;

/**
 * Concatenates decimal digits of each Unicode code point in a name, parses to {@link BigInteger},
 * and sums matching names. First-code-point filter uses case-insensitive comparison so the pinned
 * acceptance sum aligns with upstream data (e.g. {@code filter=n} matches {@code Nike}).
 */
public final class UnicodeAggregator {

	private UnicodeAggregator() {}

	public static BigInteger sumFiltered(String filter, Iterable<String> names) {
		if (filter == null || filter.isEmpty()) {
			return BigInteger.ZERO;
		}
		int filterCp = filter.codePointAt(0);
		BigInteger total = BigInteger.ZERO;
		for (String name : names) {
			if (name == null || name.isEmpty()) {
				continue;
			}
			int firstCp = name.codePointAt(0);
			if (!firstCodePointMatchesFilter(firstCp, filterCp)) {
				continue;
			}
			total = total.add(nameToBigInteger(name));
		}
		return total;
	}

	static boolean firstCodePointMatchesFilter(int nameFirstCodePoint, int filterCodePoint) {
		return Character.toLowerCase(nameFirstCodePoint) == Character.toLowerCase(filterCodePoint);
	}

	static BigInteger nameToBigInteger(String name) {
		StringBuilder digits = new StringBuilder();
		name.codePoints().forEach(cp -> digits.append(Integer.toString(cp)));
		return new BigInteger(digits.toString());
	}
}
