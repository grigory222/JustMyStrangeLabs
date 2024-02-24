package ru.ifmo.se.receiver;

import ru.ifmo.se.entity.LabWork;
import ru.ifmo.se.runner.Runner;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

// работает с командами, которые модифицируют коллекцию: add, update, remove_by_id, clear, add_if_max, add_if_min, group_counting_by_creation_date
public class CollectionReceiver extends Receiver{
    //private File myFile; // файл
    private final ArrayList<File> historyCall;
    private final String fileName;

    public CollectionReceiver(LinkedHashSet<LabWork> collection, CollectionHandler collectionHandler, ArrayList<File> historyCall, String fileName){
        super(collection, collectionHandler);
        this.historyCall = historyCall;
        this.fileName = fileName;
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

    public boolean executeScript(PrintWriter infoPrinter, File file) throws FileNotFoundException {
        List<File> recursion = historyCall.stream().filter(previous -> previous.getName().equals(file.getName())).toList();
        if (!recursion.isEmpty()){
            infoPrinter.println("Обнаружена рекурсия " + recursion);
            return false;
        }
        PrintWriter scriptPrinter = new PrintWriter("/dev/null");
        BufferedReader scriptReader = new BufferedReader(new FileReader(file));
        Runner scriptRunner = new Runner(infoPrinter, file, scriptPrinter, scriptReader);
        scriptRunner.run(collection, fileName);
        historyCall.remove(historyCall.size() - 1);
        return true;
    }

    public List<LabWork> getGroupCountingByCreationDate() {
        List<LocalDate> allDates = new ArrayList<>();
        List<LabWork> result = new ArrayList<>();

        for (LabWork lab: collection){
            allDates.add(lab.getCreationDate());
        }
        for (LocalDate date : allDates)
            result = Stream.concat(result.stream(), collection.stream().filter(lab -> lab.getCreationDate() == date)).toList();
        return result;
    }
}
