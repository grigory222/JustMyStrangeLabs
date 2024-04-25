package ru.ifmo.se.collection;

import lombok.Getter;
import ru.ifmo.se.csv.CsvHandler;
import ru.ifmo.se.entity.LabWork;

import java.time.LocalDate;
import java.util.*;

// "низкоуровневый" класс для crud операций с коллекцией
public class CollectionHandler {

    @Getter
    private LinkedHashSet<LabWork> collection;
    private final Queue<Integer> deletedId = new LinkedList<>();
    private final LocalDate initDate;
    private final CsvHandler csv;

    public CollectionHandler(LinkedHashSet<LabWork> collection, LocalDate initDate, CsvHandler csv){
        this.collection = collection;
        this.initDate = initDate;
        this.csv = csv;
    }

    public void sort(){
        List<LabWork> list = new ArrayList<LabWork>(collection);
        Collections.sort(list);
        collection = new LinkedHashSet<>(list);
        csv.setCollection(collection);
    }

    private void updateIds(){
        int id = 0;
        for (LabWork labWork : collection){
            id += 1;
            labWork.setId(id);
        }
    }

    public void add(LabWork element){
        collection.add(element);
        sort();
    }
    public void delete(LabWork element){
        //deletedId.add(element.getId());
        collection.remove(element);
        updateIds();
    }

    public void clear(){
        collection.clear();
        deletedId.clear();
    }

    public Integer getNewId() {
        return collection.size() + 1;
    }

    public String getInfo(){
        return "Тип коллекции: " + collection.getClass() + "\n" +
                "Дата инициализации: " + initDate + "\n" +
                "Размер: " + collection.size();
    }

}
