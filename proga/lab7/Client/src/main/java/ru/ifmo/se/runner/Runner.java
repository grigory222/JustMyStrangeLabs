package ru.ifmo.se.runner;

import ru.ifmo.se.auth.Authentificator;
import ru.ifmo.se.command.*;
import ru.ifmo.se.controller.Invoker;
import ru.ifmo.se.receiver.Receiver;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;

import static ru.ifmo.se.network.Network.connect;


public class Runner {

    private final PrintWriter infoPrinter; // для команд show, info внутри скриптов
    private final PrintWriter printWriter;
    private final BufferedReader bufferedReader;
    // создаем экземпляры Получателей, чтобы каждая команда знала своего исполнителя
    private Invoker invoker;
    public static final ArrayList<File> historyCall = new ArrayList<>();
    //private final HistoryCommand historyCmd;
    private final Receiver receiver;
    private final Socket socket;
    private ExecuteScriptCommand executeScriptCmd;
    private String token;

    // Конструктор без параметров будет использовать PrintWriter с stdout и BufferedReader с stdin
    public Runner() throws InterruptedException {
        this(new PrintWriter(System.out, true), new BufferedReader(new InputStreamReader(System.in)));
    }

    public static Socket connectToServer() {
        Scanner scanner = new Scanner(System.in);
        Socket socket = null;

        while (true) {
            try {
                System.out.print("Введите адрес сервера: ");
                String host = scanner.nextLine();
                System.out.print("Введите порт сервера: ");
                int port = Integer.parseInt(scanner.nextLine());
                socket = new Socket();
                socket.connect(new InetSocketAddress(host, port), 1000);
                break; // Если удалось подключиться к серверу, выходим из цикла
            } catch (IOException | NumberFormatException e) {
                System.out.println("Ошибка подключения к серверу. Пожалуйста, проверьте введенные данные.");
            }
        }

        return socket;
    }


    // Конструктор с явным определением printWriter'a и bufferedReader'а
    public Runner(PrintWriter printWriter, BufferedReader bufferedReader)  {
        //this.socket = connect(InetAddress.getByName("localhost"), 5252); //connectToServer();
        this.socket = connectToServer();
        this.receiver = new Receiver(this.socket);
        this.printWriter = printWriter;
        this.bufferedReader = bufferedReader;
        this.infoPrinter = printWriter;
        //historyCmd = new HistoryCommand("history", bufferedReader, printWriter, infoPrinter);
    }

    private Map<String, Command> fillCommandMap(){
        Map<String, Command> cmdMap = new HashMap<>();

        // создаем экземпляры команд, чтобы положить их в мапу, чтобы инвокер из неё вызывал их
        Command exitCmd = new ExitCommand(socket, receiver, bufferedReader, printWriter, infoPrinter, "exit");
        executeScriptCmd = new ExecuteScriptCommand(socket, receiver, bufferedReader, printWriter, infoPrinter, "execute_script");

        Command helpCmd = new HelpCommand(socket, receiver, bufferedReader, printWriter, infoPrinter, "help");
        Command infoCmd = new InfoCommand(socket, receiver, bufferedReader, printWriter, infoPrinter, "info");
        Command showCmd = new ShowCommand(socket, receiver, bufferedReader, printWriter, infoPrinter, "show");
        Command printUniqueDifficultyCmd = new PrintUniqueDifficultyCommand(socket, receiver, bufferedReader, printWriter, infoPrinter, "print_unique_difficulty");
        Command printFieldAscendingCmd = new PrintFieldAscendingCommand(socket, receiver, bufferedReader, printWriter, infoPrinter, "print_field_ascending_author");

        Command addCmd = new AddCommand(socket, receiver, bufferedReader, printWriter, infoPrinter, "add");
        Command addIfMaxCmd = new AddIfMaxCommand(socket, receiver, bufferedReader, printWriter, infoPrinter, "add_if_max");
        Command addIfMinCmd = new AddIfMinCommand(socket, receiver, bufferedReader, printWriter, infoPrinter, "add_if_min");
        Command updateCmd = new UpdateCommand(socket, receiver, bufferedReader, printWriter, infoPrinter, "update");
        Command removeByIdCmd = new RemoveByIdCommand(socket, receiver, bufferedReader, printWriter, infoPrinter, "remove_by_id");
        Command clearCmd = new ClearCommand(socket, receiver, bufferedReader, printWriter, infoPrinter, "clear");
        Command groupCountingByCreationDateCmd = new GroupCountingByCreationDateCommand(socket, receiver, bufferedReader, printWriter, infoPrinter, "group_counting_by_creation_date");


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
//        cmdMap.put("history", historyCmd);

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

    // Пользовательский метод. Запускает инициализации и цикл чтения команд
    public void run() {
        token = new Authentificator(printWriter, bufferedReader, socket).auth();
        invoker = new Invoker(fillCommandMap(), token);
        executeScriptCmd.setInvoker(invoker);
        printWriter.println("Добро пожаловать! Чтобы просмотреть возможные команды используйте help");
        runCommands();
    }
}


