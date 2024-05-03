package ru.ifmo.se.workers;

import ru.ifmo.se.collection.Receiver;
import ru.ifmo.se.dto.responses.HelpResponse;
import ru.ifmo.se.dto.responses.PrintFieldAscendingResponse;
import ru.ifmo.se.dto.responses.Response;
import ru.ifmo.se.dto.requests.PrintFieldAscendingRequest;
import ru.ifmo.se.dto.requests.Request;
import ru.ifmo.se.network.JwtManager;


public class PrintFieldAscendingWorker extends Worker{
    public PrintFieldAscendingWorker(Receiver receiver, JwtManager jwtManager) {
        super(receiver, jwtManager);
    }

    public Response process(Request request){
        PrintFieldAscendingRequest req = (PrintFieldAscendingRequest) request;
        PrintFieldAscendingResponse rep = new PrintFieldAscendingResponse();
        int id = jwtManager.decodeJwtToken(req.token);
        if (id < 0){
            var resp = new PrintFieldAscendingResponse();
            resp.setSuccess(false);
            resp.setTokenError(true);
            return resp;
        }

        rep.setSuccess(true);
        rep.setResult(receiver.printFieldAscendingAuthors());

        System.out.println("[DEBUG] Запрос на показ field_ascending_authors");

        return rep;
    }

}
