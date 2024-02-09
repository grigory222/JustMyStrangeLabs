package ru.ifmo.se.csv;

import com.opencsv.CSVWriter;
import com.opencsv.bean.*;
import com.opencsv.bean.comparator.LiteralComparator;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import lombok.NoArgsConstructor;
import ru.ifmo.se.entity.*;
import org.apache.commons.collections4.comparators.FixedOrderComparator;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class CsvHandler {
    private static final List<String> COLUMNS_ORDER = Arrays.asList(
            "id", "name", "x", "y", "creationDate", "minimalPoint",
            "tunedInWorks", "difficulty", "authorName", "birthday",
            "height", "weight", "hairColor"
    );


    public static class CsvException extends RuntimeException {
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


    private static String getHeaders(Object object) {
        if (object == null) return "null";

        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        StringBuilder csvData = new StringBuilder();

        // Append headers
        for (Field field : fields) {
            Class<?> fieldClass = field.getType();
            field.setAccessible(true);
            if (fieldClass.isPrimitive() || fieldClass.isEnum() || fieldClass == String.class || fieldClass == Integer.class
                    || fieldClass == Date.class || fieldClass == LocalDate.class || fieldClass == Double.class) {
                csvData.append(field.getName()).append(",");
            } else {
                try {
                    csvData.append(getHeaders(field.get(object))).append(",");;
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        csvData.deleteCharAt(csvData.length() - 1);

        return csvData.toString();
    }

    private static String getValues(Object object) {
        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        StringBuilder csvData = new StringBuilder();

        for (Field field : fields) {
            Class<?> fieldClass = field.getType();
            field.setAccessible(true);
            try {
                if (fieldClass == Date.class){
                    csvData.append(new SimpleDateFormat("yyyy-MM-dd").format(field.get(object))).append(",");
                } else
                if (fieldClass.isPrimitive() || fieldClass.isEnum() || fieldClass == String.class || fieldClass == Integer.class
                        || fieldClass == LocalDate.class || fieldClass == Double.class) {
                    csvData.append(field.get(object)).append(",");
                } else {
                    csvData.append(getValues(field.get(object))).append(",");
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

        }
        csvData.deleteCharAt(csvData.length() - 1);
        return csvData.toString();
    }

    public static void writeRows(FileWriter writer, List<LabWork> rows) throws IOException {
        if (rows.isEmpty()) return;

        writer.write(getHeaders(rows.get(0)) + "\n");

        for (LabWork row : rows){
            writer.write(getValues(row) + "\n");
        }
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