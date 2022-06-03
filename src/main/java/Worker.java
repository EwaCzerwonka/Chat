
import commons.Sockets;
import commons.TextReader;
import commons.TextWriter;

import java.net.Socket;


class Worker implements Runnable {

    private final Socket socket;
    private final EventsBus eventsBus;
    private final TextWriter writer;

    private final Manager roomManager;

    private Integer roomNumber = 0;
    public Integer getRoomNumber() {
        return roomNumber;
    }

    Worker(Socket socket, EventsBus eventsBus, Manager roomManager) {
        this.socket = socket;
        this.eventsBus = eventsBus;
        writer = new TextWriter(socket);
        this.roomManager = roomManager;
    }

    @Override
    public void run() {
        new TextReader(socket, this::onText, this::onInputClose).read();
    }

    private void onText(String text) {
        if (text.endsWith(":q")) {
            if (roomNumber != 0) {
                String leaveText = "Exited from room: " + roomNumber;
                leaveRoom();
                publish(ServerEventType.MESSAGE_RECEIVED, leaveText);
            } else {
                onInputClose();
                Sockets.close(socket);
            }
        } else if (text.contains(":room ")) {
            joinRoom(text);
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

    void send(String text) {
        writer.write(text);
    }

    private Integer getRoomNr(String text) {
        Integer number = 0;
        String num = text.substring(text.lastIndexOf(" ") + 1);
        try {
            number = Integer.parseInt(num);
        } catch (NumberFormatException nfe) {
        }
        return number;
    }

    private void joinRoom(String text) {

        Integer number = getRoomNr(text);
        if (number != 0) {
            boolean isCreated = roomManager.joinRoom(number, this);

            if (isCreated) {
                publish(ServerEventType.ROOM_CREATED, String.format("Room %s created", number));
                roomNumber = number;
            } else {
                roomNumber = number;
                publish(ServerEventType.ROOM_JOINED, String.format("Joined room %s", number));//jak tu dodać/pobrać nazwe użytkownika?
            }
        }
    }

    private void leaveRoom() {
        roomManager.leaveRoom(this);
        roomNumber = 0;
    }


}
