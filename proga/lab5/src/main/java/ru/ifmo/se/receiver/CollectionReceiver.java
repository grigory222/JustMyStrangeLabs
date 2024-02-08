package ru.ifmo.se.receiver;

import ru.ifmo.se.entity.LabWork;

import java.util.LinkedHashSet;

// работает с командами, которые модифицируют коллекцию: add, update, remove_by_id, clear, add_if_max, add_if_min, group_counting_by_creation_date
public class CollectionReceiver extends Receiver{

    public CollectionReceiver(LinkedHashSet<LabWork> collection, CollectionHandler collectionHandler){
        super(collection, collectionHandler);
    }

    public void add(/*аргументы*/){

    }

}
