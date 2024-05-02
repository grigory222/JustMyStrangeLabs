package ru.ifmo.se.workers;

import ru.ifmo.se.collection.Receiver;
import ru.ifmo.se.dto.responses.RegisterResponse;
import ru.ifmo.se.dto.responses.RemoveByIdResponse;
import ru.ifmo.se.dto.responses.Response;
import ru.ifmo.se.dto.requests.RemoveByIdRequest;
import ru.ifmo.se.dto.requests.Request;
import ru.ifmo.se.network.JwtManager;

public class RemoveByIdWorker extends Worker {
    public RemoveByIdWorker(Receiver receiver, JwtManager jwtManager) {
        super(receiver, jwtManager);
    }

    public Response process(Request request) {
        RemoveByIdRequest req = (RemoveByIdRequest) request;
        RemoveByIdResponse rep = new RemoveByIdResponse();
        long id = jwtManager.decodeJwtToken(req.token);
        if (id < 0){
            var resp = new RemoveByIdResponse();
            resp.setSuccess(false);
            resp.setTokenError(true);
            return resp;
        }

        if (receiver.removeById(req.getId())) {
            rep.setSuccess(true);
            rep.setMessage("Элемент успешно удалён");
        } else{
            rep.setSuccess(false);
            rep.setMessage("Не удалось удалить элемент. Проверьте введённый id");
        }

        System.out.println("[DEBUG] Запрос на удаление элемента из коллекции");

        return rep;
    }

}
