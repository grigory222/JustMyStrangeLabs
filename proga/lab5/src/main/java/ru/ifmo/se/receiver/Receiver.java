package ru.ifmo.se.receiver;

import ru.ifmo.se.entity.LabWork;

import java.util.LinkedHashSet;

public abstract class Receiver {
    //protected LinkedHashSet<LabWork> collection;
    protected final CollectionHandler collectionHandler;
    protected LinkedHashSet<LabWork> collection;

    public Receiver(LinkedHashSet<LabWork> collection, CollectionHandler collectionHandler){
        this.collection = collection;
        this.collectionHandler = collectionHandler;
    }

}
