package ru.ifmo.se.entity;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Coordinates {

    @CsvBindByName
    private int x;
    @CsvBindByName
    private double y; //Максимальное значение поля: 48
    public Coordinates(int x, double y){
        this.x = x;
        this.y = y;
    }
}
