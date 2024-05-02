package ru.ifmo.se.dto.requests;

import lombok.Getter;

@Getter
public class RemoveByIdRequest extends Request{
    private final int id;
    public RemoveByIdRequest(int id, String token){
        super("remove_by_id", token);
        this.id = id;
    }
}
