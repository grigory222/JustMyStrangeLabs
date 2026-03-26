package ru.itmo.tpo.log;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import ru.itmo.tpo.MathFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@DisplayName("Log (произвольное основание)")
class LogTest {

    private static final double DELTA = 1e-9;
    private static final double LN2 = 0.6931471805599453;
    private static final double LN3 = 1.0986122886681098;
    private static final double LN5 = 1.6094379124341003;
    private static final double LN10 = 2.302585092994046;

    @DisplayName("log2: табличные значения")
    @ParameterizedTest(name = "log2({0}) = {2}")
    @CsvSource({
            "1.0,  0.0,                   0.0",
            "4.0,  1.3862943611198906,    2.0"
    })
    void log2TableValues(double x, double lnX, double expected) {
        MathFunction ln = mock(MathFunction.class);
        when(ln.calculate(2.0)).thenReturn(LN2);
        when(ln.calculate(x)).thenReturn(lnX);
        assertEquals(expected, new Log(ln, 2.0).calculate(x), DELTA);
    }

    @DisplayName("log3: табличные значения")
    @ParameterizedTest(name = "log3({0}) = {2}")
    @CsvSource({
            "1.0,   0.0,                   0.0",
            "9.0,   2.1972245773362196,    2.0"
    })
    void log3TableValues(double x, double lnX, double expected) {
        MathFunction ln = mock(MathFunction.class);
        when(ln.calculate(3.0)).thenReturn(LN3);
        when(ln.calculate(x)).thenReturn(lnX);
        assertEquals(expected, new Log(ln, 3.0).calculate(x), DELTA);
    }

    @DisplayName("log5: табличные значения")
    @ParameterizedTest(name = "log5({0}) = {2}")
    @CsvSource({
            "1.0,   0.0,                   0.0",
            "25.0,  3.2188758248682006,    2.0"
    })
    void log5TableValues(double x, double lnX, double expected) {
        MathFunction ln = mock(MathFunction.class);
        when(ln.calculate(5.0)).thenReturn(LN5);
        when(ln.calculate(x)).thenReturn(lnX);
        assertEquals(expected, new Log(ln, 5.0).calculate(x), DELTA);
    }

    @DisplayName("log10: табличные значения")
    @ParameterizedTest(name = "log10({0}) = {2}")
    @CsvSource({
            "1.0,     0.0,                   0.0",
            "100.0,   4.605170185988092,     2.0"
    })
    void log10TableValues(double x, double lnX, double expected) {
        MathFunction ln = mock(MathFunction.class);
        when(ln.calculate(10.0)).thenReturn(LN10);
        when(ln.calculate(x)).thenReturn(lnX);
        assertEquals(expected, new Log(ln, 10.0).calculate(x), DELTA);
    }

    // Недопустимые основания
    @DisplayName("Недопустимое основание")
    @ParameterizedTest(name = "основание {0} недопустимо")
    @ValueSource(doubles = {-1.0, 0.0, 1.0})
    void invalidBaseThrows(double base) {
        MathFunction ln = mock(MathFunction.class);
        when(ln.calculate(anyDouble())).thenReturn(0.0);
        assertThrows(IllegalArgumentException.class, () -> new Log(ln, base));
    }
}
