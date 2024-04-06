package ru.ifmo.se.command;

import ru.ifmo.se.receiver.Receiver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ExitCommand extends AbstractCommand implements Command{
    public ExitCommand(Socket socket, Receiver receiver, BufferedReader reader, PrintWriter printer, PrintWriter infoPrinter, String name){
        super(receiver, name, reader, printer, infoPrinter, socket);
    }

    public void execute(String[] args) {
        try {
            socket.close();
        } catch (IOException ignored) {}
    }
}