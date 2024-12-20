package web.backend.lab4.util;


import lombok.Getter;

public class Calculator {
    @Getter
    private static Calculator calculator = new Calculator();
    private Calculator() {
    }

    public boolean calculate(int x, double y, int r) {
        if (x <= 0 && y >= 0 && x*x + y*y <= r*r){
            return true;
        }
        if (x <= 0 && y <= 0 && y >= -2*x - r){
            return true;
        }
        if (x >= 0 && y <= 0 && x <= r / 2 && y >= -r) {
            return true;
        }
        return false;
    }
}