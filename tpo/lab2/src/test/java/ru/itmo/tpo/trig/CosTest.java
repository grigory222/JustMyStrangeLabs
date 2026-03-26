package ru.itmo.tpo.trig;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import ru.itmo.tpo.MathFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Cos")
class CosTest {

    private static final double DELTA = 1e-9;
    private MathFunction sinMock;
    private Cos cos;

    @BeforeEach
    void setUp() {
        sinMock = mock(MathFunction.class);
        cos = new Cos(sinMock);
    }

    // Табличные значения — только нетривиальные точки, без нулей и экстремумов

    @DisplayName("Табличные значения")
    @ParameterizedTest(name = "cos({0}) = {2}")
    @CsvSource({
            " 0.5235987755982988,     0.5,                    0.8660254037844387",  // π/6
            " 0.7853981633974483,     0.7071067811865476,     0.7071067811865476",  // π/4
            " 1.0471975511965976,     0.8660254037844387,     0.5",                 // π/3
            " 2.0943951023931953,     0.8660254037844387,    -0.5",                 // 2π/3
            "-1.0471975511965976,    -0.8660254037844387,     0.5"                  // -π/3
    })
    void tableValues(double x, double sinVal, double expectedCos) {
        when(sinMock.calculate(x)).thenReturn(sinVal);
        assertEquals(expectedCos, cos.calculate(x), DELTA);
    }

    // Экстремумы

    @DisplayName("Экстремумы")
    @ParameterizedTest(name = "экстремум cos({0}) = {2}")
    @CsvSource({
            "0.0,                  0.0,  1.0",    // максимум x=0
            "3.141592653589793,    0.0, -1.0"     // минимум x=π
    })
    void extrema(double x, double sinVal, double expected) {
        when(sinMock.calculate(x)).thenReturn(sinVal);
        assertEquals(expected, cos.calculate(x), DELTA);
    }

    // Нули функции

    @DisplayName("Нули функции")
    @ParameterizedTest(name = "cos({0}) = 0")
    @CsvSource({
            " 1.5707963267948966,   1.0",    // π/2
            "-1.5707963267948966,  -1.0",    // -π/2
            " 4.71238898038469,    -1.0"     // 3π/2
    })
    void zeros(double x, double sinVal) {
        when(sinMock.calculate(x)).thenReturn(sinVal);
        assertEquals(0.0, cos.calculate(x), DELTA);
    }

    // Чётность: cos(-x) = cos(x)

    @DisplayName("Чётность")
    @ParameterizedTest(name = "cos(-{0}) = cos({0})")
    @CsvSource({
            "1.0471975511965976,   0.8660254037844387,  -0.8660254037844387"  // π/3
    })
    void evenFunction(double x, double sinPos, double sinNeg) {
        when(sinMock.calculate(x)).thenReturn(sinPos);
        when(sinMock.calculate(-x)).thenReturn(sinNeg);
        assertEquals(cos.calculate(x), cos.calculate(-x), DELTA);
    }

    // Периодичность: cos(x) = cos(x + 2π)

    @DisplayName("Период 2π")
    @ParameterizedTest(name = "cos({0}) = cos({0} + {1})")
    @CsvSource({
            "0.5235987755982988,   6.283185307179586,   0.5",    // +2π
            "0.5235987755982988,  -6.283185307179586,   0.5"     // -2π
    })
    void periodicity(double x, double shift, double sinVal) {
        when(sinMock.calculate(x)).thenReturn(sinVal);
        when(sinMock.calculate(x + shift)).thenReturn(sinVal);
        assertEquals(cos.calculate(x), cos.calculate(x + shift), DELTA);
    }

    // Знакопостоянство: cos > 0 на (-π/2, π/2), cos < 0 на (π/2, π) ∪ (-π, -π/2)

    @DisplayName("Знакопостоянство")
    @ParameterizedTest(name = "sign(cos({0})) = {2}")
    @CsvSource({
            " 0.7853981633974483,   0.7071067811865476,   1.0",   // Q1: norm в (-π/2, π/2)
            " 2.356194490192345,    0.7071067811865476,  -1.0",   // Q2: norm > π/2
            "-2.0943951023931953,  -0.8660254037844387,  -1.0"    // Q3: norm < -π/2
    })
    void signConstancy(double x, double sinVal, double expectedSign) {
        when(sinMock.calculate(x)).thenReturn(sinVal);
        double val = cos.calculate(x);
        double actualSign = val > 0 ? 1.0 : -1.0;
        assertEquals(expectedSign, actualSign);
    }

    // Недопустимые аргументы

    @DisplayName("Недопустимые значения")
    @ParameterizedTest(name = "cos({0}) → исключение")
    @ValueSource(doubles = {Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
    void invalidInputThrows(double x) {
        assertThrows(IllegalArgumentException.class, () -> cos.calculate(x));
    }
}
