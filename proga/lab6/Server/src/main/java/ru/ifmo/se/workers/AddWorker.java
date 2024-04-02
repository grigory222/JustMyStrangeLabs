package ru.ifmo.se.workers;

import ru.ifmo.se.dto.AddReply;
import ru.ifmo.se.dto.AddRequest;
import ru.ifmo.se.dto.Reply;
import ru.ifmo.se.dto.Request;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class AddWorker extends Worker{
    public Request deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bis);
        return (AddRequest) ois.readObject();
    }

    public Reply process(Request request){
        AddRequest req = (AddRequest) request;
        AddReply rep = new AddReply();
        System.out.println("[DEBUG] Запрос на добавление элемента в коллекцию!");

        return null;
    }

}
