package ru.ifmo.se.workers;

import ru.ifmo.se.collection.Receiver;
import ru.ifmo.se.dto.replies.AddReply;
import ru.ifmo.se.dto.replies.Reply;
import ru.ifmo.se.dto.replies.UpdateReply;
import ru.ifmo.se.dto.requests.AddRequest;
import ru.ifmo.se.dto.requests.Request;
import ru.ifmo.se.dto.requests.UpdateRequest;

public class UpdateWorker extends Worker {
    public UpdateWorker(Receiver receiver) {
        super(receiver);
    }

    public Reply process(Request request) {
        UpdateRequest req = (UpdateRequest) request;
        UpdateReply rep = new UpdateReply();
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
