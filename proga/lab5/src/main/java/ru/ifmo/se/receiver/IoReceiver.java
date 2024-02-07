package ru.ifmo.se.receiver;

import java.io.BufferedReader;
import java.io.PrintWriter;

// работает с командами ввода/вывода: help, info, show, print_unique_difficulty, print_field_ascending_author
public class IoReceiver<T> extends Receiver<T>{
    private final PrintWriter printWriter;
    private final BufferedReader bufferedReader;

    public IoReceiver(PrintWriter printWriter, BufferedReader bufferedReader){
        this.printWriter = printWriter;
        this.bufferedReader = bufferedReader;
    }

    public void help(){
        printWriter.println("HELP MEE!!!!");
    }
    public void show(){
        printWriter.println("SHOW!!!!");
    }
    public void info(){
        printWriter.println("INFO!!!!");
    }
}
