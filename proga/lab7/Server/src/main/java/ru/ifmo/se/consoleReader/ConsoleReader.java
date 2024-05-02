package ru.ifmo.se.consoleReader;

import ru.ifmo.se.db.DbManager;
import ru.ifmo.se.listener.Listener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleReader implements Runnable {
    private final Listener listener;
    private final DbManager db;
    public ConsoleReader(DbManager db, Listener listener) {
        this.listener = listener;
        this.db = db;
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
                    db.saveCollection();
                }
            }
            db.saveCollection(); // сохранить при выходе
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