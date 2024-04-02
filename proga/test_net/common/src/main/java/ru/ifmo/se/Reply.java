package ru.ifmo.se;

import java.io.Serializable;

public class Reply implements Serializable {
    String msg;

    public Reply(String message) {
        msg = message;
    }
}
