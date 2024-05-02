package ru.ifmo.se.workers;

import ru.ifmo.se.collection.Receiver;
import ru.ifmo.se.dto.responses.Response;
import ru.ifmo.se.dto.requests.Request;
import ru.ifmo.se.network.JwtManager;

import java.io.*;
import java.nio.ByteBuffer;

public abstract class Worker {
    protected final Receiver receiver;
    protected final JwtManager jwtManager;
    protected Worker(Receiver receiver, JwtManager jwtManager) {
        this.receiver = receiver;
        this.jwtManager = jwtManager;
    }

    public static ByteBuffer serialize(Response response) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(response);
        objectOutputStream.flush();
        byte[] serializedData = byteArrayOutputStream.toByteArray();
        objectOutputStream.close();
        return ByteBuffer.wrap(serializedData);

    }

    public static Request deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bis);
        return (Request) ois.readObject();
    }

    public abstract Response process(Request request);
}
