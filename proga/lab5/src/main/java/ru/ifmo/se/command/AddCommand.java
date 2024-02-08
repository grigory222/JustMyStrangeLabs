package ru.ifmo.se.command;

import ru.ifmo.se.receiver.CollectionReceiver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class AddCommand extends AbstractCommand implements Command{
    private final CollectionReceiver receiver;
    public AddCommand(CollectionReceiver receiver, BufferedReader reader, PrintWriter printer, String name){
        super(name, reader, printer);
        this.receiver = receiver;
    }

    public void execute(String[] args) throws IOException {
        // считать данные нового элемента
        printer.println();
        reader.readLine();
        receiver.add();
    }
}