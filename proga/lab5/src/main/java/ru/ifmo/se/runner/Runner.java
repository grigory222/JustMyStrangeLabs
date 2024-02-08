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
    private File file;


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
        ioReceiver = new IoReceiver(collection, collectionHandler, printWriter, bufferedReader);
        collectionReceiver = new CollectionReceiver(collection, collectionHandler);
        storageReceiver = new StorageReceiver(collection, collectionHandler, file);
    }

    void runCommands() throws IOException{
        String line;
        do{
            line = bufferedReader.readLine();
            if (!invoker.executeCommand(line))
                printWriter.println("Неверная команда!");
        } while(!line.equals("exit"));

    }

    public void loadCsv(String fileName) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {

        Reader reader = new BufferedReader(new FileReader(fileName));
//        CsvToBeanBuilder<LabWork> builder = new CsvToBeanBuilder<LabWork>(reader)
//                .withMappingStrategy(new LabWorkMappingStrategy());
//        collection = new LinkedHashSet<>(builder.build().parse());
//
//
//        collectionHandler = new CollectionHandler(collection);

        List<LabWork> labWorks = parseCSV(fileName);
        writeRowsToCsv(fileName+"_new", labWorks);
        collection = new LinkedHashSet<>(labWorks);
        collectionHandler = new CollectionHandler(collection);
    }

    // Пользовательский метод. Запускает инициализации и цикл чтения команд
    public void run(String fileName) throws IOException, CsvException {
        loadCsv(fileName);
        initReceivers();
        initInvoker();
        runCommands();
    }
}


