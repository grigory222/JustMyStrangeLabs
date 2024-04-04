package ru.ifmo.se.dto.requests;


import ru.ifmo.se.entity.LabWork;

public class UpdateRequest extends Request{
    int id;
    LabWork labWork;

    public UpdateRequest(int id, LabWork labWork) {
        super("update");
        this.id = id;
        this.labWork = labWork;
    }
}
