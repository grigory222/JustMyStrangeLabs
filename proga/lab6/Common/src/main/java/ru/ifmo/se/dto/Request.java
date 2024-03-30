package ru.ifmo.se.dto;

import java.io.Serializable;

public class Request implements Serializable {
    private final String name;
    protected Request(String name){
        this.name = name;
    }
}
