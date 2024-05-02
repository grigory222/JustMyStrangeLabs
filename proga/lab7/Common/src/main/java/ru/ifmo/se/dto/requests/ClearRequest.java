package ru.ifmo.se.dto.requests;

public class ClearRequest extends Request{
    public ClearRequest(String token){
        super("clear", token);
    }
}
