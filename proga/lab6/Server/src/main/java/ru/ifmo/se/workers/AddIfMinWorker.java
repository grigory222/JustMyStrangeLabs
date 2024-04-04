package ru.ifmo.se.workers;

import ru.ifmo.se.collection.Receiver;
import ru.ifmo.se.dto.replies.AddIfMinReply;
import ru.ifmo.se.dto.replies.AddReply;
import ru.ifmo.se.dto.replies.Reply;
import ru.ifmo.se.dto.requests.AddIfMaxRequest;
import ru.ifmo.se.dto.requests.AddIfMinRequest;
import ru.ifmo.se.dto.requests.AddRequest;
import ru.ifmo.se.dto.requests.Request;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class AddIfMinWorker extends Worker{
    public AddIfMinWorker(Receiver receiver) {
        super(receiver);
    }

    public Request deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bis);
        return (AddIfMinRequest) ois.readObject();
    }

    public Reply process(Request request){
        AddIfMinRequest req = (AddIfMinRequest) request;
        AddIfMinReply rep = new AddIfMinReply();
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
