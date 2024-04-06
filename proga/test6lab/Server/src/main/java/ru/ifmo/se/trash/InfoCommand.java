package ru.ifmo.se.command;

import ru.ifmo.se.receiver.Receiver;

import java.io.BufferedReader;
import java.io.PrintWriter;

public class InfoCommand extends AbstractCommand implements Command{
    private final IoReceiver receiver;
    public InfoCommand(Receiver receiver, BufferedReader reader, PrintWriter printer, PrintWriter infoPrinter, String name){
        super(name, reader, printer, infoPrinter);
        this.receiver = receiver;
    }

    public void execute(String[] args){
        // logic
        receiver.info();
    }
}