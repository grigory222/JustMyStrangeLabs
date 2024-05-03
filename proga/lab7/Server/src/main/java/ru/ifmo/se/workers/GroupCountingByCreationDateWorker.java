package ru.ifmo.se.workers;

import ru.ifmo.se.collection.Receiver;
import ru.ifmo.se.dto.responses.AddResponse;
import ru.ifmo.se.dto.responses.GroupCountingByCreationDateResponse;
import ru.ifmo.se.dto.responses.Response;
import ru.ifmo.se.dto.requests.GroupCountingByCreationDateRequest;
import ru.ifmo.se.dto.requests.Request;
import ru.ifmo.se.network.JwtManager;


public class GroupCountingByCreationDateWorker extends Worker{
    public GroupCountingByCreationDateWorker(Receiver receiver, JwtManager jwtManager) {
        super(receiver, jwtManager);
    }

    public Response process(Request request){
        GroupCountingByCreationDateRequest req = (GroupCountingByCreationDateRequest) request;
        GroupCountingByCreationDateResponse rep = new GroupCountingByCreationDateResponse();
        int id = jwtManager.decodeJwtToken(req.token);
        if (id < 0){
            var resp = new GroupCountingByCreationDateResponse();
            resp.setSuccess(false);
            resp.setTokenError(true);
            return resp;
        }

        rep.setSuccess(true);
        rep.setResult(receiver.printGroupCountingByCreationDate());

        System.out.println("[DEBUG] Запрос на показ group_counting_by_creation_date");

        return rep;
    }

}
