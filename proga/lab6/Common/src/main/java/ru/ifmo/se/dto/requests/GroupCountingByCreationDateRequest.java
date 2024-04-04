package ru.ifmo.se.dto.requests;

import lombok.Getter;
import lombok.Setter;

public class GroupCountingByCreationDateRequest extends Request{
    public GroupCountingByCreationDateRequest(){
        super("group_counting_by_creation_date");
    }
}
