package ru.ifmo.se.workers;

import ru.ifmo.se.collection.Receiver;
import ru.ifmo.se.dto.replies.AddReply;
import ru.ifmo.se.dto.requests.AddRequest;
import ru.ifmo.se.dto.replies.Reply;
import ru.ifmo.se.dto.requests.Request;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class AddWorker extends Worker{
    public AddWorker(Receiver receiver) {
        super(receiver);
    }

    public Reply process(Request request){
        AddRequest req = (AddRequest) request;
        AddReply rep = new AddReply();
        receiver.add(req.getLabWork());
        rep.setSuccess(true);
        rep.setMessage("Элемент успешно добавлен в коллекцию");

        System.out.println("[DEBUG] Запрос на добавление элемента в коллекцию");

        return rep;
    }

}
