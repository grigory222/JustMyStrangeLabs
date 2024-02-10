package ru.ifmo.se.receiver;

import ru.ifmo.se.entity.LabWork;
import ru.ifmo.se.runner.Runner;

import java.io.*;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;

// работает с командами, которые модифицируют коллекцию: add, update, remove_by_id, clear, add_if_max, add_if_min, group_counting_by_creation_date
public class CollectionReceiver extends Receiver{

    public CollectionReceiver(LinkedHashSet<LabWork> collection, CollectionHandler collectionHandler){
        super(collection, collectionHandler);
    }


    public void add(LabWork labWork){
        // устновить автогенерируемые поля

        labWork.setId(collectionHandler.getNewId());
        labWork.setCreationDate(LocalDateTime.now().toLocalDate());

        collectionHandler.add(labWork);
    }

    public LabWork getLabById(int id){
        List<LabWork> result = collection.stream().filter(lab -> lab.getId() == id).toList();
        return result.isEmpty() ? null : result.get(0);
    }

    public void update(int id, LabWork newLab){

        LabWork oldLab = getLabById(id);
        if (oldLab == null)
            throw new NullPointerException();

        newLab.setId(id);
        newLab.setCreationDate(LocalDateTime.now().toLocalDate());

        collectionHandler.delete(oldLab);
        collectionHandler.add(newLab);
    }

    public void removeById(int id) {
        LabWork lab = getLabById(id);
        if (lab == null)
            throw new NullPointerException();
        collectionHandler.delete(lab);
    }

    public void clear(){
        collectionHandler.clear();
    }

    public void executeScript(File file) throws FileNotFoundException {
        PrintWriter scriptPrinter = new PrintWriter("/dev/null");
        BufferedReader scriptReader = new BufferedReader(new FileReader(file));
        Runner scriptRunner = new Runner(scriptPrinter, scriptReader);
        scriptRunner.run(collection);
    }
}
