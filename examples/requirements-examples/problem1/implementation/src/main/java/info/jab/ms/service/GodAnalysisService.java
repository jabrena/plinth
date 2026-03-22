package info.jab.ms.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

/**
 * Service for analyzing god names and calculating Unicode-based statistics.
 * Implements the Unicode aggregation algorithm as specified in the requirements.
 */
@Service
public class GodAnalysisService {

    private static final Logger logger = LoggerFactory.getLogger(GodAnalysisService.class);

    /**
     * Calculate the sum of Unicode values for god names with optional filtering.
     *
     * Algorithm:
     * 1. Filter names by first Unicode code point (filter is normalized to uppercase before comparison)
     * 2. For each name, convert each character to its Unicode integer value
     * 3. Concatenate those integers as strings per name
     * 4. Sum all the concatenated values as BigInteger
     * 5. Return the sum as a string
     *
     * @param names  List of god names to process
     * @param filter Filter for first character, normalized to uppercase before comparison (empty string or null means no filter)
     * @return Sum of Unicode values as a string
     */
    public String calculateSum(List<String> names, String filter) {
        logger.debug("calculateSum called with {} names, filter: '{}'", names == null ? 0 : names.size(), filter);

        if (names == null || names.isEmpty()) {
            return "0";
        }

        BigInteger totalSum = BigInteger.ZERO;

        for (String name : names) {
            if (name == null || name.isEmpty()) {
                continue;
            }

            if (shouldFilterName(name, filter)) {
                continue;
            }

            String unicodeDecimalString = convertNameToUnicodeDecimalString(name);
            if (!unicodeDecimalString.isEmpty()) {
                BigInteger nameValue = new BigInteger(unicodeDecimalString);
                totalSum = totalSum.add(nameValue);
            }
        }

        logger.debug("calculateSum result: '{}' for filter: '{}'", totalSum, filter);
        return totalSum.toString();
    }

    /**
     * Check if a name should be filtered out based on the filter criteria.
     * The filter's first code point is normalized to uppercase before comparing against
     * the name's first code point, so that a lowercase filter matches capitalized god names.
     *
     * @param name   The name to check
     * @param filter The filter string (empty/null means no filtering)
     * @return true if the name should be filtered out, false if it should be included
     */
    private boolean shouldFilterName(String name, String filter) {
        if (filter == null || filter.isEmpty()) {
            return false;
        }

        int firstCodePoint = name.codePointAt(0);
        int filterCodePoint = filter.codePointAt(0);

        // Normalize filter to uppercase so that e.g. filter='n' matches names starting with 'N'
        return firstCodePoint != Character.toUpperCase(filterCodePoint);
    }

    /**
     * Convert a name to its Unicode decimal string representation.
     * Each character is converted to its Unicode integer value and concatenated as strings.
     *
     * @param name The name to convert
     * @return The concatenated Unicode decimal string
     */
    private String convertNameToUnicodeDecimalString(String name) {
        StringBuilder unicodeBuilder = new StringBuilder();
        name.codePoints().forEach(unicodeBuilder::append);
        return unicodeBuilder.toString();
    }
}