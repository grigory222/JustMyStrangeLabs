package ru.ifmo.se.entity;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import com.opencsv.bean.CsvRecurse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ifmo.se.csv.CsvHandler;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LabWork implements Comparable<LabWork>{
    @CsvBindByName
    private Integer id;

    @CsvBindByName
    private String name;

    //@CsvCustomBindByName(column = "coordinates", converter = CsvHandler.CoordinatesConverter.class)
    @CsvRecurse
    private Coordinates coordinates;

    @CsvCustomBindByName(column = "creationDate", converter = CsvHandler.DateConverter.class)
    private java.time.LocalDate creationDate;

    @CsvBindByName
    private Integer minimalPoint;

    @CsvBindByName
    private long tunedInWorks;

    @CsvCustomBindByName(column = "difficulty", converter = CsvHandler.DifficultyConverter.class)
    private Difficulty difficulty;

    @CsvRecurse
    private Person author;

    @Override
    public int compareTo(LabWork o) {
        return this.id - o.id;
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