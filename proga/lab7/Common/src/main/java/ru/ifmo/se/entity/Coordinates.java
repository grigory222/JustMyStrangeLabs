package ru.ifmo.se.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Coordinates implements Serializable {

    private int x;
    private double y; //Максимальное значение поля: 48
    public Coordinates(int x, double y){
        this.x = x;
        this.y = y;
    }
}
