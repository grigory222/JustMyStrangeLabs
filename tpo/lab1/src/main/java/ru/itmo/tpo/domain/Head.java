package ru.itmo.tpo.domain;

public class Head {
    private final String name;
    private HeadActivity activity;

    public Head(String name) {
        this.name = name;
        this.activity = HeadActivity.IDLE;
    }

    public void startSmiling() {
        this.activity = HeadActivity.SMILING;
    }

    public void pickTeeth() {
        this.activity = HeadActivity.PICKING_TEETH;
    }

    public void talk() {
        this.activity = HeadActivity.TALKING;
    }

    public void stopActivity() {
        this.activity = HeadActivity.IDLE;
    }

    public String getName() {
        return name;
    }

    public HeadActivity getActivity() {
        return activity;
    }
}
