package ru.ifmo.se.entity;

import java.util.Date;

public class Person {
    private String name; //Поле не может быть null, Строка не может быть пустой
    private java.util.Date birthday; //Поле не может быть null
    private int height; //Значение поля должно быть больше 0
    private Double weight; //Поле не может быть null, Значение поля должно быть больше 0
    private Color hairColor; //Поле не может быть null

    public Person(String name, Date birthday, int height, Double weight, Color hairColor) {
        this.name = name;
        this.birthday = birthday;
        this.height = height;
        this.weight = weight;
        this.hairColor = hairColor;
    }
}
