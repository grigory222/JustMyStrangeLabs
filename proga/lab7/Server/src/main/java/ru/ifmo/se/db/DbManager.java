package ru.ifmo.se.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DbManager {
    private final ConnectionManager connectionManager = new ConnectionManager();

    public PreparedStatement createStatement(String sql) throws SQLException {
        return connectionManager.get().prepareStatement(sql);
    }

    public void close(){
        connectionManager.closePool();
    }
}
