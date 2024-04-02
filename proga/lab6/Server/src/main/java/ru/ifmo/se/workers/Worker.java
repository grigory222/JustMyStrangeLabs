package ru.ifmo.se.workers;

import ru.ifmo.se.dto.Reply;
import ru.ifmo.se.dto.Request;

import java.io.IOException;

public abstract class Worker {
    public abstract Request deserialize(byte[] bytes) throws IOException, ClassNotFoundException;
    public abstract Reply process(Request request);
}
