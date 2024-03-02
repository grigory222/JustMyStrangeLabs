package ru.ifmo.se.controller;

import ru.ifmo.se.command.AbstractCommand;
import ru.ifmo.se.command.Command;
import ru.ifmo.se.command.HistoryCommand;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Stack;

public class Invoker {
    // мапа в которой хранится соответсвующий экзепляр класса Command для каждой команды
    private final Map<String, Command> commands;
    private final HistoryCommand historyCmd;
    public Invoker(Map<String, Command> commands) {
        this.commands = commands;
        this.historyCmd = (HistoryCommand) commands.get("history");
    }

    // Парсит команду с аргументами и делегирует выполнение классу Command
    public boolean executeCommand(String commandAndArgs) {
        String[] parsed = commandAndArgs.split(" ");
        String[] args = Arrays.copyOfRange(parsed, 1, parsed.length);
        // проверка существования такой команды
        if (!commands.containsKey(parsed[0]))
            return false;

        Command command =  commands.get(parsed[0]);
        historyCmd.add((AbstractCommand)command);
        command.execute(args);
        return true;
    }

    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch( keyCode ) {
            case KeyEvent.VK_UP:
                // handle up
                break;
            case KeyEvent.VK_DOWN:
                // handle down
                break;
            case KeyEvent.VK_LEFT:
                // handle left
                break;
            case KeyEvent.VK_RIGHT :
                // handle right
                break;
        }
    }
}
