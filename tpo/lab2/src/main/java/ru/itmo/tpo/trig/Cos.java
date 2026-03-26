package ru.itmo.tpo.trig;

import ru.itmo.tpo.MathFunction;

public class Cos implements MathFunction {

    private static final double PI = Sin.PI;

    private final MathFunction sin;

    public Cos(MathFunction sin) {
        this.sin = sin;
    }

    @Override
    public double calculate(double x) {
        if (Double.isNaN(x) || Double.isInfinite(x)) {
            throw new IllegalArgumentException("x must be finite, got: " + x);
        }

        double norm = x % (2.0 * PI);
        if (norm > PI) norm -= 2.0 * PI;
        else if (norm < -PI) norm += 2.0 * PI;

        double sinX = sin.calculate(x);
        double cosAbsSq = 1.0 - sinX * sinX;
        double cosAbs = Math.sqrt(cosAbsSq);

        if (norm > PI / 2.0 || norm < -PI / 2.0) {
            return -cosAbs;
        }
        return cosAbs;
    }
}
