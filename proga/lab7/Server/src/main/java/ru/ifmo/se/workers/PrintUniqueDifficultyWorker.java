package ru.ifmo.se.workers;

import ru.ifmo.se.collection.Receiver;
import ru.ifmo.se.dto.responses.PrintUniqueDifficultyResponse;
import ru.ifmo.se.dto.responses.Response;
import ru.ifmo.se.dto.requests.PrintUniqueDifficultyRequest;
import ru.ifmo.se.dto.requests.Request;


public class PrintUniqueDifficultyWorker extends Worker{
    public PrintUniqueDifficultyWorker(Receiver receiver) {
        super(receiver);
    }

    public Response process(Request request){
        PrintUniqueDifficultyRequest req = (PrintUniqueDifficultyRequest) request;
        PrintUniqueDifficultyResponse rep = new PrintUniqueDifficultyResponse();

        rep.setSuccess(true);
        rep.setResult(receiver.printUniqueDifficulty());

        System.out.println("[DEBUG] Запрос на показ unique_difficulty");

        return rep;
    }

}
