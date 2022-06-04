package domain;

import commons.Sockets;
import commons.TextParser;
import commons.TextReader;
import commons.TextWriter;
import contracts.Manager;

import java.net.Socket;


public class Worker implements Runnable {

    private final Socket socket;
    private final EventsBus eventsBus;
    private final TextWriter writer;

    private final Manager roomManager;

    private Integer roomNumber = 0;
    private static final String MENU = """
                    Options:
                    :q - exit room/chat
                    :room number - create/join room at this number, e.g. :room 1
                    :help - display this menu
                    """;

    public Integer getRoomNumber() {
        return roomNumber;
    }

    public Worker(Socket socket, EventsBus eventsBus, Manager roomManager) {
        this.socket = socket;
        this.eventsBus = eventsBus;
        writer = new TextWriter(socket);
        this.roomManager = roomManager;
    }

    @Override
    public void run() {
        new TextReader(socket, this::onText, this::onInputClose).read();
        publish(ServerEventType.HELP, "");
    }

    private void onText(String text) {
        if (text.endsWith(":q")) {
           exit();
        } else if (text.contains(":room ")) {
            joinRoom(text);
        } else if (text.endsWith(":help")) {
            publish(ServerEventType.HELP, MENU);
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

    private void exit(){
        if (roomNumber != 0) {
            String leaveText = "Exited from room: " + roomNumber;
            leaveRoom();
            publish(ServerEventType.MESSAGE_RECEIVED, leaveText);
        } else {
            onInputClose();
            Sockets.close(socket);
        }
    }

    private void joinRoom(String text) {

        Integer number = TextParser.parseLastNumber(text);
        if (number != 0) {
            boolean isCreated = roomManager.joinRoom(number, this);

            if (isCreated) {
                publish(ServerEventType.ROOM_CREATED, String.format("Room %s created", number));
                roomNumber = number;
            } else {
                roomNumber = number;
                publish(ServerEventType.MESSAGE_RECEIVED, String.format("Joined room %s", number));
            }
        }
    }

    private void leaveRoom() {
        roomManager.leaveRoom(this);
        roomNumber = 0;
    }


}
