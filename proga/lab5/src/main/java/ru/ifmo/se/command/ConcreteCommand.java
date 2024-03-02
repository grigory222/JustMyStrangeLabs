package ru.ifmo.se.command;

import lombok.Getter;

import java.io.BufferedReader;
import java.io.PrintWriter;

// конкретная команда. Для записи в историю команд
public class ConcreteCommand extends AbstractCommand{
    @Getter
    private final String[] args;
    ConcreteCommand(String name, BufferedReader reader, PrintWriter printWriter, PrintWriter infoPrinter, String[] args) {
        super(name, reader, printWriter, infoPrinter);
        this.args = args;
    }
    ConcreteCommand(AbstractCommand cmd, String[] args){
        super(cmd.name, cmd.reader, cmd.printer, cmd.infoPrinter);
        this.args = args;
    }
}
