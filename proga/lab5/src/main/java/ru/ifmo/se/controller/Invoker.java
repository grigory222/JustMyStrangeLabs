package ru.ifmo.se.controller;

import ru.ifmo.se.command.Command;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

public class Invoker {
    // мапа в которой хранится соответсвующий экзепляр класса Command для каждой команды
    private final Map<String, Command> commands;
    public Invoker(Map<String, Command> commands) {
        this.commands = commands;
    }

    // Парсит команду с аргументами и делегирует выполнение классу Command
    public boolean executeCommand(String commandAndArgs) {
        String[] parsed = commandAndArgs.split(" ");
        String[] args = Arrays.copyOfRange(parsed, 1, parsed.length);
        if (!commands.containsKey(parsed[0]))
            return false;
        Command command = commands.get(parsed[0]);
        command.execute(args);
        return true;
    }

}
