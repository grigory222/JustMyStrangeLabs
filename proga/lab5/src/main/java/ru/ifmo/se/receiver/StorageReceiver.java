package ru.ifmo.se.receiver;

import java.io.File;

// работает с командами, которые работают с файлом: execute_script, save, load(???)
public class StorageReceiver extends Receiver{
    public StorageReceiver(CollectionHandler collectionHandler, File file){
        super(collectionHandler);
    }
}
