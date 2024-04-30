package ru.ifmo.se.workers;

import ru.ifmo.se.collection.Receiver;
import ru.ifmo.se.dto.responses.HelpResponse;
import ru.ifmo.se.dto.responses.Response;
import ru.ifmo.se.dto.requests.HelpRequest;
import ru.ifmo.se.dto.requests.Request;


public class HelpWorker extends Worker{
    public HelpWorker(Receiver receiver) {
        super(receiver);
    }

    public Response process(Request request){
        HelpRequest req = (HelpRequest) request;
        HelpResponse rep = new HelpResponse();

        rep.setSuccess(true);
        rep.setResult(receiver.help());

        System.out.println("[DEBUG] Запрос на показ справки");

        return rep;
    }

}
