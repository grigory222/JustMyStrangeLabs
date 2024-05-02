package ru.ifmo.se.dto.requests;

public class InfoRequest extends Request{
    public InfoRequest(String token){
        super("info", token);
    }
}
