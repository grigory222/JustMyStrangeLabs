package ru.itmo.tpo.domain;

public class ControlPanel {
    private boolean hasFeetOnIt;

    public ControlPanel() {
        this.hasFeetOnIt = false;
    }

    public void putFeetOn() {
        this.hasFeetOnIt = true;
    }

    public boolean hasFeetOnIt() {
        return hasFeetOnIt;
    }
}
