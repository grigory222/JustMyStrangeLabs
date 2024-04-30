package ru.ifmo.se;

import ru.ifmo.se.listener.Listener;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
//        try {
//            Connection con = ConnectionManager.get();
//            Statement statement = con.createStatement();
//            statement.execute("CREATE TABLE IF NOT EXISTS test2 (id SERIAL PRIMARY KEY, name VARCHAR(255));");
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        } finally {
//            ConnectionManager.closePool();
//        }

        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        Listener listener = new Listener(5252);
        listener.start("test.csv");
    }
}