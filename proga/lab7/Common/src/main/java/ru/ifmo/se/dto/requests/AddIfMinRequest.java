package ru.ifmo.se.dto.requests;

import ru.ifmo.se.entity.LabWork;

public class AddIfMinRequest extends AddRequest{
    public AddIfMinRequest(LabWork labWork) {
        super("add_if_min", labWork);
    }
}
