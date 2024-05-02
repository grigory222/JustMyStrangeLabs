package ru.ifmo.se.db;

import java.sql.Connection;

public class DbManager {
    private final ConnectionManager connectionManager = new ConnectionManager();

    public void closePool(){
        connectionManager.closePool();
    }

    public Connection getConnection() {
        return connectionManager.get();
    }
}
