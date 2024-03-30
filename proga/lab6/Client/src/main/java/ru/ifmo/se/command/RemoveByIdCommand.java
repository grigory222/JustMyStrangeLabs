package ru.ifmo.se.command;

import ru.ifmo.se.entity.LabWork;
import ru.ifmo.se.receiver.Receiver;

import java.io.BufferedReader;
import java.io.PrintWriter;

public class RemoveByIdCommand extends AbstractCommand implements Command{
    private final CollectionReceiver receiver;
    public RemoveByIdCommand(Receiver receiver, BufferedReader reader, PrintWriter printer, PrintWriter infoPrinter, String name){
        super(name, reader, printer, infoPrinter);
        this.receiver = receiver;
    }

    public void execute(String[] args) {
        int id;
        LabWork labWork;

        try{
            if (args.length < 1) throw new NumberFormatException();
            id = Integer.parseInt(args[0]);
            receiver.removeById(id);
        } catch(NumberFormatException | NullPointerException e){
            printer.println("Нет элемента с таким id");
            printer.println("Через пробел укажите id элемента, который хотите удалить, 'remove_by_id {id}'");
            return;
        }
        printer.println("Элемент удалён");
    }
}