package ru.ifmo.se.workers;

import ru.ifmo.se.collection.Receiver;
import ru.ifmo.se.dto.responses.PrintFieldAscendingResponse;
import ru.ifmo.se.dto.responses.Response;
import ru.ifmo.se.dto.requests.PrintFieldAscendingRequest;
import ru.ifmo.se.dto.requests.Request;


public class PrintFieldAscendingWorker extends Worker{
    public PrintFieldAscendingWorker(Receiver receiver) {
        super(receiver);
    }

    public Response process(Request request){
        PrintFieldAscendingRequest req = (PrintFieldAscendingRequest) request;
        PrintFieldAscendingResponse rep = new PrintFieldAscendingResponse();

        rep.setSuccess(true);
        rep.setResult(receiver.printFieldAscendingAuthors());

        System.out.println("[DEBUG] Запрос на показ field_ascending_authors");

        return rep;
    }

}
