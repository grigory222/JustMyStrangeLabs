package ru.itmo.tpo.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты для класса Story - полный сценарий встречи")
class StoryTest {

    private Story story;

    @BeforeEach
    void setUp() {
        story = new Story();
    }

    @Test
    @DisplayName("Начальное состояние истории")
    void testInitialState() {
        assertNotNull(story.getArthur());
        assertNotNull(story.getZaphod());
        assertNotNull(story.getRoom());
    }

    @Test
    @DisplayName("Полный сценарий встречи")
    void testPlayFullScene() {
        story.playFullScene();
        
        Arthur arthur = story.getArthur();
        assertEquals(EmotionalState.SHOCKED, arthur.getEmotionalState());
        assertEquals(PhysicalState.JAW_DROPPED, arthur.getPhysicalState());
        assertTrue(arthur.isInRoom());
        assertEquals(3, arthur.getUnbelievableThingsCount());
        
        Zaphod zaphod = story.getZaphod();
        assertTrue(zaphod.isLounging());
        assertTrue(zaphod.hasFeetOnControlPanel());
        assertEquals(HeadActivity.PICKING_TEETH, zaphod.getRightHead().getActivity());
        assertEquals(HeadActivity.SMILING, zaphod.getLeftHead().getActivity());
        
        Room room = story.getRoom();
        assertEquals(2, room.getPeopleCount());
        assertTrue(room.isPersonInRoom(arthur));
        assertTrue(room.isPersonInRoom(zaphod));
        assertTrue(room.getChair().isOccupied());
        assertEquals(zaphod, room.getChair().getOccupant());
        assertTrue(room.getControlPanel().hasFeetOnIt());
    }
}
