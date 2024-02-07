package ru.ifmo.se;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.exceptions.CsvValidationException;
import ru.ifmo.se.runner.Runner;

public class Main {
    public static void main(String[] args) throws IOException, CsvValidationException {
//        BufferedReader br;
//        br = new BufferedReader(new FileReader("foo.in"));
//        br = new BufferedReader(new InputStreamReader(System.in));


        Runner<Integer> r = new Runner<>();
        r.run();
    }
}