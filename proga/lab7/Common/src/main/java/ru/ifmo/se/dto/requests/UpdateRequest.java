package ru.ifmo.se.dto.requests;


import lombok.Getter;
import ru.ifmo.se.entity.LabWork;

@Getter
public class UpdateRequest extends Request{
    int id;
    LabWork labWork;

    public UpdateRequest(int id, LabWork labWork) {
        super("update");
        this.id = id;
        this.labWork = labWork;
    }
}
