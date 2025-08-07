package info.jab.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class RomanNumeralConverterTest {

    private RomanNumeralConverter converter;

    @BeforeEach
    void setUp() {
        converter = new RomanNumeralConverter();
    }

    @Test
    @DisplayName("Should convert single digit numbers correctly")
    void testSingleDigitNumbers() {
        assertEquals("I", converter.toRoman(1));
        assertEquals("II", converter.toRoman(2));
        assertEquals("III", converter.toRoman(3));
        assertEquals("IV", converter.toRoman(4));
        assertEquals("V", converter.toRoman(5));
        assertEquals("VI", converter.toRoman(6));
        assertEquals("VII", converter.toRoman(7));
        assertEquals("VIII", converter.toRoman(8));
        assertEquals("IX", converter.toRoman(9));
    }

    @Test
    @DisplayName("Should convert tens correctly")
    void testTens() {
        assertEquals("X", converter.toRoman(10));
        assertEquals("XX", converter.toRoman(20));
        assertEquals("XXX", converter.toRoman(30));
        assertEquals("XL", converter.toRoman(40));
        assertEquals("L", converter.toRoman(50));
        assertEquals("LX", converter.toRoman(60));
        assertEquals("LXX", converter.toRoman(70));
        assertEquals("LXXX", converter.toRoman(80));
        assertEquals("XC", converter.toRoman(90));
    }

    @Test
    @DisplayName("Should convert hundreds correctly")
    void testHundreds() {
        assertEquals("C", converter.toRoman(100));
        assertEquals("CC", converter.toRoman(200));
        assertEquals("CCC", converter.toRoman(300));
        assertEquals("CD", converter.toRoman(400));
        assertEquals("D", converter.toRoman(500));
        assertEquals("DC", converter.toRoman(600));
        assertEquals("DCC", converter.toRoman(700));
        assertEquals("DCCC", converter.toRoman(800));
        assertEquals("CM", converter.toRoman(900));
    }

    @Test
    @DisplayName("Should convert thousands correctly")
    void testThousands() {
        assertEquals("M", converter.toRoman(1000));
        assertEquals("MM", converter.toRoman(2000));
        assertEquals("MMM", converter.toRoman(3000));
    }

    @ParameterizedTest
    @DisplayName("Should convert common numbers correctly")
    @CsvSource({
        "11, XI",
        "14, XIV",
        "19, XIX",
        "27, XXVII",
        "48, XLVIII",
        "59, LIX",
        "93, XCIII",
        "141, CXLI",
        "163, CLXIII",
        "402, CDII",
        "575, DLXXV",
        "911, CMXI",
        "1024, MXXIV",
        "3000, MMM",
        "3999, MMMCMXCIX"
    })
    void testCommonNumbers(int number, String expectedRoman) {
        assertEquals(expectedRoman, converter.toRoman(number));
    }

    @Test
    @DisplayName("Should handle edge cases correctly")
    void testEdgeCases() {
        assertEquals("I", converter.toRoman(1));
        assertEquals("MMMCMXCIX", converter.toRoman(3999));
    }

    @ParameterizedTest
    @DisplayName("Should throw exception for invalid numbers")
    @ValueSource(ints = {0, -1, -10, 4000, 5000, 10000})
    void testInvalidNumbers(int invalidNumber) {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> converter.toRoman(invalidNumber)
        );
        assertTrue(exception.getMessage().contains("Number must be between 1 and 3999"));
    }

    @Test
    @DisplayName("Should handle Integer wrapper correctly")
    void testIntegerWrapper() {
        assertEquals("V", converter.toRoman(Integer.valueOf(5)));
        assertEquals("X", converter.toRoman(Integer.valueOf(10)));
        assertEquals("MMMDCCCLXXXVIII", converter.toRoman(Integer.valueOf(3888)));
    }

    @Test
    @DisplayName("Should handle null Integer input")
    void testNullInput() {
        assertEquals("", converter.toRoman((Integer) null));
    }

    @Test
    @DisplayName("Should throw exception for invalid Integer wrapper")
    void testInvalidIntegerWrapper() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> converter.toRoman(Integer.valueOf(4000))
        );
        assertTrue(exception.getMessage().contains("Number must be between 1 and 3999"));
    }

    @Test
    @DisplayName("Should handle complex subtractive cases")
    void testSubtractiveCases() {
        assertEquals("CDXLIV", converter.toRoman(444));  // 400 + 40 + 4
        assertEquals("CMXC", converter.toRoman(990));    // 900 + 90
        assertEquals("MCDXLIV", converter.toRoman(1444)); // 1000 + 400 + 40 + 4
        assertEquals("MCMXC", converter.toRoman(1990));   // 1000 + 900 + 90
    }
}
