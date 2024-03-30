package ru.ifmo.se;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;

@Data
class ClientData{
    public ClientData(String message){
        this.message = message;
        this.buffer = ByteBuffer.allocate(message.length());
        this.buffer.put(message.getBytes());
        this.buffer.flip();
    }
    private String message;
    public ByteBuffer buffer;
}

public class Main {
    public static void main(String[] args) throws IOException {

        Selector selector = Selector.open();
        ServerSocketChannel server = ServerSocketChannel.open();
        server.bind(new InetSocketAddress(5552));
        server.configureBlocking(false);
        server.register(selector, SelectionKey.OP_ACCEPT);
        while(true) {
            selector.select();
            Set<SelectionKey> keys = selector.selectedKeys();
            for (var iter = keys.iterator(); iter.hasNext(); ) {
                SelectionKey key = iter.next(); iter.remove();
                if (key.isValid()) {
                    if (key.isAcceptable()) {
                        var ssc = (ServerSocketChannel) key.channel();
                        var sc = ssc.accept();
                        ClientData clientData = new ClientData("AAAAAAAAAAAAAAAAAaa");
                        sc.configureBlocking(false);
                        var newKey = sc.register(key.selector(), SelectionKey.OP_READ);
                        newKey.attach(clientData);
                        System.out.println("accepted client " + sc.getRemoteAddress());
                    }
                    if (key.isReadable()) {
                        var sc = (SocketChannel) key.channel();
                        ClientData data = (ClientData) key.attachment();
                        sc.read(data.buffer);
                        data.buffer.flip();
                        var newKey = sc.register(key.selector(), SelectionKey.OP_WRITE);
                        newKey.attach(data);
                        System.out.println("readed client data "+ data.buffer);
                    }
                    if (key.isWritable()) {
                        var sc = (SocketChannel) key.channel();
                        var data = (ClientData) key.attachment();
                        sc.write(data.buffer);
                        data.buffer.flip();
                        System.out.println("written data to client " + data.buffer);
                    }
                }
            }
        }
        //selector.close();
    }
}