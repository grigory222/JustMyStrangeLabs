package com.example.lab2.util;

import com.example.lab2.util.ValidationException;

public class Validator {
    public Validator() {
    }

    public void validateParams(String x, String y, String r) throws ValidationException {
        if (x != null && !x.isEmpty()) {
            if (y != null && !y.isEmpty()) {
                if (r != null && !r.isEmpty()) {
                    try {
                        float r_ = Float.parseFloat(r);
                        if (r_ < 2 || r_ > 5) {
                            throw new ValidationException("r must be in range [2;5]");
                        }
                    } catch (NumberFormatException var6) {
                        throw new ValidationException("r must be a float");
                    }

                    float x_;
                    try {
                        x_ = Float.parseFloat(x);
                        if (x_ < -2 || x_ > 2) {
                            throw new ValidationException("x must be in range [-2;2]");
                        }
                    } catch (NumberFormatException var8) {
                        throw new ValidationException("x must be float");
                    }

                    try {
                        float y_ = Float.parseFloat(y);
                        if (y_ < -5.0F || y_ > 5.0F) {
                            throw new ValidationException("y must be in range [-5;5]");
                        }
                    } catch (NumberFormatException var7) {
                        throw new ValidationException("y must be a float");
                    }

                } else {
                    throw new ValidationException("r is empty");
                }
            } else {
                throw new ValidationException("y is empty");
            }
        } else {
            throw new ValidationException("x is empty");
        }
    }
}
