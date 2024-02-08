package ru.ifmo.se.command;

import ru.ifmo.se.receiver.IoReceiver;

import java.io.BufferedReader;
import java.io.PrintWriter;

public class InfoCommand extends AbstractCommand implements Command{
    private final IoReceiver receiver;
    public InfoCommand(IoReceiver receiver, BufferedReader reader, PrintWriter printer, String name){
        super(name, reader, printer);
        this.receiver = receiver;
    }

    public void execute(String[] args){
        // logic
        receiver.info();
    }
}