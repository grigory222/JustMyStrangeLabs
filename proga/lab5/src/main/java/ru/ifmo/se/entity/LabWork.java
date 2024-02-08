package ru.ifmo.se.entity;

import java.time.LocalDate;

public class LabWork {
    private Integer id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private java.time.LocalDate creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Integer minimalPoint; //Поле не может быть null, Значение поля должно быть больше 0
    private long tunedInWorks;
    private Difficulty difficulty; //Поле не может быть null
    private Person author; //Поле может быть null

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public Integer getMinimalPoint() {
        return minimalPoint;
    }

    public void setMinimalPoint(Integer minimalPoint) {
        this.minimalPoint = minimalPoint;
    }

    public long getTunedInWorks() {
        return tunedInWorks;
    }

    public void setTunedInWorks(long tunedInWorks) {
        this.tunedInWorks = tunedInWorks;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public Person getAuthor() {
        return author;
    }

    public void setAuthor(Person author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "LabWork{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", creationDate=" + creationDate +
                ", minimalPoint=" + minimalPoint +
                ", tunedInWorks=" + tunedInWorks +
                ", difficulty=" + difficulty +
                ", author=" + author +
                '}';
    }
}
