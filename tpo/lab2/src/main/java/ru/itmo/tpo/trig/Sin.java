package ru.itmo.tpo.trig;

import ru.itmo.tpo.MathFunction;

/**
 * Базовая тригонометрическая функция sin(x).
 * Реализована через ряд Тейлора: sin(x) = x - x³/3! + x⁵/5! - ...
 * Перед вычислением угол нормализуется в [-π, π] для точности сходимости ряда.
 */
public class Sin implements MathFunction {

    public static final double PI = 3.14159265358979323846;

    private final double precision;

    public Sin(double precision) {
        this.precision = precision;
    }

    public Sin() {
        this(1e-15);
    }

    @Override
    public double calculate(double x) {
        if (Double.isNaN(x) || Double.isInfinite(x)) {
            throw new IllegalArgumentException("x must be finite, got: " + x);
        }
        double normalized = normalizeAngle(x);
        return taylorSeries(normalized);
    }

    private static double normalizeAngle(double x) {
        x = x % (2 * PI);

        if (x > PI) {
            x -= 2 * PI;
        } else if (x < -PI) {
            x += 2 * PI;
        }
        return x;
    }

    private double taylorSeries(double x) {
        double sum = 0.0;
        double term = x;
        double xSq = x * x;
        int n = 0;

        while (abs(term) >= precision) {
            sum += term;
            term *= -xSq / ((2.0 * n + 2.0) * (2.0 * n + 3.0));
            n++;
        }
        return sum;
    }

    private static double abs(double v) {
        return v < 0 ? -v : v;
    }
}
