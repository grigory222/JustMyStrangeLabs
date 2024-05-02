package ru.ifmo.se.db;

import ru.ifmo.se.entity.LabWork;

import java.sql.Connection;
import java.util.LinkedHashSet;

public class DbManager {
    private final ConnectionManager connectionManager = new ConnectionManager();

    public void closePool(){
        connectionManager.closePool();
    }

    public Connection getConnection() {
        return connectionManager.get();
    }

    public void saveCollection() {

    }

    public void loadCollection(LinkedHashSet<LabWork> collection) {

    }
}
