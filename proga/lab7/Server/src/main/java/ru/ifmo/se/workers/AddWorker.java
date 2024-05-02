package ru.ifmo.se.workers;

import ru.ifmo.se.collection.Receiver;
import ru.ifmo.se.dto.responses.AddIfMaxResponse;
import ru.ifmo.se.dto.responses.AddResponse;
import ru.ifmo.se.dto.requests.AddRequest;
import ru.ifmo.se.dto.responses.Response;
import ru.ifmo.se.dto.requests.Request;
import ru.ifmo.se.dto.responses.TokenErrorResponse;
import ru.ifmo.se.network.JwtManager;

public class AddWorker extends Worker{
    public AddWorker(Receiver receiver, JwtManager jwtManager) {
        super(receiver, jwtManager);
    }

    public Response process(Request request){
        AddRequest req = (AddRequest) request;
        long id = jwtManager.decodeJwtToken(req.token);
        if (id < 0){
            var resp = new AddResponse();
            resp.setSuccess(false);
            resp.setTokenError(true);
            return resp;
        }

        AddResponse rep = new AddResponse();
        receiver.add(req.getLabWork(), id);
        rep.setSuccess(true);
        rep.setMessage("Элемент успешно добавлен в коллекцию");

        System.out.println("[DEBUG] Запрос на добавление элемента в коллекцию");

        return rep;
    }

}
