package ru.ifmo.se.runner;

import ru.ifmo.se.command.*;
import ru.ifmo.se.controller.Invoker;
import ru.ifmo.se.entity.LabWork;
import ru.ifmo.se.receiver.CollectionHandler;
import ru.ifmo.se.receiver.CollectionReceiver;
import ru.ifmo.se.receiver.IoReceiver;
import ru.ifmo.se.receiver.StorageReceiver;

import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static ru.ifmo.se.csv.CsvHandler.*;

public class Runner {

    private final PrintWriter infoPrinter; // для команд show, info внутри скриптов
    private final PrintWriter printWriter;
    private final BufferedReader bufferedReader;
    // создаем экземпляры Получателей, чтобы каждая команда знала своего исполнителя
    private IoReceiver ioReceiver;
    private CollectionReceiver collectionReceiver;
    private StorageReceiver storageReceiver;
    private CollectionHandler collectionHandler; // обработчик crud операций с коллекцией
    private Invoker invoker;
    private LinkedHashSet<LabWork> collection;
    public static final ArrayList<File> historyCall = new ArrayList<>();


    // Конструктор без параметров будет использовать PrintWriter с stdout и BufferedReader с stdin
    public Runner(){
        this(new PrintWriter(System.out, true), new BufferedReader(new InputStreamReader(System.in)));
    }

    // Конструктор с явным определением printWriter'a и bufferedReader'а
    public Runner(PrintWriter printWriter, BufferedReader bufferedReader){
        this.printWriter = printWriter;
        this.bufferedReader = bufferedReader;
        this.infoPrinter = printWriter;
    }
    public Runner(PrintWriter infoPrinter, File myFile/*, ArrayList<File> historyCall*/, PrintWriter printWriter, BufferedReader bufferedReader){
        this.infoPrinter = infoPrinter;
        this.printWriter = printWriter;
        this.bufferedReader = bufferedReader;
        historyCall.add(myFile);
    }

    // Инициализация отправителя - invoker
    private void initInvoker(String fileName){
        invoker = new Invoker(fillCommandMap(fileName));
    }

    private Map<String, Command> fillCommandMap(String fileName){
        Map<String, Command> cmdMap = new HashMap<>();

        // создаем экземпляры команд, чтобы положить их в мапу, чтобы инвокер из неё вызывал их
        Command exitCmd = new ExitCommand(bufferedReader, printWriter, infoPrinter, "save");
        Command executeScriptCmd = new ExecuteScriptCommand(collectionReceiver, bufferedReader, printWriter, infoPrinter, "execute_script");
        Command historyCmd = new HistoryCommand("history", bufferedReader, printWriter, infoPrinter);
        // IoReceiver
        Command helpCmd = new HelpCommand(ioReceiver, bufferedReader, printWriter, infoPrinter, "help");
        Command infoCmd = new InfoCommand(ioReceiver, bufferedReader, printWriter, infoPrinter, "info");
        Command showCmd = new ShowCommand(ioReceiver, bufferedReader, printWriter, infoPrinter, "show");
        Command printUniqueDifficultyCmd = new PrintUniqueDifficultyCommand(ioReceiver, bufferedReader, printWriter, infoPrinter, "print_unique_difficulty");
        Command printFieldAscendingCmd = new PrintFieldAscendingCommand(ioReceiver, bufferedReader, printWriter, infoPrinter, "print_field_ascending_author");
        // CollectionReceiver
        Command addCmd = new AddCommand(collectionReceiver, bufferedReader, printWriter, infoPrinter, "add");
        Command updateCmd = new UpdateCommand(collectionReceiver, bufferedReader, printWriter, infoPrinter, "update");
        Command removeByIdCmd = new RemoveByIdCommand(collectionReceiver, bufferedReader, printWriter, infoPrinter, "remove_by_id");
        Command clearCmd = new ClearCommand(collectionReceiver, bufferedReader, printWriter, infoPrinter, "clear");
        Command groupCountingByCreationDateCmd = new GroupCountingByCreationDateCommand(collectionReceiver, bufferedReader, printWriter, infoPrinter, "group_counting_by_creation_date");
        // ...
        // StorageReceiver
        Command saveCmd = new SaveCommand(storageReceiver, bufferedReader, printWriter, infoPrinter, "save", fileName);
        // ...
        // ...
        // ...
        // ...
        // ...


        // кладём команды в мапу
        cmdMap.put("execute_script", executeScriptCmd);
        cmdMap.put("exit", exitCmd);
        cmdMap.put("help", helpCmd);
        cmdMap.put("info", infoCmd);
        cmdMap.put("show", showCmd);
        cmdMap.put("print_unique_difficulty", printUniqueDifficultyCmd);
        cmdMap.put("print_field_ascending_author", printFieldAscendingCmd);
        cmdMap.put("add", addCmd);
        cmdMap.put("update", updateCmd);
        cmdMap.put("remove_by_id", removeByIdCmd);
        cmdMap.put("clear", clearCmd);
        cmdMap.put("group_counting_by_creation_date", groupCountingByCreationDateCmd);
        cmdMap.put("history", historyCmd);
        cmdMap.put("save", saveCmd);


        return cmdMap;
    }

    void initReceivers(String fileName){
        // создаем экземпляры Получателей, чтобы каждая команда знала своего исполнителя
        ioReceiver = new IoReceiver(collection, collectionHandler, infoPrinter, bufferedReader);
        collectionReceiver = new CollectionReceiver(collection, collectionHandler, historyCall, fileName);
        storageReceiver = new StorageReceiver(collection, collectionHandler);
    }

    void runCommands(){
        String line;
        do{
            try {
                printWriter.print("> "); printWriter.flush();
                line = bufferedReader.readLine();
            } catch (IOException e) {
                printWriter.println("Ошибка ввода!");
                return;
            }
            if (line == null){
                printWriter.println("Конец ввода!");
                break;
            }
            if (!invoker.executeCommand(line))
                printWriter.println("Неверная команда!");
        } while(!line.equals("exit"));
    }

    private boolean checkIds(List<LabWork> labs){
        Set<Integer> ids = new HashSet<>();
        labs.forEach(x -> ids.add(x.getId()));
        if (ids.size() == labs.size()){
            return true;
        }
        // update ids
        for (int i = 0; i < labs.size(); i++){
            labs.get(i).setId(i+1);
        }
        return false;
    }

    public void loadFromCsv(String fileName) {
        List<LabWork> labWorks = new ArrayList<>(); // создадим пустой список на случай ошибки
        try {
            // проверка можем ли открыть файл
            Reader reader = new BufferedReader(new FileReader(fileName));
            reader.close();
            // парсинг
            labWorks = parseCSV(fileName, infoPrinter);
        } catch (FileNotFoundException e) {
            infoPrinter.println("Невозможно открыть файл " + fileName);
        } catch (IOException e) {
            infoPrinter.println("Не удаётся прочитать файл " + fileName);
        } catch (RuntimeException e){
            infoPrinter.println(e.getMessage());
        }

        if (!checkIds(labWorks))
            infoPrinter.println("Обнаружены повторяющиеся ID в CSV файле. Идентификаторы обновлены.");

        collection = new LinkedHashSet<>(labWorks);
        collectionHandler = new CollectionHandler(collection, LocalDate.now());
    }

    private void initAll(String fileName){
        initReceivers(fileName);
        initInvoker(fileName);
        printWriter.println("Добро пожаловать! Чтобы просмотреть возможные команды используйте help");
    }

    // Пользовательский метод. Запускает инициализации и цикл чтения команд
    public void run(String fileName)  {
        loadFromCsv(fileName);
        initAll(fileName);
        runCommands();
    }

    public void run(LinkedHashSet<LabWork> collection, String fileName){
        this.collection = collection;
        collectionHandler = new CollectionHandler(collection, LocalDate.now());
        initAll(fileName);
        runCommands();
    }

}


