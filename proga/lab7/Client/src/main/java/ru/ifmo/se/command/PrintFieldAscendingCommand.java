package ru.ifmo.se.command;

import ru.ifmo.se.dto.responses.PrintFieldAscendingResponse;
import ru.ifmo.se.dto.requests.PrintFieldAscendingRequest;
import ru.ifmo.se.network.Network;
import ru.ifmo.se.receiver.Receiver;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class PrintFieldAscendingCommand extends AbstractCommand implements Command{
    public PrintFieldAscendingCommand(Socket socket, Receiver receiver, BufferedReader reader, PrintWriter printer, PrintWriter infoPrinter, String name){
        super(receiver, name, reader, printer, infoPrinter, socket);
    }

    public void execute(String[] args, String token) {
        PrintFieldAscendingRequest request = new PrintFieldAscendingRequest(token);
        PrintFieldAscendingResponse reply = (PrintFieldAscendingResponse) Network.sendAndReceive(socket, request);
        if (reply != null && reply.isSuccess())
            infoPrinter.println(reply.getResult());
        else
            infoPrinter.println("Не удалось выполнить команду print_field_ascending");
    }
}