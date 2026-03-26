package ru.itmo.tpo.trig;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.itmo.tpo.MathFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Cot")
class CotTest {

    private static final double DELTA = 1e-9;
    private MathFunction sinMock;
    private MathFunction cosMock;
    private Cot cot;

    @BeforeEach
    void setUp() {
        sinMock = mock(MathFunction.class);
        cosMock = mock(MathFunction.class);
        cot = new Cot(sinMock, cosMock);
    }

    @DisplayName("Табличные значения")
    @ParameterizedTest(name = "cot({0}) = {3}")
    @CsvSource({
            " 0.5235987755982988,    0.5,                    0.8660254037844387,  1.7320508075688772",   // π/6
            " 0.7853981633974483,    0.7071067811865476,     0.7071067811865476,  1.0",                  // π/4
            " 1.0471975511965976,    0.8660254037844387,     0.5,                 0.5773502691896258"    // π/3
    })
    void tableValues(double x, double s, double c, double expected) {
        when(sinMock.calculate(x)).thenReturn(s);
        when(cosMock.calculate(x)).thenReturn(c);
        assertEquals(expected, cot.calculate(x), DELTA);
    }

    @DisplayName("Точки разрыва")
    @ParameterizedTest(name = "cot не определён при x = {0}")
    @CsvSource({
            "0.0,                  0.0",    // x=0
            "3.141592653589793,    0.0"     // x=π
    })
    void asymptoteThrows(double x, double sinVal) {
        when(sinMock.calculate(x)).thenReturn(sinVal);
        assertThrows(ArithmeticException.class, () -> cot.calculate(x));
    }

    @DisplayName("Нечётность")
    @ParameterizedTest(name = "cot(-{0}) = -cot({0})")
    @CsvSource({
            "0.7853981633974483,   0.7071067811865476,  0.7071067811865476"    // π/4
    })
    void oddFunction(double x, double s, double c) {
        when(sinMock.calculate(x)).thenReturn(s);
        when(cosMock.calculate(x)).thenReturn(c);
        when(sinMock.calculate(-x)).thenReturn(-s);
        when(cosMock.calculate(-x)).thenReturn(c);
        assertEquals(-cot.calculate(x), cot.calculate(-x), DELTA);
    }

    @DisplayName("Знакопостоянство по четвертям")
    @ParameterizedTest(name = "sign(cot({0})) = {3}")
    @CsvSource({
            " 0.7853981633974483,    0.7071067811865476,   0.7071067811865476,   1.0",   // Q1: +
            " 2.356194490192345,     0.7071067811865476,  -0.7071067811865476,  -1.0"    // Q2: -
    })
    void signConstancy(double x, double s, double c, double expectedSign) {
        when(sinMock.calculate(x)).thenReturn(s);
        when(cosMock.calculate(x)).thenReturn(c);
        double val = cot.calculate(x);
        double actualSign = val > 0 ? 1.0 : -1.0;
        assertEquals(expectedSign, actualSign);
    }

    @DisplayName("Период π")
    @ParameterizedTest(name = "cot({0}) = cot({0}+π)")
    @CsvSource({
            "0.7853981633974483, 0.7071067811865476, 0.7071067811865476, -0.7071067811865476, -0.7071067811865476"
    })
    void periodPi(double x, double s1, double c1, double s2, double c2) {
        when(sinMock.calculate(x)).thenReturn(s1);
        when(cosMock.calculate(x)).thenReturn(c1);
        when(sinMock.calculate(x + 3.141592653589793)).thenReturn(s2);
        when(cosMock.calculate(x + 3.141592653589793)).thenReturn(c2);
        assertEquals(cot.calculate(x), cot.calculate(x + 3.141592653589793), DELTA);
    }

    @DisplayName("Поведение у точки разрыва x=0")
    @ParameterizedTest(name = "cot у x={0}: знак {2}")
    @CsvSource({
            " 0.0001,  0.0001,  1.0",
            "-0.0001, -0.0001, -1.0"
    })
    void asymptoteNeighborhood(double x, double s, double expectedSign) {
        when(sinMock.calculate(x)).thenReturn(s);
        when(cosMock.calculate(x)).thenReturn(1.0);
        double val = cot.calculate(x);
        double actualSign = val > 0 ? 1.0 : -1.0;
        assertEquals(expectedSign, actualSign);
    }
}
