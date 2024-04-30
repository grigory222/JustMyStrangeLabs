package ru.ifmo.se.workers;

import ru.ifmo.se.collection.Receiver;
import ru.ifmo.se.dto.responses.ClearResponse;
import ru.ifmo.se.dto.responses.Response;
import ru.ifmo.se.dto.requests.ClearRequest;
import ru.ifmo.se.dto.requests.Request;


public class ClearWorker extends Worker{
    public ClearWorker(Receiver receiver) {
        super(receiver);
    }

    public Response process(Request request){
        ClearRequest req = (ClearRequest) request;
        ClearResponse rep = new ClearResponse();

        receiver.clear();
        rep.setSuccess(true);

        System.out.println("[DEBUG] Запрос на очистку коллекции");

        return rep;
    }

}
