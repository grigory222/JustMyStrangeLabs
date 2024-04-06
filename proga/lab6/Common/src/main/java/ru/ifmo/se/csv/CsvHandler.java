package ru.ifmo.se.csv;

import com.opencsv.bean.*;
import com.opencsv.enums.CSVReaderNullFieldIndicator;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.ifmo.se.entity.Color;
import ru.ifmo.se.entity.Difficulty;
import ru.ifmo.se.entity.LabWork;
import ru.ifmo.se.entity.readers.LabWorkReader;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

public class CsvHandler {
    private final String[] COLUMNS_ORDER = new String[]{
            "id", "name", "x", "y", "creationDate", "minimalPoint",
            "tunedInWorks", "difficulty", "authorName", "birthday",
            "height", "weight", "hairColor"
    };
    private final PrintWriter infoPrinter;
    private FileWriter fileWriter;
    @Setter
    private LinkedHashSet<LabWork> collection;

    public CsvHandler(PrintWriter printWriter){
        infoPrinter = printWriter;
    }


    private List<LabWork> parseCSV(String fileName, PrintWriter logger) throws IOException {
        FileReader fileReader = new FileReader(fileName);

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

    private void writeRows(FileWriter writer, List<LabWork> rows) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
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

    public void saveToFile(){
        System.out.println("Файл сохранён");
        try {
            writeRows(fileWriter, collection.stream().toList());
        } catch (IOException | CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
            infoPrinter.println("Ошибка при записи в файл");
        }
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

    private boolean checkIds(List<LabWork> labs){
        Set<Integer> ids = new HashSet<>();
        labs.forEach(x -> ids.add(x.getId()));
        if (ids.size() == labs.size()){
            return true;
        }
        // update ids
        for (int i = 0; i < labs.size(); i++){
            labs.get(i).setId(i+1);
        }
        return false;
    }

    public void loadFromCsv(String fileName, LinkedHashSet<LabWork> collection) throws IOException {
        List<LabWork> labWorks = new ArrayList<>(); // создадим пустой список на случай ошибки
        try {
            // проверка можем ли открыть файл
            Reader reader = new BufferedReader(new FileReader(fileName));
            reader.close();
            // парсинг
            labWorks = parseCSV(fileName, infoPrinter);
        } catch (FileNotFoundException e) {
            infoPrinter.println("Невозможно открыть файл");
        } catch (IOException e) {
            infoPrinter.println("Не удаётся прочитать файл");
        } catch (RuntimeException e){
            infoPrinter.println(e.getMessage());
        }

        if (!checkIds(labWorks))
            infoPrinter.println("Обнаружены повторяющиеся ID в CSV файле. Идентификаторы обновлены.");

        if (validateList(labWorks)){
            collection.addAll(labWorks);
        } else{
            System.err.println("Невалидная коллекция");
        }

        // после чтения файла, создать FileWriter для записи
        fileWriter = new FileWriter(fileName);
    }

    private boolean validateLab(LabWork labWork){
        boolean res = labWork.getId() > 0;
        res = res && LabWorkReader.validateName(labWork.getName());
        res = res && labWork.getMinimalPoint() > 0;
        res = res && labWork.getCoordinates().getY() - 48.0 <= Double.MIN_VALUE;

        return res;
    }

    private boolean validateList(List<LabWork> labWorks) {
        var res = labWorks.stream().filter(this::validateLab).toList();
        return res.size() >= labWorks.size();
    }

}