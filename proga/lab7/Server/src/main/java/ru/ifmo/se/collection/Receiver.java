package ru.ifmo.se.collection;

import ru.ifmo.se.crypto.CryptoUtils;
import ru.ifmo.se.db.ConnectionManager;
import ru.ifmo.se.entity.Difficulty;
import ru.ifmo.se.entity.LabWork;
import ru.ifmo.se.entity.Person;
import ru.ifmo.se.db.DbManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class Receiver {
    private final CollectionHandler collectionHandler;
    private final DbManager db;

    public Receiver(CollectionHandler collectionHandler, DbManager db){
        this.collectionHandler = collectionHandler;
        this.db = db;
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



    private String getSaltByLogin(String login) throws SQLException {
        //try (PreparedStatement statementSalt = db.createStatement("SELECT salt FROM users WHERE login = ?")) {
        try (var con = db.getConnection();
             var statementSalt = con.prepareStatement("SELECT salt FROM users WHERE login = ?")) {
            // select salt
            statementSalt.setString(1, login);
            ResultSet resultSet = statementSalt.executeQuery();
            if (resultSet.next())
                return resultSet.getString("salt");
        }
        // пользователя с таким логином не существует
        return null;
    }



    public boolean auth(String login, String password) {
        try (var con = db.getConnection();
             var statement = con.prepareStatement("SELECT id FROM users WHERE login = ? AND password_hash = ?")) {
            String salt = getSaltByLogin(login);
            if (salt == null)
                return false;

            statement.setString(1, login);
            statement.setString(2, CryptoUtils.hash(password, salt, CryptoUtils.getPepper()));
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()){
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    // returns:
    //        -1 - user doesn't exist
    //       >=0 - amount of added rows
    public int register(String login, String password) {
        try (var con = db.getConnection();
             var statement = con.prepareStatement("INSERT INTO users (login, password_hash, salt) VALUES (?, ?, ?)")) {
            if (getSaltByLogin(login) != null) {
                return -1;
            }
            var salt = CryptoUtils.generateSalt();
            statement.setString(1, login);
            statement.setString(2, CryptoUtils.hash(password, salt, CryptoUtils.getPepper()));
            statement.setString(3, salt);
            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
