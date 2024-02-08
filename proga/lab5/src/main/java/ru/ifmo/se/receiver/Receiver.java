package ru.ifmo.se.receiver;

import ru.ifmo.se.entity.LabWork;

import java.util.LinkedHashSet;

public abstract class Receiver {
    //protected LinkedHashSet<LabWork> collection;
    private final CollectionHandler collectionHandler;

    public Receiver(CollectionHandler collectionHandler){
        this.collectionHandler = collectionHandler;
    }

}
