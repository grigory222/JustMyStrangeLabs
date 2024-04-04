package ru.ifmo.se.command;

import ru.ifmo.se.dto.replies.GroupCountingByCreationDateReply;
import ru.ifmo.se.dto.replies.ShowReply;
import ru.ifmo.se.dto.requests.GroupCountingByCreationDateRequest;
import ru.ifmo.se.dto.requests.ShowRequest;
import ru.ifmo.se.network.Network;
import ru.ifmo.se.receiver.Receiver;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class GroupCountingByCreationDateCommand extends AbstractCommand implements Command{
    public GroupCountingByCreationDateCommand(Socket socket, Receiver receiver, BufferedReader reader, PrintWriter printer, PrintWriter infoPrinter, String name){
        super(receiver, name, reader, printer, infoPrinter, socket);
    }

    public void execute(String[] args) {
        GroupCountingByCreationDateRequest request = new GroupCountingByCreationDateRequest();
        GroupCountingByCreationDateReply reply = (GroupCountingByCreationDateReply) Network.sendAndReceive(socket, request);
        if (reply != null && reply.isSuccess())
            infoPrinter.println(reply.getResult());
        else
            infoPrinter.println("Не удалось получить сгруппированную коллекцию");
    }
}