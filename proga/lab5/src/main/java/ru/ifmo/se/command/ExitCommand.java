package ru.ifmo.se.command;

import ru.ifmo.se.receiver.IoReceiver;

import java.io.BufferedReader;
import java.io.PrintWriter;

public class ExitCommand extends AbstractCommand implements Command{
    public ExitCommand(BufferedReader reader, PrintWriter printer, String name){
        super(name, reader, printer);
    }
    public void execute(String[] args){
        return;
    }
}