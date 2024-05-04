package ru.ifmo.se.collection;

import lombok.Getter;
import ru.ifmo.se.db.DbManager;
import ru.ifmo.se.entity.LabWork;
import ru.ifmo.se.entity.Person;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class CrudCollection {

    @Getter
    private volatile LinkedHashSet<LabWork> collection;
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
        String insertLabSql = "insert into labworks (owner_id, name, coordx, coordy, creationdate, minimalpoint, tuneditworks, difficulty, author) values (?, ?, ?, ?, ?, ?, ?, ?, ?);";
        String insertAuthorSql = "insert into authors (name, birthday, height, weight, haircolor) values (?, ?, ?, ?, ?);";
        Connection con = null;
        PreparedStatement statementLab;
        PreparedStatement statementAuthor;
        try {
            con = db.connectionManager.get();
            statementLab = con.prepareStatement(insertLabSql, Statement.RETURN_GENERATED_KEYS);
            statementAuthor = con.prepareStatement(insertAuthorSql, Statement.RETURN_GENERATED_KEYS);

            con.setAutoCommit(false);

            statementLab.setInt(1, element.getOwnerId());
            statementLab.setString(2, element.getName());
            statementLab.setInt(3, element.getCoordinates().getX());
            statementLab.setDouble(4, element.getCoordinates().getY());
            statementLab.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            statementLab.setInt(6, element.getMinimalPoint());
            statementLab.setLong(7, element.getTunedInWorks());
            statementLab.setObject(8, element.getDifficulty().toString(), java.sql.Types.OTHER);
            statementLab.setNull(9, Types.INTEGER);
            if (element.getAuthor() != null){
                // вставить автора
                int authorId = newAuthor(element.getAuthor());
                if (authorId == 0)
                    return -1;
                element.getAuthor().setId(authorId);  // получить сгенерированный id из БД и записать в Author
                // установить связь
                statementLab.setInt(9, element.getAuthor().getId());
            }

            statementLab.executeUpdate();
            con.commit();

            var rs = statementLab.getGeneratedKeys();
            if (rs.next()) {
                element.setId(rs.getInt(1)); // получить сгенерированный id из БД и записать в Labwork
            } else{
                throw new RuntimeException("Не удалось вставить лабораторную работу в БД");
            }

            return element.getId();
        } catch (Exception e) {
            try{
                assert con != null : "Connection in 'add' method equals null";
                System.out.println("Произошла ошибка при добавлении элемента\n" + e.getMessage());
                con.rollback();
            } catch(Exception ignored){}
            return -1;
        }finally {
            try {
                con.close();
            } catch (SQLException e) {

            }
        }
    }

    public void addToMemory(LabWork element){
        collection.add(element);
    }


    private int newAuthor(Person author) {
        String sql = "insert into authors (name, birthday, height, weight, haircolor) values (?, ?, ?, ?, ?);";
        try (var con = db.connectionManager.get();
             var statementAuthor = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statementAuthor.setString(1, author.getAuthorName());
            statementAuthor.setTimestamp(2, new Timestamp(author.getBirthday().getTime()));
            statementAuthor.setInt(3, author.getHeight());
            statementAuthor.setDouble(4, author.getWeight());
            statementAuthor.setObject(5, author.getHairColor().toString(), java.sql.Types.OTHER);

            statementAuthor.executeUpdate();
            var rs = statementAuthor.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                throw new SQLException();
            }
        } catch (SQLException e) {
            System.out.println("Не удалось добавить автора в БД " + e.getMessage());
            return 0;
        }
    }

    public boolean update(int labId, LabWork newLab) {
        int authorId = 0;
        if (newLab.getAuthor() != null){
            authorId = newAuthor(newLab.getAuthor()); //newLab.getAuthor().getId();
            if (authorId == 0) {
                return false;
            }
        }


        String sql = "UPDATE labworks " +
                "SET owner_id = ?," +
                "    name = ?," +
                "    coordx = ?," +
                "    coordy = ?," +
                "    creationdate = ?," +
                "    minimalpoint = ?," +
                "    tuneditworks = ?," +
                "    difficulty = ?," +
                "    author = ?" +
                "WHERE id = ?;";
        try (var con = db.connectionManager.get();
             var stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, newLab.getOwnerId());
            stmt.setString(2, newLab.getName());
            stmt.setInt(3, newLab.getCoordinates().getX());
            stmt.setDouble(4, newLab.getCoordinates().getY());
            stmt.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setInt(6, newLab.getMinimalPoint());
            stmt.setLong(7, newLab.getTunedInWorks());
            stmt.setObject(8, newLab.getDifficulty().toString(), java.sql.Types.OTHER);
            //stmt.setInt(9, authorId); //newLab.getAuthor().getId()
            if (authorId > 0)
                stmt.setInt(9, authorId);
            else
                stmt.setNull(9, Types.INTEGER);
            stmt.setInt(10, labId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Ошибка при обновлении элемента коллекции " + e.getMessage());
            return false;
        }
    }

    private void deleteAuthor(int authorId){
        String deleteAuthorSql = "DELETE FROM authors WHERE id = ?";
        try (var con = db.connectionManager.get();
             var stmt = con.prepareStatement(deleteAuthorSql)){
            stmt.setInt(1, authorId);
            if (stmt.executeUpdate() <= 0)
                throw new SQLException();
        } catch (SQLException e) {
            System.out.println("Не удалось удалить автора " + authorId);
        }
    }

    public boolean delete(LabWork lab, int labId) {
        int authorId = lab.getAuthor() == null ? 0 : lab.getAuthor().getId();
        if (authorId > 0){
            deleteAuthor(authorId);;
        }

        String sql = "DELETE FROM labworks WHERE id = ?;";
        try (var con = db.connectionManager.get();
             var stmt = con.prepareStatement(sql)){
            stmt.setInt(1, labId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public void deleteFromMemory(LabWork element){
        collection.remove(element);
    }

    public boolean clear(int ownerId) {
        String sql = "DELETE FROM labworks WHERE owner_id = ?;";
        try (var con = db.connectionManager.get();
             var stmt = con.prepareStatement(sql)){
            stmt.setInt(1, ownerId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public void clearFromMemory(int ownerId){
        collection.removeIf(element -> element.getOwnerId() == ownerId);
    }

    public String getInfo(){
        return "Тип коллекции: " + collection.getClass() + "\n" +
                "Дата инициализации: " + initDate + "\n" +
                "Размер: " + collection.size();
    }

}
