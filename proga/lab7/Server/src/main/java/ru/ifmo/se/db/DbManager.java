package ru.ifmo.se.db;

import ru.ifmo.se.entity.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.LinkedHashSet;

public class DbManager {
    private final ConnectionManager connectionManager = new ConnectionManager();

    public void closePool(){
        connectionManager.closePool();
    }

    public Connection getConnection() {
        return connectionManager.get();
    }

    public void loadCollection(LinkedHashSet<LabWork> collection) {
        String sql = "select labworks.*, authors.*, authors.id AS author_id, authors.name AS author_name from labworks " +
                     "left join authors on labworks.author = authors.id;";
        try(var con = getConnection();
            var stmt = con.prepareStatement(sql)){
            var rs = stmt.executeQuery();

            while(rs.next()) {
                Person author = null;
                int authorId = rs.getInt("author_id");
                if (authorId > 0)
                    author = new Person(authorId,
                                        rs.getString("author_name"),
                                        rs.getTimestamp("birthday"),
                                        rs.getInt("height"),
                                        rs.getDouble("weight"),
                                        Color.valueOf(rs.getString("haircolor")));
                collection.add(
                        new LabWork(rs.getInt("id"),
                                    rs.getInt("owner_id"),
                                    rs.getString("name"),
                                    new Coordinates(rs.getInt("coordx"), rs.getInt("coordy")),
                                    rs.getTimestamp("creationdate").toLocalDateTime().toLocalDate(),
                                    rs.getInt("minimalpoint"),
                                    rs.getInt("tuneditworks"),
                                    Difficulty.valueOf(rs.getString("difficulty")),
                                    author
                        ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
