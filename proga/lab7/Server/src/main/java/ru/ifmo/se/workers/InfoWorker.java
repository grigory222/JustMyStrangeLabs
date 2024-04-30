package ru.ifmo.se.workers;

import ru.ifmo.se.collection.Receiver;
import ru.ifmo.se.dto.responses.InfoResponse;
import ru.ifmo.se.dto.responses.Response;
import ru.ifmo.se.dto.requests.InfoRequest;
import ru.ifmo.se.dto.requests.Request;


public class InfoWorker extends Worker{
    public InfoWorker(Receiver receiver) {
        super(receiver);
    }

    public Response process(Request request){
        InfoRequest req = (InfoRequest) request;
        InfoResponse rep = new InfoResponse();

        rep.setSuccess(true);
        rep.setResult(receiver.info());

        System.out.println("[DEBUG] Запрос на показ информации о коллекции");

        return rep;
    }

}
