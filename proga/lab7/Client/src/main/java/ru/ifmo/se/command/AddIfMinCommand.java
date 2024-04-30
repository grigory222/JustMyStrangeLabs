package ru.ifmo.se.command;

import ru.ifmo.se.dto.responses.AddIfMinResponse;
import ru.ifmo.se.dto.requests.AddIfMinRequest;
import ru.ifmo.se.entity.LabWork;
import ru.ifmo.se.network.Network;
import ru.ifmo.se.entity.readers.LabWorkReader;
import ru.ifmo.se.receiver.Receiver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class AddIfMinCommand extends AbstractCommand implements Command{
    public AddIfMinCommand(Socket socket, Receiver receiver, BufferedReader reader, PrintWriter printer, PrintWriter infoPrinter, String name){
        super(receiver, name, reader, printer, infoPrinter, socket);
    }

    public void execute(String[] args) {
        LabWork labWork;
        try {
            labWork = LabWorkReader.readLabWork(reader, printer, infoPrinter);
        } catch (IOException e) {
            infoPrinter.println("Не удалось считать элемент");
            return;
        }

        if (labWork == null){
            infoPrinter.println("Некорректный ввод! Не удалось добавить элемент");
            return;
        }

        AddIfMinRequest addIfMinRequest = new AddIfMinRequest(labWork);
        AddIfMinResponse addIfMinResponse = (AddIfMinResponse) Network.sendAndReceive(socket, addIfMinRequest);
        if (addIfMinResponse != null && addIfMinResponse.isSuccess())
            infoPrinter.println(addIfMinResponse.getMessage());
        else
            infoPrinter.println("Не удалось добавить элемент в коллекцию");
    }
}