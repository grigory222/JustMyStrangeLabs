package ru.ifmo.se.command;

import ru.ifmo.se.entity.Person;
import ru.ifmo.se.receiver.Receiver;

import java.io.BufferedReader;
import java.io.PrintWriter;

public class PrintFieldAscendingCommand extends AbstractCommand implements Command{
    private final IoReceiver receiver;
    public PrintFieldAscendingCommand(Receiver receiver, BufferedReader reader, PrintWriter printer, PrintWriter infoPrinter, String name){
        super(name, reader, printer, infoPrinter);
        this.receiver = receiver;
    }

    public void execute(String[] args){
        for (Person man: receiver.getAuthors()){
            printer.println(man);
        }

    }
}