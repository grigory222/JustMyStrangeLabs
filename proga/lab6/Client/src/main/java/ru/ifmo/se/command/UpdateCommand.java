package ru.ifmo.se.command;

import ru.ifmo.se.dto.replies.UpdateReply;
import ru.ifmo.se.dto.requests.UpdateRequest;
import ru.ifmo.se.entity.LabWork;
import ru.ifmo.se.network.Network;
import ru.ifmo.se.entity.readers.LabWorkReader;
import ru.ifmo.se.receiver.Receiver;

import java.io.BufferedReader;
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
        } catch (Exception e) {
            printer.println("Ошибка ввода. Используйте:  update <число>");
            return;
        }

        if (labWork == null){
            printer.println("Некорректный ввод! Не удалось считать элемент");
            return;
        }

        UpdateRequest updRequest = new UpdateRequest(id, labWork);
        UpdateReply updReply = (UpdateReply) Network.sendAndReceive(socket, updRequest);
        infoPrinter.println(updReply != null ? updReply.getMessage() : "Не удалось обновить элемент в коллекции");
    }
}