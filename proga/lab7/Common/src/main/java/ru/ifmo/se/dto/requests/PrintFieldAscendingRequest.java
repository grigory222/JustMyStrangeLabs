package ru.ifmo.se.dto.requests;

public class PrintFieldAscendingRequest extends Request{
    public PrintFieldAscendingRequest(String token){
        super("print_field_ascending", token);
    }
}
