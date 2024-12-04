package web.backend.lab4.util;


import lombok.Getter;

public class Validator {
    @Getter
    private static Validator validator = new Validator();
    private Validator() {
    }

    public void validateParams(Double x, Integer y, Integer r) throws ValidationException {
        if (x == null || x < -5 || x > 5){
            throw new ValidationException("Неверный x");
        }
        if (y == null || y < -4 || y > 4){
            throw new ValidationException("Неверный y");
        }
        if (r == null || r < 1 || r > 5){
            throw new ValidationException("Неверный r");
        }
    }
}