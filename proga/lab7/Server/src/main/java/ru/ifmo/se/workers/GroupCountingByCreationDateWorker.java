package ru.ifmo.se.workers;

import ru.ifmo.se.collection.Receiver;
import ru.ifmo.se.dto.replies.GroupCountingByCreationDateReply;
import ru.ifmo.se.dto.replies.PrintUniqueDifficultyReply;
import ru.ifmo.se.dto.replies.Reply;
import ru.ifmo.se.dto.requests.GroupCountingByCreationDateRequest;
import ru.ifmo.se.dto.requests.PrintUniqueDifficultyRequest;
import ru.ifmo.se.dto.requests.Request;


public class GroupCountingByCreationDateWorker extends Worker{
    public GroupCountingByCreationDateWorker(Receiver receiver) {
        super(receiver);
    }

    public Reply process(Request request){
        GroupCountingByCreationDateRequest req = (GroupCountingByCreationDateRequest) request;
        GroupCountingByCreationDateReply rep = new GroupCountingByCreationDateReply();

        rep.setSuccess(true);
        rep.setResult(receiver.printGroupCountingByCreationDate());

        System.out.println("[DEBUG] Запрос на показ group_counting_by_creation_date");

        return rep;
    }

}
