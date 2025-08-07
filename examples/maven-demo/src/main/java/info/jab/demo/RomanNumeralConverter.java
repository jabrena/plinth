package info.jab.demo;

import java.util.Objects;

/**
 * A utility class for converting numerical values to Roman numeral syntax.
 *
 * This class provides functionality to convert positive integers (1-3999)
 * into their corresponding Roman numeral representation.
 *
 * Roman numeral rules:
 * - I = 1, V = 5, X = 10, L = 50, C = 100, D = 500, M = 1000
 * - Subtractive notation: IV = 4, IX = 9, XL = 40, XC = 90, CD = 400, CM = 900
 */
public class RomanNumeralConverter {

    // Arrays for mapping values to Roman numerals in descending order
    private static final int[] VALUES = {
        1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1
    };

    private static final String[] NUMERALS = {
        "M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"
    };

    /**
     * Converts a positive integer to its Roman numeral representation.
     *
     * @param number the integer to convert (must be between 1 and 3999 inclusive)
     * @return the Roman numeral representation as a String
     * @throws IllegalArgumentException if the number is not in the valid range (1-3999)
     */
    public String toRoman(int number) {
        if (number < 1 || number > 3999) {
            throw new IllegalArgumentException("Number must be between 1 and 3999, but was: " + number);
        }

        StringBuilder result = new StringBuilder();

        // Iterate through the values array
        for (int i = 0; i < VALUES.length; i++) {
            // While the number is greater than or equal to the current value
            while (number >= VALUES[i]) {
                // Append the corresponding numeral and subtract the value
                result.append(NUMERALS[i]);
                number -= VALUES[i];
            }
        }

        return result.toString();
    }

    /**
     * Converts an Integer object to its Roman numeral representation.
     * This is a convenience method that handles null values.
     *
     * @param number the Integer to convert (must be between 1 and 3999 inclusive, or null)
     * @return the Roman numeral representation as a String, or empty string if input is null
     * @throws IllegalArgumentException if the number is not in the valid range (1-3999)
     */
    public String toRoman(Integer number) {
        if (Objects.isNull(number)) {
            return "";
        }
        return toRoman(number.intValue());
    }
}
