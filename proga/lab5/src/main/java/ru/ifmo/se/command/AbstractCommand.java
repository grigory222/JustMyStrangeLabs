package ru.ifmo.se.command;

import lombok.Getter;
import ru.ifmo.se.entity.LabWork;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.LinkedHashSet;

public abstract class AbstractCommand {
    @Getter
    protected final String name;
    protected final PrintWriter printer;
    protected final PrintWriter infoPrinter;
    protected final BufferedReader reader;

    AbstractCommand(String name, BufferedReader reader, PrintWriter printWriter, PrintWriter infoPrinter){
        this.name = name;
        this.printer = printWriter;
        this.reader = reader;
        this.infoPrinter = infoPrinter;
    }

}
