package ru.itmo.tpo.system;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.itmo.tpo.MathFunction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("TrigPart")
class TrigPartTest {

    private MathFunction tanMock;
    private MathFunction cosMock;
    private MathFunction cotMock;
    private TrigPart trigPart;

    @BeforeEach
    void setUp() {
        tanMock = mock(MathFunction.class);
        cosMock = mock(MathFunction.class);
        cotMock = mock(MathFunction.class);
        trigPart = new TrigPart(tanMock, cosMock, cotMock);
    }

    private void stub(double x, double tan, double cos, double cot) {
        when(tanMock.calculate(x)).thenReturn(tan);
        when(cosMock.calculate(x)).thenReturn(cos);
        when(cotMock.calculate(x)).thenReturn(cot);
    }

    @DisplayName("Интервал (-2pi, -3pi/2): f > 0")
    @ParameterizedTest(name = "x={0}, ожидается={1}")
    @CsvSource({
            " -5.0,   12.6110589273,  3.380515006246586,  0.283662185463226,  0.295812915532746",
            " -5.8,   64.8536912726,  0.524666221946800,  0.885519516941319,  1.905973661291649"
    })
    void intervalFirstQuarter(double x, double expected, double tanV, double cosV, double cotV) {
        stub(x, tanV, cosV, cotV);
        double result = trigPart.calculate(x);
        assertEquals(expected, result, 1e-5);
        assertTrue(result > 0);
    }

    @DisplayName("Интервал (-3pi/2, -pi): f > 0")
    @ParameterizedTest(name = "x={0}, ожидается={1}")
    @CsvSource({
            " -3.5,   403.7614858235,  -0.374585640158595, -0.936456687290796, -2.669616484968866",
            " -3.8,    11.7280657366,  -0.773556090503126, -0.790967711914417, -1.292731079590614"
    })
    void intervalSecondQuarter(double x, double expected, double tanV, double cosV, double cotV) {
        stub(x, tanV, cosV, cotV);
        double result = trigPart.calculate(x);
        assertEquals(expected, result, 1e-5);
        assertTrue(result > 0);
    }

    @DisplayName("Локальные максимумы периода")
    @ParameterizedTest(name = "x={0}, ожидается={1}")
    @CsvSource({
            " -5.33713,  4.34588,   1.386788050285400,  0.584887229780669,  0.721090724566166",
            " -4.08765,  4.34588,  -1.386794011342659, -0.584885575729681, -0.721087624997620"
    })
    void localMaxima(double x, double expected, double tanV, double cosV, double cotV) {
        stub(x, tanV, cosV, cotV);
        double result = trigPart.calculate(x);
        assertEquals(expected, result, 1e-4);
    }

    @DisplayName("Интервал (-pi, -2.40598): f > 0, убывает")
    @ParameterizedTest(name = "x={0}, ожидается={1}")
    @CsvSource({
            " -3.0,  118499.0470325923,  0.142546543074278, -0.989992496600445,  7.015252551434534",
            " -2.7,      69.8880182820,  0.472727629103037, -0.904072142017061,  2.115383020656989"
    })
    void decreasingToFirstZero(double x, double expected, double tanV, double cosV, double cotV) {
        stub(x, tanV, cosV, cotV);
        double result = trigPart.calculate(x);
        assertEquals(expected, result, 1e-6);
        assertTrue(result > 0);
    }

    @DisplayName("Интервал (-2.40598, -pi/2): f > 0, возрастает")
    @ParameterizedTest(name = "x={0}, ожидается={1}")
    @CsvSource({
            " -2.0,   5.3229013793,  2.185039863261519, -0.416146836547142,  0.457657554360286",
            " -1.8,  19.2604158954,  4.286261674628062, -0.227202094693087,  0.233303534854011"
    })
    void increasingAfterFirstZero(double x, double expected, double tanV, double cosV, double cotV) {
        stub(x, tanV, cosV, cotV);
        double result = trigPart.calculate(x);
        assertEquals(expected, result, 1e-6);
        assertTrue(result > 0);
    }

    @DisplayName("Интервал (-pi/2, -0.73562): f > 0, убывает")
    @ParameterizedTest(name = "x={0}, ожидается={1}")
    @CsvSource({
            " -1.0,  2.5156874972,  -1.557407724654902,  0.540302305868140, -0.642092615934331",
            " -0.8,  0.2695878,     -1.029638557050364,  0.696706709347165, -0.971214600650474"
    })
    void decreasingToSecondZero(double x, double expected, double tanV, double cosV, double cotV) {
        stub(x, tanV, cosV, cotV);
        double result = trigPart.calculate(x);
        assertEquals(expected, result, 1e-6);
        assertTrue(result > 0);
    }

    @DisplayName("Нулевые пересечения: f = 0")
    @ParameterizedTest(name = "x={0}")
    @CsvSource({
            " -0.73562,  -0.905089727665395,  0.741414846812603, -1.104862832306603",
            " -2.40598,   0.905076363257262, -0.741419776578845,  1.104879146772896"
    })
    void zeroCrossings(double x, double tanV, double cosV, double cotV) {
        stub(x, tanV, cosV, cotV);
        assertEquals(0.0, trigPart.calculate(x), 1e-6);
    }

    @DisplayName("Интервал (-0.73562, 0): f > 0, возрастает")
    @ParameterizedTest(name = "x={0}, ожидается={1}")
    @CsvSource({
            " -0.6,   3.65331819,  -0.684136808341692,  0.825335614909678, -1.461695947078102",
            " -0.5,  24.938981,    -0.546302489843790,  0.877582561890373, -1.830487721712452"
    })
    void increasingToAsymptote(double x, double expected, double tanV, double cosV, double cotV) {
        stub(x, tanV, cosV, cotV);
        double result = trigPart.calculate(x);
        assertEquals(expected, result, 1e-5);
        assertTrue(result > 0);
    }

    @DisplayName("Правая граница области x=0: tan=0, ArithmeticException")
    @Test
    void domainRightBoundaryThrows() {
        when(tanMock.calculate(0.0)).thenReturn(0.0);
        assertThrows(ArithmeticException.class, () -> trigPart.calculate(0.0));
    }

    @DisplayName("tan возвращает NaN: ArithmeticException")
    @Test
    void tanNaNThrows() {
        when(tanMock.calculate(-1.0)).thenReturn(Double.NaN);
        assertThrows(ArithmeticException.class, () -> trigPart.calculate(-1.0));
    }

    @DisplayName("Периодические выколотые точки tan=0: ArithmeticException")
    @ParameterizedTest(name = "x={0}")
    @CsvSource({"-3.141592653589793", "-6.283185307179586"})
    void periodicExcludedPointsThrow(double x) {
        when(tanMock.calculate(x)).thenReturn(0.0);
        assertThrows(ArithmeticException.class, () -> trigPart.calculate(x));
    }

    @DisplayName("Асимптоты cos=0: исключение пробрасывается")
    @ParameterizedTest(name = "x={0}")
    @CsvSource({"-1.5707963267948966", "-4.71238898038469"})
    void cosIsZeroThrows(double x) {
        when(tanMock.calculate(x)).thenThrow(new ArithmeticException("tan undefined"));
        assertThrows(ArithmeticException.class, () -> trigPart.calculate(x));
    }

    @DisplayName("Периодичность 2pi: f(x) = f(x - 2pi)")
    @Test
    void periodicity2Pi() {
        double x = -0.5;
        double xPer = x - 2 * Math.PI;

        double tanV = -0.546302489843790;
        double cosV = 0.877582561890373;
        double cotV = -1.830487721712452;

        stub(x, tanV, cosV, cotV);
        stub(xPer, tanV, cosV, cotV);

        assertEquals(trigPart.calculate(x), trigPart.calculate(xPer), 1e-9);
    }
}
