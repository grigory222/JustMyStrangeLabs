package ru.ifmo.se.entity.readers;

import ru.ifmo.se.entity.Color;
import ru.ifmo.se.entity.Person;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PersonReader {

    private static boolean validateHairColor(String line) {
        try{
            Color.valueOf(line);
            return true;
        } catch(IllegalArgumentException e){
            return false;
        }
    }

    private static boolean validateDate(String line) {
        return line.matches("^\\d{4}-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])$");
    }

    private static String readAndParseName(BufferedReader reader, PrintWriter printer) throws IOException {
        String line;
        do {
            printer.print("Введите имя автора: ");
            printer.flush();
            line = reader.readLine();
        }while (line != null && !LabWorkReader.validateName(line));
        return line;
    }
    private static Date readAndParseBirthday(BufferedReader reader, PrintWriter printer) throws IOException {
        String line;
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        do{
            printer.print("Введите день рождения в формате yyyy-MM-dd: ");
            printer.flush();
            line = reader.readLine();
            if (line == null)
                return null;

            try {
                date = formatter.parse(line);
            } catch (ParseException e) {}
        }while(!validateDate(line));
        return date;
    }

    private static Integer readAndParseHeight(BufferedReader reader, PrintWriter printer) throws IOException {
        String line;
        while (true) {
            try {
                printer.print("Введите рост: ");
                printer.flush();
                line = reader.readLine();
                if (line == null) return null;
                Integer.parseInt(line);
            } catch (Exception e){
                printer.println("Что-то пошло не так... Введите целое число");
                continue;
            }

            if (Integer.parseInt(line) > 0)
                break;
            printer.println("Значение должно быть больше 0");
        }
        return Integer.parseInt(line);
    }

    private static Double readAndParseWeight(BufferedReader reader, PrintWriter printer) throws IOException {
        String line;
        while (true) {
            try {
                printer.print("Введите вес: ");
                printer.flush();
                line = reader.readLine();
                if (line == null) return null;
                Double.parseDouble(line);
            } catch (Exception e){
                printer.println("Что-то пошло не так... Введите дробное число");
                continue;
            }

            if (Double.parseDouble(line) > 0)
                break;
            printer.println("Значение должно быть больше 0");
        }
        return Double.parseDouble(line);
    }

    private static Color readAndParseHairColor(BufferedReader reader, PrintWriter printer) throws IOException {
        String line;
        do {
            printer.println("Введите цвет волос");
            for (Color cur: Color.values()){
                printer.println(cur.name());
            }
            printer.print("Ваш выбор: ");
            printer.flush();
            line = reader.readLine();
            if (line == null)
                return null;
            line = line.toUpperCase();
        } while (!validateHairColor(line));
        return Color.valueOf(line);
    }


    public static Person readPerson(BufferedReader reader, PrintWriter printer) throws IOException {
        Person result = new Person();

        String name = readAndParseName(reader, printer);
        Date birthday = readAndParseBirthday(reader, printer);
        Integer height = readAndParseHeight(reader, printer);
        Double weight = readAndParseWeight(reader, printer);
        Color color = readAndParseHairColor(reader, printer);

        if (name == null || birthday == null || height == null || weight == null || color == null)
            return null;

        result.setAuthorName(name);
        result.setBirthday(birthday);
        result.setHeight(height);
        result.setWeight(weight);
        result.setHairColor(color);
        return result;
    }


}
