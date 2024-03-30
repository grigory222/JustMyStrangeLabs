package ru.ifmo.se.command;

import ru.ifmo.se.entity.Difficulty;
import ru.ifmo.se.receiver.Receiver;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.List;

public class PrintUniqueDifficultyCommand extends AbstractCommand implements Command{
    private final IoReceiver receiver;
    public PrintUniqueDifficultyCommand(Receiver receiver, BufferedReader reader, PrintWriter printer, PrintWriter infoPrinter, String name){
        super(name, reader, printer, infoPrinter);
        this.receiver = receiver;
    }

    public void execute(String[] args){
        List<Difficulty> list = receiver.printUniqueDifficulty();
        printer.println(list);
    }
}