package ru.ifmo.se.receiver;

import ru.ifmo.se.entity.LabWork;

import java.time.LocalDate;
import java.util.*;

// "низкоуровневый" класс для crud операций с коллекцией
public class CollectionHandler {
    private LinkedHashSet<LabWork> collection;
    private final Queue<Integer> deletedId = new LinkedList<>();
    private final LocalDate initDate;
    public CollectionHandler(LinkedHashSet<LabWork> collection, LocalDate initDate){
        this.collection = collection;
        this.initDate = initDate;
    }
    private void sort(){
//        List<LabWork> list = new ArrayList<LabWork>(collection);
//        Collections.sort(list);
//        collection = new LinkedHashSet<>(list);
    }

    public void add(LabWork element){
        collection.add(element);
        sort();
    }
    public void delete(LabWork element){
        deletedId.add(element.getId());
        collection.remove(element);
    }

    public void clear(){
        collection.clear();
        deletedId.clear();
    }

    public Integer getNewId() {
        if (deletedId.isEmpty())
            return collection.size() + 1;
        return deletedId.remove();
    }
    public String getInfo(){
        return "Тип коллекции: " + collection.getClass() + "\n" +
                "Дата инициализации: " + initDate + "\n" +
                "Размер: " + collection.size();
    }
}
