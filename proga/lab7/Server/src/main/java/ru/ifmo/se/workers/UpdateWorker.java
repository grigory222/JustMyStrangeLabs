package ru.ifmo.se.workers;

import ru.ifmo.se.collection.Receiver;
import ru.ifmo.se.dto.responses.Response;
import ru.ifmo.se.dto.responses.UpdateResponse;
import ru.ifmo.se.dto.requests.Request;
import ru.ifmo.se.dto.requests.UpdateRequest;

public class UpdateWorker extends Worker {
    public UpdateWorker(Receiver receiver) {
        super(receiver);
    }

    public Response process(Request request) {
        UpdateRequest req = (UpdateRequest) request;
        UpdateResponse rep = new UpdateResponse();
        if (receiver.update(req.getId(), req.getLabWork())) {
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
