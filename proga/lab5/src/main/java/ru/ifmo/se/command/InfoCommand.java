package ru.ifmo.se.command;

import ru.ifmo.se.receiver.IoReceiver;

public class InfoCommand<T> extends AbstractCommand implements Command{
    private final IoReceiver<T> receiver;
    public InfoCommand(IoReceiver<T> receiver, String name){
        super(name);
        this.receiver = receiver;
    }

    public void execute(String[] args){
        // logic
        receiver.info();
    }
}