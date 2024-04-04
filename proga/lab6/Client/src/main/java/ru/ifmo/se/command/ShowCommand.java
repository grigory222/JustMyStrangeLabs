package ru.ifmo.se.command;

import ru.ifmo.se.dto.replies.AddReply;
import ru.ifmo.se.dto.replies.ShowReply;
import ru.ifmo.se.dto.requests.ShowRequest;
import ru.ifmo.se.entity.LabWork;
import ru.ifmo.se.network.Network;
import ru.ifmo.se.readers.LabWorkReader;
import ru.ifmo.se.receiver.Receiver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ShowCommand extends AbstractCommand implements Command{
    public ShowCommand(Socket socket, Receiver receiver, BufferedReader reader, PrintWriter printer, PrintWriter infoPrinter, String name){
        super(receiver, name, reader, printer, infoPrinter, socket);
    }

    public void execute(String[] args) {
        ShowRequest showRequest = new ShowRequest();
        ShowReply showReply = (ShowReply) Network.sendAndReceive(socket, showRequest);
        if (showReply != null && showReply.isSuccess())
            infoPrinter.println(showReply.getResult());
        else
            infoPrinter.println("Не удалось получить коллекцию");
    }
}