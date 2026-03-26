package ru.itmo.tpo.system;

import ru.itmo.tpo.MathFunction;

public class TrigPart implements MathFunction {

    private final MathFunction tan;
    private final MathFunction cos;
    private final MathFunction cot;

    public TrigPart(MathFunction tan, MathFunction cos, MathFunction cot) {
        this.tan = tan;
        this.cos = cos;
        this.cot = cot;
    }

    @Override
    public double calculate(double x) {
        double tanX = tan.calculate(x);

        if (tanX == 0.0 || Double.isNaN(tanX)) {
            throw new ArithmeticException(
                    "Формула не определена: tan(x) = 0, деление 0/0 при x = " + x);
        }

        double cosX = cos.calculate(x);
        double cotX = cot.calculate(x);

        // ((tan/tan)^2) / cos  +  cot^3
        double tanOverTan = tanX / tanX;
        double inner = (tanOverTan * tanOverTan) / cosX + cotX * cotX * cotX;

        return inner * inner;
    }
}
