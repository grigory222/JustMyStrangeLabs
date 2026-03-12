package ru.itmo.tpo.domain;

public class Hand {

    private final String name;
    private HandActivity activity;

    public Hand(String name) {
        this.name = name;
        this.activity = HandActivity.IDLE;
    }

    public void pickTeeth(Head head) {
        this.activity = HandActivity.PICKING_TEETH;
        head.setActivity(HeadActivity.PICKING_TEETH);
    }

    public void stopActivity() {
        this.activity = HandActivity.IDLE;
    }

    public String getName() {
        return name;
    }

    public HandActivity getActivity() {
        return activity;
    }
}
