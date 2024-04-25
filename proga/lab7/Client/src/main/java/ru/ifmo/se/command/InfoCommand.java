package ru.ifmo.se.command;

import ru.ifmo.se.dto.replies.InfoReply;
import ru.ifmo.se.dto.replies.ShowReply;
import ru.ifmo.se.dto.requests.InfoRequest;
import ru.ifmo.se.network.Network;
import ru.ifmo.se.receiver.Receiver;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class InfoCommand extends AbstractCommand implements Command{
    public InfoCommand(Socket socket, Receiver receiver, BufferedReader reader, PrintWriter printer, PrintWriter infoPrinter, String name){
        super(receiver, name, reader, printer, infoPrinter, socket);
    }

    public void execute(String[] args) {
        InfoRequest infoRequest = new InfoRequest();
        InfoReply infoReply = (InfoReply) Network.sendAndReceive(socket, infoRequest);
        if (infoReply != null && infoReply.isSuccess())
            infoPrinter.println(infoReply.getResult());
        else
            infoPrinter.println("Не удалось получить информацию о коллекции");
    }
}