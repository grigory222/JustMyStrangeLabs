package ru.ifmo.se.command;

import ru.ifmo.se.dto.responses.InfoResponse;
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
        InfoResponse infoResponse = (InfoResponse) Network.sendAndReceive(socket, infoRequest);
        if (infoResponse != null && infoResponse.isSuccess())
            infoPrinter.println(infoResponse.getResult());
        else
            infoPrinter.println("Не удалось получить информацию о коллекции");
    }
}