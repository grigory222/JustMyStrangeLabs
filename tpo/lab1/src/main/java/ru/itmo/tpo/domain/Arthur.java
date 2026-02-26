package ru.itmo.tpo.domain;

public class Arthur implements Person {
    private EmotionalState emotionalState;
    private PhysicalState physicalState;
    private int unbelievableThingsCount;
    private boolean isInRoom;

    public Arthur() {
        this.emotionalState = EmotionalState.CALM;
        this.physicalState = PhysicalState.NORMAL;
        this.unbelievableThingsCount = 0;
        this.isInRoom = false;
    }

    @Override
    public String getName() {
        return "Arthur";
    }

    public void enterRoom() {
        if (isInRoom) {
            throw new IllegalStateException("Arthur is already in the room");
        }
        this.isInRoom = true;
    }

    public void becomeNervous() {
        if (!isInRoom) {
            this.emotionalState = EmotionalState.NERVOUS;
        }
    }

    public void becomeShocked() {
        this.emotionalState = EmotionalState.SHOCKED;
    }

    public void seeUnbelievableThing() {
        this.unbelievableThingsCount++;
    }

    public void dropJaw() {
        if (emotionalState == EmotionalState.SHOCKED) {
            this.physicalState = PhysicalState.JAW_DROPPED;
        }
    }

    public EmotionalState getEmotionalState() {
        return emotionalState;
    }

    public PhysicalState getPhysicalState() {
        return physicalState;
    }

    public int getUnbelievableThingsCount() {
        return unbelievableThingsCount;
    }

    public boolean isInRoom() {
        return isInRoom;
    }

    public void resetState() {
        this.emotionalState = EmotionalState.CALM;
        this.physicalState = PhysicalState.NORMAL;
        this.unbelievableThingsCount = 0;
    }
}
