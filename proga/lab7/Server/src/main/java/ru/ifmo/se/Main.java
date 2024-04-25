package ru.ifmo.se;

import ru.ifmo.se.listener.Listener;
import ru.ifmo.se.util.ConnectionManager;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) throws IOException {

        Connection con = ConnectionManager.open();
        Statement statement = null;
        try {
            statement = con.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS test2 (id SERIAL PRIMARY KEY, name VARCHAR(255));");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        Listener listener = new Listener(5252);
        listener.start("test.csv");
    }
}