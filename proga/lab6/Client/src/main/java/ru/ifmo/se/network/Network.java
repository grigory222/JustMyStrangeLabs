package ru.ifmo.se.network;

import ru.ifmo.se.dto.replies.Reply;
import ru.ifmo.se.dto.requests.Request;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Objects;

public class Network {
    public static Socket connect(InetAddress inetAddress, int port) throws IOException {
        return new Socket(inetAddress, port);
    }

    public static boolean send(Socket socket, byte[] buf) {
        try {
            var os = socket.getOutputStream();
            // запишем сначала длину буфера
            var byteBuf = ByteBuffer.allocate(Integer.BYTES);
            byteBuf.putInt(buf.length);
            os.write(byteBuf.array());
            // теперь основной буфер с данными
            os.write(buf);
        } catch (IOException e) {
            System.out.println("DEBUG CATCHED IOEXCEPTION");
            return false;
        }
        return true;
    }

    public static byte[] receive(Socket socket) {
        try {
            var is = socket.getInputStream();
            var dis = new DataInputStream(is);
            int len = dis.readInt();
            return dis.readNBytes(len);
        } catch (IOException e) {
            return null;
        }
    }

    public static byte[] serialize(Request request) throws IOException {
        var bos = new ByteArrayOutputStream();
        var oos = new ObjectOutputStream(bos);
        oos.writeObject(request);
        return bos.toByteArray();
    }

    public static Reply sendAndReceive(Socket socket, Request request) {
        try {
            if (!send(socket, serialize(request)))
                return null;
        } catch (IOException e) {
            return null;
        }

        try {
            var bis = new ByteArrayInputStream(Objects.requireNonNull(receive(socket)));
            var ois = new ObjectInputStream(bis);
            return (Reply) ois.readObject();
        } catch (IOException | NullPointerException | ClassNotFoundException e) {
            return null;
        }
    }
}
