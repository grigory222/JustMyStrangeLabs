package ru.itmo.tpo.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты для класса ControlPanel")
class ControlPanelTest {

    private ControlPanel controlPanel;

    @BeforeEach
    void setUp() {
        controlPanel = new ControlPanel();
    }

    @Test
    @DisplayName("Начальное состояние пульта управления")
    void testInitialState() {
        assertFalse(controlPanel.hasFeetOnIt());
    }

    @Test
    @DisplayName("Положить ноги на пульт")
    void testPutFeetOn() {
        controlPanel.putFeetOn();
        assertTrue(controlPanel.hasFeetOnIt());
    }

    @Test
    @DisplayName("Можно положить ноги несколько раз")
    void testPutFeetOnMultipleTimes() {
        controlPanel.putFeetOn();
        controlPanel.putFeetOn();
        assertTrue(controlPanel.hasFeetOnIt());
    }
}
