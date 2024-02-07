package ru.ifmo.se.command;

import ru.ifmo.se.receiver.IoReceiver;

public class ShowCommand<T> extends AbstractCommand implements Command{
    private final IoReceiver<T> receiver;
    public ShowCommand(IoReceiver<T> receiver, String name){
        super(name);
        this.receiver = receiver;
    }

    public void execute(String[] args){
        // logic
        receiver.show();
    }
}