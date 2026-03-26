package ru.itmo.tpo.system;

import ru.itmo.tpo.MathFunction;

public class LogPart implements MathFunction {

    private static final double EPSILON = 1e-10;

    private final MathFunction ln;
    private final MathFunction log2;
    private final MathFunction log3;
    private final MathFunction log5;
    private final MathFunction log10;

    public LogPart(MathFunction ln, MathFunction log2, MathFunction log3,
                   MathFunction log5, MathFunction log10) {
        this.ln = ln;
        this.log2 = log2;
        this.log3 = log3;
        this.log5 = log5;
        this.log10 = log10;
    }

    @Override
    public double calculate(double x) {
        if (x <= 0) {
            throw new IllegalArgumentException("LogPart определена только для x > 0, получено: " + x);
        }

        double lnX = ln.calculate(x);
        double log2X = log2.calculate(x);
        double log3X = log3.calculate(x);
        double log5X = log5.calculate(x);
        double log10X = log10.calculate(x);

        if (Math.abs(log10X) < EPSILON) {
            throw new ArithmeticException(
                    "Формула не определена: log₁₀(x) = 0 при x = 1");
        }

        double term1 = (log3X * log10X) / log10X - log2X;

        double term2 = log2X * (lnX * log10X);

        double term3 = ((lnX + log5X * log5X * log5X) * lnX) * log10X;

        return term1 + term2 + term3;
    }
}
