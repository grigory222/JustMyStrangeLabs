package ru.ifmo.se.tasks;

import ru.ifmo.se.dto.requests.Request;
import ru.ifmo.se.network.Network;
import ru.ifmo.se.workers.Worker;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.ReentrantLock;

public class ReadTask implements Runnable{
    private final SelectionKey key;
    private final ExecutorService processThreadPool;
    private final ExecutorService writeThreadPool;
    private final Map<String, Worker> workersMap;
    private final ReentrantLock lock;
    public ReadTask(SelectionKey key, ExecutorService processThreadPool, ExecutorService writeThreadPool, Map<String, Worker> workersMap, ReentrantLock lock){
        this.key = key;
        this.processThreadPool = processThreadPool;
        this.writeThreadPool = writeThreadPool;
        this.workersMap = workersMap;
        this.lock = lock;
    }

    @Override
    public void run() {
        try {
            Request req = Network.read(key);
            var attachment = key.attachment();
            key.channel().register(key.selector(), SelectionKey.OP_READ, attachment);
            processThreadPool.submit(new ProcessTask(req, key, workersMap, writeThreadPool, lock));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
