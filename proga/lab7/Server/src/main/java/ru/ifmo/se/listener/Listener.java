package ru.ifmo.se.listener;

import ru.ifmo.se.collection.CollectionHandler;
import ru.ifmo.se.collection.Receiver;
import ru.ifmo.se.consoleReader.ConsoleReader;
import ru.ifmo.se.db.DbManager;
import ru.ifmo.se.entity.LabWork;
import ru.ifmo.se.network.JwtManager;
import ru.ifmo.se.tasks.ReadTask;
import ru.ifmo.se.workers.*;
import ru.ifmo.se.csv.CsvHandler;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

import static ru.ifmo.se.network.Network.*;

public class Listener {
    private final int port;
    private Selector selector;
    private ServerSocketChannel server;
    private final LinkedHashSet<LabWork> collection = new LinkedHashSet<>();
    private CollectionHandler collectionHandler;
    private final HashMap<String, Worker> workersMap = new HashMap<>();
    private Receiver receiver;
    private DbManager db;
    private JwtManager jwtManager;
    private final ExecutorService readThreadPool = Executors.newFixedThreadPool(5);
    private final ExecutorService processThreadPool = Executors.newFixedThreadPool(5);
    private final ExecutorService writeThreadPool = Executors.newFixedThreadPool(5);
    private final Map<SocketChannel, ReentrantLock> locksMap = new HashMap<>();

    public Listener(int port) {
        this.port = port;
    }

    private void initReceiver() {
        db = new DbManager();
        jwtManager  = new JwtManager();
        receiver = new Receiver(collectionHandler, db);
    }

    private void initWorkers() {
        workersMap.put("add", new AddWorker(receiver, jwtManager));
        workersMap.put("add_if_max", new AddIfMaxWorker(receiver, jwtManager));
        workersMap.put("add_if_min", new AddIfMinWorker(receiver, jwtManager));
        workersMap.put("show", new ShowWorker(receiver, jwtManager));
        workersMap.put("help", new HelpWorker(receiver, jwtManager));
        workersMap.put("info", new InfoWorker(receiver, jwtManager));
        workersMap.put("group_counting_by_creation_date", new GroupCountingByCreationDateWorker(receiver, jwtManager));
        workersMap.put("print_field_ascending", new PrintFieldAscendingWorker(receiver, jwtManager));
        workersMap.put("print_unique_difficulty", new PrintUniqueDifficultyWorker(receiver, jwtManager));
        workersMap.put("remove_by_id", new RemoveByIdWorker(receiver, jwtManager));
        workersMap.put("update", new UpdateWorker(receiver, jwtManager));
        workersMap.put("clear", new ClearWorker(receiver, jwtManager));
        workersMap.put("auth", new AuthWorker(receiver, jwtManager));
        workersMap.put("reg", new RegisterWorker(receiver, jwtManager));
    }

    private boolean init() {
        initReceiver();
        initWorkers();
        try {
            selector = Selector.open();
            server.configureBlocking(false);
            server.register(selector, SelectionKey.OP_ACCEPT);

        } catch (IOException e) {
            return false;
        }
        return true;
    }


    private void listen() throws IOException {
        //Set<SelectionKey> keys = new HashSet<>();

        while (true) {
            //selector.select();
            selector.selectNow();
            Set<SelectionKey> keys = selector.selectedKeys();

            for (var iter = keys.iterator(); iter.hasNext(); ) {
                SelectionKey key = iter.next();
                iter.remove();
                if (key.isValid()) {
                    if (key.isAcceptable()) {
                        var newKey = accept(key);
                        locksMap.put((SocketChannel) newKey.channel(), new ReentrantLock());
                    }
                    if (key.isReadable()) {
                        var attachment = key.attachment();
                        key.channel().register(selector, 0).attach(attachment);
                        readThreadPool.submit(new ReadTask(key, processThreadPool, writeThreadPool, workersMap, locksMap.get(key.channel())));
                    }
                }
            }
        }
    }

    public void exit() throws IOException {
        selector.close();
        server.close();
        db.closePool();
        System.exit(0);
    }

    public ServerSocketChannel choosePort() {
        Scanner scanner = new Scanner(System.in);
        ServerSocketChannel serverSocketChannel = null;

        while (true) {
            System.out.print("Введите номер порта: ");
            try {
                //int port = Integer.parseInt(scanner.nextLine());
                int port = 5252;
                serverSocketChannel = ServerSocketChannel.open();
                serverSocketChannel.socket().bind(new InetSocketAddress(port));
                break; // Если порт доступен, выходим из цикла
            } catch (NumberFormatException e) {
                System.out.println("Неверный формат порта. Пожалуйста, введите целое число.");
            } catch (IOException e) {
                System.out.println("Порт занят. Пожалуйста, выберите другой порт.");
            }
        }

        return serverSocketChannel;
    }


    public void start(String fileName) throws IOException {

        server = choosePort();


        CsvHandler csv = new CsvHandler(new PrintWriter(System.out));
        csv.loadFromCsv(fileName, collection);
        collectionHandler = new CollectionHandler(collection, LocalDate.now(), csv);
        collectionHandler.sort();
        Thread consoleReaderThread = new Thread(new ConsoleReader(csv, this));
        consoleReaderThread.start();

        if (init()) {
            listen();
        } else {
            System.out.println("Ошибка инициализации сервера!");
        }
    }
}
