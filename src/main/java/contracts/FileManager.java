package contracts;

public interface FileManager {
    String upload(String pathName, Integer level);
    String download(Integer level);
}
