package ru.itmo.tpo.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты для класса Zaphod")
class ZaphodTest {

    private Zaphod zaphod;

    @BeforeEach
    void setUp() {
        zaphod = new Zaphod();
    }

    @Test
    @DisplayName("Начальное состояние Зафода")
    void testInitialState() {
        assertEquals("Zaphod", zaphod.getName());
        assertNotNull(zaphod.getLeftHead());
        assertNotNull(zaphod.getRightHead());
        assertEquals("left", zaphod.getLeftHead().getName());
        assertEquals("right", zaphod.getRightHead().getName());
        assertFalse(zaphod.isLounging());
        assertFalse(zaphod.hasFeetOnControlPanel());
    }

    @Test
    @DisplayName("Зафод разваливается в кресле")
    void testLoungeInChair() {
        zaphod.loungeInChair();
        assertTrue(zaphod.isLounging());
    }

    @Test
    @DisplayName("Зафод кладет ноги на пульт управления")
    void testPutFeetOnControlPanel() {
        zaphod.putFeetOnControlPanel();
        assertTrue(zaphod.hasFeetOnControlPanel());
    }

    @Test
    @DisplayName("Зафод ковыряет в зубах правой головы левой рукой")
    void testPickTeethWithLeftHand() {
        zaphod.pickTeethWithLeftHand();
        assertEquals(HeadActivity.PICKING_TEETH, zaphod.getRightHead().getActivity());
    }

    @Test
    @DisplayName("Левая голова Зафода улыбается")
    void testSmileWithLeftHead() {
        zaphod.smileWithLeftHead();
        assertEquals(HeadActivity.SMILING, zaphod.getLeftHead().getActivity());
    }

    @Test
    @DisplayName("Зафод встает")
    void testStandUp() {
        zaphod.loungeInChair();
        zaphod.putFeetOnControlPanel();
        zaphod.pickTeethWithLeftHand();
        zaphod.smileWithLeftHead();
        
        zaphod.standUp();
        
        assertFalse(zaphod.isLounging());
        assertFalse(zaphod.hasFeetOnControlPanel());
        assertEquals(HeadActivity.IDLE, zaphod.getLeftHead().getActivity());
        assertEquals(HeadActivity.IDLE, zaphod.getRightHead().getActivity());
    }
}
