package ru.ifmo.se.command;

import lombok.Getter;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.Arrays;

// конкретная команда. Для записи в историю команд
@Getter
public class ConcreteCommand extends AbstractCommand{
    private final String[] args;
    ConcreteCommand(String name, BufferedReader reader, PrintWriter printWriter, PrintWriter infoPrinter, String[] args) {
        super(name, reader, printWriter, infoPrinter);
        this.args = args;
    }
    ConcreteCommand(AbstractCommand cmd, String[] args){
        super(cmd.name, cmd.reader, cmd.printer, cmd.infoPrinter);
        this.args = args;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(name);
        for (String s: args){
            result.append(result).append(" ").append(s);
        };
        return result.toString();
    }
}
