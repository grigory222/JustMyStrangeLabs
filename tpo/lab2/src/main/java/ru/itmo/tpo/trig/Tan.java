package ru.itmo.tpo.trig;

import ru.itmo.tpo.MathFunction;

public class Tan implements MathFunction {

    private static final double EPSILON = 1e-10;

    private final MathFunction sin;
    private final MathFunction cos;

    public Tan(MathFunction sin, MathFunction cos) {
        this.sin = sin;
        this.cos = cos;
    }

    @Override
    public double calculate(double x) {
        double cosX = cos.calculate(x);
        if (Math.abs(cosX) < EPSILON) {
            throw new ArithmeticException(
                    "tan(x) не определён: cos(x) = 0, x = π/2 + πk, x = " + x);
        }
        return sin.calculate(x) / cosX;
    }
}
