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

    public static boolean validateDifficulty(String line) {
        try{
            Difficulty.valueOf(line);
            return true;
        } catch(IllegalArgumentException e){
            return false;
        }
    }

    public static boolean validateCoordinates(String line) {
        return line.matches("^\\s*-?\\d+\\s+-?\\d+(\\.\\d+)?\\s*$");
    }

    public static boolean validateName(String line){
        return line.matches("[а-яА-Яa-zA-Z0-9_ ]{2,}");
    }

    private static String readAndParseName(BufferedReader reader, PrintWriter printer, PrintWriter infoPrinter) throws IOException {
        String line;
        do {
            printer.print("Введите название лабораторной работы (разрешены латиница и кириллица, цифры и '_'): ");
            printer.flush();
            line = reader.readLine();
        }while (line != null && !validateName(line));
        return line;
    }

    private static Coordinates readAndParseCoordinates(BufferedReader reader, PrintWriter printer, PrintWriter infoPrinter) throws IOException {
        String line;
        String[] parsed;
        while (true) {

            try {
                printer.print("Введите координаты x и y через пробел: ");
                printer.flush();
                line = reader.readLine();
                if (line == null) return null;
                if (!validateCoordinates(line))
                    throw new Exception();
                parsed = line.strip().replaceAll("\\s{2,}", " ").split(" ");

                Integer.parseInt(parsed[0]);
                Double.parseDouble(parsed[1]);

            } catch (Exception e){
                printer.println("Что-то пошло не так... Введите координаты x и y через пробел. x - целое, y - дробное");
                continue;
            }

            double a = Double.parseDouble(parsed[1]);

            if (a - 48 <= Double.MIN_VALUE) { // 48.00000000000001
                //System.out.println(a - 48.0);
                break;
            }
            infoPrinter.println("Максимальное значение координаты y: 48");
        }
        return new Coordinates(Integer.parseInt(parsed[0]), Double.parseDouble(parsed[1]));
    }
    private static Integer readAndParseMinimalPoint(BufferedReader reader, PrintWriter printer, PrintWriter infoPrinter) throws IOException {
        String line;
        while (true) {
            try {
                printer.print("Введите минимальную оценку: ");
                printer.flush();
                line = reader.readLine();
                if (line == null) return null;
                Integer.parseInt(line);
            } catch (Exception e){
                infoPrinter.println("Что-то пошло не так... Введите целое число");
                continue;
            }

            if (Integer.parseInt(line) > 0)
                break;
            infoPrinter.println("Значение должно быть больше 0");
        }
        return Integer.parseInt(line);
    }

    private static Integer readAndParseTunedInWorks(BufferedReader reader, PrintWriter printer, PrintWriter infoPrinter) throws IOException {
        String line;
        while (true) {
            try {
                printer.print("Введите tunedInWorks (int): ");
                printer.flush();
                line = reader.readLine();
                if (line == null) return null;
                return Integer.parseInt(line);
            } catch (NumberFormatException e){
                printer.println("Что-то пошло не так... Введите целочисленное число.");
            }
        }

    }

    private static Difficulty readAndParseDifficulty(BufferedReader reader, PrintWriter printer, PrintWriter infoPrinter) throws IOException {
        String line;
        do {
            printer.println("Введите сложность лабораторной работы ");
            for (Difficulty cur: Difficulty.values()){
                printer.println(cur.name());
            }
            printer.print("Ваш выбор: ");
            printer.flush();
            if ((line = reader.readLine()) == null)
                break;
            line = line.toUpperCase();
        } while (!validateDifficulty(line));
        if (line == null) return null;
        return Difficulty.valueOf(line);
    }

    public static LabWork readLabWork(BufferedReader reader, PrintWriter printer, PrintWriter infoPrinter) throws IOException {
        String line;
        LabWork result = new LabWork();
        String name = readAndParseName(reader, printer, infoPrinter);
        Coordinates coordinates = readAndParseCoordinates(reader, printer, infoPrinter);
        Integer minimalPoint = readAndParseMinimalPoint(reader, printer, infoPrinter);
        Integer tunedItWorks = readAndParseTunedInWorks(reader, printer, infoPrinter);
        Difficulty difficulty = readAndParseDifficulty(reader, printer, infoPrinter);

        if (name == null || coordinates == null || minimalPoint == null || tunedItWorks == null || difficulty == null)
            return null;

        result.setName(name);
        result.setCoordinates(coordinates);
        result.setMinimalPoint(minimalPoint);
        result.setTunedInWorks(tunedItWorks);
        result.setDifficulty(difficulty);

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
