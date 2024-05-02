package ru.ifmo.se.collection;

import lombok.Getter;
import ru.ifmo.se.db.DbManager;
import ru.ifmo.se.entity.LabWork;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

// "низкоуровневый" класс для crud операций с коллекцией
public class CrudCollection {

    @Getter
    private LinkedHashSet<LabWork> collection;
    private final LocalDate initDate;
    private final DbManager db;

    public CrudCollection(LinkedHashSet<LabWork> collection, LocalDate initDate, DbManager db){
        this.collection = collection;
        this.initDate = initDate;
        this.db = db;
    }

//    public void sort(){
//        List<LabWork> list = new ArrayList<LabWork>(collection);
//        Collections.sort(list);
//        collection = new LinkedHashSet<>(list);
//        csv.setCollection(collection);
//    }

    private void updateIdsToMemory(){
        int id = 0;
        for (LabWork labWork : collection){
            id += 1;
            labWork.setId(id);
        }
    }

    public int add(LabWork element){
        String sql = "insert into labworks (owner_id, name, coordx, coordy, creationdate, minimalpoint, tuneditworks, difficulty, author) values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (var con = db.getConnection();
             var statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            statement.setInt(1, element.getOwnerId());
            statement.setString(2, element.getName());
            statement.setInt(3, element.getCoordinates().getX());
            statement.setDouble(4, element.getCoordinates().getY());
            statement.setTime(5, new Timestamp(LocalDateTime.now().toLocalTime().get);
            statement.setTimestamp();
        } catch (SQLException e) {
            return -1;
        }
    }

    public void addToMemory(LabWork element, int labId){
        element.setId(labId);
        collection.add(element);
    }

    public void deleteFromMemory(LabWork element){
        //deletedId.add(element.getId());
        collection.remove(element);
        updateIdsToMemory();
    }

    public void clearFromMemory(){
        collection.clear();
    }

    //public Integer getNewId() {
    //    return collection.size() + 1;
    //}

    public String getInfo(){
        return "Тип коллекции: " + collection.getClass() + "\n" +
                "Дата инициализации: " + initDate + "\n" +
                "Размер: " + collection.size();
    }

}
