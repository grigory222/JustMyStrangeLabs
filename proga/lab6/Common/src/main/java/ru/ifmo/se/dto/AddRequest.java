package ru.ifmo.se.dto;

import lombok.Data;
import ru.ifmo.se.entity.LabWork;

public class AddRequest extends Request{
    public final LabWork labWork;
    public AddRequest(LabWork labWork){
        super("add");
        this.labWork = labWork;
    }

    public LabWork getLabWork() {
        return labWork;
    }
}
