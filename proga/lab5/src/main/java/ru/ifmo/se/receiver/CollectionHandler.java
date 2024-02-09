package ru.ifmo.se.receiver;

import ru.ifmo.se.entity.LabWork;

import java.util.*;

// "низкоуровневый" класс для crud операций с коллекцией
public class CollectionHandler {
    private LinkedHashSet<LabWork> collection;
    private Queue<Integer> deletedId = new LinkedList<>();
    public CollectionHandler(LinkedHashSet<LabWork> collection){
        this.collection = collection;
    }

    public void add(LabWork element){
        collection.add(element);
    }

    public Integer getNewId() {
        if (deletedId.isEmpty())
            return collection.size() + 1;
        return deletedId.remove();
    }
}
