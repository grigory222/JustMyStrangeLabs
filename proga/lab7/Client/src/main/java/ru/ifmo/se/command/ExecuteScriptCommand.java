package ru.ifmo.se.command;

import lombok.Setter;
import ru.ifmo.se.controller.Invoker;
import ru.ifmo.se.receiver.Receiver;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Setter
public class ExecuteScriptCommand extends AbstractCommand implements Command{
    private Invoker invoker;
    public ExecuteScriptCommand(Socket socket, Receiver receiver, BufferedReader reader, PrintWriter printer, PrintWriter infoPrinter, String name){
        super(receiver, name, reader, printer, infoPrinter, socket);
    }

    public void execute(String[] args, String token) {
        if (args.length < 1){
            printer.println("Формат команды: 'execute_script file_name'");
            return;
        }

        String fileName = args[0];
        File file = new File(fileName);
        if (!file.canRead()){
            printer.println("Файл не найден или его невозможно прочитать");
            return;
        }
        List<String> strings;
        try {
            strings = expandExecuteScript(fileName);
            System.out.println(strings);
        } catch (IOException e){
            System.out.println("Ошибка чтения скрипта");
            return;
        }

        for (var line : strings){
            if (!invoker.executeCommand(line)) {
                infoPrinter.println("Неверная команда!");
            }
        }

    }


    public List<String> expandExecuteScript(String filename) throws IOException {
        List<String> expandedCommands = new ArrayList<>();
        Set<String> visitedFiles = new HashSet<>();
        expandExecuteScript(filename, expandedCommands, visitedFiles);
        return expandedCommands;
    }

    private void expandExecuteScript(String filename, List<String> expandedCommands, Set<String> visitedFiles) throws IOException {
        if (visitedFiles.contains(filename)) {
            infoPrinter.println("Рекурсия обнаружена: " + filename);
            return;
        }
        visitedFiles.add(filename);

        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.trim().startsWith("execute_script")) {
                String scriptFilename = line.trim().split(" ")[1];
                expandExecuteScript(scriptFilename, expandedCommands, visitedFiles);
            } else {
                expandedCommands.add(line.trim());
            }
        }
        reader.close();
    }

//    public static List<String> expandExecuteScript(String filename) throws IOException {
//        List<String> expandedCommands = new ArrayList<>();
//        expandExecuteScript(filename, expandedCommands);
//        return expandedCommands;
//    }
//
//    private static void expandExecuteScript(String filename, List<String> expandedCommands) throws IOException {
//        BufferedReader reader = new BufferedReader(new FileReader(filename));
//        String line;
//        while ((line = reader.readLine()) != null) {
//            if (line.trim().startsWith("execute_script")) {
//                String scriptFilename = line.trim().split(" ")[1];
//                expandExecuteScript(scriptFilename, expandedCommands);
//            } else {
//                expandedCommands.add(line.trim());
//            }
//        }
//        reader.close();
//    }
}