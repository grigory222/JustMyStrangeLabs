package ru.ifmo.se.dto.requests;

import java.io.Serializable;

public class Request implements Serializable {
    public final String name;

    protected Request(String name){
        this.name = name;
    }
}
