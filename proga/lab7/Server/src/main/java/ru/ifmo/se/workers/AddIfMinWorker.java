package ru.ifmo.se.workers;

import ru.ifmo.se.collection.Receiver;
import ru.ifmo.se.dto.responses.AddIfMinResponse;
import ru.ifmo.se.dto.responses.Response;
import ru.ifmo.se.dto.requests.AddIfMinRequest;
import ru.ifmo.se.dto.requests.Request;
import ru.ifmo.se.network.JwtManager;

public class AddIfMinWorker extends Worker{
    public AddIfMinWorker(Receiver receiver, JwtManager jwtManager) {
        super(receiver, jwtManager);
    }

    public Response process(Request request){
        AddIfMinRequest req = (AddIfMinRequest) request;
        long id = jwtManager.decodeJwtToken(req.token);
        if (id < 0){
            var resp = new AddIfMinResponse();
            resp.setSuccess(false);
            resp.setTokenError(true);
            return resp;
        }

        AddIfMinResponse rep = new AddIfMinResponse();
        if (receiver.addIfMin(req.getLabWork(), id)) {
            rep.setMessage("Элемент упешно добавлен в коллекцию");
        } else{
            rep.setMessage("Элемент не является минимальным. Не добавлен в коллекцию");
        }
        rep.setSuccess(true);

        System.out.println("[DEBUG] Запрос на добавление максимального элемента в коллекцию");

        return rep;
    }

}
