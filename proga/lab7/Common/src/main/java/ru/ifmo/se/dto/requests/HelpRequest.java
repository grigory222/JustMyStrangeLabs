package ru.ifmo.se.dto.requests;

public class HelpRequest extends Request{
    public HelpRequest(String token){
        super("help", token);
    }
}
