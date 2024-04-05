package ru.ifmo.se.collection;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import ru.ifmo.se.csv.CsvHandler;
import ru.ifmo.se.entity.Difficulty;
import ru.ifmo.se.entity.LabWork;
import ru.ifmo.se.entity.Person;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class Receiver {
    private final CollectionHandler collectionHandler;
    private final PrintWriter printWriter;
    public Receiver(CollectionHandler collectionHandler, PrintWriter printWriter){
        this.collectionHandler = collectionHandler;
        this.printWriter = printWriter;
    }
    public boolean addIfMax(LabWork labWork){
        if (collectionHandler.getCollection().isEmpty() || labWork.compareTo(collectionHandler.getCollection().stream().toList().get(collectionHandler.getCollection().size() - 1)) > 0){
            add(labWork);
            return true;
        }
        return false;
    }

    public boolean addIfMin(LabWork labWork){

        if (collectionHandler.getCollection().isEmpty() || labWork.compareTo(collectionHandler.getCollection().iterator().next()) < 0){
            add(labWork);
            return true;
        }
        return false;
    }
    public void add(LabWork labWork){
        // устновить автогенерируемые поля

        labWork.setId(collectionHandler.getNewId());
        labWork.setCreationDate(LocalDateTime.now().toLocalDate());

        collectionHandler.add(labWork);
    }

    public LabWork getLabById(int id){
        List<LabWork> result = collectionHandler.getCollection().stream().filter(lab -> lab.getId() == id).toList();
        return result.isEmpty() ? null : result.get(0);
    }

    public boolean update(int id, LabWork newLab){

        LabWork oldLab = getLabById(id);
        if (oldLab == null)
            return false;

        newLab.setId(id);
        newLab.setCreationDate(LocalDateTime.now().toLocalDate());

        collectionHandler.delete(oldLab);
        collectionHandler.add(newLab);
        return true;
    }

    public boolean removeById(int id) {
        LabWork lab = getLabById(id);
        if (lab == null)
            return false;
        collectionHandler.delete(lab);
        return true;
    }

    public void clear(){
        collectionHandler.clear();
    }

//    public boolean executeScript(PrintWriter infoPrinter, File file) throws FileNotFoundException {
//        List<File> recursion = historyCall.stream().filter(previous -> previous.getName().equals(file.getName())).toList();
//        if (!recursion.isEmpty()){
//            infoPrinter.println("Обнаружена рекурсия " + recursion);
//            return false;
//        }
//        PrintWriter scriptPrinter = new PrintWriter("/dev/null");
//        BufferedReader scriptReader = new BufferedReader(new FileReader(file));
//        Runner scriptRunner = new Runner(infoPrinter, file, scriptPrinter, scriptReader);
//        scriptRunner.run(collectionHandler.getCollection(), fileName);
//        historyCall.remove(historyCall.size() - 1);
//        return true;
//    }

    public List<LabWork> getGroupCountingByCreationDate() {
        List<LocalDate> allDates = new ArrayList<>();
        List<LabWork> result = new ArrayList<>();

        for (LabWork lab: collectionHandler.getCollection()){
            allDates.add(lab.getCreationDate());
        }
        for (LocalDate date : allDates)
            result = Stream.concat(result.stream(), collectionHandler.getCollection().stream().filter(lab -> lab.getCreationDate() == date)).toList();
        return result;
    }

    public String printGroupCountingByCreationDate(){
        List<LabWork> labs = getGroupCountingByCreationDate();
        StringBuilder sb = new StringBuilder();
        for (LabWork lab: labs){
            sb.append("\n=================\n").append(lab);
        }
        return sb.toString();
    }

//    public boolean saveCollection(String filePath) {
//        try(FileWriter writer = new FileWriter(filePath)) {
//            CsvHandler.writeRows(writer, new ArrayList<>(collectionHandler.getCollection()));
//        } catch (IOException e) {
//            return false;
//        } catch (CsvRequiredFieldEmptyException | CsvDataTypeMismatchException e) {
//            throw new RuntimeException(e);
//        }
//        return true; // успешно записали
//    }


    public String help(){
        return """
                    help : вывести справку по доступным командам
                    info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)
                    show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении
                    add {element} : добавить новый элемент в коллекцию
                    update id {element} : обновить значение элемента коллекции, id которого равен заданному
                    remove_by_id id : удалить элемент из коллекции по его id
                    clear : очистить коллекцию
                    execute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.
                    exit : завершить программу (без сохранения в файл)
                    add_if_max {element} : добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции
                    add_if_min {element} : добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции
                    history : вывести последние 14 команд (без их аргументов)
                    group_counting_by_creation_date : сгруппировать элементы коллекции по значению поля creationDate, вывести количество элементов в каждой группе
                    print_unique_difficulty : вывести уникальные значения поля difficulty всех элементов в коллекции
                    print_field_ascending_author : вывести значения поля author всех элементов в порядке возрастания\
                """;
    }
    public String show(){
        StringBuilder sb = new StringBuilder();
        for (LabWork lab : collectionHandler.getCollection()){
            sb.append("\n===============\n").append(lab);
        }
        return sb.toString();
    }
    public String info(){
        return collectionHandler.getInfo();
    }

    public String printUniqueDifficulty(){
        // вывести уникальные значения поля difficulty всех элементов в коллекции
        List<Difficulty> result = new ArrayList<>();
        for (Difficulty cur : Difficulty.values())
            if (!collectionHandler.getCollection().stream().filter(labWork -> labWork.getDifficulty() == cur).toList().isEmpty()){
                result.add(cur);
            }
        StringBuilder sb = new StringBuilder();
        for (var difficulty : result){
            sb.append(difficulty).append("\n");
        }
        return sb.toString();
    }

    public List<Person> getAuthors(){
        List<Person> result = new ArrayList<>();
        for (LabWork lab : collectionHandler.getCollection())
            if (lab.getAuthor() != null)
                result.add(lab.getAuthor());
        Collections.sort(result);
        return result;
    }

    public String printFieldAscendingAuthors() {
        StringBuilder sb = new StringBuilder();
        for (var man : getAuthors()){
            sb.append("\n============\n").append(man);
        }
        return sb.toString();
    }
}
