package ru.ifmo.se.command;

import ru.ifmo.se.receiver.IoReceiver;

public class PrintUniqueDifficultyCommand<T> extends AbstractCommand implements Command{
    private final IoReceiver<T> receiver;
    public PrintUniqueDifficultyCommand(IoReceiver<T> receiver, String name){
        super(name);
        this.receiver = receiver;
    }

    public void execute(String[] args){
        // logic
    }
}