package domain;

import commons.Sockets;
import contracts.FileManager;
import contracts.RoomManager;
import contracts.ServerWorkers;
import dependencies.*;
import logger.MessagesHistoryLogger;
import logger.ServerEventsLogger;
import logger.ServerHistoryLogger;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

import static java.util.concurrent.Executors.newFixedThreadPool;

@RequiredArgsConstructor
public class ChatServer {

    private static final int DEFAULT_PORT = 8888;
    private static final int THREADS_COUNT = 1024;

    private final ServerWorkers serverWorkers;
    private final EventsBus eventsBus;
    private final ExecutorService executorService;

    private final RoomManager roomManager;
    private final FileManager fileManager;

    private void start(int port) throws IOException {
        eventsBus.addConsumer(new ServerEventsProcessor(serverWorkers));
        try (var serverSocket = new ServerSocket(port)) {
            eventsBus.publish(ServerEvent.builder().type(ServerEventType.SERVER_STARTED).build());
            while (true) {
                var socket = serverSocket.accept();
                eventsBus.publish(ServerEvent.builder().type(ServerEventType.CONNECTION_ACCEPTED).build());
                createWorker(socket);
            }
        }
    }

    private void createWorker(Socket socket) {
        var worker = new Worker(socket, eventsBus, roomManager, fileManager);
        serverWorkers.add(worker);
        executorService.execute(worker);
    }

    public static void main(String[] args) throws IOException {
        var port = Sockets.parsePort(args[0], DEFAULT_PORT);
        var fileManager = new SynchronizedFileWorker(new FileWorker());
        var eventsBus = new EventsBus();
        eventsBus.addConsumer(new ServerEventsLogger());
        eventsBus.addConsumer(new MessagesHistoryLogger());
        eventsBus.addConsumer(new ServerHistoryLogger(fileManager));
        var serviceWorkers = new SynchronizedServiceWorkers(new HashSetServerWorkers());
        var roomManager = new SynchronizedRoomManager(new PrivateRoomManager());
        var server = new ChatServer(serviceWorkers, eventsBus, newFixedThreadPool(THREADS_COUNT), roomManager, fileManager);
        server.start(port);
    }

}
