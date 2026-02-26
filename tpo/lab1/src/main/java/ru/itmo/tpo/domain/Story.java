package ru.itmo.tpo.domain;

public class Story {
    private final Arthur arthur;
    private final Zaphod zaphod;
    private final Room room;

    public Story() {
        this.arthur = new Arthur();
        this.zaphod = new Zaphod();
        this.room = new Room();
    }

    public void playFullScene() {
        room.addPerson(zaphod);
        room.getChair().occupy(zaphod);
        
        zaphod.loungeInChair();
        zaphod.putFeetOnControlPanel();
        room.getControlPanel().putFeetOn();
        
        zaphod.pickTeethWithLeftHand();
        
        zaphod.smileWithLeftHead();
        
        arthur.becomeNervous();
        
        arthur.enterRoom();
        room.addPerson(arthur);
        
        arthur.becomeShocked();
        
        arthur.seeUnbelievableThing();
        arthur.seeUnbelievableThing();
        arthur.seeUnbelievableThing();
        
        arthur.dropJaw();
    }

    public Arthur getArthur() {
        return arthur;
    }

    public Zaphod getZaphod() {
        return zaphod;
    }

    public Room getRoom() {
        return room;
    }
}
