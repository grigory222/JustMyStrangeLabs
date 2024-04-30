package ru.ifmo.se.workers;

import ru.ifmo.se.collection.Receiver;
import ru.ifmo.se.dto.responses.GroupCountingByCreationDateResponse;
import ru.ifmo.se.dto.responses.Response;
import ru.ifmo.se.dto.requests.GroupCountingByCreationDateRequest;
import ru.ifmo.se.dto.requests.Request;


public class GroupCountingByCreationDateWorker extends Worker{
    public GroupCountingByCreationDateWorker(Receiver receiver) {
        super(receiver);
    }

    public Response process(Request request){
        GroupCountingByCreationDateRequest req = (GroupCountingByCreationDateRequest) request;
        GroupCountingByCreationDateResponse rep = new GroupCountingByCreationDateResponse();

        rep.setSuccess(true);
        rep.setResult(receiver.printGroupCountingByCreationDate());

        System.out.println("[DEBUG] Запрос на показ group_counting_by_creation_date");

        return rep;
    }

}
