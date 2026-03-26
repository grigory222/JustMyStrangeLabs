package ru.itmo.tpo;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class CsvWriter {

    private CsvWriter() {
    }

    public static void write(MathFunction function,
                             double from, double to, double step,
                             String filename) throws IOException {
        if (step <= 0) {
            throw new IllegalArgumentException("Шаг должен быть положительным");
        }
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            pw.println("X,Результаты модуля (X)");
            for (double x = from; x <= to + step * 1e-9; x += step) {
                try {
                    double result = function.calculate(x);
                    pw.printf("%.6f,%.10f%n", x, result);
                } catch (ArithmeticException | IllegalArgumentException e) {
                    pw.printf("%.6f,undefined%n", x);
                }
            }
        }
    }
}
