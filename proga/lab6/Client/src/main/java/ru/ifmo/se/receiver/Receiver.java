package ru.ifmo.se.receiver;

import ru.ifmo.se.dto.AddReply;
import ru.ifmo.se.dto.AddRequest;
import ru.ifmo.se.dto.Reply;
import ru.ifmo.se.dto.Request;
import ru.ifmo.se.entity.LabWork;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.LinkedHashSet;
import java.util.Objects;

import static ru.ifmo.se.network.Network.receive;
import static ru.ifmo.se.network.Network.send;

public class Receiver{
    Socket socket;
    public Receiver(Socket socket){
        this.socket = socket;
    }

    public AddReply add(LabWork labWork) {
        try {
            AddRequest addRequest = new AddRequest(labWork);
            var bos = new ByteArrayOutputStream();
            var oos = new ObjectOutputStream(bos);
            oos.writeObject(addRequest);
            if (!send(socket, bos.toByteArray()))
                return null;
        } catch (IOException e) {
            return null;
        }

        try {
            var bis = new ByteArrayInputStream(Objects.requireNonNull(receive(socket)));
            var ois = new ObjectInputStream(bis);
            return (AddReply) ois.readObject();
        } catch (IOException | NullPointerException | ClassNotFoundException e) {
            return null;
        }
    }

}
