package ru.ifmo.se;

import java.io.IOException;
import com.opencsv.exceptions.CsvException;
import ru.ifmo.se.runner.Runner;

public class Main {
    public static void main(String[] args) {
        Runner r = new Runner();
        r.run(args[0]);
    }
}