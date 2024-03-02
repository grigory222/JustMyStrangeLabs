package ru.ifmo.se.runner;

import ru.ifmo.se.command.*;
import ru.ifmo.se.controller.Invoker;
import ru.ifmo.se.entity.LabWork;
import ru.ifmo.se.entity.LabWorkReader;
import ru.ifmo.se.receiver.CollectionHandler;
import ru.ifmo.se.receiver.CollectionReceiver;
import ru.ifmo.se.receiver.IoReceiver;
import ru.ifmo.se.receiver.StorageReceiver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.List;
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
    private LinkedHashSet<LabWork> collection = new LinkedHashSet<>();
    public static final ArrayList<File> historyCall = new ArrayList<>();
    private char lastChar; // последний символ обработанный


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
        Command addIfMaxCmd = new AddIfMaxCommand(collectionReceiver, bufferedReader, printWriter, infoPrinter, "add_if_max");
        Command addIfMinCmd = new AddIfMinCommand(collectionReceiver, bufferedReader, printWriter, infoPrinter, "add_if_min");
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
        cmdMap.put("add_if_max", addIfMaxCmd);
        cmdMap.put("add_if_min", addIfMinCmd);
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

    private String input = "";
    static volatile boolean interceptingWithKeyListener = true;

    private String keyListener(){
        JPanel panel = new JPanel();
        JFrame frame = new JFrame("Console Swing Example");
        panel.setFocusable(true); // Устанавливаем фокус на панели, чтобы она могла перехватывать события клавиатуры
        panel.requestFocusInWindow();

        input = "";
        interceptingWithKeyListener = true;
        panel.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                char keyChar = e.getKeyChar();
                if (keyChar == KeyEvent.VK_BACK_SPACE) {
                    if (!input.isEmpty()) {
                        input = input.substring(0, input.length() - 1);
                        System.out.print("\b \b"); // Стереть символ в консоли
                    }
                } else {
                    input += keyChar;
                    System.out.print(keyChar);
                }
                //printWriter.print(keyChar);
                printWriter.flush();
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // Обработка события нажатия клавиши (когда клавиша только что нажата)
                if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_R) {
                    System.out.print("Нажата комбинация Ctrl+R");
                } else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_C) {
                    System.exit(0);
                } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                    System.out.println(input);
                    System.out.println("Нажата стрелка вверх");
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    System.out.println("Нажата стрелка вниз");
                } else if (e.getKeyCode() == KeyEvent.VK_ENTER){
                    // удалить фрейм, чтобы не блокировал ввод в reader.readLine()
                    printWriter.println();
                    interceptingWithKeyListener = false;
                    frame.dispose();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);

        while (interceptingWithKeyListener) Thread.onSpinWait();

        return input;
    }
    private void runCommands(){
        String line;
        do{
            //try {
            printWriter.print("> "); printWriter.flush();
            line = keyListener();
                /*line = lastChar + bufferedReader.readLine();
            } catch (IOException e) {
                printWriter.println("Ошибка ввода!");
                return;
            }*/
            if (line == null){
                printWriter.println("Конец ввода!");
                break;
            }
            if (!invoker.executeCommand(line)) {
                printWriter.println("Неверная команда!");
                printWriter.flush();
            }
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

    public void loadFromCsv(String fileName) throws InvalidCSVException {
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

        if (validateList(labWorks)){
            collection = new LinkedHashSet<>(labWorks);
        } else{
            System.err.println("Невалидная коллекция");
        }
        collectionHandler = new CollectionHandler(collection, LocalDate.now());
        collectionHandler.sort();
    }

    private boolean validateLab(LabWork labWork){
        boolean res = labWork.getId() > 0;
        res = res && LabWorkReader.validateName(labWork.getName());
        res = res && labWork.getMinimalPoint() > 0;
        res = res && labWork.getCoordinates().getY() - 48.0 <= Double.MIN_VALUE;

        return res;
    }

    private boolean validateList(List<LabWork> labWorks) {
        var res = labWorks.stream().filter(this::validateLab).toList();
        return res.size() >= labWorks.size();
    }

    private void initAll(String fileName){
        initReceivers(fileName);
        initInvoker(fileName);
        printWriter.println("Добро пожаловать! Чтобы просмотреть возможные команды используйте help");
    }

    // Пользовательский метод. Запускает инициализации и цикл чтения команд
    public void run(String fileName) throws InvalidCSVException {
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


