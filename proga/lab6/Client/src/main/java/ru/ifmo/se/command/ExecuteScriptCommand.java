package ru.ifmo.se.command;

import ru.ifmo.se.receiver.Receiver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class ExecuteScriptCommand extends AbstractCommand implements Command{
    private final CollectionReceiver receiver;
    public ExecuteScriptCommand(Receiver collectionReceiver, BufferedReader reader, PrintWriter printer, PrintWriter infoPrinter, String name){
        super(name, reader, printer, infoPrinter);
        this.receiver = collectionReceiver;
    }
    public void execute(String[] args){

        // validate fileName

        if (args.length < 1){
            printer.println("Формат команды: 'execute_script file_name'");
            return;
        }

        String fileName = args[0];
        File file = new File(fileName);
        if (!file.canRead()){
            printer.println("Файл не найден или его невозможно прочитать");
            return;
        }


        try{
            if (!receiver.executeScript(infoPrinter, file)){;
                infoPrinter.println("Рекурсия пропущена");
            };
        } catch (FileNotFoundException e){
            printer.println("Не удалось открыть /dev/null");
        }
        printer.println("Скрипт выполнен");
    }
}