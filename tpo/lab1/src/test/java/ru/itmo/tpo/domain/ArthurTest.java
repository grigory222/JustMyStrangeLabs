package ru.itmo.tpo.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты для класса Arthur")
class ArthurTest {

    private Arthur arthur;

    @BeforeEach
    void setUp() {
        arthur = new Arthur();
    }

    @Test
    @DisplayName("Начальное состояние Артура")
    void testInitialState() {
        assertEquals("Arthur", arthur.getName());
        assertEquals(EmotionalState.CALM, arthur.getEmotionalState());
        assertEquals(PhysicalState.NORMAL, arthur.getPhysicalState());
        assertEquals(0, arthur.getUnbelievableThingsCount());
        assertFalse(arthur.isInRoom());
    }

    @Test
    @DisplayName("Артур входит в комнату")
    void testEnterRoom() {
        arthur.enterRoom();
        assertTrue(arthur.isInRoom());
    }

    @Test
    @DisplayName("Артур не может войти в комнату дважды")
    void testEnterRoomTwice() {
        arthur.enterRoom();
        assertThrows(IllegalStateException.class, () -> arthur.enterRoom());
    }

    @Test
    @DisplayName("Артур нервничает до входа в комнату")
    void testBecomeNervousBeforeEntering() {
        arthur.becomeNervous();
        assertEquals(EmotionalState.NERVOUS, arthur.getEmotionalState());
    }

    @Test
    @DisplayName("Артур нервничает после входа в комнату - состояние не меняется")
    void testBecomeNervousAfterEntering() {
        arthur.enterRoom();
        arthur.becomeNervous();
        assertEquals(EmotionalState.CALM, arthur.getEmotionalState());
    }

    @Test
    @DisplayName("Артур становится ошеломлен")
    void testBecomeShocked() {
        arthur.becomeShocked();
        assertEquals(EmotionalState.SHOCKED, arthur.getEmotionalState());
    }

    @Test
    @DisplayName("Артур видит невероятные вещи")
    void testSeeUnbelievableThing() {
        arthur.seeUnbelievableThing();
        assertEquals(1, arthur.getUnbelievableThingsCount());
    }

    @Test
    @DisplayName("Артур видит три невероятные вещи")
    void testSeeThreeUnbelievableThings() {
        arthur.seeUnbelievableThing();
        arthur.seeUnbelievableThing();
        arthur.seeUnbelievableThing();
        
        assertEquals(3, arthur.getUnbelievableThingsCount());
    }

    @Test
    @DisplayName("Челюсть Артура отвисает, когда он ошеломлен")
    void testDropJawWhenShocked() {
        arthur.becomeShocked();
        arthur.dropJaw();
        
        assertEquals(PhysicalState.JAW_DROPPED, arthur.getPhysicalState());
    }

    @Test
    @DisplayName("Челюсть не отвисает, если Артур не ошеломлен")
    void testDropJawWhenNotShocked() {
        arthur.dropJaw();
        assertEquals(PhysicalState.NORMAL, arthur.getPhysicalState());
        
        arthur.becomeNervous();
        arthur.dropJaw();
        assertEquals(PhysicalState.NORMAL, arthur.getPhysicalState());
    }

    @Test
    @DisplayName("Сброс состояния Артура")
    void testResetState() {
        arthur.enterRoom();
        arthur.becomeShocked();
        arthur.dropJaw();
        arthur.seeUnbelievableThing();
        
        arthur.resetState();
        
        assertEquals(EmotionalState.CALM, arthur.getEmotionalState());
        assertEquals(PhysicalState.NORMAL, arthur.getPhysicalState());
        assertEquals(0, arthur.getUnbelievableThingsCount());
    }
}
