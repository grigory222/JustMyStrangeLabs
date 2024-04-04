package ru.ifmo.se.receiver;

import ru.ifmo.se.dto.replies.AddIfMaxReply;
import ru.ifmo.se.dto.replies.AddIfMinReply;
import ru.ifmo.se.dto.replies.AddReply;
import ru.ifmo.se.dto.replies.ShowReply;
import ru.ifmo.se.dto.requests.AddIfMaxRequest;
import ru.ifmo.se.dto.requests.AddIfMinRequest;
import ru.ifmo.se.dto.requests.AddRequest;
import ru.ifmo.se.dto.requests.ShowRequest;
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

    public AddReply add(LabWork labWork) {
        try {
            AddRequest addRequest = new AddRequest(labWork);
            if (!send(socket, serialize(addRequest)))
                return null;
        } catch (IOException e) {
            return null;
        }

        try {
            var bis = new ByteArrayInputStream(Objects.requireNonNull(receive(socket)));
            var ois = new ObjectInputStream(bis);
            return (AddReply) ois.readObject();
        } catch (IOException | NullPointerException | ClassNotFoundException e) {
            return null;
        }
    }

    public AddIfMaxReply addIfMax(LabWork labWork) {
        try {
            AddIfMaxRequest addIfMaxRequest = new AddIfMaxRequest(labWork);
            if (!send(socket, serialize(addIfMaxRequest)))
                return null;
        } catch (IOException e) {
            return null;
        }

        try {
            var bis = new ByteArrayInputStream(Objects.requireNonNull(receive(socket)));
            var ois = new ObjectInputStream(bis);
            return (AddIfMaxReply) ois.readObject();
        } catch (IOException | NullPointerException | ClassNotFoundException e) {
            return null;
        }
    }

    public AddIfMinReply addIfMin(LabWork labWork) {
        try {
            AddIfMinRequest addIfMinRequest = new AddIfMinRequest(labWork);
            if (!send(socket, serialize(addIfMinRequest)))
                return null;
        } catch (IOException e) {
            return null;
        }

        try {
            var bis = new ByteArrayInputStream(Objects.requireNonNull(receive(socket)));
            var ois = new ObjectInputStream(bis);
            return (AddIfMinReply) ois.readObject();
        } catch (IOException | NullPointerException | ClassNotFoundException e) {
            return null;
        }
    }

    public ShowReply show() {
        try {
            ShowRequest showRequest = new ShowRequest();
            if (!send(socket, serialize(showRequest)))
                return null;
        } catch (IOException e) {
            return null;
        }

        try {
            var bis = new ByteArrayInputStream(Objects.requireNonNull(receive(socket)));
            var ois = new ObjectInputStream(bis);
            return (ShowReply) ois.readObject();
        } catch (IOException | NullPointerException | ClassNotFoundException e) {
            return null;
        }
    }
}
