package ru.ifmo.se.workers;

import ru.ifmo.se.collection.Receiver;
import ru.ifmo.se.dto.responses.Response;
import ru.ifmo.se.dto.responses.ShowResponse;
import ru.ifmo.se.dto.requests.Request;
import ru.ifmo.se.dto.requests.ShowRequest;


public class ShowWorker extends Worker{
    public ShowWorker(Receiver receiver) {
        super(receiver);
    }

    public Response process(Request request){
        ShowRequest req = (ShowRequest) request;
        ShowResponse rep = new ShowResponse();

        rep.setSuccess(true);
        rep.setResult(receiver.show());

        System.out.println("[DEBUG] Запрос на показ коллекции");

        return rep;
    }

}
