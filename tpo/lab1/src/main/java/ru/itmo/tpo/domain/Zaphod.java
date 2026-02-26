package ru.itmo.tpo.domain;

public class Zaphod implements Person {
    private final Head leftHead;
    private final Head rightHead;
    private boolean isLounging;
    private boolean feetOnControlPanel;

    public Zaphod() {
        this.leftHead = new Head("left");
        this.rightHead = new Head("right");
        this.isLounging = false;
        this.feetOnControlPanel = false;
    }

    @Override
    public String getName() {
        return "Zaphod";
    }

    public void loungeInChair() {
        this.isLounging = true;
    }

    public void putFeetOnControlPanel() {
        this.feetOnControlPanel = true;
    }

    public void pickTeethWithLeftHand() {
        rightHead.pickTeeth();
    }

    public void smileWithLeftHead() {
        leftHead.startSmiling();
    }

    public void standUp() {
        this.isLounging = false;
        this.feetOnControlPanel = false;
        leftHead.stopActivity();
        rightHead.stopActivity();
    }

    public Head getLeftHead() {
        return leftHead;
    }

    public Head getRightHead() {
        return rightHead;
    }

    public boolean isLounging() {
        return isLounging;
    }

    public boolean hasFeetOnControlPanel() {
        return feetOnControlPanel;
    }
}
