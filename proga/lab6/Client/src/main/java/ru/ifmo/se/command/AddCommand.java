package ru.ifmo.se.command;

import ru.ifmo.se.dto.Reply;
import ru.ifmo.se.entity.LabWork;
import ru.ifmo.se.readers.LabWorkReader;
import ru.ifmo.se.receiver.Receiver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class AddCommand extends AbstractCommand implements Command{
    private final Receiver receiver;
    public AddCommand(Receiver receiver, BufferedReader reader, PrintWriter printer, PrintWriter infoPrinter, String name){
        super(name, reader, printer, infoPrinter);
        this.receiver = receiver;
    }

    public Reply execute(String[] args) {
        LabWork labWork;
        try {
            labWork = LabWorkReader.readLabWork(reader, printer, infoPrinter);
        } catch (IOException e) {
            printer.println("Не удалось считать элемент");
            return null;
        }

        if (labWork == null){
            printer.println("Некорректный ввод! Не удалось добавить элемент");
            return null;
        }

        return receiver.add(labWork);

    }
}