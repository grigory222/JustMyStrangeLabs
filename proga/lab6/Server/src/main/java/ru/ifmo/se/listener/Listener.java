package ru.ifmo.se.listener;

import ru.ifmo.se.collection.CollectionHandler;
import ru.ifmo.se.collection.Receiver;
import ru.ifmo.se.dto.AddRequest;
import ru.ifmo.se.dto.Reply;
import ru.ifmo.se.dto.Request;
import ru.ifmo.se.entity.LabWork;
import ru.ifmo.se.workers.AddWorker;
import ru.ifmo.se.workers.Worker;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

public class Listener {
    private final int port;
    private Selector selector;
    private ServerSocketChannel server;
    private final LinkedHashSet<LabWork> collection = new LinkedHashSet<>();
    private CollectionHandler collectionHandler;
    private final HashMap<String, Worker> workersMap = new HashMap<>();
    private Receiver receiver;
    public Listener(int port){
        this.port = port;
    }

    private void initReceiver(){
        collectionHandler = new CollectionHandler(collection, LocalDate.now());
        receiver = new Receiver(collectionHandler, new PrintWriter(System.out));
    }

    private void initWorkers(){
        workersMap.put("add", new AddWorker(receiver));
    }

    private boolean init(){
        initReceiver();
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
        ByteBuffer byteBuf = (ByteBuffer) key.attachment();
        int bytesRead = channel.read(byteBuf);
        if (bytesRead == -1){
            channel.close();
            return null;
        }
        byteBuf.flip();
        int len = byteBuf.getInt();
        if (byteBuf.remaining() == len){
            // объект пришёл полностью и его можно обрабатывать, десериализовывать и т.д.
            ByteArrayInputStream bis = new ByteArrayInputStream(byteBuf.array(), 4, len);

            ObjectInputStream ois = new ObjectInputStream(bis);
            try {
                // считаем реквест. пока не знаем какой именно
                Request req = (Request)ois.readObject();
                // скопируем буфер без его длины
                byte[] buf = new byte[len];
                byteBuf.get(buf, 0, len);
                // получим воркера по имени запроса
                Worker worker = workersMap.get(req.name);
                // десериализуем в конкретный класс (уже не в Request, а в AddRequest, например)
                return worker.deserialize(buf);
            } catch (ClassNotFoundException e) {
                return null;
            }
        } else{
            // вернём указатель на начало и дождемся пока не придёт объект полностью
            byteBuf.rewind();
            return null;
        }
    }

    private void write(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        Reply reply = (Reply) key.attachment();
        ByteBuffer buffer = Worker.serialize(reply);

        while (buffer.hasRemaining()) {
            int bytesWritten = channel.write(buffer);

            if (bytesWritten == -1) {
                // Если соединение было закрыто
                channel.close();
                key.cancel();
                return;
            }
        }
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
