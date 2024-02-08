package ru.ifmo.se.receiver;

import java.io.BufferedReader;
import java.io.PrintWriter;

// работает с командами ввода/вывода: help, info, show, print_unique_difficulty, print_field_ascending_author
public class IoReceiver extends Receiver{
    private final PrintWriter printer;
    private final BufferedReader reader;

    public IoReceiver(CollectionHandler collectionHandler, PrintWriter printWriter, BufferedReader bufferedReader){
        super(collectionHandler);
        this.printer = printWriter;
        this.reader = bufferedReader;
    }

    public void help(){
        printer.println("HELP MEE!!!!");
    }
    public void show(){
        printer.println("SHOW!!!!");
    }
    public void info(){
        printer.println("INFO!!!!");
    }
}
