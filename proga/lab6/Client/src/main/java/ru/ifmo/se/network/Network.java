package ru.ifmo.se.network;

import ru.ifmo.se.dto.Reply;
import ru.ifmo.se.dto.Request;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;

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
        } catch (IOException e){
            return false;
        }
        return true;
    }

    public static byte[] receive(Socket socket){
        try {
            var is = socket.getInputStream();
            var dis = new DataInputStream(is);
            int len = dis.readInt();
            return dis.readNBytes(len);
        } catch (IOException e){
            return null;
        }
    }
}
