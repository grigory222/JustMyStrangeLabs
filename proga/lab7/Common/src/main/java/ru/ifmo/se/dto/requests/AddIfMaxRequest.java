package ru.ifmo.se.dto.requests;

import ru.ifmo.se.entity.LabWork;

public class AddIfMaxRequest extends AddRequest{
    public AddIfMaxRequest(LabWork labWork, String token) {
        super("add_if_max", labWork, token);
    }
}
