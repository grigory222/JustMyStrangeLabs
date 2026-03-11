package ru.itmo.tpo;

public class TangentFunction {

    private static final double PRECISION = 1e-10;
    private static final double PI = 3.14159265358979323846;

    public static double tg(double x) {
        double reducedX = normalizeAngle(x);

        double sinX = sin(reducedX);
        double cosX = cos(reducedX);
        
        if (abs(cosX) < PRECISION) {
            throw new IllegalArgumentException("tg(x) не определен в точке x = π/2 + πk");
        }
        
        return sinX / cosX;
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

    private static double sin(double x) {
        double sum = 0.0;
        double term = x;
        double xSquared = x * x;
        int n = 0;

        while (abs(term) >= PRECISION) {
            sum += term;
            term *= -xSquared / ((2*n + 2) * (2*n + 3));
            n++;
        }
        
        return sum;
    }
    
    private static double cos(double x) {
        double sum = 0.0;
        double term = 1.0;
        double xSquared = x * x;
        int n = 0;

        while (abs(term) >= PRECISION) {
            sum += term;
            term *= -xSquared / ((2*n + 1) * (2*n + 2));
            n++;
        }
        
        return sum;
    }
    
    private static double abs(double value) {
        return value < 0 ? -value : value;
    }
}
