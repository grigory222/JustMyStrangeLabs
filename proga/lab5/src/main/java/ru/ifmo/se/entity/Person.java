package ru.ifmo.se.entity;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ifmo.se.csv.CsvHandler;

import java.util.Date;

@Data
@NoArgsConstructor
public class Person {
    @CsvBindByName(column = "authorName")
    private String name; //Поле не может быть null, Строка не может быть пустой
    @CsvCustomBindByName(column = "birthday", converter = CsvHandler.DateConverterSecond.class)
    private java.util.Date birthday; //Поле не может быть null
    @CsvBindByName
    private int height; //Значение поля должно быть больше 0
    @CsvBindByName
    private Double weight; //Поле не может быть null, Значение поля должно быть больше 0
    @CsvBindByName
    private Color hairColor; //Поле не может быть null

    public Person(String name, Date birthday, int height, Double weight, Color hairColor) {
        this.name = name;
        this.birthday = birthday;
        this.height = height;
        this.weight = weight;
        this.hairColor = hairColor;
    }

}
