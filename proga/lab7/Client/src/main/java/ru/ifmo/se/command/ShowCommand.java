package ru.ifmo.se.command;

import ru.ifmo.se.dto.responses.ShowResponse;
import ru.ifmo.se.dto.requests.ShowRequest;
import ru.ifmo.se.network.Network;
import ru.ifmo.se.receiver.Receiver;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ShowCommand extends AbstractCommand implements Command{
    public ShowCommand(Socket socket, Receiver receiver, BufferedReader reader, PrintWriter printer, PrintWriter infoPrinter, String name){
        super(receiver, name, reader, printer, infoPrinter, socket);
    }

    public void execute(String[] args, String token) {
        ShowRequest showRequest = new ShowRequest(token);
        ShowResponse showReply = (ShowResponse) Network.sendAndReceive(socket, showRequest);
        if (showReply != null && showReply.isSuccess())
            infoPrinter.println(showReply.getResult());
        else
            infoPrinter.println("Не удалось получить коллекцию");
    }
}