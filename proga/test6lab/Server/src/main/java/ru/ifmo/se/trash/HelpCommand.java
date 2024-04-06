package ru.ifmo.se.command;

import ru.ifmo.se.dto.Reply;
import ru.ifmo.se.receiver.Receiver;

import java.io.BufferedReader;
import java.io.PrintWriter;

public class HelpCommand extends AbstractCommand implements Command{
    private final Receiver receiver;
    public HelpCommand(Receiver receiver, BufferedReader reader, PrintWriter printer, PrintWriter infoPrinter, String name){
        super(name, reader, printer, infoPrinter);
        this.receiver = receiver;
    }

    public Reply execute(String[] args){
        receiver.help();
    }
}