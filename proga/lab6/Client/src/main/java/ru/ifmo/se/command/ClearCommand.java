package ru.ifmo.se.command;

import ru.ifmo.se.dto.replies.ClearReply;
import ru.ifmo.se.dto.replies.HelpReply;
import ru.ifmo.se.dto.requests.ClearRequest;
import ru.ifmo.se.dto.requests.HelpRequest;
import ru.ifmo.se.network.Network;
import ru.ifmo.se.receiver.Receiver;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClearCommand extends AbstractCommand implements Command{
    public ClearCommand(Socket socket, Receiver receiver, BufferedReader reader, PrintWriter printer, PrintWriter infoPrinter, String name){
        super(receiver, name, reader, printer, infoPrinter, socket);
    }

    public void execute(String[] args) {
        ClearRequest request = new ClearRequest();
        ClearReply clearReply = (ClearReply) Network.sendAndReceive(socket, request);
        if (clearReply != null && clearReply.isSuccess())
            infoPrinter.println("Коллекция очищена");
        else
            infoPrinter.println("Не удалось очистить коллекцию");
    }
}