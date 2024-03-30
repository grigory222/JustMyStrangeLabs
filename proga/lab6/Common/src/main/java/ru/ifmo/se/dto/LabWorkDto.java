package ru.ifmo.se.dto;


import ru.ifmo.se.entity.Coordinates;
import ru.ifmo.se.entity.Difficulty;
import ru.ifmo.se.entity.Person;

import java.io.Serializable;
import java.time.LocalDate;

public class LabWorkDto implements Serializable {
    private Integer id;
    private String name;

    private Coordinates coordinates;

    private LocalDate creationDate;
    private Integer minimalPoint;
    private long tunedInWorks;
    private Difficulty difficulty;
    private Person author;

}
