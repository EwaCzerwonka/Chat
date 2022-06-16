package logger;

import contracts.FileManager;
import domain.ServerEvent;
import domain.ServerEventType;
import domain.Worker;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Consumer;

@Log
public class ServerHistoryLogger implements Consumer<ServerEvent> {
    private final Integer capacity = 1024;

    private StringBuffer buffer;
    private final String filePath = System.getProperty("user.home") + File.separator + "ChatHistory.txt";
    private final FileManager fileManager;
    private final File historyLog;

    public ServerHistoryLogger(FileManager fileManager) {
        this.fileManager = fileManager;
        historyLog = fileManager.prepareFile(filePath);
        buffer = new StringBuffer(capacity);
    }

    @Override
    public void accept(ServerEvent event) {
        if (event.getType().equals(ServerEventType.MESSAGE_RECEIVED)) {
            saveMsg( event.getSource().getRoomNumber() + ": " + getDateTime()+ ": " + event.getPayload() + System.lineSeparator(), false);
        }
        else if(event.getType().equals(ServerEventType.CONNECTION_CLOSED)){
             saveMsg("", true);
        }
        else if(event.getType().equals(ServerEventType.HISTORY_READ)){
            saveMsg("", true);
            getHistory(event.getSource(), event.getPayload(), historyLog);
        }
    }

    private String getDateTime(){
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
    }
    private void saveMsg(String text, boolean force) {
        if(StringUtils.isNotEmpty(text)) {
            buffer.append(text);
        }
        if (buffer.capacity() > capacity || force) {
            fileManager.saveToFile(buffer.toString(), historyLog);
            buffer.delete(0, buffer.length());
            buffer = new StringBuffer(capacity);
        }
    }
    private void getHistory(Worker worker, String roomNumber, File file){
        List<String> list = fileManager.getHistory(roomNumber, file);
        worker.displayHistory(list);
    }
}
