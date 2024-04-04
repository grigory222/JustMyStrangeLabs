package ru.ifmo.se.command;

import ru.ifmo.se.dto.replies.AddReply;
import ru.ifmo.se.dto.replies.ShowReply;
import ru.ifmo.se.entity.LabWork;
import ru.ifmo.se.readers.LabWorkReader;
import ru.ifmo.se.receiver.Receiver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class ShowCommand extends AbstractCommand implements Command{
    public ShowCommand(Receiver receiver, BufferedReader reader, PrintWriter printer, PrintWriter infoPrinter, String name){
        super(receiver, name, reader, printer, infoPrinter);
    }

    public void execute(String[] args) {
        ShowReply showReply = receiver.show();
        if (showReply.isSuccess())
            infoPrinter.println(showReply.getResult());
        else
            infoPrinter.println("Не удалось получить коллекцию");
    }
}