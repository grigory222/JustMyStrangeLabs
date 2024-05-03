package ru.ifmo.se.collection;

import ru.ifmo.se.crypto.CryptoUtils;
import ru.ifmo.se.entity.Difficulty;
import ru.ifmo.se.entity.LabWork;
import ru.ifmo.se.entity.Person;
import ru.ifmo.se.db.DbManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Stream;

public class Receiver {
    private final CrudCollection crudCollection;
    private final DbManager db;

    public Receiver(CrudCollection crudCollection, DbManager db) {
        this.crudCollection = crudCollection;
        this.db = db;
    }

    public synchronized boolean addIfMax(LabWork labWork, int id) {
        if (crudCollection.getCollection().isEmpty() || labWork.compareTo(crudCollection.getCollection().stream().toList().get(crudCollection.getCollection().size() - 1)) > 0) {
            add(labWork, id);
            return true;
        }
        return false;
    }

    public synchronized boolean addIfMin(LabWork labWork, int id) {
        if (crudCollection.getCollection().isEmpty() || labWork.compareTo(crudCollection.getCollection().iterator().next()) < 0) {
            add(labWork, id);
            return true;
        }
        return false;
    }

    public synchronized boolean add(LabWork labWork, int ownerId) {
        labWork.setOwnerId(ownerId);
        int labId = crudCollection.add(labWork);
        if (labId > 0) { // если получилось добавить в БД
            labWork.setId(labId);
            crudCollection.addToMemory(labWork);    // то добавим в коллекцию в памяти
            return true;
        }
        return false;
    }

    public synchronized LabWork getLabById(int id) {
        List<LabWork> result = crudCollection.getCollection().stream().filter(lab -> lab.getId() == id).toList();
        return result.isEmpty() ? null : result.get(0);
    }

    public synchronized boolean update(int ownerId, int labId, LabWork newLab) {
        LabWork lab = getLabById(labId);
        if (lab.getOwnerId() != ownerId) {
            return false;
        }
        if (crudCollection.update(newLab)) { // если успешно обновили в БД, то обновить и в памяти
            crudCollection.deleteFromMemory(lab);
            crudCollection.addToMemory(newLab);
        }
        return true;
    }

    public synchronized boolean removeById(int ownerId, int labId) {
        LabWork lab = getLabById(labId);
        if (lab == null || lab.getOwnerId() != ownerId) {
            return false;
        }
        if (crudCollection.delete(labId)) {
            crudCollection.deleteFromMemory(lab);
            return true;
        }
        return false;
    }

    public synchronized boolean clear(int ownerId) {
        if (crudCollection.clear(ownerId)) {
            crudCollection.clearFromMemory(ownerId);
            return true;
        }
        return false;
    }

    public synchronized List<LabWork> getGroupCountingByCreationDate() {
        List<LocalDate> allDates = new ArrayList<>();
        List<LabWork> result = new ArrayList<>();

        for (LabWork lab : crudCollection.getCollection()) {
            allDates.add(lab.getCreationDate());
        }
        for (LocalDate date : allDates)
            result = Stream.concat(result.stream(), crudCollection.getCollection().stream().filter(lab -> lab.getCreationDate() == date)).toList();
        return result;
    }

    public synchronized String printGroupCountingByCreationDate() {
        List<LabWork> labs = getGroupCountingByCreationDate();
        StringBuilder sb = new StringBuilder();
        for (LabWork lab : labs) {
            sb.append("\n=================\n").append(lab);
        }
        return sb.toString();
    }


    public String help() {
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

    public synchronized String show() {
        StringBuilder sb = new StringBuilder();
        for (LabWork lab : crudCollection.getCollection()) {
            sb.append("\n===============\n").append(lab);
        }
        return sb.toString();
    }

    public synchronized String info() {
        return crudCollection.getInfo();
    }

    public synchronized String printUniqueDifficulty() {
        // вывести уникальные значения поля difficulty всех элементов в коллекции
        List<Difficulty> result = new ArrayList<>();
        for (Difficulty cur : Difficulty.values())
            if (!crudCollection.getCollection().stream().filter(labWork -> labWork.getDifficulty() == cur).toList().isEmpty()) {
                result.add(cur);
            }
        StringBuilder sb = new StringBuilder();
        for (var difficulty : result) {
            sb.append(difficulty).append("\n");
        }
        return sb.toString();
    }

    public synchronized List<Person> getAuthors() {
        List<Person> result = new ArrayList<>();
        for (LabWork lab : crudCollection.getCollection())
            if (lab.getAuthor() != null)
                result.add(lab.getAuthor());
        Collections.sort(result);
        return result;
    }

    public synchronized String printFieldAscendingAuthors() {
        StringBuilder sb = new StringBuilder();
        for (var man : getAuthors()) {
            sb.append("\n============\n").append(man);
        }
        return sb.toString();
    }


    private synchronized String getSaltByLogin(String login) throws SQLException {
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


    public synchronized long auth(String login, String password) {
        try (var con = db.getConnection();
             var statement = con.prepareStatement("SELECT id FROM users WHERE login = ? AND password_hash = ?")) {
            String salt = getSaltByLogin(login);
            if (salt == null)
                return -1;

            statement.setString(1, login);
            statement.setString(2, CryptoUtils.hash(password, salt, CryptoUtils.getPepper()));
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getLong("id");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return -1;
    }

    // returns:
    //        -1 - user doesn't exist
    //       >=0 - id
    public synchronized int register(String login, String password) {
        try (var con = db.getConnection();
             var statement = con.prepareStatement("INSERT INTO users (login, password_hash, salt) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            if (getSaltByLogin(login) != null) {
                return -1;
            }
            var salt = CryptoUtils.generateSalt();
            statement.setString(1, login);
            statement.setString(2, CryptoUtils.hash(password, salt, CryptoUtils.getPepper()));
            statement.setString(3, salt);
            statement.executeUpdate();
            var resultSet = statement.getGeneratedKeys();
            if (resultSet.next())
                return resultSet.getInt("id");
            else
                return -1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
