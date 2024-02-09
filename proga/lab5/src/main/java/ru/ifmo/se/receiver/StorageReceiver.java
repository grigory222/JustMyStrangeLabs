package ru.ifmo.se.receiver;

import ru.ifmo.se.csv.CsvHandler;
import ru.ifmo.se.entity.LabWork;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;

// работает с командами, которые работают с файлом: execute_script, save, load(???)
public class StorageReceiver extends Receiver{
    public StorageReceiver(LinkedHashSet<LabWork> collection, CollectionHandler collectionHandler){
        super(collection, collectionHandler);
    }
    public boolean saveCollection(String filePath) {
        try(FileWriter writer = new FileWriter(filePath)) {
            CsvHandler.writeRows(writer, new ArrayList<>(collection));
        } catch (IOException e) {
            return false;
        }
        return true; // успешно записали
    }
}
