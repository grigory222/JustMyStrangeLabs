package ru.ifmo.se;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;

public class Main {
    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(5252);
        var sock = ss.accept();
        var os = sock.getOutputStream();
        var oos = new ObjectOutputStream(os);
        oos.writeObject(new ExtReply("message", 123));

        sock.close();
        ss.close();
    }
}