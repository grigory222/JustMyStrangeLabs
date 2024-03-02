package ru.ifmo.se.receiver;

import ru.ifmo.se.entity.LabWork;

import java.util.LinkedHashSet;

public abstract class Receiver {
    public CollectionHandler getCollectionHandler() {
        return collectionHandler;
    }

    protected final CollectionHandler collectionHandler;

    public Receiver(LinkedHashSet<LabWork> collection, CollectionHandler collectionHandler){
        this.collectionHandler = collectionHandler;
    }
}
