package ru.ifmo.se.command;

import ru.ifmo.se.dto.replies.AddReply;
import ru.ifmo.se.dto.replies.UpdateReply;
import ru.ifmo.se.dto.requests.AddRequest;
import ru.ifmo.se.dto.requests.UpdateRequest;
import ru.ifmo.se.entity.LabWork;
import ru.ifmo.se.network.Network;
import ru.ifmo.se.readers.LabWorkReader;
import ru.ifmo.se.receiver.Receiver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class UpdateCommand extends AbstractCommand implements Command{
    public UpdateCommand(Socket socket, Receiver receiver, BufferedReader reader, PrintWriter printer, PrintWriter infoPrinter, String name){
        super(receiver, name, reader, printer, infoPrinter, socket);
    }

    public void execute(String[] args) {
        int id;
        LabWork labWork;
        try {
            labWork = LabWorkReader.readLabWork(reader, printer, infoPrinter);
            id = Integer.parseInt(args[0]);
        } catch (IOException e) {
            printer.println("Не удалось считать элемент");
            return;
        }

        if (labWork == null){
            printer.println("Некорректный ввод! Не удалось считать элемент");
            return;
        }

        UpdateRequest updRequest = new UpdateRequest(id, labWork);
        UpdateReply updReply = (UpdateReply) Network.sendAndReceive(socket, updRequest);
        if (updReply != null && updReply.isSuccess())
            infoPrinter.println("Элемент успешно обновлён");
        else
            infoPrinter.println("Не удалось обновить элемент в коллекции");
        infoPrinter.println(updReply != null ? updReply.getMessage() : "");
    }
}