package ru.ifmo.se.command;

import ru.ifmo.se.dto.replies.InfoReply;
import ru.ifmo.se.dto.replies.PrintUniqueDifficultyReply;
import ru.ifmo.se.dto.requests.PrintUniqueDifficultyRequest;
import ru.ifmo.se.network.Network;
import ru.ifmo.se.receiver.Receiver;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class PrintUniqueDifficultyCommand extends AbstractCommand implements Command{
    public PrintUniqueDifficultyCommand(Socket socket, Receiver receiver, BufferedReader reader, PrintWriter printer, PrintWriter infoPrinter, String name){
        super(receiver, name, reader, printer, infoPrinter, socket);
    }

    public void execute(String[] args) {
        PrintUniqueDifficultyRequest request = new PrintUniqueDifficultyRequest();
        PrintUniqueDifficultyReply reply = (PrintUniqueDifficultyReply) Network.sendAndReceive(socket, request);
        if (reply != null && reply.isSuccess())
            infoPrinter.println(reply.getResult());
        else
            infoPrinter.println("Не удалось выполнить команду print_unique_difficulty");
    }
}