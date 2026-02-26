package ru.itmo.tpo.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты для класса Room")
class RoomTest {

    private Room room;
    private Person arthur;
    private Person zaphod;

    @BeforeEach
    void setUp() {
        room = new Room();
        arthur = new Arthur();
        zaphod = new Zaphod();
    }

    @Test
    @DisplayName("Начальное состояние комнаты")
    void testInitialState() {
        assertNotNull(room.getChair());
        assertNotNull(room.getControlPanel());
        assertEquals(0, room.getPeopleCount());
        assertTrue(room.getPeopleInRoom().isEmpty());
    }

    @Test
    @DisplayName("Добавить человека в комнату")
    void testAddPerson() {
        room.addPerson(arthur);
        
        assertEquals(1, room.getPeopleCount());
        assertTrue(room.isPersonInRoom(arthur));
    }

    @Test
    @DisplayName("Добавить несколько человек в комнату")
    void testAddMultiplePeople() {
        room.addPerson(arthur);
        room.addPerson(zaphod);
        
        assertEquals(2, room.getPeopleCount());
        assertTrue(room.isPersonInRoom(arthur));
        assertTrue(room.isPersonInRoom(zaphod));
    }

    @Test
    @DisplayName("Нельзя добавить одного человека дважды")
    void testAddPersonTwice() {
        room.addPerson(arthur);
        
        IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> room.addPerson(arthur)
        );
        assertTrue(exception.getMessage().contains("Arthur"));
    }

    @Test
    @DisplayName("Удалить человека из комнаты")
    void testRemovePerson() {
        room.addPerson(arthur);
        room.removePerson(arthur);
        
        assertEquals(0, room.getPeopleCount());
        assertFalse(room.isPersonInRoom(arthur));
    }

    @Test
    @DisplayName("Нельзя удалить человека, которого нет в комнате")
    void testRemovePersonNotInRoom() {
        IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> room.removePerson(arthur)
        );
        assertTrue(exception.getMessage().contains("Arthur"));
    }

    @Test
    @DisplayName("Проверка наличия человека в комнате")
    void testIsPersonInRoom() {
        assertFalse(room.isPersonInRoom(arthur));
        
        room.addPerson(arthur);
        assertTrue(room.isPersonInRoom(arthur));
        assertFalse(room.isPersonInRoom(zaphod));
    }

    @Test
    @DisplayName("Получить список людей в комнате")
    void testGetPeopleInRoom() {
        room.addPerson(arthur);
        room.addPerson(zaphod);
        
        List<Person> people = room.getPeopleInRoom();
        
        assertEquals(2, people.size());
        assertTrue(people.contains(arthur));
        assertTrue(people.contains(zaphod));
    }

    @Test
    @DisplayName("Список людей - это копия, изменения не влияют на оригинал")
    void testGetPeopleInRoomReturnsCopy() {
        room.addPerson(arthur);
        
        List<Person> people = room.getPeopleInRoom();
        people.add(zaphod);
        
        assertEquals(1, room.getPeopleCount());
        assertFalse(room.isPersonInRoom(zaphod));
    }
}
