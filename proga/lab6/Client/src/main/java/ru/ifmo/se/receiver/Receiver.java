package ru.ifmo.se.receiver;

import ru.ifmo.se.dto.replies.*;
import ru.ifmo.se.dto.requests.*;
import ru.ifmo.se.entity.LabWork;

import java.io.*;
import java.net.Socket;
import java.util.Objects;

import static ru.ifmo.se.network.Network.*;

public class Receiver{
    Socket socket;
    public Receiver(Socket socket){
        this.socket = socket;
    }

}
