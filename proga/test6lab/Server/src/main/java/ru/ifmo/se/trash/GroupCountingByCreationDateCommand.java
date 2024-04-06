package ru.ifmo.se.command;

import ru.ifmo.se.entity.LabWork;
import ru.ifmo.se.receiver.Receiver;

import java.io.BufferedReader;
import java.io.PrintWriter;

public class GroupCountingByCreationDateCommand extends AbstractCommand implements Command{
    public GroupCountingByCreationDateCommand(Receiver receiver, BufferedReader reader, PrintWriter printer, PrintWriter infoPrinter, String name){
        super(name, reader, printer, infoPrinter);
        this.receiver = receiver;
    }

    public void execute(String[] args) {
        for (LabWork lab : receiver.getGroupCountingByCreationDate()){
            printer.println(lab);
            printer.println("===========");
        }
    }
}