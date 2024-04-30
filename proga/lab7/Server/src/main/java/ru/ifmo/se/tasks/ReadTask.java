package ru.ifmo.se.tasks;

import ru.ifmo.se.dto.requests.Request;
import ru.ifmo.se.network.Network;
import ru.ifmo.se.workers.Worker;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.util.Map;
import java.util.concurrent.ExecutorService;

public class ReadTask implements Runnable{
    private final SelectionKey key;
    private final ExecutorService processThreadPool;
    private final ExecutorService writeThreadPool;
    private final Map<String, Worker> workersMap;
    public ReadTask(SelectionKey key, ExecutorService processThreadPool, ExecutorService writeThreadPool, Map<String, Worker> workersMap){
        this.key = key;
        this.processThreadPool = processThreadPool;
        this.writeThreadPool = writeThreadPool;
        this.workersMap = workersMap;
    }

    @Override
    public void run() {
        try {
            Request req = Network.read(key);
            processThreadPool.submit(new ProcessTask(req, key, workersMap, writeThreadPool));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
