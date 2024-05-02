package ru.ifmo.se.dto.requests;

import lombok.Getter;
import ru.ifmo.se.entity.LabWork;

@Getter
public class AddRequest extends Request {
    public final LabWork labWork;
    public AddRequest(LabWork labWork, String token){
        super("add", token);
        this.labWork = labWork;
    }

    protected AddRequest(String name, LabWork labWork, String token){
        super(name, token);
        this.labWork = labWork;
    }

}
