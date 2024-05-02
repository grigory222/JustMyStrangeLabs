package ru.ifmo.se.dto.requests;

public class ShowRequest extends Request{

    public ShowRequest(String token) {
        super("show", token);
    }
}
