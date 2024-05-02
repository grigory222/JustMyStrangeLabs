package ru.ifmo.se.command;

import ru.ifmo.se.dto.responses.RemoveByIdResponse;
import ru.ifmo.se.dto.requests.RemoveByIdRequest;
import ru.ifmo.se.network.Network;
import ru.ifmo.se.receiver.Receiver;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class RemoveByIdCommand extends AbstractCommand implements Command{
    public RemoveByIdCommand(Socket socket, Receiver receiver, BufferedReader reader, PrintWriter printer, PrintWriter infoPrinter, String name){
        super(receiver, name, reader, printer, infoPrinter, socket);
    }

    public void execute(String[] args, String token) {
        int id;
        try {
            id = Integer.parseInt(args[0]);
        } catch (Exception e) {
            printer.println("Неверный формат команды. Используйте remove_by_id <число>");
            return;
        }

        RemoveByIdRequest request = new RemoveByIdRequest(id, token);
        RemoveByIdResponse reply = (RemoveByIdResponse) Network.sendAndReceive(socket, request);
        if (reply != null && reply.isSuccess())
            infoPrinter.println("Элемент успешно удалён");
        else
            infoPrinter.println("Не удалось удалить элемент из коллекции");
        infoPrinter.println(reply != null ? reply.getMessage() : "");
    }
}