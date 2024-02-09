package ru.ifmo.se.entity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class LabWorkReader {


    public static boolean validateInt(String line){
        return line.matches("^\\s*-?\\d+\\s*$");
    }

    public static boolean validateDouble(String line){
        //return line.matches("^\\s+-?\\d+(\\.\\d+)?\\s*$");
        return line.matches("^-?\\d+(\\.\\d+)?$");
    }

    private static boolean validateDifficulty(String line) {
        try{
            Difficulty.valueOf(line);
            return true;
        } catch(IllegalArgumentException e){
            return false;
        }
    }

    private static boolean validateTunedInWorks(String line) {
        return validateInt(line);
    }

    private static boolean validateMinimalPoint(String line) {
        return validateInt(line);
    }

    private static boolean validateCoordinates(String line) {
        return line.matches("^\\s*-?\\d+\\s+-?\\d+(\\.\\d+)?\\s*$");
    }

    public static boolean validateName(String line){
        return line.matches("[а-яА-Яa-zA-Z0-9_ ]{2,}");
    }

    private static String readAndParseName(BufferedReader reader, PrintWriter printer) throws IOException {
        String line;
        do {
            printer.print("Введите название лабороторной работы: ");
            printer.flush();
            line = reader.readLine();
        }while (!validateName(line));
        return line;
    }

    private static Coordinates readAndParseCoordinates(BufferedReader reader, PrintWriter printer) throws IOException {
        String line;
        String[] parsed;
        while (true) {
            do {
                printer.print("Введите координаты x и y через пробел: ");
                printer.flush();
                line = reader.readLine();
            } while (!validateCoordinates(line));
            parsed = line.strip().replaceAll("[\\s]{2,}", " ").split(" ");

            // проверка не ввёл ли пользователь число, выходящее за границы
            try {
                Integer.parseInt(parsed[0]);
                Double.parseDouble(parsed[1]);
            } catch (Exception e){
                printer.println("Что-то пошло не так... Возможно, вы ввели слишком большое число");
                continue;
            }

            if (Double.parseDouble(parsed[1]) <= 48)
                break;
            printer.println("Максимальное значение координаты y: 48");
        }
        return new Coordinates(Integer.parseInt(parsed[0]), Double.parseDouble(parsed[1]));
    }
    private static Integer readAndParseMinimalPoint(BufferedReader reader, PrintWriter printer) throws IOException {
        String line;
        while (true) {
            do {
                printer.print("Введите минимальную оценку: ");
                printer.flush();
                line = reader.readLine();
            } while (!validateMinimalPoint(line));

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

    private static Integer readAndParseTunedInWorks(BufferedReader reader, PrintWriter printer) throws IOException {
        String line;
        while (true) {
            do {
                printer.print("Введите tunedInWorks: ");
                printer.flush();
                line = reader.readLine();
            } while (!validateTunedInWorks(line));

            // проверка не ввёл ли пользователь число, выходящее за границы
            try {
                Integer.parseInt(line);
                break;
            } catch (Exception e){
                printer.println("Что-то пошло не так... Возможно, вы ввели слишком большое число");
            }
        }
        return Integer.parseInt(line);
    }

    private static Difficulty readAndParseDifficulty(BufferedReader reader, PrintWriter printer) throws IOException {
        String line;
        do {
            printer.println("Введите сложность лабораторной работы ");
            for (Difficulty cur: Difficulty.values()){
                printer.println(cur.name());
            }
            printer.print("Ваш выбор: ");
            printer.flush();
            line = reader.readLine().toUpperCase();
        } while (!validateDifficulty(line));
        return Difficulty.valueOf(line);
    }

    public static LabWork readLabWork(BufferedReader reader, PrintWriter printer) throws IOException {
        String line;
        LabWork result = new LabWork();

        result.setName(readAndParseName(reader, printer));
        result.setCoordinates(readAndParseCoordinates(reader, printer));
        result.setMinimalPoint(readAndParseMinimalPoint(reader, printer));
        result.setTunedInWorks(readAndParseTunedInWorks(reader, printer));
        result.setDifficulty(readAndParseDifficulty(reader, printer));

        do{
            printer.print("Хотите указать автора?(y/n) ");
            printer.flush();
        }while  (!((line = reader.readLine().toLowerCase()).equals("y") || line.equals("n")));

        if (line.equals("y"))
            result.setAuthor(PersonReader.readPerson(reader, printer));
        else
            result.setAuthor(null);

        return result;
    }


}