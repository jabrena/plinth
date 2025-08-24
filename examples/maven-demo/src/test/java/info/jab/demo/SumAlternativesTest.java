package info.jab.demo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SumAlternativesTest {

    @ParameterizedTest
    @DisplayName("All approaches should yield the same sum")
    @ValueSource(ints = {0, 1, 2, 3, 10, 100, 1000})
    void allApproachesReturnSameResult(int n) {
        int expected = SumAlternatives.sumWithFormula(n);

        assertEquals(expected, SumAlternatives.sumWithForLoop(n));
        assertEquals(expected, SumAlternatives.sumWithWhileLoop(n));
        assertEquals(expected, SumAlternatives.sumWithRecursion(n));
        assertEquals(expected, SumAlternatives.sumWithStreams(n));
        assertEquals(expected, SumAlternatives.sumWithEnhancedFor(n));
        assertEquals(expected, SumAlternatives.sumWithCollections(n));
        assertEquals(expected, SumAlternatives.sumWithTailRecursion(n));
    }
}
