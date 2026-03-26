package ru.itmo.tpo.trig;

import ru.itmo.tpo.MathFunction;

/**
 * cot(x) = cos(x) / sin(x)
 * Не определён при sin(x) = 0, т.е. x = πk.
 */
public class Cot implements MathFunction {

    private static final double EPSILON = 1e-10;

    private final MathFunction sin;
    private final MathFunction cos;

    public Cot(MathFunction sin, MathFunction cos) {
        this.sin = sin;
        this.cos = cos;
    }

    @Override
    public double calculate(double x) {
        double sinX = sin.calculate(x);
        if (Math.abs(sinX) < EPSILON) {
            throw new ArithmeticException(
                "cot(x) не определён: sin(x) ≈ 0, x ≈ πk, x = " + x);
        }
        return cos.calculate(x) / sinX;
    }
}
