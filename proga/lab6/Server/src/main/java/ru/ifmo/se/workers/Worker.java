package ru.ifmo.se.workers;

import ru.ifmo.se.collection.Receiver;
import ru.ifmo.se.dto.Reply;
import ru.ifmo.se.dto.Request;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;

public abstract class Worker {
    protected final Receiver receiver;

    protected Worker(Receiver receiver) {
        this.receiver = receiver;
    }

    public static ByteBuffer serialize(Reply reply) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(reply);
        objectOutputStream.flush();
        byte[] serializedData = byteArrayOutputStream.toByteArray();
        objectOutputStream.close();
        return ByteBuffer.wrap(serializedData);

    }

    public abstract Request deserialize(byte[] bytes) throws IOException, ClassNotFoundException;
    public abstract Reply process(Request request);
}
