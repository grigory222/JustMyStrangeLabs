package ru.ifmo.se.command;

import ru.ifmo.se.receiver.IoReceiver;

import java.io.BufferedReader;
import java.io.PrintWriter;

public class HelpCommand extends AbstractCommand implements Command{
    private final IoReceiver receiver;
    public HelpCommand(IoReceiver receiver, BufferedReader reader, PrintWriter printer, PrintWriter infoPrinter, String name){
        super(name, reader, printer, infoPrinter);
        this.receiver = receiver;
    }

    public void execute(String[] args){
        receiver.help();
    }
}