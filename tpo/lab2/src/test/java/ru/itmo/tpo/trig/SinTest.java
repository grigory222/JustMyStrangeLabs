package ru.itmo.tpo.trig;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Sin")
class SinTest {

    private static final double DELTA = 1e-9;
    private static final double PI = 3.14159265358979323846;
    private Sin sin;

    @BeforeEach
    void setUp() {
        sin = new Sin();
    }

    @DisplayName("Табличные значения")
    @ParameterizedTest(name = "sin({0}) = {1}")
    @CsvSource({
            " 0.5235987755982988,     0.5",                   // pi/6
            " 0.7853981633974483,     0.7071067811865476",    // pi/4
            " 1.0471975511965976,     0.8660254037844387",    // pi/3
            "-0.5235987755982988,    -0.5",                   // -pi/6
            "-0.7853981633974483,    -0.7071067811865476",    // -pi/4
            "-1.0471975511965976,    -0.8660254037844387"     // -pi/3
    })
    void tableValues(double x, double expected) {
        assertEquals(expected, sin.calculate(x), DELTA);
    }

    @DisplayName("Экстремумы")
    @ParameterizedTest(name = "экстремум sin({0}) = {1}")
    @CsvSource({
            " 1.5707963267948966,  1.0",    // максимум pi/2
            "-1.5707963267948966, -1.0"     // минимум -pi/2
    })
    void extrema(double x, double expected) {
        assertEquals(expected, sin.calculate(x), DELTA);
    }

    @DisplayName("Нули функции")
    @ParameterizedTest(name = "sin({0}) = 0")
    @CsvSource({"0.0", "3.141592653589793", "-3.141592653589793", "6.283185307179586", "-6.283185307179586"})
    void zeros(double x) {
        assertEquals(0.0, sin.calculate(x), DELTA);
    }


    @DisplayName("Нечётность")
    @ParameterizedTest(name = "sin(-{0}) = -sin({0})")
    @ValueSource(doubles = {0.5235987755982988, 2.5})
    void oddFunction(double x) {
        assertEquals(-sin.calculate(x), sin.calculate(-x), DELTA);
    }

    // Периодичность: sin(x) = sin(x +- 2pi*k)
    @DisplayName("Периодичность")
    @ParameterizedTest(name = "sin({0}) = sin({0} + {1})")
    @CsvSource({
            "0.5,   6.283185307179586",     // +2pi
            "0.5,  -6.283185307179586",     // -2pi
            "-0.5, 12.566370614359172",      // +4pi
            "-0.5, -12.566370614359172"      // -4pi
    })
    void periodicity(double x, double shift) {
        assertEquals(sin.calculate(x), sin.calculate(x + shift), DELTA);
    }

    @DisplayName("Знакопостоянство по полуплоскостям")
    @ParameterizedTest(name = "sign(sin({0})) = {1}")
    @CsvSource({
            " 1.0,  1.0",    // I, II
            "-1.0, -1.0"     // III, IV
    })
    void signConstancy(double x, double expectedSign) {
        double val = sin.calculate(x);
        double actualSign = val > 0 ? 1.0 : -1.0;
        assertEquals(expectedSign, actualSign);
    }

    @ParameterizedTest(name = "sin'({0}) ≈ cos({0})")
    @ValueSource(doubles = {0.0, 0.5, 1.0, -0.5, 1.5})
    @DisplayName("Производная")
    void derivative(double x) {
        double h = 1e-6;
        double numerical = (sin.calculate(x + h) - sin.calculate(x - h)) / (2 * h);
        double expected = sin.calculate(PI / 2 - x);
        assertEquals(expected, numerical, 1e-5);
    }

    @ParameterizedTest(name = "sin({0}) → исключение")
    @ValueSource(doubles = {Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
    @DisplayName("Недопустимые значения")
    void invalidInputThrows(double x) {
        assertThrows(IllegalArgumentException.class, () -> sin.calculate(x));
    }
}
