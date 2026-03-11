package ru.itmo.tpo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class TangentFunctionTest {

    private static final double DELTA = 1e-5;
    private static final double PI = 3.14159265358979323846;

    @ParameterizedTest(name = "x = {0}, ожидается tg(x) ≈ {1}")
    @CsvSource({
            "0.7853981633974483, 1.0",                   // π/4
            "-0.7853981633974483, -1.0",                 // -π/4
            "0.5235987755982989, 0.5773502691896257",    // π/6
            "-0.5235987755982989, -0.5773502691896257",  // -π/6
            "1.0471975511965976, 1.7320508075688772",    // π/3
            "-1.0471975511965976, -1.7320508075688772"   // -π/3
    })
    @DisplayName("Проверка вычислений на табличных знначениях")
    void testBasicTabularValues(double x, double expected) {
        assertEquals(expected, TangentFunction.tg(x), DELTA);
    }

    @ParameterizedTest(name = "x = {0}, tg(x) должен быть 0")
    @ValueSource(doubles = {0.0, PI, -PI, 2 * PI, -2 * PI})
    @DisplayName("Проверка нулей функции")
    void testZeros(double x) {
        assertEquals(0.0, TangentFunction.tg(x), DELTA);
    }

    @ParameterizedTest(name = "В точке x = {0} функция не определена")
    @ValueSource(doubles = {PI / 2, -PI / 2, 3 * PI / 2, -3 * PI / 2})
    @DisplayName("Проверка поведения в точках разрыва")
    void testAsymptotes(double x) {
        assertThrows(IllegalArgumentException.class, () -> TangentFunction.tg(x));
    }

    @ParameterizedTest(name = "Для x = {0} знак функции должен быть {1}")
    @CsvSource({
            "0.5, 1.0",     // I четверть
            "2.0, -1.0",    // II
            "4.0, 1.0",     // III
            "-0.5, -1.0"    // IV
    })
    @DisplayName("Проверка знакопостоянства по четвертям")
    void testSignConstancy(double x, double expectedSign) {
        double actualValue = TangentFunction.tg(x);

        double actualSign = 0.0;
        if (actualValue > DELTA) actualSign = 1.0;
        else if (actualValue < -DELTA) actualSign = -1.0;

        assertEquals(expectedSign, actualSign, "Неверный знак в данной четверти");
    }

    @ParameterizedTest(name = "tg({0}) == tg({0} + {1})")
    @CsvSource({
            "0.5, 3.141592653589793",   // сдвиг на PI
            "0.5, 6.283185307179586",   // 2*PI
            "0.5, -3.141592653589793",  // -PI
            "-0.5, 31.41592653589793"   // 10*PI
    })
    @DisplayName("Проверка периодичности")
    void testPeriodicity(double x, double shift) {
        double original = TangentFunction.tg(x);
        double shifted = TangentFunction.tg(x + shift);

        assertEquals(original, shifted, DELTA);
    }

    @ParameterizedTest(name = "Производная в точке x = {0}")
    @ValueSource(doubles = {0.0, 0.5, -0.5, 1.0, 3.0})
    @DisplayName("Проверка угла наклона (производной)")
    void testDerivativeAngle(double x) {
        double h = 1e-6;

        // Численная производная (f(x+h) - f(x-h)) / 2h
        double fPlus = TangentFunction.tg(x + h);
        double fMinus = TangentFunction.tg(x - h);
        double numericalDerivative = (fPlus - fMinus) / (2 * h);

        // Аналитическая производная tg'(x) = 1 + tg^2(x)
        double tgX = TangentFunction.tg(x);
        double expectedDerivative = 1.0 + (tgX * tgX);

        assertEquals(expectedDerivative, numericalDerivative, DELTA, "Угол наклона не совпадает с аналитическим значением");
    }
}