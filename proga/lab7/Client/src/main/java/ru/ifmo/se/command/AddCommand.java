package ru.ifmo.se.command;

import ru.ifmo.se.dto.responses.AddResponse;
import ru.ifmo.se.dto.requests.AddRequest;
import ru.ifmo.se.entity.LabWork;
import ru.ifmo.se.network.Network;
import ru.ifmo.se.entity.readers.LabWorkReader;
import ru.ifmo.se.receiver.Receiver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class AddCommand extends AbstractCommand implements Command{
    public AddCommand(Socket socket, Receiver receiver, BufferedReader reader, PrintWriter printer, PrintWriter infoPrinter, String name){
        super(receiver, name, reader, printer, infoPrinter, socket);
    }

    public void execute(String[] args, String token) {
        LabWork labWork;
        try {
            labWork = LabWorkReader.readLabWork(reader, printer, infoPrinter);
        } catch (IOException e) {
            printer.println("Не удалось считать элемент");
            return;
        }

        if (labWork == null){
            printer.println("Некорректный ввод! Не удалось добавить элемент");
            return;
        }

        AddRequest addRequest = new AddRequest(labWork, token);
        AddResponse addResponse = (AddResponse) Network.sendAndReceive(socket, addRequest);
        if (addResponse != null && addResponse.isSuccess())
            infoPrinter.println(addResponse.getMessage());
        else {
            infoPrinter.println("Не удалось добавить элемент в коллекцию");
        }
    }
}