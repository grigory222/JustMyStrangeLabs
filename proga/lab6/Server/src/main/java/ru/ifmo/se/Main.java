package ru.ifmo.se;

import ru.ifmo.se.listener.Listener;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Listener listener = new Listener(5252);
        listener.start();
    }
}