package ru.itmo.tpo.domain;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.junit.jupiter.api.Assertions;

public class StoryStepDefs {
    private Arthur arthur;
    private Zaphod zaphod;
    private Room room;

    @Given("существует комната с креслом и пультом управления")
    public void roomExists() {
        room = new Room();
        Assertions.assertNotNull(room.getChair());
        Assertions.assertNotNull(room.getControlPanel());
    }

    @Given("двухголовый человек развалился в кресле в комнате")
    public void manSprawledInChairInRoom() {
        zaphod = new Zaphod();
        room.addPerson(zaphod);
        room.getChair().occupy(zaphod);
        zaphod.loungeInChair();
        Assertions.assertTrue(zaphod.isLounging());
        Assertions.assertTrue(room.getChair().isOccupied());
        Assertions.assertEquals(zaphod, room.getChair().getOccupant());
        Assertions.assertTrue(room.isPersonInRoom(zaphod));
    }

    @Given("Артур спокоен и находится вне комнаты")
    public void arthurIsCalmAndOutside() {
        arthur = new Arthur();
        Assertions.assertEquals(EmotionalState.CALM, arthur.getEmotionalState());
        Assertions.assertFalse(arthur.isInRoom());
        Assertions.assertEquals("Arthur", arthur.getName());
    }

    @Given("челюсть Артура уже отвисла от 3 невероятных вещей")
    public void jawAlreadyDropped() {
        arthur = new Arthur();
        arthur.seeUnbelievableThing();
        arthur.seeUnbelievableThing();
        arthur.seeUnbelievableThing();
        arthur.becomeShocked();
        arthur.dropJaw();
        Assertions.assertEquals(PhysicalState.JAW_DROPPED, arthur.getPhysicalState());
    }

    @When("Артур входит в комнату следом")
    public void arthurEntersRoom() {
        arthur.becomeNervous();
        arthur.enterRoom();
        if (room != null) {
            room.addPerson(arthur);
        }
    }

    @When("Артур видит, что человек положил ноги на пульт управления")
    public void arthurSeesLegsOnPanel() {
        zaphod.putFeetOnControlPanel();
        room.getControlPanel().putFeetOn();
        Assertions.assertTrue(zaphod.hasFeetOnControlPanel());
        Assertions.assertTrue(room.getControlPanel().hasFeetOnIt());
        arthur.seeUnbelievableThing();
    }

    @When("Артур видит, что человек ковыряет левой рукой в зубах правой головы")
    public void seesPickingTeeth() {
        zaphod.pickTeethWithLeftHand();
        arthur.seeUnbelievableThing();
    }

    @When("Артур замечает, что левая голова человека при этом широко и непринужденно улыбается")
    public void seesLeftHeadSmiling() {
        zaphod.smileWithLeftHead();
        arthur.seeUnbelievableThing();
        if (arthur.getUnbelievableThingsCount() >= 3) {
            arthur.becomeShocked();
            arthur.dropJaw();
        }
    }

    @When("Артур видит еще одну невероятную вещь")
    public void seesOneMoreThing() {
        arthur.seeUnbelievableThing();
    }

    @When("человек встаёт с кресла")
    public void manStandsUp() {
        zaphod.standUp();
        room.getChair().vacate();
    }

    @When("человек уходит из комнаты")
    public void manLeavesRoom() {
        room.removePerson(zaphod);
    }

    @When("Артур пытается занервничать в комнате")
    public void arthurTriesToBecomeNervousInRoom() {
        arthur.becomeNervous(); // should be no-op since isInRoom == true
    }

    @When("Артур пытается уронить челюсть")
    public void arthurTriesToDropJaw() {
        arthur.dropJaw();
    }

    // === Then ===

    @Then("Артур начинает нервничать")
    public void arthurIsNervous() {
        Assertions.assertEquals(EmotionalState.NERVOUS, arthur.getEmotionalState());
    }

    @Then("в комнате находятся {int} человека")
    public void peopleInRoomCount2(int count) {
        Assertions.assertEquals(count, room.getPeopleCount());
        Assertions.assertEquals(count, room.getPeopleInRoom().size());
    }

    @Then("в комнате находятся {int} человек")
    public void peopleInRoomCount0(int count) {
        Assertions.assertEquals(count, room.getPeopleCount());
        Assertions.assertEquals(count, room.getPeopleInRoom().size());
    }

    @Then("количество невероятных вещей возрастает до {int}")
    public void unbelievableThingsCountIncreases(int count) {
        Assertions.assertEquals(count, arthur.getUnbelievableThingsCount());
    }

    @Then("челюсть Артура всё еще находится в нормальном состоянии")
    public void jawIsStillNormal() {
        Assertions.assertEquals(PhysicalState.NORMAL, arthur.getPhysicalState());
    }

    @Then("правая голова всецело этим занята")
    public void rightHeadIsBusy() {
        Assertions.assertEquals(HeadActivity.PICKING_TEETH, zaphod.getRightHead().getActivity());
        Assertions.assertEquals("right", zaphod.getRightHead().getName());
    }

    @Then("эмоциональное состояние Артура переходит в {string}")
    public void emotionalStateTransitions(String expectedState) {
        Assertions.assertEquals(EmotionalState.valueOf(expectedState), arthur.getEmotionalState());
    }

    @Then("его физическое состояние переходит в {string}")
    public void physicalStateTransitions(String expectedState) {
        Assertions.assertEquals(PhysicalState.valueOf(expectedState), arthur.getPhysicalState());
    }

    @Then("его физическое состояние остается {string}")
    public void physicalStateRemains(String expectedState) {
        Assertions.assertEquals(PhysicalState.valueOf(expectedState), arthur.getPhysicalState());
    }

    @Then("попытка посадить в кресло ещё одного человека вызывает ошибку")
    public void occupyOccupiedChairThrows() {
        Arthur anotherPerson = new Arthur();
        Assertions.assertThrows(IllegalStateException.class, () -> room.getChair().occupy(anotherPerson));
    }

    @Then("кресло свободно")
    public void chairIsFree() {
        Assertions.assertFalse(room.getChair().isOccupied());
    }

    @Then("человек больше не развалился")
    public void manIsNotLounging() {
        Assertions.assertFalse(zaphod.isLounging());
        Assertions.assertEquals(HandActivity.IDLE, zaphod.getLeftHand().getActivity());
        Assertions.assertEquals("left", zaphod.getLeftHand().getName());
        Assertions.assertEquals(HandActivity.IDLE, zaphod.getRightHand().getActivity());
        Assertions.assertEquals("right", zaphod.getRightHand().getName());
        Assertions.assertEquals(HeadActivity.IDLE, zaphod.getLeftHead().getActivity());
        Assertions.assertEquals("left", zaphod.getLeftHead().getName());
        Assertions.assertEquals(HeadActivity.IDLE, zaphod.getRightHead().getActivity());
        Assertions.assertFalse(zaphod.hasFeetOnControlPanel());
        Assertions.assertEquals("Zaphod", zaphod.getName());
    }

    @Then("попытка освободить пустое кресло вызывает ошибку")
    public void vacateEmptyChairThrows() {
        Assertions.assertThrows(IllegalStateException.class, () -> room.getChair().vacate());
    }

    @Then("повторный вход Артура в комнату вызывает ошибку")
    public void doubleEntryThrows() {
        Assertions.assertThrows(IllegalStateException.class, () -> arthur.enterRoom());
    }

    @Then("попытка убрать Артура из комнаты вызывает ошибку")
    public void removeAbsentPersonThrows() {
        Assertions.assertThrows(IllegalStateException.class, () -> room.removePerson(arthur));
    }

    @Then("повторное добавление человека в комнату вызывает ошибку")
    public void addDuplicatePersonThrows() {
        Assertions.assertThrows(IllegalStateException.class, () -> room.addPerson(zaphod));
    }
}
