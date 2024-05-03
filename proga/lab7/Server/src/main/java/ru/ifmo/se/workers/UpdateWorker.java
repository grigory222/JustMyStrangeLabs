package ru.ifmo.se.workers;

import ru.ifmo.se.collection.Receiver;
import ru.ifmo.se.dto.responses.Response;
import ru.ifmo.se.dto.responses.ShowResponse;
import ru.ifmo.se.dto.responses.UpdateResponse;
import ru.ifmo.se.dto.requests.Request;
import ru.ifmo.se.dto.requests.UpdateRequest;
import ru.ifmo.se.network.JwtManager;

public class UpdateWorker extends Worker {
    public UpdateWorker(Receiver receiver, JwtManager jwtManager) {
        super(receiver, jwtManager);
    }

    public Response process(Request request) {
        UpdateRequest req = (UpdateRequest) request;
        UpdateResponse rep = new UpdateResponse();
        int ownerId = jwtManager.decodeJwtToken(req.token);
        if (ownerId < 0){
            var resp = new UpdateResponse();
            resp.setSuccess(false);
            resp.setTokenError(true);
            return resp;
        }

        if (receiver.update(ownerId, req.getId(), req.getLabWork())) {
            rep.setSuccess(true);
            rep.setMessage("Элемент успешно обновлён");
        } else{
            rep.setSuccess(false);
            rep.setMessage("Не удалось обновить элемент. Проверьте введённый id");
        }

        System.out.println("[DEBUG] Запрос на обновление элемента в коллекции");

        return rep;
    }

}
