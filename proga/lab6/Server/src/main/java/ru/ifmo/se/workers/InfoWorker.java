package ru.ifmo.se.workers;

import ru.ifmo.se.collection.Receiver;
import ru.ifmo.se.dto.replies.Reply;
import ru.ifmo.se.dto.replies.ShowReply;
import ru.ifmo.se.dto.requests.InfoRequest;
import ru.ifmo.se.dto.requests.Request;
import ru.ifmo.se.dto.requests.ShowRequest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class InfoWorker extends Worker{
    public InfoWorker(Receiver receiver) {
        super(receiver);
    }

    public Request deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bis);
        return (InfoRequest) ois.readObject();
    }

    public Reply process(Request request){
        ShowRequest req = (ShowRequest) request;
        ShowReply rep = new ShowReply();

        rep.setSuccess(true);
        rep.setResult(receiver.show());

        System.out.println("[DEBUG] Запрос на показ коллекции");

        return rep;
    }

}
