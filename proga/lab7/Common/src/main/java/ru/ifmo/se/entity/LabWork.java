package ru.ifmo.se.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LabWork implements Comparable<LabWork>, Serializable {
    private Integer id;
    private int ownerId;
    private String name;
    private Coordinates coordinates;
    private java.time.LocalDate creationDate;
    private Integer minimalPoint;
    private long tunedInWorks;
    private Difficulty difficulty;
    private Person author;

    @Override
    public int compareTo(LabWork o) {
        return this.name.compareTo(o.name);
    }

    @Override
    public String toString() {
        return  "id: " + id +
                "\nlab name: " + name +
                "\ncoordinates: " + coordinates.getX() + " " + coordinates.getY() +
                "\ncreationDate: " + creationDate +
                "\nminimalPoint: " + minimalPoint +
                "\ntunedInWorks: " + tunedInWorks +
                "\ndifficulty: " + difficulty.name() +
                "\nauthor: " + author;

    }
}