package ru.ifmo.se.runner;

import ru.ifmo.se.command.*;
import ru.ifmo.se.controller.Invoker;
import ru.ifmo.se.dto.Request;
import ru.ifmo.se.entity.LabWork;
import ru.ifmo.se.readers.LabWorkReader;
import ru.ifmo.se.receiver.Receiver;

import java.io.*;
import java.util.List;
import java.util.*;

import static ru.ifmo.se.csv.CsvHandler.*;

public class Runner {

    private final PrintWriter infoPrinter; // для команд show, info внутри скриптов
    private final PrintWriter printWriter;
    private final BufferedReader bufferedReader;
    // создаем экземпляры Получателей, чтобы каждая команда знала своего исполнителя
    private Invoker invoker;
    public static final ArrayList<File> historyCall = new ArrayList<>();
    private final HistoryCommand historyCmd;
    private final Receiver receiver = new Receiver();


    // Конструктор без параметров будет использовать PrintWriter с stdout и BufferedReader с stdin
    public Runner(){
        this(new PrintWriter(System.out, true), new BufferedReader(new InputStreamReader(System.in)));
    }

    // Конструктор с явным определением printWriter'a и bufferedReader'а
    public Runner(PrintWriter printWriter, BufferedReader bufferedReader){
        this.printWriter = printWriter;
        this.bufferedReader = bufferedReader;
        this.infoPrinter = printWriter;
        historyCmd = new HistoryCommand("history", bufferedReader, printWriter, infoPrinter);
    }
    public Runner(PrintWriter infoPrinter, File myFile/*, ArrayList<File> historyCall*/, PrintWriter printWriter, BufferedReader bufferedReader){
        this.infoPrinter = infoPrinter;
        this.printWriter = printWriter;
        this.bufferedReader = bufferedReader;
        historyCmd = new HistoryCommand("history", bufferedReader, printWriter, infoPrinter);
        historyCall.add(myFile);
    }

    // Инициализация отправителя - invoker
    private void initInvoker(){
        invoker = new Invoker(fillCommandMap());
    }

    private Map<String, Command> fillCommandMap(){
        Map<String, Command> cmdMap = new HashMap<>();

        // создаем экземпляры команд, чтобы положить их в мапу, чтобы инвокер из неё вызывал их
        Command exitCmd = new ExitCommand(bufferedReader, printWriter, infoPrinter, "save");
        Command executeScriptCmd = new ExecuteScriptCommand(receiver, bufferedReader, printWriter, infoPrinter, "execute_script");

        Command helpCmd = new HelpCommand(receiver, bufferedReader, printWriter, infoPrinter, "help");
        Command infoCmd = new InfoCommand(receiver, bufferedReader, printWriter, infoPrinter, "info");
        Command showCmd = new ShowCommand(receiver, bufferedReader, printWriter, infoPrinter, "show");
        Command printUniqueDifficultyCmd = new PrintUniqueDifficultyCommand(receiver, bufferedReader, printWriter, infoPrinter, "print_unique_difficulty");
        Command printFieldAscendingCmd = new PrintFieldAscendingCommand(receiver, bufferedReader, printWriter, infoPrinter, "print_field_ascending_author");

        Command addCmd = new AddCommand(receiver, bufferedReader, printWriter, infoPrinter, "add");
        Command addIfMaxCmd = new AddIfMaxCommand(receiver, bufferedReader, printWriter, infoPrinter, "add_if_max");
        Command addIfMinCmd = new AddIfMinCommand(receiver, bufferedReader, printWriter, infoPrinter, "add_if_min");
        Command updateCmd = new UpdateCommand(receiver, bufferedReader, printWriter, infoPrinter, "update");
        Command removeByIdCmd = new RemoveByIdCommand(receiver, bufferedReader, printWriter, infoPrinter, "remove_by_id");
        Command clearCmd = new ClearCommand(receiver, bufferedReader, printWriter, infoPrinter, "clear");
        Command groupCountingByCreationDateCmd = new GroupCountingByCreationDateCommand(receiver, bufferedReader, printWriter, infoPrinter, "group_counting_by_creation_date");


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

        return cmdMap;
    }


    private void runCommands(){
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

    // Пользовательский метод. Запускает инициализации и цикл чтения команд
    public void run() {
        initInvoker();
        printWriter.println("Добро пожаловать! Чтобы просмотреть возможные команды используйте help");
        runCommands();
    }

}

