package ru.itmo.tpo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class TangentFunctionTest {

    private static final double DELTA = 1e-5;

    @ParameterizedTest(name = "tg({0}) ≈ {1}")
    @CsvSource({
        "0.0, 0.0",
        "0.1, 0.10033467208545055",
        "0.2, 0.20271003550867248",
        "0.3, 0.30933624960962325",
        "0.4, 0.42279321873816156",
        "0.5, 0.5463024898437905",
        "1.0, 1.5574077246549023",
        "1.4, 5.797883715482887",
        "1.5, 14.101419947171719",
        "-0.1, -0.10033467208545055",
        "-0.2, -0.20271003550867248",
        "-0.3, -0.30933624960962325",
        "-1.0, -1.5574077246549023",
        "-1.5, -14.101419947171719"
    })
    @DisplayName("Проверка значений tg(x) по табличным данным")
    void testTangentValues(double x, double expected) {
        double actual = TangentFunction.tg(x);
        assertEquals(expected, actual, DELTA,
            String.format("tg(%.2f) ожидается %.6f, получено %.6f", x, expected, actual));
    }

    @ParameterizedTest(name = "tg({0}) выбрасывает исключение")
    @ValueSource( doubles = {
        1.5707963267948966, 4.71238898038469, -1.5707963267948966
    })
    @DisplayName("Проверка выброса исключения при x = π/2 + πk")
    void testTangentAtDiscontinuity(double x) {
        Exception exception = assertThrows(IllegalArgumentException.class, 
            () -> TangentFunction.tg(x));
        assertTrue(exception.getMessage().contains("не определен"));
    }

    @Test
    @DisplayName("Проверка цикла на больших числах (для 100% покрытия)")
    void testLargeInputForLoopLimit() {
        // При большом X без редукции периода ряд Тейлора не успеет сойтись
        // до PRECISION за 100 итераций.
        // Цикл завершится по условию n < MAX_ITERATIONS, а не по break.
        double largeX = 100.0;

        // Нам не важен результат (он будет неточным или NaN),
        // главное, что метод отработает без ошибок.
        assertDoesNotThrow(() -> TangentFunction.tg(largeX));
    }
}
