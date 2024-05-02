package ru.ifmo.se.workers;

import ru.ifmo.se.collection.Receiver;
import ru.ifmo.se.dto.responses.AddIfMaxResponse;
import ru.ifmo.se.dto.responses.Response;
import ru.ifmo.se.dto.requests.AddIfMaxRequest;
import ru.ifmo.se.dto.requests.Request;
import ru.ifmo.se.dto.responses.TokenErrorResponse;
import ru.ifmo.se.network.JwtManager;

public class AddIfMaxWorker extends Worker{
    public AddIfMaxWorker(Receiver receiver, JwtManager jwtManager) {
        super(receiver, jwtManager);
    }

    public Response process(Request request){
        AddIfMaxRequest req = (AddIfMaxRequest) request;
        long id = jwtManager.decodeJwtToken(req.token);
        if (id < 0){
            var resp = new AddIfMaxResponse();
            resp.setSuccess(false);
            resp.setTokenError(true);
            return resp;
        }

        AddIfMaxResponse rep = new AddIfMaxResponse();
        if (receiver.addIfMax(req.getLabWork(), id)) {
            rep.setMessage("Элемент упешно добавлен в коллекцию");
        } else{
            rep.setMessage("Элемент не является максимальным. Не добавлен в коллекцию");
        }
        rep.setSuccess(true);

        System.out.println("[DEBUG] Запрос на добавление максимального элемента в коллекцию");

        return rep;
    }

}
