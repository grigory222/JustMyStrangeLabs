package ru.ifmo.se.command;

import ru.ifmo.se.entity.LabWork;
import ru.ifmo.se.entity.LabWorkReader;
import ru.ifmo.se.receiver.CollectionReceiver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class ClearCommand  extends AbstractCommand implements Command{
    private final CollectionReceiver receiver;
    public ClearCommand(CollectionReceiver receiver, BufferedReader reader, PrintWriter printer, String name){
        super(name, reader, printer);
        this.receiver = receiver;
    }

    public void execute(String[] args) {
        receiver.clear();
        printer.println("Коллекция очищена");
    }
}