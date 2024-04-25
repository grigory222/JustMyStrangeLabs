package ru.ifmo.se.command;

import lombok.Getter;
import ru.ifmo.se.receiver.Receiver;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public abstract class AbstractCommand {
    @Getter
    protected final String name;
    protected final PrintWriter printer;
    protected final PrintWriter infoPrinter;
    protected final BufferedReader reader;
    protected final Receiver receiver;
    protected final Socket socket;

    AbstractCommand(Receiver receiver, String name, BufferedReader reader, PrintWriter printWriter, PrintWriter infoPrinter, Socket socket){
        this.receiver = receiver;
        this.name = name;
        this.printer = printWriter;
        this.reader = reader;
        this.infoPrinter = infoPrinter;
        this.socket = socket;
    }

}
