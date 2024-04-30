package ru.ifmo.se.listener;

import ru.ifmo.se.collection.CollectionHandler;
import ru.ifmo.se.collection.Receiver;
import ru.ifmo.se.consoleReader.ConsoleReader;
import ru.ifmo.se.db.DbManager;
import ru.ifmo.se.dto.requests.Request;
import ru.ifmo.se.entity.LabWork;
import ru.ifmo.se.tasks.ReadTask;
import ru.ifmo.se.workers.*;
import ru.ifmo.se.csv.CsvHandler;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    private final ExecutorService readThreadPool = Executors.newFixedThreadPool(5);
    private final ExecutorService processThreadPool = Executors.newFixedThreadPool(5);
    private final ExecutorService writeThreadPool = Executors.newFixedThreadPool(5);

    public Listener(int port) {
        this.port = port;
    }

    private void initReceiver() {
        db = new DbManager();
        receiver = new Receiver(collectionHandler, db);
    }

    private void initWorkers() {
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
        workersMap.put("auth", new AuthWorker(receiver));
        workersMap.put("reg", new RegisterWorker(receiver));
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
        while (true) {
            selector.select();
            Set<SelectionKey> keys = selector.selectedKeys();
            for (var iter = keys.iterator(); iter.hasNext(); ) {
                SelectionKey key = iter.next();
                iter.remove();
                if (key.isValid()) {
                    if (key.isAcceptable()) {
                        accept(key);
                    }
                    if (key.isReadable()) {
                        readThreadPool.submit(new ReadTask(key, processThreadPool, writeThreadPool, workersMap));
                    }
                }
            }
        }
    }


    public void exit() throws IOException {
        selector.close();
        server.close();
        db.close();
        System.exit(0);
    }

    public ServerSocketChannel choosePort() {
        Scanner scanner = new Scanner(System.in);
        ServerSocketChannel serverSocketChannel = null;

        while (true) {
            System.out.print("Введите номер порта: ");
            try {
                int port = Integer.parseInt(scanner.nextLine());
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
