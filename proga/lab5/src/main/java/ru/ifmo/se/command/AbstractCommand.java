package ru.ifmo.se.command;

import ru.ifmo.se.entity.LabWork;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.LinkedHashSet;

public abstract class AbstractCommand {
    protected final String name;
    protected final PrintWriter printer;
    protected final BufferedReader reader;

    AbstractCommand(String name, BufferedReader reader, PrintWriter printWriter){
        this.name = name;
        this.printer = printWriter;
        this.reader = reader;
    }
    public String getName(){
        return name;
    }

}
