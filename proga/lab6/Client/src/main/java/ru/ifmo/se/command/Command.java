package ru.ifmo.se.command;

import ru.ifmo.se.dto.Reply;

public interface Command {
    void execute(String[] args);
}