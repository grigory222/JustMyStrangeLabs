package ru.ifmo.se.runner;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import ru.ifmo.se.command.*;
import ru.ifmo.se.controller.Invoker;
import ru.ifmo.se.entity.LabWork;
import ru.ifmo.se.receiver.CollectionHandler;
import ru.ifmo.se.receiver.CollectionReceiver;
import ru.ifmo.se.receiver.IoReceiver;
import ru.ifmo.se.receiver.StorageReceiver;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import static ru.ifmo.se.csv.CsvHandler.*;

public class Runner {

    private final PrintWriter printWriter;
    private final BufferedReader bufferedReader;
    // создаем экземпляры Получателей, чтобы каждая команда знала своего исполнителя
    private IoReceiver ioReceiver;
    private CollectionReceiver collectionReceiver;
    private StorageReceiver storageReceiver;
    private CollectionHandler collectionHandler; // обработчик crud операций с коллекцией
    private Invoker invoker;
    private LinkedHashSet<LabWork> collection;


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
        Command exitCmd = new ExitCommand(bufferedReader, printWriter, "save");
        // IoReceiver
        Command helpCmd = new HelpCommand(ioReceiver, bufferedReader, printWriter, "help"); // не передаю bufferedReader, поому что эти команды не читают
        Command infoCmd = new InfoCommand(ioReceiver, bufferedReader, printWriter, "info"); // ничего с клавиатуры, в отличии от addCommand, например
        Command showCmd = new ShowCommand(ioReceiver, bufferedReader, printWriter, "show");
        Command printUniqueDifficultyCmd = new PrintUniqueDifficultyCommand(ioReceiver, bufferedReader, printWriter, "print_unique_difficulty");
        Command printFieldAscendingCmd = new PrintFieldAscendingCommand(ioReceiver, bufferedReader, printWriter, "print_field_ascending_author");
        // CollectionReceiver
        Command addCmd = new AddCommand(collectionReceiver, bufferedReader, printWriter, "add");
        // ...
        // ...
        // ...
        // ...
        // StorageReceiver
        Command saveCmd = new SaveCommand(storageReceiver, bufferedReader, printWriter, "save");
        // ...
        // ...
        // ...
        // ...
        // ...


        // кладём команды в мапу
        cmdMap.put("exit", exitCmd);
        cmdMap.put("help", helpCmd);
        cmdMap.put("info", infoCmd);
        cmdMap.put("show", showCmd);
        cmdMap.put("print_unique_difficulty", printUniqueDifficultyCmd);
        cmdMap.put("print_field_ascending_author", printFieldAscendingCmd);
        cmdMap.put("add", addCmd);
        cmdMap.put("save", saveCmd);


        return cmdMap;
    }

    void initReceivers(){
        // создаем экземпляры Получателей, чтобы каждая команда знала своего исполнителя
        ioReceiver = new IoReceiver(collection, collectionHandler, printWriter, bufferedReader);
        collectionReceiver = new CollectionReceiver(collection, collectionHandler);
        storageReceiver = new StorageReceiver(collection, collectionHandler);
    }

    void runCommands(){
        String line;
        do{
            try {
                line = bufferedReader.readLine();
            } catch (IOException e) {
                printWriter.println("Ошибка ввода!");
                return;
            }
            if (!invoker.executeCommand(line))
                printWriter.println("Неверная команда!");
        } while(!line.equals("exit"));

    }

    public void loadFromCsv(String fileName) {
        try {
            Reader reader = new BufferedReader(new FileReader(fileName));
            List<LabWork> labWorks = parseCSV(fileName);
            collection = new LinkedHashSet<>(labWorks);
            collectionHandler = new CollectionHandler(collection);
        } catch (FileNotFoundException e) {
            printWriter.write("Невозможно открыть файл " + fileName);
        } catch (IOException e) {
            printWriter.write("Не удаётся прочитать файл " + fileName);
        }
    }





    // Пользовательский метод. Запускает инициализации и цикл чтения команд
    public void run(String fileName)  {
        loadFromCsv(fileName);
        initReceivers();
        initInvoker();
        runCommands();
    }
}


