package com.example.lab2.util;

public class Calculator {
    public Calculator() {
    }

    public boolean calculate(float x, float y, float r) {
        if (x >= -r/2 && x <= 0 && y >= 0 && y <= r) {
            return true;
        } else if (x >= 0 && y <= -x + r && y >= 0) {
            return true;
        } else if ((x*x + y*y <= r*r / 4) && x <= 0 && y <= 0) {
            return true;
        }
        return false;
    }
}
