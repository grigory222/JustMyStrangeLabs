package ru.ifmo.se.consoleReader;

import org.w3c.dom.ls.LSOutput;
import ru.ifmo.se.csv.CsvHandler;
import ru.ifmo.se.entity.LabWork;
import ru.ifmo.se.listener.Listener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashSet;

public class ConsoleReader implements Runnable {
    private final CsvHandler csv;
    private final Listener listener;

    public ConsoleReader(CsvHandler csv, Listener listener) {
        this.csv = csv;
        this.listener = listener;
    }

    @Override
    public void run() {
        try {
            // Создаем BufferedReader для чтения ввода с консоли
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Введите команду (для выхода введите exit):");

            // Бесконечный цикл для чтения ввода до тех пор, пока не будет введено "exit"
            String input;
            while (!(input = reader.readLine()).equals("exit")) {
                if (input.equals("save")) {
                    csv.saveToFile();
                }
            }
            csv.saveToFile(); // сохранить при выходе
            reader.close();
            exit();
        } catch (IOException e) {
            System.out.println("Ошибка чтения ввода из stdin");
        }
    }

    private void exit() throws IOException {
        listener.exit();
        System.exit(0);
    }
}