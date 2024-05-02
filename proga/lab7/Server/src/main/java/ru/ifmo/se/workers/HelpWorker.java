package ru.ifmo.se.workers;

import ru.ifmo.se.collection.Receiver;
import ru.ifmo.se.dto.responses.GroupCountingByCreationDateResponse;
import ru.ifmo.se.dto.responses.HelpResponse;
import ru.ifmo.se.dto.responses.Response;
import ru.ifmo.se.dto.requests.HelpRequest;
import ru.ifmo.se.dto.requests.Request;
import ru.ifmo.se.network.JwtManager;


public class HelpWorker extends Worker{
    public HelpWorker(Receiver receiver, JwtManager jwtManager) {
        super(receiver, jwtManager);
    }

    public Response process(Request request){
        HelpRequest req = (HelpRequest) request;
        HelpResponse rep = new HelpResponse();
        long id = jwtManager.decodeJwtToken(req.token);
        if (id < 0){
            var resp = new HelpResponse();
            resp.setSuccess(false);
            resp.setTokenError(true);
            return resp;
        }

        rep.setSuccess(true);
        rep.setResult(receiver.help());

        System.out.println("[DEBUG] Запрос на показ справки");

        return rep;
    }

}
