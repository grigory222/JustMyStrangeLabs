package ru.ifmo.se.receiver;

import ru.ifmo.se.dto.AddRequest;
import ru.ifmo.se.dto.Reply;
import ru.ifmo.se.dto.Request;
import ru.ifmo.se.entity.LabWork;

import java.util.LinkedHashSet;

public class Receiver{
    public Receiver(){

    }

    public Reply add(LabWork labWork) {
        Request addRequest = new AddRequest(labWork);

    }
}
