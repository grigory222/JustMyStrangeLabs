package ru.ifmo.se.dto.requests;

public class PrintUniqueDifficultyRequest extends Request{
    public PrintUniqueDifficultyRequest(String token){
        super("print_unique_difficulty", token);
    }
}
