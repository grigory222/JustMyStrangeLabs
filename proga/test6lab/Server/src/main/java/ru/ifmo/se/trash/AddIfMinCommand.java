package ru.ifmo.se.command;

import ru.ifmo.se.entity.LabWork;
import ru.ifmo.se.readers.LabWorkReader;
import ru.ifmo.se.receiver.Receiver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class AddIfMinCommand extends AbstractCommand implements Command{
    private final CollectionReceiver receiver;
    public AddIfMinCommand(Receiver receiver, BufferedReader reader, PrintWriter printer, PrintWriter infoPrinter, String name){
        super(name, reader, printer, infoPrinter);
        this.receiver = receiver;
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
        };

        if (receiver.addIfMin(labWork)){
            infoPrinter.println("Элемент добавлен");
        } else{
            printer.println("Элемент не добавлен");
        }
    }
}