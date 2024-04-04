package ru.ifmo.se.command;

import ru.ifmo.se.dto.replies.AddIfMaxReply;
import ru.ifmo.se.entity.LabWork;
import ru.ifmo.se.readers.LabWorkReader;
import ru.ifmo.se.receiver.Receiver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class AddIfMaxCommand extends AbstractCommand implements Command{
    public AddIfMaxCommand(Receiver receiver, BufferedReader reader, PrintWriter printer, PrintWriter infoPrinter, String name){
        super(receiver, name, reader, printer, infoPrinter);
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

        AddIfMaxReply addIfMaxReply = receiver.addIfMax(labWork);
        if (addIfMaxReply.isSuccess())
            infoPrinter.println(addIfMaxReply.getMessage());
        else
            infoPrinter.println("Не удалось добавить элемент в коллекцию");
    }
}