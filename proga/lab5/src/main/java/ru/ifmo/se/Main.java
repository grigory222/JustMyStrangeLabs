package ru.ifmo.se;

import ru.ifmo.se.runner.InvalidCSVException;
import ru.ifmo.se.runner.Runner;

public class Main {
    public static void main(String[] args) throws InvalidCSVException {
        Runner r = new Runner();
        if (args.length == 0){
            System.out.println("Введите имя CSV файла через пробел");
            return;
        }
        r.run(args[0]);
    }
}