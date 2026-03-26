package ru.itmo.tpo.system;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import ru.itmo.tpo.MathFunction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@DisplayName("LogPart")
class LogPartTest {

    private MathFunction lnMock;
    private MathFunction log2Mock;
    private MathFunction log3Mock;
    private MathFunction log5Mock;
    private MathFunction log10Mock;
    private LogPart logPart;

    @BeforeEach
    void setUp() {
        lnMock = mock(MathFunction.class);
        log2Mock = mock(MathFunction.class);
        log3Mock = mock(MathFunction.class);
        log5Mock = mock(MathFunction.class);
        log10Mock = mock(MathFunction.class);
        logPart = new LogPart(lnMock, log2Mock, log3Mock, log5Mock, log10Mock);
    }

    private void stub(double x, double ln, double log2, double log3, double log5, double log10) {
        when(lnMock.calculate(x)).thenReturn(ln);
        when(log2Mock.calculate(x)).thenReturn(log2);
        when(log3Mock.calculate(x)).thenReturn(log3);
        when(log5Mock.calculate(x)).thenReturn(log5);
        when(log10Mock.calculate(x)).thenReturn(log10);
    }

    @DisplayName("Интервал (0, 0.5004): f < 0")
    @ParameterizedTest(name = "x={0}, ожидается={1}")
    @CsvSource({
            "0.221,  -3.6624,          -1.509592577464384, -2.177881725270655, -1.374090380232795, -0.937962605330509, -0.655607726314889",
            "0.357,  -0.731622565582,  -1.030019497202498, -1.486004020632988, -0.937564150544166, -0.639987096889438, -0.447331783887807"
    })
    void negativeBeforeZeroCrossing(double x, double expected,
                                    double ln, double log2, double log3, double log5, double log10) {
        stub(x, ln, log2, log3, log5, log10);
        double result = logPart.calculate(x);
        assertEquals(expected, result, 1e-6);
        assertTrue(result < 0);
    }

    @DisplayName("Интервал (0.5004, 0.66786): f > 0, восходящий участок")
    @ParameterizedTest(name = "x={0}, ожидается={1}")
    @CsvSource({
            "0.5265, 0.0501944961568,  -0.641503947408107, -0.925494563636370, -0.583922056966819, -0.398588813182549, -0.278601624478495",
            "0.619,  0.135682727871,   -0.479650006297541, -0.691988685447822, -0.436596250783831, -0.298023305274400, -0.208309350979882"
    })
    void positiveAscending(double x, double expected,
                           double ln, double log2, double log3, double log5, double log10) {
        stub(x, ln, log2, log3, log5, log10);
        double result = logPart.calculate(x);
        assertEquals(expected, result, 1e-9);
        assertTrue(result > 0);
    }

    @DisplayName("Интервал (0.66786, 1): f > 0, нисходящий участок")
    @ParameterizedTest(name = "x={0}, ожидается={1}")
    @CsvSource({
            "0.736,  0.132376340238,   -0.306525160253261, -0.442222328605074, -0.279011224810595, -0.190454790386834, -0.133122185662501",
            "0.936,  0.0349094584966,  -0.066139802504545, -0.095419565078682, -0.060203042680989, -0.041094969860947, -0.028724151261895"
    })
    void positiveDescending(double x, double expected,
                            double ln, double log2, double log3, double log5, double log10) {
        stub(x, ln, log2, log3, log5, log10);
        double result = logPart.calculate(x);
        assertEquals(expected, result, 1e-9);
        assertTrue(result > 0);
    }

    // Нулевые пересечения: (0.5004, 0) и (1.99841, 0)

    @DisplayName("Нулевые пересечения оси x")
    @ParameterizedTest(name = "x={0}")
    @CsvSource({
        "0.5004,  -0.692347500389381, -0.998846305383630, -0.630201853311454, -0.430179688846947, -0.300682698978618",
        "1.99841,  0.692351864379859,  0.998852601291050,  0.630205825586772,  0.430182400346685,  0.300684594235601"
    })
    void zeroCrossings(double x, double ln, double log2, double log3, double log5, double log10) {
        stub(x, ln, log2, log3, log5, log10);
        assertEquals(0.0, logPart.calculate(x), 1e-3);
    }

    // Экстремумы: максимум (0.66786; 0.14404) и минимум (1.49733; -0.14404)

    @DisplayName("Экстремумы: максимум и минимум")
    @ParameterizedTest(name = "x={0}, ожидается={1}")
    @CsvSource({
        "0.66786,  0.14404,  -0.403676708248948, -0.582382385113138, -0.367442374723789, -0.250818441103099, -0.175314566865387",
        "1.49733, -0.14404,   0.403683522025734,  0.582392215315117,  0.367448576890702,  0.250822674740653,  0.175317526051046"
    })
    void extrema(double x, double expected, double ln, double log2, double log3, double log5, double log10) {
        stub(x, ln, log2, log3, log5, log10);
        assertEquals(expected, logPart.calculate(x), 1e-4);
    }

    // Выколотая точка x = 1: log10(1) = 0

    @DisplayName("Выколотая точка x = 1")
    @Test
    void excludedPointAtOneThrows() {
        stub(1.0, 0.0, 0.0, 0.0, 0.0, 0.0);
        assertThrows(ArithmeticException.class, () -> logPart.calculate(1.0));
    }

    @DisplayName("Интервал (1, 1.99841): f < 0")
    @ParameterizedTest(name = "x={0}, ожидается={1}")
    @CsvSource({
            "1.15,  -0.0715153582561,  0.139761942375159, 0.201633861169650, 0.127216802339429, 0.086838977319593, 0.060697840353612",
            "1.6,   -0.137723806772,   0.470003629245736, 0.678071905112638, 0.427815739996445, 0.292029674220179, 0.204119982655925"
    })
    void negativeAfterOne(double x, double expected,
                          double ln, double log2, double log3, double log5, double log10) {
        stub(x, ln, log2, log3, log5, log10);
        double result = logPart.calculate(x);
        assertEquals(expected, result, 1e-9);
        assertTrue(result < 0);
    }

    @DisplayName("Интервал (1.99841, +inf): f > 0")
    @ParameterizedTest(name = "x={0}, ожидается={1}")
    @CsvSource({
            "2.25,  0.170471190541,    0.810930216216329, 1.169925001442312, 0.738140492857085, 0.503859272825185, 0.352182518111362",
            "3.0,   0.98840885,        1.098612288668110, 1.584962500721156, 1.000000000000000, 0.682606194485985, 0.477121254719662"
    })
    void positiveAfterSecondZeroCrossing(double x, double expected,
                                         double ln, double log2, double log3, double log5, double log10) {
        stub(x, ln, log2, log3, log5, log10);
        double result = logPart.calculate(x);
        assertEquals(expected, result, 1e-6);
        assertTrue(result > 0);
    }

    @DisplayName("Недопустимые значения x <= 0")
    @ParameterizedTest(name = "x={0} выбрасывает исключение")
    @ValueSource(doubles = {0.0, -1.0})
    void nonPositiveXThrows(double x) {
        assertThrows(IllegalArgumentException.class, () -> logPart.calculate(x));
    }
}
