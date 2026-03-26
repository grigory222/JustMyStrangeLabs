package ru.itmo.tpo.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import ru.itmo.tpo.MathFunction;
import ru.itmo.tpo.log.Ln;
import ru.itmo.tpo.log.Log;
import ru.itmo.tpo.system.FunctionSystem;
import ru.itmo.tpo.system.LogPart;
import ru.itmo.tpo.system.TrigPart;
import ru.itmo.tpo.trig.Cos;
import ru.itmo.tpo.trig.Cot;
import ru.itmo.tpo.trig.Sin;
import ru.itmo.tpo.trig.Tan;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class IntegrationTest {

    private static final double PI = 3.14159265358979323846;
    private static final double DELTA = 1e-6;
    private static final double SQRT2 = 1.4142135623730951;

    @Nested
    @DisplayName("Шаг 0: все модули - заглушки")
    class Step0_AllStubs {

        @Test
        @DisplayName("x <= 0 маршрут к TrigPart-заглушке")
        void routesToTrigStub() {
            MathFunction trigStub = mock(MathFunction.class);
            MathFunction logStub = mock(MathFunction.class);
            when(trigStub.calculate(-1.0)).thenReturn(42.0);

            FunctionSystem system = new FunctionSystem(trigStub, logStub);
            assertEquals(42.0, system.calculate(-1.0), 1e-9);
            verify(trigStub).calculate(-1.0);
            verifyNoInteractions(logStub);
        }

        @Test
        @DisplayName("x > 0 маршрут к LogPart-заглушке")
        void routesToLogStub() {
            MathFunction trigStub = mock(MathFunction.class);
            MathFunction logStub = mock(MathFunction.class);
            when(logStub.calculate(2.0)).thenReturn(7.0);

            FunctionSystem system = new FunctionSystem(trigStub, logStub);
            assertEquals(7.0, system.calculate(2.0), 1e-9);
            verify(logStub).calculate(2.0);
            verifyNoInteractions(trigStub);
        }

    }

    @Nested
    @DisplayName("Шаг 1: Sin + Cos")
    class Step1_SinCos {

        @ParameterizedTest(name = "cos({0}) = {1}")
        @CsvSource({
                "0.0,                   1.0",    // экстремум
                "1.0471975511965976,    0.5",    // pi/3 табличное
                "3.141592653589793,    -1.0"     // экстремум
        })
        @DisplayName("Табличные значения")
        void cosTableValues(double x, double expected) {
            Cos cos = new Cos(new Sin());
            assertEquals(expected, cos.calculate(x), DELTA);
        }
    }

    @Nested
    @DisplayName("Шаг 2: + Tan")
    class Step2_AddTan {

        @ParameterizedTest(name = "tan({0}) = {1}")
        @CsvSource({
                "0.7853981633974483,   1.0",                  // pi/4
                "0.5235987755982988,   0.5773502691896258"    // pi/6
        })
        @DisplayName("Табличные значения")
        void tanTableValues(double x, double expected) {
            Sin sin = new Sin();
            Tan tan = new Tan(sin, new Cos(sin));
            assertEquals(expected, tan.calculate(x), DELTA);
        }

        @Test
        @DisplayName("Точка разрыва pi/2")
        void tanAsymptote() {
            Sin sin = new Sin();
            assertThrows(ArithmeticException.class,
                    () -> new Tan(sin, new Cos(sin)).calculate(PI / 2));
        }
    }

    @Nested
    @DisplayName("Шаг 3: + Cot")
    class Step3_AddCot {

        @ParameterizedTest(name = "cot({0}) = {1}")
        @CsvSource({
                "0.7853981633974483,   1.0",                  // pi/4
                "0.5235987755982988,   1.7320508075688772"    // pi/6
        })
        @DisplayName("Табличные значения")
        void cotTableValues(double x, double expected) {
            Sin sin = new Sin();
            Cot cot = new Cot(sin, new Cos(sin));
            assertEquals(expected, cot.calculate(x), DELTA);
        }

        @Test
        @DisplayName("Точка разрыва pi")
        void cotAsymptote() {
            Sin sin = new Sin();
            assertThrows(ArithmeticException.class,
                    () -> new Cot(sin, new Cos(sin)).calculate(PI));
        }
    }

    @Nested
    @DisplayName("Шаг 4: TrigPart")
    class Step4_TrigPart {

        private TrigPart build() {
            Sin sin = new Sin();
            Cos cos = new Cos(sin);
            return new TrigPart(new Tan(sin, cos), cos, new Cot(sin, cos));
        }

        @Test
        @DisplayName("x = -pi/4: результат = (sqrt(2) - 1)^2")
        void valueAtMinusPiOver4() {
            assertEquals(3.0 - 2.0 * SQRT2, build().calculate(-PI / 4), DELTA);
        }

        @ParameterizedTest(name = "x = {0}: исключение")
        @CsvSource({
                "0.0",                  // tan = 0
                "-3.141592653589793",   // tan = 0, cot не определён
                "-1.5707963267948966"   // tan не определён (cos = 0)
        })
        @DisplayName("Выколотые точки и асимптоты")
        void excludedPoints(double x) {
            assertThrows(ArithmeticException.class, () -> build().calculate(x));
        }
    }

    @Nested
    @DisplayName("Шаг 5: Ln + Log")
    class Step5_LnLog {

        @ParameterizedTest(name = "log_{1}({0}) = {2}")
        @CsvSource({
                "4.0,   2.0,  2.0",    // log2(4) = 2
                "100.0, 10.0, 2.0",    // log10(100) = 2
                "27.0,  3.0,  3.0"     // log3(27) = 3
        })
        @DisplayName("Табличные значения")
        void logTableValues(double x, double base, double expected) {
            assertEquals(expected, new Log(new Ln(), base).calculate(x), DELTA);
        }
    }

    @Nested
    @DisplayName("Шаг 6: LogPart")
    class Step6_LogPart {

        private LogPart build() {
            Ln ln = new Ln();
            return new LogPart(ln, new Log(ln, 2), new Log(ln, 3),
                    new Log(ln, 5), new Log(ln, 10));
        }

        @ParameterizedTest(name = "x = {0}: результат < 0")
        @ValueSource(doubles = {0.1, 0.3})
        @DisplayName("Интервал (0, 0.5): отрицательные значения")
        void negativeOnZeroHalf(double x) {
            assertTrue(build().calculate(x) < 0);
        }

        @ParameterizedTest(name = "x = {0}: результат > 0")
        @ValueSource(doubles = {0.7, 10.0})
        @DisplayName("После нуля и после x=1: положительные значения")
        void positiveAfterCrossing(double x) {
            assertTrue(build().calculate(x) > 0);
        }

        @Test
        @DisplayName("Выколотая точка x = 1")
        void excludedPointOne() {
            assertThrows(ArithmeticException.class, () -> build().calculate(1.0));
        }

        @ParameterizedTest(name = "x = {0}: исключение")
        @ValueSource(doubles = {0.0, -1.0})
        @DisplayName("Недопустимые значения x <= 0")
        void invalidDomain(double x) {
            assertThrows(IllegalArgumentException.class, () -> build().calculate(x));
        }
    }

    @Nested
    @DisplayName("Шаг 7: FunctionSystem")
    class Step7_FullSystem {

        private FunctionSystem build() {
            Sin sin = new Sin();
            Cos cos = new Cos(sin);
            TrigPart trig = new TrigPart(new Tan(sin, cos), cos, new Cot(sin, cos));
            Ln ln = new Ln();
            LogPart log = new LogPart(ln, new Log(ln, 2), new Log(ln, 3),
                    new Log(ln, 5), new Log(ln, 10));
            return new FunctionSystem(trig, log);
        }

        @Test
        @DisplayName("x = -pi/4: trigPart, результат = (sqrt(2) - 1)^2")
        void trigPartValue() {
            assertEquals(3.0 - 2.0 * SQRT2, build().calculate(-PI / 4), DELTA);
        }

        @Test
        @DisplayName("x = 10: logPart, результат > 0")
        void logPartValue() {
            assertTrue(build().calculate(10.0) > 0);
        }

        @ParameterizedTest(name = "x = {0}: исключение")
        @CsvSource({
                "0.0",                  // tan = 0
                "-1.5707963267948966",  // tan не определён
                "1.0"                   // log10 = 0
        })
        @DisplayName("Выколотые точки")
        void excludedPoints(double x) {
            assertThrows(RuntimeException.class, () -> build().calculate(x));
        }
    }
}
