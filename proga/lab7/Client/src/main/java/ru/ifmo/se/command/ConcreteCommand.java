package ru.ifmo.se.command;

import lombok.Getter;
import ru.ifmo.se.receiver.Receiver;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

// конкретная команда. Для записи в историю команд
@Getter
public class ConcreteCommand extends AbstractCommand{
    private final String[] args;
    ConcreteCommand(Socket socket, Receiver receiver, String name, BufferedReader reader, PrintWriter printWriter, PrintWriter infoPrinter, String[] args) {
        super(receiver, name, reader, printWriter, infoPrinter, socket);
        this.args = args;
    }
    ConcreteCommand(Socket socket, Receiver receiver, AbstractCommand cmd, String[] args){
        super(receiver, cmd.name, cmd.reader, cmd.printer, cmd.infoPrinter, socket);
        this.args = args;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(name);
        for (String s: args){
            result.append(result).append(" ").append(s);
        };
        return result.toString();
    }
}
