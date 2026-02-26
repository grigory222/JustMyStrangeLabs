package ru.itmo.tpo.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты для класса Head")
class HeadTest {

    private Head head;

    @BeforeEach
    void setUp() {
        head = new Head("test");
    }

    @Test
    @DisplayName("Начальное состояние головы")
    void testInitialState() {
        assertEquals("test", head.getName());
        assertEquals(HeadActivity.IDLE, head.getActivity());
    }

    @Test
    @DisplayName("Голова начинает улыбаться")
    void testStartSmiling() {
        head.startSmiling();
        assertEquals(HeadActivity.SMILING, head.getActivity());
    }

    @Test
    @DisplayName("Голова ковыряет в зубах")
    void testPickTeeth() {
        head.pickTeeth();
        assertEquals(HeadActivity.PICKING_TEETH, head.getActivity());
    }

    @Test
    @DisplayName("Голова разговаривает")
    void testTalk() {
        head.talk();
        assertEquals(HeadActivity.TALKING, head.getActivity());
    }

    @Test
    @DisplayName("Голова прекращает деятельность")
    void testStopActivity() {
        head.pickTeeth();
        head.stopActivity();
        
        assertEquals(HeadActivity.IDLE, head.getActivity());
    }

    @Test
    @DisplayName("Активность головы можно переопределить")
    void testActivityOverride() {
        head.pickTeeth();
        assertEquals(HeadActivity.PICKING_TEETH, head.getActivity());
        
        head.startSmiling();
        assertEquals(HeadActivity.SMILING, head.getActivity());
    }
}
