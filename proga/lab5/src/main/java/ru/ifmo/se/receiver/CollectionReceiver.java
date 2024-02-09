package ru.ifmo.se.receiver;

import ru.ifmo.se.entity.LabWork;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;

// работает с командами, которые модифицируют коллекцию: add, update, remove_by_id, clear, add_if_max, add_if_min, group_counting_by_creation_date
public class CollectionReceiver extends Receiver{

    public CollectionReceiver(LinkedHashSet<LabWork> collection, CollectionHandler collectionHandler){
        super(collection, collectionHandler);
    }


    public void add(LabWork labWork){
        // устновить автогенерируемые поля

        labWork.setId(collectionHandler.getNewId());
        labWork.setCreationDate(LocalDateTime.now().toLocalDate());
        //

        collectionHandler.add(labWork);
    }

    public void update(){

    }

}
