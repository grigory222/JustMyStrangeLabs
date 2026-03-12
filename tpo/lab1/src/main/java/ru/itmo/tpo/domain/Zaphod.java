package ru.itmo.tpo.domain;

public class Zaphod implements Person {
    private final Head leftHead;
    private final Head rightHead;
    private final Hand leftHand;
    private final Hand rightHand;
    private boolean isLounging;
    private boolean feetOnControlPanel;

    public Zaphod() {
        this.leftHead = new Head("left");
        this.rightHead = new Head("right");
        this.leftHand = new Hand("left");
        this.rightHand = new Hand("right");
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
        leftHand.pickTeeth(rightHead);
    }

    public void smileWithLeftHead() {
        leftHead.startSmiling();
    }

    public void standUp() {
        this.isLounging = false;
        this.feetOnControlPanel = false;
        leftHead.stopActivity();
        rightHead.stopActivity();
        leftHand.stopActivity();
        rightHand.stopActivity();
    }

    public Head getLeftHead() {
        return leftHead;
    }

    public Head getRightHead() {
        return rightHead;
    }

    public Hand getLeftHand() {
        return leftHand;
    }

    public Hand getRightHand() {
        return rightHand;
    }

    public boolean isLounging() {
        return isLounging;
    }

    public boolean hasFeetOnControlPanel() {
        return feetOnControlPanel;
    }
}
