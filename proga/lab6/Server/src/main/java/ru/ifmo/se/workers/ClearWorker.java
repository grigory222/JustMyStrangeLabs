package ru.ifmo.se.workers;

import ru.ifmo.se.collection.Receiver;
import ru.ifmo.se.dto.replies.ClearReply;
import ru.ifmo.se.dto.replies.Reply;
import ru.ifmo.se.dto.requests.ClearRequest;
import ru.ifmo.se.dto.requests.Request;


public class ClearWorker extends Worker{
    public ClearWorker(Receiver receiver) {
        super(receiver);
    }

    public Reply process(Request request){
        ClearRequest req = (ClearRequest) request;
        ClearReply rep = new ClearReply();

        receiver.clear();
        rep.setSuccess(true);

        System.out.println("[DEBUG] Запрос на очистку коллекции");

        return rep;
    }

}
