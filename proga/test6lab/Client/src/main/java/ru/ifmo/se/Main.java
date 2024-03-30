package ru.ifmo.se;

import java.io.IOException;
import java.net.Socket;

public class Main {
    public static void main(String[] args) throws IOException {
        byte[] arr = new byte[1000];

        Socket sock = new Socket("127.0.0.1", 5552);
        var os = sock.getOutputStream();
        os.write("Hello server!".getBytes());

        var is = sock.getInputStream();
        is.read(arr);

        var str = new String(arr);
        System.out.println(str);

    }
}