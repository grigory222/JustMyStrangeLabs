package ru.ifmo.se.collection;

import lombok.Getter;
import ru.ifmo.se.db.DbManager;
import ru.ifmo.se.entity.LabWork;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

// "низкоуровневый" класс для crud операций с коллекцией
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
            con = db.getConnection();
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
                var author = element.getAuthor();
                statementAuthor.setString(1, author.getAuthorName());
                statementAuthor.setTimestamp(2, new Timestamp(author.getBirthday().getTime()));
                statementAuthor.setInt(3, author.getHeight());
                statementAuthor.setDouble(4, author.getWeight());
                statementAuthor.setObject(5, author.getHairColor().toString(), java.sql.Types.OTHER);

                statementAuthor.executeUpdate();
                var rs = statementAuthor.getGeneratedKeys();
                if (rs.next()) {
                    element.getAuthor().setId(rs.getInt(1));  // получить сгенерированный id из БД и записать в Author
                } else{
                    throw new RuntimeException("Не удалось вставить автора в БД");
                }
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
        }
    }

    public void addToMemory(LabWork element){
        collection.add(element);
    }

    public boolean update(LabWork lab) {
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
        try (var con = db.getConnection();
             var stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, lab.getOwnerId());
            stmt.setString(2, lab.getName());
            stmt.setInt(3, lab.getCoordinates().getX());
            stmt.setDouble(4, lab.getCoordinates().getY());
            stmt.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setInt(6, lab.getMinimalPoint());
            stmt.setLong(7, lab.getTunedInWorks());
            stmt.setObject(8, lab.getDifficulty().toString(), java.sql.Types.OTHER);
            stmt.setInt(9, lab.getAuthor().getId());
            stmt.setInt(10, lab.getId());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Ошибка при обновлении элемента коллекции " + e.getMessage());
            return false;
        }
    }

    public boolean delete(int labId) {
        String sql = "DELETE FROM labworks WHERE id = ?;";
        try (var con = db.getConnection();
             var stmt = con.prepareStatement(sql)){
            stmt.setInt(1, labId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public void deleteFromMemory(LabWork element){
        collection.remove(element);
        updateIdsToMemory();
    }

    public boolean clear(int ownerId) {
        String sql = "DELETE FROM labworks WHERE owner_id = ?;";
        try (var con = db.getConnection();
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
