package ru.ifmo.se.command;

import ru.ifmo.se.entity.LabWork;
import ru.ifmo.se.entity.LabWorkReader;
import ru.ifmo.se.receiver.CollectionReceiver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class UpdateCommand extends AbstractCommand implements Command{
    private final CollectionReceiver receiver;
    public UpdateCommand(CollectionReceiver receiver, BufferedReader reader, PrintWriter printer, PrintWriter infoPrinter, String name){
        super(name, reader, printer, infoPrinter);
        this.receiver = receiver;
    }

    public void execute(String[] args) {
        int id;
        LabWork labWork;

        printer.println("Введите новые данные для лабороторной работы");
        try {
            if (args.length < 1) throw new NumberFormatException();
            id = Integer.parseInt(args[0]);
            labWork = LabWorkReader.readLabWork(reader, printer, infoPrinter);
            receiver.update(id, labWork);
        } catch (IOException e) {
            printer.println("Не удалось считать элемент");
            return;
        } catch (NullPointerException | NumberFormatException e) {
            printer.println("Нет элемента с таким id");
            printer.println("Через пробел укажите id элемента, который хотите удалить, 'update {id}'");
            return;
        }

        printer.println("Элемент изменён");
    }
}