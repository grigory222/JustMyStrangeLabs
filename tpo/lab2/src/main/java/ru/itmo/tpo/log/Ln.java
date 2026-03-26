package ru.itmo.tpo.log;

import ru.itmo.tpo.MathFunction;

public class Ln implements MathFunction {

    public static final double LN2 = 0.6931471805599453;
    public static final double LN3 = 1.0986122886681098;
    public static final double LN5 = 1.6094379124341003;
    public static final double LN10 = 2.302585092994046;

    private final double precision;

    public Ln(double precision) {
        this.precision = precision;
    }

    public Ln() {
        this(1e-15);
    }

    @Override
    public double calculate(double x) {
        if (x <= 0) {
            throw new IllegalArgumentException("ln(x) не определён для x <= 0, получено: " + x);
        }
        if (Double.isInfinite(x)) {
            throw new IllegalArgumentException("x должно быть конечным");
        }

        double[] norm = normalize(x);
        double reduced = norm[0];
        int k = (int) norm[1];

        double lnReduced = arctanhSeries(reduced);

        return k * LN2 + lnReduced;
    }

    private static double[] normalize(double x) {
        int k = 0;
        while (x >= 2.0) {
            x /= 2.0;
            k++;
        }
        while (x < 0.5) {
            x *= 2.0;
            k--;
        }
        return new double[]{x, k};
    }

    private double arctanhSeries(double y) {
        double u = (y - 1.0) / (y + 1.0);
        double uSq = u * u;
        double sum = 0.0;
        double term = u;
        int n = 1;

        while (abs(term / n) >= precision) {
            sum += term / n;
            term *= uSq;
            n += 2;
        }
        return 2.0 * sum;
    }

    private static double abs(double v) {
        return v < 0 ? -v : v;
    }
}
