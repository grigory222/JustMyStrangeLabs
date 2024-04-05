package ru.ifmo.se.workers;

import ru.ifmo.se.collection.Receiver;
import ru.ifmo.se.dto.replies.PrintUniqueDifficultyReply;
import ru.ifmo.se.dto.replies.Reply;
import ru.ifmo.se.dto.replies.ShowReply;
import ru.ifmo.se.dto.requests.PrintUniqueDifficultyRequest;
import ru.ifmo.se.dto.requests.Request;
import ru.ifmo.se.dto.requests.ShowRequest;


public class PrintUniqueDifficultyWorker extends Worker{
    public PrintUniqueDifficultyWorker(Receiver receiver) {
        super(receiver);
    }

    public Reply process(Request request){
        PrintUniqueDifficultyRequest req = (PrintUniqueDifficultyRequest) request;
        PrintUniqueDifficultyReply rep = new PrintUniqueDifficultyReply();

        rep.setSuccess(true);
        rep.setResult(receiver.printUniqueDifficulty());

        System.out.println("[DEBUG] Запрос на показ unique_difficulty");

        return rep;
    }

}
