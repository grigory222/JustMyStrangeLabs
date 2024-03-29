package ru.ifmo.se.command;

import ru.ifmo.se.entity.LabWork;
import ru.ifmo.se.entity.LabWorkReader;
import ru.ifmo.se.receiver.CollectionReceiver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class AddCommand extends AbstractCommand implements Command{
    private final CollectionReceiver receiver;
    public AddCommand(CollectionReceiver receiver, BufferedReader reader, PrintWriter printer, PrintWriter infoPrinter, String name){
        super(name, reader, printer, infoPrinter);
        this.receiver = receiver;
    }

    public void execute(String[] args) {
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

        // установка автогенерируемых полей + вызов collectionHandler
        receiver.add(labWork);

        printer.println("Элемент добавлен");
    }
}