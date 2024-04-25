package ru.ifmo.se.command;

import ru.ifmo.se.dto.replies.HelpReply;
import ru.ifmo.se.dto.replies.InfoReply;
import ru.ifmo.se.dto.requests.HelpRequest;
import ru.ifmo.se.dto.requests.InfoRequest;
import ru.ifmo.se.network.Network;
import ru.ifmo.se.receiver.Receiver;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class HelpCommand extends AbstractCommand implements Command{
    public HelpCommand(Socket socket, Receiver receiver, BufferedReader reader, PrintWriter printer, PrintWriter infoPrinter, String name){
        super(receiver, name, reader, printer, infoPrinter, socket);
    }

    public void execute(String[] args) {
        HelpRequest request = new HelpRequest();
        HelpReply helpReply = (HelpReply) Network.sendAndReceive(socket, request);
        if (helpReply != null && helpReply.isSuccess())
            infoPrinter.println(helpReply.getResult());
        else
            infoPrinter.println("Не удалось получить справочную информацию");
    }
}