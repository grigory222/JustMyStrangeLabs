package ru.ifmo.se.dto;

import java.io.Serializable;

public class Request implements Serializable {
    public String name;
    public Request(){

    }
    protected Request(String name){
        this.name = name;
    }
}
