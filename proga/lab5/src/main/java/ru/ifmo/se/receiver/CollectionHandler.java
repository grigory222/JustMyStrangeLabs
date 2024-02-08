package ru.ifmo.se.receiver;

import ru.ifmo.se.entity.LabWork;

import java.util.Collection;
import java.util.LinkedHashSet;

// "низкоуровневый" класс для crud операций с коллекцией
public class CollectionHandler {
    private LinkedHashSet<LabWork> collection;
    public CollectionHandler(LinkedHashSet<LabWork> collection){
        this.collection = collection;
    }


}
