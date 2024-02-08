package ru.ifmo.se.runner;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;
import ru.ifmo.se.command.*;
import ru.ifmo.se.controller.Invoker;
import ru.ifmo.se.entity.LabWork;
import ru.ifmo.se.entity.LabWorkMappingStrategy;
import ru.ifmo.se.receiver.*;

import java.io.*;
import java.util.*;

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
        ioReceiver = new IoReceiver(collectionHandler, printWriter, bufferedReader);
        collectionReceiver = new CollectionReceiver(collectionHandler);
        storageReceiver = new StorageReceiver(collectionHandler, file);
    }

    void runCommands() throws IOException{
        String line;
        do{
            line = bufferedReader.readLine();
            if (!invoker.executeCommand(line))
                printWriter.println("Неверная команда!");
        } while(!line.equals("exit"));

    }

    public boolean loadCsv(String fileName) throws IOException, CsvException {
//        CSVReader reader = new CSVReaderBuilder(new FileReader(fileName)).build();
//        String [] line;
//
//        // ключ - заголовок. значение - соответствующий столбец
//        Map<String, ArrayList<String>> map = new HashMap<>();
//        String[] headers = reader.readNext();
//        for (String cur : headers)
//            map.put(cur, new ArrayList<String>());
//
//        //
//        for (String[] row: reader.readAll()){
//            for (int i = 0; i < row.length; i++){
//                map.get(headers[i]).add(row[i]);
//            }
//        }
//        System.out.println(map);


        Reader reader = new BufferedReader(new FileReader(fileName));

        CsvToBeanBuilder<LabWork> builder = new CsvToBeanBuilder<LabWork>(reader)
                .withMappingStrategy(new LabWorkMappingStrategy());
        CsvToBean<LabWork> ctb = builder.build();
        for (LabWork lab: ctb.parse()) {
            System.out.println(lab);
        }

//        CsvToBean<LabWork> csvReader = new CsvToBeanBuilder<LabWork>(reader)
//                .withType(LabWork.class)
//                .withSeparator(',')
//                .withIgnoreLeadingWhiteSpace(true)
//                .withIgnoreEmptyLine(true)
//                .build();
//
//        LinkedHashSet<LabWork> results = new LinkedHashSet<>(csvReader.parse());

        collection = new LinkedHashSet<>();

        collectionHandler = new CollectionHandler(collection);
        return true; // успешно считали
    }

    // Пользовательский метод. Запускает инициализации и цикл чтения команд
    public void run(String fileName) throws IOException, CsvException {
        loadCsv(fileName);
        initReceivers();
        initInvoker();
        runCommands();
    }
}


