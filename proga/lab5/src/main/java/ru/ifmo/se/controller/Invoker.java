package ru.ifmo.se.controller;

import ru.ifmo.se.command.Command;
import java.util.Map;

public class Invoker {
    // мапа в которой хранится соответсвующий экзепляр класса Command для каждой команды
    private final Map<String, Command> commands;

    public Invoker(Map<String, Command> commands) {
        this.commands = commands;
    }

    // Парсит команду с аргументами и делегирует выполнение классу Command
    public void executeCommand(String commandAndArgs){
        // parse args; create String parsed and String[] args
        // Command command = commands.get(parsed[0]]
        // command.execute(args);
    }

}
