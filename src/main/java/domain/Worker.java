package domain;

import commons.Sockets;
import commons.TextParser;
import commons.TextReader;
import commons.TextWriter;
import contracts.FileManager;
import contracts.RoomManager;
import org.apache.commons.lang3.StringUtils;

import java.net.Socket;


public class Worker implements Runnable {

    private final Socket socket;
    private final EventsBus eventsBus;
    private final TextWriter writer;
    private final FileManager fileManager;

    private final RoomManager roomManager;

    private Integer roomNumber = 0;

    public Integer getRoomNumber() {
        return roomNumber;
    }

    public Worker(Socket socket, EventsBus eventsBus, RoomManager roomManager, FileManager fileManager) {
        this.socket = socket;
        this.eventsBus = eventsBus;
        writer = new TextWriter(socket);
        this.fileManager = fileManager;
        this.roomManager = roomManager;
    }

    @Override
    public void run() {
        publish(ServerEventType.INTERNAL, WorkerEventType.MENU.label);
        new TextReader(socket, this::onText, this::onInputClose).read();
    }

    private void onText(String text) {
        if (text.endsWith(WorkerEventType.QUIT.label)) {
            exit(text, WorkerEventType.QUIT.label);
        } else if (text.contains(WorkerEventType.JOIN.label)) {
            joinRoom(text, WorkerEventType.JOIN.label);
        } else if (text.contains(WorkerEventType.UPLOAD.label)) {
            uploadFile(text, WorkerEventType.UPLOAD.label);
        } else if (text.contains(WorkerEventType.DOWNLOAD.label)) {
            downloadFile();
        } else if (text.endsWith(WorkerEventType.HELP.label)) {
            publish(ServerEventType.INTERNAL, WorkerEventType.MENU.label);
        } else {
            publish(ServerEventType.MESSAGE_RECEIVED, text);
        }
    }

    private void onInputClose() {
        eventsBus.publish(ServerEvent.builder()
                .type(ServerEventType.CONNECTION_CLOSED)
                .source(this)
                .build());
    }

    private void publish(ServerEventType msgType, String text) {
        eventsBus.publish(ServerEvent.builder()
                .type(msgType)
                .payload(text)
                .source(this)
                .build());
    }

    public void send(String text) {
        writer.write(text);
    }

    private void exit(String text, String separator) {
        if (roomNumber != 0) {
            String leaveText = TextParser.getFirstPart(text, separator) + " Exited from room: " + roomNumber;
            leaveRoom();
            publish(ServerEventType.MESSAGE_RECEIVED, leaveText);
        } else {
            Sockets.close(socket);
        }
    }

    private void joinRoom(String text, String separator) {
        Integer number = TextParser.parseLastNumber(text, separator);
        if (number != 0) {
            boolean isCreated = roomManager.joinRoom(number, this);
            String user = TextParser.getFirstPart(text, separator);

            if (isCreated) {
                publish(ServerEventType.ROOM_CREATED,
                        String.format("%s Room %s created", user, number));
                roomNumber = number;
            } else {
                roomNumber = number;
                publish(ServerEventType.MESSAGE_RECEIVED, String.format("%s Joined room %s", user, number));
            }
        }
    }

    private void leaveRoom() {
        roomManager.leaveRoom(this);
        roomNumber = 0;
    }

    private void uploadFile(String text, String separator) {
        String uploaded = fileManager.upload(TextParser.getLastPart(text, separator), roomNumber);
        if (StringUtils.isNotEmpty(uploaded)) {
            publish(ServerEventType.MESSAGE_RECEIVED, String.format("A file: %s  has been uploaded", uploaded));
        } else {
            publish(ServerEventType.INTERNAL, WorkerEventType.ERROR.label);
        }
    }

    private void downloadFile() {
        String downloaded = fileManager.download(roomNumber);
        if (StringUtils.isNotEmpty(downloaded)) {
            publish(ServerEventType.INTERNAL, "Downloaded: " + downloaded);
        } else {
            publish(ServerEventType.INTERNAL, WorkerEventType.ERROR.label);
        }
    }

}
