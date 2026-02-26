package ru.itmo.tpo.domain;

public class Chair {
    private boolean isOccupied;
    private Person occupant;

    public Chair() {
        this.isOccupied = false;
        this.occupant = null;
    }

    public void occupy(Person person) {
        if (isOccupied) {
            throw new IllegalStateException("Chair is already occupied by " + this.occupant.getName());
        }
        this.isOccupied = true;
        this.occupant = person;
    }

    public void vacate() {
        if (!isOccupied) {
            throw new IllegalStateException("Chair is not occupied");
        }
        this.isOccupied = false;
        this.occupant = null;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public Person getOccupant() {
        return occupant;
    }
}
