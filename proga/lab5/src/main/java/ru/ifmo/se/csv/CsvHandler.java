package ru.ifmo.se.csv;

import com.opencsv.CSVWriter;
import com.opencsv.bean.*;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import lombok.NoArgsConstructor;
import ru.ifmo.se.entity.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CsvHandler {

    public static class CsvException extends RuntimeException{
        public CsvException(String message) {
            super(message);
        }
    }

    public static List<LabWork> parseCSV(String filePath) throws IOException, CsvException {
        FileReader fileReader = new FileReader(filePath);

        CsvToBean<LabWork> csvToBean = new CsvToBeanBuilder<LabWork>(fileReader)
                .withType(LabWork.class)
                .withSeparator(',')
                .withIgnoreLeadingWhiteSpace(true)
                .build();

        return csvToBean.parse();
    }

    public static void writeRowsToCsv(String filePath, List<LabWork> rows)
            throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {

        Writer writer = new FileWriter("src/main/resources/result.csv");
        StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer).build();
        beanToCsv.write(rows);
        writer.close();

    }

    @NoArgsConstructor
    public static class DateConverter extends AbstractBeanField<LocalDate, String> {
        @Override
        protected Object convert(String value) {
            return java.time.LocalDate.parse(value);
        }
    }

    @NoArgsConstructor
    public static class DateConverterSecond extends AbstractBeanField<java.util.Date, String> {
        @Override
        protected Object convert(String value) {
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            try {
                return formatter.parse(value);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

        }
    }

    @NoArgsConstructor
    public static class DifficultyConverter extends AbstractBeanField<Difficulty, String> {
        @Override
        protected Object convert(String value) {
            return Difficulty.valueOf(value);
        }
    }

    @NoArgsConstructor
    public static class ColorConverter extends AbstractBeanField<Color, String> {
        @Override
        protected Object convert(String value) {
            return Color.valueOf(value);
        }
    }


}