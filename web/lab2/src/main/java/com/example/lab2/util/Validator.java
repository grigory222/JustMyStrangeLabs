package com.example.lab2.util;

import com.example.lab2.util.ValidationException;

public class Validator {
    public Validator() {
    }

    public void validateParams(String x, String y, String r) throws ValidationException {
        if (x != null && !x.isEmpty()) {
            if (y != null && !y.isEmpty()) {
                if (r != null && !r.isEmpty()) {
                    int r_;
                    try {
                        r_ = Integer.parseInt(x);
                        if (r_ < -5 || r_ > 3) {
                            throw new ValidationException("x must be in range [-5;3]");
                        }
                    } catch (NumberFormatException var8) {
                        throw new ValidationException("x must be an integer");
                    }

                    try {
                        float y_ = Float.parseFloat(y);
                        if (y_ < -5.0F || y_ > 5.0F) {
                            throw new ValidationException("y must be in range [-5;5]");
                        }
                    } catch (NumberFormatException var7) {
                        throw new ValidationException("y must be a float");
                    }

                    try {
                        r_ = Integer.parseInt(r);
                        if (r_ < 1 || r_ > 5) {
                            throw new ValidationException("r must be in range [1;5]");
                        }
                    } catch (NumberFormatException var6) {
                        throw new ValidationException("r must be an integer");
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
