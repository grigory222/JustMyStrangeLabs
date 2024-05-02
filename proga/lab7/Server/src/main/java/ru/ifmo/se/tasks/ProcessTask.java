package ru.ifmo.se.tasks;

import ru.ifmo.se.dto.requests.Request;
import ru.ifmo.se.dto.responses.Response;
import ru.ifmo.se.network.Network;
import ru.ifmo.se.workers.Worker;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.ReentrantLock;

public class ProcessTask implements Runnable{
    private final Request request;
    private final ExecutorService writeThreadPool;
    private final Map<String, Worker> workersMap;
    private final SelectionKey key;
    private final ReentrantLock lock;
    public ProcessTask(Request request, SelectionKey key, Map<String, Worker> workersMap, ExecutorService writeThreadPool, ReentrantLock lock){
        this.request = request;
        this.writeThreadPool = writeThreadPool;
        this.workersMap = workersMap;
        this.key = key;
        this.lock = lock;
    }

    @Override
    public void run() {
        Response response = workersMap.get(request.name).process(request);
        writeThreadPool.submit(new WriteTask(response, key, lock));
    }
}
