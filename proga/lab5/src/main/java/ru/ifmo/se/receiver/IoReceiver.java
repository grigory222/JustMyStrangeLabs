package ru.ifmo.se.receiver;

import org.apache.commons.lang3.builder.Diff;
import ru.ifmo.se.entity.Difficulty;
import ru.ifmo.se.entity.LabWork;
import ru.ifmo.se.entity.Person;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Stream;

// работает с командами ввода/вывода: help, info, show, print_unique_difficulty, print_field_ascending_author
public class IoReceiver extends Receiver{
    private final PrintWriter printer;
    private final BufferedReader reader;

    public IoReceiver(LinkedHashSet<LabWork> collection, CollectionHandler collectionHandler, PrintWriter printWriter, BufferedReader bufferedReader){
        super(collection, collectionHandler);
        this.printer = printWriter;
        this.reader = bufferedReader;
    }

    public void help(){
        printer.println("""
                    help : вывести справку по доступным командам
                    info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)
                    show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении
                    add {element} : добавить новый элемент в коллекцию
                    update id {element} : обновить значение элемента коллекции, id которого равен заданному
                    remove_by_id id : удалить элемент из коллекции по его id
                    clear : очистить коллекцию
                    save : сохранить коллекцию в файл
                    execute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.
                    exit : завершить программу (без сохранения в файл)
                    add_if_max {element} : добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции
                    add_if_min {element} : добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции
                    history : вывести последние 14 команд (без их аргументов)
                    group_counting_by_creation_date : сгруппировать элементы коллекции по значению поля creationDate, вывести количество элементов в каждой группе
                    print_unique_difficulty : вывести уникальные значения поля difficulty всех элементов в коллекции
                    print_field_ascending_author : вывести значения поля author всех элементов в порядке возрастания\
                """);
    }
    public void show(){
        for (LabWork lab : collection){
            printer.println("===============\n" + lab);
        }
    }
    public void info(){
        // вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)
        printer.println(collectionHandler.getInfo());
    }

    public List<Difficulty> printUniqueDifficulty(){
        // вывести уникальные значения поля difficulty всех элементов в коллекции
        List<Difficulty> result = new ArrayList<>();
        for (Difficulty cur : Difficulty.values())
            if (!collection.stream().filter(labWork -> labWork.getDifficulty() == cur).toList().isEmpty()){
                result.add(cur);
            }
        return result;
    }

    public List<Person> getAuthors(){
        List<Person> result = new ArrayList<>();
        for (LabWork lab : collection)
            if (lab.getAuthor() != null)
                result.add(lab.getAuthor());
        Collections.sort(result);
        return result;
    }
}