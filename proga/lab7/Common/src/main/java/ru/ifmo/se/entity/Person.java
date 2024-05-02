package ru.ifmo.se.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
public class Person implements Comparable<Person>, Serializable {
    private static final Person EMPTY = new Person();

    private String authorName; //Поле не может быть null, Строка не может быть пустой
    private Date birthday; //Поле не может быть null
    private int height; //Значение поля должно быть больше 0
    private Double weight; //Поле не может быть null, Значение поля должно быть больше 0
    private Color hairColor; //Поле не может быть null

    public Person(String authorName, Date birthday, int height, Double weight, Color hairColor) {
        this.authorName = authorName;
        this.birthday = birthday;
        this.height = height;
        this.weight = weight;
        this.hairColor = hairColor;
    }

    public boolean isEmpty() {
        return this.equals(EMPTY);
    }

    @Override
    public int compareTo(Person o) {
        return this.authorName.compareTo(o.getAuthorName());
    }

    @Override
    public String toString() {
        return  authorName +
                "\n\tbirthday: " + birthday +
                "\n\theight: " + height +
                "\n\tweight: " + weight +
                "\n\thairColor: " + hairColor ;
    }
}
