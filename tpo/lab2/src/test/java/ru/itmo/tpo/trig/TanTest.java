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

@DisplayName("Tan")
class TanTest {

    private static final double DELTA = 1e-9;
    private MathFunction sinMock;
    private MathFunction cosMock;
    private Tan tan;

    @BeforeEach
    void setUp() {
        sinMock = mock(MathFunction.class);
        cosMock = mock(MathFunction.class);
        tan = new Tan(sinMock, cosMock);
    }

    @DisplayName("Табличные значения")
    @ParameterizedTest(name = "tan({0}) = {3}")
    @CsvSource({
            " 0.5235987755982988,     0.5,                    0.8660254037844387,     0.5773502691896258",  // pi/6
            " 0.7853981633974483,     0.7071067811865476,     0.7071067811865476,     1.0",                 // pi/4
            " 1.0471975511965976,     0.8660254037844387,     0.5,                    1.7320508075688772",  // pi/3
            "-0.7853981633974483,    -0.7071067811865476,     0.7071067811865476,    -1.0",                 // pi/4
            "-1.0471975511965976,    -0.8660254037844387,     0.5,                   -1.7320508075688772"   // pi/3
    })
    void tableValues(double x, double s, double c, double expected) {
        when(sinMock.calculate(x)).thenReturn(s);
        when(cosMock.calculate(x)).thenReturn(c);
        assertEquals(expected, tan.calculate(x), DELTA);
    }

    @DisplayName("Нули функции")
    @ParameterizedTest(name = "tan({0}) = 0")
    @CsvSource({
            "0.0,                  0.0,   1.0",
            "3.141592653589793,    0.0,  -1.0"
    })
    void zeros(double x, double s, double c) {
        when(sinMock.calculate(x)).thenReturn(s);
        when(cosMock.calculate(x)).thenReturn(c);
        assertEquals(0.0, tan.calculate(x), DELTA);
    }

    @DisplayName("Точки разрыва")
    @ParameterizedTest(name = "tan не определён при x = {0}")
    @CsvSource({
            " 1.5707963267948966,   1e-11",
            "-1.5707963267948966,   1e-11"
    })
    void asymptoteThrows(double x, double cosVal) {
        when(cosMock.calculate(x)).thenReturn(cosVal);
        assertThrows(ArithmeticException.class, () -> tan.calculate(x));
    }

    @DisplayName("Нечётность")
    @ParameterizedTest(name = "tan(-{0}) = -tan({0})")
    @CsvSource({
            "0.7853981633974483,  0.7071067811865476,  0.7071067811865476"
    })
    void oddFunction(double x, double s, double c) {
        when(sinMock.calculate(x)).thenReturn(s);
        when(cosMock.calculate(x)).thenReturn(c);
        when(sinMock.calculate(-x)).thenReturn(-s);
        when(cosMock.calculate(-x)).thenReturn(c);
        assertEquals(-tan.calculate(x), tan.calculate(-x), DELTA);
    }

    @DisplayName("Знакопостоянство по четвертям")
    @ParameterizedTest(name = "sign(tan({0})) = {3}")
    @CsvSource({
            " 0.7853981633974483,    0.7071067811865476,   0.7071067811865476,   1.0",
            " 2.356194490192345,     0.7071067811865476,  -0.7071067811865476,  -1.0"
    })
    void signConstancy(double x, double s, double c, double expectedSign) {
        when(sinMock.calculate(x)).thenReturn(s);
        when(cosMock.calculate(x)).thenReturn(c);
        double val = tan.calculate(x);
        double actualSign = val > 0 ? 1.0 : -1.0;
        assertEquals(expectedSign, actualSign);
    }

    @DisplayName("Период π")
    @ParameterizedTest(name = "tan({0}) = tan({0}+π)")
    @CsvSource({
            "0.7853981633974483, 0.7071067811865476, 0.7071067811865476, -0.7071067811865476, -0.7071067811865476"
    })
    void periodPi(double x, double s1, double c1, double s2, double c2) {
        when(sinMock.calculate(x)).thenReturn(s1);
        when(cosMock.calculate(x)).thenReturn(c1);
        when(sinMock.calculate(x + 3.141592653589793)).thenReturn(s2);
        when(cosMock.calculate(x + 3.141592653589793)).thenReturn(c2);
        assertEquals(tan.calculate(x), tan.calculate(x + 3.141592653589793), DELTA);
    }
}
