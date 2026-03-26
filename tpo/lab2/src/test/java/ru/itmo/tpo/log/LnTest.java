package ru.itmo.tpo.log;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Ln")
class LnTest {

    private static final double DELTA = 1e-9;
    private Ln ln;

    @BeforeEach
    void setUp() {
        ln = new Ln();
    }

    @DisplayName("Табличные значения")
    @ParameterizedTest(name = "ln({0}) = {1}")
    @CsvSource({
            "1.0,                    0.0",               // ln(1) = 0
            "2.718281828459045,      1.0",               // ln(e) = 1
            "2.0,                    0.6931471805599453", // ln(2)
            "10.0,                   2.302585092994046"  // ln(10)
    })
    void tableValues(double x, double expected) {
        assertEquals(expected, ln.calculate(x), DELTA);
    }

    @DisplayName("Недопустимые значения")
    @ParameterizedTest(name = "ln({0}) выбрасывает исключение")
    @ValueSource(doubles = {0.0, -1.0, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
    void domainViolationThrows(double x) {
        assertThrows(IllegalArgumentException.class, () -> ln.calculate(x));
    }

    @DisplayName("Знакопостоянство")
    @ParameterizedTest(name = "sign(ln({0})) = {1}")
    @CsvSource({
            "0.5, -1.0",    // (0, 1): ln < 0
            "2.0,  1.0"     // (1, +inf): ln > 0
    })
    void signConstancy(double x, double expectedSign) {
        double val = ln.calculate(x);
        double actualSign = val > 0 ? 1.0 : -1.0;
        assertEquals(expectedSign, actualSign);
    }

    @DisplayName("Монотонно возрастает")
    @ParameterizedTest(name = "ln({0}) < ln({1})")
    @CsvSource({
            "0.5, 1.0",    // (0, 1)
            "1.0, 2.0"     // (1, +inf)
    })
    void monotonicallyIncreasing(double x1, double x2) {
        assertTrue(ln.calculate(x1) < ln.calculate(x2));
    }
    
    @DisplayName("Производная")  // ln'(x) = 1/x
    @ParameterizedTest(name = "ln'({0}) ≈ 1/{0}")
    @ValueSource(doubles = {0.5, 1.0, 2.0, 10.0})
    void derivative(double x) {
        double h = 1e-7;
        double numerical = (ln.calculate(x + h) - ln.calculate(x - h)) / (2 * h);
        assertEquals(1.0 / x, numerical, 1e-5);
    }
}
