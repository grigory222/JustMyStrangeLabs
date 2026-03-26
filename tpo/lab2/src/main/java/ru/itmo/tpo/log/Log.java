package ru.itmo.tpo.log;

import ru.itmo.tpo.MathFunction;

public class Log implements MathFunction {

    private final MathFunction ln;
    private final double lnBase;

    public Log(MathFunction ln, double base) {
        if (base <= 0.0 || base == 1.0) {
            throw new IllegalArgumentException(
                    "Основание логарифма должно быть > 0 и ≠ 1, получено: " + base);
        }
        this.ln = ln;
        this.lnBase = ln.calculate(base);
    }

    @Override
    public double calculate(double x) {
        return ln.calculate(x) / lnBase;
    }
}
