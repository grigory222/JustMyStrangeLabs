package ru.ifmo.se.receiver;

import ru.ifmo.se.entity.LabWork;

import java.io.File;
import java.util.LinkedHashSet;

// работает с командами, которые работают с файлом: execute_script, save, load(???)
public class StorageReceiver extends Receiver{
    public StorageReceiver(LinkedHashSet<LabWork> collection, CollectionHandler collectionHandler, File file){
        super(collection, collectionHandler);
    }
}
