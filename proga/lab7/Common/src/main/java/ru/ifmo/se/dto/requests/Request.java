package ru.ifmo.se.dto.requests;

import java.io.Serializable;

public class Request implements Serializable {
    public final String name; // name of request
    //public final String login;
    //public final String password;
    protected Request(String name){
        this.name = name;
    }
}
