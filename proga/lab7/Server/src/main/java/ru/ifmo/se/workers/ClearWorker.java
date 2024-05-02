package ru.ifmo.se.workers;

import ru.ifmo.se.collection.Receiver;
import ru.ifmo.se.dto.responses.AddResponse;
import ru.ifmo.se.dto.responses.ClearResponse;
import ru.ifmo.se.dto.responses.Response;
import ru.ifmo.se.dto.requests.ClearRequest;
import ru.ifmo.se.dto.requests.Request;
import ru.ifmo.se.network.JwtManager;


public class ClearWorker extends Worker{
    public ClearWorker(Receiver receiver, JwtManager jwtManager) {
        super(receiver, jwtManager);
    }

    public Response process(Request request){
        ClearRequest req = (ClearRequest) request;
        ClearResponse rep = new ClearResponse();
        long id = jwtManager.decodeJwtToken(req.token);
        if (id < 0){
            var resp = new ClearResponse();
            resp.setSuccess(false);
            resp.setTokenError(true);
            return resp;
        }

        receiver.clear();
        rep.setSuccess(true);

        System.out.println("[DEBUG] Запрос на очистку коллекции");

        return rep;
    }

}
