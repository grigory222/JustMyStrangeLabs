package ru.ifmo.se.workers;

import ru.ifmo.se.collection.Receiver;
import ru.ifmo.se.dto.replies.HelpReply;
import ru.ifmo.se.dto.replies.Reply;
import ru.ifmo.se.dto.replies.ShowReply;
import ru.ifmo.se.dto.requests.HelpRequest;
import ru.ifmo.se.dto.requests.Request;
import ru.ifmo.se.dto.requests.ShowRequest;


public class HelpWorker extends Worker{
    public HelpWorker(Receiver receiver) {
        super(receiver);
    }

    public Reply process(Request request){
        HelpRequest req = (HelpRequest) request;
        HelpReply rep = new HelpReply();

        rep.setSuccess(true);
        rep.setResult(receiver.help());

        System.out.println("[DEBUG] Запрос на показ справки");

        return rep;
    }

}
