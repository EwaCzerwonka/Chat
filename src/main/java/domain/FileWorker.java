package domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log
@RequiredArgsConstructor
public class FileWorker {

    private String uploadedData;
    private String fileName;
    private Integer level;

    public String upload(String pathName, Integer level){
        Path path = Paths.get(pathName);
        try( BufferedReader reader = Files.newBufferedReader(path)) {
            uploadedData = reader.lines()
                    .collect(Collectors.joining(System.lineSeparator()));
            fileName = pathName.substring(pathName.lastIndexOf(File.separator) +1);
            this.level = level;
        } catch (IOException e) {
            return "";
        }
        return fileName;
    }

    public String download(Integer level){
        String pathName = null;
        if(this.level == level) {
            pathName = System.getProperty("user.home") + File.separator + fileName;
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(pathName))) {
                writer.write(uploadedData);
            } catch (IOException e) {
                return "";
            }
        }
        return pathName;
    }

    public File prepareFile(String filePath) {
        File file = new File(filePath);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            log.info("Error during saving history to file");
        }
        return file;
    }

    public void saveToFile(String text, File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.append(text);
        } catch (IOException ioe) {
            log.info("Error during saving history to file");
        }
    }

    public List<String> getHistory(String roomNumber, File file){
        List<String> list = new ArrayList<>();
        if (file.exists()) {
            try (Stream<String> stream = Files.lines(file.toPath())) {
                list = stream
                        .filter(line -> line.startsWith(roomNumber))
                        .collect(Collectors.toList());
            } catch (IOException e) {
                log.info("Error during reading history file");
            }
        }
        return list;
    }
}
