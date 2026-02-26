package ru.itmo.tpo.domain;

import java.util.ArrayList;
import java.util.List;

public class Room {
    private final Chair chair;
    private final ControlPanel controlPanel;
    private final List<Person> peopleInRoom;

    public Room() {
        this.chair = new Chair();
        this.controlPanel = new ControlPanel();
        this.peopleInRoom = new ArrayList<>();
    }

    public void addPerson(Person person) {
        if (peopleInRoom.contains(person)) {
            throw new IllegalStateException(person.getName() + " is already in the room");
        }
        peopleInRoom.add(person);
    }

    public void removePerson(Person person) {
        if (!peopleInRoom.contains(person)) {
            throw new IllegalStateException(person.getName() + " is not in the room");
        }
        peopleInRoom.remove(person);
    }

    public boolean isPersonInRoom(Person person) {
        return peopleInRoom.contains(person);
    }

    public Chair getChair() {
        return chair;
    }

    public ControlPanel getControlPanel() {
        return controlPanel;
    }

    public int getPeopleCount() {
        return peopleInRoom.size();
    }

    public List<Person> getPeopleInRoom() {
        return new ArrayList<>(peopleInRoom);
    }
}
