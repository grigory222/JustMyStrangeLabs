package ru.ifmo.se.tasks;

import ru.ifmo.se.dto.requests.Request;
import ru.ifmo.se.dto.responses.Response;
import ru.ifmo.se.network.Network;
import ru.ifmo.se.workers.Worker;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.ReentrantLock;

public class WriteTask implements Runnable{
    private final Response response;
    private final SelectionKey key;
    private final ReentrantLock lock;

    public WriteTask(Response response, SelectionKey key, ReentrantLock lock){
        this.response = response;
        this.key = key;
        this.lock = lock;
    }

    @Override
    public void run() {
        try {
            //lock.lock();
            Network.write((SocketChannel) key.channel(), response);
            //key.channel().register(key.selector(), SelectionKey.OP_READ);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            //lock.unlock();
        }
    }
}
