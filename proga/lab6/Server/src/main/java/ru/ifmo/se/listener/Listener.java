package ru.ifmo.se.listener;

import ru.ifmo.se.collection.CollectionHandler;
import ru.ifmo.se.collection.Receiver;
import ru.ifmo.se.dto.replies.Reply;
import ru.ifmo.se.dto.requests.Request;
import ru.ifmo.se.entity.LabWork;
import ru.ifmo.se.workers.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import static ru.ifmo.se.network.Network.*;

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
        workersMap.put("add_if_max", new AddIfMaxWorker(receiver));
        workersMap.put("add_if_min", new AddIfMinWorker(receiver));
        workersMap.put("show", new ShowWorker(receiver));
        workersMap.put("help", new HelpWorker(receiver));
        workersMap.put("info", new InfoWorker(receiver));
        workersMap.put("group_counting_by_creation_date", new GroupCountingByCreationDateWorker(receiver));
        workersMap.put("print_field_ascending", new PrintFieldAscendingWorker(receiver));
        workersMap.put("print_unique_difficulty", new PrintUniqueDifficultyWorker(receiver));
        workersMap.put("remove_by_id", new RemoveByIdWorker(receiver));
        workersMap.put("update", new UpdateWorker(receiver));
        workersMap.put("clear", new ClearWorker(receiver));
    }

    private boolean init(){
        initReceiver();
        initWorkers();
        try{
            server = ServerSocketChannel.open();
            server.bind(new InetSocketAddress(port));
            server.configureBlocking(false);
            server.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            return false;
        }
        return true;
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
                        Request req = read(key, workersMap);
                        if (req != null) {
                            Reply reply = workersMap.get(req.name).process(req);
                            key.interestOps(SelectionKey.OP_WRITE);
                            key.attach(reply);
                        }
                    }
                    try {
                        if (key.isWritable()) {
                            write(key);
                            ByteBuffer buf = ByteBuffer.allocate(4096);
                            key.interestOps(SelectionKey.OP_READ);
                            key.attach(buf);
                        }
                    }catch (CancelledKeyException | IOException e){
                        key.channel().close();
                        key.cancel();
                    }
                }
            }
        }
    }

    private void save(){
        // CSVHandler.save()...?
        System.out.println("tipa save");
    }

    private void exit() throws IOException {
        selector.close();
        server.close();
        System.exit(0);
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
