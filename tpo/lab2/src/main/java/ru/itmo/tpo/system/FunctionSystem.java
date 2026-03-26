package ru.itmo.tpo.system;

import ru.itmo.tpo.MathFunction;

public class FunctionSystem implements MathFunction {

    private final MathFunction trigPart;
    private final MathFunction logPart;

    public FunctionSystem(MathFunction trigPart, MathFunction logPart) {
        this.trigPart = trigPart;
        this.logPart = logPart;
    }

    @Override
    public double calculate(double x) {
        if (x <= 0) {
            return trigPart.calculate(x);
        } else {
            return logPart.calculate(x);
        }
    }
}
