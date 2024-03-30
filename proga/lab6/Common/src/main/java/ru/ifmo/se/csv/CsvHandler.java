package ru.ifmo.se.csv;

import com.opencsv.bean.*;
import com.opencsv.enums.CSVReaderNullFieldIndicator;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import lombok.NoArgsConstructor;
import ru.ifmo.se.entity.Color;
import ru.ifmo.se.entity.Difficulty;
import ru.ifmo.se.entity.LabWork;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class CsvHandler {
    private static final String[] COLUMNS_ORDER = new String[]{
            "id", "name", "x", "y", "creationDate", "minimalPoint",
            "tunedInWorks", "difficulty", "authorName", "birthday",
            "height", "weight", "hairColor"
    };


    public static List<LabWork> parseCSV(String filePath, PrintWriter logger) throws IOException {
        FileReader fileReader = new FileReader(filePath);

        CsvToBean<LabWork> csvToBean = new CsvToBeanBuilder<LabWork>(fileReader)
                .withType(LabWork.class)
                .withSeparator(',')
                .withFieldAsNull(CSVReaderNullFieldIndicator.EMPTY_SEPARATORS)
                .withIgnoreLeadingWhiteSpace(true)
                .withThrowExceptions(false)
                .build();

        var result = csvToBean.parse();
        result.forEach(lab -> {if (lab.getAuthor().isEmpty()) lab.setAuthor(null);});
        csvToBean.getCapturedExceptions().forEach((exception) -> { //3
            logger.println("Некорректные данные в CSV файле: \"" + String.join("", exception.getLine()) + "\"");
        });

        return result;
    }

    public static void writeRows(FileWriter writer, List<LabWork> rows) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        String headers = String.join(",", COLUMNS_ORDER);
        writer.write(headers+"\n");

        ColumnPositionMappingStrategy strat = new ColumnPositionMappingStrategy();
        strat.setType(LabWork.class);
        strat.setColumnMapping(COLUMNS_ORDER);

        StatefulBeanToCsv<LabWork> beanToCsv = new StatefulBeanToCsvBuilder<>(writer)
                .withMappingStrategy(strat)
                .withApplyQuotesToAll(false)
                .build();

        beanToCsv.write(rows);
        writer.close();
    }

    @NoArgsConstructor
    public static class DateConverter extends AbstractBeanField<LocalDate, String> {
        @Override
        protected Object convert(String value) {
            return LocalDate.parse(value);
        }
    }

    @NoArgsConstructor
    public static class DateConverterSecond extends AbstractBeanField<Date, String> {
        @Override
        protected Object convert(String value) {
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            try {
                return formatter.parse(value);
            } catch (ParseException | NullPointerException e) {
                return null;
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