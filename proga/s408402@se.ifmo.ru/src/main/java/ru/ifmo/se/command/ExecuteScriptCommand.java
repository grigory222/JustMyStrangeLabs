package ru.ifmo.se.command;

import ru.ifmo.se.controller.Invoker;
import ru.ifmo.se.csv.CsvHandler;
import ru.ifmo.se.receiver.CollectionReceiver;
import ru.ifmo.se.runner.Runner;

import java.io.*;
import java.util.ArrayList;

public class ExecuteScriptCommand extends AbstractCommand implements Command{
    private final CollectionReceiver receiver;
    public ExecuteScriptCommand(CollectionReceiver collectionReceiver, BufferedReader reader, PrintWriter printer, PrintWriter infoPrinter, String name){
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