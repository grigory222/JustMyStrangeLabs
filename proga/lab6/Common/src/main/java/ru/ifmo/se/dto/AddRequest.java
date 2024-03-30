package ru.ifmo.se.dto;

import ru.ifmo.se.entity.LabWork;

public class AddRequest extends Request{
    public final LabWork labWork;
    public AddRequest(LabWork labWork){
        super("add");
        this.labWork = labWork;
    }
}
