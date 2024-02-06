package ru.ifmo.se.runner;

import ru.ifmo.se.command.Command;
import ru.ifmo.se.controller.Invoker;
import ru.ifmo.se.io.*;
import ru.ifmo.se.receiver.*;



import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class Runner {
    private final Reader reader;
    private final PrintWriter printWriter;
    private Invoker invoker;

    // Конструктор без параметров будет использовать StdinReader и PrintReader с stdout
    Runner(){
        reader = new StdinReader();
        printWriter = new PrintWriter(System.out, true);
    }

    // Конструктор с явным определением reader'а и printWriter'a
    Runner(Reader reader, PrintWriter printWriter){
        this.reader = reader;
        this.printWriter = printWriter;
    }

    // Инициализация отправителя - invoker
    private void initInvoker(){
        invoker = new Invoker(fillCommandMap(reader));
    }

    private Map<String, Command> fillCommandMap(Reader reader){
        Map<String, Command> cmdMap = new HashMap<>();

        // создаем экземпляры Получателей, чтобы каждая команда знала своего исполнителя
        Receiver<Integer> ioReceiver = new IoReceiver<>(printWriter);
        Receiver<Integer> collectionReceiver = new CollectionReceiver<>();
        Receiver<Integer> storageReceiver = new StorageReceiver<>();

        // создаем экземпляры команд, чтобы положить их в мапу, чтобы инвокер из неё вызывал их
        Command helpCmd = new HelpCommand(ioReceiver);
        // ....

        // кладём команды в мапу
        сщьь

    }

    // Пользовательский метод. Запускает инициализации и цикл чтения команд
    public void run(){

    }
}
