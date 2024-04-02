package ru.ifmo.se.command;

import lombok.Getter;
import ru.ifmo.se.receiver.Receiver;

import java.io.BufferedReader;
import java.io.PrintWriter;

public abstract class AbstractCommand {
    @Getter
    protected final String name;
    protected final PrintWriter printer;
    protected final PrintWriter infoPrinter;
    protected final BufferedReader reader;
    protected final Receiver receiver;

    AbstractCommand(Receiver receiver, String name, BufferedReader reader, PrintWriter printWriter, PrintWriter infoPrinter){
        this.receiver = receiver;
        this.name = name;
        this.printer = printWriter;
        this.reader = reader;
        this.infoPrinter = infoPrinter;
    }

}
