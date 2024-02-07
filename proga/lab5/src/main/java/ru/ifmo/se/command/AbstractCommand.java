package ru.ifmo.se.command;

public abstract class AbstractCommand {
    private final String name;
    AbstractCommand(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }

}
