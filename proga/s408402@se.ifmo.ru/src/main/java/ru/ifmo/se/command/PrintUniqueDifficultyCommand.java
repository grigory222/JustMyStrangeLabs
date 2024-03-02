package ru.ifmo.se.command;

import ru.ifmo.se.entity.Difficulty;
import ru.ifmo.se.entity.LabWork;
import ru.ifmo.se.receiver.IoReceiver;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.List;

public class PrintUniqueDifficultyCommand extends AbstractCommand implements Command{
    private final IoReceiver receiver;
    public PrintUniqueDifficultyCommand(IoReceiver receiver, BufferedReader reader, PrintWriter printer, PrintWriter infoPrinter, String name){
        super(name, reader, printer, infoPrinter);
        this.receiver = receiver;
    }

    public void execute(String[] args){
        List<Difficulty> list = receiver.printUniqueDifficulty();
        printer.println(list);
    }
}