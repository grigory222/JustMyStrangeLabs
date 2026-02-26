package ru.itmo.tpo.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты для класса Chair")
class ChairTest {

    private Chair chair;
    private Person arthur;
    private Person zaphod;

    @BeforeEach
    void setUp() {
        chair = new Chair();
        arthur = new Arthur();
        zaphod = new Zaphod();
    }

    @Test
    @DisplayName("Начальное состояние кресла")
    void testInitialState() {
        assertFalse(chair.isOccupied());
        assertNull(chair.getOccupant());
    }

    @Test
    @DisplayName("Кто-то занимает кресло")
    void testOccupy() {
        chair.occupy(zaphod);
        
        assertTrue(chair.isOccupied());
        assertEquals(zaphod, chair.getOccupant());
    }

    @Test
    @DisplayName("Нельзя занять занятое кресло")
    void testOccupyWhenAlreadyOccupied() {
        chair.occupy(zaphod);
        
        IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> chair.occupy(arthur)
        );
        assertTrue(exception.getMessage().contains("Zaphod"));
    }

    @Test
    @DisplayName("Освобождение кресла")
    void testVacate() {
        chair.occupy(zaphod);
        chair.vacate();
        
        assertFalse(chair.isOccupied());
        assertNull(chair.getOccupant());
    }

    @Test
    @DisplayName("Нельзя освободить незанятое кресло")
    void testVacateWhenNotOccupied() {
        assertThrows(IllegalStateException.class, () -> chair.vacate());
    }
}
