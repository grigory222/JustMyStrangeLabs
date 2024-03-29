package ru.ifmo.se.command;

import ru.ifmo.se.receiver.IoReceiver;
import ru.ifmo.se.receiver.StorageReceiver;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class SaveCommand extends AbstractCommand implements Command{
    private final StorageReceiver receiver;
    private final String fileName;
    public SaveCommand(StorageReceiver receiver, BufferedReader reader, PrintWriter printWriter, PrintWriter infoPrinter, String name, String fileName) {
        super(name, reader, printWriter, infoPrinter);
        this.receiver = receiver;
        this.fileName = fileName;
    }

    public void execute(String[] args){
        if (receiver.saveCollection(fileName)) { // если успешно записали, то завершаем
            printer.println("Ваша коллекция записана в файл");
            return;
        }
        printer.println("Не удалось записать в файл " + fileName);
        printer.println("Введите 'back' для отмены команды");
    }

//    public void execute(String[] args){
//        while (true){
//            printer.println("Введите имя(путь) файла для сохранения: ");
//            try {
//                String fileName = reader.readLine();
//                if (receiver.saveCollection(fileName) || fileName.equals("back")) // если успешно записали, то завершаем
//                    break;
//                printer.println("Не удалось записать в файл " + fileName);
//                printer.println("Введите 'back' для отмены команды");
//            } catch (IOException e) {
//                printer.println("Ошибка чтения!");
//                return;
//            }
//        }
//        printer.println("Ваша коллекция записана в файл");
//    }

}