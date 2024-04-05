package ru.ifmo.se.workers;

import ru.ifmo.se.collection.Receiver;
import ru.ifmo.se.dto.replies.Reply;
import ru.ifmo.se.dto.replies.ShowReply;
import ru.ifmo.se.dto.requests.Request;
import ru.ifmo.se.dto.requests.ShowRequest;


public class ShowWorker extends Worker{
    public ShowWorker(Receiver receiver) {
        super(receiver);
    }

    public Reply process(Request request){
        ShowRequest req = (ShowRequest) request;
        ShowReply rep = new ShowReply();

        rep.setSuccess(true);
        rep.setResult(receiver.show());

        System.out.println("[DEBUG] Запрос на показ коллекции");

        return rep;
    }

}
