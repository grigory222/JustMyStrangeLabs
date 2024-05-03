package ru.ifmo.se.workers;

import ru.ifmo.se.collection.Receiver;
import ru.ifmo.se.dto.responses.RemoveByIdResponse;
import ru.ifmo.se.dto.responses.Response;
import ru.ifmo.se.dto.responses.ShowResponse;
import ru.ifmo.se.dto.requests.Request;
import ru.ifmo.se.dto.requests.ShowRequest;
import ru.ifmo.se.network.JwtManager;


public class ShowWorker extends Worker{
    public ShowWorker(Receiver receiver, JwtManager jwtManager) {
        super(receiver, jwtManager);
    }

    public Response process(Request request){
        ShowRequest req = (ShowRequest) request;
        ShowResponse rep = new ShowResponse();
        int id = jwtManager.decodeJwtToken(req.token);
        if (id < 0){
            var resp = new ShowResponse();
            resp.setSuccess(false);
            resp.setTokenError(true);
            return resp;
        }

        rep.setSuccess(true);
        rep.setResult(receiver.show());

        System.out.println("[DEBUG] Запрос на показ коллекции");

        return rep;
    }

}
