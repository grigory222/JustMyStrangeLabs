package ru.ifmo.se.dto.requests;

import java.io.Serializable;

public class Request implements Serializable {
    public final String name; // name of request
    public final String token; // jwt token
    protected Request(String name, String token){
        this.name = name;
        this.token = token;
    }
}
