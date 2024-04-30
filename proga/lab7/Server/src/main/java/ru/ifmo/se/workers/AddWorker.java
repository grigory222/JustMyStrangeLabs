package ru.ifmo.se.workers;

import ru.ifmo.se.collection.Receiver;
import ru.ifmo.se.dto.responses.AddResponse;
import ru.ifmo.se.dto.requests.AddRequest;
import ru.ifmo.se.dto.responses.Response;
import ru.ifmo.se.dto.requests.Request;

public class AddWorker extends Worker{
    public AddWorker(Receiver receiver) {
        super(receiver);
    }

    public Response process(Request request){
        AddRequest req = (AddRequest) request;
        AddResponse rep = new AddResponse();
        receiver.add(req.getLabWork());
        rep.setSuccess(true);
        rep.setMessage("Элемент успешно добавлен в коллекцию");

        System.out.println("[DEBUG] Запрос на добавление элемента в коллекцию");

        return rep;
    }

}
