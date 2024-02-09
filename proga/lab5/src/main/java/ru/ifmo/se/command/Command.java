package ru.ifmo.se.command;

import ru.ifmo.se.receiver.Receiver;

import java.io.IOException;

public interface Command {
    void execute(String[] args);
}