package contracts;

import java.io.File;
import java.util.List;

public interface FileManager {
    String upload(String pathName, Integer level);
    String download(Integer level);
    File prepareFile(String filePath);
    void saveToFile(String text, File file);
    List<String> getHistory(String roomNumber, File file);
}
