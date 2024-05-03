package ru.ifmo.se.workers;

import ru.ifmo.se.collection.Receiver;
import ru.ifmo.se.dto.responses.HelpResponse;
import ru.ifmo.se.dto.responses.InfoResponse;
import ru.ifmo.se.dto.responses.Response;
import ru.ifmo.se.dto.requests.InfoRequest;
import ru.ifmo.se.dto.requests.Request;
import ru.ifmo.se.network.JwtManager;


public class InfoWorker extends Worker{
    public InfoWorker(Receiver receiver, JwtManager jwtManager) {
        super(receiver, jwtManager);
    }

    public Response process(Request request){
        InfoRequest req = (InfoRequest) request;
        InfoResponse rep = new InfoResponse();
        int id = jwtManager.decodeJwtToken(req.token);
        if (id < 0){
            var resp = new InfoResponse();
            resp.setSuccess(false);
            resp.setTokenError(true);
            return resp;
        }

        rep.setSuccess(true);
        rep.setResult(receiver.info());

        System.out.println("[DEBUG] Запрос на показ информации о коллекции");

        return rep;
    }

}
