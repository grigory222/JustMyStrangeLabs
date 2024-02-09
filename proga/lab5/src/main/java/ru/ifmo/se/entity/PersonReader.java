package ru.ifmo.se.entity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static ru.ifmo.se.entity.LabWorkReader.*;

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
        }while (!validateName(line));
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

            try {
                date = formatter.parse(line);
            } catch (ParseException e) {}
        }while(!validateDate(line));
        return date;
    }

    private static int readAndParseHeight(BufferedReader reader, PrintWriter printer) throws IOException {
        String line;
        while (true) {
            do {
                printer.print("Введите рост: ");
                printer.flush();
                line = reader.readLine();
            } while (!validateInt(line));

            // проверка не ввёл ли пользователь число, выходящее за границы
            try {
                Integer.parseInt(line);
            } catch (Exception e){
                printer.println("Что-то пошло не так... Возможно, вы ввели слишком большое число");
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
            do {
                printer.print("Введите вес: ");
                printer.flush();
                line = reader.readLine();
            } while (!validateDouble(line));

            // проверка не ввёл ли пользователь число, выходящее за границы
            try {
                Double.parseDouble(line);
            } catch (Exception e){
                printer.println("Что-то пошло не так... Возможно, вы ввели слишком большое число");
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
            line = reader.readLine().toUpperCase();
        } while (!validateHairColor(line));
        return Color.valueOf(line);
    }


    public static Person readPerson(BufferedReader reader, PrintWriter printer) throws IOException {
        Person result = new Person();

        result.setAuthorName(readAndParseName(reader, printer));
        result.setBirthday(readAndParseBirthday(reader, printer));
        result.setHeight(readAndParseHeight(reader, printer));
        result.setWeight(readAndParseWeight(reader, printer));
        result.setHairColor(readAndParseHairColor(reader, printer));
        return result;
    }


}
