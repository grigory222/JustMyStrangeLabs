package ru.itmo.tpo;

public class TangentFunction {

    private static final double PRECISION = 1e-10;
    private static final int MAX_ITERATIONS = 100;

    public static double tg(double x) {
        double sinX = sin(x);
        double cosX = cos(x);
        
        if (abs(cosX) < 1e-10) {
            throw new IllegalArgumentException("tg(x) не определен в точке x = π/2 + πk");
        }
        
        return sinX / cosX;
    }
    
    private static double sin(double x) {
        double sum = 0.0;
        double term = x;
        double xSquared = x * x;
        
        for (int n = 0; n < MAX_ITERATIONS; n++) {
            sum += term;
            term *= -xSquared / ((2*n + 2) * (2*n + 3));
            
            if (abs(term) < PRECISION) {
                break;
            }
        }
        
        return sum;
    }
    
    private static double cos(double x) {
        double sum = 0.0;
        double term = 1.0;
        double xSquared = x * x;
        
        for (int n = 0; n < MAX_ITERATIONS; n++) {
            sum += term;
            term *= -xSquared / ((2*n + 1) * (2*n + 2));
            
            if (abs(term) < PRECISION) {
                break;
            }
        }
        
        return sum;
    }
    
    private static double abs(double value) {
        return value < 0 ? -value : value;
    }
}
