package ru.ifmo.se.command;

import ru.ifmo.se.receiver.CollectionReceiver;

import java.io.BufferedReader;

public class AddCommand<T> extends AbstractCommand implements Command{
    private final CollectionReceiver<T> receiver;
    public AddCommand(CollectionReceiver<T> receiver, BufferedReader bufferedReader, String name){
        super(name);
        this.receiver = receiver;
    }

    public void execute(String[] args){
        // logic
    }
}