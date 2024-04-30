package ru.ifmo.se.workers;

import ru.ifmo.se.collection.Receiver;
import ru.ifmo.se.dto.responses.AddIfMaxResponse;
import ru.ifmo.se.dto.responses.Response;
import ru.ifmo.se.dto.requests.AddIfMaxRequest;
import ru.ifmo.se.dto.requests.Request;

public class AddIfMaxWorker extends Worker{
    public AddIfMaxWorker(Receiver receiver) {
        super(receiver);
    }


    public Response process(Request request){
        AddIfMaxRequest req = (AddIfMaxRequest) request;
        AddIfMaxResponse rep = new AddIfMaxResponse();
        if (receiver.addIfMax(req.getLabWork())) {
            rep.setMessage("Элемент упешно добавлен в коллекцию");
        } else{
            rep.setMessage("Элемент не является максимальным. Не добавлен в коллекцию");
        }
        rep.setSuccess(true);

        System.out.println("[DEBUG] Запрос на добавление максимального элемента в коллекцию");

        return rep;
    }

}
