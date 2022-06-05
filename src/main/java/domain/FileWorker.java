package domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

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
}
