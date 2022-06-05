package logger;

import domain.ServerEvent;
import domain.ServerEventType;
import lombok.extern.java.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;

@Log
public class ServerHistoryLogger implements Consumer<ServerEvent> {
    private final Integer capacity = 1024;

    private StringBuffer buffer;
    private final String filePath = System.getProperty("user.home") + File.separator + "ChatHistory.txt";
    private final File file;

    public ServerHistoryLogger() {
        file = new File(filePath);
        prepareFile(file);
        buffer = new StringBuffer(capacity);
    }

    private void prepareFile(File file) {
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            log.info("Error during saving history to file");
        }
    }

    @Override
    public void accept(ServerEvent event) {
        if (event.getType().equals(ServerEventType.MESSAGE_RECEIVED)) {
            saveMsg( getDateTime()+ " New message: " + event.getPayload() + System.lineSeparator(), false);
        }
        else if(event.getType().equals(ServerEventType.CONNECTION_CLOSED)){
             saveMsg(getDateTime()+ " Connection from client closed." + System.lineSeparator(), true);
        }
    }

    private String getDateTime(){
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
    }
    private void saveMsg(String text, boolean force) {
        buffer.append(text);
        if (buffer.capacity() > capacity || force) {
            saveToFile(buffer.toString());
            buffer.delete(0, buffer.length());
            buffer = new StringBuffer(capacity);
        }
    }

    private void saveToFile(String text) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.append(text);
        } catch (IOException ioe) {
            log.info("Error during saving history to file");
        }
    }
}
