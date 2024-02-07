package ru.ifmo.se.command;

import ru.ifmo.se.receiver.IoReceiver;

public class PrintFieldAscendingCommand<T> extends AbstractCommand implements Command{
    private final IoReceiver<T> receiver;
    public PrintFieldAscendingCommand(IoReceiver<T> receiver, String name){
        super(name);
        this.receiver = receiver;
    }

    public void execute(String[] args){
        // logic
    }
}