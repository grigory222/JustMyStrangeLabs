package org.example;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.exceptions.CsvValidationException;

public class Main {
    public static void main(String[] args) throws IOException, CsvValidationException {

        Map<String, String> values = new CSVReaderHeaderAware(new FileReader("src/main/resources/1.csv")).readMap();
        System.out.println(values);
        System.out.println("Hello world!");
    }
}