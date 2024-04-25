package ru.ifmo.se.workers;

import ru.ifmo.se.collection.Receiver;
import ru.ifmo.se.dto.replies.PrintFieldAscendingReply;
import ru.ifmo.se.dto.replies.PrintUniqueDifficultyReply;
import ru.ifmo.se.dto.replies.Reply;
import ru.ifmo.se.dto.requests.PrintFieldAscendingRequest;
import ru.ifmo.se.dto.requests.PrintUniqueDifficultyRequest;
import ru.ifmo.se.dto.requests.Request;


public class PrintFieldAscendingWorker extends Worker{
    public PrintFieldAscendingWorker(Receiver receiver) {
        super(receiver);
    }

    public Reply process(Request request){
        PrintFieldAscendingRequest req = (PrintFieldAscendingRequest) request;
        PrintFieldAscendingReply rep = new PrintFieldAscendingReply();

        rep.setSuccess(true);
        rep.setResult(receiver.printFieldAscendingAuthors());

        System.out.println("[DEBUG] Запрос на показ field_ascending_authors");

        return rep;
    }

}
