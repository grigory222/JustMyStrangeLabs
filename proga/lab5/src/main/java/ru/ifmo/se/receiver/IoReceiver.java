package ru.ifmo.se.receiver;

import java.io.PrintWriter;

// работает с командами ввода/вывода: help, info, show, print_unique_difficulty, print_field_ascending_author
public class IoReceiver<T> extends Receiver<T>{
    private final PrintWriter printWriter;

    public IoReceiver(PrintWriter printWriter){
        this.printWriter = printWriter;
    }

    void help(){
        printWriter.println("HELP MEE!!!!");
    }
}
