package ru.ifmo.se.workers;

import ru.ifmo.se.collection.Receiver;
import ru.ifmo.se.dto.replies.RemoveByIdReply;
import ru.ifmo.se.dto.replies.Reply;
import ru.ifmo.se.dto.replies.UpdateReply;
import ru.ifmo.se.dto.requests.RemoveByIdRequest;
import ru.ifmo.se.dto.requests.Request;
import ru.ifmo.se.dto.requests.UpdateRequest;

public class RemoveByIdWorker extends Worker {
    public RemoveByIdWorker(Receiver receiver) {
        super(receiver);
    }

    public Reply process(Request request) {
        RemoveByIdRequest req = (RemoveByIdRequest) request;
        RemoveByIdReply rep = new RemoveByIdReply();

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
