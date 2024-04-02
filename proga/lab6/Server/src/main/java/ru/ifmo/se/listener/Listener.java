package ru.ifmo.se.listener;

import ru.ifmo.se.dto.AddRequest;
import ru.ifmo.se.dto.Reply;
import ru.ifmo.se.dto.Request;
import ru.ifmo.se.workers.AddWorker;
import ru.ifmo.se.workers.Worker;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.*;
import java.util.HashMap;
import java.util.Set;

public class Listener {
    private final int port;
    private Selector selector;
    private ServerSocketChannel server;
    private final HashMap<String, Worker> workersMap = new HashMap<>();
    public Listener(int port){
        this.port = port;
    }

    private void initWorkers(){
        workersMap.put("add", new AddWorker());
    }

    private boolean init(){
        initWorkers();
        try{
            selector = Selector.open();
            server = ServerSocketChannel.open();
            server.bind(new InetSocketAddress(port));
            server.configureBlocking(false);
            server.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    private void accept(SelectionKey key) throws IOException {
        var ssc = (ServerSocketChannel) key.channel();
        var sc = ssc.accept();
        ByteBuffer buf = ByteBuffer.allocate(1024);
        sc.configureBlocking(false);
        var newKey = sc.register(key.selector(), SelectionKey.OP_READ);
        newKey.attach(buf);
        System.out.println("New client connected from " + sc.getRemoteAddress());
    }

    // получает Worker'а для конкретного запроса. Здесь же происходит десериалзиация
    private Request read(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buf = (ByteBuffer) key.attachment();
        int bytesRead = channel.read(buf);
        if (bytesRead == -1){
            channel.close();
            return null;
        }
        buf.flip();
        int len = buf.getInt();
        if (buf.remaining() == len){
            // объект пришёл полностью и его можно обрабатывать, десериализовывать и т.д.
            ByteArrayInputStream bis = new ByteArrayInputStream(buf.array(), 4, len);

            ObjectInputStream ois = new ObjectInputStream(bis);
            try {
                // считаем реквест. пока не знаем какой именно
                Request req = (Request)ois.readObject();
                // переместить в начало объекта чтобы заново считать
                buf.position(4);
                // получим воркера по имени запроса
                Worker worker = workersMap.get(req.name);
                // десериализуем в конкретный класс (уже не в Request, а в AddRequest, например)
                return worker.deserialize(buf.array());
            } catch (ClassNotFoundException e) {
                return null;
            }
        } else{
            // вернём указатель на начало и дождемся пока не придёт объект полностью
            buf.rewind();
            return null;
        }
    }

    private void write(SelectionKey key){
        //map.get(request.type)
    }

    private void listen() throws IOException {
        while(true) {
            selector.select();
            Set<SelectionKey> keys = selector.selectedKeys();
            for (var iter = keys.iterator(); iter.hasNext(); ) {
                SelectionKey key = iter.next(); iter.remove();
                if (key.isValid()) {
                    if (key.isAcceptable()) {
                        accept(key);
                    }
                    if (key.isReadable()) {
                        Request req = read(key);
                        if (req != null) {
                            Reply reply = workersMap.get(req.name).process(req);
                            key.attach(reply);
                            key.interestOps(SelectionKey.OP_WRITE);
                        }
                    }
                    if (key.isWritable()) {
                        write(key);
                    }
                }
            }
        }
        //selector.close();
    }

    public void start() throws IOException {
        if (init()) {
            listen();
        }
        else {
            System.out.println("Ошибка инициализации сервера!");
        }
    }
}
