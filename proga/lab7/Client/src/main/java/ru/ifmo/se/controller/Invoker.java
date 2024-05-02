package ru.ifmo.se.controller;

import ru.ifmo.se.command.Command;
//import ru.ifmo.se.command.HistoryCommand;

import java.util.Arrays;
import java.util.Map;

public class Invoker {
    // мапа в которой хранится соответсвующий экзепляр класса Command для каждой команды
    private final Map<String, Command> commands;
    private final String token;
    //private final HistoryCommand historyCmd;
    public Invoker(Map<String, Command> commands, String token) {
        this.commands = commands;
        //this.historyCmd = (HistoryCommand) commands.get("history");
        this.token = token;
    }

    // Парсит команду с аргументами и делегирует выполнение классу Command
    public boolean executeCommand(String commandAndArgs) {
        commandAndArgs = commandAndArgs.trim();
        String[] parsed = commandAndArgs.split(" ");
        String[] args = Arrays.copyOfRange(parsed, 1, parsed.length);
        // проверка существования такой команды
        if (!commands.containsKey(parsed[0]))
            return false;

        Command command =  commands.get(parsed[0]);
        //historyCmd.add((AbstractCommand)command, args);
        command.execute(args, token);
        return true;
    }
}
