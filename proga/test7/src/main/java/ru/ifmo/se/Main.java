package ru.ifmo.se;
import java.sql.*;

public class Main {
    public static void main(String[] args) {
        Connection con = ConnectionManager.open();
        Statement statement = null;
        try {
            statement = con.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS test (id SERIAL PRIMARY KEY, name VARCHAR(255));");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}