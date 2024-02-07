package ru.ifmo.se.runner;

import ru.ifmo.se.command.*;
import ru.ifmo.se.controller.Invoker;
import ru.ifmo.se.receiver.*;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Runner<T> {

    private final PrintWriter printWriter;
    private final BufferedReader bufferedReader;
    // создаем экземпляры Получателей, чтобы каждая команда знала своего исполнителя
    IoReceiver<T> ioReceiver;
    CollectionReceiver<T> collectionReceiver;
    StorageReceiver<T> storageReceiver;
    private Invoker invoker;

    // Конструктор без параметров будет использовать PrintWriter с stdout и BufferedReader с stdin
    public Runner(){
        printWriter = new PrintWriter(System.out, true);
        bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    }

    // Конструктор с явным определением printWriter'a и bufferedReader'а
    public Runner(PrintWriter printWriter, BufferedReader bufferedReader){
        this.printWriter = printWriter;
        this.bufferedReader = bufferedReader;
    }

    // Инициализация отправителя - invoker
    private void initInvoker(){
        invoker = new Invoker(fillCommandMap());
    }

    private Map<String, Command> fillCommandMap(){
        Map<String, Command> cmdMap = new HashMap<>();


        // создаем экземпляры команд, чтобы положить их в мапу, чтобы инвокер из неё вызывал их
        // IoReceiver
        Command helpCmd = new HelpCommand<T>(ioReceiver, "help"); // не передаю bufferedReader, поому что эти команды не читают
        Command infoCmd = new InfoCommand<T>(ioReceiver, "info"); // ничего с клавиатуры, в отличии от addCommand, например
        Command showCmd = new ShowCommand<T>(ioReceiver, "show");
        Command printUniqueDifficultyCmd = new PrintUniqueDifficultyCommand<T>(ioReceiver, "print_unique_difficulty");
        Command printFieldAscendingCmd = new PrintFieldAscendingCommand<T>(ioReceiver, "print_field_ascending_author");
        // CollectionReceiver
        Command addCmd = new AddCommand<T>(collectionReceiver, bufferedReader, "add");
        // ...
        // ...
        // ...
        // ...
        // StorageReceiver
        // ...
        // ...
        // ...
        // ...
        // ...


        // кладём команды в мапу
        cmdMap.put("help", helpCmd);
        cmdMap.put("info", infoCmd);
        cmdMap.put("show", showCmd);
        cmdMap.put("print_unique_difficulty", printUniqueDifficultyCmd);
        cmdMap.put("print_field_ascending_author", printFieldAscendingCmd);
        cmdMap.put("add", addCmd);

        return cmdMap;
    }

    void initReceivers(){
        // создаем экземпляры Получателей, чтобы каждая команда знала своего исполнителя
        ioReceiver = new IoReceiver<>(printWriter, bufferedReader);
        collectionReceiver = new CollectionReceiver<>();
        storageReceiver = new StorageReceiver<>();
    }

    void runCommands() throws IOException{
        String line;
        do{
            line = bufferedReader.readLine();
            invoker.executeCommand(line);
        } while(!line.equals("exit"));

    }

    // Пользовательский метод. Запускает инициализации и цикл чтения команд
    public void run() throws IOException{
        initReceivers();
        initInvoker();
        runCommands();
    }
}
