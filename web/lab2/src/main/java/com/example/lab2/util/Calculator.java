package com.example.lab2.util;

public class Calculator {
    public Calculator() {
    }

    public boolean calculate(int x, float y, int r) {
        if (-r <= x && x <= 0 && 0.0F <= y && y <= (float)(r / 2)) {
            return true;
        } else if (x >= 0 && y <= 0.0F && Math.sqrt((double)((float)(x * x) + y * y)) <= (double)r) {
            return true;
        } else {
            return x >= 0 && y >= 0.0F && y <= (float)(-x + r / 2);
        }
    }
}
