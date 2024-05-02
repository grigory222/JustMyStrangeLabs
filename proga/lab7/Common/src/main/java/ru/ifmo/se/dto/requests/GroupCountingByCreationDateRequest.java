package ru.ifmo.se.dto.requests;

public class GroupCountingByCreationDateRequest extends Request{
    public GroupCountingByCreationDateRequest(String token){
        super("group_counting_by_creation_date", token);
    }
}
