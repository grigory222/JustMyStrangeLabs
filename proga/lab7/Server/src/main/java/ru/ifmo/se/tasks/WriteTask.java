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

public class WriteTask implements Runnable{
    private final Response response;
    private final SelectionKey key;

    public WriteTask(Response response, SelectionKey key){
        this.response = response;
        this.key = key;
    }

    @Override
    public void run() {
        while (!key.isWritable()){};
        try {
            Network.write((SocketChannel) key.channel(), response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
