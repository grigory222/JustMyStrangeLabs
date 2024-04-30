package ru.ifmo.se.workers;

import ru.ifmo.se.collection.Receiver;
import ru.ifmo.se.dto.responses.AddIfMinResponse;
import ru.ifmo.se.dto.responses.Response;
import ru.ifmo.se.dto.requests.AddIfMinRequest;
import ru.ifmo.se.dto.requests.Request;

public class AddIfMinWorker extends Worker{
    public AddIfMinWorker(Receiver receiver) {
        super(receiver);
    }

    public Response process(Request request){
        AddIfMinRequest req = (AddIfMinRequest) request;
        AddIfMinResponse rep = new AddIfMinResponse();
        if (receiver.addIfMin(req.getLabWork())) {
            rep.setMessage("Элемент упешно добавлен в коллекцию");
        } else{
            rep.setMessage("Элемент не является минимальным. Не добавлен в коллекцию");
        }
        rep.setSuccess(true);

        System.out.println("[DEBUG] Запрос на добавление максимального элемента в коллекцию");

        return rep;
    }

}
