package ru.ifmo.se.workers;

import ru.ifmo.se.collection.Receiver;
import ru.ifmo.se.dto.replies.InfoReply;
import ru.ifmo.se.dto.replies.Reply;
import ru.ifmo.se.dto.requests.InfoRequest;
import ru.ifmo.se.dto.requests.Request;


public class InfoWorker extends Worker{
    public InfoWorker(Receiver receiver) {
        super(receiver);
    }

    public Reply process(Request request){
        InfoRequest req = (InfoRequest) request;
        InfoReply rep = new InfoReply();

        rep.setSuccess(true);
        rep.setResult(receiver.info());

        System.out.println("[DEBUG] Запрос на показ информации о коллекции");

        return rep;
    }

}
