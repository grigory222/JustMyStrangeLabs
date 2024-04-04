package ru.ifmo.se.workers;

import ru.ifmo.se.collection.Receiver;
import ru.ifmo.se.dto.replies.AddIfMaxReply;
import ru.ifmo.se.dto.replies.AddReply;
import ru.ifmo.se.dto.replies.Reply;
import ru.ifmo.se.dto.requests.AddIfMaxRequest;
import ru.ifmo.se.dto.requests.AddRequest;
import ru.ifmo.se.dto.requests.Request;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class AddIfMaxWorker extends Worker{
    public AddIfMaxWorker(Receiver receiver) {
        super(receiver);
    }

    public Request deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bis);
        return (AddIfMaxRequest) ois.readObject();
    }

    public Reply process(Request request){
        AddIfMaxRequest req = (AddIfMaxRequest) request;
        AddIfMaxReply rep = new AddIfMaxReply();
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
