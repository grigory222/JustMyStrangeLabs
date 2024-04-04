package ru.ifmo.se.dto.requests;

import lombok.Getter;
import lombok.Setter;

public class RemoveByIdRequest extends Request{
    private int id;
    public RemoveByIdRequest(int id){
        super("remove_by_id");
        this.id = id;
    }
}
