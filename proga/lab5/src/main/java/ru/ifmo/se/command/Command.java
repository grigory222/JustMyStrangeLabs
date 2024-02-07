package ru.ifmo.se.command;

import ru.ifmo.se.receiver.Receiver;

public interface Command {
    void execute(String[] args);
}